package com.weatherapp.location;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.*;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;


public class FusedLocationHandler {

    public static final int GPS_AND_INTERNET = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;
    public static final int ONLY_GPS = LocationRequest.PRIORITY_HIGH_ACCURACY;
    public static final int LOW_POWER = LocationRequest.PRIORITY_LOW_POWER;
    public static final int NO_POWER = LocationRequest.PRIORITY_NO_POWER;
    private static FusedLocationHandler locationHandler;
    public boolean shouldFallback;
    private Context context;
    private FusedLocationProviderClient client;
    private SettingsClient settings;
    private LocationRequest request;
    private LocationCallback callback;
    private LocationSettingsRequest requestSettings;
    private OnLocationChangeCallback myLocationCallback;
    private OnLocationSettingsSuccessful locationSettingsSuccessful;
    private LocationSettingsRequest.Builder builder = null;
    private long locationRequestUpdateInterval = 10;
    private long locationRequestFastestInterval = 5;
    private int accuracy = GPS_AND_INTERNET;
    private double latitude = 0;

    private double longitude = 0;
    private LocationManager locationManager;

    private boolean gps_enabled = false;
    private boolean network_enabled = false;
    private boolean isDialogShowing = false;


    /**
     * @param context (Activity context)
     */
    private FusedLocationHandler(Context context) {
        this.context = context;
        client = LocationServices.getFusedLocationProviderClient(context);
        settings = LocationServices.getSettingsClient(context);
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public static FusedLocationHandler getInstance(Context context) {
        if (locationHandler == null) {
            locationHandler = new FusedLocationHandler(context);
        }
        return locationHandler;
    }

    public void setOnLocationChangedListener(OnLocationChangeCallback locationCallback) {
        this.myLocationCallback = locationCallback;
    }

    public void setLocationSettingsSuccessfulCallback(OnLocationSettingsSuccessful listener) {
        Log.d("fusedLocationHandler==", "shouldFallback" + shouldFallback);
        this.locationSettingsSuccessful = listener;
    }


    private void createLocationCallback() {
        callback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult != null) {
                    List<Location> locations = locationResult.getLocations();
                    if (locations.size() > 0) {

                        latitude = locations.get(0).getLatitude();
                        longitude = locations.get(0).getLongitude();

                        if (myLocationCallback != null)
                            myLocationCallback.onLocationChanged(latitude, longitude);
                    }
                }
            }
        };
    }

    private void createLocationRequest() {
        request = new LocationRequest();
        request.setInterval(locationRequestUpdateInterval);
        request.setFastestInterval(locationRequestFastestInterval);
        request.setPriority(accuracy);
    }

    private void buildLocationSettingsRequest() {
        if (builder == null) {
            builder = new LocationSettingsRequest.Builder();
            builder.addLocationRequest(request);
            builder.setAlwaysShow(true);
            requestSettings = builder.build();
        }
    }

    /**
     * Should be called before calling
     * startLocationUpdates
     *
     * @param interval        (interval of receiving location updates)
     * @param fastestInterval (fastest interval of receiving location updates)
     * @param accuracy        (Accuracy of the location result)
     */
    public void formFusedLocationRequest(long interval, long fastestInterval, int accuracy) {
        this.locationRequestUpdateInterval = interval;
        this.locationRequestFastestInterval = fastestInterval;
        this.accuracy = accuracy;
        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();
        //checkLocationSettings();
    }

    /**
     * Start location
     * updates, permission check needs
     * to be done prior to making
     * this call
     */
    @SuppressLint("MissingPermission")
    public void startLocationUpdates() {
        client.requestLocationUpdates(request,
                callback, Looper.myLooper());
    }

    public boolean checkLocationSettings(Context context,boolean shouldFallBack) {
        this.context = context;
        this.shouldFallback=shouldFallBack;
        //sendLastKnownLocation();
        // Begin by checking if the device has the necessary location settings.
        // settings.checkLocationSettings(requestSettings).

        if (builder == null) {
            builder = new LocationSettingsRequest.Builder();

            if (request == null)
                request = new LocationRequest();

            builder.addLocationRequest(request);
            builder.setAlwaysShow(true);
            requestSettings = builder.build();
        }

        settings.checkLocationSettings(requestSettings)
                .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        //Log.i("Location", "All location settings are satisfied.");

                        isDialogShowing = true;
                        if (locationSettingsSuccessful != null) {
                            locationSettingsSuccessful.onLocationSettingSuccessful();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();

                        if (!shouldFallback) {
                            switch (statusCode) {
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    try {
                                        // Show the dialog by calling startResolutionForResult(), and check the
                                        // result in onActivityResult().

//                                        if (!isDialogShowing) {
                                            ResolvableApiException rae = (ResolvableApiException) e;
                                            rae.startResolutionForResult(((Activity) FusedLocationHandler.this.context), 2);
                                            isDialogShowing = true;
//                                        } else
//                                            return;
                                    } catch (IntentSender.SendIntentException sie) {
                                        //Log.i("Location", "PendingIntent unable to execute request.");
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    //String errorMessage = "Location settings are inadequate, and cannot be fixed here. Fix in Settings.";
                                    //Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            FusedLocationHandler.this.sendLastKnownLocation();
                        }
                    }
                });
        return isDialogShowing;
    }

    @SuppressLint("MissingPermission")
    public void sendLastKnownLocation() {
        client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
                myLocationCallback.onLocationChanged(latitude, longitude);
            }
        });
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    /**
     * Stop location updates
     */
    public void stopLocationUpdates() {
        client.removeLocationUpdates(callback);
    }

    public interface OnLocationChangeCallback {
        void onLocationChanged(double latitude, double longitude);
    }

    public interface OnLocationSettingsSuccessful {
        void onLocationSettingSuccessful();
    }
}

