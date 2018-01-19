package com.example.hppc.earthquake;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;

public class UserAdapter extends ArrayAdapter<EarthQuake>{

    UserAdapter(@NonNull Context context, ArrayList<EarthQuake> earthQuakes) {
        super(context,0, earthQuakes);

    }

    @NonNull
    @Override
    public View getView(int position, View convertView,@NonNull ViewGroup parent){
        //get the data item for this position
        EarthQuake earthQuake = getItem(position);

        //check if an existing view is being reused, otherwise inflate the view
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user,parent, false);
        }
        //Lock view for data population
        TextView tvMag = (TextView) convertView.findViewById(R.id.tvMag);
        TextView tvLocation = (TextView) convertView.findViewById(R.id.tvLoc);
        TextView tvLocPrefix = (TextView) convertView.findViewById(R.id.tvLocPrefix);
        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
        TextView tvtime = (TextView) convertView.findViewById(R.id.tvTime);

        //set things up...
        String location = earthQuake.getLocation();
        String prefixLocation="";
        if(location.contains("of")){
            int indexOfOF = location.indexOf("of ");
            prefixLocation = location.substring(0,indexOfOF+3);
            location = location.substring(indexOfOF+3);
        }else{
            prefixLocation = "Near the";
        }

        long unixTime = earthQuake.getTimeInMillis();

        //Populate the data into the template view using the data object
        tvMag.setText(decimalFormatter(earthQuake.getMag()));
        tvLocPrefix.setText(prefixLocation);
        tvLocation.setText(location);
        tvDate.setText(formatDate(unixTime));
        tvtime.setText(formatTIme(unixTime));

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) tvMag.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColorID((int) earthQuake.getMag());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);
        return convertView;

    }

    /**
     * Helper method to get the correct color code for the magnitude text view background
     * @param mag floor value of the magnitude of the earthquake magnitude.
     * @return correct color code id.
     * */
    private int getMagnitudeColorID(int mag) {
        switch(mag+1){
            case 1: return ContextCompat.getColor(getContext(),R.color.magnitude1);
            case 2: return ContextCompat.getColor(getContext(),R.color.magnitude2);
            case 3: return ContextCompat.getColor(getContext(),R.color.magnitude3);
            case 4: return ContextCompat.getColor(getContext(),R.color.magnitude4);
            case 5: return ContextCompat.getColor(getContext(),R.color.magnitude5);
            case 6: return ContextCompat.getColor(getContext(),R.color.magnitude6);
            case 7: return ContextCompat.getColor(getContext(),R.color.magnitude7);
            case 8: return ContextCompat.getColor(getContext(),R.color.magnitude8);
            case 9: return ContextCompat.getColor(getContext(),R.color.magnitude9);
        }
        return ContextCompat.getColor(getContext(),R.color.magnitude10plus);
    }

    /**
     * Converts the given value to a specified format
     * @param mag earthquake magnitude
     * @return 0.00 pattern
     * */
    private String decimalFormatter(double mag) {
        DecimalFormat decFormatter = new DecimalFormat("0.00");
        return decFormatter.format(mag);
    }

    /**
     * Converts given unix format time tp Time String in specified format
     * @param timeInMillies unix format time provided by the JSON source
     * @return Time as String
     * */
    private String formatTIme(long timeInMillies) {
        Date dateObject = new Date(timeInMillies);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    /**
     * Converts given unix time into Date String in specified format
     * @param timeInMillies unix format time provided by the JSON source
     * @return Date as String
     * */
    private String formatDate(long timeInMillies) {
        Date dateObject = new Date(timeInMillies);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, ''yy");
        return dateFormatter.format(dateObject);
    }
}
