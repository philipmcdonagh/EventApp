package com.example.philip.demoappl;

import com.example.philip.demoappl.View.VendorView;
import com.example.philip.demoappl.View.hideVendorView;
import com.example.philip.demoappl.businesslogic.*;
import com.example.philip.demoappl.model.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.example.philip.demoappl.model.User;
import com.example.philip.demoappl.businesslogic.importVendors;

import com.example.philip.demoappl.database.DatabaseHelper;

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

import static android.provider.Contacts.SettingsColumns.KEY;

public class MainActivity extends AppCompatActivity {

    //declare database helper
    DatabaseHelper db;

    //declare user and make available to everything, this is a place holder until a proper user login has been set up
    public static User currentUser = new User("Phil","1","phil@myemail.com","philsCompany","myLinkedin.com");

    // 1. Add a property to hold the Proximity Observer to listen for bluetooth beacons
    private ProximityObserver proximityObserver;

    public Vendor v1 = new Vendor("Omeara camping","camping equipment","tents","Gazebos, Marquees, Accessories, Ice Boxes And So Much More",1);
    public Vendor v11 = new Vendor("Millets","camping equipment","stoves","Stoves, portable cookers and pots",2);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //hide all the buttons so nothing displays until in range of a beacon
        hideVendorView hideVendorView = new hideVendorView(MainActivity.this);
        hideVendorView.Display();

        //create database helper instance
        db = new DatabaseHelper(getApplicationContext());

        //create vendor on db - dummy
        db.createVendor(v11);

        //download the vendors from the db to local
        downloadVendors task = new downloadVendors();
        task.execute();

        //cloud credentials for estimote SDK
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
                        Event e = new Event("checkin","1",currentDateTimeString,currentUser.getUserId());
                        //pop up to inform of check in
                        Toast.makeText(MainActivity.this, "You entered s1 " + currentDateTimeString, Toast.LENGTH_SHORT).show();
                        //upload the task to the database using the upload task
                        upLoad task = new upLoad();
                        task.execute(e.getEvent(),e.getBeaconId(),e.getEventTime(),e.getUserId());
                        //check connection, if no connection available write the event to the local db for later upload
                        //record event to local sqllite db
                        db.createEvent(e);
                        //Retrieve vendor from the db with stand number that matches the beacon id and display the details for this vendor

                        Vendor v2 = db.getSingleVendor(2);
                        VendorView vendorView = new VendorView(v2,MainActivity.this);
                        vendorView.Display();

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
                        Event e = new Event("checkout","1",currentDateTimeString,currentUser.getUserId());
                        //pop up to inform of check in
                        Toast.makeText(MainActivity.this, "You have left stand 1 at " + currentDateTimeString, Toast.LENGTH_LONG).show();
                        //upload the task to the database using the upload task
                        upLoad task = new upLoad();
                        task.execute(e.getEvent(),e.getBeaconId(),e.getEventTime(),e.getUserId());
                        //record event to local sqllite db
                        db.createEvent(e);

                        hideVendorView hideVendorView = new hideVendorView(MainActivity.this);
                        hideVendorView.Display();

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
