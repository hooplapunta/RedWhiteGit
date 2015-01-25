package me.redwhite.redwhite.fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.redwhite.redwhite.R;
import me.redwhite.redwhite.SingleProfileActivity;
import me.redwhite.redwhite.models.Question;


public class QuestionDetailActivity extends Activity {
    TextView welcome;
    TextView questionTxt;

    private MapView mMapView;
    private GoogleMap gMap;

    FrameLayout frameLayout;
    LinearLayout layout;

    private boolean listViewActive = true;

    Question question;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);

        Question.findNodeByKey("1", new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                question = Question.convertFromMap((Map<String, Object>)dataSnapshot.getValue());

                frameLayout = (FrameLayout) findViewById(R.id.frameLayoutSingleQuestion);

                layout = (LinearLayout) findViewById(R.id.linearLayoutCard);
                LinearLayout.LayoutParams buttonMargins = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                buttonMargins.setMargins(0, 16, 0, 16);
                questionTxt = (TextView) findViewById(R.id.tvQuestionText);

                Bundle extras = QuestionDetailActivity.this.getIntent().getExtras();
                String usernameFromMain = extras.getString("username");
                String questionFromMain = extras.getString("question");

                ActionBar actionBar = getActionBar();
                actionBar.setTitle(usernameFromMain+"'s Activity");

                questionTxt.setText(questionFromMain);

                TextView tv = new TextView(QuestionDetailActivity.this);
                tv.setText(usernameFromMain+ "'s answer:");
                layout.addView(tv);

                Button option1 = new Button(QuestionDetailActivity.this);
                option1.setText("Yes, questions are awesome!");
                option1.setBackgroundColor(Color.parseColor("#F44336"));
                option1.setTextColor(Color.WHITE);
                option1.setElevation(2);
                option1.setLayoutParams(buttonMargins);

                layout.addView(option1);

                LinearLayout.LayoutParams tvMargins = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                tvMargins.setMargins(0, 24, 0, 16);

                TextView yourtv = new TextView(QuestionDetailActivity.this);
                yourtv.setText("Add your answer:");
                yourtv.setLayoutParams(tvMargins);


                layout.addView(yourtv);

                Button option3 = new Button(QuestionDetailActivity.this);
                option3.setText("Yes, questions are awesome!");
                option3.setBackgroundColor(Color.parseColor("#F44336"));
                option3.setTextColor(Color.WHITE);
                option3.setElevation(2);
                option3.setLayoutParams(buttonMargins);

                layout.addView(option3);

                Button option2 = new Button(QuestionDetailActivity.this);
                option2.setText("No, I think we can do better than just questions.");
                option2.setBackgroundColor(Color.parseColor("#B0BEC5"));
                option2.setTextColor(Color.BLACK);
                option2.setElevation(2);
                option2.setLayoutParams(buttonMargins);
                layout.addView(option2);


                //TODO: Junhong this is where you get the map ready and put in the heatmap
                mMapView = (MapView) findViewById(R.id.mapView);
                mMapView.onCreate(savedInstanceState);

                mMapView.onResume();// needed to get the map to display immediately

                try {
                    MapsInitializer.initialize(getApplicationContext());
                } catch (Exception e) {
                    e.printStackTrace();
                }


                // set up the map data once the view is ready
                mMapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        gMap = googleMap;
                        LatLng singpaore = new LatLng(1.3, 103.8);

                        googleMap.setMyLocationEnabled(true);

                        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {

                                //TODO: if you want something to happen when a marker is tapped, write code here

                            }
                        });

                        // examples on how to add markers
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

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_question_detail, menu);
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

        if (id == R.id.menuShowProfile)
        {
            Intent myIntent = new Intent(this, SingleProfileActivity.class);
            //TODO: Hookup to the right user id
            myIntent.putExtra("username", "BAA");
            startActivity(myIntent);
        }

        if (id == R.id.action_showAnswers)
        {
            List<LatLng> redlist = new ArrayList<LatLng>();

            //TODO: Loop through question.getQuestionOptions().get(1).getQuestionAnswers() arraylist
            redlist.add(new LatLng(1.381, 103.844));
            redlist.add(new LatLng(1.379, 103.841));
            redlist.add(new LatLng(1.382, 103.845));
            redlist.add(new LatLng(1.384, 103.840));

            // Create the gradient.
            int[] colors = {
                    Color.parseColor("#f44336")   // red
            };
            float[] startPoints = {
                    0.2f
            };
            Gradient redgradient = new Gradient(colors, startPoints);

            // Create a heat map tile provider, passing it the latlngs of the police stations.
            HeatmapTileProvider mProvider = new HeatmapTileProvider.Builder()
                    .data(redlist)
                    .gradient(redgradient)
                    .build();
            // Add a tile overlay to the map, using the heat map tile provider.
            mMapView.getMap().addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));


            List<LatLng> whitelist = new ArrayList<LatLng>();;

            //TODO: Loop through question.getQuestionOptions().get(2).getQuestionAnswers() arraylist
            whitelist.add(new LatLng(1.380, 103.842));
            whitelist.add(new LatLng(1.381, 103.843));
            whitelist.add(new LatLng(1.378, 103.848));
            whitelist.add(new LatLng(1.379, 103.839));

            // Create the gradient.
            int[] wcolors = {
                    Color.parseColor("#cfd8dc")   // red
            };
            float[] wstartPoints = {
                    0.2f
            };
            Gradient wgradient = new Gradient(wcolors, wstartPoints);

            // Create a heat map tile provider, passing it the latlngs of the police stations.
            HeatmapTileProvider whiteProvider = new HeatmapTileProvider.Builder()
                    .data(whitelist)
                    .gradient(wgradient)
                    .build();
            // Add a tile overlay to the map, using the heat map tile provider.
            mMapView.getMap().addTileOverlay(new TileOverlayOptions().tileProvider(whiteProvider));


            // swap the display over
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
                layout.startAnimation(animation);
                item.setTitle("Show Question");
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
                frameLayout.startAnimation(animation);
                item.setTitle("Show Answers");
            }

            listViewActive = !listViewActive;
        }

        return super.onOptionsItemSelected(item);
    }
}
