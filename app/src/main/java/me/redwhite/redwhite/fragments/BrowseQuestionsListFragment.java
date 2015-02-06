package me.redwhite.redwhite.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
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

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.redwhite.redwhite.MainActivity;
import me.redwhite.redwhite.R;
import me.redwhite.redwhite.models.Community;
import me.redwhite.redwhite.models.Question;
import me.redwhite.redwhite.models.User;
import me.redwhite.redwhite.utils.BaseFragment;
import me.redwhite.redwhite.utils.CacheFragmentStatePagerAdapter;
import me.redwhite.redwhite.utils.SlidingTabLayout;
import me.redwhite.redwhite.utils.ViewPagerTabListViewFragment;
import me.redwhite.redwhite.utils.ViewPagerTabRecyclerViewFragment;
import me.redwhite.redwhite.utils.ViewPagerTabScrollViewFragment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BrowseQuestionsListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BrowseQuestionsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrowseQuestionsListFragment extends Fragment {
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

    ArrayList<Question> questionList = new ArrayList<Question>();
    ArrayList<String> titleList = new ArrayList<String>();

    private MapView mMapView;
    private GoogleMap gMap;

    private boolean listViewActive;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BrowseQuestionsListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BrowseQuestionsListFragment newInstance(String param1, String param2) {
        BrowseQuestionsListFragment fragment = new BrowseQuestionsListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public BrowseQuestionsListFragment() {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        // Original inflater
        //return inflater.inflate(R.layout.fragment_browse_questions_list, container, false);

        View v = inflater.inflate(R.layout.fragment_browse_questions_list, container, false);

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

                        FrameLayout frameLayout = (FrameLayout) getActivity().findViewById(R.id.frameLayoutBrowseQuestions);
                        final LinearLayout question = (LinearLayout) frameLayout.findViewById(R.id.linearLayoutCard);

                        TextView questionText = (TextView) question.findViewById(R.id.tvQuestionText);
                        questionText.setText(marker.getTitle());

                        LinearLayout.LayoutParams buttonMargins = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                        buttonMargins.setMargins(0, 16, 0, 16);

                        Button option1 = new Button(getActivity());
                        option1.setText("Yes, questions are awesome!");
                        option1.setBackgroundColor(Color.parseColor("#F44336"));
                        option1.setTextColor(Color.WHITE);
                        option1.setElevation(2);
                        option1.setLayoutParams(buttonMargins);

                        option1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                            }
                        });

                        question.addView(option1);

                        Button option2 = new Button(getActivity());
                        option2.setText("No, I think we can do better than just questions.");
                        option2.setBackgroundColor(Color.parseColor("#B0BEC5"));
                        option2.setTextColor(Color.BLACK);
                        option2.setElevation(2);
                        option2.setLayoutParams(buttonMargins);
                        question.addView(option2);

                        View tempFrom = frameLayout.getChildAt(0);
                        frameLayout.bringChildToFront(tempFrom);
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

        questionList = new ArrayList<Question>();
        titleList = new ArrayList<String>();

        class RecommendedQuestionsTask extends AsyncTask<String, Boolean, ArrayList<Question>> {
            @Override
            protected void onPreExecute() {
                // show the progress indicator
            }

            @Override
            protected ArrayList<Question> doInBackground(String... communities) {
                final ArrayList<Question> incoming = new ArrayList<Question>();

                for(final String communityKey: communities) {
                    //Log.println(Log.INFO, "Finding community: ", communityKey);
                    Community.findNodeByKey(communityKey, new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Community c = Community.convertFromMap((Map<String, Object>) dataSnapshot.getValue());

                            for (final Community.QuestionStatus qs : c.get_questions()) {
                                //Log.println(Log.INFO, "Finding question: ", qs.question);
                                Question.findNodeByKey(qs.question, new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        Question q = Question.convertFromMap(qs.question, (Map<String, Object>) dataSnapshot.getValue());

                                        boolean questionExists = false;
                                        for(Question z : questionList)
                                        {
                                            if(z.getKey().equals(q.getKey()))
                                            {
                                                questionExists = true;
                                                break;
                                            }
                                        }

                                        if (!questionExists) {
                                            //bughack recovery
                                            if(questionList.size() == 2 && (questionList.get(0) == questionList.get(1)))
                                            {
                                                questionList.remove(0);
                                                titleList.remove(0);
                                            }

                                            questionList.add(q);
                                            titleList.add("#" +communityKey);

                                            //bughack to prevent crashing when reloading for this second time
                                            if(questionList.size() == 1)
                                            {
                                                questionList.add(q);
                                                titleList.add("#" +communityKey);
                                            }

                                            gMap.addMarker(new MarkerOptions()
                                                    .title(q.getQuestion())
                                                    .snippet("asked by " +q.getCreated_username())
                                                    .position(new LatLng(q.get_location().getLat(), q.get_location().getLng())));
                                        }

                                        navigationAdapter = new NavigationAdapter(((FragmentActivity)getActivity()).getSupportFragmentManager(), questionList, titleList, u);
                                        viewPager = (ViewPager) getActivity().findViewById(R.id.pager);
                                        viewPager.setAdapter(navigationAdapter);

                                        final SlidingTabLayout slidingTabLayout = (SlidingTabLayout) getActivity().findViewById(R.id.sliding_tabs);
                                        slidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
                                        slidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.accent));
                                        slidingTabLayout.setDistributeEvenly(true);
                                        slidingTabLayout.setViewPager(viewPager);

                                        slidingTabLayout.attachFragment(BrowseQuestionsListFragment.this);
                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });
                            }
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

        private User user;

        public NavigationAdapter(FragmentManager fm) {
            super(fm);
        }

        public NavigationAdapter(FragmentManager fm, ArrayList<Question> questions, ArrayList<String> titles, User user) {
            super(fm);
            this.user = user;
            this.questions = questions;
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
            //TODO: update this to refer from a completeList
            android.support.v4.app.Fragment f = SingleQuestionFragment.newInstance(q, user, false, false, null);

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

            final FrameLayout frameLayout = (FrameLayout) getActivity().findViewById(R.id.frameLayoutBrowseQuestions);
            ListView listViewQuestions = (ListView) getActivity().findViewById(R.id.listViewQuestions);

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
                        View tempFrom = frameLayout.getChildAt(1);
                        frameLayout.bringChildToFront(tempFrom);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                listViewQuestions.startAnimation(animation);
                item.setTitle("Show All");
            } else {
                Animation animation = new AlphaAnimation(0.0f, 1.0f);
                animation.setFillAfter(true);
                animation.setDuration(350);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        View tempFrom = frameLayout.getChildAt(1);
                        frameLayout.bringChildToFront(tempFrom);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                listViewQuestions.startAnimation(animation);
                item.setTitle("Show Nearby");
            }

            listViewActive = !listViewActive;

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
