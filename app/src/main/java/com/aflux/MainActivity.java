package com.aflux;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.aflux.Fragments.Gestures;
import com.aflux.Fragments.Metadata;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;


public class MainActivity extends ActionBarActivity implements Metadata.OnFragmentInteractionListener, Gestures.OnFragmentInteractionListener{

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
    public void onProceedButtonPressed(String name, String sex, int age) {
        // Save this user on parse, get id and pass it to collector fragment
        final ParseObject me = new ParseObject("People");
        me.put("name", name);
        me.put("sex", sex);
        me.put("age", age);
        me.pinInBackground();
        me.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    String meId = me.getObjectId();
                    setTitle((String) me.get("name"));
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, Gestures.newInstance(meId))
                            .commit();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onResumeButtonPressed(String name) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("People");

        query.whereEqualTo("name", name);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e==null) {
                    if (parseObjects.size() == 1) {
                        final ParseObject me = parseObjects.get(0);
                        me.pinInBackground();
                        String meId = me.getObjectId();
                        setTitle((String) me.get("name"));
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, Gestures.newInstance(meId))
                                .commit();
                    } else {
                        Log.d("Main activity", "No match found, register first");
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

}
