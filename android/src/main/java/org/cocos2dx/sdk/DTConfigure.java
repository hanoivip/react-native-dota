package org.cocos2dx.sdk;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import sh.lilith.dgame.DGame;

/**
 * Execution context
 * 
 * @author toannguyen
 *
 */
public class DTConfigure {
    private static DTConfigure config;
    public static int sdkID;
    protected DTAlarmManager alarm;
    protected Context context;
    protected Activity dgame;
    protected DTHandler handler;
    protected DTNotification notification;
    protected DTPlatform platform;
    protected DTTools tools;
    protected DTURLTools urltools;
    
    //dotaus
    protected Application app;

    private DTConfigure() {
    }

    public static DTConfigure getInstance() {
        if (config == null) {
            config = new DTConfigure();
        }
        return config;
    }

    public Activity getActivity() {
        return this.dgame;
    }

    public DTAlarmManager getAlarmManager() {
        return this.alarm;
    }

    public Context getContext() {
        return this.context;
    }

    public DGame getDGame() {
        return (DGame) this.dgame;
    }

    public DTHandler getHandler() {
        return this.handler;
    }

    public DTNotification getNotification() {
        return this.notification;
    }

    public DTPlatform getPlatform() {
        return this.platform;
    }

    public DTTools getTools() {
        return this.tools;
    }

    public DTURLTools getURLTools() {
        return this.urltools;
    }

    public void setActivity(Activity activity) {
        this.dgame = activity;
    }

    public void setAlarmManager(DTAlarmManager dTAlarmManager) {
        this.alarm = dTAlarmManager;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setHandler(DTHandler dTHandler) {
        this.handler = dTHandler;
    }

    public void setNotification(DTNotification dTNotification) {
        this.notification = dTNotification;
    }

    public void setPlatform(DTPlatform dTPlatform) {
        this.platform = dTPlatform;
    }

    public void setTools(DTTools dTTools) {
        this.tools = dTTools;
    }

    public void setURLTools(DTURLTools dTURLTools) {
        this.urltools = dTURLTools;
    }

	public Application getApp() {
		return app;
	}

	public void setApp(Application app) {
		this.app = app;
	}
}
