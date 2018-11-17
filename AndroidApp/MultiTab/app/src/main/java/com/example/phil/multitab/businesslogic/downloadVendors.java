package com.example.phil.multitab.businesslogic;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.phil.multitab.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class downloadVendors extends AsyncTask<String,Void,String> {


    @Override
    protected String doInBackground(String... strings) {
        Log.i("145 xyInfo","upLoad triggered");
        StringBuilder sb = new StringBuilder();

        String http = "https://wij8zg4o5i.execute-api.eu-west-1.amazonaws.com/PROD";

        HttpURLConnection urlConnection=null;
        try {

            URL url = new URL(http);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            //urlConnection.setUseCaches(false);
            //urlConnection.setConnectTimeout(10000);
            //urlConnection.setReadTimeout(10000);
            urlConnection.setRequestProperty("Content-Type","application/json");

            urlConnection.connect();

            String result = "";
            int HttpResult =urlConnection.getResponseCode();

            if(HttpResult ==HttpURLConnection.HTTP_OK){
                //BufferedReader br = new BufferedReader(new InputStreamReader(
                //        urlConnection.getInputStream(),"utf-8"));
                //String line = null;
                //while ((line = br.readLine()) != null) {
                //    sb.append(line + "\n");
                //}
                ///br.close();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1) {

                    char current = (char) data;

                    result += current;

                    data = reader.read();

                    //Log.i("FUCK Result",result);

                }

            }else{
                System.out.println(urlConnection.getResponseMessage());
            }
        } catch (MalformedURLException e) {

            e.printStackTrace();
        }
        catch (IOException e) {

            e.printStackTrace();
        }finally{
            if(urlConnection!=null)
                urlConnection.disconnect();
        }

        return null;
    }
}
