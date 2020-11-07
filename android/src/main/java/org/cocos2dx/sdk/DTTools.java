package org.cocos2dx.sdk;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Debug;
import android.os.Environment;
import android.os.Message;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.loopj.android.http.BuildConfig;
import java.io.File;
import java.util.List;
import org.cocos2dx.lib.Cocos2dxGLSurfaceView;

/**
 * User ultility
 * 
 * @author toannguyen
 * 
 */
public class DTTools {
	private static final long MEGABYTE = 1048576;
	private static DTConfigure config;

	public DTTools(DTConfigure dTConfigure) {
		config = dTConfigure;
		dTConfigure.setTools(this);
	}

	public static long bytesToMegabytes(long j) {
		return j / MEGABYTE;
	}

	public static final void callExtra(String str, String str2) {
		DTMsg dTMsg = new DTMsg("extra");
		dTMsg.setExtra(str, str2);
		sendMsg(dTMsg);
	}

	public static final void callSDK(String str) {
		checkCodeValid(str);
		sendMsg(str);
	}

	public static boolean checkAction() {
		return getTopPackageName().equals(config.context.getPackageName());
	}

	public static boolean checkCodeValid(String str) {
		String[] strArr = new String[] { "init", "openLoginCenter",
				"fastLogin", "login", "register", "logoff", "purchase",
				"openWebCenter", "extra" };
		for (String equals : strArr) {
			if (equals.equals(str)) {
				return true;
			}
		}
		System.out.println("Invalid code : " + str);
		return false;
	}

