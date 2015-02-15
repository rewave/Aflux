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
    private final Gestures gestureRepo = Gestures.newInstance(this);

    private String meId;
    private Person me;
    private List<Gesture> gestures;
    private final static String TAG = "RGFragment";

    private OnFragmentInteractionListener mListener;

    public static RemainingGestures newInstance(String meId) {
        RemainingGestures fragment = new RemainingGestures();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, meId);
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
            try {
                me = ParseQuery.getQuery(Person.class).get(meId);
            } catch (ParseException e) {
                e.printStackTrace();
            }
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
        mListener = null;
    }

    /**
     * Repository Interactions
     */

    @Override
    public void onGesturesFound(List<Gesture> gestures) {
        People peopleRepository = People.newInstance(this);
        this.gestures = gestures;
        peopleRepository.findGesturesStatus(me, gestures); // calls onGesturesStatusFound() on completion
    }

    @Override
    public void onGestureFindException(ParseException e) {
        e.printStackTrace();
    }

    @Override
    public void onSensorValuesFound(List<SensorValue> sensorValues) {}

    @Override
    public void onSensorValueFindException(ParseException e) {
        e.printStackTrace();
    }

    @Override
    public void onPeopleFound(List<Person> people){}

    @Override
    public void onPersonFindException(ParseException e) {
        e.printStackTrace();
    }

    @Override
    public void onGesturesStatusFound(List<Gesture> gestures) {
        ListView remainingGesturesList = (ListView) getActivity().findViewById(R.id.gestures_list);
        RemainingGesturesAdapter remainingGesturesAdapter = new RemainingGesturesAdapter(getActivity(), gestures);
        remainingGesturesList.setAdapter(remainingGesturesAdapter);
        remainingGesturesList.setOnItemClickListener(this);
    }

    @Override
    public void onGestureStatusFindException(ParseException e) {
        e.printStackTrace();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
        mListener.onGestureClicked(meId, gestures.get(position).getObjectId());
    }

    public interface OnFragmentInteractionListener {
        public void onGestureClicked(String meId, String gestureId);
    }

}
