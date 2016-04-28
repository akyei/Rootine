package com.team16umd.routineapp;

import android.content.Intent;

import com.shaded.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shaded.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by thekyei on 4/20/16.
 */

//DONE Define the XML layout for the ReminderItems (Each Row in the ListView)
@JsonIgnoreProperties({"mKey"})
public class ReminderItem {
    public final static String TITLE = "mTitle";
    public final static String DESCRIPTION = "mDesc";
    public final static String NOTIFICATION = "mNotification";
    public final static String FEED = "mFeed";
    public final static String DAY = "mDayStatus";
    public final static String NIGHT = "mNightStatus";
    public final static String TAG = "REMINDER_ITEM";

    //DONE: Add private/public/protected fields as needed for the ReminderItem
    private String mDesc;
    private String mTitle;
    private boolean mFeed;
    private boolean mNotification;
    private boolean mDayStatus;
    private boolean mNightStatus;

    private String mKey = "";

    //Set the key for the reminder Item
    public void setReferenceId(String id){
        mKey = id;
    }
    //Getter for mKey
    public String getReferenceId(){
        return mKey;
    }

    /* Anything with @JsonProperty is used when retrieving a complex item from firebase */
    @JsonProperty("mTitle")
    public String getmTitle(){
        return mTitle;
    }

    public void setmTitle(String title){
        mTitle = title;

    }
    @JsonProperty("mDesc")
    public String getmDesc(){
        return mDesc;
    }

    public void setmDesc(String desc){
        mDesc = desc;
    }
    @JsonProperty("mNightStatus")
    public boolean getmNightStatus(){
        return mNightStatus;
    }
    public void setmNightStatus(boolean status){
        mNightStatus = status;
    }

    @JsonProperty("mDayStatus")
    public boolean getmDayStatus(){
        return mDayStatus;
    }

    public void setmDayStatus(boolean status){
        mDayStatus = status;
    }
    @JsonProperty("mFeed")
    public boolean getmFeed(){
        return mFeed;
    }
    public void setmFeed(boolean status){
        mFeed = status;
    }

    @JsonProperty("mNotification")
    public boolean getmNotification(){
        return mNotification;
    }
    public void setmNotification(boolean status){
        mNotification = status;
    }
    //EMPTY CONSTRUCTOR REQUIRED FOR FIREBASE
    public ReminderItem(){
        //Don't delete me
    }

    /*
        Used when adding a Reminder Item to firebase: clone of setReferenceId
     */
    public void addReferenceId(String key){
        mKey = key;
    }

    //DONE: Create a Reminder constructor that uses a packaged Intent to create a Reminder Item
    public ReminderItem(Intent data){
        mTitle = data.getStringExtra(TITLE);
        mDesc = data.getStringExtra(DESCRIPTION);
        mFeed = data.getBooleanExtra(FEED, true);
        mNotification = data.getBooleanExtra(NOTIFICATION, true);

    }
    //DONE: Create a Reminder constructor with the appropriate fields as parementers
    public ReminderItem(String title, String description, boolean feed, boolean notification){
        mTitle = title;
        mDesc = description;
        mFeed = feed;
        mNotification = notification;
        mDayStatus = false;
        mNightStatus = false;
    }

    /*
        Packages intent when a new Routine is created
     */
    public static Intent packageIntent(Intent data, String title, String description, boolean notification, boolean feed){
        data.putExtra(TITLE, title);
        data.putExtra(DESCRIPTION, description);
        data.putExtra(NOTIFICATION, notification);
        data.putExtra(FEED, feed);

        return data;
    }


    /*
        Converts Reminder Item to a JSON format.
     */
    public Map<String, Object> toMap(){
        Map<String, Object> reminderDetails = new HashMap<String, Object>();

        reminderDetails.put(NOTIFICATION, mNotification);
        reminderDetails.put(DESCRIPTION, mDesc);
        reminderDetails.put(TITLE, mTitle);
        reminderDetails.put(FEED, mFeed);
        reminderDetails.put(DAY,mDayStatus);
        reminderDetails.put(NIGHT, mNightStatus);

        return reminderDetails;
    }
}
