package com.vitaliyhtc.googlemaps1.data;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
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

    public void generateMarkers(){
        if(!isInProgress){
            isInProgress = true;
            MarkersGenerationAsyncTask task = new MarkersGenerationAsyncTask();
            task.execute();
        }
    }

    private void generateMarkers1() {
        mMarkerInfoStorage = new MarkerInfoRealmStorageImpl();
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
        double deltaY = computeDistanceBetween(yLatLng, mCenterLatLng);
        double deltaX = computeDistanceBetween(xLatLng, mCenterLatLng);
        mDeltaLat = 1 * mRadiusInMeters / deltaY;
        mDeltaLng = 1 * mRadiusInMeters / deltaX;
    }

    private void startMarkersGeneration() {
        List<MarkerInfo> markers = new ArrayList<>();
        while (mCounter < mTargetMarkersCount) {
            double lat = mCenterLatLng.latitude + randomDoubleWithRange(-mDeltaLat, mDeltaLat);
            double lng = mCenterLatLng.longitude + randomDoubleWithRange(-mDeltaLng, mDeltaLng);
            LatLng latLng = new LatLng(lat, lng);
            if (computeDistanceBetween(latLng, mCenterLatLng) <= mRadiusInMeters) {
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

    private void onMarkersGenerated(){
        isInProgress = false;
        mListener.onMarkersGeneratedSuccessful();
    }


    // Next 3 methods taken from:
    // https://github.com/googlemaps/android-maps-utils/blob/master/library/src/com/google/maps/android/SphericalUtil.java

    /**
     * Returns the distance between two LatLngs, in meters.
     */
    private static double computeDistanceBetween(LatLng from, LatLng to) {
        return computeAngleBetween(from, to) * 6371009.0D;
    }

    /**
     * Returns the angle between two LatLngs, in radians. This is the same as the distance
     * on the unit sphere.
     */
    private static double computeAngleBetween(LatLng from, LatLng to) {
        return distanceRadians(Math.toRadians(from.latitude), Math.toRadians(from.longitude), Math.toRadians(to.latitude), Math.toRadians(to.longitude));
    }

    /**
     * Returns distance on the unit sphere; the arguments are in radians.
     */
    private static double distanceRadians(double lat1, double lng1, double lat2, double lng2) {
        return arcHav(havDistance(lat1, lat2, lng1 - lng2));
    }

    // Next 3 methods taken from:
    // https://github.com/googlemaps/android-maps-utils/blob/master/library/src/com/google/maps/android/MathUtil.java

    /**
     * Computes inverse haversine. Has good numerical stability around 0.
     * arcHav(x) == acos(1 - 2 * x) == 2 * asin(sqrt(x)).
     * The argument must be in [0, 1], and the result is positive.
     */
    private static double arcHav(double x) {
        return 2.0D * Math.asin(Math.sqrt(x));
    }

    /**
     * Returns hav() of distance from (lat1, lng1) to (lat2, lng2) on the unit sphere.
     */
    private static double havDistance(double lat1, double lat2, double dLng) {
        return hav(lat1 - lat2) + hav(dLng) * Math.cos(lat1) * Math.cos(lat2);
    }

    /**
     * Returns haversine(angle-in-radians).
     * hav(x) == (1 - cos(x)) / 2 == sin(x / 2)^2.
     */
    private static double hav(double x) {
        double sinHalf = Math.sin(x * 0.5D);
        return sinHalf * sinHalf;
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
