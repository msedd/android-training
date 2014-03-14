package com.buschmais.xpl;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

/**
 * Created by training on 3/13/14.
 */
public class MyTask extends AsyncTask{

    ProgressDialog progressDialog;
    String searchString;
    WeatherClient weatherClient;

    OnTaskCompleted onTaskCompletedListener;

    public MyTask(Context context, String search, OnTaskCompleted listener){
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(4);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        searchString = search;

        weatherClient = new WeatherClient();

        onTaskCompletedListener = listener;

    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //progressDialog.show();
    }

    @Override
    protected void onPostExecute(Object o) {

        if(o != null && o instanceof Weather){
           onTaskCompletedListener.onTaskCompleted(o);

        }

        super.onPostExecute(o);
        progressDialog.dismiss();

    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        Integer progressValue = (Integer) values[0];
        progressDialog.setProgress(progressValue);
        super.onProgressUpdate(values);

    }

    @Override
    protected Object doInBackground(Object[] objects) {

        /*

        for(int i = 0; i<4; i++){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            publishProgress(i+1);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }*/
        publishProgress(1);
        Weather weather = weatherClient.getWeather(searchString);
        publishProgress(4);

        return weather;
    }
}
