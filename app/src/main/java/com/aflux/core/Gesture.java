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
   private String status = "not done";

    public Gesture() {
        // Require Empty Constructor
    }

    public String getName() {
        return getString("name");
    }

    public String getDescription() {
        return getString("description");
    }

    public String getTag() {
        return getString("tag");
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(Boolean s) {
        if (s) {
            status = "done";
        } else {
            status = "not done";
        }
    }
}