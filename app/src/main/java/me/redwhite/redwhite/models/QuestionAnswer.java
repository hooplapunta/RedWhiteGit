package me.redwhite.redwhite.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firebase.client.Firebase;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Rong Kang on 1/16/2015.
 */
@Parcel
public class QuestionAnswer implements FirebaseNode{

    String answered_username;
    double lat;
    double lng;
    String question_answer;
    String response_data;
    long response_datetime;
    String question_key;

    public String getAnswered_username() {
        return answered_username;
    }

    public void setAnswered_username(String answered_username) {
        this.answered_username = answered_username;
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

    public String getQuestion_answer() {
        return question_answer;
    }

    public void setQuestion_answer(String question_answer) {
        this.question_answer = question_answer;
    }

    public String getResponse_data() {
        return response_data;
    }

    public void setResponse_data(String response_data) {
        this.response_data = response_data;
    }

    public long getResponse_datetime() {
        return response_datetime;
    }

    public void setResponse_datetime(long response_datetime) {
        this.response_datetime = response_datetime;
    }

    public String getQuestion_key() {
        return question_key;
    }

    public void setQuestion_key(String question_key) {
        this.question_key = question_key;
    }

    public QuestionAnswer(){

    }

    public QuestionAnswer(String answered_username, double lat, double lng, String question_answer, String response_data, long response_datetime, String question_key) {
        this.answered_username = answered_username;
        this.lat = lat;
        this.lng = lng;
        this.question_answer = question_answer;
        this.response_data = response_data;
        this.response_datetime = response_datetime;
        this.question_key = question_key;
    }

    public static QuestionAnswer convertFromMap(Map<String, Object> map)
    {
        return new QuestionAnswer(
                (String)map.get("answered_username"),
                (double)map.get("lat"),
                (double)map.get("lng"),
                ((String)map.get("question_answer")),
                (String)map.get("response_data"),
                (long)map.get("response_datetime"),
                (String)map.get("question")
        );
    }

    public static void findNodes(ValueEventListener listener) {
        Firebase ref = new Firebase(FIREBASEPATH + "question_answer");
        ref.addListenerForSingleValueEvent(listener);
    }

    public static void findNodeByKey(String key, ValueEventListener listener) {
        Firebase ref = new Firebase(FIREBASEPATH + "question_answer/" + key);
        ref.addListenerForSingleValueEvent(listener);
    }

    public static String addNode(QuestionAnswer qa) {
        String key = null;

        Firebase ref = new Firebase(FIREBASEPATH + "question_answer/");

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> m = mapper.convertValue(qa, Map.class);

        Firebase newRef = ref.push();
        newRef.setValue(m);

        qa.setQuestion_answer(newRef.getKey());
        editNode(qa);

        return newRef.getKey();
    }

    public static String editNode(QuestionAnswer qa) {
        String key = null;

        Firebase ref = new Firebase(FIREBASEPATH + "question_answer/" +qa.getQuestion_answer());

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> m = mapper.convertValue(qa, Map.class);

        ref.updateChildren(m);

        return qa.getQuestion_answer();
    }
}
