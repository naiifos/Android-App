package com.example.janazahapp;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetNearbyPlacesData extends AsyncTask<Object,String,String> {
    String googlePlacesData;
    GoogleMap mMap;
    String url;

    @Override
    protected String doInBackground(Object... objects) {
        mMap=(GoogleMap)objects[0];
        url=(String)objects[1];

        DownloadUrl downloadUrl=new DownloadUrl();
        try {
            googlePlacesData=downloadUrl.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s) {
        JSONArray resultsJsonArray;
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(s);
            resultsJsonArray = jsonObject.getJSONArray("results");
            List<Map<String, Object>> mosques = new ArrayList<>();

            for (int i = 0; i < resultsJsonArray.length(); i++) {
                final JSONObject resultjsonObject = resultsJsonArray.getJSONObject(i);
                if(resultjsonObject.getJSONArray("types").toString().contains("mosque")) {
                    final JSONObject locationJSONObject = resultjsonObject.getJSONObject("geometry").getJSONObject("location");

                    mosques.add(
                      new HashMap<String, Object>() {
                          {
                              {put("vicinity", resultjsonObject.getString("vicinity"));}
                              {put("lat", locationJSONObject.getDouble("lat"));}
                              {put("lng", locationJSONObject.getDouble("lng"));}
                          }
                      }
                    );
                }
            }
            showNearbyPlaces(mosques);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void showNearbyPlaces(List<Map<String, Object>> mosques){
        for (Map<String, Object> mosque : mosques) {
            double lat = (double) mosque.get("lat");
            double lng = (double) mosque.get("lng");
            LatLng latLng = new LatLng(lat, lng);
            MarkerOptions markerOptions= new MarkerOptions();

            markerOptions.position(latLng);
            markerOptions.title((String) mosque.get("vicinity"));
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        }
    }
}
