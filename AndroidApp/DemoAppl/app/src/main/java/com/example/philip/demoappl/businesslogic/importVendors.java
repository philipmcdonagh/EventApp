package com.example.philip.demoappl.businesslogic;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import retrofit2.HttpException;


public class importVendors extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... urls) {

        String result = "";
        URL url;
        HttpURLConnection urlConnection = null;

        try {

            url = new URL(urls[0]);

            urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in = urlConnection.getInputStream();

            InputStreamReader reader = new InputStreamReader(in);

            int data = reader.read();


            while (data != -1) {

                char current = (char) data;

                result += current;

                data = reader.read();

            }

            return result;


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        try {

            Log.i("FUCK POST",result);

            JSONObject jsonObject = new JSONObject(result);

            //String vendorInfo = jsonObject.getString("vendor");

            //Log.i("Vendor content", vendorInfo);

            //JSONArray arr = new JSONArray(vendorInfo);

            //for (int i = 0; i < arr.length(); i++) {

            //    JSONObject jsonPart = arr.getJSONObject(i);

            //    Log.i("vendorId", jsonPart.getString("vendorId"));
            //    Log.i("vendorName", jsonPart.getString("vendorName"));
            //    Log.i("vendorCategory", jsonPart.getString("vendorCategory"));
            //    Log.i("vendorSubCategory", jsonPart.getString("vendorSubCategory"));

           // }


        } catch (JSONException e) {
            e.printStackTrace();
        }



    }
}