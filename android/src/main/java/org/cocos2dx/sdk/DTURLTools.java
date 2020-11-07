package org.cocos2dx.sdk;

import android.content.Intent;
import android.net.Uri;

public class DTURLTools {
    protected DTConfigure config;

    public DTURLTools(DTConfigure dTConfigure) {
        this.config = dTConfigure;
        dTConfigure.setURLTools(this);
    }

    public void open(String str) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse(str));
        this.config.getContext().startActivity(intent);
    }
}
