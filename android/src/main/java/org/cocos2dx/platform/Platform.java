package org.cocos2dx.platform;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.cocos2dx.sdk.DTPlatform;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * CP system adaptor. CP specifier
 * 
 * @author toannguyen
 *
 */
public class Platform extends DTPlatform {
    private static final String APPFLYER_ADGROUP_ID_KEY = "adgroup_id";
    private static final String APPFLYER_ADGROUP_NAME_KEY = "adgroup_name";
    private static final String APPFLYER_ADSET_ID_KEY = "adset_id";
    private static final String APPFLYER_ADSET_NAME_KEY = "adset_name";
    private static final String APPFLYER_AD_ID_KEY = "ad_id";
    private static final String APPFLYER_AF_SITEID_KEY = "af_siteid";
    private static final String APPFLYER_AF_SUB1_KEY = "af_sub1";
    private static final String APPFLYER_AF_SUB2_KEY = "af_sub2";
    private static final String APPFLYER_AF_SUB3_KEY = "af_sub3";
    private static final String APPFLYER_AF_SUB4_KEY = "af_sub4";
    private static final String APPFLYER_AF_SUB5_KEY = "af_sub5";
    private static final String APPFLYER_CAMPAIGN_NAME_KEY = "campaign";
    private static final String APPFLYER_COMPAIGN_ID_KEY = "campaign_id";
    private static final String APPFLYER_INSTALL_TIME_KEY = "install_time";
    private static final String APPFLYER_IS_FB_KEY = "is_fb";
    private static final String APPFLYER_MEDIA_SOURCE_KEY = "media_source";
    private static String APP_ID_CONNECTOR = null;
    private static String FaceBookID = null;
    private static String MY_ACCOUNT_CONVERSION_ID = null;
    public static final int RESULT_OK = -1;
    private static int RechargeType;
    private static int SDK_ID;
    private static String ad_id;
    private static String adgroup_id;
    private static String adgroup_name;
    private static String adset_id;
    private static String adset_name;
    private static String af_siteid;
    private static String af_sub1;
    private static String af_sub2;
    private static String af_sub3;
    private static String af_sub4;
    private static String af_sub5;
    private static String appsFlyerId;
    private static String campaign_id;
    private static String campaign_name;
    private static String googleplayInfo;
    private static String googleplaySign;
    private static String install_time;
    private static String is_fb;
    private static String media_source;
    private static String payment;
    private static String vietnam_to_serverInfo;
    private static String vng_Level;
    private static String vng_servername;
    private int LoginSDK_ID;
    private final int REQUEST_OAUTH_CODE;
    private final int REQUEST_PERMISSION;
    Handler ggHandler;
    private final String keyContent;
    private final String keyMyPackage;
    private String localeProducts;
    private final String packageHotro;
    private Platform plat;
    private ArrayList<String> productIds;
    private String serverid;
    private String vng_userid;



    static {
        googleplaySign = "xxx";
        googleplayInfo = "xxx";
        APP_ID_CONNECTOR = "1186149433682093493";
        MY_ACCOUNT_CONVERSION_ID = "1986";
        FaceBookID = "796538840408929";
        RechargeType = 1;
        vietnam_to_serverInfo = null;
        payment = "offical";
    }

    public Platform() {
        this.packageHotro = "vng.cs.td.hotro";
        this.vng_userid = "xxx";
        this.userid = "xxx";
        this.serverid = "xxx";
        this.REQUEST_OAUTH_CODE = 1234;
        this.REQUEST_PERMISSION = 1111;
        this.productIds = new ArrayList();
        this.plat = this;
        this.keyContent = "HoTroInterContent";
        this.keyMyPackage = "PackageName";
    }

