package com.example.hppc.earthquake;
import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;


public class EarthQuakeLoader extends AsyncTaskLoader<List<EarthQuake>> {

    private String url;

    public EarthQuakeLoader(Context context, String Url) {
        super(context);
        this.url = Url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<EarthQuake> loadInBackground() {
        if(url == null)
            return null;
        String jsonResponse = QueryUtils.fetchJSONArray(url);
        ArrayList<EarthQuake> earthQuakes = QueryUtils.extractEarthquakes(jsonResponse);
        return earthQuakes;
    }
}
