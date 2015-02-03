package me.redwhite.redwhite.models;

import java.util.Map;

/**
 * Created by Rong Kang on 2/4/2015.
 */
public class QuestUser implements FirebaseNode {
    boolean complete;
    long complete_datetime;
    long join_datetime;
    double lat;
    double lng;
    boolean online;

    Map<String, Boolean> complete_questions;

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public long getComplete_datetime() {
        return complete_datetime;
    }

    public void setComplete_datetime(long complete_datetime) {
        this.complete_datetime = complete_datetime;
    }

    public long getJoin_datetime() {
        return join_datetime;
    }

    public void setJoin_datetime(long join_datetime) {
        this.join_datetime = join_datetime;
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

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public Map<String, Boolean> getComplete_questions() {
        return complete_questions;
    }

    public void setComplete_questions(Map<String, Boolean> complete_questions) {
        this.complete_questions = complete_questions;
    }

    public QuestUser() {
    }

    public QuestUser(boolean complete, long complete_datetime, long join_datetime, double lat, double lng, boolean online, Map<String, Boolean> complete_questions) {
        this.complete = complete;
        this.complete_datetime = complete_datetime;
        this.join_datetime = join_datetime;
        this.lat = lat;
        this.lng = lng;
        this.online = online;
        this.complete_questions = complete_questions;
    }
}
