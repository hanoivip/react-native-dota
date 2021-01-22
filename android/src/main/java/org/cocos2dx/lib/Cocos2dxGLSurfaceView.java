package org.cocos2dx.lib;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import com.loopj.android.http.BuildConfig;
import android.os.Process;
import com.chukong.cocosplay.client.CocosPlayClient;

public class Cocos2dxGLSurfaceView extends GLSurfaceView {
    private static final int HANDLER_CLOSE_IME_KEYBOARD = 3;
    private static final int HANDLER_OPEN_IME_KEYBOARD = 2;
    private static final String TAG;
    private static Cocos2dxGLSurfaceView mCocos2dxGLSurfaceView;
    private static Cocos2dxTextInputWraper sCocos2dxTextInputWraper;
    private static Handler sHandler;
    private Cocos2dxEditText mCocos2dxEditText;
    private Cocos2dxRenderer mCocos2dxRenderer;

    /* renamed from: org.cocos2dx.lib.Cocos2dxGLSurfaceView.10 */
    class AnonymousClass10 implements Runnable {
        final /* synthetic */ int[] val$ids;
        final /* synthetic */ float[] val$xs;
        final /* synthetic */ float[] val$ys;

        AnonymousClass10(int[] iArr, float[] fArr, float[] fArr2) {
            this.val$ids = iArr;
            this.val$xs = fArr;
            this.val$ys = fArr2;
        }

        public void run() {
            Cocos2dxGLSurfaceView.this.mCocos2dxRenderer.handleActionCancel(this.val$ids, this.val$xs, this.val$ys);
        }
    }

    /* renamed from: org.cocos2dx.lib.Cocos2dxGLSurfaceView.11 */
    class AnonymousClass11 implements Runnable {
        final /* synthetic */ int val$pKeyCode;

        AnonymousClass11(int i) {
            this.val$pKeyCode = i;
        }

        public void run() {
            Cocos2dxGLSurfaceView.this.mCocos2dxRenderer.handleKeyDown(this.val$pKeyCode);
        }
    }

    /* renamed from: org.cocos2dx.lib.Cocos2dxGLSurfaceView.12 */
    class AnonymousClass12 implements Runnable {
        final /* synthetic */ String val$pText;

        AnonymousClass12(String str) {
            this.val$pText = str;
        }

        public void run() {
            Cocos2dxGLSurfaceView.this.mCocos2dxRenderer.handleInsertText(this.val$pText);
        }
    }

    /* renamed from: org.cocos2dx.lib.Cocos2dxGLSurfaceView.1 */
    class C24731 extends Handler {
        C24731() {
        }

        public void handleMessage(Message message) {
            switch (message.what) {
                case Cocos2dxGLSurfaceView.HANDLER_OPEN_IME_KEYBOARD /*2*/:
                    if (Cocos2dxGLSurfaceView.this.mCocos2dxEditText != null && Cocos2dxGLSurfaceView.this.mCocos2dxEditText.requestFocus()) {
                        Cocos2dxGLSurfaceView.this.mCocos2dxEditText.removeTextChangedListener(Cocos2dxGLSurfaceView.sCocos2dxTextInputWraper);
                        Cocos2dxGLSurfaceView.this.mCocos2dxEditText.setText(BuildConfig.VERSION_NAME);
                        String str = (String) message.obj;
                        Cocos2dxGLSurfaceView.this.mCocos2dxEditText.append(str);
                        Cocos2dxGLSurfaceView.sCocos2dxTextInputWraper.setOriginText(str);
                        Cocos2dxGLSurfaceView.this.mCocos2dxEditText.addTextChangedListener(Cocos2dxGLSurfaceView.sCocos2dxTextInputWraper);
                        ((InputMethodManager) Cocos2dxGLSurfaceView.mCocos2dxGLSurfaceView.getContext().getSystemService("input_method")).showSoftInput(Cocos2dxGLSurfaceView.this.mCocos2dxEditText, 0);
                        Log.d("GLSurfaceView", "showSoftInput");
                    }
                case Cocos2dxGLSurfaceView.HANDLER_CLOSE_IME_KEYBOARD /*3*/:
                    if (Cocos2dxGLSurfaceView.this.mCocos2dxEditText != null) {
                        Cocos2dxGLSurfaceView.this.mCocos2dxEditText.removeTextChangedListener(Cocos2dxGLSurfaceView.sCocos2dxTextInputWraper);
                        ((InputMethodManager) Cocos2dxGLSurfaceView.mCocos2dxGLSurfaceView.getContext().getSystemService("input_method")).hideSoftInputFromWindow(Cocos2dxGLSurfaceView.this.mCocos2dxEditText.getWindowToken(), 0);
                        Cocos2dxGLSurfaceView.this.requestFocus();
                        Log.d("GLSurfaceView", "HideSoftInput");
                    }
                default:
            }
        }
    }

