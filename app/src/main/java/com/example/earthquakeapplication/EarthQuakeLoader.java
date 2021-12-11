package com.example.earthquakeapplication;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

public class EarthQuakeLoader extends AsyncTaskLoader<ArrayList<InformationList>> {

    /** Variable to store the String Url*/
    private final String murl;

    /**
     * Constructor to create the instance of EarthQuakeLoader class
     * @param context  Activity from which it is called
     * @param url      String url used to connect to the API
     */
    public EarthQuakeLoader(Context context, String url){
        super(context);
        murl = url;
    }

    /**
     * This method is executed to load data
     */
    @Override
    protected void onStartLoading() {
        //  Used for Asynchronous load
        forceLoad();
    }

    /**
     * This method run network operations on the background thread
     * @return  ArrayList of InformationList Object
     */
    @Override
    public ArrayList<InformationList> loadInBackground() {
        if(murl == null){
            return null;
        }

        ArrayList<InformationList> earthquakes = QueryUtils.fetchArrayListData(murl);

        return earthquakes;
    }
}
