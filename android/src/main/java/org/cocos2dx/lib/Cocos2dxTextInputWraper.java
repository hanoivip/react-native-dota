package org.cocos2dx.lib;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class Cocos2dxTextInputWraper implements TextWatcher, OnEditorActionListener {
    private static final String TAG;
    private final Cocos2dxGLSurfaceView mCocos2dxGLSurfaceView;
    private String mOriginText;
    private String mText;

    static {
        TAG = Cocos2dxTextInputWraper.class.getSimpleName();
    }

    public Cocos2dxTextInputWraper(Cocos2dxGLSurfaceView cocos2dxGLSurfaceView) {
        this.mCocos2dxGLSurfaceView = cocos2dxGLSurfaceView;
    }

    private boolean isFullScreenEdit() {
        return ((InputMethodManager) this.mCocos2dxGLSurfaceView.getCocos2dxEditText().getContext().getSystemService("input_method")).isFullscreenMode();
    }

    public void afterTextChanged(Editable editable) {
        if (!isFullScreenEdit()) {
            int length = editable.length() - this.mText.length();
            if (length > 0) {
                this.mCocos2dxGLSurfaceView.insertText(editable.subSequence(this.mText.length(), editable.length()).toString());
            } else {
                while (length < 0) {
                    this.mCocos2dxGLSurfaceView.deleteBackward();
                    length++;
                }
            }
            this.mText = editable.toString();
        }
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        this.mText = charSequence.toString();
    }

    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (this.mCocos2dxGLSurfaceView.getCocos2dxEditText() == textView && isFullScreenEdit() && i != 0) {
            try {
                for (int length = this.mOriginText.length(); length > 0; length--) {
                    this.mCocos2dxGLSurfaceView.deleteBackward();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.mCocos2dxGLSurfaceView.insertText(textView.getText().toString());
        }
        if (i == 6) {
            this.mCocos2dxGLSurfaceView.requestFocus();
        }
        return false;
    }

    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public void setOriginText(String str) {
        this.mOriginText = str;
    }
}
