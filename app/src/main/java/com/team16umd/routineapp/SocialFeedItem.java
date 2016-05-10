package com.team16umd.routineapp;

import java.util.Date;

/**
 * Created by thekyei on 4/27/16.
 */
public class SocialFeedItem {

    private String mFeedMessage;


    public SocialFeedItem(String feedMessage){
        this.mFeedMessage = feedMessage;
    }

    public String getMessage(){
        return mFeedMessage;
    }

}
