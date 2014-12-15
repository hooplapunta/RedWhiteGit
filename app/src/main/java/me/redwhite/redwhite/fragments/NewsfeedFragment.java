package me.redwhite.redwhite.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import me.redwhite.redwhite.R;
import me.redwhite.redwhite.dummy.DummyContent;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class NewsfeedFragment extends ListFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    ListView list;
    String userId;
    String questionDetail;

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

    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types of parameters
    public static NewsfeedFragment newInstance(String param1, String param2) {
        NewsfeedFragment fragment = new NewsfeedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NewsfeedFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }




        CustomListView adapter = new CustomListView(getActivity(),questions,imageId,username);


        setListAdapter(adapter);
        // TODO: Change Adapter to display your content
        //setListAdapter(new ArrayAdapter<DummyContent.DummyItem>(getActivity(),
        //        android.R.layout.simple_list_item_1, android.R.id.text1, DummyContent.ITEMS));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        list = getListView();

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
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
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
        public void onFragmentInteraction(String id);
    }

}
