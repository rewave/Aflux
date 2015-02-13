package com.aflux.core;


import android.support.v4.app.Fragment;

import com.aflux.repository.Gestures;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


@ParseClassName("Gestures")
public class Gesture extends ParseObject {

//    private networkInteractionListener mListener;

    public Gesture() {
        // Require Empty Constructor
    }

    public String getName() {
        return getString("name");
    }
    // Don't need a setter because sensor names are fixed.

    public String getDescription() {
        return getString("description");
    }


//    /*
//        Relations
//     */
//    public void findSensorValues() {
//        ParseQuery<SensorValue> query = ParseQuery.getQuery("GestureSensorValues");
//        query.findInBackground(new FindCallback<SensorValue>() {
//            @Override
//            public void done(List<SensorValue> sensorValues, ParseException e) {
//                mListener.onSensorValuesFound(sensorValues);
//            }
//        });
//    }
//
//    public void setSensorValues(List<SensorValue> sensorValues) {
//        // can be on a thread, but parse probably uses some kind of scheduling.
//        for (SensorValue sensorValue : sensorValues) {
//            final ParseObject gestureSensorValue = new ParseObject("GestureSensorValues");
//            gestureSensorValue.put("gesture", this);
//            gestureSensorValue.put("sensor_value", sensorValue);
//            gestureSensorValue.saveInBackground();
//        }
//    }
//
//    public interface networkInteractionListener {
//        public void onSensorValuesFound(List<SensorValue> sensorValues);
//    }
}