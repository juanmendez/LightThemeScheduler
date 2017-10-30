package info.juanmendez.daynightthemescheduler;

import info.juanmendez.daynightthemescheduler.services.LightThemePlanner;

/**
 * Created by Juan Mendez on 10/17/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

class LightThemeScheduler {

    LightThemeClient client;
    LightThemePlanner planner;

    public LightThemeScheduler(LightThemeClient lightThemeClient) {
        client = lightThemeClient;
        planner = new LightThemePlanner( client.getLightTimeModule(), client.getAppLightTime() );
    }
}
