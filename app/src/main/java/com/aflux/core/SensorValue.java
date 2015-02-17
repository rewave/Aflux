package com.aflux.core;

import android.hardware.SensorEvent;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseRelation;

@ParseClassName("SensorValues")
public class SensorValue extends ParseObject {

    private SensorEvent sensorEvent;

    public SensorValue() {

    }

    public SensorValue(String timestamp, SensorEvent sensorEvent) {
        this.sensorEvent = sensorEvent;
        setAx(sensorEvent.values[0]);
        setAy(sensorEvent.values[1]);
        setAz(sensorEvent.values[2]);
        setTime(timestamp);
    }

    public void setPersonGesture(ParseObject personGesture) {
        ParseRelation relation = this.getRelation("person_gesture");
        relation.add(personGesture);
    }

    public void setTime(String timestamp) {
        put("time", timestamp);
    }

    public String getTime() {
        return getString("time");
    }

    public void setAx(float ax) {
        put("ax", ax);
    }

    public double getAx() {
        return getDouble("ax");
    }

    public void setAy(float ay) {
        put("ay", ay);
    }

    public double getAy() {
        return getDouble("ay");
    }

    public void setAz(float az) {
        put("az", az);
    }

    public double getAz() {
        return getDouble("az");
    }

    public void setWx(float wx) {
        put("wx", wx);
    }

    public double getWx() {
        return getDouble("wx");
    }

    public void setWy(float wy) {
        put("wy", wy);
    }

    public double getWy() {
        return getDouble("wy");
    }

    public void setWz(float wz) {
        put("wz", wz);
    }

    public double getWz() {
        return getDouble("wz");
    }

}
