package org.cocos2dx.sdk;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.loopj.android.http.BuildConfig;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DTAlarmManager {
	public static void alarmNotify(Context context, String str) {
		String optString;
		JSONException jSONException;
		long j;
		String str2;
		String str3;
		long j2;
		String str4;
		String str5;
		int i = 0;
		JSONException jSONException2;
		String str6;
		int i2;
		Intent intent;
		Bundle bundle;
		PendingIntent broadcast;
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService("alarm");
		int i3 = 0;
		String str7 = "\u5200\u5854\u4f20\u5947";
		String str8 = "\u5200\u5854\u4f20\u5947";
		String str9 = BuildConfig.VERSION_NAME;
		String str10 = "once";
		long currentTimeMillis = System.currentTimeMillis() / 1000;
		long j3 = 86400;
		String str11 = "rtc";
		String str12;
		try {
			JSONObject jSONObject = new JSONObject(str);
			optString = jSONObject.optString("ticker", "null");
			String optString2 = jSONObject.optString("title", "null");
			String optString3 = jSONObject.optString("text", "null");
			str10 = jSONObject.optString("tag", "once");
			currentTimeMillis = jSONObject.optLong("triggerAtMillis",
					System.currentTimeMillis() / 1000);
			long optLong = jSONObject.optLong("triggerOffset", 0);
			j3 = jSONObject.optLong("intervalAtMillis", 864000);
			str11 = jSONObject.optString("countTimeType", "rtc");
			currentTimeMillis *= 1000;
			optLong *= 1000;
			j3 *= 1000;
			i3 = jSONObject.optInt("id", 0);
			if (optString.equals("null")) {
				optString = str7;
			}
			str7 = !optString2.equals("null") ? optString2 : str8;
			optString2 = !optString3.equals("null") ? optString3 : str9;
			if (optLong > 0) {
				j2 = currentTimeMillis + optLong;
				i2 = i3;
				str2 = str7;
				str6 = optString;
				optString = str11;
				j = j2;
				j2 = j3;
				str5 = str10;
				str4 = optString2;
			} else {
				j2 = j3;
				str4 = optString2;
				str5 = str10;
				str12 = str7;
				j = currentTimeMillis;
				str2 = str12;
				i2 = i3;
				str6 = optString;
				optString = str11;
			}

		} catch (JSONException e3) {
			jSONException = e3;
			i = i3;
			jSONException2 = jSONException;
			long j5 = j3;
			str4 = str10;
			str5 = str7;
			j = currentTimeMillis;
			str2 = str11;
			str3 = str8;
			j2 = j5;
			jSONException2.printStackTrace();
			str6 = str5;
			str5 = str4;
			str4 = str9;
			str12 = str2;
			str2 = str3;
			i2 = i;
			optString = str12;
			if (optString.equals("rtc")) {
				if (optString.equals("rtc_wakeup")) {
					if (optString.equals("elapsed_wakeup")) {
					}
				}
			}
			intent = new Intent("dgame_receiver");
			bundle = new Bundle();
			bundle.putInt("flag", 1);
			bundle.putString("package", context.getPackageName());
			bundle.putString("ticker", str6);
			bundle.putString("title", str2);
			bundle.putString("text", str4);
			intent.putExtras(bundle);
			broadcast = PendingIntent.getBroadcast(context, i2, intent,
					134217728);
			if (str5.equals("once")) {
				alarmManager.setRepeating(i, j, j2, broadcast);
			} else {
				alarmManager.set(i, j, broadcast);
			}
		}
		if (optString.equals("rtc")) {
		}
		intent = new Intent("dgame_receiver");
		bundle = new Bundle();
		bundle.putInt("flag", 1);
		bundle.putString("package", context.getPackageName());
		bundle.putString("ticker", str6);
		bundle.putString("title", str2);
		bundle.putString("text", str4);
		intent.putExtras(bundle);
		broadcast = PendingIntent.getBroadcast(context, i2, intent, 134217728);
		if (str5.equals("once")) {
			alarmManager.set(i, j, broadcast);
		} else {
			alarmManager.setRepeating(i, j, j2, broadcast);
		}
	}

	public static void cancelNotify(Context context, int i) {
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService("alarm");
		PendingIntent broadcast = PendingIntent.getBroadcast(context, i,
				new Intent("dgame_receiver"), 536870912);
		if (broadcast != null) {
			alarmManager.cancel(broadcast);
		}
	}

	public static void cancelNotify(Context context, String str) {
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService("alarm");
		try {
			JSONArray optJSONArray = new JSONObject(str).optJSONArray("piids");
			for (int i = 0; i < optJSONArray.length(); i++) {
				PendingIntent broadcast = PendingIntent.getBroadcast(context,
						optJSONArray.getInt(i), new Intent("dgame_receiver"),
						536870912);
				if (broadcast != null) {
					alarmManager.cancel(broadcast);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
