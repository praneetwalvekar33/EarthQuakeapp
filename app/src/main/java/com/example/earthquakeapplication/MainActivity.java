package com.example.earthquakeapplication;


import android.app.LoaderManager;
import android.content.Intent;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<InformationList>>{

    /** Variable to store the InformationAdapter*/
    private InformationAdapter informationAdapter;

    /** String variable that contains link for the USGS site*/
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=6&limit=10";

    TextView mEmptyView;
    ProgressBar mProgressBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        LoaderManager lm = getLoaderManager();

        lm.initLoader(1,null, this);


        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        mEmptyView = findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(mEmptyView);

        mProgressBarView = findViewById(R.id.progress_bar);

        // Create a new {@link ArrayAdapter} of earthquakes
        informationAdapter = new InformationAdapter(
                this, new ArrayList<InformationList>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(informationAdapter);


        // This code is executed whenever the user click on the ListView
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                InformationList currentEarthquake = informationAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });
    }

    /**
     * This method is used to create a new loader
     * @param i Number of the Loader
     * @param bundle
     * @return  A new Loader Instance
     */
    @Override
    public Loader<List<InformationList>> onCreateLoader(int i, Bundle bundle) {
        // TODO: Create a new loader for the given URL
        return new EarthQuakeLoader(this, USGS_REQUEST_URL);
    }

    /**
     * This method returns the data when it retrived from the background thread
     * @param loader        Loader instance
     * @param earthquakes   ArrayList of InformationList object
     */
    @Override
    public void onLoadFinished(Loader<List<InformationList>> loader, List<InformationList> earthquakes) {

        //  Clear the Adapter
        informationAdapter.clear();

        //  Add the new data from informationLists array
        if(earthquakes != null && !earthquakes.isEmpty()) {
            informationAdapter.addAll(earthquakes);
        }


            mEmptyView.setText(R.string.no_earthquake);

        mProgressBarView.setVisibility(View.GONE);
    }

    /**
     * Resets the data in loader
     * @param loader    Loader Instance to be reseted
     */
    @Override
    public void onLoaderReset(Loader<List<InformationList>> loader) {
        // TODO: Loader reset, so we can clear out our existing data.
        informationAdapter.clear();
    }


}
