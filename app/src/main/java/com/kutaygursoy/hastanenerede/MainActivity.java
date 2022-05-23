package com.kutaygursoy.hastanenerede;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;


public class MainActivity extends FragmentActivity implements OnMapReadyCallback {
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;


    public static class Global {
        public static double ivar1;
        public static double ivar2;
    }


    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();
    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            if (location != null) {
                currentLocation = location;

                double lat = currentLocation.getLatitude();
                double lon = currentLocation.getLongitude();

                Global.ivar1 = lat;
                Global.ivar2 = lon;

                FloatingActionButton mAddFab;
                mAddFab = findViewById(R.id.fab);

                mAddFab.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, OnlineSearch.class);

                    intent.putExtra("lat", lat);
                    intent.putExtra("lon", lon);
                    startActivity(intent);
                });


                FloatingActionButton btnYakin;
                btnYakin = findViewById(R.id.btn_yakin);
                btnYakin.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, NearestHospital.class);
                    startActivity(intent);
                });

                SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.myMap);
                assert supportMapFragment != null;
                supportMapFragment.getMapAsync(MainActivity.this);
            }
        });
    }

    @SuppressLint({"RtlHardcoded", "ShowToast"})
    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());//canli

        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Buradayim!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
        googleMap.addMarker(markerOptions);

        BufferedInputStream bufferedInputStream = new BufferedInputStream(getResources().openRawResource(R.raw.database));
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(bufferedInputStream));

        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {

                String[] veriler = line.split(";");
                Log.i("kontrol", veriler[2]);

                double l1 = Double.parseDouble(veriler[0]);
                double l2 = Double.parseDouble(veriler[1]);

                double mesafe = distance(Global.ivar1, l1, Global.ivar2, l2, 0, 0);

                @SuppressLint("DefaultLocale") String strDouble = String.format("%.3f", mesafe / 1000);


                LatLng hastane1 = new LatLng(l1, l2);
                MarkerOptions markerOptions2 = new MarkerOptions().position(hastane1).title(veriler[2]).snippet("MESAFE :  " + strDouble + " KM");
                googleMap.addMarker(markerOptions2);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation();
            }
        }
    }

    public static double distance(double lat1, double lat2, double lon1, double lon2, double el1, double el2) {

        final int R = 6371; // dünyanın yariçapı

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000;

        double height = el1 - el2;//1. ve 2. lokasyonun rakimi, aradaki farki 0 kabul ettik

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }


}