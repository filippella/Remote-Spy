package org.dalol.remotespy.common.gcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.dalol.remotespy.utilities.DeviceUtils;

/**
 * @author Filippo Engidashet [filippo.eng@gmail.com]
 * @version 1.0.0
 * @since Sunday, 25/03/2018 at 13:57.
 */
public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (DeviceUtils.checkPlayServices(context)) {
            Intent gcmIntent = new Intent(context, GCMRegistrationIntentService.class);
            context.startService(gcmIntent);
        }
    }
}