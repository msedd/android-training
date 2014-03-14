package com.buschmais.xpl;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Instrumentation;
import android.content.pm.ActivityInfo;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by training on 3/14/14.
 */
public class MyTestcase extends ActivityInstrumentationTestCase2 <MainActivity> {

    public MyTestcase() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        //activity = getActivity();
    }

    public void testSearchWetherSuccess(){

        final MainActivity activity = getActivity();
        final TextView searchField = (TextView) activity.findViewById(R.id.textView);

        final Button button = (Button) activity.findViewById(R.id.button);

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                searchField.setText("Dresden");
                button.performClick();
                activity.onClick(null,0);

            }
        });


        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(MainActivity.class.getName(),null,false);
        activityMonitor.waitForActivityWithTimeout(10000);

        ListView listView = (ListView)activity.findViewById(R.id.list_view);
        assertEquals("Anzahl der Einträge in der Liste",1,listView.getCount());


        activityMonitor.waitForActivityWithTimeout(10000);


    }

    public void testSearchWetherFailure(){

        final MainActivity activity = getActivity();
        final TextView searchField = (TextView) activity.findViewById(R.id.textView);

        final Button button = (Button) activity.findViewById(R.id.button);

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                searchField.setText("");
                button.performClick();
                activity.onClick(null,0);

            }
        });


        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(MainActivity.class.getName(),null,false);
        activityMonitor.waitForActivityWithTimeout(10000);


        ListView listView = (ListView)activity.findViewById(R.id.list_view);
        assertEquals("Anzahl der Einträge in der Liste",0,listView.getCount());


        activityMonitor.waitForActivityWithTimeout(10000);


    }
    public void testOrientation(){

        final MainActivity activity = getActivity();
        final TextView searchField = (TextView) activity.findViewById(R.id.textView);

        final Button button = (Button) activity.findViewById(R.id.button);

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                searchField.setText("Dresden");
                button.performClick();
                activity.onClick(null,0);

            }
        });


        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(MainActivity.class.getName(),null,false);
        activityMonitor.waitForActivityWithTimeout(10000);

        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setActivity(null);
        final MainActivity activity2 = getActivity();
        ListView listView = (ListView)activity2.findViewById(R.id.list_view);
        assertEquals("Anzahl der Einträge in der Liste",1,listView.getCount());


        activityMonitor.waitForActivityWithTimeout(10000);


    }
}
