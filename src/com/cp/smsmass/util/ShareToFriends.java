package com.cp.smsmass.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.provider.SyncStateContract.Constants;

import com.cp.smsmass.activity.ShareAppListActivity;
/**
 * 分享到第三方应用
 * @author Jerry
 *
 */
public class ShareToFriends {
	public static final int SHARE_TYPE_TEXT = 1;
	public static final int SHARE_TYPE_TEXT_AND_IMG = 2;
	static String sendtype = "image/*";
	/**
	 * 
	 * @param context
	 * @param content 分享内容
	 * @param imageURL 图片链接
	 * @param activityURL 信息链接（除微信外其他不需要）
	 */
	public static void share(Context context,String content){
		Intent intent = new Intent(context,ShareAppListActivity.class);
		intent.putExtra("content", content);
		intent.putExtra("imageURL", "");
		intent.putExtra("url", "");
		context.startActivity(intent);
	}
	/**
	 * 
	 * @param context
	 * @param content 分享内容
	 * @param imageURL 图片链接
	 * @param activityURL 信息链接（除微信外其他不需要）
	 */
	public static void missionShare(Context context,String content,String imageURL,String url){		
		Intent intent = new Intent(context,ShareAppListActivity.class);
		intent.putExtra("content", content);
		intent.putExtra("imageURL", imageURL);
		intent.putExtra("url", url);
		context.startActivity(intent);
	}
	public static List<ResolveInfo> getShareSendApps(Context context){
		List<ResolveInfo> appsList = new ArrayList<ResolveInfo>(); 
		ResolveInfo info = new ResolveInfo();
		
		 Intent intent=new Intent(Intent.ACTION_SEND,null);  
		 intent.addCategory(Intent.CATEGORY_DEFAULT);  
		 intent.setType(sendtype);  
		 PackageManager pm=context.getPackageManager();
		 appsList = pm.queryIntentActivities(intent,PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
		return appsList;
	}
}
