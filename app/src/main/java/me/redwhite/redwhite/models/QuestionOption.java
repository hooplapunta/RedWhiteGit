package me.redwhite.redwhite.models;

import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel
public class QuestionOption {
    String key;
    ArrayList<QuestionAnswer> _answers;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ArrayList<QuestionAnswer> get_answers() {
        return _answers;
    }

    public void set_answers(ArrayList<QuestionAnswer> _answers) {
        this._answers = _answers;
    }

    public QuestionOption() {

    }

    public QuestionOption(String key, ArrayList<QuestionAnswer> _answers) {
        this.key = key;
        this._answers = _answers;
    }

    public int countLastHour() {
        int count = 0;

        for(QuestionAnswer qa : _answers) {
            if (((System.currentTimeMillis()/1000) - qa.getResponse_datetime()) <= 3600) {
                count++;
            }
        }

        return count;
    }
}