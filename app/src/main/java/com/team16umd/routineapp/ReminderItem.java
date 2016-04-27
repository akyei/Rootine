package com.team16umd.routineapp;

import android.content.Intent;

import java.util.Map;

/**
 * Created by thekyei on 4/20/16.
 */

//TODO Define the XML layout for the ReminderItems (Each Row in the ListView)
public class ReminderItem {
    public final static String TITLE = "title";
    public final static String DESCRIPTION = "description";
    public final static String NOTIFICATION = "notification";
    public final static String FEED = "feed";

    //TODO: Add private/public/protected fields as needed for the ReminderItem


    //TODO: Create a Reminder constructor that uses a packaged Intent to create a Reminder Item
    public ReminderItem(Intent data){

    }
    //TODO: Create a Reminder constructor with the appropriate fields as parementers
    public ReminderItem(){

    }

    public static Intent packageIntent(Intent data, String title, String description, boolean notification, boolean feed){
        data.putExtra(TITLE, title);
        data.putExtra(DESCRIPTION, description);
        data.putExtra(NOTIFICATION, notification);
        data.putExtra(FEED, feed);

        return data;
    }

    public Map<String, Object> toMap(){
        //TODO create a method that converts a Reminder Item to a Map/JSON in order to input it
        //into firebase.

        //Stub
        return null;
    }
}
