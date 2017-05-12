package com.vitaliyhtc.googlemaps1.data;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.vitaliyhtc.googlemaps1.model.MarkerInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 09/05/17 create DataMarkersGenerator to generate random markers and work with data base
 * when there will be 100, 1000, 10000 markers
 * generated markers are inside of circle with center in (lat, long)
 * you need to be sure that app will work when there would be a lot of data
 */
public class DataMarkersGenerator {

    private LatLng mCenterLatLng;
    private double mRadiusInMeters;
    private int mTargetMarkersCount;

    private MarkersGeneratedResultListener mListener;

    private MarkerInfoStorage mMarkerInfoStorage;

    private double mDeltaLat;
    private double mDeltaLng;
    private int mCounter;

    private boolean isInProgress;

    public DataMarkersGenerator(
            LatLng centerLatLng, double radiusInMeters, int targetMarkersCount,
            MarkersGeneratedResultListener listener
    ) {
        mCenterLatLng = centerLatLng;
        mRadiusInMeters = radiusInMeters;
        mTargetMarkersCount = targetMarkersCount;
        mListener = listener;
    }

    public void generateMarkers() {
        if (!isInProgress) {
            isInProgress = true;
            MarkersGenerationAsyncTask task = new MarkersGenerationAsyncTask();
            task.execute();
        }
    }


    private void generateMarkers1() {
        mMarkerInfoStorage = DataStorageUtils.getMarkerInfoStorageInstance();
        mMarkerInfoStorage.initResources();
        calculateDeltas();
        startMarkersGeneration();
        mMarkerInfoStorage.releaseResources();
    }

    private void calculateDeltas() {
        mDeltaLat = 1;
        mDeltaLng = 1;
        LatLng yLatLng = new LatLng(mCenterLatLng.latitude + mDeltaLat, mCenterLatLng.longitude);
        LatLng xLatLng = new LatLng(mCenterLatLng.latitude, mCenterLatLng.longitude + mDeltaLng);
        double deltaY = SphericalUtil.computeDistanceBetween(yLatLng, mCenterLatLng);
        double deltaX = SphericalUtil.computeDistanceBetween(xLatLng, mCenterLatLng);
        mDeltaLat = 1 * mRadiusInMeters / deltaY;
        mDeltaLng = 1 * mRadiusInMeters / deltaX;
    }

    private void startMarkersGeneration() {
        List<MarkerInfo> markers = new ArrayList<>();
        while (mCounter < mTargetMarkersCount) {
            double lat = mCenterLatLng.latitude + randomDoubleWithRange(-mDeltaLat, mDeltaLat);
            double lng = mCenterLatLng.longitude + randomDoubleWithRange(-mDeltaLng, mDeltaLng);
            LatLng latLng = new LatLng(lat, lng);
            if (SphericalUtil.computeDistanceBetween(latLng, mCenterLatLng) <= mRadiusInMeters) {
                MarkerInfo markerInfo = new MarkerInfo();

                markerInfo.setId(UUID.randomUUID().toString());
                markerInfo.setLatitude(lat);
                markerInfo.setLongitude(lng);
                markerInfo.setTitle("Generated marker #" + mCounter);
                markerInfo.setIconHue(getHue(mCounter));

                markers.add(markerInfo);
                mCounter++;
            }
        }
        mMarkerInfoStorage.saveMarkersListSynchronously(markers);
    }

    private void onMarkersGenerated() {
        isInProgress = false;
        mListener.onMarkersGeneratedSuccessful();
    }


    //other helper methods
    private double randomDoubleWithRange(double min, double max) {
        double range = Math.abs(max - min);
        return (Math.random() * range) + (min <= max ? min : max);
    }

    private float getHue(int index) {
        return MarkerInfo.hues[index % MarkerInfo.huesSize];
    }


    private class MarkersGenerationAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            generateMarkers1();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            onMarkersGenerated();
        }
    }

    public interface MarkersGeneratedResultListener {
        void onMarkersGeneratedSuccessful();
    }
}
