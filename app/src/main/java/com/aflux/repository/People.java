package com.aflux.repository;

import android.support.v4.app.Fragment;

import com.aflux.core.Gesture;
import com.aflux.core.Person;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;


public class People implements Person.OnCoreInteractionListener{

    private OnRepositoryInteractionListener mListener;
    ParseQuery<Person> query = ParseQuery.getQuery(Person.class);

    public People(Fragment fragment) {
        this.mListener = (OnRepositoryInteractionListener) fragment;
    }

    public static People newInstance(Fragment fragment) {
        return new People(fragment);
    }

    public void get() {
        this.query.findInBackground(new FindCallback<Person>() {
            @Override
            public void done(List<Person> people, ParseException e) {
                if (e == null) {
                    mListener.onPeopleFound(people);
                } else {
                    mListener.onPersonFindException(e);
                }
            }
        });
    }

    public interface OnRepositoryInteractionListener {
        public void onPeopleFound(List<Person> people);
        public void onPersonFindException(ParseException e);
    }
}
