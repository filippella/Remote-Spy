package org.dalol.remotespy.common.gcm;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * @author Filippo Engidashet [filippo.eng@gmail.com]
 * @version 1.0.0
 * @since Sunday, 25/03/2018 at 13:38.
 */
public class GCMIDIntentService extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, GCMRegistrationIntentService.class);
        intent.putExtra(GCMRegistrationIntentService.EXTRA_TOKEN_REFRESHED, true);
        startService(intent);
    }
}
