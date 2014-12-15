package me.redwhite.redwhite.models;

/**
 * Created by t-rochew on 12/15/2014.
 */
public class Question {

    public String QuestionText = "\"Do you think questions are the start of something great in Singapore?\"";
    public String QuestionType = "TwoOptions";

    public Question()
    {

    }

    public Question(String type)
    {
        QuestionType = type;
    }

}
