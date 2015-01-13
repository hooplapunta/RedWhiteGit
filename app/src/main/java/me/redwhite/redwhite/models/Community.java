package me.redwhite.redwhite.models;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rong Kang on 1/13/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Community implements FirebaseNode{
    String description;
    String imageurl;
    String name;
    String shortname;
    boolean temp;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public boolean isTemp() {
        return temp;
    }

    public void setTemp(boolean temp) {
        this.temp = temp;
    }

    public Community() {
    }

    public Community(String description, String imageurl, String name, String shortname, boolean temp) {
        this.description = description;
        this.imageurl = imageurl;
        this.name = name;
        this.shortname = shortname;
        this.temp = temp;
    }

    public static void findNodes(ValueEventListener listener) {
        Firebase ref = new Firebase(FIREBASEPATH + "community");
        ref.addListenerForSingleValueEvent(listener);
    }

    public static void findNodeByKey(String key, ValueEventListener listener) {
        Firebase ref = new Firebase(FIREBASEPATH + "community/" + key);
        ref.addListenerForSingleValueEvent(listener);
    }

    public static void addUserToCommunity(String key, String user)
    {
        Firebase ref = new Firebase(FIREBASEPATH + "community/" + key).child("users");
        Map<String, Object> users = new HashMap<String, Object>();
        users.put("user", true);
        ref.updateChildren(users);
    }

    public static void removeUserFromCommunity(String key, String user)
    {
        Firebase ref = new Firebase(FIREBASEPATH + "community/" + key).child("users").child(user);
        ref.removeValue();
    }
}
