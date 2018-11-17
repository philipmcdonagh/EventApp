package com.example.phil.multitab.model;

import java.text.SimpleDateFormat;

public class Event {

    private String event;
    private String beaconId;
    private String userId;
    private String eventTime;
    private int eventId;

    public Event() {
    }

    //Constructor
    public Event(String event, String beaconId, String eventTime, String userId ) {
        this.event = event;
        this.beaconId = beaconId;
        this.eventTime = eventTime;
        this.userId = userId;
    }

    //getters & setters
    public String getEvent() {
        return event;
    }

    public String getBeaconId() {
        return beaconId;
    }

    public String getEventTime() {
        return eventTime;
    }

    public String getUserId() { return  userId; }

    public int getEventId () {return eventId; }

    public void setEvent(String event) {
        this.event = event;
    }

    public void setBeaconId(String beaconId) {
        this.beaconId = beaconId;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public void setUserId(String userId) {this.userId = userId;}

    public void setEventId(int eventId) {this.eventId = eventId;}

}

