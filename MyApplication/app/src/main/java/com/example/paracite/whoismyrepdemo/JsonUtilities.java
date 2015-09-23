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
import java.util.Iterator;

/**
 Created by paracite on 9/10/15.
 */
public class JsonUtilities {




    // Create human readable string, separated by new line character for display in a textView or similar.
    public static String parseResult(JSONObject result) {

        String readable = "";
        String[] persons;
        JSONArray jArray = null;

        try {
            jArray = result.getJSONArray("results");

            //TODO: break out each representative into their own swipe-able fragment.
            // Iterate through all data contained in all entries, each being concatenated to a new line.
            for (int i=0; i< jArray.length();i++ ) {
                JSONObject jPerson = jArray.getJSONObject(i);
                Iterator<String> personItr = jPerson.keys();
                String person = "";

                //TODO: add very careful validation here.
                while(personItr.hasNext()){
                    String nextKey = personItr.next();
                    person = person.concat(nextKey.substring(0,1).toUpperCase()).concat(nextKey.substring(1));
                    person = person.concat("    ").concat(jPerson.getString(nextKey)).concat("\n");
                }

                readable = readable.concat(person).concat("\n");
            }

            persons = new String[jArray.length()];

        } catch (JSONException e) {
            e.printStackTrace();
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




