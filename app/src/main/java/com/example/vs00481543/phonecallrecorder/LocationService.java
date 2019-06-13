package com.example.vs00481543.phonecallrecorder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;
import java.util.Locale;

/**
 * Created by VS00481543 on 30-10-2017.
 */

public class LocationService extends Service implements LocationListener {

    public static final int INTERVAL = 5000; // 2 sec
    public static final int FIRST_RUN = 5000; // 2 seconds
    int REQUEST_CODE = 11223344;
    private LocationManager locationManager;
    AlarmManager alarmManager;
    static int i = 0;
    InetAddress inetAddress;
    Geocoder geocoder;
    List<Address> addresses;
    String address = "";
    // GPSTracker class

    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // Declaring a Location Manager..

    public Criteria criteria;
    public String bestProvider;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("MissingPermission")
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "vao roi", Toast.LENGTH_SHORT).show();
        Config.context = this;
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        fn_getlocation();
       /* initBestProvider();
        initLocation();*/
//        getLocation();
//        locationManager.requestLocationUpdates(CurrentLocation.getProvider(), 1000, 1, this);


       /* try {
            addresses = geocoder.getFromLocation(CurrentLocation.getCurrentLocation().getLatitude(), CurrentLocation.getCurrentLocation().getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            address = addresses.get(0).getAddressLine(0);
            Toast.makeText(this, "testPro: " + address, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
        return START_NOT_STICKY;
    }

    /* protected void getLocation() {
 //        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
         criteria = new Criteria();
         bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();

         //You can still do this if you like, you might get lucky:
         if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
             // TODO: Consider calling
             //    ActivityCompat#requestPermissions
             // here to request the missing permissions, and then overriding
             //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
             //                                          int[] grantResults)
             // to handle the case where the user grants the permission. See the documentation
             // for ActivityCompat#requestPermissions for more details.
             return;
         }
         Location location = locationManager.getLastKnownLocation(bestProvider);
         Toast.makeText(this, "locationaa" + location, Toast.LENGTH_SHORT).show();
         if (location != null) {
             Log.e("TAG", "GPS is on");
             latitude = location.getLatitude();
             longitude = location.getLongitude();
             Toast.makeText(this, "latitude:" + latitude + " longitude:" + longitude, Toast.LENGTH_SHORT).show();
             geocoder = new Geocoder(this, Locale.getDefault());
             try {
                 addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                 address = addresses.get(0).getAddressLine(0);
                 Toast.makeText(this, "testPro: " + address, Toast.LENGTH_LONG).show();
             } catch (IOException e) {
                 e.printStackTrace();
             }

         } else {
             //This is what you need:
             locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);
 //            Toast.makeText(this, "location mânget" + locationManager, Toast.LENGTH_SHORT).show();
             if (locationManager != null) {
                 location = locationManager
                         .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                 if (location != null) {
                     latitude = location.getLatitude();
                     longitude = location.getLongitude();
                     Toast.makeText(this, "location: " + longitude, Toast.LENGTH_SHORT).show();
                     geocoder = new Geocoder(this, Locale.getDefault());
                     try {
                         addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                         address = addresses.get(0).getAddressLine(0);
                         Toast.makeText(this, "testPro: " + address, Toast.LENGTH_LONG).show();
                     } catch (IOException e) {
                         e.printStackTrace();
                     }


                 }else {
                     getLocation();
                 }
             }else {
                 getLocation();
             }

         }

     }*/
    private void fn_getlocation() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {

        } else {

            if (isNetworkEnabled) {
                location = null;
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {

                        Log.e("latitude", location.getLatitude() + "");
                        Log.e("longitude", location.getLongitude() + "");

                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        fn_update(location);
                    }
                }

            }


            if (isGPSEnabled) {
                location = null;
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        Log.e("latitude", location.getLatitude() + "");
                        Log.e("longitude", location.getLongitude() + "");
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        fn_update(location);
                    }
                }
            }


        }

    }
    private void fn_update(Location location){
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            address = addresses.get(0).getAddressLine(0);
            Toast.makeText(this, "testPro: " + address, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void initBestProvider() {
        Criteria criteria = new Criteria();
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        CurrentLocation.setProvider(locationManager.getBestProvider(criteria, true));
        Toast.makeText(this, "bestProvider", Toast.LENGTH_SHORT).show();
    }

    private void initLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        CurrentLocation.setCurrentLocation(locationManager.getLastKnownLocation(CurrentLocation.getProvider()));


//        Toast.makeText(this, "initLocation"+CurrentLocation.getCurrentLocation().getLatitude(), Toast.LENGTH_SHORT).show();
    }

    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onLocationChanged(Location location) {
        locationManager.removeUpdates(this);

        //open the map:
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        fn_update(location);
//        Toast.makeText(this, "latitude:" + latitude + " longitude:" + longitude, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    /* @Override
     public void onLocationChanged(Location location) {
         Toast.makeText(this, "locationChange", Toast.LENGTH_SHORT).show();
         i++;
         CurrentLocation.setCurrentLocation(location);


         try {
             addresses = geocoder.getFromLocation(CurrentLocation.getCurrentLocation().getLatitude(), CurrentLocation.getCurrentLocation().getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
         } catch (IOException e) {
             e.printStackTrace();
         }

         address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
         *//*String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();*//*


        Toast.makeText(this, "Provider: " + address, Toast.LENGTH_LONG).show();

        new RequestTask().execute("grope.io");
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }*/
    class RequestTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("lat", Double.toString(CurrentLocation.getCurrentLocation().getLatitude()));
                jsonObject.put("long", Double.toString(CurrentLocation.getCurrentLocation().getLongitude()));

                if (inetAddress == null) {
                    try {
                        inetAddress = InetAddress.getByName(params[0]);
                    } catch (Exception e) {
                        System.out.println("Exp=" + e);
                    }
                }
                if (inetAddress != null) {
                    DatagramSocket sock = new DatagramSocket();

                    byte[] buf = (jsonObject.toString()).getBytes();

                    DatagramPacket pack = new DatagramPacket(buf, jsonObject.toString().length(), inetAddress, 12345);

                    sock.send(pack);

                    sock.close();
                }
            } catch (Exception e) {
                System.out.println("Exp=" + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
}
