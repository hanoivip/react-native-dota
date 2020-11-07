package org.cocos2dx.sdk;

import android.content.Intent;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Process;
import android.util.Log;
import com.loopj.android.http.BuildConfig;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class DTPlatform {
	
	protected static final int ZALO_ID = 137;

	public static String activityName;
	protected String alarmData;
	protected String alarmIds;
	protected DTConfigure config;
	protected String gameData;
	protected String gameName;
	protected boolean isDestroyKillProcess;
    
    public String userid;
    public String token;

	public DTPlatform() {
		this.isDestroyKillProcess = false;
		this.alarmData = BuildConfig.VERSION_NAME;
		this.alarmIds = BuildConfig.VERSION_NAME;
		this.gameData = BuildConfig.VERSION_NAME;
		this.gameName = "dgame of lilith";
	}

	public abstract void FastLogin(String str);

	public abstract void Init();

	public abstract void Login(String str, String str2);

	public abstract void Logoff();

	public abstract void OpenLoginCenter();

	public abstract void OpenWebCenter();

	public abstract void Purchase(String str);

	public abstract void Register(String str, String str2);

	public abstract void closeFloatButton();

	public void dealGameData(String str) {
	}

	public void destroy() {
		if (this.isDestroyKillProcess) {
			this.config.getHandler().post(new Runnable() {
				public void run() {
					Process.killProcess(Process.myPid());
				}
			});
		}
	}

	public void doExtra(String str, String str2) {
		if (str.equals("openURL")) {
			this.config.getTools().openURL(str2);
		} else if (str.equals("setAlarm")) {
			DTAlarmManager.alarmNotify(this.config.getContext(), str2);
		} else if (str.equals("setAlarmData")) {
			this.alarmData = str2;
		} else if (str.equals("cancelAlarm")) {
			DTAlarmManager.cancelNotify(this.config.getContext(),
					Integer.parseInt(str2));
		} else if (str.equals("setAlarmIds")) {
			this.alarmIds = str2;
		} else if (str.equals("clearAlarm")) {
			this.alarmIds = str2;
			DTAlarmManager.cancelNotify(this.config.getContext(), str2);
		} else if (str.equals("setGameData")) {
			this.gameData = str2;
			dealGameData(this.gameData);
		} else if (str.equals("restart")) {
			DTTools.restart();
		}
	}

	public abstract void exit();

	public void gameInit() {
		//PushManager.getInstance().initialize(
		//		this.config.getContext().getApplicationContext());
		this.config.getTools().setDeviceID(DTTools.getAddress());
		this.config.getTools().setgBundleVersion(DTTools.getVersionName());
		if (VERSION.SDK_INT >= 9) {
			this.config.getActivity().setRequestedOrientation(6);
		}
		DTURLTools dTURLTools = new DTURLTools(this.config);
	}

	public String getGameData(String str) {
		try {
			return new JSONObject(this.gameData).optString(str);
		} catch (JSONException e) {
			e.printStackTrace();
			return BuildConfig.VERSION_NAME;
		}
	}

	public String getParam(String str) {
		Log.d("DTPlatform", "get param key:"+str);
		return str.equals("gameName") ? this.gameName : str
				.equals("ipushClientId") ? PushMessageReciever.sClientId : str
				.equals("deviceModel") ? Build.MODEL : str
				.equals("deviceVersionRelease") ? VERSION.RELEASE : str
				.equals("deviceLanguage") ? this.config.getActivity()
				.getResources().getConfiguration().locale.getLanguage() : str
				.equals("deviceOS") ? "Android"
				: "";
	}

	public void log(String str) {
		System.out.println("-->>>>>>" + str);
	}

	public void onActivityResult(int i, int i2, Intent intent) {
	}

	public void onGameResume() {
	}

	public void onGameStart() {
		if (!this.alarmIds.equals(BuildConfig.VERSION_NAME)) {
			DTAlarmManager
					.cancelNotify(this.config.getContext(), this.alarmIds);
		}
	}

	public void onGameStop() {
		try {
			JSONArray optJSONArray = new JSONObject(this.alarmData)
					.optJSONArray("data");
			for (int i = 0; i < optJSONArray.length(); i++) {
				DTAlarmManager.alarmNotify(this.config.getContext(),
						optJSONArray.get(i).toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void onNewIntent(Intent intent) {
	}

	public abstract void openFloatButton();

	public abstract void pause();

	public void setConfigure(DTConfigure dTConfigure) {
		this.config = dTConfigure;
		dTConfigure.setPlatform(this);
	}

	public abstract void setID();

	public void switchFloatButton(int i) {
		if (i <= 0) {
			closeFloatButton();
		} else {
			openFloatButton();
		}
	}
}
