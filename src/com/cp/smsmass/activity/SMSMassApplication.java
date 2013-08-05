package com.cp.smsmass.activity;

import net.youmi.android.AdManager;
import android.app.Application;
import cn.jpush.android.api.JPushInterface;

public class SMSMassApplication extends Application {
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
		JPushInterface.init(this); // 初始化 JPush
		AdManager.getInstance(this).init("c9251c4b199c15f9","eac94f50f06169f1", false); 
	}
}
