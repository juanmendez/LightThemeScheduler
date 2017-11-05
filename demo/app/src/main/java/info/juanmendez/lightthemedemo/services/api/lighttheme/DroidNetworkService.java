package info.juanmendez.lightthemedemo.services.api.lighttheme;

import android.net.ConnectivityManager;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.SystemService;

import info.juanmendez.lighttheme.services.LightNetworkService;


/**
 * Created by Juan Mendez on 10/28/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
@EBean
public class DroidNetworkService implements LightNetworkService {

    @SystemService
    ConnectivityManager mConnectivityManager;

    @Override
    public boolean isOnline() {
        if (mConnectivityManager.getActiveNetworkInfo() != null && mConnectivityManager.getActiveNetworkInfo().isAvailable()
                && mConnectivityManager.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
