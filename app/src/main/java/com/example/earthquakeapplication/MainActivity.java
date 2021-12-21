package com.example.earthquakeapplication;


import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
            "https://earthquake.usgs.gov/fdsnws/event/1/query";

    /** TextView for EmptyView of ListView*/
    TextView mEmptyView;

    /** ProgressBarView variable*/
    ProgressBar mProgressBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  ConnectivityManager to check whether the device is connected to the internet or not
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        //  NetworkInfo variable used to determine the internet is connected or not
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        //  TextView used to set the empty view of ListView
        mEmptyView = findViewById(R.id.empty_view);

        //  ProgressBar displayed when network operation takes a lot of time
        mProgressBarView = findViewById(R.id.progress_bar);

        //  If connected to network only then loader are created
        if(networkInfo != null && networkInfo.isConnected()){
            //  LoaderManager instance
            LoaderManager lm = getLoaderManager();

            //  creating the loader
            lm.initLoader(1,null, this);
        }
        else{

            //  if not connected to internet then display the message
            mProgressBarView.setVisibility(View.GONE);
            mEmptyView.setText(R.string.no_internet);
        }

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        //  Setting a emptyview for ListView
        earthquakeListView.setEmptyView(mEmptyView);


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

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String minMagnitude = sharedPreferences.getString(getString(
                R.string.settings_min_magnitude_key), getString(R.string.settings_min_magnitude_default));

        Uri baseUri = Uri.parse(USGS_REQUEST_URL);

        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit","10");
        uriBuilder.appendQueryParameter("minmag",minMagnitude);
        uriBuilder.appendQueryParameter("orderby","time");
        //  Create a new loader for the given URL
        return new EarthQuakeLoader(this, uriBuilder.toString());
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

        //  When no data is present the ListView will show emptyview
        mEmptyView.setText(R.string.no_earthquake);

        //  Making the progressbar disappear when the loader receives the data
        mProgressBarView.setVisibility(View.GONE);
    }

    /**
     * Resets the data in loader
     * @param loader    Loader Instance to be reseted
     */
    @Override
    public void onLoaderReset(Loader<List<InformationList>> loader) {
        //  Loader reset, so we can clear out our existing data.
        informationAdapter.clear();
    }

    /**
     * Inflates the options menu when it is first created
     * @param   menu Variable of Menu type
     * @return  a boolean value
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Method is called when the menu is first loaded
     * @param   item  One of the items in Menu
     * @return  a boolean value
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.action_settings){
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
