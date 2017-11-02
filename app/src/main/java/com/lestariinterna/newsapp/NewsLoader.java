package com.lestariinterna.newsapp;

import android.content.Context;

import java.util.List;

/**
 * Created by AllinOne on 30/10/2017.
 */

public class NewsLoader extends android.content.AsyncTaskLoader<List<News>> {

    /** Query URL */
    private String mUrl;

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
        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<News> news = QueryUtils.fetchEarthquakeData(mUrl);
        return news;
    }
}
