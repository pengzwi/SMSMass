package com.cp.smsmass.activity;

import net.youmi.android.spot.SpotManager;

import com.cp.smsmass.R;
import com.cp.smsmass.R.layout;
import com.cp.smsmass.R.menu;
import com.cp.smsmass.util.ShareToFriends;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends YouMengBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SpotManager.getInstance(this).showSpotAds(this);
//        ShareToFriends.share(this, "发现一款短信群发软件，大家都来用吧");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    protected void onRestart() {
    	// TODO Auto-generated method stub
    	super.onRestart();
    }
    
}
