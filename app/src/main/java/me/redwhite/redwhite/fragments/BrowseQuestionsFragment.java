package me.redwhite.redwhite.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.redwhite.redwhite.R;
import me.redwhite.redwhite.models.Community;
import me.redwhite.redwhite.models.Question;
import me.redwhite.redwhite.models.User;
import me.redwhite.redwhite.utils.QuestionsListAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BrowseQuestionsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BrowseQuestionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrowseQuestionsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private MapView mMapView;
    private GoogleMap gMap;

    private boolean listViewActive = true;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BrowseQuestionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BrowseQuestionsFragment newInstance(String param1, String param2) {
        BrowseQuestionsFragment fragment = new BrowseQuestionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public BrowseQuestionsFragment() {
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
        View v =  inflater.inflate(R.layout.fragment_browse_questions, container, false);

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
                LatLng singpaore = new LatLng(1.3, 103.8);

                googleMap.setMyLocationEnabled(true);

                gMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                    @Override
                    public void onMyLocationChange(Location location) {
                        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
                    }
                });

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
                                LinearLayout.LayoutParams tvMargins = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                                tvMargins.setMargins(0, 24, 0, 16);

                                TextView yourtv = new TextView(getActivity());
                                yourtv.setTextColor(Color.parseColor("#2196f3"));
                                yourtv.setText("Thanks for your feedback. Find out how SMRT is improving your daily ride on train services in Singapore every day >");
                                yourtv.setLayoutParams(tvMargins);
                                question.addView(yourtv);

                                Button analysis = new Button(getActivity());

                                LinearLayout.LayoutParams newbutton = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                                newbutton.setMargins(0, 16, 0, 16);

                                analysis.setText("View community answers");
                                analysis.setBackgroundColor(Color.parseColor("#2196F3"));
                                analysis.setTextColor(Color.WHITE);
                                analysis.setTextSize(10);
                                analysis.setElevation(2);
                                analysis.setLayoutParams(newbutton);

                                analysis.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        List<LatLng> redlist = new ArrayList<LatLng>();

                                        redlist.add(new LatLng(1.381, 103.844));
                                        redlist.add(new LatLng(1.379, 103.841));
                                        redlist.add(new LatLng(1.382, 103.845));
                                        redlist.add(new LatLng(1.384, 103.840));

                                        // Create a heat map tile provider, passing it the latlngs of the police stations.
                                        HeatmapTileProvider mProvider = new HeatmapTileProvider.Builder()
                                                .data(redlist)
                                                .build();
                                        // Add a tile overlay to the map, using the heat map tile provider.
                                        mMapView.getMap().addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));


                                        List<LatLng> whitelist = new ArrayList<LatLng>();;

                                        whitelist.add(new LatLng(1.380, 103.842));
                                        whitelist.add(new LatLng(1.381, 103.843));
                                        whitelist.add(new LatLng(1.378, 103.848));
                                        whitelist.add(new LatLng(1.379, 103.839));

                                        // Create a heat map tile provider, passing it the latlngs of the police stations.
                                        HeatmapTileProvider whiteProvider = new HeatmapTileProvider.Builder()
                                                .data(redlist)
                                                .build();
                                        // Add a tile overlay to the map, using the heat map tile provider.
                                        mMapView.getMap().addTileOverlay(new TileOverlayOptions().tileProvider(whiteProvider));


                                    }
                                });

                                question.addView(analysis);
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

                googleMap.addMarker(new MarkerOptions()
                        .title("What do you think is in this new building?")
                        .snippet("asked by Nanyang Polytechnic")
                        .position(new LatLng(1.379, 103.847)));

                googleMap.addMarker(new MarkerOptions()
                        .title("Rate the appearance of the new building and interior!")
                        .snippet("asked by Nanyang Polytechnic")
                        .position(new LatLng(1.380, 103.849)));

                googleMap.addMarker(new MarkerOptions()
                        .title("How do you feel about the upcoming exams?")
                        .snippet("asked by NYP SIT")
                        .position(new LatLng(1.379, 103.850)));

                googleMap.addMarker(new MarkerOptions()
                        .title("Did you find this MRT station to be crowded?")
                        .snippet("asked by SMRT")
                        .position(new LatLng(1.381, 103.844)));
            }
        });

        List<Question> listQuestions = new ArrayList<Question>();
        listQuestions.add(new Question("twooption"));
        listQuestions.add(new Question("completeoption"));
        listQuestions.add(new Question("fuzzytext"));
        listQuestions.add(new Question("photo"));
        listQuestions.add(new Question("twooption"));

        // NEW QUESTION RETRIEVAL
        User u = new User();
        u.set_communities_joined(new ArrayList<String>());
        u.get_communities_joined().add("sg");
        u.get_communities_joined().add("nyp");

        final ArrayList<Question> qlist = new ArrayList<Question>();
        final QuestionsListAdapter adapter = new QuestionsListAdapter(getActivity(), 0, qlist);
        ListView listViewQuestions = (ListView) v.findViewById(R.id.listViewQuestions);
        listViewQuestions.addHeaderView(new View(v.getContext()));
        listViewQuestions.addFooterView(new View(v.getContext()));

        class RecommendedQuestionsTask extends AsyncTask<String, Boolean, ArrayList<Question>> {
            @Override
            protected void onPreExecute() {
                // show the progress indicator
            }

            @Override
            protected ArrayList<Question> doInBackground(String... communities) {
                final ArrayList<Question> incoming = new ArrayList<Question>();

                for(String communityKey: communities) {
                    Community.findNodeByKey(communityKey, new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Community c = Community.convertFromMap((Map<String, Object>)dataSnapshot.getValue());

                            for(final Community.QuestionStatus qs : c.get_questions()) {
                                Question.findNodeByKey(qs.question, new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Question q = Question.convertFromMap(qs.question, (Map<String, Object>)dataSnapshot.getValue());
                                        adapter.add(q);
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
                //qlist.addAll(list);

                // TODO: hide progress bar
            }

            @Override
            protected void onProgressUpdate(Boolean... values) {
                super.onProgressUpdate(values);
            }
        }

        RecommendedQuestionsTask task = new RecommendedQuestionsTask();
        task.execute(u.get_communities_joined().toArray(new String[]{}));

        //TEST
        listViewQuestions.setAdapter(adapter);

        return v;
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView listViewQuestions = (ListView) getActivity().findViewById(R.id.listViewQuestions);
        listViewQuestions.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
                if (scrollState == SCROLL_STATE_FLING) {
                    View itemView = view.getChildAt(0);
                    int top = Math.abs(itemView.getTop());
                    int bottom = Math.abs(itemView.getBottom());
                    int scrollBy = top >= bottom ? bottom : -top;
                    if (scrollBy == 0) {
                        return;
                    }
                    smoothScrollDeferred(scrollBy, (ListView)view);
                }
            }

            private void smoothScrollDeferred(final int scrollByF,
                                              final ListView viewF) {
                final Handler h = new Handler();
                h.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        viewF.smoothScrollBy(scrollByF, 350);
                    }
                });
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub

            }
        });

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

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
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

}
