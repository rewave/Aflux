package com.aflux.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.aflux.R;
import com.gc.materialdesign.views.ButtonRectangle;

public class Metadata extends Fragment {
    private OnFragmentInteractionListener mListener;

    public static Metadata newInstance() {
        return new Metadata();
    }

    public Metadata() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_metadata, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ButtonRectangle proceed = (ButtonRectangle) getActivity().findViewById(R.id.proceed);
        proceed.setOnClickListener(proceedListener);
    }

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

    View.OnClickListener proceedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String name = ((EditText) getActivity().findViewById(R.id.name)).getText().toString();
            String age = ((EditText) getActivity().findViewById(R.id.age)).getText().toString();
            String sex = ((RadioButton) getActivity().findViewById(((RadioGroup) getActivity().findViewById(R.id.sexes)).getCheckedRadioButtonId())).getText().toString();
            if (mListener != null) {
                mListener.onProceedButtonPressed(name, sex, age);
            }
        }
    };

    public interface OnFragmentInteractionListener {
        public void onProceedButtonPressed(String name, String sex, String age);
    }

}
