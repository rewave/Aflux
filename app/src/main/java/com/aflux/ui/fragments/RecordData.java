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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.aflux.Config;
import com.aflux.R;
import com.aflux.core.Gesture;
import com.aflux.core.Person;
import com.aflux.core.SensorValue;
import com.aflux.repository.Gestures;
import com.aflux.repository.People;
import com.gc.materialdesign.views.ButtonRectangle;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class RecordData extends Fragment implements SensorEventListener, View.OnTouchListener{
    private static final String ARG_PARAM1 = "meId";
    private static final String ARG_PARAM2 = "gestureId";

    private static final String TAG = "RecordData";

    private String meId;
    private Person me;
    private String gestureId;
    private Gesture gesture;
    private int numSamples = 1;
    private List<SensorValue> sensorValues = new ArrayList<>();
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private boolean recordData = false;

    private ButtonRectangle hold_to_send;
    private ProgressBar progress;


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

        People peopleRepo = People.newInstance();
        Gestures gestureRepo = Gestures.newInstance();
        try {
            me = peopleRepo.getQuery().get(meId);
            gesture = gestureRepo.getQuery().get(gestureId);
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
        sensorValues = null;
        accelerometer = null;
        gesture = null;
        sensorManager.unregisterListener(this);
        mListener = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((TextView) getActivity().findViewById(R.id.gesture_name)).setText(gesture.getName());

        progress = (ProgressBar) getActivity().findViewById(R.id.progress);

        hold_to_send = (ButtonRectangle) getActivity().findViewById(R.id.hold_to_record);
        hold_to_send.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                v.setPressed(true);
                recordData = true;
                break;
            case MotionEvent.ACTION_UP:
                v.setPressed(false);
                recordData = false;
                saveData(me, gesture, numSamples, sensorValues);
                sensorValues = new ArrayList<>();
                if (numSamples == Config.numSamples) {
                    hold_to_send.setOnTouchListener(null);
                }
                numSamples += 1;
                break;
            default:
                break;
        }
        return true;
    }

    ///////////////////////////
    @Override
    public void onAccuracyChanged(Sensor sensor, int change) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (recordData) {
            sensorValues.add(new SensorValue(String.valueOf(event.timestamp), event));
        }
    }
    ///////////////////////////

    public double mod(SensorEvent event) {
        double mod = Math.pow(event.values[0], 2) + Math.pow(event.values[1], 2) + Math.pow(event.values[2], 2);
        mod = Math.pow(mod, 0.5);
        return mod;
    }

    public void saveData(Person me, Gesture g, final int sample_number, final List<SensorValue> sensorValues) {
        ((TextView) getActivity().findViewById(R.id.status)).setText("Saving Data " + String.valueOf(sample_number));
        final ParseObject personGesture = new ParseObject("PersonGestures");

        ParseRelation<ParseObject> relation;
        relation = personGesture.getRelation("person");
        relation.add(me);
        relation = personGesture.getRelation("gesture");
        relation.add(g);

        personGesture.put("sample_number", sample_number);
        personGesture.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    ((TextView) getActivity().findViewById(R.id.status)).setText("Person Gesture " + String.valueOf(sample_number) + " Saved");
                    for (SensorValue sensorValue : sensorValues) {
                        sensorValue.setPersonGesture(personGesture);
                    }


                    SensorValue.saveAllInBackground(sensorValues, new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                ((TextView) getActivity().findViewById(R.id.status)).setText("Sensor Values " + String.valueOf(sample_number) + " saved");
                                progress.setProgress(progress.getProgress() + 100 / Config.numSamples);
                                if (sample_number == Config.numSamples) {
                                    // all data saved, go back
                                    getActivity().onBackPressed();
                                }
                            } else {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public interface OnFragmentInteractionListener {

    }

}