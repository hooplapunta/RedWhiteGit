package me.redwhite.redwhite.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.redwhite.redwhite.R;
import me.redwhite.redwhite.SingleProfileActivity;
import me.redwhite.redwhite.models.Question;
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
public class SingleQuestionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Question question;
    private User user;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SingleQuestionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SingleQuestionFragment newInstance(Question q, User u) {
        SingleQuestionFragment fragment = new SingleQuestionFragment();
        Bundle args = new Bundle();

        Parcelable qParcel = Parcels.wrap(q);
        Parcelable uParcel = Parcels.wrap(u);

        args.putParcelable("question", qParcel);
        args.putParcelable("user", uParcel);

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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_question, container, false);

        final LinearLayout layout = (LinearLayout) view.findViewById(R.id.linearLayoutCard);
        LinearLayout.LayoutParams buttonMargins = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonMargins.setMargins(0, 16, 0, 16);

        LinearLayout.LayoutParams narrowButtonMargins = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,110);
        narrowButtonMargins.setMargins(0, 12, 0, 12);

        TextView tvUser = (TextView) layout.findViewById(R.id.tvUsername);
        tvUser.setText(user.getKey() +" asked:");
        tvUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), SingleProfileActivity.class);
                //TODO: Hookup to the right user id
                myIntent.putExtra("username", "BAA");
                startActivity(myIntent);
            }
        });

        switch(question.getType())
        {
            case "twooption":
                // option 1 and option 2
                final Button option1 = new Button(getActivity());
                final Button option2 = new Button(getActivity());

                QuestionOption o1 = question.get_options().get(0);
                if (o1 != null) {
                    option1.setText(o1.getKey());
                    option1.setBackgroundColor(Color.parseColor("#EF5350"));
                    option1.setTextColor(Color.WHITE);
                    option1.setElevation(2);
                    option1.setLayoutParams(buttonMargins);

                    option1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            layout.removeView(option2);

                            LinearLayout.LayoutParams tvMargins = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                            tvMargins.setMargins(0, 24, 0, 16);

                            TextView yourtv = new TextView(getActivity());
                            yourtv.setTextColor(Color.parseColor("#2196f3"));
                            yourtv.setText("Thanks for your feedback. Click to find out how SMRT is improving your daily ride on train services in Singapore every day.");
                            yourtv.setLayoutParams(tvMargins);
                            layout.addView(yourtv);

                        }
                    });

                    layout.addView(option1);
                }

                QuestionOption o2 = question.get_options().get(1);
                if (o1 != null) {
                    option2.setText(o2.getKey());
                    option2.setBackgroundColor(Color.parseColor("#42A5F5"));
                    option2.setTextColor(Color.WHITE);
                    option2.setElevation(2);
                    option2.setLayoutParams(buttonMargins);
                    layout.addView(option2);

                    option2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            layout.removeView(option1);

                            LinearLayout.LayoutParams tvMargins = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                            tvMargins.setMargins(0, 24, 0, 16);

                            TextView yourtv = new TextView(getActivity());
                            yourtv.setTextColor(Color.parseColor("#2196f3"));
                            yourtv.setText("Thanks for your feedback. Click to find out how SMRT is improving your daily ride on train services in Singapore every day.");
                            yourtv.setLayoutParams(tvMargins);
                            layout.addView(yourtv);

                        }
                    });
                }
                break;
            case "multioption":
                for(QuestionOption qo : question.get_options())
                {
                    if (qo != null) {
                        Button button = new Button(getActivity());
                        button.setText(qo.getKey());
                        button.setBackgroundColor(Color.parseColor("#CFD8DC"));
                        button.setTextColor(Color.BLACK);
                        button.setTextSize(12f);
                        button.setElevation(2);
                        button.setPadding(0, 2, 0, 2);
                        button.setLayoutParams(narrowButtonMargins);

                        layout.addView(button);
                    }
                }
                break;
            case "rating":
                RatingBar rb = new RatingBar(getActivity());
                rb.setNumStars(5);
                LinearLayout.LayoutParams ratingMargins = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                ratingMargins.setMargins(0, 16, 0, 16);
                ratingMargins.gravity = Gravity.CENTER_HORIZONTAL;
                rb.setLayoutParams(ratingMargins);
                rb.setStepSize(1);
                layout.addView(rb);

                LinearLayout ll = new LinearLayout(getActivity());
                ll.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayout.LayoutParams labels = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                labels.setMargins(0, 16, 0, 16);

                //sort options before putting in:
                Collections.sort(question.get_options(), new Comparator<QuestionOption>() {
                    @Override
                    public int compare(QuestionOption p1, QuestionOption p2) {
                        return p1.getKey().charAt(0) - p2.getKey().charAt(0); // Ascending
                    }
                });

                TextView tv1 = new TextView(getActivity());
                tv1.setText(question.get_options().get(0).getKey());
                tv1.setTextSize(12);
                tv1.setLayoutParams(labels);
                tv1.setGravity(Gravity.LEFT);
                ll.addView(tv1);

                TextView tv2 = new TextView(getActivity());
                tv2.setText(question.get_options().get(4).getKey());
                tv2.setTextSize(12);
                tv2.setLayoutParams(labels);
                tv2.setGravity(Gravity.RIGHT);
                ll.addView(tv2);

                layout.addView(ll);

                Button button = new Button(getActivity());
                button.setText("RATE!");
                button.setBackgroundColor(Color.parseColor("#42A5F5"));
                button.setTextColor(Color.WHITE);
                button.setTextSize(12f);
                button.setElevation(2);
                button.setPadding(0, 2, 0, 2);
                button.setLayoutParams(narrowButtonMargins);

                layout.addView(button);

                break;
            case "fuzzytext":
                EditText text = new EditText(getActivity());
                text.setHint("Enter the correct answer...");
                text.setElevation(2);
                text.setLayoutParams(buttonMargins);
                text.setMaxLines(1);
                text.setSingleLine();
                text.setImeOptions(EditorInfo.IME_ACTION_GO);

                layout.addView(text);
                break;

            case "photo":
                Button camerabutton = new Button(getActivity());
                camerabutton.setText("Take a photo");
                camerabutton.setBackgroundColor(Color.parseColor("#F44336"));
                camerabutton.setTextColor(Color.WHITE);
                camerabutton.setElevation(2);
                camerabutton.setLayoutParams(buttonMargins);

                layout.addView(camerabutton);
                break;

            default:
                break;
        }

        TextView tvQuestionText = (TextView) view.findViewById(R.id.tvQuestionText);
        tvQuestionText.setText(question.getQuestion());

        return view;
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
