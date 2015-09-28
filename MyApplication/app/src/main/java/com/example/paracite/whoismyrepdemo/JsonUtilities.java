package com.example.paracite.whoismyrepdemo;

import android.util.Log;

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

    /*  Return an array of Representative objects from JSON array, or null if any JSON keys don't evaluate. */
    public static Representative[] parseRepResult(JSONObject result) {
        Representative[] Reps = null;
        JSONArray jArray = null;
        boolean isAllRepsValid = true;

        try {
            jArray = result.getJSONArray("results");

            Reps = new Representative[jArray.length()];

            //TODO: break out each representative into their own swipe-able fragment. (move this todo)

            for (int i = 0; i< jArray.length();i++ ) {
                Reps[i] = new Representative(jArray.getJSONObject(i));
                isAllRepsValid = Reps[i].isRepsValid();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            isAllRepsValid = false;
        }

        return (isAllRepsValid)? Reps : null;
    }

    // Forms a usable URL with the JSON flag appended.
    public static URL formURLforGrab (String baseURL, String validatedString) {

        URL requestURL = null;
        try {
            requestURL = new URL(baseURL.concat(validatedString).concat(Con.JSON_SUFIX));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            //TODO: Add error logging to record URL validation failure
        }
        return  requestURL;
    }


    // Reads an inputStream onto a String that is returned. The inputStream is left open.
    public static JSONObject convertInputStreamToString(InputStream inputStream) throws IOException, JSONException {
        String line;
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        reader.close();

        return new JSONObject(builder.toString());
    }

    public static String[] parseAjaxResult(JSONObject result) {
        String[] urlStrings = new String[0];
        //FIXME
        //URL[] imageURLs = null;
        JSONArray jArray;
        JSONObject responseData;
        boolean isAllValid = true;

        try {
            responseData = result.getJSONObject("responseData");
            jArray = responseData.getJSONArray("results");
            urlStrings = new String[jArray.length()];
            //FIXME
            //imageURLs = new URL[jArray.length()];

            for (int i = 0; i < jArray.length(); i++) {
                String urlString = jArray.getJSONObject(i).getString("url");
                if (urlString.equals(""))
                    isAllValid = false;
                urlStrings[i] = urlString;
            }

            //FIXME
            /*if (isAllValid) {
                for (int i = 0; i < jArray.length(); i++) {
                    imageURLs[i] = new URL(urlStrings[i]);
                }
            }*/

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("JSON exception :", e.getMessage());
        }

        return (isAllValid)? urlStrings : null;
    }
}




