package com.lestariinterna.newsapp;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private static final String LOG_TAG = NewsActivity.class.getName();
    private NewsAdapter newsAdapter;
    private String todayDate;
    /** URL for the guardian*/
    private static final String GuardianURL =
    //       " https://content.guardianapis.com/search?q=&from-date=2017-11-02&show-fields=thumbnail,headline&show-tags=contributor&api-key=test";

            " https://content.guardianapis.com/search?";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        ListView listView = (ListView)findViewById(R.id.ListViewID);
        newsAdapter = new NewsAdapter(this,new ArrayList<News>());
        listView.setAdapter(newsAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected news
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // Find the current news
                News currentNews = newsAdapter.getItem(position);
                Log.v("CurrentNewsURL",currentNews.getArtikelUrl());
                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsUri = Uri.parse(currentNews.getArtikelUrl());
                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);

            }
        });

        getLoaderManager().initLoader(1,null,this);
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {


        Uri builderBase = Uri.parse(GuardianURL);
        Uri.Builder builder = builderBase.buildUpon();
        builder.appendQueryParameter("q","");
        builder.appendQueryParameter("from-date",getTodayDate());
        builder.appendQueryParameter("show-fields","thumbnail,headline");
        builder.appendQueryParameter("show-tags","contributor");

        builder.appendQueryParameter("api-key","test");
        return new NewsLoader(this,builder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        newsAdapter.clear();
        if(data !=null && ! data.isEmpty()){
            newsAdapter.addAll(data);}
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        newsAdapter.clear();

    }
    public String getTodayDate(){

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat  = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(c.getTime());

        return date;
    }
}
