package com.example.earthquakeapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    /** Variable to store the InformationAdapter*/
    private InformationAdapter informationAdapter;

    /** String variable that contains link for the USGS site*/
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=6&limit=10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EarthQuakeAsyncTask earthQuakeAsyncTask = new EarthQuakeAsyncTask();
        earthQuakeAsyncTask.execute(USGS_REQUEST_URL);


        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

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
     * AsyncTask is used for running expensive operation(like network operations)
     */
    private class EarthQuakeAsyncTask extends AsyncTask<String, Void, ArrayList<InformationList>>{

        /**
         * This method runs the network operation on the background thread
          * @param url String formatted
         * @return Arraylist of InformaionList Objects
         */
        protected ArrayList<InformationList> doInBackground(String... url){

            if(url.length < 1 || url[0] == null){
                return null;
            }

             ArrayList<InformationList> earthquakes = QueryUtils.fetchArrayListData(url[0]);

            return earthquakes;
        }

        /**
         * This method runs on the mainthread after the task on background thread is completed
         * @param informationLists ArrayList of InformationList Object
         */
        @Override
        protected void onPostExecute(ArrayList<InformationList> informationLists) {

            //  Clear the Adapter
            informationAdapter.clear();

            //  Add the new data from informationLists array
            informationAdapter.addAll(informationLists);
        }
    }
}
