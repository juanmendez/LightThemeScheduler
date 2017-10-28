package info.juanmendez.daynightthemescheduler.services;

import org.joda.time.LocalTime;

import info.juanmendez.daynightthemescheduler.models.LightTime;
import info.juanmendez.daynightthemescheduler.models.Response;
import info.juanmendez.daynightthemescheduler.utils.LightTimeUtils;
import info.juanmendez.daynightthemescheduler.utils.LocalTimeUtils;

/**
 * Created by Juan Mendez on 10/27/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 * This class takes care of figuring out the light times needed for either today, tomorrow or both
 */
public class LightTimePlanner {

    public static final int NO_SCHEDULE = 0;
    public static final int SUNRISE_SCHEDULE = 1;
    public static final int SUNSET_SCHEDULE = 2;
    public static final int TOMORROW_SCHEDULE = 3;

    private ApiProxy apiProxy;
    private LocalTime now  = LocalTime.now();

    /**
     * @param ApiRetro makes calls to get
     * @param lightTime
     */
    public LightTimePlanner(ApiRetro apiRetro, NetworkService networkService, LightTime lightTime ) {
        this.apiProxy = new ApiProxy( networkService, apiRetro, lightTime );
    }

    public void provideNextTimeLight(Response<LightTime> response){
        provideTodayLightTime(  response );
    }

    private void provideTodayLightTime(Response<LightTime> response ){
        apiProxy.provideTodaysSchedule(lightTimeResult -> {

            if(LightTimeUtils.isValid( lightTimeResult )){
                LocalTime sunrise = LocalTimeUtils.getLocalTime( lightTimeResult.getSunRise() );
                LocalTime sunset = LocalTimeUtils.getLocalTime( lightTimeResult.getSunSet() );

                int when = whatSchedule( now, sunrise, sunset );

                if( when == SUNRISE_SCHEDULE ){
                    lightTimeResult.setNextSchedule( lightTimeResult.getSunRise() );
                    response.onResult( lightTimeResult );
                }else if(  when == SUNSET_SCHEDULE ){
                    lightTimeResult.setNextSchedule( lightTimeResult.getSunSet() );
                    response.onResult( lightTimeResult );
                }else if( when == TOMORROW_SCHEDULE ){
                    //ok, we need to call and get tomorrows..
                    provideTomorrowLightTime( response );
                }
            }else{
                response.onResult( lightTimeResult );
            }
        });
    }

    private void provideTomorrowLightTime(Response<LightTime> response ){
        apiProxy.provideTomorrowSchedule(lightTimeResult -> {
            lightTimeResult.setNextSchedule( lightTimeResult.getSunRise() );
            response.onResult( lightTimeResult );
        });
    }

    public static int whatSchedule( LocalTime now, LocalTime sunrise, LocalTime sunset ){
        if( now.isBefore( sunrise )){
            return SUNRISE_SCHEDULE;
        }else if( now.isBefore( sunset) ){
            return SUNSET_SCHEDULE;
        }else{
            return TOMORROW_SCHEDULE;
        }
    }
}