package me.redwhite.redwhite.models;

import com.google.android.gms.maps.model.LatLng;

import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel
public class QuestionLocation {
    ArrayList<LatLng> geofence = new ArrayList<LatLng>();
    double lat;
    double lng;
    String name;
    int proximity;
    boolean trigger;

    public ArrayList<LatLng> getGeofence() {
        return geofence;
    }

    public void setGeofence(ArrayList<LatLng> geofence) {
        this.geofence = geofence;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProximity() {
        return proximity;
    }

    public void setProximity(int proximity) {
        this.proximity = proximity;
    }

    public boolean isTrigger() {
        return trigger;
    }

    public void setTrigger(boolean trigger) {
        this.trigger = trigger;
    }

    public QuestionLocation() {

    }

    public QuestionLocation(ArrayList<LatLng> geofence, double lat, double lng, String name, int proximity, boolean trigger) {
        this.geofence = geofence;
        this.lat = lat;
        this.lng = lng;
        this.name = name;
        this.proximity = proximity;
        this.trigger = trigger;
    }

    public QuestionLocation(String geofence, double lat, double lng, String name, int proximity, boolean trigger) {
        String[] points = geofence.split(";");

        this.geofence = new ArrayList<LatLng>();

        for(String p: points)
        {
            String[] ll = p.split(",");

            this.geofence.add(new LatLng(Double.parseDouble(ll[0]), Double.parseDouble(ll[1])));
        }

        this.lat = lat;
        this.lng = lng;
        this.name = name;
        this.proximity = proximity;
        this.trigger = trigger;
    }
}