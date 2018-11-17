package com.example.phil.multitab;

import com.example.phil.multitab.businesslogic.*;
import com.example.phil.multitab.View.*;

import com.estimote.cloud_plugin.common.EstimoteCloudCredentials;
import com.estimote.internal_plugins_api.cloud.CloudCredentials;
import com.estimote.internal_plugins_api.cloud.proximity.ProximityAttachment;
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.Requirement;
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory;
import com.estimote.proximity_sdk.proximity.ProximityObserver;
import com.estimote.proximity_sdk.proximity.ProximityObserverBuilder;
import com.estimote.proximity_sdk.proximity.ProximityZone;


import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phil.multitab.View.hideVendorView;
import com.example.phil.multitab.businesslogic.downloadVendors;
import com.example.phil.multitab.database.DatabaseHelper;
import com.example.phil.multitab.model.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity {

    //declare database helper
    DatabaseHelper db;

    //declare user and make available to everything, this is a place holder until a proper user login has been set up
    public static User currentUser = new User("Phil","1","phil@myemail.com","philsCompany","myLinkedin.com");

    //Add a property to hold the Proximity Observer to listen for bluetooth beacons
    private ProximityObserver proximityObserver;

    //dummy vendors until db populated
    public Vendor v1 = new Vendor("Omeara camping","camping equipment","tents","Gazebos, Marquees, Accessories, Ice Boxes And So Much More",1);
    public Vendor v11 = new Vendor("Millets","camping equipment","stoves","Stoves, portable cookers and pots",2);




///////////////////////////////////////////////////////////////////////////////////

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create database helper instance
        db = new DatabaseHelper(getApplicationContext());

        //create vendor on db - dummy
        db.createVendor(v11);

        //download the vendors from the db to local
        downloadVendors task = new downloadVendors();
        task.execute();

        //hide all the buttons so nothing displays until in range of a beacon
        //hideVendorView hideVendorView = new hideVendorView(MainActivity.this);
        //hideVendorView.Display();

        //cloud credentials for estimote SDK
        CloudCredentials cloudCredentials = new EstimoteCloudCredentials("philtest-mef", "8d0c2f2b4fdd8c2a24c0a38f2079249b");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //Return the current tabs
            switch(position) {
                case 0:
                    homeTab tab1 = new homeTab();
                    return tab1;
                case 1:
                    eventTab tab2 = new eventTab();
                    return tab2;
                case 2:
                    standsTab tab3 = new standsTab();
                    return tab3;
                default:
                    return null;

            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position){
            switch (position){
                case 0:
                    return "EVENTS";
                case 1:
                    return "HOME";
                case 2:
                    return "STANDS";
                }return null;
            }
        }
    }