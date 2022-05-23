package com.kutaygursoy.hastanenerede;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.RankBy;
import com.google.maps.model.LatLng;

import java.io.IOException;

public class NearbySearch {


    String map_key = "YOUR_API_KEY_HERE"; // Api anahtarı buraya girilecek.

    public PlacesSearchResponse run() {
        PlacesSearchResponse request = new PlacesSearchResponse();
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(map_key)
                .build();
        LatLng location = new LatLng(MainActivity.Global.ivar1, MainActivity.Global.ivar2);

        try {
            request = PlacesApi.nearbySearchQuery(context, location)
                    .rankby(RankBy.DISTANCE)
                    .language("tr")
                    .type(PlaceType.HOSPITAL)
                    .await();
        } catch (ApiException | IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            return request;
        }
    }
}