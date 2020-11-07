package org.cocos2dx.platform;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
//import com.google.analytics.tracking.android.CampaignTrackingReceiver;
//import com.kochava.android.tracker.ReferralCapture;
//import com.mobileapptracker.Tracker;

public class InstallReferrerReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        //new Tracker().onReceive(context, intent);
        //new CampaignTrackingReceiver().onReceive(context, intent);//google play tracking
        //new ReferralCapture().onReceive(context, intent);
    }
}
