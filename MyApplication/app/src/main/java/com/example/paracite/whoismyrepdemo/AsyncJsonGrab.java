package com.example.paracite.whoismyrepdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 Created by paracite on 9/15/15.
 */
public class AsyncJsonGrab extends AsyncTask<URL, Void, JSONObject> {
    // This class pulls JSON from the API asyncronously while also placing a spinner on the UI to notify the user

    ProgressDialog progressDialog;
    Context context;

    public AsyncJsonGrab(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setTitle(Parms.DIAG_TITLE);
        progressDialog.setMessage(Parms.DIAG_MSG);
        progressDialog.show();
    }

    @Override
    protected JSONObject doInBackground(URL... params) {

        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;

        //TODO: parseResult is informed of and handles failure if jData returns null;
        JSONObject jData = null;

        try {
            // forming the java.net.URL object
            urlConnection = (HttpURLConnection) params[0].openConnection();

            // optional request header
            urlConnection.setRequestProperty("Content-Type", "application/json");

            // optional request header
            urlConnection.setRequestProperty("Accept", "application/json");

            // for Get request
            urlConnection.setRequestMethod("GET");
            int statusCode = urlConnection.getResponseCode();

            // 200 represents HTTP OK
            //TODO: implement other error codes here.
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
                jData = new JSONObject(JsonUtilities.convertInputStreamToString(inputStream));
                inputStream.close();
            } else {
                //TODO: Instantiate popup dialogue to inform user of connectivity problem
                //TODO: first initiate a handful of retry attempts
            }
        } catch (Exception e) {
            //FIXME: hardcoded string
            Log.d("JsonUtilities", e.getLocalizedMessage());
            return null;
        } finally {
            urlConnection.disconnect();
        }

        // A delay, to ensure the user experience of "things" happening behind the scenes.
        // TODO: reimplement using a check in the difference in system time
        //try {
        //    Thread.sleep(2000);
        //} catch (InterruptedException e) {
        //    e.printStackTrace();
        //}

        return jData;
        //TODO: Add timeout timer?
    }

    @Override
    protected void onPostExecute(JSONObject jData) {
        super.onPostExecute(jData);

        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();

        if (jData.has("results")) { //Success

            String readable = JsonUtilities.parseResult(jData);

            //FIXME: Poor MVC separation, implement a callback pattern, where the callback is called from here.

            // Start result activity
            Intent i = new Intent(context, ResultActivity.class);
            i.putExtra(Parms.RSLT_MSG, readable);
            context.startActivity(i);
        } //TODO: else popup of problem (what kind of problems could this be? corrupt data?).
        //TODO: Attempt retry's first, depending on the nature of the code.
    }

}

