package com.example.paracite.whoismyrepdemo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
            readable.concat(jnames[i].concat("\n\n"));
        }

        return readable;
    }


    public static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line);
        }

            /* Close Stream */
        inputStream.close();

        return total.toString();
    }

}




