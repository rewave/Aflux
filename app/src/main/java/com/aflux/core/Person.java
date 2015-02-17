package com.aflux.core;

import android.os.Build;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("People")
public class Person extends ParseObject {

    private OnCoreInteractionListener mListner;

    public Person() {

    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name", name);
    }

    public int getAge() {
        return getInt("age");
    }

    public void setAge(int age) {
        put("age", age);
    }

    public String getSex() {
        return getString("sex");
    }

    public void setSex(String sex) {
        put("sex", sex);
    }

    public String getDeviceManufacturer() {
        return getString("device_manufacturer");
    }

    public void setDeviceManufacturer(String manufacturer) {
        put("device_manufacturer", manufacturer);
    }

    public String getDeviceModel() {
        return getString("device_model");
    }

    public void setDeviceModel(String model) {
        put("device_model", model);
    }

    public int getAccMinDelay() {
        return getInt("acc_min_delay");
    }

    public void setAccMinDelay(int minDelay) {
        put("acc_min_delay", minDelay);
    }

    public double getAccMaxRange() {
        return getDouble("acc_max_range");
    }

    public void setAccMaxRange(double maxRange) {
        put("acc_max_range", maxRange);
    }

    public String getAccName() {
        return getString("acc_name");
    }

    public void setAccName(String name) {
        put("acc_name", name);
    }

    public float getAccPower() {
        return (float) getDouble("acc_power");
    }

    public void setAccPower(float power) {
        put("acc_power", power);
    }

    public float getAccResolution() {
        return (float) getDouble("acc_resolution");
    }

    public void setAccResolution(float resolution) {
        put("acc_resolution", resolution);
    }

    public String getAccVendor() {
        return getString("acc_vendor");
    }

    public void setAccVendor(String vendor) {
        put("acc_vendor", vendor);
    }

    public interface OnCoreInteractionListener {

    }
}

