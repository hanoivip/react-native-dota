package sh.lilith.dgame;

import android.content.Context;
import android.os.Process;
import android.view.KeyEvent;
import org.cocos2dx.lib.Cocos2dxGLSurfaceView;

class LuaGLSurfaceView extends Cocos2dxGLSurfaceView {
    public LuaGLSurfaceView(Context context) {
        super(context);
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            Process.killProcess(Process.myPid());
        }
        return super.onKeyDown(i, keyEvent);
    }
}
