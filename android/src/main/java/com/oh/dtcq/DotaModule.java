package com.oh.dtcq;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

import android.content.Intent;
import sh.lilith.dgame.DGame;

public class DotaModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public DotaModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "Dota";
    }

	@ReactMethod
	public void enterGame(String token, int uid, Callback callback) {
		ReactApplicationContext context = getReactApplicationContext();
		Intent intent = new Intent(context, DGame.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("token", token);
		intent.putExtra("uid", uid);
		context.startActivity(intent);
	}
}
