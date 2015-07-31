package com.aflux.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.aflux.R;
import com.aflux.core.Gesture;
import com.aflux.core.Person;
import com.aflux.core.SensorValue;
import com.aflux.repository.Gestures;
import com.aflux.repository.People;
import com.aflux.ui.adapters.RemainingGesturesAdapter;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class RemainingGestures extends Fragment implements Gestures.OnRepositoryInteractionListener, People.OnRepositoryInteractionListener, AdapterView.OnItemClickListener{

    private static final String ARG_PARAM1 = "meId";
    private static final String ARG_PARAM2 = "resume";
    private final Gestures gestureRepo = Gestures.newInstance(this);

    private String meId;
    private boolean resume = false;
    private Person me;
    private List<Gesture> gestures;
    private final static String TAG = "RGFragment";

    private OnFragmentInteractionListener mListener;



    public static RemainingGestures newInstance(String meId, String resume) {
        RemainingGestures fragment = new RemainingGestures();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, meId);
        args.putString(ARG_PARAM2, resume);
        fragment.setArguments(args);
        return fragment;
    }

    public RemainingGestures() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            meId = getArguments().getString(ARG_PARAM1);
            me = Person.createWithoutData(Person.class, meId);

            resume = getArguments().getString(ARG_PARAM2).equals("yes");
            Log.i(TAG, "Resume " + String.valueOf(resume));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gestures, container, false);
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gestureRepo.get(); // calls onGesturesFound() on completion
    }

    @Override
    public void onDetach() {
        super.onDetach();
        gestures = null;
        mListener = null;
    }

    /**
     * Repository Interactions
     */

    @Override
    public void onGesturesFound(List<Gesture> gestures) {
        People peopleRepository = People.newInstance(this);
        this.gestures = gestures;
        if (resume) {
            Log.i(TAG, "In resume");
            peopleRepository.findGesturesStatus(me, gestures); // calls onGesturesStatusFound() on completion
            resume = false;
        } else {
            Log.i(TAG, "In no resume");
            ListView remainingGesturesList = (ListView) getActivity().findViewById(R.id.gestures_list);
            RemainingGesturesAdapter remainingGesturesAdapter = new RemainingGesturesAdapter(getActivity(), gestures);
            remainingGesturesList.setAdapter(remainingGesturesAdapter);
            remainingGesturesList.setOnItemClickListener(this);
        }
    }

    @Override
    public void onGestureFindException(ParseException e) {
        e.printStackTrace();
    }

    @Override
    public void onPeopleFound(List<Person> people){}

    @Override
    public void onGesturesStatusFound(List<Gesture> gestures) {
        Log.d(TAG, "Listner called");
        ListView remainingGesturesList = (ListView) getActivity().findViewById(R.id.gestures_list);
        RemainingGesturesAdapter remainingGesturesAdapter = new RemainingGesturesAdapter(getActivity(), gestures);
        remainingGesturesList.setAdapter(remainingGesturesAdapter);
        remainingGesturesList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
        Gesture g = gestures.get(position);
        g.setStatus(true);
        g.saveInBackground();
        mListener.onGestureClicked(meId, g.getObjectId());
    }

    public interface OnFragmentInteractionListener {
        public void onGestureClicked(String meId, String gestureId);
    }

}
