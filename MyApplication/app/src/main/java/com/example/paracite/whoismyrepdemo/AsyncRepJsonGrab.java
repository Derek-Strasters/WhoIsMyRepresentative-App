package com.example.paracite.whoismyrepdemo;

import android.app.ProgressDialog;
import android.content.Context;
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
public class AsyncRepJsonGrab extends AsyncTask<URL, Void, JSONObject> {
    // This class pulls JSON from the API asyncronously while also placing a spinner on the UI to notify the user

    RepJsonGrabListener repJsonGrabListener;
    ProgressDialog progressDialog;
    Context context;

    public AsyncRepJsonGrab(Context context, RepJsonGrabListener repJsonGrabListener) {
        this.repJsonGrabListener = repJsonGrabListener;
        this.context = context;
        progressDialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setTitle(Con.Dlg.TITLE);
        progressDialog.setMessage(Con.Dlg.MSG);
        progressDialog.setCanceledOnTouchOutside(false);
        //TODO: handle any other cancel conditions.
        progressDialog.show();
    }

    @Override
    protected JSONObject doInBackground(URL... params) {

        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;

        //TODO: parseRepResult is informed of and handles failure if jData returns null;
        JSONObject jData = null;

        int retrys = 0;
        int statusCode;

        try {
            do {
                retrys++;

                urlConnection = (HttpURLConnection) params[0].openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestMethod("GET");
                statusCode = urlConnection.getResponseCode();

                //TODO: implement other error codes here.
                if (statusCode == 200) {

                    /*
                    TODO: implement data verification by getting JSON twice and comparing.
                    Side thought on the compare verification.  We could implement a difference checking
                    method that is called upon getting a failure from the direct compare check that would
                    start logging the results from many get attempts assign each char in the results a
                    confidence value and would check the total result string length's std. deviation.
                    Repeating the request until the std. dev. drops below a certain threshold deemed
                    acceptable, or a max retry number is reached.  Overkill here, but would be useful in
                    any kind of financial or banking software.
                    */

                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    jData = JsonUtilities.convertInputStreamToString(inputStream);
                    inputStream.close();

                    break;
                } else {
                    //TODO: Instantiate popup dialogue to inform user of connectivity problem
                }
            } while (statusCode != 200 && retrys < 10);

        } catch (Exception e) {
            //FIXME: hardcoded string
            Log.d("JsonUtilities", e.getLocalizedMessage());
            return null;
        } finally {
            urlConnection.disconnect();
        }

        // A delay, to ensure the user experience is consistent and pleasant.
        // This decision was made to give the user enough time to read the progress dialog
        //      rather than
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

        if (jData != null) {
            if (jData.has("results")) { //Success
                Representative[] reps = JsonUtilities.parseRepResult(jData);
                //TODO: Some kind of assertion that reps succeeded?
                repJsonGrabListener.onRepJsonGrabComplete(reps);
            }
        }//TODO: else popup of problem (what kind of problems could this be? corrupt data?).
        //TODO: Attempt retry's first, depending on the nature of the code.
    }

    ///////////////////////////////////////////////////////////////////////////
    // Listener Interface
    ///////////////////////////////////////////////////////////////////////////

    public interface RepJsonGrabListener {
        public void onRepJsonGrabComplete(Representative[] reps);
    }
}

