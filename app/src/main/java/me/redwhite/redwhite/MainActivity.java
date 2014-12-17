package me.redwhite.redwhite;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;

import me.redwhite.redwhite.fragments.BrowseCommunityFragment;
import me.redwhite.redwhite.fragments.BrowseQuestionsFragment;
import me.redwhite.redwhite.fragments.NavigationDrawerFragment;
import me.redwhite.redwhite.fragments.NewsfeedFragment;
import me.redwhite.redwhite.fragments.OldNewsfeedFragment;


public class MainActivity extends Activity
        implements NewsfeedFragment.OnFragmentInteractionListener, NavigationDrawerFragment.NavigationDrawerCallbacks, OldNewsfeedFragment.OnFragmentInteractionListener, BrowseQuestionsFragment.OnFragmentInteractionListener, BrowseCommunityFragment.OnFragmentInteractionListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();

        Fragment nextFragment;

        switch (position)
        {
            case 0:
                nextFragment = NewsfeedFragment.newInstance(null, null);
                break;
            case 1:
                nextFragment = BrowseQuestionsFragment.newInstance(null, null);
                break;
            case 2:
                nextFragment = BrowseCommunityFragment.newInstance(null, null);
                break;
            case 3:
                //TODO: Update
                nextFragment = OldNewsfeedFragment.newInstance(null, null);
                break;
            case 4:
                nextFragment = OldNewsfeedFragment.newInstance(null, null);
                break;
            default:
                return;
        }

        fragmentManager.beginTransaction()
                // TODO: Update this line to navigate to defined fragments
                .replace(R.id.container, nextFragment)
                .commit();
    }

    // FRAGMENT INTERACTIONS LISTENER

    // For Newsfeed
    @Override
    public void onFragmentInteraction(Uri uri) {

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
