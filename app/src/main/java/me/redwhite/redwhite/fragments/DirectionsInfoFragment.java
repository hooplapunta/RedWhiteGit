package me.redwhite.redwhite.fragments;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.redwhite.redwhite.R;
import me.redwhite.redwhite.models.Quest;
import me.redwhite.redwhite.models.Question;
import me.redwhite.redwhite.models.User;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link me.redwhite.redwhite.fragments.DirectionsInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link me.redwhite.redwhite.fragments.DirectionsInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DirectionsInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    ArrayList<String> instructions;
    ArrayList<Integer> distances;
    ArrayList<Integer> durations;

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
    public static DirectionsInfoFragment newInstance(ArrayList<String> instructions, ArrayList<Integer> distances, ArrayList<Integer> durations) {
        DirectionsInfoFragment fragment = new DirectionsInfoFragment();
        Bundle args = new Bundle();

        Parcelable iParcel = Parcels.wrap(instructions);
        args.putParcelable("instructions", iParcel);

        Parcelable diParcel = Parcels.wrap(distances);
        args.putParcelable("distances", diParcel);

        Parcelable duParcel = Parcels.wrap(durations);
        args.putParcelable("durations", duParcel);

        fragment.setArguments(args);
        return fragment;
    }

    public DirectionsInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            instructions = Parcels.unwrap(getArguments().getParcelable("instructions"));
            distances = Parcels.unwrap(getArguments().getParcelable("distances"));
            durations = Parcels.unwrap(getArguments().getParcelable("durations"));

        }

        rootActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_quest_directions, container, false);
        rootView = view;

        int totalTime = 0;
        for(Integer t : durations) {
            totalTime = totalTime + t;
        }
        totalTime = totalTime/60;

        int totalDistance = 0;
        for(Integer t : distances) {
            totalDistance = totalDistance + t;
        }

        double finalValue = Math.round( (totalDistance/1000) * 100.0 ) / 100.0;

        ((TextView)view.findViewById(R.id.tvQuestTitle)).setText("Directions: " +totalTime +" min (" +finalValue +"km)");

        new CountDownTimer(2000, 2000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                ListView lvRoute = (ListView) view.findViewById(R.id.lvRoute);

                List<Map<String, String>> data = new ArrayList<Map<String, String>>();
                for (int i=0; i < instructions.size(); i++) {

                    int t = durations.get(i)/60;
                    if(t == 0) {
                        t = 1;
                    }

                    Map<String, String> datum = new HashMap<String, String>(2);
                    datum.put("title", Html.fromHtml(instructions.get(i)).toString());
                    datum.put("date", t  +" min (" +distances.get(i) +"m) \n");
                    data.add(datum);
                }
                SimpleAdapter adapter = new SimpleAdapter(rootActivity, data,
                        android.R.layout.simple_list_item_2,
                        new String[] {"title", "date"},
                        new int[] {android.R.id.text1,
                                android.R.id.text2});
                lvRoute.setAdapter(adapter);

            }
        }.start();

        return view;
    }

    public void updateDirections(ArrayList<String> instructions, ArrayList<Integer> distances, ArrayList<Integer> durations) {
        int count = 0;

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