	public static boolean checkNetActive() {
		NetworkInfo activeNetworkInfo = ((ConnectivityManager) config
				.getContext().getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();
		return activeNetworkInfo != null
				&& activeNetworkInfo.getState() == State.CONNECTED;
	}

	public static final boolean checkNetwork(String str) {
		return ((ConnectivityManager) config.getContext().getSystemService(
				Context.CONNECTIVITY_SERVICE)).getNetworkInfo(str == "mobile" ? 0 : 1)
				.getState() == State.CONNECTED;
	}

	public static final boolean checkWeekDevice(String str) {
		return ((long) (((double) getTotalMemory()) / 1048576.0d)) < 128;
	}

	public static void doFinish() {
		config.getActivity().finish();
	}

	public static void doFinishAndKill() {
		doFinish();
		doKillProcess();
		doSystemExit();
	}

	public static void doKillProcess() {
		config.getPlatform().isDestroyKillProcess = true;
	}

	public static void doSystemExit() {
		System.exit(0);
	}

	public static void exitGame() {
		doFinishAndKill();
	}

	public static boolean externalMemoryAvailable() {
		return Environment.getExternalStorageState().equals("mounted");
	}

	public static String getAddress() {
		String deviceId = ((TelephonyManager) config.getActivity()
				.getBaseContext().getSystemService("phone")).getDeviceId();
		return deviceId == null ? "StrangeDevice" : deviceId;
	}

	private static final long getAvailMemory() {
		ActivityManager activityManager = (ActivityManager) config
				.getActivity().getSystemService("activity");
		MemoryInfo memoryInfo = new MemoryInfo();
		activityManager.getMemoryInfo(memoryInfo);
		return memoryInfo.availMem;
	}

	public static long getAvailableExternalStorageSize() {
		if (!externalMemoryAvailable()) {
			return -1;
		}
		StatFs statFs = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		return (((long) statFs.getAvailableBlocks()) * ((long) statFs
				.getBlockSize())) / 1024;
	}

	public static long getAvailableInternalMemorySize() {
		StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
		return (((long) statFs.getAvailableBlocks()) * ((long) statFs
				.getBlockSize())) / 1024;
	}

	public static final String getMemoryInfo() {
		Debug.MemoryInfo r0 = new Debug.MemoryInfo();
		Debug.getMemoryInfo(r0);
		String format = String
				.format("Memory: TotalPss=%.2f MB, c++Heap(%.2f/%.2fM), JavaHeap(%.2f/%.2fM), Phone(%.2f/%.2fM)",
						new Object[] {
								Double.valueOf(((double) r0.getTotalPss()) / 1024.0d),
								Double.valueOf(((double) Debug
										.getNativeHeapAllocatedSize()) / 1048576.0d),
								Double.valueOf(((double) Debug
										.getNativeHeapSize()) / 1048576.0d),
								Double.valueOf(((double) (Runtime.getRuntime()
										.totalMemory() - Runtime.getRuntime()
										.freeMemory())) / 1048576.0d),
								Double.valueOf(((double) Runtime.getRuntime()
										.totalMemory()) / 1048576.0d),
								Double.valueOf(((double) getAvailMemory()) / 1048576.0d),
								Double.valueOf(((double) getTotalMemory()) / 1048576.0d) });
		System.out.println(format);
		return format;
	}

	public static String getPackageName() {
		return config.context.getPackageName();
	}

	public static final String getParam(String str) {
		Log.d("DTTools", "get value of param:" + str);
		return config.getPlatform().getParam(str);
	}

	public static String getSharedStoragePath() {
		return Environment.getExternalStorageDirectory().getPath();
	}

	public static String getTopPackageName() {
		List runningTasks = ((ActivityManager) config.context
				.getSystemService("activity")).getRunningTasks(1);
		return !runningTasks.isEmpty() ? ((RunningTaskInfo) runningTasks.get(0)).topActivity
				.getPackageName() : BuildConfig.VERSION_NAME;
	}

	@TargetApi(16)
	private static final long getTotalMemory() {
		ActivityManager activityManager = (ActivityManager) config
				.getActivity().getSystemService("activity");
		MemoryInfo memoryInfo = new MemoryInfo();
		activityManager.getMemoryInfo(memoryInfo);
		return memoryInfo.totalMem;
	}

	public static final int getVersionCode() {
		int i = 0;
		try {
			return config.context.getPackageManager().getPackageInfo(
					config.context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return i;
		}
	}

	public static final String getVersionName() {
		try {
			return config.context.getPackageManager().getPackageInfo(
					config.context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static final boolean installApkProgrammatically(String str) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.setDataAndType(Uri.fromFile(new File(str)),
				"application/vnd.android.package-archive");
		intent.setFlags(268435456);
		config.context.startActivity(intent);
		return true;
	}

	public static final void login(String str, String str2) {
		DTMsg dTMsg = new DTMsg("login");
		dTMsg.setLoginMsg(str, str2);
		sendMsg(dTMsg);
	}

	public static final void purchase(String str) {
		DTMsg dTMsg = new DTMsg("purchase");
		dTMsg.setPurchaseMsg(str);
		sendMsg(dTMsg);
	}

	public static final void register(String str, String str2) {
		DTMsg dTMsg = new DTMsg("register");
		dTMsg.setRegisterMsg(str, str2);
		sendMsg(dTMsg);
	}

	public static void restart() {
		Context activity = config.getActivity();
		Context baseContext = config.getActivity().getBaseContext();
		activity.startActivity(baseContext.getPackageManager()
				.getLaunchIntentForPackage(baseContext.getPackageName()));
		doSystemExit();
	}

	public static void sendMsg(String str) {
		sendMsg(new DTMsg(str));
	}

	public static void sendMsg(DTMsg dTMsg) {
		Message message = new Message();
		message.obj = dTMsg;
		config.getHandler().sendMessage(message);
	}

	public static final void switchFloatButton(int i) {
		DTMsg dTMsg = new DTMsg("switchFloatButton");
		dTMsg.setFloatButtonSwith(i);
		sendMsg(dTMsg);
	}

	public void SDKInit() {
		Cocos2dxGLSurfaceView.getInstance().queueEvent(new Runnable() {
			public void run() {
				DTTools.this.doSDKInit();
			}
		});
	}

	public native void doPurchaseDone();

	public native void doSDKInit();

	public native void doSetSoundSwitch(boolean z);

	public native void doSetUserID(boolean result, String sessionId,
			String userId);

	public native void exit();

	public void openURL(String str) {
		config.getURLTools().open(str);
	}

	public void purchaseDone() {
		Cocos2dxGLSurfaceView.getInstance().queueEvent(new Runnable() {
			public void run() {
				DTTools.this.doPurchaseDone();
			}
		});
	}

	public native void setDeviceID(String str);

	public native void setSDKID(int i);

	public void setSoundSwitch(final boolean z) {
		Cocos2dxGLSurfaceView.getInstance().queueEvent(new Runnable() {
			public void run() {
				DTTools.this.doSetSoundSwitch(z);
			}
		});
	}

	public void setUserID(final boolean result, final String session,
			final String uid) {
		Cocos2dxGLSurfaceView.getInstance().queueEvent(new Runnable() {
			public void run() {
				DTTools.this.doSetUserID(result, session, uid);
			}
		});
	}

	public native void setgBundleVersion(String str);
}
