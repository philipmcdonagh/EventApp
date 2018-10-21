package com.example.philip.demoappl;

import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.estimote.cloud_plugin.common.EstimoteCloudCredentials;
import com.estimote.internal_plugins_api.cloud.CloudCredentials;
import com.estimote.internal_plugins_api.cloud.proximity.ProximityAttachment;
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.Requirement;
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory;
import com.estimote.proximity_sdk.proximity.ProximityObserver;
import com.estimote.proximity_sdk.proximity.ProximityObserverBuilder;
import com.estimote.proximity_sdk.proximity.ProximityZone;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.text.DateFormat;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity {

     // 1. Add a property to hold the Proximity Observer
    private ProximityObserver proximityObserver;

    public class upLoad extends AsyncTask<String,Void,String>{

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CloudCredentials cloudCredentials = new EstimoteCloudCredentials("philtest-mef", "8d0c2f2b4fdd8c2a24c0a38f2079249b");

        // 2. Create the Proximity Observer
        this.proximityObserver =
                new ProximityObserverBuilder(getApplicationContext(),cloudCredentials)
                        .withOnErrorAction(new Function1<Throwable, Unit>() {
                            @Override
                            public Unit invoke(Throwable throwable) {
                                return null;
                            }
                        })
                .withBalancedPowerMode()
                .build();

        ProximityZone zone1 = this.proximityObserver.zoneBuilder()
                .forAttachmentKeyAndValue("position", "x")
                .inFarRange()
                .withOnEnterAction(new Function1<ProximityAttachment, Unit>() {
                    @Override
                    public Unit invoke(ProximityAttachment attachment) {
                        //record current date and format to pass to db
                        String pattern = "yyyy-MM-dd HH:mm:ss";
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                        String currentDateTimeString = simpleDateFormat.format(new Date());

                        //pop up to inform of check in
                        Toast.makeText(MainActivity.this, "You entered s1 " + currentDateTimeString, Toast.LENGTH_SHORT).show();

                        //upload the task to the database using the upload task
                        upLoad task = new upLoad();
                        task.execute("checkin","s1",currentDateTimeString);

                        return null;
                    }
                })
                .withOnExitAction(new Function1<ProximityAttachment, Unit>() {
                    @Override
                    public Unit invoke(ProximityAttachment attachment) {
                        //record current date and format to pass to db
                        String pattern = "yyyy-MM-dd HH:mm:ss";
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                        String currentDateTimeString = simpleDateFormat.format(new Date());

                        //pop up to inform of check in
                        Toast.makeText(MainActivity.this, "You have left stand 1 at " + currentDateTimeString, Toast.LENGTH_LONG).show();

                        //upload the task to the database using the upload task
                        upLoad task = new upLoad();
                        task.execute("checkout","s1",currentDateTimeString);
                        return null;
                    }
                })
                .create();
        this.proximityObserver.addProximityZone(zone1);


        RequirementsWizardFactory
                .createEstimoteRequirementsWizard()
                .fulfillRequirements(this,
                        // onRequirementsFulfilled
                        new Function0<Unit>() {
                            @Override public Unit invoke() {
                                Log.d("app", "requirements fulfilled");
                                proximityObserver.start();
                                return null;
                            }
                        },
                        // onRequirementsMissing
                        new Function1<List<? extends Requirement>, Unit>() {
                            @Override public Unit invoke(List<? extends Requirement> requirements) {
                                Log.e("app", "requirements missing: " + requirements);
                                return null;
                            }
                        },
                        // onError
                        new Function1<Throwable, Unit>() {
                            @Override public Unit invoke(Throwable throwable) {
                                Log.e("app", "requirements error: " + throwable);
                                return null;
                            }
                        });

    }





}
