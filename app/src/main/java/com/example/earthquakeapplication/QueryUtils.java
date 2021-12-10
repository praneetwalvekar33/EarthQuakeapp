package com.example.earthquakeapplication;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

public class QueryUtils {


    /** LOG_TAG for the log message */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Creating a private constructor because the class contains static method ans variables which
     * can accessed only by class name
     */
    private QueryUtils(){

    }

    /**
     * This method can be called by other Classes to retrive the EarthQuake Information
     * @return  ArrayList of InformationList Object
     */
    public static ArrayList<InformationList> fetchArrayListData(String requestUrl){

        URL url = createUrl(requestUrl);

        //  Throws an IOException while calling the makeHttpUrlConnection Method.
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpsUrlConnection(url);
        }catch(IOException e){
            Log.e(LOG_TAG,"Error while closing the input stream" + e);
        }

        ArrayList<InformationList> output = extractInformationOfEarthQuake(jsonResponse);
        return output;
    }

    /**
     * This method creates a URL from a String
     * @param    stringUrl String
     * @return   URL
     */
    private static URL createUrl(String stringUrl){
        URL url = null;

        //  Creating a URL from a String variable
        try{
            url = new URL(stringUrl);
        } catch(MalformedURLException e){
            Log.e(LOG_TAG, "Error Creating URL" + e);
        }

        return url;
    }

    /**
     * This method setup a HttpsURLConnection with the URL
     * @param   url URL Format
     * @return  JSON Response String Format
     * @throws  IOException if url does not give appropriate input
     */
    private static String makeHttpsUrlConnection(URL url) throws IOException {
        String jsonResponse = "";

        HttpsURLConnection urlConnection = null;
        InputStream inputStream = null;
        // Connecting to the URL
        try{
            urlConnection = (HttpsURLConnection) url.openConnection();

            // Create SSL connection
            SSLContext sc;
            sc = SSLContext.getInstance("TLS");
            sc.init(null, null, new java.security.SecureRandom());
            urlConnection.setSSLSocketFactory(sc.getSocketFactory());

            // Setting the HttpsURLConnection parameter
            urlConnection.setReadTimeout(10000 /* millisecond */);
            urlConnection.setConnectTimeout(15000/* millisecond */);
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();


            //  Checking response code to store the data from URL
            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = fromInputStream(inputStream);
            }else{
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        }catch(IOException e){
            Log.e(LOG_TAG, "Error while retrieving the JSON response:" + e);
        }catch(NoSuchAlgorithmException e) {
            Log.e(LOG_TAG, "Error while retrieving the JSON response:" + e);
        }catch(KeyManagementException e){
            Log.e(LOG_TAG, "Error while retrieving the JSON response:" + e);
        }finally{
            if(urlConnection != null){
                urlConnection.disconnect();
            }

            if(inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * This method takes the InputStream reads and add it to the StringBuilder
     * @param   inputStream InputStream Format
     * @return  String of JSON Response
     * @throws IOException
     */
    private static String fromInputStream(InputStream inputStream) throws IOException{
        StringBuilder outputString =  new StringBuilder();

        // Check the InputStream is not null before adding it to the StringBuilder
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while(line != null){
                outputString.append(line);
                line = bufferedReader.readLine();
            }
        }
        return outputString.toString();
    }

    /**
     * This method takes the JSON format data creates a InformationList object and it a String
     * @param    jsonResponse String containing JSON Response
     * @return   ArrayList of InformationList Object
     */
    private static ArrayList<InformationList> extractInformationOfEarthQuake(String jsonResponse){

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(jsonResponse)) {
            Log.e(LOG_TAG, "JSON resonse is empty");
            return null;
        }

        //  Creating a empty ArrayList object to store the information
        ArrayList<InformationList> earthQuakeInformation = new ArrayList<>();

        //  Accessing the JSONObject inside try block to handle any exception thrown because of
        //  unknown JSON format
        try{
            //  Storing the base JSONObject in JSONObject variable
            JSONObject baseJsonObject = new JSONObject(jsonResponse);

            //  Storing the  JSONArray with key "features"in the variable
            JSONArray featuresArray = baseJsonObject.optJSONArray("features");

            //  Accessing each element of the JSONarray through for loop
            for(int i = 0; i < featuresArray.length(); i++){

                // Storing object present at each index i
                JSONObject currentInformation = featuresArray.getJSONObject(i);

                // Storing the properties object inside the currentinformation
                JSONObject propertiesObject = currentInformation.getJSONObject("properties");

                //  Storing the object with key "mag" in variable
                Double magnitude = propertiesObject.getDouble("mag");

                //  Storing the Object with key "place" in variable
                String place = propertiesObject.getString("place");

                //  Storing the Object with key "time" in variable
                long time = propertiesObject.getLong("time");

                // Stroing the object with key "url" in variable
                String url = propertiesObject.getString("url");

                //  Creating the InformationList Object from the properties collected from JSON
                //  String
                InformationList information = new InformationList(magnitude, place, time, url);

                //  Adding the object the earthQuakeInformation ArrayList
                earthQuakeInformation.add(information);
            }

        }
        catch(JSONException e){
            //  If an error is encountered while executing the code in the try block
            //  catch the exception over here so that the app does not crash and print a log
            //  message with the message in the exception
            Log.e("QueryUtils","Problem Parsing the EarthQuake JSON result", e);
        }
        return earthQuakeInformation;

    }

}
