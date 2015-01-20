package me.redwhite.redwhite.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

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

import java.util.ArrayList;
import java.util.List;

import me.redwhite.redwhite.R;
import me.redwhite.redwhite.SingleProfileActivity;
import me.redwhite.redwhite.models.Question;
import me.redwhite.redwhite.utils.QuestionsListAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsfeedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsfeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsfeedFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private MapView mMapView;
    private GoogleMap googleMap;

    ListView list;
    String userId;
    String questionDetail;
    ArrayList<Marker> listMarkers;
    CountDownTimer timer;

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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsfeedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsfeedFragment newInstance(String param1, String param2) {
        NewsfeedFragment fragment = new NewsfeedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public NewsfeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_newsfeed, container, false);

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

                googleMap.setMyLocationEnabled(true);
                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        String userId = "junhong14";
                        String questionDetail = marker.getTitle();
                        Intent myIntent = new Intent(getActivity(),QuestionDetailActivity.class);
                        myIntent.putExtra("username", userId);
                        myIntent.putExtra("question", questionDetail);
                        startActivity(myIntent);
                    }
                });

                // latitude and longitude
                double latitude = 1.3667;
                double longitude = 103.8;

                // create marker
                MarkerOptions marker = new MarkerOptions().position(
                        new LatLng(latitude, longitude)).title("Singapore").snippet("Home of RedWhite");

                // Changing marker icon
//                marker.icon(BitmapDescriptorFactory
//                        .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

                // adding marker
                googleMap.addMarker(marker);
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(latitude, longitude)).zoom(10).build();
                googleMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));

                // Perform any camera updates here
            }
        });


        CustomListView adapter = new CustomListView(getActivity(),questions,imageId,username);
        list = (ListView) v.findViewById(R.id.listViewActivity);
        list.setAdapter(adapter);

        listMarkers = new ArrayList<>();

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                userId = username[position];
                questionDetail = questions[position];
                Intent myIntent = new Intent(getActivity(),QuestionDetailActivity.class);
                myIntent.putExtra("username", userId);
                myIntent.putExtra("question", questionDetail);
                startActivity(myIntent);

            }
        });


        // Randomly generate points in the map
        timer = new CountDownTimer(1500, 20) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                try{
                    addMarkers();
                }catch(Exception e){

                }
            }
        }.start();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_newsfeed, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuShowProfile)
        {
            Intent myIntent = new Intent(getActivity(), SingleProfileActivity.class);
            //TODO: Hookup to the right user id
            myIntent.putExtra("username", userId);
            startActivity(myIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void addMarkers()
    {
        double lat1 = 1.24273092;
        double lng1 = 103.61000061;
        double lat2 = 1.46376897;
        double lng2 = 104.07691956;

        double diffLat = lat2 - lat1;
        double diffLng = lng2 - lng1;

        double rGen = Math.random();
        double rGen2 = Math.random();
        double rLat = rGen * diffLat;
        double rLng = rGen2 * diffLng;
        double nLat = (lat1 + rLat);
        double nLng = (lng1 + rLng);
        LatLng point = new LatLng(nLat, nLng);

        Marker newMarker = mMapView.getMap().addMarker(new MarkerOptions()
                        .position(point)
                        .title("What should Bishan CC do next?")
                        .snippet("junhong14 answered")
        );

        listMarkers.add(newMarker);

        if (listMarkers.size() > 4)
        {
            listMarkers.get(0).remove();
        }

        timer.start();
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
