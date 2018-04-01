package org.dalol.remotespy.common.gcm;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmListenerService;

import org.dalol.remotespy.controller.MainController;

import static android.content.ContentValues.TAG;

/**
 * @author Filippo Engidashet [filippo.eng@gmail.com]
 * @version 1.0.0
 * @since Sunday, 25/03/2018 at 13:38.
 */

public class GCMReceiveListenerService extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);


        Toast.makeText(this, "Processing request...", Toast.LENGTH_SHORT).show();

        Bundle notification = data.getBundle("notification");
        if (notification != null) {
            String body = notification.getString("body");
            if (body != null) {
                MainController.getInstance().handleBody(body);
            }
        }
    }
}