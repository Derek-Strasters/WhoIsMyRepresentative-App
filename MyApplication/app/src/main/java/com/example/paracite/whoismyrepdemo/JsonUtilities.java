package com.example.paracite.whoismyrepdemo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 Created by paracite on 9/10/15.
 */
public class JsonUtilities {

    // Create Representative objects from JSON, separated by new line character for display in a textView or similar.
    public static Representative[] parseRepResult(JSONObject result) {

        Representative[] Reps = null;
        JSONArray jArray = null;

        try {
            jArray = result.getJSONArray("results");

            Reps = new Representative[jArray.length()];

            //TODO: break out each representative into their own swipe-able fragment.
            // Iterate through all data contained in all entries, each being concatenated to a new line.

            // Deposit the JSON data as representative objects.
            for (int i=0; i< jArray.length();i++ ) {
                Reps[i] = new Representative(jArray.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Reps;
    }

    // Forms a usable URL with the JSON flag appended.
    public static URL formURLforGrab (String baseURL, String validatedString) {

        URL requestURL = null;
        try {
            requestURL = new URL(baseURL.concat(validatedString).concat(Consts.JSON_SUFIX));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            //TODO: Add error logging to record URL validation failure
        }
        return  requestURL;
    }


    // Reads an inputStream onto a String that is returned. The inputStream is left open.
    public static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            total.append(line);
        }
        reader.close();
        return total.toString();
    }
}




