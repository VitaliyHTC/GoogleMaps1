package com.vitaliyhtc.googlemaps1.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.vitaliyhtc.googlemaps1.Config;
import com.vitaliyhtc.googlemaps1.R;
import com.vitaliyhtc.googlemaps1.adapter.MarkerInfoRecyclerViewAdapterImpl;
import com.vitaliyhtc.googlemaps1.adapter.RecyclerViewAdapter;
import com.vitaliyhtc.googlemaps1.data.DataMarkersGenerator;
import com.vitaliyhtc.googlemaps1.data.MarkerInfoAllMarkersDeletedListener;
import com.vitaliyhtc.googlemaps1.data.MarkerInfoRealmStorageImpl;
import com.vitaliyhtc.googlemaps1.data.MarkerInfoStorage;
import com.vitaliyhtc.googlemaps1.model.MarkerInfo;
import com.vitaliyhtc.googlemaps1.presenter.MarkersListPresenter;
import com.vitaliyhtc.googlemaps1.presenter.MarkersListPresenterImpl;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MarkersListFragment extends Fragment implements MarkersListView {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.spn_count)
    Spinner mCountSpinner;
    @BindView(R.id.spn_place)
    Spinner mPlaceSpinner;
    @BindView(R.id.tv_dataGeneratorStatus)
    TextView mStatusTextView;
    private boolean isGenerating;
    private String[] mCountSpinnerData = {
            "Generate 100 items",
            "Generate 1000 items",
            "Generate 3000 items",
            "Generate 10000 items"
    };
    private double mRadiuses[] = {
            5000.0D,
            16000.0D,
            23000.0D,
            50000.0D
    };
    private String[] mPlaceSpinnerData = {
            "Kyiv",
            "Kamianets-Podilskyi",
            "Chernivtsi"
    };
    private int mCountSelected;
    private LatLng mPlaceSelected;
    private Double mRadiusSelected;

    MarkerInfoStorage mMarkerInfoStorage;

    private MarkersListPresenter mMarkersListPresenter;

    private MarkerInfoRecyclerViewAdapterImpl mMarkerInfoRecyclerViewAdapter;

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

        mMarkerInfoStorage = new MarkerInfoRealmStorageImpl();
        mMarkerInfoStorage.initResources();

        setUpRecyclerView();
        initDataGenerator();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        recyclerView.setAdapter(null);
        mMarkerInfoStorage.releaseResources();
        mMarkersListPresenter.onDetachView();
    }


    @OnClick(R.id.iv_action_start_generation)
    void actionStartGeneration() {
        DataMarkersGenerator dataMarkersGenerator = new DataMarkersGenerator(
                mPlaceSelected,
                mRadiusSelected,
                mCountSelected,
                new DataMarkersGenerator.MarkersGeneratedResultListener() {
                    @Override
                    public void onMarkersGeneratedSuccessful() {
                        mStatusTextView.setText("Markers generation done!");
                    }
                }
        );
        mStatusTextView.setText("Markers generation started.");
        dataMarkersGenerator.generateMarkers();
    }

    @OnClick(R.id.iv_action_delete_all)
    void actionDeleteAll() {
        mMarkerInfoStorage.deleteAllMarkers(new MarkerInfoAllMarkersDeletedListener() {
            @Override
            public void onAllMarkersDeleted() {
                mStatusTextView.setText("All markers deleted!");
            }
        });
        mStatusTextView.setText("All markers deletion");
    }


    private void setUpRecyclerView() {
        mMarkerInfoRecyclerViewAdapter = new MarkerInfoRecyclerViewAdapterImpl(
                new MarkerInfoRecyclerViewAdapterImpl.OnMarkerIconClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        MarkerInfo markerInfo = mMarkerInfoRecyclerViewAdapter.getItem(position);
                        // do some action with markerInfo
                        Toast.makeText(
                                getContext(),
                                "Marker: " + markerInfo.getTitle() + "; clicked.",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mMarkerInfoRecyclerViewAdapter);
        recyclerView.setHasFixedSize(false);
        fillMarkersData(mMarkerInfoRecyclerViewAdapter);
    }

    private void fillMarkersData(RecyclerViewAdapter<MarkerInfo> adapter) {
        mMarkersListPresenter.subscribeForMarkersInfoData(adapter);
    }

    private void initDataGenerator() {
        ArrayAdapter<String> countAdapter =
                new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, mCountSpinnerData);
        countAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> placeAdapter =
                new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, mPlaceSpinnerData);
        placeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mCountSpinner.setAdapter(countAdapter);
        mPlaceSpinner.setAdapter(placeAdapter);

        mCountSpinner.setSelection(0);
        mCountSelected = 100;
        mRadiusSelected = mRadiuses[0];
        mPlaceSpinner.setSelection(0);
        mPlaceSelected = Config.LAT_LNG_KIEV;

        mCountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    mCountSelected = 100;
                    mRadiusSelected = mRadiuses[0];
                } else if (position == 1) {
                    mCountSelected = 1000;
                    mRadiusSelected = mRadiuses[1];
                } else if (position == 2) {
                    mCountSelected = 3000;
                    mRadiusSelected = mRadiuses[2];
                } else if (position == 3) {
                    mCountSelected = 10000;
                    mRadiusSelected = mRadiuses[3];
                } else {
                    mCountSelected = 100;
                    mRadiusSelected = mRadiuses[0];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mCountSpinner.setSelection(0);
                mCountSelected = 100;
                mRadiusSelected = mRadiuses[0];
            }
        });
        mPlaceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    mPlaceSelected = Config.LAT_LNG_KIEV;
                } else if (position == 1) {
                    mPlaceSelected = Config.LAT_LNG_KAM_POD;
                } else if (position == 2) {
                    mPlaceSelected = Config.LAT_LNG_CHERNIVTSI;
                } else {
                    mPlaceSelected = Config.LAT_LNG_KIEV;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mPlaceSelected = Config.LAT_LNG_KIEV;
            }
        });
    }

}

/**
 * TODO: це завдання можна переробити на 2 build flavors, один realm другий dbflow,
 * і реалізувати 2 різні реалізації для бази даних, які залежать від однієї абстракції
 * бачу що за попередні 2 дні тільки 2 коміти є, не дуже добре так робити
 */
