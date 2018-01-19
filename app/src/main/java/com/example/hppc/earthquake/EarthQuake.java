package com.example.hppc.earthquake;

class EarthQuake {
    private double mag;
    private String location;
    private long timeInMilli;
    private String url;

    EarthQuake(double mag, String location, long timeInMilli, String url){
        this.timeInMilli = timeInMilli;
        this.location = location;
        this.mag = mag;
        this.url = url;
    }

    double getMag() {
        return mag;
    }

    String getLocation() {
        return location;
    }

    long getTimeInMillis() {
        return timeInMilli;
    }

    String getUrl(){
        return url;
    }
}