    /* renamed from: org.cocos2dx.lib.Cocos2dxGLSurfaceView.2 */
    static final class C24742 implements Runnable {
        final /* synthetic */ long val$timestamp;
        final /* synthetic */ float val$x;
        final /* synthetic */ float val$y;
        final /* synthetic */ float val$z;

        C24742(float f, float f2, float f3, long j) {
            this.val$x = f;
            this.val$y = f2;
            this.val$z = f3;
            this.val$timestamp = j;
        }

        public void run() {
            Cocos2dxAccelerometer.onSensorChanged(this.val$x, this.val$y, this.val$z, this.val$timestamp);
        }
    }

    /* renamed from: org.cocos2dx.lib.Cocos2dxGLSurfaceView.3 */
    class C24753 implements Runnable {
        C24753() {
        }

        public void run() {
            Cocos2dxGLSurfaceView.this.mCocos2dxRenderer.handleOnResume();
        }
    }

    /* renamed from: org.cocos2dx.lib.Cocos2dxGLSurfaceView.4 */
    class C24764 implements Runnable {
        C24764() {
        }

        public void run() {
            Cocos2dxGLSurfaceView.this.mCocos2dxRenderer.handleOnPause();
        }
    }

    /* renamed from: org.cocos2dx.lib.Cocos2dxGLSurfaceView.5 */
    class C24775 implements Runnable {
        final /* synthetic */ int val$idPointerDown;
        final /* synthetic */ float val$xPointerDown;
        final /* synthetic */ float val$yPointerDown;

        C24775(int i, float f, float f2) {
            this.val$idPointerDown = i;
            this.val$xPointerDown = f;
            this.val$yPointerDown = f2;
        }

        public void run() {
            Cocos2dxGLSurfaceView.this.mCocos2dxRenderer.handleActionDown(this.val$idPointerDown, this.val$xPointerDown, this.val$yPointerDown);
        }
    }

    /* renamed from: org.cocos2dx.lib.Cocos2dxGLSurfaceView.6 */
    class C24786 implements Runnable {
        final /* synthetic */ int val$idDown;
        final /* synthetic */ float val$xDown;
        final /* synthetic */ float val$yDown;

        C24786(int i, float f, float f2) {
            this.val$idDown = i;
            this.val$xDown = f;
            this.val$yDown = f2;
        }

        public void run() {
            Cocos2dxGLSurfaceView.this.mCocos2dxRenderer.handleActionDown(this.val$idDown, this.val$xDown, this.val$yDown);
        }
    }

    /* renamed from: org.cocos2dx.lib.Cocos2dxGLSurfaceView.7 */
    class C24797 implements Runnable {
        final /* synthetic */ int[] val$ids;
        final /* synthetic */ float[] val$xs;
        final /* synthetic */ float[] val$ys;

        C24797(int[] iArr, float[] fArr, float[] fArr2) {
            this.val$ids = iArr;
            this.val$xs = fArr;
            this.val$ys = fArr2;
        }

        public void run() {
            Cocos2dxGLSurfaceView.this.mCocos2dxRenderer.handleActionMove(this.val$ids, this.val$xs, this.val$ys);
        }
    }

    /* renamed from: org.cocos2dx.lib.Cocos2dxGLSurfaceView.8 */
    class C24808 implements Runnable {
        final /* synthetic */ int val$idPointerUp;
        final /* synthetic */ float val$xPointerUp;
        final /* synthetic */ float val$yPointerUp;

        C24808(int i, float f, float f2) {
            this.val$idPointerUp = i;
            this.val$xPointerUp = f;
            this.val$yPointerUp = f2;
        }

        public void run() {
            Cocos2dxGLSurfaceView.this.mCocos2dxRenderer.handleActionUp(this.val$idPointerUp, this.val$xPointerUp, this.val$yPointerUp);
        }
    }

    /* renamed from: org.cocos2dx.lib.Cocos2dxGLSurfaceView.9 */
    class C24819 implements Runnable {
        final /* synthetic */ int val$idUp;
        final /* synthetic */ float val$xUp;
        final /* synthetic */ float val$yUp;

        C24819(int i, float f, float f2) {
            this.val$idUp = i;
            this.val$xUp = f;
            this.val$yUp = f2;
        }

        public void run() {
            Cocos2dxGLSurfaceView.this.mCocos2dxRenderer.handleActionUp(this.val$idUp, this.val$xUp, this.val$yUp);
        }
    }

    static {
        TAG = Cocos2dxGLSurfaceView.class.getSimpleName();
    }

    public Cocos2dxGLSurfaceView(Context context) {
        super(context);
        initView();
    }

    public Cocos2dxGLSurfaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public static void closeIMEKeyboard() {
        Message message = new Message();
        message.what = HANDLER_CLOSE_IME_KEYBOARD;
        sHandler.sendMessage(message);
    }

