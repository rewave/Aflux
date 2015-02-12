package com.aflux.repository;

import com.aflux.core.Gesture;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class Gestures {

    private networkInteractionListener mListner;

    public Gestures() {

    }

    public static Gestures newInstance() {
        return new Gestures();
    }

    public void all() {
        ParseQuery<Gesture> query = ParseQuery.getQuery(Gesture.class);
        query.findInBackground(new FindCallback<Gesture>() {
            @Override
            public void done(List<Gesture> gestures, ParseException e) {
                mListner.onAllGesturesFound(gestures);
            }
        });
    }

    public interface networkInteractionListener {
        public void onAllGesturesFound(List<Gesture> gestures);
    }
}
