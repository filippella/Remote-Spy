package org.dalol.remotespy.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.dalol.remotespy.services.GCMRegistrationIntentService;
import org.dalol.remotespy.utils.DeviceUtils;

/**
 * @author Filippo Engidashet [filippo.eng@gmail.com]
 * @version 1.0.0
 * @since Sunday, 25/03/2018 at 13:57.
 */
public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (DeviceUtils.checkPlayServices(context)) {
            Intent gcmIntent = new Intent(context, GCMRegistrationIntentService.class);
            context.startService(gcmIntent);
        }
    }
}