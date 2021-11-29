package com.example.earthquakeapplication;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class InformationAdapter extends ArrayAdapter<InformationList> {

    public InformationAdapter(Activity context, ArrayList<InformationList> arrayList){
        //  Initating the internal storage of arrayadapter for context and arraylist
        super(context, 0, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;

        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.information_list, parent, false);
        }

        //  Storing the InformationList object present at this position in arrayList
        InformationList currentItem = getItem(position);

        //  Setting the TextView(magnitude)
        TextView magnitudeView =(TextView) listItemView.findViewById(R.id.magnitude);
        magnitudeView.setText(formatMagnitude(currentItem.getMagnitude()));

        //  Offset location variable
        String offSetLocation = getOffSetLocation(currentItem.getPlaceName());

        //  Primary location variable
        String primaryLocation = getPrimaryLocation(currentItem.getPlaceName());

        //  Setting TextView(Place)
        TextView primaryLocationView = (TextView) listItemView.findViewById(R.id.primary_name_of_place);
        primaryLocationView.setText(primaryLocation);

        //  Setting TextView(OffSet Location)
        TextView offsetLocationView = (TextView) listItemView.findViewById(R.id.offset_name_of_place);
        offsetLocationView.setText(offSetLocation);

        //  Creating new date object to from the time in milisecond from earthquake
        Date dateObject = new Date(currentItem.getDate());


        // Formatting the dateObject into hr:min format
        String timeFormatted = formatTime(dateObject);

        //  MMM DD, yyyy format of time
        String dayFormatted = formatDay(dateObject);

        //  Setting TextView(Date);
        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        dateView.setText(dayFormatted);

        //  Setting TextView(Time);
        TextView timeView = (TextView) listItemView.findViewById(R.id.time);
        timeView.setText(timeFormatted);

        return listItemView;
    }

    /**
     * Method to convert the Double formatted mahnitude into String containing only one decimal
     * point
     * @param magnitude The magnitude of earthquake
     * @return  Formatted string with only one decimal point value
     */
    private String formatMagnitude(Double magnitude){
        DecimalFormat magnitudeFormatted = new DecimalFormat("0.0");
        return magnitudeFormatted.format(magnitude);
    }

    /**
     * Convert the Date into a String with format(4:30 am)
     * @param dateObject    Date in milisecond
     * @return              Returns a String
     */
    private String formatTime(Date dateObject){
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    private String formatDay(Date dateObject){
        SimpleDateFormat dayFormat = new SimpleDateFormat("MMM DD, yyyy");
        return dayFormat.format(dateObject);
    }

    /**
     * Method to obtain the Offset Location from the String containing Offset Location and Primary Location
     * @param location  String value containing Offset Location and Primary Location
     * @return          Returns String value containing Offset Location
     */
    private String getOffSetLocation(String location){
        String offSetPlace= "Near the";
        if(location.contains("of")){
            int index = location.indexOf("of");
            offSetPlace = location.substring(0, index+2);
        }
        return offSetPlace;
    }

    /**
     * Method to obtain the Primary location from String containing Offset Location and Primary Location
     * @param location  String value containing the Offset Location and Primary Location
     * @return          Returns String value containing the Primary Location
     */
    private String getPrimaryLocation(String location){
        String primaryLocation = "";
        if(location.contains("of")){
            int index = location.indexOf("of");
            primaryLocation = location.substring(index+3, location.length());
        }
        else{
            primaryLocation = location;
        }
        return primaryLocation;
    }
}
