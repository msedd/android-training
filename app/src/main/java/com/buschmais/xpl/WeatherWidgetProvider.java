package com.buschmais.xpl;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.RemoteViews;

/**
 * Created by training on 3/14/14.
 */
public class WeatherWidgetProvider extends AppWidgetProvider implements OnTaskCompleted{

    private int[] appWidgetIds;
    private Context context;
    private AppWidgetManager appWidgetManager;

    @Override
    public void onUpdate(Context aContext, AppWidgetManager widgetManager, int[] widgetIds) {

        appWidgetManager = widgetManager;
        appWidgetIds = widgetIds;
        context = aContext;

        AsyncTask task = new MyTask(context,"Dresden",this);
        task.execute();

    }

    @Override
    public void onTaskCompleted(Object object) {
        final int N = appWidgetIds.length;
            RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.weather_widget);

            Weather weather = (Weather) object;

            Integer code = Integer.valueOf(weather.getConditionCode());
            DrawableIds drawable = DrawableIds.values()[code-1];


            views.setImageViewResource(R.id.widget_image,drawable.getDrawableId());
            views.setTextViewText(R.id.widget_text, weather.toString());

        for (int i=0; i<N; i++){
            int appWidgetId = appWidgetIds[i];
            appWidgetManager.updateAppWidget(appWidgetId,views);
        }

    }
}
