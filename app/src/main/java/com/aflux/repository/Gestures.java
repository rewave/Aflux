package com.aflux.repository;

import android.support.v4.app.Fragment;
import com.aflux.core.Gesture;
import com.aflux.core.SensorValue;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class Gestures extends Base{

    private OnRepositoryInteractionListener mListener;
    ParseQuery<Gesture> query = ParseQuery.getQuery(Gesture.class);

    public Gestures() {

    }

    public Gestures(Fragment fragment) {
        this.mListener = (OnRepositoryInteractionListener) fragment;
    }

    public static Gestures newInstance(Fragment fragment) {
        return new Gestures(fragment);
    }
    public static Gestures newInstance() {
        return new Gestures();
    }

    public ParseQuery<Gesture> getQuery() {
        return this.query;
    }

    public void get() {
        this.query.findInBackground(new FindCallback<Gesture>() {
            @Override
            public void done(List<Gesture> gestures, ParseException e) {
                if (e == null) {
                    mListener.onGesturesFound(gestures);
                } else {
                    mListener.onGestureFindException(e);
                }
            }
        });
    }

    /**
     * Relations
     */
    public void findSensorValues() {

        ParseQuery<SensorValue> query = ParseQuery.getQuery("GestureSensorValues");
        query.findInBackground(new FindCallback<SensorValue>() {
            @Override
            public void done(List<SensorValue> sensorValues, ParseException e) {
                if (e == null) {
                    mListener.onSensorValuesFound(sensorValues);
                } else {
                    mListener.onSensorValueFindException(e);
                }
            }
        });
    }

    public void setSensorValues(List<SensorValue> sensorValues) {
        // can be on a thread, but parse probably uses some kind of scheduling.
        for (SensorValue sensorValue : sensorValues) {
            final ParseObject gestureSensorValue = new ParseObject("GestureSensorValues");
            gestureSensorValue.put("gesture", this);
            gestureSensorValue.put("sensor_value", sensorValue);
            gestureSensorValue.saveInBackground();
        }
    }


    public interface OnRepositoryInteractionListener {
        public void onGesturesFound(List<Gesture> gestures);
        public void onGestureFindException(ParseException e);

        public void onSensorValuesFound(List<SensorValue> sensorValues);
        public void onSensorValueFindException(ParseException e);
    }
}
