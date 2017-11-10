package com.lestariinterna.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>>,SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String LOG_TAG = NewsActivity.class.getSimpleName() ;
    private NewsAdapter newsAdapter;
    private String todayDate;
    private String searchText;
    private TextView myEmptyTextView;
    private Toolbar toolbar;

    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int NEWSAPP_LOADER_ID = 1;
    /** URL for the guardian*/
    private static final String GuardianURL =
    //       " https://content.guardianapis.com/search?q=&from-date=2017-11-02&show-fields=thumbnail,headline&show-tags=contributor&api-key=test";

            " https://content.guardianapis.com/search?";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        if(savedInstanceState==null){
            searchText = "";
        }

        toolbar = (Toolbar)findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        ListView listView = (ListView)findViewById(R.id.ListViewID);

        newsAdapter = new NewsAdapter(this,new ArrayList<News>());

        myEmptyTextView = (TextView)findViewById(R.id.textViewEmpty);
        listView.setEmptyView(myEmptyTextView);
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

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo!= null){
            getLoaderManager().initLoader(NEWSAPP_LOADER_ID,null,this);
        }else {
            View loadingIndicator = findViewById(R.id.proggres_Indicator);
            loadingIndicator.setVisibility(View.GONE);
            myEmptyTextView.setText(" No Internet Connection");

        }



        final EditText eTSearchText = (EditText)findViewById(R.id.editTextSearch);
        eTSearchText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction()== KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    searchText = eTSearchText.getText().toString();
                    getLoaderManager().restartLoader(NEWSAPP_LOADER_ID,null,NewsActivity.this);
                }

                return false;
            }
        });
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        View proggress = (View)findViewById(R.id.proggres_Indicator);
        proggress.setVisibility(View.GONE);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String Order_by = sharedPreferences.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        String Sections = sharedPreferences.getString(
                getString(R.string.settings_sections_key),getString(R.string.settings_sections_default)
        );
        Uri builderBase = Uri.parse(GuardianURL);
        Uri.Builder builder = builderBase.buildUpon();
        builder.appendQueryParameter("q",searchText);
        builder.appendQueryParameter("to-date",getTodayDate());
        builder.appendQueryParameter("show-fields","thumbnail,headline");
        builder.appendQueryParameter("order-by",Order_by);
        builder.appendQueryParameter("section",Sections);
        builder.appendQueryParameter("show-tags","contributor");

        builder.appendQueryParameter("api-key","test");
        Log.v("Loader", builder.toString());
        return new NewsLoader(this,builder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        myEmptyTextView.setText("No Data");
        View proggress = (View)findViewById(R.id.proggres_Indicator);
        proggress.setVisibility(View.GONE);
        newsAdapter.clear();
        if(data !=null && !data.isEmpty()){
            newsAdapter.addAll(data);
        }
    }

    @Override
    protected void onRestart() {
        Log.v(LOG_TAG,"get Restarted");
        // Restart the loader as the query settings have been updated
        getLoaderManager().restartLoader(NEWSAPP_LOADER_ID, null, NewsActivity.this);
        super.onRestart();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         int id = item.getItemId();
        if (id==R.id.action_settings){
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;}
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.v("OnSharedPref",key);
        if (key.equals(getString(R.string.settings_sections_key))||key.equals(getString(R.string.settings_order_by_key))){
            newsAdapter.clear();
            myEmptyTextView.setVisibility(View.GONE);
            View proggress = (View)findViewById(R.id.proggres_Indicator);
            proggress.setVisibility(View.GONE);

            // Restart the loader as the query settings have been updated
            getLoaderManager().restartLoader(NEWSAPP_LOADER_ID, null, NewsActivity.this);}
    }
}
