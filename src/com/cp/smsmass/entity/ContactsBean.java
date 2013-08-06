package com.cp.smsmass.entity;

import java.io.Serializable;
import java.util.Map;

import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;




/**
 * 通讯录实体
 * @author lvan
 *
 */
public class ContactsBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1851545343736678824L;
	public ContactsBean(Map<Integer, String> phoneInfo,
			 Map<Integer, String> emailInfo, String conpany,
			String jobdescription, String offLocation, 
			String id, String name) {
		super();
		this.phoneInfo = phoneInfo;
		this.emailInfo = emailInfo;
		this.conpany = conpany;
		this.jobdescription = jobdescription;
		this.offLocation = offLocation;
		this.id = id;
		this.name = name;
	}
	public ContactsBean(){
		
	}
	Map<Integer, String> phoneInfo;
	// key is Phone.TYPE_MOBILE or Phone.TYPE_WORK
	Map<Integer, String> emailInfo;
	// key is Email.TYPE_HOME or Email.TYPE_WORK
	String conpany;
	String jobdescription;
	String offLocation;
	String id;
	String name;
	byte[] title ;
	
	public byte[] getTitle() {
		return title;
	}
	public void setTitle(byte[] title) {
		this.title = title;
	}
	public Map<Integer, String> getPhoneInfo() {
		return phoneInfo;
	}
	public void setPhoneInfo(Map<Integer, String> phoneInfo) {
		this.phoneInfo = phoneInfo;
	}
	public Map<Integer, String> getEmailInfo() {
		return emailInfo;
	}
	public void setEmailInfo(Map<Integer, String> emailInfo) {
		this.emailInfo = emailInfo;
	}
	public String getConpany() {
		return conpany;
	}
	public void setConpany(String conpany) {
		this.conpany = conpany;
	}
	public String getJobdescription() {
		return jobdescription;
	}
	public void setJobdescription(String jobdescription) {
		this.jobdescription = jobdescription;
	}
	public String getOffLocation() {
		return offLocation;
	}
	public void setOffLocation(String offLocation) {
		this.offLocation = offLocation;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}