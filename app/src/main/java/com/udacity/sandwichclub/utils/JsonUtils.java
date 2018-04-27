package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) {
        final String NAME = "name";
        final String MAIN_NAME = "mainName";
        final String ALSO_KNOWN_AS = "alsoKnownAs";
        final String ORIGIN = "placeOfOrigin";
        final String DESCRIPTION = "description";
        final String IMAGE = "image";
        final String INGREDIENTS = "ingredients";

        Sandwich sandwich = null;

        try {
            // can't use try-with; needs API 19
            JSONObject sandwichJson = new JSONObject(json);
            JSONObject nameJson = sandwichJson.getJSONObject(NAME);

            final String sandwichMainName = nameJson.getString(MAIN_NAME);
            final List<String> nickNamesList = getList(nameJson.getJSONArray(ALSO_KNOWN_AS));
            final String originCountry = sandwichJson.getString(ORIGIN);
            final String sandwichDescription = sandwichJson.getString(DESCRIPTION);
            final String imageUrl = sandwichJson.getString(IMAGE);
            final List<String> sandwichIngredients = getList(sandwichJson.getJSONArray(INGREDIENTS));

            sandwich = new Sandwich(sandwichMainName,
                    nickNamesList,
                    originCountry,
                    sandwichDescription,
                    imageUrl,
                    sandwichIngredients);

        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }

        return sandwich;
    }

    /**
     * Get a List of string values from a JSONArray
     * @param array JSONArray to be used to populate List
     * @return List of String values read from JSONArray
     */
    private static List<String> getList(JSONArray array) {
        List<String> list = new ArrayList<>();

        for(int i=0; i<array.length(); i++) {
            try {
                list.add(array.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
