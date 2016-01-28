package com.studentsyard;

import android.app.Application;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;


public class StarterApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();




        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
       // Parse.initialize(this);
        Parse.initialize(this, "0VmO9KjV0hrEnabdvwyKGDs4pATtfItSmyI885aO", "SOk2GiscBAM9DSD0fOSGO8LB7NikPohrHiHlJMtZ");
        ParseUser.enableRevocableSessionInBackground();


        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
