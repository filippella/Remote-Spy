package org.dalol.remotespy.application;

import android.app.Application;
import android.content.Context;

import com.google.firebase.FirebaseApp;

/**
 * @author Filippo Engidashet
 * @version 1.0.0
 * @since Sun, 01/04/2018 at 11:50.
 */

public class MainApplication extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        FirebaseApp.initializeApp(this);
    }

    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onTerminate() {
        sContext = null;
        super.onTerminate();
    }
}
