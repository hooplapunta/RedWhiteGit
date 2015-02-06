package me.redwhite.redwhite.fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.redwhite.redwhite.R;
import me.redwhite.redwhite.models.Community;
import me.redwhite.redwhite.models.Quest;
import me.redwhite.redwhite.models.QuestQuestion;
import me.redwhite.redwhite.models.QuestUser;
import me.redwhite.redwhite.models.Question;
import me.redwhite.redwhite.models.User;
import me.redwhite.redwhite.utils.CacheFragmentStatePagerAdapter;
import me.redwhite.redwhite.utils.HttpConnection;
import me.redwhite.redwhite.utils.PathJSONParser;
import me.redwhite.redwhite.utils.SlidingTabLayout;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QuestDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QuestDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestDetailFragment extends Fragment
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ViewPager viewPager;
    private NavigationAdapter navigationAdapter;

    Quest quest;
    ArrayList<Question> questionList = new ArrayList<Question>();
    ArrayList<String> titleList = new ArrayList<String>();
    ArrayList<Boolean> completeList = new ArrayList<Boolean>();
    ArrayList<QuestUser> userList = new ArrayList<QuestUser>();

    private MapView mMapView;
    private GoogleMap gMap;
    private Polyline currentLine;

    private boolean listViewActive;

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private static final LatLng LOWER_MANHATTAN = new LatLng(40.722543,
            -73.998585);
    private static final LatLng BROOKLYN_BRIDGE = new LatLng(40.7057, -73.9964);
    private static final LatLng WALL_STREET = new LatLng(40.7064, -74.0094);

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BrowseQuestionsListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuestDetailFragment newInstance(String param1, String param2) {
        QuestDetailFragment fragment = new QuestDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public QuestDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // Allow access to the ActionBar
        setHasOptionsMenu(true);
        buildGoogleApiClient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_quest_detail, container, false);

        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                gMap = googleMap;
                LatLng singapore = new LatLng(1.3, 103.8);
                LatLng offset = new LatLng(1.55, 103.8);
                googleMap.setMyLocationEnabled(true);

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(offset).zoom(10).build();

                googleMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));

                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        final FrameLayout frameLayout = (FrameLayout) getActivity().findViewById(R.id.frameLayoutQuestions);
                        final MapView mapView = (MapView) getActivity().findViewById(R.id.mapView);
                        final FrameLayout pagerContainer = (FrameLayout) getActivity().findViewById(R.id.pagerContainer);
                        final LinearLayout header = (LinearLayout) getActivity().findViewById(R.id.header);

                        Animation animation = new AlphaAnimation(0.0f, 1.0f);
                        animation.setFillAfter(true);
                        animation.setDuration(350);
                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                pagerContainer.bringToFront();
                                header.bringToFront();
                                listViewActive = !listViewActive;
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        pagerContainer.startAnimation(animation);
                    }
                });


            }
        });

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // views are ready for loading, wait for data before populating.

        final User u = new User();
        u.setKey("BAA");
        u.set_communities_joined(new ArrayList<String>());
        u.get_communities_joined().add("sg");
        u.get_communities_joined().add("nyp");

        //TODO: need to pickup the quest from an earlier screen
        final Quest quest = new Quest();
        String[] questions = {"1","2","3","4","5","somethingsomething"};
        ArrayList<String> questionlist = new ArrayList<>(Arrays.asList(questions));
        quest.setQuestions(questionlist);
        quest.setShortname("orchardps2015");

        this.quest = quest;

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setTitle("THE QUEST TITLE");

        questionList = new ArrayList<Question>();
        titleList = new ArrayList<String>();

        class ParserTask extends
                AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

            @Override
            protected List<List<HashMap<String, String>>> doInBackground(
                    String... jsonData) {
               Log.println(Log.INFO, "google map path with JSON:", jsonData[0]);
                JSONObject jObject;
                List<List<HashMap<String, String>>> routes = null;

                try {
                    jObject = new JSONObject(jsonData[0]);
                    PathJSONParser parser = new PathJSONParser();
                    routes = parser.parse(jObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return routes;
            }

            @Override
            protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
                ArrayList<LatLng> points = null;
                PolylineOptions polyLineOptions = null;

                // traversing through routes
                for (int i = 0; i < routes.size(); i++) {
                    points = new ArrayList<LatLng>();
                    polyLineOptions = new PolylineOptions();
                    List<HashMap<String, String>> path = routes.get(i);

                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    polyLineOptions.addAll(points);
                    polyLineOptions.width(12);
                    polyLineOptions.color(Color.parseColor("#42A5F5"));
                }

                Toast.makeText(getActivity(), "Redrawing original path.", Toast.LENGTH_SHORT).show();
                currentLine =  gMap.addPolyline(polyLineOptions);
            }
        }

        class ReadTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... url) {
                String data = "";
                try {
                    Log.println(Log.INFO, "google map requesting url:", url[0]);
                    HttpConnection http = new HttpConnection();
                    data = http.readUrl(url[0]);
                } catch (Exception e) {
                    Log.d("Background Task", e.toString());
                }
                return data;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                new ParserTask().execute(result);
            }
        }

        class RecommendedQuestionsTask extends AsyncTask<String, Boolean, ArrayList<Question>> {
            @Override
            protected void onPreExecute() {
                // show the progress indicator
            }

            @Override
            protected ArrayList<Question> doInBackground(String... communities) {
                final ArrayList<Question> incoming = new ArrayList<Question>();

                    for (int i=0; i<quest.getQuestions().size(); i++) {
                        //Log.println(Log.INFO, "Finding question: ", qs.question);
                        final String qs = quest.getQuestions().get(i);
                        Question.findNodeByKey(qs, new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                Question q = Question.convertFromMap(qs, (Map<String, Object>) dataSnapshot.getValue());

                                boolean questionExists = false;
                                for (Question z : questionList) {
                                    if (z.getKey().equals(q.getKey())) {
                                        questionExists = true;
                                        break;
                                    }
                                }

                                if (!questionExists) {
                                    //bughack recovery
                                    if (questionList.size() == 2 && (questionList.get(0) == questionList.get(1))) {
                                        questionList.remove(0);
                                        titleList.remove(0);
                                        completeList.remove(0);
                                    }


                                    questionList.add(q);
                                    titleList.add("question");
                                    completeList.add(false);

//                                    // how to ensure that order is maintained?
//                                    // compare to original question list in quests
//                                    if (questionList.size() == 0) {
//
//                                    } else {
//                                        // there is at least one item in the list
//                                        // check if it should be placed before or after the current item
//
//                                        // find the position of the question that should be before the current item
//                                        String beforeKey = null;
//                                        for (int i=0; i < quest.getQuestions().size(); i++)
//                                        {
//                                            String currentKey = quest.getQuestions().get(i);
//                                            if (currentKey.equals(q.getKey())) {
//                                                beforeKey = quest.getQuestions().get(i--);
//
//                                            }
//                                        }
//
//
//                                        for (int i=0; i < questionList.size(); i++)
//                                        {
//                                            // find the position of the current question
//
//                                            // find the position of the
//
//
//                                        }
//
//                                    }

                                    //bughack to prevent crashing when reloading for this second time
                                    if (questionList.size() == 1) {
                                        questionList.add(q);
                                        titleList.add("question");
                                        completeList.add(false);
                                    }

                                    gMap.addMarker(new MarkerOptions()
                                            .title(q.getQuestion())
                                            .snippet("asked by " + q.getCreated_username())
                                            .position(new LatLng(q.get_location().getLat(), q.get_location().getLng())));

                                    if (questionList.size() > 4) {
                                        redrawMap();
                                    }
                                }

                                navigationAdapter = new NavigationAdapter(((FragmentActivity) getActivity()).getSupportFragmentManager(), questionList, titleList, completeList, u);
                                viewPager = (ViewPager) getActivity().findViewById(R.id.pager);
                                viewPager.setAdapter(navigationAdapter);

                                final SlidingTabLayout slidingTabLayout = (SlidingTabLayout) getActivity().findViewById(R.id.sliding_tabs);
                                slidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
                                slidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.accent));
                                slidingTabLayout.setDistributeEvenly(true);
                                slidingTabLayout.setViewPager(viewPager);

                                slidingTabLayout.attachFragment(QuestDetailFragment.this);
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                    }
                return incoming;
            }

            @Override
            protected void onPostExecute(ArrayList<Question> list) {

                // TODO: hide progress bar
            }

            @Override
            protected void onProgressUpdate(Boolean... values) {
                super.onProgressUpdate(values);
            }
        }

        RecommendedQuestionsTask task = new RecommendedQuestionsTask();
        task.execute(u.get_communities_joined().toArray(new String[]{}));

        startListeningToOnlineUsers();
    }

    private void startListeningToOnlineUsers() {
        Quest.listenToOnlineQuestUsers(quest.getShortname(), new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // save values to fragment
                Map<String, Object> map = (Map<String, Object>)dataSnapshot.getValue();
                userList = QuestUser.convertListFromMap(map);

                // redraw map
                // API call called too fast
                redrawMap();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void redrawMap() {
        gMap.clear();

        if (questionList.size() > 3) {
            String url = getMapsApiDirectionsUrl();
            ReadTask downloadTask = new ReadTask();
            downloadTask.execute(url);
        }

        // redraw the locations
        for(QuestUser qu : userList) {
            gMap.addMarker(new MarkerOptions().position(new LatLng(qu.getLat(), qu.getLng()))
                    .title("Online User").snippet("Live Position").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        }
    }

    private String getMapsApiDirectionsUrl() {

        String origin = "origin=" + questionList.get(0).get_location().getLat() +"," + questionList.get(0).get_location().getLng();
        String destination = "destination=" +questionList.get(questionList.size()-1).get_location().getLat() +"," +questionList.get(questionList.size()-1).get_location().getLng();

        String waypoints = "waypoints=optimize:false|";
        for(int i=1; i < questionList.size()-1; i++) {
            if (!completeList.get(i)) {
                waypoints += questionList.get(i).get_location().getLat() +"," + questionList.get(i).get_location().getLng();
                waypoints += "|";
            } else {
                //waypoint was ignored as question is complete
            }

        }
        waypoints = waypoints.substring(0, waypoints.length()-1);

        String sensor = "sensor=false";
        String params = waypoints + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" +origin +"&" +destination +"&" + params +"&key=AIzaSyCIqH10fDisGK7uC7TBWec6kqLL_e8VdeI";
        return url;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void pagerUpdate(int position) {

        if (questionList != null) {
            Question q = questionList.get(position);

            if (q != null) {
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(q.get_location().getLat() + 0.015, q.get_location().getLng())).zoom(14).build();

                gMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));
            }
        }
    }

    public void completeQuestion(Question q, User u, boolean isComplete, boolean isQuest, Quest qu) {
        // this updates the completion list on callback from single question fragment
        // Toast.makeText(getActivity(), "host complete called", Toast.LENGTH_LONG).show();

        for(int i=0; i < questionList.size(); i++)
        {
            if(q.getKey().equals(questionList.get(i).getKey())) {
                completeList.set(i, true);
                break;
            }
        }

        redrawMap();
    }

    class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {
            Log.println(Log.INFO, "google map path with JSON:", jsonData[0]);
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            // traversing through routes
            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<LatLng>();
                polyLineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = routes.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                polyLineOptions.addAll(points);
                polyLineOptions.width(12);
                polyLineOptions.color(Color.parseColor("#42A5F5"));
            }


            for(Question q : questionList) {
                gMap.addMarker(new MarkerOptions()
                        .title(q.getQuestion())
                        .snippet("asked by " + q.getCreated_username())
                        .position(new LatLng(q.get_location().getLat(), q.get_location().getLng())));
            }


            currentLine = gMap.addPolyline(polyLineOptions);

        }
    }

    class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                Log.println(Log.INFO, "google map requesting url:", url[0]);
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    /**
     * This adapter provides two types of fragments as an example.
     * {@linkplain #createItem(int)} should be modified if you use this example for your app.
     */
    private static class NavigationAdapter extends CacheFragmentStatePagerAdapter {

        private static String[] TITLES = new String[]{};

        private int mScrollY;

        private ArrayList<Question> questions;
        private ArrayList<Boolean> completes;
        private User user;

        public NavigationAdapter(FragmentManager fm) {
            super(fm);
        }

        public NavigationAdapter(FragmentManager fm, ArrayList<Question> questions, ArrayList<String> titles, ArrayList<Boolean> completes, User user) {
            super(fm);
            this.user = user;
            this.questions = questions;
            this.completes = completes;

            TITLES = titles.toArray(TITLES);
        }

        public void setScrollY(int scrollY) {
            mScrollY = scrollY;
        }

        @Override
        protected android.support.v4.app.Fragment createItem(int position) {
            // Initialize fragments.
            // Please be sure to pass scroll position to each fragments using setArguments.

            // retrieve the question

            Question q = questions.get(position);
            boolean c = completes.get(position);

            //TODO: update this to use real quest
            final Quest quest = new Quest();
            String[] questions = {"1","2","3","4","5","somethingsomething"};
            ArrayList<String> questionlist = new ArrayList<>(Arrays.asList(questions));
            quest.setQuestions(questionlist);
            quest.setShortname("orchardps2015");

            android.support.v4.app.Fragment f = SingleQuestionFragment.newInstance(q, user, c, true, quest);

            return f;
            // assign new instance of fragment

        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_browse_questions, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menuShowNearby) {

            final FrameLayout frameLayout = (FrameLayout) getActivity().findViewById(R.id.frameLayoutQuestions);
            final MapView mapView = (MapView) getActivity().findViewById(R.id.mapView);
            final FrameLayout pagerContainer = (FrameLayout) getActivity().findViewById(R.id.pagerContainer);
            final LinearLayout header = (LinearLayout) getActivity().findViewById(R.id.header);

            if(listViewActive){
                Animation animation = new AlphaAnimation(1.0f, 0.0f);
                animation.setFillAfter(true);
                animation.setDuration(350);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mapView.bringToFront();
                        listViewActive = !listViewActive;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                pagerContainer.startAnimation(animation);
                item.setTitle("Show Questions");
            } else {
                Animation animation = new AlphaAnimation(0.0f, 1.0f);
                animation.setFillAfter(true);
                animation.setDuration(350);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        pagerContainer.bringToFront();
                        header.bringToFront();
                        listViewActive = !listViewActive;
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                pagerContainer.startAnimation(animation);
                item.setTitle("Show Map");
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this.getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        // tell the quest where are we
        // TODO: update the user selected
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final String username = sp.getString("example_text", "bam").toUpperCase();
        QuestUser.updateUserLocation(quest.getShortname(), username, location.getLatitude(), location.getLongitude());
    }


}
