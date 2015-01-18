package me.redwhite.redwhite;

import android.app.Activity;
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
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import me.redwhite.redwhite.models.Question;
import me.redwhite.redwhite.models.User;
import me.redwhite.redwhite.utils.MiniQuestionListAdapter;
import me.redwhite.redwhite.utils.ProfileQuestionListAdapter;


public class SingleProfileActivity extends Activity {

    ListView plist;
    String userId;
    String questionDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_profile);

        Firebase.setAndroidContext(getApplicationContext());
    /*    if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }*/

        Question.findNodes(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Object> arrayList = (ArrayList<Object>)dataSnapshot.getValue();

                Map<String, Object> map = new HashMap<String,Object>();

                for(Object o: arrayList)
                {
                    if(o != null) {
                        map.put(o.toString(), o);
                    }
                }
                final ArrayList<Question> listViewQuestions = Question.convertListFromMap(map);
                ProfileQuestionListAdapter adapter = new ProfileQuestionListAdapter(SingleProfileActivity.this,listViewQuestions);
                plist = (ListView)findViewById(R.id.listViewQuestions);
                plist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        userId = listViewQuestions.get(position).getCreated_username();
                        questionDetail = listViewQuestions.get(position).getQuestion();

                    }
                });
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_single_profile, menu);
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
            View rootView = inflater.inflate(R.layout.fragment_single_profile, container, false);
            return rootView;
        }
    }
}
