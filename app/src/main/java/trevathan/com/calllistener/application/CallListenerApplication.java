package trevathan.com.calllistener.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by dnorvell on 1/5/16.
 */
public class CallListenerApplication extends Application {

    /**
     * A reference to the application context. Can be used as a normal context for most cases.
     */
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();

        sContext = getApplicationContext();

    }

    /**
     * Singleton method.
     *
     * @return A reference to this application context.
     */
    public static CallListenerApplication getAppContext() {
        return (CallListenerApplication) CallListenerApplication.sContext;
    }

}
