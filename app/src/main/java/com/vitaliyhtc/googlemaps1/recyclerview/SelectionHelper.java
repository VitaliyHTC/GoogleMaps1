package com.vitaliyhtc.googlemaps1.recyclerview;

import android.database.Observable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.HashSet;

/**
 * Original code by bejibx.
 * Modified and ViewHolderSingleSelectionWrapper added by VitaliyHTC.
 * <p>
 * See next materials:
 * https://habrahabr.ru/post/258195/
 * https://github.com/bejibx/android-recyclerview-example
 * https://github.com/bejibx/android-recyclerview-example/blob/master/library/src/main/java/com/bejibx/android/recyclerview/selection/SelectionHelper.java
 */
public final class SelectionHelper {
    private final HashSet<Integer> mSelectedItems = new HashSet<>();
    private final HolderClickObservable mHolderClickObservable = new HolderClickObservable();
    private final SelectionObservable mSelectionObservable = new SelectionObservable();

    private final WeakHolderTracker mTracker;

    private boolean mIsSelectable = false;

    public SelectionHelper() {
        this(new WeakHolderTracker());
    }

    @SuppressWarnings("WeakerAccess")
    public SelectionHelper(@NonNull WeakHolderTracker tracker) {
        mTracker = tracker;
    }

    public <H extends RecyclerView.ViewHolder> H wrapSingleSelectable(H holder) {
        new ViewHolderSingleSelectionWrapper(holder);
        return holder;
    }

    public <H extends RecyclerView.ViewHolder> H wrapMultiSelectable(H holder) {
        new ViewHolderMultiSelectionWrapper(holder);
        return holder;
    }

    public <H extends RecyclerView.ViewHolder> H wrapClickable(H holder) {
        new ViewHolderClickWrapper(holder);
        return holder;
    }

    public void bindHolder(RecyclerView.ViewHolder holder, int position) {
        mTracker.bindHolder(holder, position);
    }

    @SuppressWarnings("WeakerAccess")
    public void toggleItemSelected(RecyclerView.ViewHolder holder) {
        setItemSelected(holder, !isItemSelected(holder));
    }

