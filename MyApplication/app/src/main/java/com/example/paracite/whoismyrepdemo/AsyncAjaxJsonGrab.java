package com.example.paracite.whoismyrepdemo;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.Enumeration;

import javax.net.ssl.HttpsURLConnection;

/**
 Created by paracite on 9/15/15.
 */
public class AsyncAjaxJsonGrab extends AsyncTask<String, Void, JSONObject> {
    // This class pulls JSON from the API asyncronously while also placing a spinner on the UI to notify the user

    AjaxJsonGrabListener AjaxJsonGrabListener;

    public AsyncAjaxJsonGrab(AjaxJsonGrabListener AjaxJsonGrabListener) {
        this.AjaxJsonGrabListener = AjaxJsonGrabListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected JSONObject doInBackground(String... params) {

        InputStream inputStream = null;
        HttpsURLConnection conn = null;

        JSONObject jData = null;

        URL ajaxUrl = null;
        try {
            String urlString = Con.Ajax.PREFIX.concat(params[0].replace(" ","%20"));
            //FIXME: bad string given for IP address
            //urlString = urlString.concat(getLocalIpAddress());
            Log.d("URL String VALUE: ", urlString);
            ajaxUrl = new URL(urlString);
            Log.d(" URL Object VALUE: ",ajaxUrl.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.d(" String to URL ERROR", e.getLocalizedMessage());
        }

        int retrys = 0;
        int statusCode = 0;

        try {
            do {
                retrys++;
                if (ajaxUrl != null) {
                    //FIXME: rmnxtln
                    /*URL test = new URL("https://ajax.googleapis.com/ajax/services/search/images?" +
                                              "v=1.0&q=barack%20obama");
*/
                    conn = (HttpsURLConnection) ajaxUrl.openConnection();
                    //conn.addRequestProperty("Referer", "Who's Representing Me - App for Android");
                    //conn.setRequestProperty("Content-Type", "text/javascript");
                    //conn.setRequestProperty("Accept", "text/javascript");
                    conn.setConnectTimeout(10000);
                    conn.setReadTimeout(15000);

                    conn.setRequestMethod("GET");
                    statusCode = conn.getResponseCode();

                    //TODO: implement other error codes here.
                    if (statusCode == 200) {
                        inputStream = new BufferedInputStream(conn.getInputStream());
                        jData = JsonUtilities.convertInputStreamToString(inputStream);
                        Log.d("Jdata : ", jData.toString());
                        inputStream.close();
                    } else {
                        //TODO: Instantiate popup dialogue to inform user of connectivity problem
                        Log.d("Status code ERROR", "Status code bad. Value = " + statusCode);
                    }
                } else {
                    Log.d("Ajax json pull ERROR", "URL is null!");
                }

            } while (statusCode != 200 && retrys < 2);
        } catch (Exception e) {
            //FIXME: hardcoded string
            Log.d(" URLConnection ERROR", e.getLocalizedMessage());
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return jData;
        //TODO: Add timeout timer?
    }

    @Override
    protected void onPostExecute(JSONObject jData) {
        super.onPostExecute(jData);

        if (jData != null) {
            if (jData.has("responseData")) { //Success
                String [] imageURLs = JsonUtilities.parseAjaxResult(jData);
                //TODO: Some kind of assertion that reps succeeded?
                AjaxJsonGrabListener.onAjaxJsonGrabComplete(imageURLs);
            }
        }//TODO: else popup of problem (what kind of problems could this be? corrupt data?).
        //TODO: Attempt retry's first, depending on the nature of the code.
    }

    ///////////////////////////////////////////////////////////////////////////
    // User IP address tools
    ///////////////////////////////////////////////////////////////////////////
    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("IP Address", ex.toString());
        }
        return null;
    }

}

