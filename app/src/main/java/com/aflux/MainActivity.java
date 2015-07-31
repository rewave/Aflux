package com.aflux;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.aflux.core.Gesture;
import com.aflux.core.Person;
import com.aflux.core.SensorValue;
import com.aflux.ui.fragments.RecordData;
import com.aflux.ui.fragments.RemainingGestures;
import com.aflux.ui.fragments.Metadata;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;


public class MainActivity extends ActionBarActivity implements Metadata.OnFragmentInteractionListener, RemainingGestures.OnFragmentInteractionListener, RecordData.OnFragmentInteractionListener{

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseObject.registerSubclass(Person.class);
        ParseObject.registerSubclass(Gesture.class);
        ParseObject.registerSubclass(SensorValue.class);

        Parse.enableLocalDatastore(getApplicationContext());
        Parse.initialize(this, Config.parse_app_id, Config.parse_client_key);

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
        final Person me = new Person();
        me.setName(name);
        me.setAge(age);
        me.setSex(sex);
        me.pinInBackground();
        me.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    String meId = me.getObjectId();
                    setTitle((String) me.get("name"));

                    Person savedMe = Person.createWithoutData(Person.class, meId);
                    savedMe = setDeviceInfo(savedMe);
                    savedMe.saveInBackground();

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, RemainingGestures.newInstance(meId, "no"))
                            .addToBackStack(RemainingGestures.class.getName())
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
                                .replace(R.id.container, RemainingGestures.newInstance(meId, "yes")) // resume yes
                                .addToBackStack(RemainingGestures.class.getName())
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

    public Person setDeviceInfo(Person me) {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        int minDelay = accelerometer.getMinDelay();
        float maxRange = accelerometer.getMaximumRange();
        String name = accelerometer.getName();
        float power = accelerometer.getPower();
        float resolution = accelerometer.getResolution();
        String vendor = accelerometer.getVendor();

        me.setDeviceManufacturer(manufacturer);
        me.setDeviceModel(model);
        me.setAccMinDelay(minDelay);
        me.setAccMaxRange(maxRange);
        me.setAccName(name);
        me.setAccPower(power);
        me.setAccResolution(resolution);
        me.setAccVendor(vendor);

        return me;
    }

    @Override
    public void onGestureClicked(String meId, String gestureId) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, RecordData.newInstance(meId, gestureId))
                .addToBackStack(RecordData.class.getName())
                .commit();
    }
}
