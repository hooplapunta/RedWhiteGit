package me.redwhite.redwhite.fragments;

import android.animation.ObjectAnimator;
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
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.android.Utils;
import com.cloudinary.utils.ObjectUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.joda.time.Interval;
import org.parceler.Parcels;

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
import java.util.Map;

import me.redwhite.redwhite.R;
import me.redwhite.redwhite.SingleProfileActivity;
import me.redwhite.redwhite.models.Quest;
import me.redwhite.redwhite.models.Question;
import me.redwhite.redwhite.models.QuestionAnswer;
import me.redwhite.redwhite.models.QuestionOption;
import me.redwhite.redwhite.models.User;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link me.redwhite.redwhite.fragments.QuestInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link me.redwhite.redwhite.fragments.QuestInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Quest quest;
    private ArrayList<Question> questionList;
    private ArrayList<Boolean> completeList;

    private Activity rootActivity;
    private View rootView;

    private ProgressBar pbTimeLeft;
    private ProgressBar pbProgress;

    private OnFragmentInteractionListener mListener;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SingleQuestionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuestInfoFragment newInstance(Quest qu, ArrayList<Question> questionList, ArrayList<Boolean> completeList) {
        QuestInfoFragment fragment = new QuestInfoFragment();
        Bundle args = new Bundle();

        Parcelable quParcel = Parcels.wrap(qu);
        args.putParcelable("quest", quParcel);

        Parcelable qlParcel = Parcels.wrap(questionList);
        args.putParcelable("questionList", qlParcel);

        Parcelable coParcel = Parcels.wrap(completeList);
        args.putParcelable("completeList", coParcel);

        fragment.setArguments(args);
        return fragment;
    }

    public QuestInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            quest = Parcels.unwrap(getArguments().getParcelable("quest"));
            questionList = Parcels.unwrap(getArguments().getParcelable("questionList"));
            completeList = Parcels.unwrap(getArguments().getParcelable("completeList"));

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_quest_info, container, false);
        rootView = view;

        ((TextView)view.findViewById(R.id.tvQuestTitle)).setText(quest.getName());
        ((TextView)view.findViewById(R.id.tvQuestDescription)).setText(quest.getDescription());

        new CountDownTimer(2000, 2000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                pbTimeLeft = (ProgressBar)view.findViewById(R.id.pbTimeLeft);
                pbTimeLeft.setIndeterminate(false);
                pbTimeLeft.setMax((int) (quest.getEnd_datetime() * 1000 - quest.getStart_datetime() * 1000));
                pbTimeLeft.setProgress(0);

                ObjectAnimator animation = ObjectAnimator.ofInt(pbTimeLeft, "progress", (int) (quest.getEnd_datetime() * 1000 - quest.getStart_datetime() * 1000), (int)(quest.getEnd_datetime()*1000 - System.currentTimeMillis()));
                animation.setDuration(1000);
                animation.setInterpolator(new DecelerateInterpolator());
                animation.start();

                String timeLeft = DateUtils.getRelativeTimeSpanString(quest.getEnd_datetime()*1000, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS).toString();
                final TextView tvTimeLeft = ((TextView) view.findViewById(R.id.tvTimeLeft));
                tvTimeLeft.setText("Quest ends " +timeLeft);

                new CountDownTimer(600*1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        //this will be done every 1000 milliseconds ( 1 seconds )
                        pbTimeLeft.setProgress((int)(quest.getEnd_datetime()*1000 - System.currentTimeMillis()));
                        String timeLeft = DateUtils.getRelativeTimeSpanString(quest.getEnd_datetime()*1000, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS).toString();
                        tvTimeLeft.setText("Quest ends " +timeLeft);
                    }

                    @Override
                    public void onFinish() {
                    }

                }.start();

                pbProgress = (ProgressBar)view.findViewById(R.id.pbProgress);
                pbProgress.setIndeterminate(false);
                pbProgress.setMax(questionList.size()*100);
                int count = 0;
                for(Boolean complete : completeList) {
                    if(complete) {
                        count++;
                    }
                }
                ObjectAnimator aanimation = ObjectAnimator.ofInt(pbProgress, "progress", 0, count*100);
                aanimation.setDuration(1000);
                aanimation.setInterpolator(new DecelerateInterpolator());
                aanimation.start();

                ((TextView) view.findViewById(R.id.tvProgress)).setText((questionList.size() - count) +" questions left");
            }
        }.start();

        return view;
    }

    public void updateProgress(ArrayList<Boolean> completeList) {
        int count = 0;
        for(Boolean complete : completeList) {
            if(complete) {
                count++;
            }
        }

        pbProgress.setProgress(count);
    }

    private void showAnsweredToast() {
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
