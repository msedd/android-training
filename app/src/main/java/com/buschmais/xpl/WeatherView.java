package com.buschmais.xpl;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by training on 3/14/14.
 */
public class WeatherView extends FrameLayout{

    private Weather weather;

    public WeatherView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray  typedArray = context.obtainStyledAttributes(attrs, R.styleable.WeatherView,0,0);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.weather_layout,this,true);

    }

    public void setWeather(Weather aWeather) {
        weather = aWeather;

        TextView text = (TextView) findViewById(R.id.list_item_text);
        text.setText(weather.toString());

        Integer code = Integer.valueOf(weather.getConditionCode());
        DrawableIds drawable = DrawableIds.values()[code-1];


        ImageView image = (ImageView) findViewById(R.id.list_item_image);
        image.setImageResource(drawable.getDrawableId());


    }
}
