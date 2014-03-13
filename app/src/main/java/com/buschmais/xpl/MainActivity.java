package com.buschmais.xpl;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener, OnTaskCompleted {


    TextView textView;
    TextView searchField;
    ArrayList<Weather> list = new ArrayList<Weather>();
    WeatherListAdapter adapter;

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable("liste",list);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null && savedInstanceState.getSerializable("liste")!=null){
            list = (ArrayList<Weather>) savedInstanceState.getSerializable("liste");
        }

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        textView = (TextView)findViewById(R.id.textView2);

        searchField = (TextView)findViewById(R.id.textView);


        adapter = new WeatherListAdapter(this,R.layout.list_item,list);

                //(this,R.layout.list_item,R.id.list_item,list);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

        Log.d(MainActivity.class.getName(),"onClick: ");




        String search = searchField.getText().toString();
        if(search != null && !search.isEmpty()){


            AsyncTask task = new MyTask(this,search,this);
            task.execute();

            textView.setText(searchField.getText());
            //list.add(searchField.getText().toString());
            searchField.setText("");


        }
        else{
            Toast toast = Toast.makeText(this, getString(R.string.error_empty_city),Toast.LENGTH_LONG);
            toast.show();
        }

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

        list.add((Weather) object);
        adapter.notifyDataSetChanged();
    }
}