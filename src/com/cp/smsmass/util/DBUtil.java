package com.cp.smsmass.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Organization;

import com.cp.smsmass.entity.ContactsBean;

public class DBUtil {
	/**
	 * 获取所有联系人列表
	 * 
	 * @param activyt
	 * @return
	 */
	public static ArrayList<ContactsBean> getAllContacts(Activity activity) {
		ArrayList<ContactsBean> list = new ArrayList<ContactsBean>();
		ContentResolver cr = activity.getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
				null, null, null);
		if (cur.getCount() > 0) {
			while (cur.moveToNext()) {
				String conpany = null;
				Map<Integer, String> phoneInfo = new HashMap<Integer, String>();
				Map<Integer, String> emailInfo = new HashMap<Integer, String>();
				String jobdescription = null;
				String offLocation = null;
				String id = cur.getString(cur
						.getColumnIndex(ContactsContract.Contacts._ID));
				String name = cur
						.getString(cur
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				String noteWhere = ContactsContract.Data.CONTACT_ID
						+ " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
				String[] noteWhereParams = new String[] { id,
						ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE };
				Cursor noteCur = cr.query(ContactsContract.Data.CONTENT_URI,
						null, noteWhere, noteWhereParams, null);
				noteCur.close();
				// 电话号码
				if (Integer
						.parseInt(cur.getString(cur
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
					Cursor pCur = cr.query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = ?", new String[] { id }, null);
					while (pCur.moveToNext()) {
						String phoneNumber = pCur
								.getString(pCur
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						int phoneType = pCur
								.getInt(pCur
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
						phoneInfo.put(phoneType, phoneNumber);
					}
					pCur.close();
				}

				// email
				Cursor emailCur = cr.query(
						ContactsContract.CommonDataKinds.Email.CONTENT_URI,
						null, ContactsContract.CommonDataKinds.Email.CONTACT_ID
								+ " = ?", new String[] { id }, null);
				while (emailCur.moveToNext()) {
					String email = emailCur
							.getString(emailCur
									.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
					int emailType = emailCur
							.getInt(emailCur
									.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
					emailInfo.put(emailType, email);
				}
				emailCur.close();

				// 公司名称
				String orgWhere = ContactsContract.Data.CONTACT_ID
						+ " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
				String[] orgWhereParams = new String[] {
						id,
						ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE };
				Cursor orgCur = cr.query(ContactsContract.Data.CONTENT_URI,
						null, orgWhere, orgWhereParams, null);
				if (orgCur.moveToFirst()) {
					int work = orgCur
							.getInt(orgCur
									.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TYPE));
					if (work == Organization.TYPE_WORK) {
						conpany = orgCur
								.getString(orgCur
										.getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY));
						jobdescription = orgCur
								.getString(orgCur
										.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));
					}
				}
				orgCur.close();
				Cursor address = cr
						.query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
								null,
								ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID
										+ " = ?", new String[] { id }, null);
				if (address.moveToFirst()) {
					do {
						// 遍历所有的地址
						offLocation = address
								.getString(address
										.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
					} while (address.moveToNext());
				}
				address.close();
				list.add(new ContactsBean(phoneInfo, emailInfo, conpany,
						jobdescription, offLocation, id, name));
			}
			cur.close();
		}
		return list;
	}

}
