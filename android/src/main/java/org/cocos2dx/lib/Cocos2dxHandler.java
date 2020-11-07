package org.cocos2dx.lib;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.os.Message;
import java.lang.ref.WeakReference;

public class Cocos2dxHandler extends Handler {
    public static final int HANDLER_SHOW_DIALOG = 1;
    public static final int HANDLER_SHOW_EDITBOX_DIALOG = 2;
    private WeakReference<Cocos2dxActivity> mActivity;

    /* renamed from: org.cocos2dx.lib.Cocos2dxHandler.1 */
    class C24821 implements OnClickListener {
        C24821() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
        }
    }

    public static class DialogMessage {
        public String message;
        public String titile;

        public DialogMessage(String str, String str2) {
            this.titile = str;
            this.message = str2;
        }
    }

    public static class EditBoxMessage {
        public String content;
        public int inputFlag;
        public int inputMode;
        public int maxLength;
        public int returnType;
        public String title;

        public EditBoxMessage(String str, String str2, int i, int i2, int i3, int i4) {
            this.content = str2;
            this.title = str;
            this.inputMode = i;
            this.inputFlag = i2;
            this.returnType = i3;
            this.maxLength = i4;
        }
    }

    public Cocos2dxHandler(Cocos2dxActivity cocos2dxActivity) {
        this.mActivity = new WeakReference(cocos2dxActivity);
    }

    private void showDialog(Message message) {
        DialogMessage dialogMessage = (DialogMessage) message.obj;
        new Builder((Cocos2dxActivity) this.mActivity.get()).setTitle(dialogMessage.titile).setMessage(dialogMessage.message).setPositiveButton("Ok", new C24821()).create().show();
    }

    private void showEditBoxDialog(Message message) {
        EditBoxMessage editBoxMessage = (EditBoxMessage) message.obj;
        new Cocos2dxEditBoxDialog((Context) this.mActivity.get(), editBoxMessage.title, editBoxMessage.content, editBoxMessage.inputMode, editBoxMessage.inputFlag, editBoxMessage.returnType, editBoxMessage.maxLength).show();
    }

    public void handleMessage(Message message) {
        switch (message.what) {
            case HANDLER_SHOW_DIALOG /*1*/:
                showDialog(message);
            case HANDLER_SHOW_EDITBOX_DIALOG /*2*/:
                showEditBoxDialog(message);
            default:
        }
    }
}
