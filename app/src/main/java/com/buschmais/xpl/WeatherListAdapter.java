package com.buschmais.xpl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by training on 3/12/14.
 */
public class WeatherListAdapter extends ArrayAdapter {

    int layoutId;
    List<Weather> weatherList ;
    Context context;


    public WeatherListAdapter(Context aContext, int resource, List<Weather> objects) {
        super(aContext, resource, objects);
        layoutId = resource;
        weatherList = objects;
        context = aContext;
    }

    @Override
    public int getCount() {
        return weatherList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(layoutId,parent,false);

        TextView text = (TextView) view.findViewById(R.id.list_item_text);
        Weather weather = weatherList.get(position);
        text.setText(weather.toString());

        Integer code = Integer.valueOf(weather.getConditionCode());
        DrawableIds drawable = DrawableIds.values()[code-1];


        ImageView image = (ImageView) view.findViewById(R.id.list_item_image);
        image.setImageResource(drawable.getDrawableId());

        return view;
    }
}
