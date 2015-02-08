package com.aflux;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.aflux.fragments.Metadata;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;


public class MainActivity extends ActionBarActivity implements Metadata.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "XyXr94ZIIctDpsiZxfZMyVOvks07Xw7evChcc6WO", "ObpL774pWlxmplT6Ke9oj5763KCwaMzQ3ALCONaF");

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, Metadata.newInstance())
                    .commit();
        }
    }

    // Fragment Interactions
    @Override
    public void onProceedButtonPressed(String name, String sex, String age) {
        // Save this user on parse, get id and pass it to collector fragment
        ParseObject me = new ParseObject("People");
        me.put("name", name);
        me.put("sex", sex);
        me.put("age", Integer.valueOf(age));
        me.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                try {
                    e.printStackTrace();
                }
                catch (NullPointerException e1) {
                    Log.i("Main", "saved");
                }
            }
        });
    }
}
