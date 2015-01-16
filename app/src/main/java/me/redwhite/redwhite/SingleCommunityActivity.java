package me.redwhite.redwhite;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.redwhite.redwhite.fragments.CustomListView;
import me.redwhite.redwhite.fragments.QuestionDetailActivity;
import me.redwhite.redwhite.models.Community;
import me.redwhite.redwhite.models.Question;
import me.redwhite.redwhite.utils.MiniQuestionListAdapter;


public class SingleCommunityActivity extends Activity {

    final String[] username ={"junhong14",
            "TheUsername",
            "Marly"};
    final String[] questions ={"Do you take public transport or drive to work",
            "How often do you have dinner at home",
            "How often do you spend your weekends with your family"};
    final Integer[] imageId ={
            R.drawable.user1,
            R.drawable.user2,
            R.drawable.user2
    };
    ListView list;
    String userId;
    String questionDetail;
    Button joinBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_single_community);
        if (savedInstanceState == null) {

        }

        Bundle extras = this.getIntent().getExtras();
        final String shortname = extras.getString("shortname");

        Community.findNodeByKey(shortname, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Community c = Community.convertFromMap((Map<String,Object>) dataSnapshot.getValue());

                TextView shortName = (TextView)findViewById(R.id.tvShortName);
                TextView fullName = (TextView)findViewById(R.id.tvFullName);

                shortName.setText("#" + c.getShortname());
                fullName.setText(c.getName());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Question.findNodes(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Set up listview for the list of questions asked by the users in the community

                ArrayList<Object> arrayList = (ArrayList<Object>)dataSnapshot.getValue();
                Map<String, Object> map = new HashMap<String,Object>();

                for(Object o: arrayList)
                {
                    map.put(o.toString(), o);
                }

                final ArrayList<Question> qList = Question.convertListFromMap(map);

                MiniQuestionListAdapter adapter = new MiniQuestionListAdapter(getApplicationContext(), qList);
                list = (ListView) findViewById(R.id.listViewQuestions);
                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        userId = qList.get(position).getCreated_username();
                        questionDetail = qList.get(position).getQuestion();
                        Intent myIntent = new Intent(getApplicationContext() ,QuestionDetailActivity.class);
                        myIntent.putExtra("username", userId);
                        myIntent.putExtra("question", questionDetail);
                        startActivity(myIntent);
                    }
                });
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });




        joinBtn = (Button)findViewById(R.id.btn_join);
        joinBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (joinBtn.getText().equals("Join")) {
                            joinBtn.setText("Leave");
                            joinBtn.setBackgroundColor(Color.GRAY);

                            Community.addUserToCommunity(shortname, "ZZZ");

                        } else if (joinBtn.getText().equals("Leave")) {
                            joinBtn.setText("Join");
                            joinBtn.setBackgroundColor(Color.RED);

                            Community.removeUserFromCommunity(shortname, "ZZZ");
                        }

                    }      }
        );
                }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_single_community, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_single_community, container, false);
            return rootView;
        }
    }
}
