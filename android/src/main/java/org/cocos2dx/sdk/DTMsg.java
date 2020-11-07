package org.cocos2dx.sdk;

import com.loopj.android.http.BuildConfig;

public class DTMsg {
    public String extra;
    public String extraAddition;
    public int floatButtonSwitch;
    public String ip;
    public String item;
    public String password;
    public String purchaseInfo;
    public String userid;
    public String username;
    public String what;

    public DTMsg(String str) {
        this.what = BuildConfig.VERSION_NAME;
        this.username = BuildConfig.VERSION_NAME;
        this.password = BuildConfig.VERSION_NAME;
        this.item = BuildConfig.VERSION_NAME;
        this.userid = BuildConfig.VERSION_NAME;
        this.ip = BuildConfig.VERSION_NAME;
        this.purchaseInfo = BuildConfig.VERSION_NAME;
        this.what = str;
    }

    public void setExtra(String str, String str2) {
        this.extra = str;
        this.extraAddition = str2;
    }

    public void setFloatButtonSwith(int i) {
        this.floatButtonSwitch = i;
    }

    public void setLoginMsg(String str, String str2) {
        this.username = str;
        this.password = str2;
    }

    public void setPurchaseMsg(String str) {
        this.purchaseInfo = str;
    }

    public void setRegisterMsg(String str, String str2) {
        this.username = str;
        this.password = str2;
    }

    public void setWhat(String str) {
        this.what = str;
    }
}
