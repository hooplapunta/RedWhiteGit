package me.redwhite.redwhite;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import me.redwhite.redwhite.fragments.BrowseCommunityFragment;
import me.redwhite.redwhite.fragments.BrowseQuestionsFragment;
import me.redwhite.redwhite.fragments.BrowseQuestionsListFragment;
import me.redwhite.redwhite.fragments.DirectionsInfoFragment;
import me.redwhite.redwhite.fragments.NavigationDrawerFragment;
import me.redwhite.redwhite.fragments.NewsfeedFragment;
import me.redwhite.redwhite.fragments.OldNewsfeedFragment;
import me.redwhite.redwhite.fragments.QuestDetailFragment;
import me.redwhite.redwhite.fragments.QuestInfoFragment;
import me.redwhite.redwhite.fragments.QuestWebFragment;
import me.redwhite.redwhite.fragments.QuestionDetailActivity;
import me.redwhite.redwhite.fragments.SingleQuestionFragment;
import me.redwhite.redwhite.models.Quest;
import me.redwhite.redwhite.models.Question;
import me.redwhite.redwhite.models.User;


public class MainActivity extends FragmentActivity
        implements NewsfeedFragment.OnFragmentInteractionListener, NavigationDrawerFragment.NavigationDrawerCallbacks, OldNewsfeedFragment.OnFragmentInteractionListener, BrowseQuestionsFragment.OnFragmentInteractionListener, BrowseCommunityFragment.OnFragmentInteractionListener, BrowseQuestionsListFragment.OnFragmentInteractionListener, SingleQuestionFragment.OnFragmentInteractionListener, QuestDetailFragment.OnFragmentInteractionListener, QuestWebFragment.OnFragmentInteractionListener, QuestInfoFragment.OnFragmentInteractionListener, DirectionsInfoFragment.OnFragmentInteractionListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    /**
     * Currently logged in user in the application session
     */
    public User loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO: Come up with method to show a splashscreen with animation within the activity_main layout
        setContentView(R.layout.activity_main);

        // Ready Firebase
        Firebase.setAndroidContext(this);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Check for a logged in user
        loadLoggedInUser();

        // loadLoggedInUser handles the rest of the loading of the activity
    }

    private void loadActivityOnAuth() {
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        mNavigationDrawerFragment.selectItem(0);
    }

    /**
     * Retrieve the logged in user from shared preferences
     * @return
     */
    private void loadLoggedInUser() {

        //TODO: Check for saved user in shared preferences key?
        if(/*User.wasLoggedIn()*/ false)
        {
            // reload from sharedprefs

            loadActivityOnAuth();
            Toast.makeText(getApplicationContext(), User.getAuth().toString(), Toast.LENGTH_LONG).show();

        } else {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            final String username = sp.getString("example_text", "bam").toLowerCase();

            // TODO: reload username and password from shared preference
            User.loginToFirebase(username+"@hotmail.com", "password", new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authData) {

                    User.findNodeByKey(username.toUpperCase(), new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot != null) {
                                User user;

                                // restore the user into activity
                                user = dataSnapshot.getValue(User.class);
                                user.setKey(dataSnapshot.getKey());
                                loggedInUser = user;

                                // call the activity to continue loading
                                loadActivityOnAuth();
                                Toast.makeText(getApplicationContext(), "Logged in as " +user.getKey(), Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                // Login error, show the login screen
                                Intent myIntent = new Intent(MainActivity.this, NewUserActivity.class);
                                myIntent.putExtra("loginError", true);
                                startActivity(myIntent);
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }

                @Override
                public void onAuthenticationError(FirebaseError firebaseError) {
                    // Login error, show the login screen
                    Intent myIntent = new Intent(MainActivity.this, NewUserActivity.class);
                    myIntent.putExtra("loginError", true);
                    startActivity(myIntent);
                }
            });
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();

        Fragment nextFragment = null;

        boolean goToFragment = true;
        String tag = "";

        switch (position)
        {
            case 0:
                nextFragment = NewsfeedFragment.newInstance(null, null);
                break;
            case 1:
                nextFragment = BrowseQuestionsListFragment.newInstance(null, null);
                break;
            case 2:
                nextFragment = BrowseCommunityFragment.newInstance(null, null);
                break;
            case 3:
                //TODO: Update
                nextFragment = QuestDetailFragment.newInstance(null, null);
                tag = "QuestDetailFragment";
                break;
            case 4:
                nextFragment = QuestWebFragment.newInstance(null, null);
                break;
            case 5:
                goToFragment = false;
                Intent myIntent = new Intent(getApplicationContext(),SettingsActivity.class);
                startActivity(myIntent);
                break;
            default:
                return;
        }

        if(goToFragment)
        {
            fragmentManager.beginTransaction()
                    // TODO: Update this line to navigate to defined fragments
                    .replace(R.id.container, nextFragment, tag)
                    .commit();
        }
    }

    // FRAGMENT INTERACTIONS LISTENER

    // For Newsfeed
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void completeQuestion(Question q, User u, boolean isComplete, boolean isQuest, Quest qu) {
        QuestDetailFragment fragment = (QuestDetailFragment) getFragmentManager().findFragmentByTag("QuestDetailFragment");
        fragment.completeQuestion(q, u, isComplete, isQuest, qu);
    }

    // For others
    @Override
    public void onFragmentInteraction(String id) {

    }



    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = "Newsfeed";
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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
     * THIS IS DEMO CODE. CAN IGNORE.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
