package com.team16umd.routineapp;

import java.util.Date;

/**
 * Created by thekyei on 4/27/16.
 */
public class SocialFeedItem {

    private String mUid;
    private String mFeedMessage;
    private Date mTimestamp;


    public SocialFeedItem(String uid, String feedMessage, Date timestamp){
        this.mUid = uid;
        this.mFeedMessage = feedMessage;
        this.mTimestamp = timestamp;
    }



}
