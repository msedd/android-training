package com.buschmais.xpl;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.UiModeManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener, DialogInterface.OnClickListener, OnTaskCompleted {


    TextView textView;
    TextView searchField;


    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //
        // CAR + Nachtmodus einschalten
        //
        Button button_night = (Button) findViewById(R.id.button_night);
        button_night.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UiModeManager  mgr = (UiModeManager) getSystemService(UI_MODE_SERVICE);
                mgr.enableCarMode(0);
                mgr.setNightMode(UiModeManager.MODE_NIGHT_YES);
            }
        });

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        textView = (TextView)findViewById(R.id.textView2);

        searchField = (TextView)findViewById(R.id.textView);

        SharedPreferences preferences = getSharedPreferences("MyPrefs",0);
        searchField.setText(preferences.getString("searchstring", null));

    }

    @Override
    public void onClick(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

        Log.d(MainActivity.class.getName(),"onClick: ");

        MyDialog d = new MyDialog();
        d.show(getFragmentManager(),"Result");


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.y
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }


    @Override
    public void onTaskCompleted(Object object) {

        Intent intent = new Intent(this,WeatherActivity.class);

        intent.putExtra("weather",(Weather) object);

        startActivity(intent);

        //
        // wird jetzt von der neuen Activity erledigt
        //
        //list.add((Weather) object);
        //adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

        String search = searchField.getText().toString();
        if(search != null && !search.isEmpty()){


            AsyncTask task = new MyTask(this,search,this);
            task.execute();

            textView.setText(searchField.getText());
            SharedPreferences preferences = getSharedPreferences("MyPrefs",0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("searchstring",searchField.getText().toString());
            editor.commit();
            //list.add(searchField.getText().toString());
            searchField.setText("");


        }
        else{
            Toast toast = Toast.makeText(this, getString(R.string.error_empty_city),Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
