package me.redwhite.redwhite.models;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Rong Kang on 1/16/2015.
 */
public class QuestionAnswer {

    String answered_username;
    double lat;
    double lng;
    String question_answer;
    String response_data;
    String response_datetime;

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

    public String getResponse_datetime() {
        return response_datetime;
    }

    public void setResponse_datetime(String response_datetime) {
        this.response_datetime = response_datetime;
    }

    public QuestionAnswer(){

    }

    public QuestionAnswer(String answered_username, double lat, double lng, String question_answer, String response_data, String response_datetime) {
        this.answered_username = answered_username;
        this.lat = lat;
        this.lng = lng;
        this.question_answer = question_answer;
        this.response_data = response_data;
        this.response_datetime = response_datetime;
    }

    public static QuestionAnswer convertFromMap(Map<String, Object> map)
    {
        return new QuestionAnswer(
                (String)map.get("answered_username"),
                (double)map.get("lat"),
                (double)map.get("lng"),
                (Long.toString((Long)map.get("question_answer"))),
                (String)map.get("response_data"),
                (String)map.get("response_datetime")
        );
    }
}
