package com.kutaygursoy.hastanenerede;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.model.PlacesSearchResult;


public class OnlineSearch extends FragmentActivity implements OnMapReadyCallback {


    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onlinesearch);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        PlacesSearchResult[] placesSearchResults = new NearbySearch().run().results;


        double lat1 = placesSearchResults[0].geometry.location.lat;
        double lng1 = placesSearchResults[0].geometry.location.lng;

        for (int i = 0; i < placesSearchResults.length; i++) {


            double lat = placesSearchResults[i].geometry.location.lat;
            double lng = placesSearchResults[i].geometry.location.lng;

            String name = placesSearchResults[i].name;

            double mesafe = MainActivity.distance(MainActivity.Global.ivar1, lat, MainActivity.Global.ivar2, lng, 0, 0);
            @SuppressLint("DefaultLocale") String strDouble = String.format("%.3f", mesafe / 1000);


            googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)).title(name).snippet("MESAFE :  " + strDouble + " KM"));


        }

        googleMap.setMinZoomPreference(14.0f);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat1, lng1)));
    }


}