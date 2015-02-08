package me.redwhite.redwhite.fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.redwhite.redwhite.R;
import me.redwhite.redwhite.SingleProfileActivity;
import me.redwhite.redwhite.models.Question;
import me.redwhite.redwhite.models.QuestionAnswer;

//import chart
import org.achartengine.model.SeriesSelection;

public class QuestionDetailActivity extends Activity {
    TextView welcome;
    TextView questionTxt;

    TextView tvOption1;
    TextView tvOption2;

    int option1count;
    int option2count;
    int totalOptionCount;

    String mapMode;

    private CircleOptions circleOptions;
    private Circle circle;

    private MapView mMapView;
    private GoogleMap gMap;
    private TileOverlay mSeverityOverlay;
    private TileOverlay mRedOverlay;
    private TileOverlay mWhiteOverlay;

    FrameLayout frameLayout;
    LinearLayout layout;

    private boolean listViewActive = true;

    Question question;

    Button buttonBuffer;

    CheckBox checkBoxCompare;


    TextView tvMode;
    private GraphicalView chartOption;


    List<LatLng> redList = new ArrayList<LatLng>();
    List<LatLng> whitelist = new ArrayList<LatLng>();
    List<LatLng> totalList = new ArrayList<LatLng>();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);

        tvOption1 = (TextView) findViewById(R.id.tvOption1);
        tvOption2 = (TextView) findViewById(R.id.tvOption2);

        buttonBuffer = (Button) findViewById(R.id.btn_Buffer);

        checkBoxCompare = (CheckBox) findViewById(R.id.cb_Comparison);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayoutSingleQuestion);
        tvMode =(TextView)findViewById(R.id.tv_Mode);

        Question.findNodeByKey("1", new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                question = Question.convertFromMap("1", (Map<String, Object>) dataSnapshot.getValue());

                layout = (LinearLayout) findViewById(R.id.linearLayoutCard);
                LinearLayout.LayoutParams buttonMargins = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                buttonMargins.setMargins(0, 16, 0, 16);
                questionTxt = (TextView) findViewById(R.id.tvQuestionText);

                Bundle extras = QuestionDetailActivity.this.getIntent().getExtras();
                String usernameFromMain = extras.getString("username");
                String questionFromMain = extras.getString("question");

                ActionBar actionBar = getActionBar();
                actionBar.setTitle(usernameFromMain + "'s Activity");

                questionTxt.setText(questionFromMain);

                //Buffer Button
                buttonBuffer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doBuffer();

                }
                });

                checkBoxCompare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(checkBoxCompare.isChecked()) {
                            tvMode.setText("Options Comparison Mode");
                            mSeverityOverlay.setVisible(false);
                            mRedOverlay.setVisible(true);
                            mWhiteOverlay.setVisible(true);



                        } else {
                            tvMode.setText("Severity Distribution Mode");
                            mSeverityOverlay.setVisible(true);
                            mRedOverlay.setVisible(false);
                            mWhiteOverlay.setVisible(false);


                        }
                    }
                });

                //Hard coded details
                {
                    TextView tv = new TextView(QuestionDetailActivity.this);
                    tv.setText(usernameFromMain + "'s answer:");
                    layout.addView(tv);

                    final Button option1 = new Button(QuestionDetailActivity.this);
                    option1.setText("Yes, questions are awesome!");
                    option1.setBackgroundColor(Color.parseColor("#F44336"));
                    option1.setTextColor(Color.WHITE);
                    option1.setElevation(2);
                    option1.setLayoutParams(buttonMargins);

                    layout.addView(option1);

                    LinearLayout.LayoutParams tvMargins = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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

                    final Button option2 = new Button(QuestionDetailActivity.this);
                    option2.setText("No, I think we can do better than just questions.");
                    option2.setBackgroundColor(Color.parseColor("#B0BEC5"));
                    option2.setTextColor(Color.BLACK);
                    option2.setElevation(2);
                    option2.setLayoutParams(buttonMargins);
                    layout.addView(option2);
                }

                //Setting up Map
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
                        LatLng singapore = new LatLng(1.3, 103.8);

                        googleMap.setMyLocationEnabled(true);

                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(singapore).zoom(13).build();
                        googleMap.animateCamera(CameraUpdateFactory
                                .newCameraPosition(cameraPosition));


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

                        //Storing LAT LONG INTO ARRAYS (RED + TOTAL)
                        ArrayList<QuestionAnswer> qaList = question.get_options().get(0).get_answers();
                        option1count = qaList.size();

                        //get an array list of responses that used (0) question option
                        for (QuestionAnswer qa : qaList) {
                            final double lat = qa.getLat();
                            final double lng = qa.getLng();

                            redList.add(new LatLng(lat, lng));
                            totalList.add(new LatLng(lat, lng));
                        }


                        //Storing LAT LONG INTO ARRAYS (WHITE + TOTAL)
                        ArrayList<QuestionAnswer> qaList2 = question.get_options().get(1).get_answers();
                        //get an array list of responses that used (0) question option
                        option2count = qaList2.size();
                        for (QuestionAnswer qa2 : qaList2) {
                            double lat = qa2.getLat();
                            double lng = qa2.getLng();

                            whitelist.add(new LatLng(lat, lng));
                            totalList.add(new LatLng(lat, lng));
                        }

                        //TODO: Data is ready, create the heatmaps
                        createSeverityLayer();
                        createRedComparison();
                        createWhiteComparison();

                        tvOption1.setText(Integer.toString(option1count));
                        tvOption2.setText(Integer.toString(option2count));

                    }
                });
                //totalOptionCount = option1count + option2count;
                //  Log.println(Log.INFO,"FUCK","hellocb"+String.valueOf(totalOptionCount));

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void createSeverityLayer() {
        int[] tcolors = {
                Color.rgb(102, 225, 0), // green
                Color.rgb(255, 0, 0)     // red
        };
        float[] tstartPoints = {
                0.2f, 1f
        };
        int radius = 10;
        Gradient totalGradient = new Gradient(tcolors, tstartPoints);

        // Create a heat map tile provider, passing it the latlngs of the police stations.
        HeatmapTileProvider totalProvider = new HeatmapTileProvider.Builder()
                .radius(10)
                .data(totalList)
                .gradient(totalGradient)
                .build();
        // Add a tile overlay to the map, using the heat map tile provider.
        mSeverityOverlay = mMapView.getMap().addTileOverlay(new TileOverlayOptions().tileProvider(totalProvider));
    }

    public void createRedComparison() {
        int[] rcolors = {
                Color.parseColor("#ffcdd2"),
                Color.parseColor("#f44336")   // red
        };
        float[] rstartPoints = {
                0.2f, 1f
        };
        Gradient redgradient = new Gradient(rcolors, rstartPoints);

        // Create a heat map tile provider, passing it the latlngs of the police stations.
        HeatmapTileProvider redProvider = new HeatmapTileProvider.Builder()
                .data(redList)
                .gradient(redgradient)
                .build();
        // Add a tile overlay to the map, using the heat map tile provider.
        mRedOverlay = mMapView.getMap().addTileOverlay(new TileOverlayOptions().tileProvider(redProvider));
        mRedOverlay.setVisible(false);
    }

    public void createWhiteComparison() {
        // Setting up Heat Map ( WHITE )
        // Create the gradient.
        int[] wcolors = {
                Color.parseColor("#ddbefb"),
                Color.parseColor("#A62196f3")   // blue
        };
        float[] wstartPoints = {
                0.2f, 1f
        };
        Gradient wgradient = new Gradient(wcolors, wstartPoints);

        // Create a heat map tile provider, passing it the latlngs of the police stations.
        HeatmapTileProvider whiteProvider = new HeatmapTileProvider.Builder()
                .data(whitelist)
                .gradient(wgradient)
                .build();
        // Add a tile overlay to the map, using the heat map tile provider.
        mWhiteOverlay = mMapView.getMap().addTileOverlay(new TileOverlayOptions().tileProvider(whiteProvider));
        mWhiteOverlay.setVisible(false);
    }



    private void doBuffer() {
        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            public void onMapClick(LatLng point) {

                 circleOptions = new CircleOptions()
                        .center(point)
                        .radius(500)
                        .fillColor(Color.argb(95, 178, 30, 37))
                        .strokeColor(Color.TRANSPARENT);

               circle = gMap.addCircle(circleOptions);
                Log.println(Log.INFO, "", "failure");
                showBuffer(point);
                Log.println(Log.INFO, "", "pass");
            }
        });


    }

    private void showBuffer(LatLng point) {
        Log.println(Log.INFO, "", "managed to run show buffer");
        option1count = 0;
        option2count = 0;
        double x1 = point.longitude;
        double y1 = point.latitude;
        double x2, y2;


        for (int count = 0; count < redList.size(); count++) {
            double distance = 0;
            x2 = redList.get(count).latitude;
            y2 = redList.get(count).longitude;
            distance = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
            Log.println(Log.INFO, "", "hellomother" + String.valueOf(distance));
            //distance = distance * 100;
            if (distance < 145.027) {
                option1count++;
                Log.println(Log.INFO, "", "helloFuckerR" + Integer.toString(option1count));
            }
        }
        for (int count = 0; count < whitelist.size(); count++) {
            double distance = 0;
            x2 = whitelist.get(count).latitude;
            y2 = whitelist.get(count).longitude;
            distance = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
            //distance = distance * 100;
            if (distance < 145.027) {
                option2count++;
                Log.println(Log.INFO, "", "helloFuckerW" + Integer.toString(option2count));
            }
        }
        tvOption1.setText(Integer.toString(option1count));
        tvOption2.setText(Integer.toString(option2count));
        setUpOptionChart();
    }

    private void setUpOptionChart() {
        double percentage = 0;
        int totalOptions = 0;
        String formatOptionStr;



        int[] colors = {Color.rgb(250, 88, 130), Color.rgb(46, 154, 254)};
        // totalOptions= totalOptionCount;
        CategorySeries distributionSeries = new CategorySeries(" Options ");

        ArrayList<Integer> percentArray = new ArrayList<Integer>();
        percentArray.add(option1count);

        percentArray.add(option2count);
        totalOptions = option1count + option2count;
        for (int i = 0; i < percentArray.size(); i++) {

            Log.println(Log.INFO, "PERCENTAGE TOTAL", String.valueOf(percentArray.get(i)));
            Log.println(Log.INFO, "PERCENTAGE TOTAL", String.valueOf(totalOptions));
            percentage = Math.round(((double) percentArray.get(i) / (double) totalOptions) * (double) 100);
            int percentageInt = (int)percentage;
            Log.println(Log.INFO, "PERCENTAGE", String.valueOf(percentage));
            //distributionSeries.add("" + "" + percentageInt + "%", totalOptions);
            distributionSeries
                    .add("" + " : " + percentageInt + " %", percentArray.get(i));



        }

        DefaultRenderer defaultRenderer = new DefaultRenderer();
        for (int j = 0; j < percentArray.size(); j++) {
            SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
            seriesRenderer.setColor(colors[j]);
            //seriesRenderer.setDisplayChartValues(true);
            // Adding a renderer for a slice
            defaultRenderer.addSeriesRenderer(seriesRenderer);
        }

        // Customize default renderer
        defaultRenderer.setChartTitle(" Event Population by Gender ");
        defaultRenderer.setChartTitleTextSize(18);
        defaultRenderer.setLabelsTextSize(13);
        defaultRenderer.setLabelsColor(Color.rgb(42, 47, 49));
        defaultRenderer.setZoomButtonsVisible(false);
        defaultRenderer.setShowLegend(true);
        defaultRenderer.setInScroll(true);
        defaultRenderer.setMargins(new int[]{50, 25, 20, 20});

        // Get the component from XML file
        RelativeLayout chartContainer = (RelativeLayout) findViewById(R.id.chart);

        // Creating a Pie Chart
        chartOption = ChartFactory.getPieChartView(QuestionDetailActivity.this,
                distributionSeries, defaultRenderer);
        defaultRenderer.setClickEnabled(true);
        defaultRenderer.setSelectableBuffer(10);
        /*chartOption.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SeriesSelection seriesSelection = chartOption
                        .getCurrentSeriesAndPoint();
                if (seriesSelection != null) {
                    // Getting the name of the clicked slice
                    int seriesIndex = seriesSelection.getPointIndex();
                    String selectedSeries = "";
                    for (int count = 0; count < genderAmtList.size(); count++) {
                        if (genderAmtList.get(seriesIndex).getGender()
                                .equals("M")) {
                            selectedSeries = "Male";
                        } else {
                            selectedSeries = "Female";
                        }
                    }

                    // Getting the value of the clicked slice
                    double value = seriesSelection.getXValue();

                    // Displaying the message
                    Toast.makeText(
                            getApplicationContext(),
                            selectedSeries + " : " + Math.round(value)
                                    + " person(s)", Toast.LENGTH_SHORT).show();
                }
            }
        });
        */
        // Adding the Pie Chart to the LinearLayout
        chartContainer.addView(chartOption);
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

        if (id == R.id.menuShowProfile) {
            Intent myIntent = new Intent(this, SingleProfileActivity.class);
            //TODO: Hookup to the right user id
            myIntent.putExtra("username", "BAA");
            startActivity(myIntent);
        }

        if (id == R.id.action_showAnswers) {
            // swap the display over
            if (listViewActive) {
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