package com.vitaliyhtc.googlemaps1.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vitaliyhtc.googlemaps1.R;
import com.vitaliyhtc.googlemaps1.adapter.MarkerInfoRecyclerViewAdapter;
import com.vitaliyhtc.googlemaps1.model.MarkerInfo;
import com.vitaliyhtc.googlemaps1.presenter.MarkersListPresenter;
import com.vitaliyhtc.googlemaps1.presenter.MarkersListPresenterImpl;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MarkersListFragment extends Fragment implements MarkersListView {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private MarkersListPresenter mMarkersListPresenter;

    private MarkerInfoRecyclerViewAdapter mMarkerInfoRecyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_markers, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mMarkersListPresenter = new MarkersListPresenterImpl();
        mMarkersListPresenter.onAttachView(MarkersListFragment.this);

        setUpRecyclerView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        recyclerView.setAdapter(null);
        mMarkersListPresenter.onDetachView();
    }

    private void setUpRecyclerView() {
        mMarkerInfoRecyclerViewAdapter = new MarkerInfoRecyclerViewAdapter(
                mMarkersListPresenter.getRealmResultWithMarkerInfo(),
                true,
                new MarkerInfoRecyclerViewAdapter.OnMarkerIconClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        MarkerInfo markerInfo = mMarkerInfoRecyclerViewAdapter.getItem(position);
                        // do some action with markerInfo
                        Toast.makeText(getContext(), "Marker: " + markerInfo.getTitle() + "; clicked.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mMarkerInfoRecyclerViewAdapter);
        recyclerView.setHasFixedSize(false);
    }

}
