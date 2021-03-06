package info.juanmendez.lightthemedemo;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EReceiver;
import org.androidannotations.annotations.sharedpreferences.Pref;

import info.juanmendez.lightthemedemo.services.lighttheme.LightManagerFactory;
import info.juanmendez.lightthemedemo.services.preferences.WidgetPrefs_;
import info.juanmendez.lightthemescheduler.models.WidgetScreenStatus;


/**
 * Created by Juan Mendez on 9/4/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
@EReceiver
public class WidgetProvider extends AppWidgetProvider {

    @App
    MyApp myApp;

    @Bean
    LightManagerFactory clientBuilder;

    @Pref
    WidgetPrefs_ widgetPrefs;

    @Override
    public void onReceive(Context context, Intent intent) {
        if ( intent.getAction() == null ) {

            int[] widget_ids = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
            for( int widget_id: widget_ids){
                updateWidget( context, AppWidgetManager.getInstance(context), widget_id );
            }
        } else {
            super.onReceive(context, intent);
        }

        clientBuilder.getManager().onAppEvent( intent.getAction() );
    }

    @Override
    public void onUpdate(Context ctxt, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        int len = appWidgetIds.length;

        for( int i = 0; i < len; i++ ) {
            updateWidget( ctxt, appWidgetManager, appWidgetIds[i]);
        }

        super.onUpdate(ctxt, appWidgetManager, appWidgetIds);
    }

    private void updateWidget( Context context, AppWidgetManager manager, int widgetId) {
        if( widgetId > 0 ){

            /**
             * The only way to update a widget day-night theme is done by choosing the layout
             */
            RemoteViews widget = new RemoteViews(context.getPackageName(),
                            isNightMode()?R.layout.widet_layout_night:R.layout.widget_layout);

            manager.updateAppWidget(widgetId, widget);
        }
    }

    /**
     * if user clears the data, provide day mode.
     * @return
     */
    private boolean isNightMode(){
        return widgetPrefs.screenMode().get()== WidgetScreenStatus.WIDGET_NIGHT_SCREEN;
    }
}