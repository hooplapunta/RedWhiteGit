package me.redwhite.redwhite.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.android.Utils;
import com.cloudinary.utils.ObjectUtils;
import com.firebase.client.Firebase;
import com.firebase.client.ServerValue;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.redwhite.redwhite.R;
import me.redwhite.redwhite.SingleProfileActivity;
import me.redwhite.redwhite.models.Quest;
import me.redwhite.redwhite.models.Question;
import me.redwhite.redwhite.models.QuestionAnswer;
import me.redwhite.redwhite.models.QuestionOption;
import me.redwhite.redwhite.models.User;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SingleQuestionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SingleQuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SingleQuestionFragment extends Fragment
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Question question;
    private User user;
    private boolean isComplete;
    private boolean isQuest;
    private Quest quest;

    private boolean isFirstComplete;

    private Activity rootActivity;

    private OnFragmentInteractionListener mListener;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private int cameraButtonId;
    private int qrcodeButtonId;
    private String mCurrentPhotoPath;

    private View rootView;
    private LinearLayout rootLayout;
    private ImageView qrcodeImage;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SingleQuestionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SingleQuestionFragment newInstance(Question q, User u, boolean isComplete, boolean isQuest, Quest qu) {
        SingleQuestionFragment fragment = new SingleQuestionFragment();
        Bundle args = new Bundle();

        Parcelable qParcel = Parcels.wrap(q);
        Parcelable uParcel = Parcels.wrap(u);

        args.putParcelable("question", qParcel);
        args.putParcelable("user", uParcel);
        args.putBoolean("isComplete", isComplete);
        args.putBoolean("isQuest", isQuest);

        if(isQuest) {
            Parcelable quParcel = Parcels.wrap(qu);
            args.putParcelable("quest", quParcel);
        }

        fragment.setArguments(args);
        return fragment;
    }

    public SingleQuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            question = Parcels.unwrap(getArguments().getParcelable("question"));
            user = Parcels.unwrap(getArguments().getParcelable("user"));
            isComplete = getArguments().getBoolean("isComplete", false);
            isQuest = getArguments().getBoolean("isQuest", false);
            if (isQuest) {
                quest = Parcels.unwrap(getArguments().getParcelable("quest"));
            }
            isFirstComplete = false;
        }
        buildGoogleApiClient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_question, container, false);
        rootView = view;
        loadQuestion(view);

        return view;
    }

    public void loadQuestion(View view) {
        if (isAdded() && !isFirstComplete) {
            rootLayout = (LinearLayout) view.findViewById(R.id.linearLayoutCard);
            final LinearLayout layout = (LinearLayout) view.findViewById(R.id.questionControls);
            layout.removeAllViews();

            LinearLayout.LayoutParams buttonMargins = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            buttonMargins.setMargins(0, 16, 0, 16);

            final LinearLayout.LayoutParams narrowButtonMargins = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,110);
            narrowButtonMargins.setMargins(0, 12, 0, 12);

            TextView tvUser = (TextView) rootLayout.findViewById(R.id.tvUsername);
            tvUser.setText(user.getKey() +" asked:");
            tvUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(getActivity(), SingleProfileActivity.class);
                    //TODO: Hookup to the right user id
                    myIntent.putExtra("username", user.getKey());
                    startActivity(myIntent);
                }
            });

            if (!isComplete) {

                LocationManager mLocationManager = (LocationManager) rootActivity.getSystemService(rootActivity.LOCATION_SERVICE);
                Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (location != null) {
                    // check for user's location
                    Location qLocation = new Location("question");
                    qLocation.setLatitude(question.get_location().getLat());
                    qLocation.setLongitude(question.get_location().getLng());
                    qLocation.setTime(System.currentTimeMillis());

                    float distance = location.distanceTo(qLocation);
                    if (distance < 500f) {
                        switch(question.getType())
                        {
                            case "twooption":
                                // option 1 and option 2
                                final Button option1 = new Button(rootActivity);
                                final Button option2 = new Button(rootActivity);

                                QuestionOption o1 = question.get_options().get(0);
                                if (o1 != null) {
                                    option1.setText(o1.getKey());
                                    option1.setBackgroundColor(Color.parseColor("#EF5350"));
                                    option1.setTextColor(Color.WHITE);
                                    option1.setElevation(2);
                                    option1.setLayoutParams(buttonMargins);
                                    layout.addView(option1);
                                    option1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (mLastLocation != null) {
                                                // setup question answer to send
                                                QuestionAnswer qa = new QuestionAnswer(
                                                        user.getKey(),
                                                        mLastLocation.getLatitude(),
                                                        mLastLocation.getLongitude(),
                                                        "...",
                                                        question.getQuestion(),
                                                        question.get_options().get(0).getKey(),
                                                        System.currentTimeMillis()/1000,
                                                        question.getKey()
                                                );

                                                QuestionAnswer.addNodeToFirebase(question.getKey(), question.get_options().get(0).getKey(), qa);

                                                if(isQuest) {
                                                    Quest.completeQuestQuestion(quest.getShortname(), user.getKey(), question.getKey());
                                                }
                                                isComplete = true;
                                                isFirstComplete = true;
                                                mListener.completeQuestion(question, user, isComplete, isQuest, quest);

                                                LayoutInflater tinflater = rootActivity.getLayoutInflater();
                                                View toastlayout = tinflater.inflate(R.layout.toast_crowdops, (ViewGroup)rootActivity.findViewById(R.id.toastContainer));
                                                TextView text = (TextView) toastlayout.findViewById(R.id.toastTitle);
                                                text.setText("Question Complete!");
                                                TextView subtext = (TextView) toastlayout.findViewById(R.id.toastSubTitle);
                                                subtext.setText("+50 Ops Points, spend it on powerups to get ahead!");
                                                Toast toast = new Toast(rootActivity);
                                                toast.setGravity(Gravity.BOTTOM, 0, 0);
                                                toast.setDuration(Toast.LENGTH_LONG);
                                                toast.setView(toastlayout);
                                                toast.show();

                                                //Toast.makeText(rootActivity, "Awesome! You completed the question.", Toast.LENGTH_SHORT).show();

                                                // disable current button
                                                // display show answer button
                                                layout.removeView(option2);
                                                option1.setEnabled(false);

                                                Button button = new Button(rootActivity);
                                                button.setText("Show Community Answers");
                                                button.setBackgroundColor(Color.parseColor("#2196f3"));
                                                button.setTextColor(Color.WHITE);
                                                button.setTextSize(12f);
                                                button.setElevation(2);
                                                button.setPadding(0, 2, 0, 2);
                                                button.setLayoutParams(narrowButtonMargins);
                                                button.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent myIntent = new Intent(rootActivity,QuestionDetailActivity.class);
                                                        myIntent.putExtra("username", user.getKey());
                                                        myIntent.putExtra("question", question.getKey());
                                                        startActivity(myIntent);
                                                    }
                                                });

                                                layout.addView(button);

                                                // send intent to question detail with new button
                                                // abstract to standard method

                                            } else {
                                                Toast.makeText(rootActivity, "Try again. Waiting for location...", Toast.LENGTH_SHORT).show();
                                            }

                                            // previous followup action code
                                            //                            LinearLayout.LayoutParams tvMargins = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                                            //                            tvMargins.setMargins(0, 24, 0, 16);
                                            //
                                            //                            TextView yourtv = new TextView(rootActivity);
                                            //                            yourtv.setTextColor(Color.parseColor("#2196f3"));
                                            //                            yourtv.setText("Thanks for your feedback. Click to find out how SMRT is improving your daily ride on train services in Singapore every day.");
                                            //                            yourtv.setLayoutParams(tvMargins);
                                            //                            layout.addView(yourtv);

                                        }
                                    });

                                }

                                QuestionOption o2 = question.get_options().get(1);
                                if (o2 != null) {
                                    option2.setText(o2.getKey());
                                    option2.setBackgroundColor(Color.parseColor("#42A5F5"));
                                    option2.setTextColor(Color.WHITE);
                                    option2.setElevation(2);
                                    option2.setLayoutParams(buttonMargins);
                                    layout.addView(option2);

                                    option2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            if (mLastLocation != null) {
                                                // setup question answer to send
                                                QuestionAnswer qa = new QuestionAnswer(
                                                        user.getKey(),
                                                        mLastLocation.getLatitude(),
                                                        mLastLocation.getLongitude(),
                                                        "...",
                                                        question.getQuestion(),
                                                        question.get_options().get(1).getKey(),
                                                        System.currentTimeMillis()/1000,
                                                        question.getKey()
                                                );

                                                QuestionAnswer.addNodeToFirebase(question.getKey(), question.get_options().get(1).getKey(), qa);
                                                if(isQuest) {
                                                    Quest.completeQuestQuestion(quest.getShortname(), user.getKey(), question.getKey());
                                                }
                                                isComplete = true;
                                                isFirstComplete = true;
                                                mListener.completeQuestion(question, user, isComplete, isQuest, quest);
                                                Toast.makeText(rootActivity, "Awesome! You completed the question.", Toast.LENGTH_SHORT).show();

                                                // disable current button
                                                // display show answer button
                                                layout.removeView(option1);
                                                option2.setEnabled(false);

                                                Button button = new Button(rootActivity);
                                                button.setText("Show Community Answers");
                                                button.setBackgroundColor(Color.parseColor("#2196f3"));
                                                button.setTextColor(Color.WHITE);
                                                button.setTextSize(12f);
                                                button.setElevation(2);
                                                button.setPadding(0, 2, 0, 2);
                                                button.setLayoutParams(narrowButtonMargins);
                                                button.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent myIntent = new Intent(rootActivity,QuestionDetailActivity.class);
                                                        myIntent.putExtra("username", user.getKey());
                                                        myIntent.putExtra("question", question.getKey());
                                                        startActivity(myIntent);
                                                    }
                                                });

                                                layout.addView(button);

                                                // send intent to question detail with new button
                                                // abstract to standard method

                                            } else {
                                                Toast.makeText(rootActivity, "Try again. Waiting for location...", Toast.LENGTH_SHORT).show();
                                            }


                                            //                            LinearLayout.LayoutParams tvMargins = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                                            //                            tvMargins.setMargins(0, 24, 0, 16);
                                            //
                                            //                            TextView yourtv = new TextView(rootActivity);
                                            //                            yourtv.setTextColor(Color.parseColor("#2196f3"));
                                            //                            yourtv.setText("Thanks for your feedback. Click to find out how SMRT is improving your daily ride on train services in Singapore every day.");
                                            //                            yourtv.setLayoutParams(tvMargins);
                                            //                            layout.addView(yourtv);
                                        }
                                    });
                                }
                                break;
                            case "multioption":

                                final ScrollView scroll = new ScrollView(rootActivity);
                                scroll.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                                scroll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                                final LinearLayout lis = new LinearLayout(rootActivity);
                                lis.setOrientation(LinearLayout.VERTICAL);
                                lis.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                                lis.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));

                                int buttonid = 0;
                                for(final QuestionOption qo : question.get_options())
                                {
                                    if (qo != null) {
                                        final Button button = new Button(rootActivity);
                                        button.setText(qo.getKey());
                                        button.setBackgroundColor(Color.parseColor("#CFD8DC"));
                                        button.setTextColor(Color.BLACK);
                                        button.setId(buttonid);
                                        button.setTextSize(12f);
                                        button.setElevation(2);
                                        button.setPadding(0, 2, 0, 2);
                                        button.setLayoutParams(narrowButtonMargins);

                                        button.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (mLastLocation != null) {
                                                    // setup question answer to send
                                                    QuestionAnswer qa = new QuestionAnswer(
                                                            user.getKey(),
                                                            mLastLocation.getLatitude(),
                                                            mLastLocation.getLongitude(),
                                                            "...",
                                                            question.getQuestion(),
                                                            qo.getKey(),
                                                            System.currentTimeMillis()/1000,
                                                            question.getKey()
                                                    );

                                                    QuestionAnswer.addNodeToFirebase(question.getKey(), qo.getKey(), qa);
                                                    if(isQuest) {
                                                        Quest.completeQuestQuestion(quest.getShortname(), user.getKey(), question.getKey());
                                                    }
                                                    isComplete = true;
                                                    isFirstComplete = true;
                                                    mListener.completeQuestion(question, user, isComplete, isQuest, quest);
                                                    Toast.makeText(rootActivity, "Awesome! You completed the question.", Toast.LENGTH_SHORT).show();

                                                    // disable current button
                                                    // display show answer button
                                                    scroll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                                    lis.removeAllViews();
                                                    lis.addView(button);
                                                    button.setEnabled(false);

                                                    Button rbutton = new Button(rootActivity);
                                                    rbutton.setText("Show Community Answers");
                                                    rbutton.setBackgroundColor(Color.parseColor("#2196f3"));
                                                    rbutton.setTextColor(Color.WHITE);
                                                    rbutton.setTextSize(12f);
                                                    rbutton.setElevation(2);
                                                    rbutton.setPadding(0, 2, 0, 2);
                                                    rbutton.setLayoutParams(narrowButtonMargins);
                                                    rbutton.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            Intent myIntent = new Intent(rootActivity,QuestionDetailActivity.class);
                                                            myIntent.putExtra("username", user.getKey());
                                                            myIntent.putExtra("question", question.getKey());
                                                            startActivity(myIntent);
                                                        }
                                                    });

                                                    layout.addView(rbutton);

                                                    // send intent to question detail with new button
                                                    // abstract to standard method

                                                } else {
                                                    Toast.makeText(rootActivity, "Try again. Waiting for location...", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                        lis.addView(button);
                                        buttonid++;
                                    }
                                }

                                scroll.addView(lis);
                                layout.addView(scroll);

                                break;
                            case "rating":
                                final RatingBar rb = new RatingBar(rootActivity);
                                rb.setNumStars(5);
                                LinearLayout.LayoutParams ratingMargins = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                ratingMargins.setMargins(0, 16, 0, 0);
                                ratingMargins.gravity = Gravity.CENTER_HORIZONTAL;
                                rb.setLayoutParams(ratingMargins);
                                rb.setStepSize(1);
                                LayerDrawable stars = (LayerDrawable) rb.getProgressDrawable();
                                stars.getDrawable(2).setColorFilter(Color.parseColor("#2196F3"), PorterDuff.Mode.SRC_ATOP);
                                layout.addView(rb);

                                RelativeLayout relativeLayout = new RelativeLayout(rootActivity);
                                relativeLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                //sort options before putting in:
                                Collections.sort(question.get_options(), new Comparator<QuestionOption>() {
                                    @Override
                                    public int compare(QuestionOption p1, QuestionOption p2) {
                                        return p1.getKey().charAt(0) - p2.getKey().charAt(0); // Ascending
                                    }
                                });

                                TextView tv1 = new TextView(rootActivity);
                                tv1.setText(question.get_options().get(0).getKey().split(";")[1]);
                                tv1.setTextSize(12);
                                RelativeLayout.LayoutParams left = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                left.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                tv1.setLayoutParams(left);
                                tv1.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                                relativeLayout.addView(tv1);

                                TextView tv2 = new TextView(rootActivity);
                                tv2.setText(question.get_options().get(4).getKey().split(";")[1]);
                                tv2.setTextSize(12);
                                RelativeLayout.LayoutParams right = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                right.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                                tv2.setLayoutParams(right);
                                tv2.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                                relativeLayout.addView(tv2);

                                layout.addView(relativeLayout);

                                final Button button = new Button(rootActivity);
                                button.setText("RATE!");
                                button.setBackgroundColor(Color.parseColor("#42A5F5"));
                                button.setTextColor(Color.WHITE);
                                button.setTextSize(12f);
                                button.setElevation(2);
                                button.setPadding(0, 2, 0, 2);
                                narrowButtonMargins.setMargins(0, 16, 0, 2);
                                button.setLayoutParams(narrowButtonMargins);
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        float s = rb.getRating();
                                        int selected = (int) s;
                                        if (selected != 0) {
                                            if (mLastLocation != null) {

                                                // setup question answer to send
                                                QuestionAnswer qa = new QuestionAnswer(
                                                        user.getKey(),
                                                        mLastLocation.getLatitude(),
                                                        mLastLocation.getLongitude(),
                                                        "...",
                                                        question.getQuestion(),
                                                        question.get_options().get(selected-1).getKey(),
                                                        System.currentTimeMillis()/1000,
                                                        question.getKey()
                                                );

                                                QuestionAnswer.addNodeToFirebase(question.getKey(), question.get_options().get(selected-1).getKey(), qa);
                                                if(isQuest) {
                                                    Quest.completeQuestQuestion(quest.getShortname(), user.getKey(), question.getKey());
                                                }
                                                isComplete = true;
                                                isFirstComplete = true;
                                                mListener.completeQuestion(question, user, isComplete, isQuest, quest);
                                                Toast.makeText(rootActivity, "Awesome! You completed the question.", Toast.LENGTH_SHORT).show();
                                                // disable current button
                                                // display show answer button

                                                rb.setEnabled(false);
                                                button.setEnabled(false);

                                                Button rbutton = new Button(rootActivity);
                                                rbutton.setText("Show Community Answers");
                                                rbutton.setBackgroundColor(Color.parseColor("#2196f3"));
                                                rbutton.setTextColor(Color.WHITE);
                                                rbutton.setTextSize(12f);
                                                rbutton.setElevation(2);
                                                rbutton.setPadding(0, 2, 0, 2);
                                                //narrowButtonMargins.setMargins(0, 12, 0, 12);
                                                rbutton.setLayoutParams(narrowButtonMargins);
                                                rbutton.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent myIntent = new Intent(rootActivity,QuestionDetailActivity.class);
                                                        myIntent.putExtra("username", user.getKey());
                                                        myIntent.putExtra("question", question.getKey());
                                                        startActivity(myIntent);
                                                    }
                                                });
                                                layout.removeView(button);
                                                layout.addView(rbutton);

                                                // send intent to question detail with new button
                                                // abstract to standard method

                                            } else {
                                                Toast.makeText(rootActivity, "Try again. Waiting for location...", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(rootActivity, "Select a rating first!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                layout.addView(button);

                                break;
                            case "fuzzytext":
                                final EditText text = new EditText(rootActivity);
                                text.setHint("Enter the correct answer...");
                                text.setElevation(2);
                                text.setLayoutParams(buttonMargins);
                                text.setMaxLines(1);
                                text.setSingleLine();
                                text.setImeOptions(EditorInfo.IME_ACTION_GO);
                                text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                    @Override
                                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                                        if(actionId == EditorInfo.IME_ACTION_GO) {
                                            if (mLastLocation != null) {
                                                // setup question answer to send
                                                QuestionAnswer qa = new QuestionAnswer(
                                                        user.getKey(),
                                                        mLastLocation.getLatitude(),
                                                        mLastLocation.getLongitude(),
                                                        "...",
                                                        question.getQuestion(),
                                                        text.getText().toString(),
                                                        System.currentTimeMillis()/1000,
                                                        question.getKey()
                                                );

                                                QuestionAnswer.addNodeToFirebase(question.getKey(), question.get_options().get(0).getKey(), qa);
                                                if(isQuest) {
                                                    Quest.completeQuestQuestion(quest.getShortname(), user.getKey(), question.getKey());
                                                }
                                                isComplete = true;
                                                isFirstComplete = true;
                                                mListener.completeQuestion(question, user, isComplete, isQuest, quest);
                                                Toast.makeText(rootActivity, "Awesome! You completed the question.", Toast.LENGTH_SHORT).show();
                                                // disable current button
                                                // display show answer button
                                                text.setEnabled(false);

                                                Button rbutton = new Button(rootActivity);
                                                rbutton.setText("Show Community Answers");
                                                rbutton.setBackgroundColor(Color.parseColor("#2196f3"));
                                                rbutton.setTextColor(Color.WHITE);
                                                rbutton.setTextSize(12f);
                                                rbutton.setElevation(2);
                                                rbutton.setPadding(0, 2, 0, 2);
                                                rbutton.setLayoutParams(narrowButtonMargins);
                                                rbutton.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent myIntent = new Intent(rootActivity,QuestionDetailActivity.class);
                                                        myIntent.putExtra("username", user.getKey());
                                                        myIntent.putExtra("question", question.getKey());
                                                        startActivity(myIntent);
                                                    }
                                                });

                                                layout.addView(rbutton);

                                                // send intent to question detail with new button
                                                // abstract to standard method

                                            } else {
                                                Toast.makeText(rootActivity, "Try again. Waiting for location...", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        return false;
                                    }
                                });

                                layout.addView(text);
                                break;

                            case "photo":
                                Button camerabutton = new Button(rootActivity);
                                camerabutton.setText("Take a photo");
                                camerabutton.setBackgroundColor(Color.parseColor("#F44336"));
                                camerabutton.setTextColor(Color.WHITE);
                                camerabutton.setElevation(2);
                                camerabutton.setLayoutParams(buttonMargins);
                                cameraButtonId = View.generateViewId();
                                camerabutton.setId(cameraButtonId);
                                camerabutton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                                        String imageFileName = "CrowdOps_" + timeStamp + "_";
                                        File storageDir = Environment.getExternalStoragePublicDirectory(
                                                Environment.DIRECTORY_PICTURES);
                                        File image = null;
                                        try {
                                            image = File.createTempFile(
                                                    imageFileName,  /* prefix */
                                                    ".jpg",         /* suffix */
                                                    storageDir      /* directory */
                                            );
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        mCurrentPhotoPath = image.getAbsolutePath();

                                        if (image != null) {
                                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
                                            startActivityForResult(cameraIntent, 1337);
                                        }
                                    }
                                });

                                layout.addView(camerabutton);
                                break;
                            case "qrcode":
                                ImageView iv = new ImageView(rootActivity);
                                iv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 300));
                                iv.setImageResource(R.drawable.ic_opscodered);
                                qrcodeImage = iv;
                                layout.addView(iv);

                                Button qrbutton = new Button(rootActivity);
                                qrbutton.setText("Scan Ops Code");
                                qrbutton.setBackgroundColor(Color.parseColor("#F44336"));
                                qrbutton.setTextColor(Color.WHITE);
                                qrbutton.setElevation(2);
                                qrbutton.setLayoutParams(buttonMargins);
                                qrcodeButtonId = View.generateViewId();
                                qrbutton.setId(qrcodeButtonId);
                                qrbutton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
