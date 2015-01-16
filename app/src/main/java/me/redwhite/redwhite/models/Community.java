package me.redwhite.redwhite.models;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    ArrayList<QuestionStatus> _questions = new ArrayList<QuestionStatus>();
    ArrayList<String> _users = new ArrayList<String>();

    public class QuestionStatus
    {
        String question;
        boolean active;

        public QuestionStatus() {
        }

        public QuestionStatus(String question, boolean active) {
            this.question = question;
            this.active = active;
        }
    }

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

    public ArrayList<QuestionStatus> get_questions() {
        return _questions;
    }

    public void set_questions(ArrayList<QuestionStatus> _questions) {
        this._questions = _questions;
    }

    public ArrayList<String> get_users() {
        return _users;
    }

    public void set_users(ArrayList<String> _users) {
        this._users = _users;
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

    public Community(String description, String imageurl, String name, String shortname, boolean temp, Map<String, Object> questions, Map<String, Object> users) {
        this.description = description;
        this.imageurl = imageurl;
        this.name = name;
        this.shortname = shortname;
        this.temp = temp;

        for (Map.Entry<String, Object> e : questions.entrySet()) {
            _questions.add(new QuestionStatus(e.getKey(), (boolean)e.getValue()));
        }

        for (Map.Entry<String, Object> e : users.entrySet()) {
            _questions.add(new QuestionStatus(e.getKey(), (boolean)e.getValue()));
        }
    }

    public Community(String description, String imageurl, String name, String shortname, boolean temp, ArrayList<Object> questions, Map<String, Object> users) {
        this.description = description;
        this.imageurl = imageurl;
        this.name = name;
        this.shortname = shortname;
        this.temp = temp;
        this._questions = new ArrayList<QuestionStatus>();
        this._users = new ArrayList<String>();

        for (Object e : questions) {
            if(e != null) {
                this._questions.add(new QuestionStatus("???", (boolean) e));
            }
        }

        for (Map.Entry<String, Object> e : users.entrySet()) {
            this._users.add(e.getKey());
        }
    }

    public static Community convertFromMap(Map<String, Object> map)
    {
        return new Community(
                (String)map.get("description"),
                (String)map.get("imageurl"),
                (String)map.get("name"),
                (String)map.get("shortname"),
                (boolean)map.get("temp"),
                (ArrayList<Object>)map.get("questions"),
                (Map<String, Object>)map.get("users")
        );
    }

    public static ArrayList<Community> convertListFromMap(Map<String, Object> map)
    {
        ArrayList<Community> c = new ArrayList<Community>();

        for (Map.Entry<String, Object> e : map.entrySet()) {
            c.add(
                    Community.convertFromMap((Map)e.getValue())
            );
        }

        return c;
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
