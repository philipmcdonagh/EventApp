package com.example.philip.demoappl.database;

import com.example.philip.demoappl.model.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "EVENTDB";

    // Table Names
    private static final String TABLE_EVENTS = "events";
    private static final String TABLE_VENDORS = "vendors";
    private static final String TABLE_USER = "user";

    // Common column names
    //private static final String KEY_ID = "id";
    //private static final String KEY_CREATED_AT = "created_at";

    //EVENTS  Table - column names
    public static final String COLUMN_EVENT = "event";
    public static final String COLUMN_BEACONID = "beaconId";
    public static final String COLUMN_EVENTTIME = "eventTime";
    public static final String COLUMN_USERID = "userId";
    public static final String COLUMN_EVENTID = "eventId";

    //VENDORS Table - column names
    public static final String COLUMN_VENDORNAME = "vendorName";
    public static final String COLUMN_VENDORCATEGORY = "vendorCategory";
    public static final String COLUMN_VENDORSUBCATEGORY = "vendorSubCategory";
    public static final String COLUMN_VENDORDESCRIPTION = "vendorDescription";
    public static final String COLUMN_VENDORSTAND = "vendorStand";

    //USER Table - column names
    public static final String COLUMN_USERNAME = "userName";
    public static final String COLUMN_USER_ID = "userId";
    public static final String COLUMN_USEREMAIL = "userEmail";
    public static final String COLUMN_USERLINKEDIN = "userLinkedin";
    public static final String COLUMN_USERCOMPANY = "eventId";
    public static final String COLUM_USERFAVOURITES = "userFavourites";

    // Table Create Statements
    // EVENTS table create statement
    public static final String CREATE_TABLE_EVENTS =
            "CREATE TABLE " + TABLE_EVENTS + "("
                    + COLUMN_EVENT + " TEXT,"
                    + COLUMN_BEACONID + " TEXT,"
                    + COLUMN_EVENTTIME + " TEXT,"
                    + COLUMN_USERID + " TEXT,"
                    + COLUMN_EVENTID + "INTEGER PRIMARY KEY"
                    + ")";

    // VENDOR table create statement
    public static final String CREATE_TABLE_VENDORS =
            "CREATE TABLE " + TABLE_VENDORS + "("
                    + COLUMN_VENDORNAME + " TEXT,"
                    + COLUMN_VENDORCATEGORY + " TEXT,"
                    + COLUMN_VENDORSUBCATEGORY + " TEXT,"
                    + COLUMN_VENDORDESCRIPTION + " TEXT,"
                    + COLUMN_VENDORSTAND + " INT"
                    + ")";

    //Create USER table
    public static final String CREATE_TABLE_USER =
            "CREATE TABLE " + TABLE_USER + "("
                    + COLUMN_USERNAME + " TEXT,"
                    + COLUMN_USERID + " TEXT,"
                    + COLUMN_USEREMAIL + " TEXT,"
                    + COLUMN_USERLINKEDIN + " TEXT,"
                    + COLUMN_USERCOMPANY + " TEXT,"
                    + COLUM_USERFAVOURITES  + " TEXT"
                    + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_VENDORS);
        db.execSQL(CREATE_TABLE_EVENTS);
        db.execSQL(CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VENDORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // create new tables
        onCreate(db);
    }

    // ------------------------ "EVENTS" table methods ----------------//

    //Creating an event entry in the table
    public void createEvent(Event events) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT, events.getEvent());
        values.put(COLUMN_BEACONID, events.getBeaconId());
        values.put(COLUMN_EVENTTIME, events.getEventTime());
        values.put(COLUMN_USERID, events.getUserId());

        // insert row
        db.insert(TABLE_EVENTS, null, values);
    }

    // getting all Events
    public List<Event> getAllEvents() {
        List<Event> eventsList = new ArrayList<Event>();
        String selectQuery = "SELECT  * FROM " + TABLE_EVENTS;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Event e = new Event();
                e.setEvent(c.getString((c.getColumnIndex(COLUMN_EVENT))));
                e.setBeaconId((c.getString(c.getColumnIndex(COLUMN_BEACONID))));
                e.setEventTime(c.getString(c.getColumnIndex(COLUMN_EVENTTIME)));
                e.setUserId(c.getString(c.getColumnIndex(COLUMN_USERID)));

                // adding to event list
                eventsList.add(e);
            } while (c.moveToNext());
        }
        return eventsList;
    }

     // Deleting a event

    public void deleteToDo(long event_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EVENTS, COLUMN_EVENTID + " = ?",
                new String[] { String.valueOf(event_id) });
    }


    //Vendor table methods

    //create
    //Creating an event entry in the table
    public void createVendor(Vendor vendor) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_VENDORNAME, vendor.getVendorName());
        values.put(COLUMN_VENDORCATEGORY, vendor.getVendorCategory());
        values.put(COLUMN_VENDORSUBCATEGORY, vendor.getVendorSubCategory());
        values.put(COLUMN_VENDORDESCRIPTION, vendor.getVendorDescription());
        values.put(COLUMN_VENDORSTAND, vendor.getVendorStand());
        // insert row
        db.insert(TABLE_VENDORS, null, values);
    }

    //Get all vendors
    /*
    public List<Vendor> getAllVendors() {
        List<Vendor> vendorList = new ArrayList<Vendor>();
        String selectQuery = "SELECT  * FROM " + TABLE_VENDORS;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Vendor v = new Vendor();
                v.setVendorName(c.getString((c.getColumnIndex(COLUMN_VENDORNAME))));
                v.setVendorCategory(c.getString((c.getColumnIndex(COLUMN_VENDORCATEGORY))));
                v.setVendorSubCategory(c.getString((c.getColumnIndex(COLUMN_VENDORSUBCATEGORY))));
                v.setVendorDescription(c.getString((c.getColumnIndex(COLUMN_VENDORDESCRIPTION))));
                v.setVendorStand(c.getInt((c.getColumnIndex(COLUMN_VENDORSTAND))));

                // adding to event list
                vendorList.add(v);
            } while (c.moveToNext());
        }
        return vendorList;
    } */

    public Vendor getSingleVendor(int vendorStand) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_VENDORS + " WHERE "
                + COLUMN_VENDORSTAND + " = " + vendorStand;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Vendor v = new Vendor();
        v.setVendorName(c.getString((c.getColumnIndex(COLUMN_VENDORNAME))));
        v.setVendorCategory(c.getString((c.getColumnIndex(COLUMN_VENDORCATEGORY))));
        v.setVendorSubCategory(c.getString((c.getColumnIndex(COLUMN_VENDORSUBCATEGORY))));
        v.setVendorDescription(c.getString((c.getColumnIndex(COLUMN_VENDORDESCRIPTION))));
        v.setVendorStand(c.getInt((c.getColumnIndex(COLUMN_VENDORSTAND))));

        return v;
    }

    // ------------------------ "USER" table methods ----------------//

    //Creating an event entry in the table
    public void createUSER(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, user.getUserName());
        values.put(COLUMN_USER_ID, user.getUserId());
        values.put(COLUMN_USERCOMPANY, user.getUserCompany());
        values.put(COLUMN_USERLINKEDIN, user.getUserLinkedin());
        values.put(COLUMN_USEREMAIL, user.getUserEmail());

        // insert row
        db.insert(TABLE_EVENTS, null, values);
    }

    // getting all Events
    public List<Event> getAllUser() {
        List<Event> eventsList = new ArrayList<Event>();
        String selectQuery = "SELECT  * FROM " + TABLE_EVENTS;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Event e = new Event();
                e.setEvent(c.getString((c.getColumnIndex(COLUMN_EVENT))));
                e.setBeaconId((c.getString(c.getColumnIndex(COLUMN_BEACONID))));
                e.setEventTime(c.getString(c.getColumnIndex(COLUMN_EVENTTIME)));
                e.setUserId(c.getString(c.getColumnIndex(COLUMN_USERID)));

                // adding to event list
                eventsList.add(e);
            } while (c.moveToNext());
        }
        return eventsList;
    }

}

