package com.aflux.repository;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ListView;

import com.aflux.core.Gesture;
import com.aflux.core.Person;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class People implements Person.OnCoreInteractionListener{

    private OnRepositoryInteractionListener mListener;
    private static final String TAG = "People Repo";

    ParseQuery<Person> query = ParseQuery.getQuery(Person.class);

    public People() {

    }

    public People(Fragment fragment) {
        this.mListener = (OnRepositoryInteractionListener) fragment;
    }

    public static People newInstance(Fragment fragment) {
        return new People(fragment);
    }

    public static People newInstance() {
        return new People();
    }

    public People personById(String personId) {
        this.query.whereEqualTo("objectId", personId);
        return this;
    }

    public ParseQuery<Person> getQuery() {
        return this.query;
    }

    public void get() {
        this.query.findInBackground(new FindCallback<Person>() {
            @Override
            public void done(List<Person> people, ParseException e) {
                if (e == null) {
                    mListener.onPeopleFound(people);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Relations
     */
    public void findGesturesStatus(Person me, final List<Gesture> gestures) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("PersonGestures");
        query.whereEqualTo("person", me);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    List<Gesture> doneGestures = new ArrayList<>();
                    for (ParseObject p : parseObjects) {
                        Gesture g = null;
                        try {
                            g = (Gesture) p.getRelation("gesture").getQuery().find().get(0);
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                        doneGestures.add(g);
                    }

                    for (Gesture gesture : gestures) {
                        if (doneGestures.contains(gesture)) {
                            gesture.setStatus(true);
                        }
                    }

                    Log.d(TAG, "Gesture status found, calling listerns");
                    mListener.onGesturesStatusFound(gestures);
                    /*
                     * Functional way:
                     * gesture.setStatus(false) for gesture in gestures if gesture not in (map(parseObjects fn{p} return p.get("gesture")))
                    */

                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public void findGesturesStatusNew(Person me, final List<Gesture> gestures) {
//        Log.i(TAG, "Find gesture status called");
//        ParseQuery<ParseObject> query = ParseQuery.getQuery("PersonGestures");
//        query.whereEqualTo("person", me);
//        query.whereContainedIn("gesture", gestures);
//        query.fromLocalDatastore().findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> parseObjects, ParseException e) {
//                ParseObject.pinAllInBackground(parseObjects);
//                if (e != null) {
//                    e.printStackTrace();
//                    return;
//                }
//                for (ParseObject p : parseObjects) {
//                    try {
//                        gestures.get(gestures.indexOf(p.getRelation("gesture").getQuery().getFirst())).setStatus(true);
//                    } catch (ParseException e1) {
//                        e1.printStackTrace();
//                    }
//                }
//                mListener.onGesturesStatusFound(gestures);
//            }
//        });
        mListener.onGesturesStatusFound(gestures);

    }

    public void setPersonGesture(Person p, Gesture g, int sample_number) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("PersonGestures");
        final ParseObject personGesture = new ParseObject("PersonGestures");

        ParseRelation relation = personGesture.getRelation("gesture");
        relation.add(g);
        relation = personGesture.getRelation("person");
        relation.add(p);
        personGesture.put("sample_number", sample_number);
        personGesture.saveInBackground();
    }

    public interface OnRepositoryInteractionListener {
        public void onPeopleFound(List<Person> people);
        public void onGesturesStatusFound(List<Gesture> gestures);;
    }
}
