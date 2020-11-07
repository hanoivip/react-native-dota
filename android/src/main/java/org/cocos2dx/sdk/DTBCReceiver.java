package org.cocos2dx.sdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class DTBCReceiver extends BroadcastReceiver {
    public static final int FLAG_NOTIFY = 1;

    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        int i = extras.getInt("flag");
        String string = extras.getString("package");
        String string2 = extras.getString("ticker");
        String string3 = extras.getString("title");
        String string4 = extras.getString("text");
        switch (i) {
            case FLAG_NOTIFY /*1*/:
                DTNotification.doNotify(context, string, string2, string3, string4);
            default:
        }
    }
}