    private String getInfo() {
        Map hashMap = new HashMap();
        hashMap.put(APPFLYER_MEDIA_SOURCE_KEY, media_source);
        hashMap.put("campaign_name", campaign_name);
        hashMap.put(APPFLYER_COMPAIGN_ID_KEY, campaign_id);
        hashMap.put(APPFLYER_IS_FB_KEY, is_fb);
        hashMap.put(APPFLYER_ADGROUP_NAME_KEY, adgroup_name);
        hashMap.put(APPFLYER_ADGROUP_ID_KEY, adgroup_id);
        hashMap.put(APPFLYER_ADSET_NAME_KEY, adset_name);
        hashMap.put(APPFLYER_ADSET_ID_KEY, adset_id);
        hashMap.put(APPFLYER_AD_ID_KEY, ad_id);
        hashMap.put(APPFLYER_AF_SITEID_KEY, af_siteid);
        hashMap.put(APPFLYER_AF_SUB1_KEY, af_sub1);
        hashMap.put(APPFLYER_AF_SUB2_KEY, af_sub2);
        hashMap.put(APPFLYER_AF_SUB3_KEY, af_sub3);
        hashMap.put(APPFLYER_AF_SUB4_KEY, af_sub4);
        hashMap.put(APPFLYER_AF_SUB5_KEY, af_sub5);
        hashMap.put(APPFLYER_INSTALL_TIME_KEY, install_time);
        return new JSONObject(hashMap).toString();
    }

    private String intToIp(int i) {
        return "x.x.x.x";
    }

