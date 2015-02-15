package com.aflux.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aflux.Config;
import com.aflux.R;
import com.aflux.core.Gesture;
import com.aflux.core.Person;
import com.aflux.core.SensorValue;
import com.aflux.repository.Gestures;
import com.aflux.repository.People;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class RecordData extends Fragment implements SensorEventListener{
    private static final String ARG_PARAM1 = "meId";
    private static final String ARG_PARAM2 = "gestureId";

    private static final String TAG = "RecordData";

    private String meId;
    private Person me;
    private String gestureId;
    private Gesture gesture;
    private int numSamples = 0;
    private final ParseObject personGesture = new ParseObject("PersonGestures");
    private List<SensorValue> sensorValues = new ArrayList<>();
    private SensorManager sensorManager;
    private Sensor accelerometer;

    private OnFragmentInteractionListener mListener;

    public static RecordData newInstance(String meId, String gestureId) {
        RecordData fragment = new RecordData();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, meId);
        args.putString(ARG_PARAM2, gestureId);
        fragment.setArguments(args);
        return fragment;
    }

    public RecordData() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            meId = getArguments().getString(ARG_PARAM1);
            gestureId = getArguments().getString(ARG_PARAM2);
        }

        // TODO : Remove this relation and all relavent sensor data if exists to avoid multiple readings

        People peopleRepo = People.newInstance();
        Gestures gestureRepo = Gestures.newInstance();
        try {
            me = peopleRepo.getQuery().get(meId);
            gesture = gestureRepo.getQuery().get(gestureId);

            ParseRelation relation = personGesture.getRelation("gesture");
            relation.add(gesture);
            relation = personGesture.getRelation("person");
            relation.add(me);


        } catch (ParseException e) {
            e.printStackTrace();
        }

        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_record_data, container, false);
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
        sensorManager.unregisterListener(this);
        mListener = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((TextView) getActivity().findViewById(R.id.gesture_name)).setText(gesture.getName());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int change) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.e("ACCELEROMETER", "ACC: x=" + event.values[0] + " y=" + event.values[1] + " z=" + event.values[2]);
        // Do something here to threshold values and increment counter so as to read 5 samples and save in bg
        if (passesThreshold(event)) {
            numSamples += 1;
            sensorValues.add(new SensorValue(event));

            if (numSamples == Config.numSamples) {
                sensorManager.unregisterListener(this);
                saveData();
            }
        }
    }

    public boolean passesThreshold(SensorEvent event) {
        // TODO
        return true;
    }

    public void saveData() {
        ((TextView) getActivity().findViewById(R.id.gesture_name)).setText("Saving Data");
        personGesture.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    ((TextView) getActivity().findViewById(R.id.gesture_name)).setText("Person Gesture Saved");
                } else {
                    e.printStackTrace();
                }

                for (SensorValue sensorValue : sensorValues) {
                    sensorValue.setPersonGesture(personGesture);
                }

                ((TextView) getActivity().findViewById(R.id.gesture_name)).setText("Saving Sensor Values");

                SensorValue.saveAllInBackground(sensorValues, new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            // go back
                            getActivity().onBackPressed();
                        } else {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    /**
     * Repository Interactions
     */

    public void onGesturesStatusFound(List<Gesture> gestures) {

    }

    public void onGestureStatusFindException(ParseException e) {
        e.printStackTrace();
    }


    public void onSensorValuesFound(List<SensorValue> sensorValues) {

    }

    public void onSensorValueFindException(ParseException e) {
        e.printStackTrace();
    }

    public interface OnFragmentInteractionListener {

    }

}
