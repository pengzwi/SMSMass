package com.cp.smsmass.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cp.smsmass.R;
import com.cp.smsmass.util.ShareToFriends;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;


/**
 * 发送分享的第三方应用列表
 * @author Jerry
 *
 */
public class ShareAppListActivity extends Activity{
	private GridView gv;
	List<ResolveInfo> list;

	private String imageURL;
	private String content;
	private File bitmapFile;
	private String filename;
	static String sendtype = "image/*";
	
	private String shareToWeixinContent,shareToWeixinTitle;
	Bitmap bmp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_applist);
		content = this.getIntent().getStringExtra("content");//分享到微博的内容
		
		imageURL = this.getIntent().getStringExtra("imageURL");//分享到微信的url
		SharedPreferences sp = this.getSharedPreferences("sharetoweixin", MODE_PRIVATE);
		shareToWeixinTitle =sp.getString("ShareToWeixinTitle", "");
		shareToWeixinContent = sp.getString("ShareToWixinContent", "");//分享到微信的内容
		final String url = this.getIntent().getStringExtra("url");
		gv = (GridView)findViewById(R.id.gv_shareapplist);
		list = ShareToFriends.getShareSendApps(this);
		filename = imageURL
				.substring(imageURL
						.lastIndexOf("/") + 1);
		
		MyAdapter adapter = new MyAdapter(list);
		gv.setAdapter(adapter);
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
				if(bmp==null){
					Toast.makeText(ShareAppListActivity.this, "图片正在加载，请稍后再试", Toast.LENGTH_SHORT).show();
					return;
				}
				ResolveInfo info = list.get(arg2);
				String packageName = info.activityInfo.packageName;
				String className = info.activityInfo.name;
				
				if(packageName.equals("com.tencent.mm")){//选择的是微信
					 SendMessageToWX.Req localReq = new SendMessageToWX.Req();
					if(className.equals("com.tencent.mm.ui.tools.ShareImgUI")){
						localReq.scene = SendMessageToWX.Req.WXSceneSession;
					}else{
						localReq.scene = SendMessageToWX.Req.WXSceneTimeline;
					}
					sendReq(ShareAppListActivity.this, shareToWeixinTitle,shareToWeixinContent, url,localReq);						
				}else{//其它app
					Intent intent=new Intent(Intent.ACTION_SEND);  
					//intent.setPackage(info.activityInfo.packageName);
					intent.setClassName(packageName, className);
					intent.setType(sendtype);//intent.setType("text/plain")				
					
					Uri uri = null;
					try {
						uri = getPicUri();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					intent.putExtra(Intent.EXTRA_STREAM, uri); 
					intent.putExtra(Intent.EXTRA_SUBJECT, "分享");  
					intent.putExtra(Intent.EXTRA_TEXT, content);  
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
					startActivity(intent);			
					bmp.recycle();
				}
				finish();
			}
		});
	}
	/**
	 * 分享到微信
	 * @param context
	 * @param text 分享内容
	 * @param url 分享链接
	 * @param localReq
	 */
	public void sendReq(Context context,String title,String text,String url, SendMessageToWX.Req localReq){
		WXWebpageObject localWXWebpageObject = new WXWebpageObject();
		localWXWebpageObject.webpageUrl = url;
		WXMediaMessage localWXMediaMessage = new WXMediaMessage(localWXWebpageObject);
		if(text.length()>=30)
			text = text.substring(0, 27) + "...";
		localWXMediaMessage.title = title;
		localWXMediaMessage.description = text;
		byte[] b = getPicIS();
		Log.e("aa", b.length+"");
		localWXMediaMessage.thumbData = b;
		
		 localReq.transaction = System.currentTimeMillis() + "";
		 localReq.message = localWXMediaMessage;
		 
		 IWXAPI api = WXAPIFactory.createWXAPI(context, "wx8b312a7e6c1f64f1", true);
		 
		boolean result = api.sendReq(localReq);
		
	}
	public byte[] getPicIS(){
		InputStream is;
		try {
			is = this.getAssets().open("logo.png");
			Bitmap bitmap = BitmapFactory.decodeStream(is);
			return getBitmapBytes(bitmap,false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null; 
	}
	public Uri getPicUri() throws IOException{
		InputStream is=this.getAssets().open("logo.png"); 
		File file = new File(Environment.getExternalStorageDirectory().getPath()+File.separator+"logo.png");
		if(file.exists())file.delete();
		file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
		byte[] a = new byte[1024] ;
		 int size;
		while ( (size = is.read(a)) != -1)     
		      fos.write(a, 0, size);    
		fos.write(a);
		fos.close();
		is.close();
		Uri u =Uri.fromFile(file);
		return u;
	}
	// 需要对图片进行处理，否则微信会在log中输出thumbData检查错误
    private static byte[] getBitmapBytes(Bitmap bitmap, boolean paramBoolean) {
        Bitmap localBitmap = Bitmap.createBitmap(80, 80, Bitmap.Config.RGB_565);
        Canvas localCanvas = new Canvas(localBitmap);
        int i;
        int j;
        if (bitmap.getHeight() > bitmap.getWidth()) {
            i = bitmap.getWidth();
            j = bitmap.getWidth();
        } else {
            i = bitmap.getHeight();
            j = bitmap.getHeight();
        }
        while (true) {
            localCanvas.drawBitmap(bitmap, new Rect(0, 0, i, j), new Rect(0, 0,80, 80), null);
            if (paramBoolean)
                bitmap.recycle();
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            localBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    localByteArrayOutputStream);
            localBitmap.recycle();
            byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
            try {
                localByteArrayOutputStream.close();
                return arrayOfByte;
            } catch (Exception e) {
                e.printStackTrace();
            }
            i = bitmap.getHeight();
            j = bitmap.getHeight();
        }
    }
	class MyAdapter extends BaseAdapter{
		List<ResolveInfo> appsList;
		public MyAdapter(List<ResolveInfo> appsList) {
			super();
			this.appsList = appsList;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return appsList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ResolveInfo info  = appsList.get(position);
			View item = (View)LayoutInflater.from(ShareAppListActivity.this).inflate(R.layout.share_applist_item, null);
			ImageView iv = (ImageView)item.findViewById(R.id.iv_shareapplist);
			TextView tv = (TextView)item.findViewById(R.id.tv_shareapplist);
			iv.setImageDrawable(info.loadIcon(getPackageManager()));
//info.activityInfo.packageName
			tv.setText(info.loadLabel(getPackageManager()));
			return item;
		}
		
	}
}
