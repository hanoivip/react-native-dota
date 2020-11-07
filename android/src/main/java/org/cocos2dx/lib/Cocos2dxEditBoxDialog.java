package org.cocos2dx.lib;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.loopj.android.http.BuildConfig;


public class Cocos2dxEditBoxDialog extends Dialog {
    private final int kEditBoxInputFlagInitialCapsAllCharacters;
    private final int kEditBoxInputFlagInitialCapsSentence;
    private final int kEditBoxInputFlagInitialCapsWord;
    private final int kEditBoxInputFlagPassword;
    private final int kEditBoxInputFlagSensitive;
    private final int kEditBoxInputModeAny;
    private final int kEditBoxInputModeDecimal;
    private final int kEditBoxInputModeEmailAddr;
    private final int kEditBoxInputModeNumeric;
    private final int kEditBoxInputModePhoneNumber;
    private final int kEditBoxInputModeSingleLine;
    private final int kEditBoxInputModeUrl;
    private final int kKeyboardReturnTypeDefault;
    private final int kKeyboardReturnTypeDone;
    private final int kKeyboardReturnTypeGo;
    private final int kKeyboardReturnTypeSearch;
    private final int kKeyboardReturnTypeSend;
    private EditText mInputEditText;
    private final int mInputFlag;
    private int mInputFlagConstraints;
    private final int mInputMode;
    private int mInputModeContraints;
    private boolean mIsMultiline;
    private final int mMaxLength;
    private final String mMessage;
    private final int mReturnType;
    private TextView mTextViewTitle;
    private final String mTitle;

    /* renamed from: org.cocos2dx.lib.Cocos2dxEditBoxDialog.1 */
    class C24711 implements Runnable {
        C24711() {
        }

        public void run() {
            Cocos2dxEditBoxDialog.this.mInputEditText.requestFocus();
            Cocos2dxEditBoxDialog.this.mInputEditText.setSelection(Cocos2dxEditBoxDialog.this.mInputEditText.length());
            Cocos2dxEditBoxDialog.this.openKeyboard();
        }
    }

