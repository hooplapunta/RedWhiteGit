package me.redwhite.redwhite.utils;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;

import java.util.List;

import me.redwhite.redwhite.R;
import me.redwhite.redwhite.models.Question;

/**
 * Created by t-rochew on 12/15/2014.
 */
public class QuestionsListAdapter extends ArrayAdapter<Question> {

    private final Activity context;
    private final List<Question> questions;

    public QuestionsListAdapter(Context context, int resource, List<Question> objects) {
        super(context, R.layout.list_browse_questions, objects);

        this.context = (Activity) context;
        this.questions = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View v = inflater.inflate(R.layout.list_browse_questions, null, true);

        LinearLayout layout = (LinearLayout) v.findViewById(R.id.linearLayoutCard);
        LinearLayout.LayoutParams buttonMargins = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonMargins.setMargins(0, 16, 0, 16);

        // Change question display based on type
        Question q = questions.get(position);
        switch(questions.get(position).getType())
        {
            case "twooption":

                // option 1 and option 2
                Question.QuestionOption o1 = q.get_options().get(0);
                if (o1 != null) {
                    Button option1 = new Button(context);
                    option1.setText(o1.getKey());
                    option1.setBackgroundColor(Color.parseColor("#F44336"));
                    option1.setTextColor(Color.WHITE);
                    option1.setElevation(2);
                    option1.setLayoutParams(buttonMargins);

                    layout.addView(option1);
                }

                Question.QuestionOption o2 = q.get_options().get(0);
                if (o1 != null) {
                    Button option2 = new Button(context);
                    option2.setText("No, I think we can do better than just questions.");
                    option2.setBackgroundColor(Color.parseColor("#ECEFF1"));
                    option2.setTextColor(Color.BLACK);
                    option2.setElevation(2);
                    option2.setLayoutParams(buttonMargins);
                    layout.addView(option2);
                }
                break;
            case "completeoption":
                TextView tv = new TextView(context);
                tv.setText("Your answer:");
                layout.addView(tv);

                Button myoption = new Button(context);
                myoption.setText("Yes, questions are awesome!");
                myoption.setBackgroundColor(Color.parseColor("#F44336"));
                myoption.setTextColor(Color.WHITE);
                myoption.setElevation(2);
                myoption.setLayoutParams(buttonMargins);
                layout.addView(myoption);

                Button analysis = new Button(context);
                analysis.setText("View community answers");
                analysis.setBackgroundColor(Color.parseColor("#2196F3"));
                analysis.setTextColor(Color.WHITE);
                analysis.setTextSize(10);
                analysis.setElevation(2);
                analysis.setLayoutParams(buttonMargins);
                layout.addView(analysis);

                break;
            case "CompleteTwoOptions":

                break;
            case "fuzzytext":
                EditText text = new EditText(context);
                text.setHint("Enter the corect answer...");
                text.setElevation(2);
                text.setLayoutParams(buttonMargins);

                layout.addView(text);
                break;

            case "photo":
                Button camerabutton = new Button(context);
                camerabutton.setText("Take a photo");
                camerabutton.setBackgroundColor(Color.parseColor("#F44336"));
                camerabutton.setTextColor(Color.WHITE);
                camerabutton.setElevation(2);
                camerabutton.setLayoutParams(buttonMargins);

                layout.addView(camerabutton);
                break;

            default:
                break;
        }


        TextView tvQuestionText = (TextView) v.findViewById(R.id.tvQuestionText);
        tvQuestionText.setText(questions.get(position).getQuestion());

        return v;
    }
}