    @SuppressWarnings("WeakerAccess")
    public boolean setItemSelected(RecyclerView.ViewHolder holder, boolean isSelected) {
        int position = holder.getAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
            boolean isAlreadySelected = isItemSelected(position);
            if (isSelected) {
                mSelectedItems.add(position);
            } else {
                mSelectedItems.remove(position);
            }
            if (isSelected ^ isAlreadySelected) {
                mSelectionObservable.notifySelectionChanged(holder, isSelected);
            }
            return true;
        } else {
            return false;
        }
    }

    @SuppressWarnings("WeakerAccess")
    public boolean isItemSelected(RecyclerView.ViewHolder holder) {
        return mSelectedItems.contains(holder.getAdapterPosition());
    }

    @SuppressWarnings("WeakerAccess")
    public boolean isItemSelected(int position) {
        return mSelectedItems.contains(position);
    }


    public int getSelectedItemsCount() {
        return mSelectedItems.size();
    }

    @SuppressWarnings("WeakerAccess")
    public boolean isSelectable() {
        return mIsSelectable;
    }

    @SuppressWarnings("WeakerAccess")
    public void setSelectable(boolean isSelectable) {
        mIsSelectable = isSelectable;
        if (!isSelectable) clearSelection();
        mSelectionObservable.notifySelectableChanged(isSelectable);
    }

    @SuppressWarnings("WeakerAccess")
    public void clearSelection() {
        mSelectedItems.clear();
        for (RecyclerView.ViewHolder holder : mTracker.getTrackedHolders()) {
            if (holder != null) {
                mSelectionObservable.notifySelectionChanged(holder, false);
            }
        }
    }

    public final void registerHolderClickObserver(@NonNull HolderClickObserver observer) {
        mHolderClickObservable.registerObserver(observer);
    }

    public final void registerSelectionObserver(@NonNull SelectionObserver observer) {
        mSelectionObservable.registerObserver(observer);
    }

    @SuppressWarnings("UnusedDeclaration")
    public final void unregisterHolderClickObserver(@NonNull HolderClickObserver observer) {
        mHolderClickObservable.unregisterObserver(observer);
    }

    @SuppressWarnings("UnusedDeclaration")
    public final void unregisterSelectionObserver(@NonNull SelectionObserver observer) {
        mSelectionObservable.unregisterObserver(observer);
    }

    private class HolderClickObservable extends Observable<HolderClickObserver> {
        final void notifyOnHolderClick(RecyclerView.ViewHolder holder) {
            synchronized (mObservers) {
                for (HolderClickObserver observer : mObservers) {
                    observer.onHolderClick(holder);
                }
            }
        }

        final boolean notifyOnHolderLongClick(RecyclerView.ViewHolder holder) {
            boolean isConsumed = false;
            synchronized (mObservers) {
                for (HolderClickObserver observer : mObservers) {
                    isConsumed = isConsumed || observer.onHolderLongClick(holder);
                }
            }
            return isConsumed;
        }
    }

    private class SelectionObservable extends Observable<SelectionObserver> {
        private void notifySelectionChanged(RecyclerView.ViewHolder holder, boolean isSelected) {
            synchronized (mObservers) {
                for (SelectionObserver observer : mObservers) {
                    observer.onSelectedChanged(holder, isSelected);
                }
            }
        }

        private void notifySelectableChanged(boolean isSelectable) {
            synchronized (mObservers) {
                for (SelectionObserver observer : mObservers) {
                    observer.onSelectableChanged(isSelectable);
                }
            }
        }
    }

    private abstract class ViewHolderWrapper implements android.view.View.OnClickListener {
        final WeakReference<RecyclerView.ViewHolder> mWrappedHolderRef;

        ViewHolderWrapper(RecyclerView.ViewHolder holder) {
            mWrappedHolderRef = new WeakReference<>(holder);
        }
    }

    /**
     * for correct work you need to add next to your adapter:
     *
        // variable to store current selected position
        private int mSelectedPosition = RecyclerView.NO_POSITION;
     *
        // Next to save current selected position
        @Override
        public void onHolderClick(RecyclerView.ViewHolder holder) {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION && mClickListener != null) {
                mSelectedPosition = adapterPosition;
                mClickListener.onItemClick(adapterPosition);
            }
        }
     *
        //Next to change background when reuse holder for new item.
        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            holder.bind(mMarkers.get(position));
            int color = Config.RV_SELECTED_OFF_BACKGROUND_COLOR;
            if (position != RecyclerView.NO_POSITION && position == mSelectedPosition) {
                color = Config.RV_SELECTED_ON_BACKGROUND_COLOR;
            }
            holder.setBackgroundColor(color);
            mSelectionHelper.bindHolder(holder, position);
        }
     *
     */
    private class ViewHolderSingleSelectionWrapper extends ViewHolderWrapper
            implements View.OnLongClickListener {
        private ViewHolderSingleSelectionWrapper(RecyclerView.ViewHolder holder) {
            super(holder);
            View itemView = holder.itemView;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            itemView.setLongClickable(true);
            setSelectable(true);
        }

        @Override
        public final void onClick(View v) {
            RecyclerView.ViewHolder holder = mWrappedHolderRef.get();
            if (holder != null && isSelectable()) {
                clearSelection();
                setItemSelected(holder, true);
                mHolderClickObservable.notifyOnHolderClick(mWrappedHolderRef.get());
            }
        }

        @Override
        public final boolean onLongClick(View v) {
            RecyclerView.ViewHolder holder = mWrappedHolderRef.get();
            return holder != null && mHolderClickObservable.notifyOnHolderLongClick(holder);
        }
    }

    private class ViewHolderMultiSelectionWrapper extends ViewHolderWrapper
            implements View.OnLongClickListener {
        private ViewHolderMultiSelectionWrapper(RecyclerView.ViewHolder holder) {
            super(holder);
            View itemView = holder.itemView;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            itemView.setLongClickable(true);
        }

        @Override
        public final void onClick(View v) {
            RecyclerView.ViewHolder holder = mWrappedHolderRef.get();
            if (holder != null) {
                if (isSelectable()) {
                    toggleItemSelected(holder);
                } else {
                    mHolderClickObservable.notifyOnHolderClick(mWrappedHolderRef.get());
                }
            }
        }

        @Override
        public final boolean onLongClick(View v) {
            RecyclerView.ViewHolder holder = mWrappedHolderRef.get();
            if (!isSelectable()) {
                setSelectable(true);
                if (holder != null) {
                    setItemSelected(holder, true);
                }
                return true;
            } else {
                return holder == null || mHolderClickObservable.notifyOnHolderLongClick(holder);
            }
        }
    }

    private class ViewHolderClickWrapper extends ViewHolderWrapper {
        private ViewHolderClickWrapper(RecyclerView.ViewHolder holder) {
            super(holder);
            View itemView = holder.itemView;
            itemView.setOnClickListener(this);
            itemView.setClickable(true);
        }

        @Override
        public final void onClick(View v) {
            RecyclerView.ViewHolder holder = mWrappedHolderRef.get();
            if (holder != null) {
                mHolderClickObservable.notifyOnHolderClick(mWrappedHolderRef.get());
            }
        }
    }
}