    /* renamed from: org.cocos2dx.lib.Cocos2dxEditBoxDialog.2 */
    class C24722 implements OnEditorActionListener {
        C24722() {
        }

        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == 0 && (i != 0 || keyEvent == null || keyEvent.getAction() != 0)) {
                return false;
            }
            Cocos2dxHelper.setEditTextDialogResult(Cocos2dxEditBoxDialog.this.mInputEditText.getText().toString());
            Cocos2dxEditBoxDialog.this.closeKeyboard();
            Cocos2dxEditBoxDialog.this.dismiss();
            return true;
        }
    }

    public Cocos2dxEditBoxDialog(Context context, String str, String str2, int i, int i2, int i3, int i4) {
        super(context, 16973841);
        this.kEditBoxInputModeAny = 0;
        this.kEditBoxInputModeEmailAddr = 1;
        this.kEditBoxInputModeNumeric = 2;
        this.kEditBoxInputModePhoneNumber = 3;
        this.kEditBoxInputModeUrl = 4;
        this.kEditBoxInputModeDecimal = 5;
        this.kEditBoxInputModeSingleLine = 6;
        this.kEditBoxInputFlagPassword = 0;
        this.kEditBoxInputFlagSensitive = 1;
        this.kEditBoxInputFlagInitialCapsWord = 2;
        this.kEditBoxInputFlagInitialCapsSentence = 3;
        this.kEditBoxInputFlagInitialCapsAllCharacters = 4;
        this.kKeyboardReturnTypeDefault = 0;
        this.kKeyboardReturnTypeDone = 1;
        this.kKeyboardReturnTypeSend = 2;
        this.kKeyboardReturnTypeSearch = 3;
        this.kKeyboardReturnTypeGo = 4;
        this.mTitle = str;
        this.mMessage = str2;
        this.mInputMode = i;
        this.mInputFlag = i2;
        this.mReturnType = i3;
        this.mMaxLength = i4;
    }

    private void closeKeyboard() {
        ((InputMethodManager) getContext().getSystemService("input_method")).hideSoftInputFromWindow(this.mInputEditText.getWindowToken(), 0);
    }

    private int convertDipsToPixels(float f) {
        return Math.round(getContext().getResources().getDisplayMetrics().density * f);
    }

    private void openKeyboard() {
        ((InputMethodManager) getContext().getSystemService("input_method")).showSoftInput(this.mInputEditText, 0);
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(1);
        LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
        this.mTextViewTitle = new TextView(getContext());
        LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -2);
        int convertDipsToPixels = convertDipsToPixels(10.0f);
        //layoutParams2.rightMargin = convertDipsToPixels;
        //layoutParams2.leftMargin = convertDipsToPixels;
        this.mTextViewTitle.setTextSize(1, 20.0f);
        linearLayout.addView(this.mTextViewTitle, layoutParams2);
        this.mInputEditText = new EditText(getContext());
        layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
        convertDipsToPixels = convertDipsToPixels(10.0f);
        //layoutParams2.rightMargin = convertDipsToPixels;
        //layoutParams2.leftMargin = convertDipsToPixels;
        linearLayout.addView(this.mInputEditText, layoutParams2);
        setContentView(linearLayout, layoutParams);
        //getWindow().addFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
        try {
            this.mTextViewTitle.setText(this.mTitle);
            this.mInputEditText.setText(this.mMessage);
        } catch (Exception e) {
            Log.e("catch_log", BuildConfig.VERSION_NAME + e);
            this.mTextViewTitle.setText(BuildConfig.VERSION_NAME);
            this.mInputEditText.setText(BuildConfig.VERSION_NAME);
        }
        this.mInputEditText.setImeOptions(this.mInputEditText.getImeOptions() | 268435456);
        int imeOptions = this.mInputEditText.getImeOptions();
        switch (this.mInputMode) {
            case 0:
                this.mInputModeContraints = 131073;
                break;
            case 1:
                this.mInputModeContraints = 33;
                break;
            case 2:
                this.mInputModeContraints = 4098;
                break;
            case 3:
                this.mInputModeContraints = 3;
                break;
            case 4:
                this.mInputModeContraints = 17;
                break;
            case 5:
                this.mInputModeContraints = 12290;
                break;
            case 6:
                this.mInputModeContraints = 1;
                break;
        }
        //if (this.mIsMultiline) {
        //    this.mInputModeContraints |= AccessibilityNodeInfoCompat.ACTION_SET_SELECTION;
        //}
        this.mInputEditText.setInputType(this.mInputModeContraints | this.mInputFlagConstraints);
        switch (this.mInputFlag) {
            case 0:
                this.mInputFlagConstraints = 129;
                break;
            case 1:
                this.mInputFlagConstraints = 0; //AccessibilityEventCompat.TYPE_GESTURE_DETECTION_END;
                break;
            case 2:
                this.mInputFlagConstraints = 0;// AsyncHttpClient.DEFAULT_SOCKET_BUFFER_SIZE;
                break;
            case 3:
                this.mInputFlagConstraints = 0;//AccessibilityEventCompat.TYPE_ANNOUNCEMENT;
                break;
            case 4:
                this.mInputFlagConstraints = 0;//AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD;
                break;
        }
        this.mInputEditText.setInputType(this.mInputFlagConstraints | this.mInputModeContraints);
        switch (this.mReturnType) {
            case 0:
                this.mInputEditText.setImeOptions(imeOptions | 1);
                break;
            case 1:
                this.mInputEditText.setImeOptions(imeOptions | 6);
                break;
            case 2:
                this.mInputEditText.setImeOptions(imeOptions | 4);
                break;
            case 3:
                this.mInputEditText.setImeOptions(imeOptions | 3);
                break;
            case 4:
                this.mInputEditText.setImeOptions(imeOptions | 2);
                break;
            default:
                this.mInputEditText.setImeOptions(imeOptions | 1);
                break;
        }
        if (this.mMaxLength > 0) {
            this.mInputEditText.setFilters(new InputFilter[]{new LengthFilter(this.mMaxLength)});
        }
        new Handler().postDelayed(new C24711(), 200);
        this.mInputEditText.setOnEditorActionListener(new C24722());
    }
}