    private static void dumpMotionEvent(MotionEvent motionEvent) {
        int i = 0;
        StringBuilder stringBuilder = new StringBuilder();
        int action = motionEvent.getAction();
        int i2 = action;
        stringBuilder.append("event ACTION_").append(new String[]{"DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE", "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?"}[i2]);
        if (i2 == 5 || i2 == 6) {
            stringBuilder.append("(pid ").append(action >> 8);
            stringBuilder.append(")");
        }
        stringBuilder.append("[");
        while (i < motionEvent.getPointerCount()) {
            stringBuilder.append("#").append(i);
            stringBuilder.append("(pid ").append(motionEvent.getPointerId(i));
            stringBuilder.append(")=").append((int) motionEvent.getX(i));
            stringBuilder.append(",").append((int) motionEvent.getY(i));
            if (i + 1 < motionEvent.getPointerCount()) {
                stringBuilder.append(";");
            }
            i++;
        }
        stringBuilder.append("]");
        Log.d(TAG, stringBuilder.toString());
    }

    private String getContentText() {
        return this.mCocos2dxRenderer.getContentText();
    }

    public static Cocos2dxGLSurfaceView getInstance() {
        return mCocos2dxGLSurfaceView;
    }

    public static void openIMEKeyboard() {
        Message message = new Message();
        message.what = HANDLER_OPEN_IME_KEYBOARD;
        message.obj = mCocos2dxGLSurfaceView.getContentText();
        sHandler.sendMessage(message);
    }

    public static void queueAccelerometer(float f, float f2, float f3, long j) {
        mCocos2dxGLSurfaceView.queueEvent(new C24742(f, f2, f3, j));
    }

    public void deleteBackward() {
        queueEvent(new Runnable() {
            public void run() {
                Cocos2dxGLSurfaceView.this.mCocos2dxRenderer.handleDeleteBackward();
            }
        });
    }

    public Cocos2dxEditText getCocos2dxEditText() {
        return this.mCocos2dxEditText;
    }

    protected void initView() {
        setEGLContextClientVersion(HANDLER_OPEN_IME_KEYBOARD);
        setFocusableInTouchMode(true);
        mCocos2dxGLSurfaceView = this;
        sCocos2dxTextInputWraper = new Cocos2dxTextInputWraper(this);
        sHandler = new C24731();
    }

    public void insertText(String str) {
        queueEvent(new AnonymousClass12(str));
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        switch (i) {
            case KeyEvent.KEYCODE_BACK:
                //Process.killProcess(Process.myPid());
                if (CocosPlayClient.m_activity != null)
                    CocosPlayClient.m_activity.finish();
            case 82:
                queueEvent(new AnonymousClass11(i));
                return true;
            default:
                return super.onKeyDown(i, keyEvent);
        }
    }

    public void onPause() {
        queueEvent(new C24764());
        super.onPause();
    }

    public void onResume() {
        super.onResume();
        queueEvent(new C24753());
    }

    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        if (!isInEditMode()) {
            this.mCocos2dxRenderer.setScreenWidthAndHeight(i, i2);
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        int i;
        int pointerCount = motionEvent.getPointerCount();
        int[] iArr = new int[pointerCount];
        float[] fArr = new float[pointerCount];
        float[] fArr2 = new float[pointerCount];
        for (i = 0; i < pointerCount; i++) {
            iArr[i] = motionEvent.getPointerId(i);
            fArr[i] = motionEvent.getX(i);
            fArr2[i] = motionEvent.getY(i);
        }
        switch (motionEvent.getAction()) {
            case 0:
                queueEvent(new C24786(motionEvent.getPointerId(0), fArr[0], fArr2[0]));
                break;
            case 1:
                queueEvent(new C24819(motionEvent.getPointerId(0), fArr[0], fArr2[0]));
                break;
            case 2:
                queueEvent(new C24797(iArr, fArr, fArr2));
                break;
            case 3:
                queueEvent(new AnonymousClass10(iArr, fArr, fArr2));
                break;
            case 5:
                i = motionEvent.getAction() >> 8;
                queueEvent(new C24775(motionEvent.getPointerId(i), motionEvent.getX(i), motionEvent.getY(i)));
                break;
            case 6:
                i = motionEvent.getAction() >> 8;
                queueEvent(new C24808(motionEvent.getPointerId(i), motionEvent.getX(i), motionEvent.getY(i)));
                break;
        }
        return true;
    }

    public void setCocos2dxEditText(Cocos2dxEditText cocos2dxEditText) {
        this.mCocos2dxEditText = cocos2dxEditText;
        if (this.mCocos2dxEditText != null && sCocos2dxTextInputWraper != null) {
            this.mCocos2dxEditText.setOnEditorActionListener(sCocos2dxTextInputWraper);
            this.mCocos2dxEditText.setCocos2dxGLSurfaceView(this);
            requestFocus();
        }
    }

    public void setCocos2dxRenderer(Cocos2dxRenderer cocos2dxRenderer) {
        this.mCocos2dxRenderer = cocos2dxRenderer;
        setRenderer(this.mCocos2dxRenderer);
    }
}
