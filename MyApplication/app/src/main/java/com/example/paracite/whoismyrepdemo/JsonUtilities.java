package com.example.paracite.whoismyrepdemo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 Created by paracite on 9/10/15.
 */
public class JsonUtilities {




    // Create human readable string, separated by new line character for display in a textView or similar.
    public static String parseResult(JSONObject result) {

        String readable = null;

        JSONArray names = result.optJSONArray("name");
        String[] jnames = new String[names.length()];

        for (int i=0; i< names.length();i++ ) {
        }

        return readable;
    }

    // Forms a usable URL with the JSON flag appended.
    public static URL formURLforGrab (String baseURL, String validatedString) {

        URL requestURL = null;
        try {
            requestURL = new URL(baseURL.concat(validatedString).concat(Parms.JSON_SUFIX));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            //TODO: Add error logging to record URL validation failure
        }
        return  requestURL;
    }


    // Reads an inputStream onto a String that is returned. The inputStream is left open.
    public static String convertInputStreamToString(InputStream inputStream) throws IOException {
        //BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
        //StringBuilder total = new StringBuilder();
        //String line;
        //while ((line = r.readLine()) != null) {
        //    total.append(line);
        //}

        return null; //total.toString();
    }
}




