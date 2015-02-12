package com.aflux.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aflux.R;

public class RemainingGestures extends Fragment {

    private static final String ARG_PARAM1 = "meId";

    private String meId;

    private OnFragmentInteractionListener mListener;

    public static RemainingGestures newInstance(String meId) {
        RemainingGestures fragment = new RemainingGestures();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, meId);
        fragment.setArguments(args);
        return fragment;
    }

    public RemainingGestures() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            meId = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gestures, container, false);
    }

//
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//        }
//    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
    }

}
