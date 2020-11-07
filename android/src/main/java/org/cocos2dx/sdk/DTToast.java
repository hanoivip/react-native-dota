package org.cocos2dx.sdk;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class DTToast {
    private static Handler handler;
    private static Object synObj;
    private static Toast toast;

    /* renamed from: org.cocos2dx.sdk.DTToast.1 */
    static final class C15751 implements Runnable {
        final /* synthetic */ Context val$act;
        final /* synthetic */ int val$len;
        final /* synthetic */ String val$msg;

        C15751(Context context, String str, int i) {
            this.val$act = context;
            this.val$msg = str;
            this.val$len = i;
        }

        public void run() {
            synchronized (DTToast.synObj) {
                if (DTToast.toast == null) {
                    DTToast.toast = Toast.makeText(this.val$act, this.val$msg, this.val$len);
                } else {
                    DTToast.toast.setText(this.val$msg);
                    DTToast.toast.setDuration(this.val$len);
                }
                DTToast.toast.show();
            }
        }
    }

    static {
        handler = new Handler(Looper.getMainLooper());
        toast = null;
        synObj = new Object();
    }

    public static void showMessage(Context context, String str) {
        showMessage(context, str, 1);
    }

    public static void showMessage(Context context, String str, int i) {
        handler.post(new C15751(context, str, i));
    }
}
