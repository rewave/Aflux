package com.aflux.core;

import android.hardware.SensorEvent;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("SensorValue")
public class SensorValue extends ParseObject {

    private SensorEvent sensorEvent;

    SensorValue() {

    }

    SensorValue(SensorEvent sensorEvent) {
        this.sensorEvent = sensorEvent;
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
