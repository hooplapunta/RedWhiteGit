package me.redwhite.redwhite.fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.redwhite.redwhite.R;


public class QuestionDetailActivity extends Activity {
    TextView welcome;
    TextView questionTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);

        LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayoutCard);
        LinearLayout.LayoutParams buttonMargins = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonMargins.setMargins(0, 16, 0, 16);
        questionTxt = (TextView) findViewById(R.id.tvQuestionText);

        Bundle extras = this.getIntent().getExtras();
        String usernameFromMain = extras.getString("username");
        String questionFromMain = extras.getString("question");

        ActionBar actionBar = getActionBar();
        actionBar.setTitle(usernameFromMain+"'s Activity");

        questionTxt.setText(questionFromMain);

        TextView tv = new TextView(this);
        tv.setText(usernameFromMain+ "'s answer:");
        layout.addView(tv);

        Button option1 = new Button(this);
        option1.setText("Yes, questions are awesome!");
        option1.setBackgroundColor(Color.parseColor("#F44336"));
        option1.setTextColor(Color.WHITE);
        option1.setElevation(2);
        option1.setLayoutParams(buttonMargins);

        layout.addView(option1);

        LinearLayout.LayoutParams tvMargins = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        tvMargins.setMargins(0, 24, 0, 16);

        TextView yourtv = new TextView(this);
        yourtv.setText("Add your answer:");
        yourtv.setLayoutParams(tvMargins);


        layout.addView(yourtv);

        Button option3 = new Button(this);
        option3.setText("Yes, questions are awesome!");
        option3.setBackgroundColor(Color.parseColor("#F44336"));
        option3.setTextColor(Color.WHITE);
        option3.setElevation(2);
        option3.setLayoutParams(buttonMargins);

        layout.addView(option3);

        Button option2 = new Button(this);
        option2.setText("No, I think we can do better than just questions.");
        option2.setBackgroundColor(Color.parseColor("#B0BEC5"));
        option2.setTextColor(Color.BLACK);
        option2.setElevation(2);
        option2.setLayoutParams(buttonMargins);
        layout.addView(option2);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_question_detail, menu);
        return true;




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
