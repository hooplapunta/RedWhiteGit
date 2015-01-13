package me.redwhite.redwhite.models;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Rong Kang on 1/13/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements FirebaseNode{

    public String key;

    //public LinkedHashMap<String, Object> communities_joined;
    public String dob;
    public String gender;
    public String name;
    public int points;
    public String postalcode;
    //public LinkedHashMap<String, Object> questions_answered;
    //public LinkedHashMap<String, Object> questions_created;

    // GETTERS AND SETTERS

//    public LinkedHashMap<String, Object> getCommunities_joined() {
//        return communities_joined;
//    }
//
//    public void setCommunities_joined(LinkedHashMap<String, Object> communities_joined) {
//        this.communities_joined = communities_joined;
//    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
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

//    public LinkedHashMap<String, Object> getQuestions_answered() {
//        return questions_answered;
//    }
//
//    public void setQuestions_answered(LinkedHashMap<String, Object> questions_answered) {
//        this.questions_answered = questions_answered;
//    }
//
//    public Map<String, Object> getQuestions_created() {
//        return questions_created;
//    }
//
//    public void setQuestions_created(LinkedHashMap<String, Object> questions_created) {
//        this.questions_created = questions_created;
//    }


    // CONSTRUCTORS

    public User() {
    }

    public User(LinkedHashMap<String, Object> communities_joined, String dob, String gender, String name, int points, String postalcode, LinkedHashMap<String, Object> questions_answered, LinkedHashMap<String, Object> questions_created) {
//        this.communities_joined = communities_joined;
        this.dob = dob;
        this.gender = gender;
        this.name = name;
        this.points = points;
        this.postalcode = postalcode;
//        this.questions_answered = questions_answered;
//        this.questions_created = questions_created;
    }

    @Override
    public String toString() {
        return "USER " +this.key +", " +this.name;
    }

    // FIREBASE METHODS

    public static User findNodeByKey(String key) {
        Firebase ref = new Firebase(FIREBASEPATH + "user/" + key);

        final User[] node = {null};

        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("USER FOUND", dataSnapshot.getValue(User.class).toString());

                node[0] = dataSnapshot.getValue(User.class);
                node[0].key = dataSnapshot.getKey();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return node[0];
    }

    public static void findNodeByKey(String key, ValueEventListener listener) {
        Firebase ref = new Firebase(FIREBASEPATH + "user/" + key);
        ref.addListenerForSingleValueEvent(listener);
    }
}
