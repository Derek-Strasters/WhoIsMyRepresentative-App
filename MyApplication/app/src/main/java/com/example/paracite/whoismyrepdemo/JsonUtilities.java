package com.example.paracite.whoismyrepdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 Created by paracite on 9/10/15.
 */
public class JsonUtilities {



    public static boolean startJsonRequest(Context baseContext, String baseURL, String validatedString) {
        try {
            URL requestURL = new URL(baseURL.concat(validatedString).concat(Parms.JSON_SUFIX));
            doAsyncJsonGrab(baseContext, requestURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            //TODO: Add error logging to record URL validation failure
        }



        return false;
    }



    private static void doAsyncJsonGrab(Context baseContext, URL apiURL) {

        final Context fContext = baseContext;
        final ProgressDialog fProgressDialog = new ProgressDialog(fContext);

        new AsyncTask<URL, Void, JSONObject>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                fProgressDialog.show(fContext, Parms.DIAG_TITLE, Parms.DIAG_MSG);
            }

            @Override
            protected JSONObject doInBackground(URL... params) {

                InputStream inputStream = null;
                HttpURLConnection urlConnection = null;

                //TODO: parseResult is informed of and handles failure if jData returns null;
                JSONObject jData = null;

                try {
                    // forming th java.net.URL object
                    urlConnection = (HttpURLConnection) params[0].openConnection();

                    // optional request header
                    urlConnection.setRequestProperty("Content-Type", "application/json");

                    // optional request header
                    urlConnection.setRequestProperty("Accept", "application/json");

                    // for Get request
                    urlConnection.setRequestMethod("GET");
                    int statusCode = urlConnection.getResponseCode();

                    // 200 represents HTTP OK
                    if (statusCode ==  200) {
                        inputStream = new BufferedInputStream(urlConnection.getInputStream());
                        jData = new JSONObject(convertInputStreamToString(inputStream));
                    }else{
                        //TODO: Instantiate popup dialogue to inform user of connectivity problem
                    }
                } catch (Exception e) {
                    //FIXME: hardcoded string
                    Log.d("JsonUtilities", e.getLocalizedMessage());
                    return null;
                }
                return jData;
                //TODO: Add timeout timer?
            }

            @Override
            protected void onPostExecute(JSONObject jData) {
                super.onPostExecute(jData);
                fProgressDialog.dismiss();


                if (jData != null) { //Success

                    String readable = parseResult(jData);

                    // Start result activity
                    //Intent i = Intent.
                }
            }


        }.execute(apiURL);

    }







// Create human readable string for display in a textView
    private static String parseResult(JSONObject result) {

        String readable = null;

        //try{
        //    JSONArray posts = response.optJSONArray("posts");
        //    blogTitles = new String[posts.length()];
        //
        //
        //    for(int i=0; i< posts.length();i++ ){
        //        JSONObject post = posts.optJSONObject(i);
        //        String title = post.optString("title");
        //        blogTitles[i] = title;
        //    }
        //
        //
        //}catch (JSONException e) {
        //    e.printStackTrace();
        //    //TODO: Mitigate corrupt JSON data with retries then failure notification popup.
        //}

        return readable;
    }






    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null){
            result += line;
        }

            /* Close Stream */
        if(null!=inputStream){
            inputStream.close();
        }
        return result;
    }

}


