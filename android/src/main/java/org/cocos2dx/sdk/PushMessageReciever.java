package org.cocos2dx.sdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
//import com.igexin.sdk.PushConsts;
//import com.localytics.android.LocalyticsProvider.ProfileDbColumns;
import com.loopj.android.http.BuildConfig;

public class PushMessageReciever extends BroadcastReceiver {
    public static String sClientId;

    static {
        sClientId = BuildConfig.VERSION_NAME;
    }

    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        Log.d("GetuiSdkDemo", "onReceive() action=" + extras.getInt("action"));
        switch (extras.getInt("action")) {
            case 10001:
                byte[] byteArray = extras.getByteArray("payload");
                if (byteArray != null) {
                    Log.i("pushMessage", "Got Payload :" + new String(byteArray));
                }
            case 10002:
                String string = extras.getString("clientid");
                Log.i("pushMessage", "get client id: " + string);
                sClientId = string;
            default:
        }
    }
}
