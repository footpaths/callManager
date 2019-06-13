package com.example.vs00481543.phonecallrecorder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fleps_000 on 07.07.2015.
 */
public class CurrentLocation implements Serializable {
    private static List<UpdateListener> listeners = new ArrayList<>();
    private static Location location;
    private static String provider;

    private static void initBestProvider(LocationManager locationManager) {
        Criteria criteria = new Criteria();
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        provider = locationManager.getBestProvider(criteria, true);
        notifyProviderChanged();
    }

    @SuppressLint("MissingPermission")
    private static void updateLocation() {

        LocationManager lm = (LocationManager) Config.context.getSystemService(Context.LOCATION_SERVICE);
        initBestProvider(lm);


        location = lm.getLastKnownLocation(provider);
        notifyPositionChanged();
    }

    public void addListener(UpdateListener updateListener) {
        listeners.add(updateListener);
    }

    private static void notifyPositionChanged() {
        for(UpdateListener updateListener : listeners) updateListener.onPositionChanged();
    }

    private static void notifyProviderChanged() {
        for(UpdateListener updateListener : listeners) updateListener.onProviderChanged();
    }

    public static void setProvider(String provider) {
        CurrentLocation.provider = provider;
        notifyProviderChanged();
    }

    public static String getProvider() {
        if (provider == null) {
            updateLocation();
        }
        return provider;
    }


    public static Location getCurrentLocation() {
        if (location == null) {
            updateLocation();
        }
        return location;
    }

    public static void setCurrentLocation(Location location) {
        CurrentLocation.location = location;
        notifyPositionChanged();
    }

}
