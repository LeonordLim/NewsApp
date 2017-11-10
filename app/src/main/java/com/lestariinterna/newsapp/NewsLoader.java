package com.lestariinterna.newsapp;

import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by AllinOne on 30/10/2017.
 */

public class NewsLoader extends android.content.AsyncTaskLoader<List<News>> {

    /** Query URL */
    private String mUrl;
    /** Tag for log messages */
    private static final String LOG_TAG =NewsLoader.class.getSimpleName();

    public NewsLoader(Context context,String Url){
        super(context);
        mUrl= Url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<News> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        Log.v(LOG_TAG,"url:"+mUrl);
        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<News> news = QueryUtils.fetchEarthquakeData(mUrl);
        return news;
    }
}
