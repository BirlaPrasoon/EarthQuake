package com.example.hppc.earthquake;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<EarthQuake>>{

    private static final String stringURL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&starttime=2017-01-01&endtime=2017-02-01&limit=20";

    private static final int EARTHQUAKE_LOADER_ID = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(EARTHQUAKE_LOADER_ID,null,this);*/

        //Async helper to run the network programming in the background
        AsyncHelper helper = new AsyncHelper();
        helper.execute(stringURL);
    }

    private void updateUI(ArrayList<EarthQuake> earthQuakes) {
        ListView listView = findViewById(R.id.lv);
        if(earthQuakes==null||earthQuakes.isEmpty()){
            TextView emptyTextView = findViewById(R.id.emptyView);
            emptyTextView.setText(R.string.no_Data_Found);
            listView.setEmptyView(emptyTextView);
            return;
        }
        UserAdapter userAdapter = new UserAdapter(this,earthQuakes);
        listView.setAdapter(userAdapter);
        handleAdapterTouchEvent(listView,userAdapter);
    }


    /**
     * Handles the touch events happening on the ListView and
     * in turn send an intent to the browsers to open the website url associated with
     * the touched event
     * @param listView listView
     * @param userAdapter*/
    private void handleAdapterTouchEvent(ListView listView,final UserAdapter userAdapter) {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                EarthQuake currentEarthquake = userAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

    }

    @Override
    public Loader<List<EarthQuake>> onCreateLoader(int id, Bundle args) {
        EarthQuakeLoader earthQuakeLoader = new EarthQuakeLoader(this,stringURL);
        return earthQuakeLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<EarthQuake>> loader, List<EarthQuake> earthQuakes) {
        //update the ui.
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        if(earthQuakes!=null && !earthQuakes.isEmpty()){
            updateUI((ArrayList)earthQuakes);
        }

        //updateUI(null);

    }

    @Override
    public void onLoaderReset(Loader<List<EarthQuake>> loader) {
        //adapter's data isn't valid now
        updateUI(new ArrayList<EarthQuake>());

    }


    private class AsyncHelper extends AsyncTask<String,Void,ArrayList<EarthQuake>>{

        @Override
        protected ArrayList<EarthQuake> doInBackground(String... strings) {
            String jsonResponse = QueryUtils.fetchJSONArray(stringURL);
            ArrayList<EarthQuake> earthQuakes = QueryUtils.extractEarthquakes(jsonResponse);
            return earthQuakes;
        }

        @Override
        protected void onPostExecute(ArrayList<EarthQuake> earthQuakes) {
            ProgressBar progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.GONE);
            updateUI(earthQuakes);
        }
    }
}
