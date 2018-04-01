package org.dalol.remotespy.common.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.dalol.remotespy.data.ApiResponse;
import org.dalol.remotespy.R;
import org.dalol.remotespy.data.api.RegisterTokenApi;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Filippo Engidashet [filippo.eng@gmail.com]
 * @version 1.0.0
 * @since Sunday, 25/03/2018 at 13:39.
 */

public class GCMRegistrationIntentService extends IntentService {

    public static final String EXTRA_TOKEN_REFRESHED = "org.dalol.remotespy.services.EXTRA_TOKEN_REFRESHED";

    private static final String TAG = GCMRegistrationIntentService.class.getSimpleName();
    private static final String[] TOPICS = {"global"};

    public GCMRegistrationIntentService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Registering...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.remotespy_gcm_sender_id), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            Log.i(TAG, "GCM Registration Token: " + token);

            sendRegistrationToServer(token); //Send token to server

            //subscribeTopics(token);
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
        }
        Log.d(TAG, "REGISTRATION_COMPLETE");
        // Notify UI that registration has completed, so the progress indicator can be hidden.
//        Intent registrationComplete = new Intent(QuickstartPreferences.REGISTRATION_COMPLETE);
//        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Persist registration to third-party servers.
     *
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.

        String deviceId = Build.BRAND +":"+Build.MODEL + ":"+Build.SERIAL;

        try{
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://www.dalol.org")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            RegisterTokenApi api = retrofit.create(RegisterTokenApi.class);
            Call<ApiResponse> call = api.registerToken(deviceId, token);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        ApiResponse apiResponse = response.body();
                        if (apiResponse != null) {
                            //Toast.makeText(GCMRegistrationIntentService.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            //Toast.makeText(GCMRegistrationIntentService.this, response.message(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        //Toast.makeText(GCMRegistrationIntentService.this, response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    //Toast.makeText(GCMRegistrationIntentService.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch(Exception e){
            System.err.println(e.getMessage());
        }
    }

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
}