    public static boolean isInstalledApp(Context context, String str) {
        Log.e("Zalo", "Start check");
        try {
            return context.getPackageManager().getPackageInfo(str, 0) != null;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    private void launchComponent(String str, String str2, String str3) {
        String str4 = vng_servername;
        String str5 = this.vng_userid;
        String str6 = vng_Level;
        String str7 = Build.MODEL;
        str4 = (((((((str3 + "PlayerServer: " + str4 + "\n") + "PlayerRole: " + str5 + "\n") + "PlayerLevel: " + str6 + "\n") 
        		+ "DeviceType: " + str7 + "\n") + "DeviceOS:  " + "Android" + "\n") 
        		+ "DeviceVersion: " + VERSION.RELEASE + "\n") + "DeviceMemory: " + "167981056" + "\n") 
        		+ "Description:" + "xxx" + "\n";
        str5 = this.config.getActivity().getApplicationContext().getPackageName();
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setComponent(new ComponentName(str, str2));
        intent.setFlags(268435456);
        intent.putExtra("HoTroInterContent", str4);
        intent.putExtra("PackageName", str5);
        this.config.getActivity().startActivity(intent);
    }


    private void purchaseDone() {
        this.config.getTools().purchaseDone();
    }

    private void setSDKID(int i) {
        this.config.getTools().setSDKID(i);
    }

    private void setUserInfo(String str, String str2) {
        this.config.getTools().setUserID(true, str, str2);
    }

    private void showInMarket(String str) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + str));
        intent.setFlags(268435456);
        this.config.getActivity().startActivity(intent);
    }

    public void FastLogin(String str) {
    }

    public void Init() {
        Log.d("Zalo", "init..");

    }

    public void Login(String str, String str2) {
    }

    public void Logoff() {
    }

    public void OpenLoginCenter() {
        Log.e("Zalo", "OpenLoginCenter");
        this.config.getTools().setUserID(true, this.token, this.userid);
    }

    public void OpenWebCenter() {
    }

    public void Purchase(String str) {
    	Log.d("dotaus", "Purchase request param " + str);
    	
    }

    public void Register(String str, String str2) {
    }

    public void closeFloatButton() {
    }

    public void dealGameData(String str) {
    	Log.d("Platform", "dealing with game data:" + str);
        
    }

    public void destroy() {
    }

    public void exit() {
    }

    public String getLocaleProduct(String str) {
        String itemtowaresid = itemtowaresid(str);
        if (this.localeProducts == null) {
            return "xxx";
        }
        try {
            JSONArray jSONArray = new JSONArray(this.localeProducts);
            for (int i = 0; i < jSONArray.length(); i++) {
                if (new JSONObject(jSONArray.getString(i)).optString("productId", "xxx").equals(itemtowaresid)) {
                    return String.format("id:%s,title:%s,price:%s,type:%s,desc:%s,code:%s,micros:%s,", 
                    		new Object[]{new JSONObject(jSONArray.getString(i)).optString("productId", "xxx"), 
                    		new JSONObject(jSONArray.getString(i)).optString("title", "xxx"), 
                    		new JSONObject(jSONArray.getString(i)).optString("price", "xxx"), 
                    		new JSONObject(jSONArray.getString(i)).optString("type", "xxx"), 
                    		new JSONObject(jSONArray.getString(i)).optString("description", "xxx"), 
                    		new JSONObject(jSONArray.getString(i)).optString("price_amount_micros", "xxx"), 
                    		new JSONObject(jSONArray.getString(i)).optString("price_currency_code", "xxx")});
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "xxx";
    }

    public String getParam(String str) {
    	/*
        if (str.equals("googleplaySign")) {
            return googleplaySign;
        }
        if (str.equals("googleplayInfo")) {
            return googleplayInfo;
        }
        Message message;
        if (str.equals("initgoogle")) {
            message = new Message();
            message.what = 1;
            message.obj = this;
            this.ggHandler.sendMessage(message);
        } else if (str.equals("1")) {
            message = new Message();
            message.what = 2;
            this.ggHandler.sendMessage(message);
            return "true";
        } else if (str.equals("checkGooglePurchease")) {
            message = new Message();
            message.what = 4;
            this.ggHandler.sendMessage(message);
        } else if (Pattern.matches("^localeproduct.*", str)) {
            if (this.localeProducts != null) {
                Matcher matcher = Pattern.compile("localeproduct-(.*)").matcher(str);
                if (matcher.find()) {
                    return getLocaleProduct(matcher.group(1));
                }
            }
            return "search failed";
        } else if (str.equals("checkPurcheaseMethod")) {
            return payment.equals("offical") ? "offical" : "other";
        } */
        if (str.equals("VietnamGetInfo")) {
            Log.e("Zalo", "VietnamGetInfo");
            return getInfo();
        }

        return super.getParam(str);
    }

    public String itemtowaresid(String str) {
        return "";
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        //if (i == M6_LoginManager.REQUEST_CODE_LOGIN) {
        //    this.m6_loginManager.onActivityResult(this.config.getActivity(), i, i2, intent);
        //}
        //if (GoogleInAppHandler.sharedInstance().mHelper == null || !GoogleInAppHandler.sharedInstance().mHelper.handleActivityResult(i, i2, intent)) {
        //    super.onActivityResult(i, i2, intent);
        //}
    }

    public void onGameResume() {
        //if (mFirebaseAnalytics != null)
        //    mFirebaseAnalytics.logEvent("resume", null);
    }

    public void onGameStart() {
        super.onGameStart();
    }

    public void onGameStop() {
        super.onGameStop();
    }

    public void onLoginFailed(int i, String str)
    {
        //Bundle bundle = new Bundle();
        //bundle.putInt("int", i);
        //bundle.putString("str", str);
        //mFirebaseAnalytics.logEvent("login", bundle);

        Log.e("Zalo", "login fail");
    }

    public void onLoginSuccessful(String userId, String str2, String sessionId, String str4)
    {
        Log.e("Zalo", "onLoginSuccessful");
        setUserInfo(sessionId, userId);
        Log.e("Zalo", sessionId + "***" + userId);

        //Bundle bundle = new Bundle();
        //bundle.putString("user", userId);
        //mFirebaseAnalytics.logEvent("login", bundle);
    }

    public void openFloatButton() {
    }

    public void pause() {
    }

    public void setGoogleplayInfo(String str, String str2) {
        googleplaySign = str;
        googleplayInfo = str2;
        Log.e("Zalo", "setGoogleplayInfo");
        purchaseDone();
    }

    public void setID() {
        SDK_ID = ZALO_ID;
        setSDKID(SDK_ID);
    }
}
