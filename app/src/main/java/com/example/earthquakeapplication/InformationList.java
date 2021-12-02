package com.example.earthquakeapplication;

public class InformationList {

    // Varibles for magnitude, place name and date
    private final Double magnitude;
    private final String place, url;
    private final long date;

    /**
     * Constructor to create instance of InformationList class
     * @param magnitude Gives magnitude of an earthquake
     * @param place     Gives the name of the place where earthquake happened
     * @param date      Gives the date at which earthquake happened
     */
    public InformationList(Double magnitude, String place, Long date, String url){
        this.magnitude = magnitude;
        this.place = place;
        this.date = date;
        this.url = url;
    }

    /**
     * Getter method to get value of the magnitude variable
     * @return  magnitude
     */
    public Double getMagnitude(){
        return this.magnitude;
    }

    /**
     * Getter method to get the value of place variable
     * @return  place
     */
    public String getPlaceName() {
        return this.place;
    }

    /**
     * Getter method to get the value of date variable
     * @return  date
     */
    public Long getDate(){
        return this.date;
    }

    /**
     * Getter method to get value of url variable
     * @return  url
     */
    public String getUrl(){
        return this.url;
    }
}
