package org.dalol.remotespy.utilities;

import android.content.Context;
import android.os.Build;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

/**
 * @author Filippo Engidashet [filippo.eng@gmail.com]
 * @version 1.0.0
 * @since Sunday, 25/03/2018 at 13:59.
 */

public class DeviceUtils {

    public static boolean checkPlayServices(Context context) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        return apiAvailability.isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS;
    }

    public static String getDeviceId() {
        return String.format("%s:%s:%s", Build.BRAND, Build.MODEL, Build.SERIAL);
    }
}
