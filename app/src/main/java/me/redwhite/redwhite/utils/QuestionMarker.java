package me.redwhite.redwhite.utils;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Rong Kang on 2/7/2015.
 */
public class QuestionMarker implements ClusterItem {
    public LatLng mPosition;
    private String title;
    private BitmapDescriptor icon;
    private String snippet;

    public LatLng getmPosition() {
        return mPosition;
    }

    public void setmPosition(LatLng mPosition) {
        this.mPosition = mPosition;
    }

    public QuestionMarker(){};

    public QuestionMarker(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BitmapDescriptor getIcon() {
        return icon;
    }

    public void setIcon(BitmapDescriptor icon) {
        this.icon = icon;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }
}
