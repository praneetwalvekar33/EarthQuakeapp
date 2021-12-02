package com.example.earthquakeapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a fake list of earthquake locations.
        ArrayList<InformationList> earthquakes = QueryUtils.extractInformationOfEarthQuake();

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of earthquakes
        InformationAdapter adapter = new InformationAdapter(
                this, earthquakes);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);


        //
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = earthquakes.get(position).getUrl();
                System.out.println(1);
                Intent earthQuakeUrlIntent = new Intent(Intent.ACTION_VIEW);
                earthQuakeUrlIntent.setData(Uri.parse(url));
                //if(earthQuakeUrlIntent.resolveActivity(getPackageManager()) != null){
                    startActivity(earthQuakeUrlIntent);
                    System.out.println("clicked");
                //}
            }
        });
    }
}