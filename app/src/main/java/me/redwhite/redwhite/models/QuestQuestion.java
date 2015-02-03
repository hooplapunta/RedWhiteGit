package me.redwhite.redwhite.models;

/**
 * Created by Rong Kang on 2/4/2015.
 */
public class QuestQuestion implements FirebaseNode {
    String key;
    double lat;
    double lng;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public QuestQuestion() {
    }

    public QuestQuestion(String key, double lat, double lng) {
        this.key = key;
        this.lat = lat;
        this.lng = lng;
    }
}
