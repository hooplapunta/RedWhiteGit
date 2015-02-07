package me.redwhite.redwhite.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rong Kang on 2/4/2015.
 */
public class QuestUser implements FirebaseNode {
    String userKey;
    boolean complete;
    long complete_datetime;
    long join_datetime;
    double lat;
    double lng;
    boolean online;

    Map<String, Boolean> complete_questions;

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

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

    public QuestUser(String userKey, boolean complete, long complete_datetime, long join_datetime, double lat, double lng, boolean online, Map<String, Boolean> complete_questions) {
        this.userKey = userKey;
        this.complete = complete;
        this.complete_datetime = complete_datetime;
        this.join_datetime = join_datetime;
        this.lat = lat;
        this.lng = lng;
        this.online = online;
        this.complete_questions = complete_questions;
    }

    public QuestUser(String userKey, boolean complete, long complete_datetime, long join_datetime, double lat, double lng, boolean online, HashMap<String, Object> complete_questions) {
        this.userKey = userKey;
        this.complete = complete;
        this.complete_datetime = complete_datetime;
        this.join_datetime = join_datetime;
        this.lat = lat;
        this.lng = lng;
        this.online = online;
        this.complete_questions = new HashMap(complete_questions);
    }

    public static QuestUser convertFromMap(String key, Map<String, Object> map)
    {
        return new QuestUser(
                key,
                (boolean)map.get("complete"),
                (int)(long)map.get("complete_datetime"),
                (int)(long)map.get("joined_datetime"),
                (double)map.get("lat"),
                (double)map.get("lng"),
                (boolean)map.get("online"),
                (HashMap<String,Object>)map.get("complete_questions")
        );
    }

    public static ArrayList<QuestUser> convertListFromMap(Map<String, Object> map)
    {
        ArrayList<QuestUser> c = new ArrayList<QuestUser>();

        for (Map.Entry<String, Object> e : map.entrySet()) {
            c.add(
                    QuestUser.convertFromMap(e.getKey(), (Map)e.getValue())
            );
        }

        return c;
    }

    public static void updateUserLocation(String questKey, String userKey, double latitude, double longitude) {
        Firebase ref = new Firebase(FIREBASEPATH + "quest/" +questKey +"/users/" +userKey);

        Map<String, Object> online = new HashMap<String, Object>();
        online.put("online", true);
        online.put("lat", latitude);
        online.put("lng", longitude);

        ref.updateChildren(online);
    }

    public int getCompletionPercentage(Quest quest) {
        return (int)(((double)complete_questions.size()/(double)quest.getQuestions().size())*100d);
    }
}
