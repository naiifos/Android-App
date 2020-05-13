package com.example.janazahapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.file.Paths.get;

public class DataParser {

    private HashMap<String, String> getPlace (JSONObject googlePlaceJson) throws JSONException {
        List<Map<String, Object>> results = (List<Map<String, Object>>) googlePlaceJson.get("results");

        List<String> vicinities = new ArrayList<>();

        for (Map<String, Object> result : results) {
            if(((List<String>) result.get("types")).contains("mosque")) {
                vicinities.add(((String) result.get("vicinity")));
            }
        }

        HashMap<String, String> googlePlacesMap = new HashMap<>();
        String placeName = "-NA-";//so that when we see NA we now it entered the catch
        String vicinity = "-NA-";
        String latitude = "";
        String longitude= "";
        String reference = "";

        try {
            if(!googlePlaceJson.isNull("name")){

                placeName=googlePlaceJson.getString("name");

            }
            if(!googlePlaceJson.isNull("vicinity")){
                vicinity=googlePlaceJson.getString("vicinity");
            }
            latitude=googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");//lat=latitude
            latitude=googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");//lng=longitude

            reference=googlePlaceJson.getString("reference");
            googlePlaceJson.put("place_name", placeName);
            googlePlaceJson.put("vicinity", vicinity);
            googlePlaceJson.put("lat",latitude);
            googlePlaceJson.put("lng",longitude);
            googlePlaceJson.put("reference",reference);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return googlePlacesMap;
    }
    //the following method will use the getPlace method to get all infos about a place
    // and store them into a list of hashmap(because a hashmap is what contains the infos about a place)
    private List<HashMap <String, String>> getPlaces(JSONArray jsonArray){
        int count=jsonArray.length();
        List<HashMap<String,String>> placesList=new ArrayList<>();
        HashMap<String,String> placeMap=null;

        for(int i=0;i<count;i++){
            try {
                placeMap=getPlace((JSONObject) jsonArray.get(i));
                placesList.add(placeMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return placesList;
    }

    public List<HashMap<String, String>> parse(String jsonData){
        //here we parse the data and then we send them to getPlaces method
        // which use getPlace method then
        JSONArray jsonArray=null;
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getPlaces(jsonArray);
    }
}
