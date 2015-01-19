package me.redwhite.redwhite.models;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Rong Kang on 1/13/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements FirebaseNode{

    String key;
    String dob;
    String email;
    String gender;
    String name;
    int points;
    String postalcode;

    ArrayList<String> _communities_joined = new ArrayList<String>();
    ArrayList<String> _questions_answered = new ArrayList<String>();
    ArrayList<String> _questions_created = new ArrayList<String>();

    // GETTERS AND SETTERS


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public ArrayList<String> get_communities_joined() {
        return _communities_joined;
    }

    public void set_communities_joined(ArrayList<String> _communities_joined) {
        this._communities_joined = _communities_joined;
    }

    public ArrayList<String> get_questions_answered() {
        return _questions_answered;
    }

    public void set_questions_answered(ArrayList<String> _questions_answered) {
        this._questions_answered = _questions_answered;
    }

    public ArrayList<String> get_questions_created() {
        return _questions_created;
    }

    public void set_questions_created(ArrayList<String> _questions_created) {
        this._questions_created = _questions_created;
    }

    // CONSTRUCTORS

    public User() {
    }

    public User(String key, String dob, String email, String gender, String name, int points, String postalcode, ArrayList<String> _communities_joined, ArrayList<String> _questions_answered, ArrayList<String> _questions_created) {
        this.key = key;
        this.dob = dob;
        this.email = email;
        this.gender = gender;
        this.name = name;
        this.points = points;
        this.postalcode = postalcode;
        this._communities_joined = _communities_joined;
        this._questions_answered = _questions_answered;
        this._questions_created = _questions_created;
    }

    // TODO: update when key for question and question_answer changes
    public User(String key, String dob, String email, String gender, String name, int points, String postalcode, Map<String, Object> _communities_joined, ArrayList<Object> _questions_answered, ArrayList<Object> _questions_created) {
        this.key = key;
        this.dob = dob;
        this.email = email;
        this.gender = gender;
        this.name = name;
        this.points = points;
        this.postalcode = postalcode;

        for (Map.Entry<String, Object> e : _communities_joined.entrySet()) {
            this._communities_joined.add(e.getKey());
        }

        for (int i = 0; i < _questions_answered.size(); i ++) {
            Object e = _questions_answered.get(i);

            if(e != null) {
                this._questions_answered.add(Integer.toString(i));
            }
        }

        for (int i = 0; i < _questions_created.size(); i ++) {
            Object e = _questions_created.get(i);

            if(e != null) {
                this._questions_created.add(Integer.toString(i));
            }
        }
    }

    // FIREBASE METHODS

    public static void findNodeByKey(String key, ValueEventListener listener) {
        Firebase ref = new Firebase(FIREBASEPATH + "user/" + key);
        ref.addListenerForSingleValueEvent(listener);
    }

    public static User convertFromMap(Map<String, Object> map) {
        return new User(
                "???",
                (String)map.get("dob"),
                (String)map.get("email"),
                (String)map.get("gender"),
                (String)map.get("name"),
                (int)map.get("points"),
                (String)map.get("postalcode"),
                (Map<String, Object>)map.get("communities_joined"),
                (ArrayList<Object>)map.get("questions_answered"),
                (ArrayList<Object>)map.get("questions_created")
        );
    }

    public static ArrayList<User> convertListFromMap(Map<String, Object> map)
    {
        ArrayList<User> c = new ArrayList<User>();

        for (Map.Entry<String, Object> e : map.entrySet()) {
            c.add(
                    User.convertFromMap((Map)e.getValue())
            );
        }

        return c;
    }
}
