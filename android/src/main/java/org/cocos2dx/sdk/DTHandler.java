package org.cocos2dx.sdk;

import android.os.Handler;
import android.os.Message;
import org.cocos2dx.platform.Platform;

public class DTHandler extends Handler {
    private DTConfigure config;

    public DTHandler(DTConfigure dTConfigure) {
        this.config = dTConfigure;
        dTConfigure.setHandler(this);
    }

    public void handleMessage(Message message) {
        super.handleMessage(message);
        Platform platform = (Platform) this.config.getPlatform();
        DTMsg dTMsg = (DTMsg) message.obj;
        String str = dTMsg.what;
        if (str.equals("openLoginCenter")) {
            platform.OpenLoginCenter();
        } else if (str.equals("init")) {
            platform.Init();
        } else if (str.equals("fastLogin")) {
            platform.FastLogin(DTTools.getAddress());
        } else if (str.equals("login")) {
            platform.Login(dTMsg.username, dTMsg.password);
        } else if (str.equals("register")) {
            platform.Register(dTMsg.username, dTMsg.password);
        } else if (str.equals("logoff")) {
            platform.Logoff();
        } else if (str.equals("openWebCenter")) {
            platform.OpenWebCenter();
        } else if (str.equals("purchase")) {
            platform.Purchase(dTMsg.purchaseInfo);
        } else if (str.equals("switchFloatButton")) {
            platform.switchFloatButton(dTMsg.floatButtonSwitch);
        } else if (str.equals("exit")) {
            platform.exit();
        } else if (str.equals("extra")) {
            platform.doExtra(dTMsg.extra, dTMsg.extraAddition);
        }
    }
}
