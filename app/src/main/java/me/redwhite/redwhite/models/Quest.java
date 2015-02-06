package me.redwhite.redwhite.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rong Kang on 2/4/2015.
 */
@Parcel
public class Quest implements FirebaseNode {
    String description;
    long end_datetime;
    String imageurl;
    String name;
    boolean order_enforced;
    String shortname;
    long start_datetime;

    ArrayList<String> questions;
    ArrayList<QuestUser> users;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getEnd_datetime() {
        return end_datetime;
    }

    public void setEnd_datetime(long end_datetime) {
        this.end_datetime = end_datetime;
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

    public boolean isOrder_enforced() {
        return order_enforced;
    }

    public void setOrder_enforced(boolean order_enforced) {
        this.order_enforced = order_enforced;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public long getStart_datetime() {
        return start_datetime;
    }

    public void setStart_datetime(long start_datetime) {
        this.start_datetime = start_datetime;
    }

    public ArrayList<String> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<String> questions) {
        this.questions = questions;
    }

    public ArrayList<QuestUser> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<QuestUser> users) {
        this.users = users;
    }

    public Quest() {
    }

    public Quest(ArrayList<QuestUser> users, String description, long end_datetime, String imageurl, String name, boolean order_enforced, String shortname, long start_datetime, ArrayList<String> questions) {
        this.users = users;
        this.description = description;
        this.end_datetime = end_datetime;
        this.imageurl = imageurl;
        this.name = name;
        this.order_enforced = order_enforced;
        this.shortname = shortname;
        this.start_datetime = start_datetime;
        this.questions = questions;
    }

    //need new constructor for Map values of users and questions

    //need convert from map option

    //write convert from maps for question and user

    public static void completeQuestQuestion(String questKey, String userKey, String questionKey) {
        Firebase ref = new Firebase(FIREBASEPATH + "quest/" +questKey +"/users/" +userKey +"/complete_questions/" );

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> m = new HashMap<String, Object>();
        m.put(questionKey, true);

        ref.updateChildren(m);
    }

    public static void listenToOnlineQuestUsers(String questKey, ValueEventListener listener) {
        Firebase ref = new Firebase(FIREBASEPATH + "quest/" +questKey +"/users/");
        ref.orderByChild("online").equalTo(true).addValueEventListener(listener);
    }
}
