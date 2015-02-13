package com.aflux.repository;

import android.support.v4.app.Fragment;
import com.aflux.core.Gesture;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class Gestures extends Base{

    private OnRepositoryInteractionListener mListener;
    ParseQuery<Gesture> query = ParseQuery.getQuery(Gesture.class);

    public Gestures(Fragment fragment) {
        this.mListener = (OnRepositoryInteractionListener) fragment;
    }

    public static Gestures newInstance(Fragment fragment) {
        return new Gestures(fragment);
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

    public interface OnRepositoryInteractionListener {
        public void onGesturesFound(List<Gesture> gestures);
        public void onGestureFindException(ParseException e);
    }
}
