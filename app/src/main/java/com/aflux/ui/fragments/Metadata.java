package com.aflux.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.aflux.R;
import com.andreabaccega.formedittextvalidator.Validator;
import com.andreabaccega.widget.FormEditText;
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

        ButtonRectangle resume= (ButtonRectangle) getActivity().findViewById(R.id.resume);
        resume.setOnClickListener(resumeListener);

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
            FormEditText nameField = (FormEditText) getActivity().findViewById(R.id.name);
            FormEditText ageField = (FormEditText) getActivity().findViewById(R.id.age);

            if (nameField.testValidity() && ageField.testValidity())
            {
                ((ButtonRectangle) v).setText("•••");
                String name = nameField.getText().toString();
                int age = Integer.valueOf(ageField.getText().toString());
                String sex = ((RadioButton) getActivity().findViewById(((RadioGroup) getActivity().findViewById(R.id.sexes)).getCheckedRadioButtonId())).getText().toString();
                if (mListener != null) {
                    mListener.onProceedButtonPressed(name, sex, age);
                }
            }
        }
    };

    View.OnClickListener resumeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FormEditText nameField = (FormEditText) getActivity().findViewById(R.id.name);

            if (nameField.testValidity()) {
                if (mListener != null) {
                    ((ButtonRectangle) v).setText("•••");
                    mListener.onResumeButtonPressed(nameField.getText().toString());
                }
            }
        }
    };

    public interface OnFragmentInteractionListener {
        public void onProceedButtonPressed(String name, String sex, int age);
        public void onResumeButtonPressed(String name);
    }
}
