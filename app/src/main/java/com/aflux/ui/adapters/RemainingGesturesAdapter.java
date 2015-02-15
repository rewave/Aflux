package com.aflux.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aflux.core.Gesture;

import java.util.List;


public class RemainingGesturesAdapter extends BaseAdapter {

    private List<Gesture> gestures;
    private LayoutInflater inflater;

    public RemainingGesturesAdapter(Context context, List<Gesture> gestures) {
        this.gestures = gestures;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return gestures.size();
    }

    @Override
    public Gesture getItem(int position) {
        return gestures.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.getItem(position).getLong("objectId");
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if (view == null) {
            view = inflater.inflate(android.R.layout.simple_list_item_2, null);
        }

        TextView gestureName = (TextView) view.findViewById(android.R.id.text1);
        TextView status = (TextView) view.findViewById(android.R.id.text2); // done or not
        Gesture gesture = this.getItem(position);
        gestureName.setText(gesture.getName());
        status.setText(gesture.getStatus());

        return view;
    }
}
