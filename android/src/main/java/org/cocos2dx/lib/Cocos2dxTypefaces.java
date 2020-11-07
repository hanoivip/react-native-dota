package org.cocos2dx.lib;

import android.content.Context;
import android.graphics.Typeface;
import java.util.HashMap;

public class Cocos2dxTypefaces {
    private static final HashMap<String, Typeface> sTypefaceCache;

    static {
        sTypefaceCache = new HashMap();
    }

    public static Typeface get(Context context, String str) {
        Typeface createFromAsset;
        synchronized (Cocos2dxTypefaces.class) {
            try {
            } finally {
                Class r1 = Cocos2dxTypefaces.class;
            }
        }
        if (!sTypefaceCache.containsKey(str)) {
            createFromAsset = Typeface.createFromAsset(context.getAssets(), str);
            sTypefaceCache.put(str, createFromAsset);
        }
        createFromAsset = (Typeface) sTypefaceCache.get(str);
        return createFromAsset;
    }
}
