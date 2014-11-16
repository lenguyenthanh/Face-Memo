package lenguyenthanh.facememo.util;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.provider.ContactsContract;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.content.ContentProviderResult;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts.Data;

import org.apache.commons.io.IOUtils;

import lenguyenthanh.facememo.data.entities.Contact;
import lenguyenthanh.facememo.ui.fragments.EditContactFragment;
import timber.log.Timber;


/**
 * Created by lenguyenthanh on 11/16/14.
 */
public class ContactBuilder {
    private Contact contact;
    private EditContactFragment.ContactHelper contactHelper;
    private Context context;

    private ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
    private int rawContactInsertIndex;

    public ContactBuilder(Context context, Contact contact, EditContactFragment.ContactHelper contactHelper) {
        this.contact = contact;
        this.context = context;
        this.contactHelper = contactHelper;
    }

    public int build() {
        if(!contact.isLocalExist()) {
            init();
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Contacts.Data.RAW_CONTACT_ID,
                            rawContactInsertIndex)
                    .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(StructuredName.GIVEN_NAME, contact.getFirstName())
                    .withValue(StructuredName.FAMILY_NAME, contact.getLastName())
                    .build());
            if (!StringUtil.isEmpty(contact.getNumber())) {
                ops.add(ContentProviderOperation
                        .newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
                                rawContactInsertIndex)
                        .withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
                        .withValue(Phone.NUMBER, contact.getNumber())
                        .withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
                        .withValue(Phone.TYPE, "1").build());
            }

            if (!StringUtil.isEmpty(contact.getNote())) {
                ops.add(ContentProviderOperation
                        .newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
                                rawContactInsertIndex)
                        .withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE)
                        .withValue(Phone.NUMBER, contact.getNote())
                        .build());
            }

            if (!StringUtil.isEmpty(contact.getPhoto())) {
                byte[] photoByteArray = new byte[0];
                try {
                    photoByteArray = IOUtils.toByteArray(new FileInputStream(contact.getPhoto()));
                } catch (IOException e) {
                    Timber.e(e, "Read photo exception");
                }
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, photoByteArray)
                        .build());
            }


            try {
                ContentProviderResult[] res = context.getContentResolver().applyBatch(
                        ContactsContract.AUTHORITY, ops);
                if (res.length >= 1 && res[0] != null) {
                    if (!contact.isLocalExist())
                        return Integer.parseInt(res[0].uri.getLastPathSegment());
                    else return contact.getRawContactId();
                } else return -1;
            } catch (RemoteException e) {
                Timber.e(e, "RemoteException");
            } catch (OperationApplicationException e) {
                Timber.e(e, "RemoteException");
            }
            return -1;
        }else{
            rawContactInsertIndex = contact.getRawContactId();
            addName();
            addPhoneNumber();
            addPhoto();
            addNote();
            return contact.getRawContactId();
        }
    }

    private int build(ArrayList<ContentProviderOperation> ops) {
        if(ops == null || ops.size() == 0) return -1;
        try {
            ContentProviderResult[] res = context.getContentResolver().applyBatch(
                    ContactsContract.AUTHORITY, ops);
            if (res.length >= 1 && res[0] != null) {
                if (!contact.isLocalExist())
                    return Integer.parseInt(res[0].uri.getLastPathSegment());
                else return contact.getRawContactId();
            } else return -1;
        } catch (RemoteException e) {
            Timber.e(e, "RemoteException");
        } catch (OperationApplicationException e) {
            Timber.e(e, "RemoteException");
        }
        return -1;
    }


    private void init() {
        rawContactInsertIndex = ops.size();
        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());

    }

    private void addName() {
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        if (!contact.isLocalExist()) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Contacts.Data.RAW_CONTACT_ID,
                            rawContactInsertIndex)
                    .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(StructuredName.GIVEN_NAME, contact.getFirstName())
                    .withValue(StructuredName.FAMILY_NAME, contact.getLastName())
                    .build());
        } else {
            // Name
            ContentProviderOperation.Builder builder = ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI);
            builder.withSelection(ContactsContract.Data.CONTACT_ID + "=?" + " AND " + ContactsContract.Data.MIMETYPE + "=?", new String[]{String.valueOf(contact.getRawContactId()), ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE});
            builder.withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, contact.getFirstName());
            builder.withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, contact.getLastName());
            ops.add(builder.build());
        }
        build(ops);
    }

    private void addPhoneNumber() {
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        if (!contactHelper.hasNumber) {
            if (!StringUtil.isEmpty(contact.getNumber())) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, contact.getRawContactId());
                contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                contentValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.getNumber());
                contentValues.put(ContactsContract.CommonDataKinds.Phone.TYPE, "1");

                ops.add(ContentProviderOperation.newInsert(
                        ContactsContract.Data.CONTENT_URI).withValues(contentValues).build());
            }
        } else {
            ContentProviderOperation.Builder builder = ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI);
            builder.withSelection(ContactsContract.Data.CONTACT_ID + "=?" + " AND " + ContactsContract.Data.MIMETYPE + "=?" + " AND " + ContactsContract.CommonDataKinds.Organization.TYPE + "=?", new String[]{String.valueOf(contact.getRawContactId()), ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE, String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_HOME)});
            builder.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.getNumber());
            builder.withValue(Phone.TYPE, "1");
            ops.add(builder.build());
        }
        build(ops);
    }

    private void addNote() {
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        if (!contactHelper.hasNote) {
            if (!StringUtil.isEmpty(contact.getNote())) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, contact.getRawContactId());
                contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE);
                contentValues.put(ContactsContract.CommonDataKinds.Note.DATA1, contact.getNote());

                ops.add(ContentProviderOperation.newInsert(
                        ContactsContract.Data.CONTENT_URI).withValues(contentValues).build());
            }
        } else {
            ContentProviderOperation.Builder builder = ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI);
            builder.withSelection(ContactsContract.Data.CONTACT_ID + "=?" + " AND " + ContactsContract.Data.MIMETYPE + "=?" + " AND " + ContactsContract.CommonDataKinds.Organization.TYPE + "=?", new String[]{String.valueOf(contact.getRawContactId()), ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE, String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_HOME)});
            builder.withValue(ContactsContract.CommonDataKinds.Note.DATA1, contact.getNote());
            ops.add(builder.build());
        }
        build(ops);
    }

    private void addPhoto() {
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        if (contactHelper.hasPhoto && StringUtil.isEmpty(contact.getPhoto())) {
            //delete
        } else if (!StringUtil.isEmpty(contact.getPhoto())) {
            byte[] photoByteArray = new byte[0];
            try {
                photoByteArray = IOUtils.toByteArray(new FileInputStream(contact.getPhoto()));
            } catch (IOException e) {
                Timber.e(e, "Read photo exception");
            }
            if (contactHelper.hasPhoto) {
                ContentProviderOperation.Builder builder = ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI);
                builder.withSelection(ContactsContract.Data.CONTACT_ID + "=?" + " AND " + ContactsContract.Data.MIMETYPE + "=?", new String[]{String.valueOf(contact.getRawContactId()), ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE});
                builder.withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, photoByteArray);
                ops.add(builder.build());
            } else {
                ContentValues contentValues = new ContentValues();
                contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, contact.getRawContactId());
                contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE);
                contentValues.put(ContactsContract.CommonDataKinds.Photo.PHOTO, photoByteArray);
                ops.add(ContentProviderOperation.newInsert(
                        ContactsContract.Data.CONTENT_URI).withValues(contentValues).build());
            }
        }
        build(ops);
    }
}