//                                        IntentIntegrator integrator = new IntentIntegrator(rootActivity);
//                                        integrator.initiateScan();

                                        Intent intent = new Intent(
                                                "com.google.zxing.client.android.SCAN");
                                        intent.putExtra("SCAN_FORMATS", "QR_CODE_MODE");
                                        startActivityForResult(intent,
                                                IntentIntegrator.REQUEST_CODE);

                                    }
                                });

                                layout.addView(qrbutton);

                                break;
                            default:
                                break;
                        }
                    } else {
                        // not close enough!
                        TextView tv1 = new TextView(rootActivity);
                        tv1.setText("You need to be within 500m to answer this question!");
                        tv1.setTextSize(16);
                        tv1.setTypeface(tv1.getTypeface(), Typeface.BOLD);
                        tv1.setLayoutParams(buttonMargins);
                        tv1.setGravity(Gravity.CENTER);
                        tv1.setTextColor(Color.RED);
                        layout.addView(tv1);
                    }
                }


            } else {
                // IS COMPLETE
                TextView tv1 = new TextView(rootActivity);
                tv1.setText("You have answered this question!");
                tv1.setTextSize(16);
                tv1.setTypeface(tv1.getTypeface(), Typeface.BOLD);
                tv1.setLayoutParams(buttonMargins);
                tv1.setGravity(Gravity.CENTER);
                layout.addView(tv1);

                Button rbutton = new Button(rootActivity);
                rbutton.setText("Show Community Answers");
                rbutton.setBackgroundColor(Color.parseColor("#2196f3"));
                rbutton.setTextColor(Color.WHITE);
                rbutton.setTextSize(12f);
                rbutton.setElevation(2);
                rbutton.setPadding(0, 2, 0, 2);
                rbutton.setLayoutParams(narrowButtonMargins);
                rbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent myIntent = new Intent(rootActivity,QuestionDetailActivity.class);
                        myIntent.putExtra("username", user.getKey());
                        myIntent.putExtra("question", question.getKey());
                        startActivity(myIntent);
                    }
                });

                layout.addView(rbutton);

            }

            TextView tvQuestionText = (TextView) rootLayout.findViewById(R.id.tvQuestionText);
            tvQuestionText.setText(question.getQuestion());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // photo action
        if(requestCode == 1337 && resultCode == Activity.RESULT_OK) {

            final LinearLayout layout = rootLayout;
            final LinearLayout.LayoutParams narrowButtonMargins = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 110);
            narrowButtonMargins.setMargins(0, 12, 0, 12);

            final Button camerabutton = (Button) layout.findViewById(cameraButtonId);
            final ImageView iv = new ImageView(rootActivity);

            if (mLastLocation != null) {

                class CloudinaryUpload extends AsyncTask<InputStream, Boolean, String> {

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                    }

                    @Override
                    protected String doInBackground(InputStream... params) {
                        Cloudinary cloudinary = new Cloudinary(Utils.cloudinaryUrlFromContext(rootActivity));
                        Map map = new HashMap<String, String>();
                        try {
                            map = cloudinary.uploader().upload(params[0], ObjectUtils.emptyMap());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (map.size() != 0) {
                            // send intent to question detail with new button
                            // abstract to standard method
                            return (String) map.get("url");
                        } else {
                            cancel(true);
                            return null;
                        }
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        // setup question answer to send
                        QuestionAnswer qa = new QuestionAnswer(
                                user.getKey(),
                                mLastLocation.getLatitude(),
                                mLastLocation.getLongitude(),
                                "...",
                                question.getQuestion(),
                                s,
                                System.currentTimeMillis() / 1000,
                                question.getKey()
                        );

                        QuestionAnswer.addNodeToFirebase(question.getKey(), question.get_options().get(0).getKey(), qa);
                        if (isQuest) {
                            Quest.completeQuestQuestion(quest.getShortname(), user.getKey(), question.getKey());
                        }
                        isComplete = true;
                        isFirstComplete = true;
                        mListener.completeQuestion(question, user, isComplete, isQuest, quest);
                        Toast.makeText(rootActivity, "Awesome! You completed the question.", Toast.LENGTH_SHORT).show();

                        // disable current button
                        // display show answer button
                        camerabutton.setEnabled(false);
                        layout.removeView(camerabutton);

                        Button rbutton = new Button(rootActivity);
                        rbutton.setText("Show Community Answers");
                        rbutton.setBackgroundColor(Color.parseColor("#2196f3"));
                        rbutton.setTextColor(Color.WHITE);
                        rbutton.setTextSize(12f);
                        rbutton.setElevation(2);
                        rbutton.setPadding(0, 2, 0, 2);
                        rbutton.setLayoutParams(narrowButtonMargins);
                        rbutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent myIntent = new Intent(rootActivity, QuestionDetailActivity.class);
                                myIntent.putExtra("username", user.getKey());
                                myIntent.putExtra("question", question.getKey());
                                startActivity(myIntent);
                            }
                        });

                        layout.addView(rbutton);

                        super.onPostExecute(s);
                    }

                    @Override
                    protected void onProgressUpdate(Boolean... values) {
                        super.onProgressUpdate(values);
                    }

                    @Override
                    protected void onCancelled() {
                        layout.removeView(iv);
                        Toast.makeText(rootActivity, "Try again. We weren't able to upload the photo.", Toast.LENGTH_SHORT).show();
                        super.onCancelled();
                    }
                }

                try {
                    iv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 300));
                    // Get the dimensions of the bitmap
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    bmOptions.inJustDecodeBounds = false;
                    bmOptions.inSampleSize = 4;
                    Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
                    iv.setImageBitmap(bitmap);

                    layout.addView(iv);

                    File file = new File(mCurrentPhotoPath);
                    FileInputStream fileInputStream = new FileInputStream(file);
                    new CloudinaryUpload().execute(fileInputStream);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    //layout.removeView(iv);
                    Toast.makeText(rootActivity, "Try again. We weren't able to save the photo.", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(rootActivity, "Try again. Waiting for location...", Toast.LENGTH_SHORT).show();
            }

        } else {
            // qrcode intent
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (scanResult != null) {

                final LinearLayout layout = rootLayout;
                final LinearLayout.LayoutParams narrowButtonMargins = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 110);
                narrowButtonMargins.setMargins(0, 12, 0, 12);

                final Button qrcodeButton = (Button) layout.findViewById(qrcodeButtonId);

                if (mLastLocation != null) {
                    // check if scan results match expected answer
                    String expected = question.get_options().get(0).getKey();
                    String result = scanResult.getContents();

                    if(expected.equals(result)) {
                        QuestionAnswer qa = new QuestionAnswer(
                                user.getKey(),
                                mLastLocation.getLatitude(),
                                mLastLocation.getLongitude(),
                                "...",
                                question.getQuestion(),
                                expected,
                                System.currentTimeMillis()/1000,
                                question.getKey()
                        );

                        QuestionAnswer.addNodeToFirebase(question.getKey(), question.get_options().get(0).getKey(), qa);
                        if(isQuest) {
                            Quest.completeQuestQuestion(quest.getShortname(), user.getKey(), question.getKey());
                        }
                        isComplete = true;
                        isFirstComplete = true;
                        mListener.completeQuestion(question, user, isComplete, isQuest, quest);
                        Toast.makeText(rootActivity, "Awesome! You completed the question.", Toast.LENGTH_SHORT).show();

                        qrcodeImage.setImageResource(R.drawable.ic_opscodegreen);
                        qrcodeButton.setEnabled(false);
                        ((LinearLayout)layout.findViewById(R.id.questionControls)).removeView(qrcodeButton);

                        Button rbutton = new Button(rootActivity);
                        rbutton.setText("Show Community Answers");
                        rbutton.setBackgroundColor(Color.parseColor("#2196f3"));
                        rbutton.setTextColor(Color.WHITE);
                        rbutton.setTextSize(12f);
                        rbutton.setElevation(2);
                        rbutton.setPadding(0, 2, 0, 2);
                        rbutton.setLayoutParams(narrowButtonMargins);
                        rbutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent myIntent = new Intent(rootActivity, QuestionDetailActivity.class);
                                myIntent.putExtra("username", user.getKey());
                                myIntent.putExtra("question", question.getKey());
                                startActivity(myIntent);
                            }
                        });

                        layout.addView(rbutton);

                    } else {
                        Toast.makeText(rootActivity, "Try again. That's not the right OpsCode!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(rootActivity, "Try again. Waiting for location...", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(rootActivity, "That didn't look like a QR code or photo.", Toast.LENGTH_SHORT).show();
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {

        rootActivity = activity;

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
        // update the location drawer
        loadQuestion(rootView);
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
        public void completeQuestion(Question q, User u, boolean isComplete, boolean isQuest, Quest qu);
    }

}
