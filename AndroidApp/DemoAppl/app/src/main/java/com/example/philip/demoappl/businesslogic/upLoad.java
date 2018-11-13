package com.example.philip.demoappl.businesslogic;


import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class upLoad extends AsyncTask<String,Void,String> {

    @Override
    protected String doInBackground(String... strings) {
        Log.i("Info","upLoad triggered");
        Log.i("Info",strings[0]);
        Log.i("Info",strings[1]);
        //Toast.makeText(MainActivity.this, "Upload function triggered ", Toast.LENGTH_LONG).show();
        StringBuilder sb = new StringBuilder();

        String http = "https://udmcrp0q5j.execute-api.eu-west-1.amazonaws.com/PROD";

        HttpURLConnection urlConnection=null;
        try {

            URL url = new URL(http);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            urlConnection.setRequestProperty("Content-Type","application/json");

            //urlConnection.setRequestProperty("Host", "android.schoolportal.gr");
            urlConnection.connect();


            //Create JSONObject here
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("eventName", strings[0]);
            jsonParam.put("beaconId", strings[1]);
            jsonParam.put("time",strings[2]);
            jsonParam.put("userId",strings[3]);
            //jsonParam.put("isComplete", "true");
            OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
            out.write(jsonParam.toString());
            out.close();

            int HttpResult =urlConnection.getResponseCode();
            System.out.println("Http response is :" + HttpResult);
            if(HttpResult ==HttpURLConnection.HTTP_OK){
                //Log.i("Info","connection OK!");
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream(),"utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();

                System.out.println(""+sb.toString());

            }else{
                System.out.println(urlConnection.getResponseMessage());
            }
        } catch (MalformedURLException e) {

            e.printStackTrace();
        }
        catch (IOException e) {

            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            if(urlConnection!=null)
                urlConnection.disconnect();
        }

        return null;
    }
}