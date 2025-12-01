package com.example.addqr.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.core.content.ContextCompat;

public class LocationService {

    private static final String TAG = "LocationService";
    private final Context context;
    private LocationManager locationManager;
    private LocationListener locationListener;

    public LocationService(Context context) {
        this.context = context;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public interface LocationCallback {
        void onLocationResult(Location location);
        void onLocationFailed(String error);
    }

    @SuppressLint("MissingPermission")
    public void requestCurrentLocation(final LocationCallback callback) {
        if (!checkLocationPermissions()) {
            callback.onLocationFailed("Permisos de ubicación no concedidos.");
            return;
        }

        if (locationManager == null || !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            callback.onLocationFailed("GPS no está habilitado.");
            return;
        }

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                stopLocationUpdates();
                callback.onLocationResult(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {
                callback.onLocationFailed("El proveedor de ubicación ha sido deshabilitado.");
            }
        };

        try {
            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null);
            Log.i(TAG, "Solicitando ubicación GPS...");
        } catch (Exception e) {
            Log.e(TAG, "Error al solicitar la ubicación: " + e.getMessage());
            callback.onLocationFailed("Error interno al acceder al GPS.");
        }
    }

    public void stopLocationUpdates() {
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
            locationListener = null;
            Log.i(TAG, "Actualizaciones de ubicación detenidas.");
        }
    }

    private boolean checkLocationPermissions() {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
}