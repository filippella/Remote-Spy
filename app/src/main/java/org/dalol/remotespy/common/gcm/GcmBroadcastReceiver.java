package org.dalol.remotespy.common.gcm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.widget.Toast;

import org.dalol.remotespy.controller.MainController;

/**
 * @author Filippo Engidashet [filippo.eng@gmail.com]
 * @version 1.0.0
 * @since Sunday, 25/03/2018 at 15:42.
 */

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data = intent.getExtras();
        if (data != null) {
            System.out.println("FLIPPO -> " + data.toString());

            String body = data.getString("gcm.notification.body");

            if (body != null) {
                MainController.getInstance().handleBody(body);
            }
        }

        Toast.makeText(context, "Received something", Toast.LENGTH_SHORT).show();
    }
}
