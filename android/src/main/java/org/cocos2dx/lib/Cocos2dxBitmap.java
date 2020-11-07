package org.cocos2dx.lib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.FloatMath;
import android.util.Log;
import com.loopj.android.http.BuildConfig;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.LinkedList;

public class Cocos2dxBitmap {
    private static final int HORIZONTALALIGN_CENTER = 3;
    private static final int HORIZONTALALIGN_LEFT = 1;
    private static final int HORIZONTALALIGN_RIGHT = 2;
    private static final int VERTICALALIGN_BOTTOM = 2;
    private static final int VERTICALALIGN_CENTER = 3;
    private static final int VERTICALALIGN_TOP = 1;
    private static int lineHeightFix;
    private static Context sContext;
    private static boolean useWinBoard;

    private static class TextProperty {
        private final int mHeightPerLine;
        private final String[] mLines;
        private final int mMaxWidth;
        private final int mTotalHeight;

        TextProperty(int i, int i2, String[] strArr) {
            this.mMaxWidth = i;
            this.mHeightPerLine = i2;
            this.mTotalHeight = strArr.length * i2;
            this.mLines = strArr;
        }
    }

    static {
        lineHeightFix = 0;
        useWinBoard = false;
    }

    private static TextProperty computeTextProperty(String str, int i, int i2, Paint paint) {
        int heightPerLine = getHeightPerLine(paint.getFontMetricsInt());
        String[] splitString = splitString(str, i, i2, paint);
        if (i == 0) {
            int length = splitString.length;
            i = VERTICALALIGN_TOP;
            for (int i3 = 0; i3 < length; i3 += VERTICALALIGN_TOP) {
                String str2 = splitString[i3];
                int ceil = (int) Math.ceil(paint.measureText(str2, 0, str2.length()));
                if (ceil > i) {
                    i = ceil;
                }
            }
        }
        return new TextProperty(i, heightPerLine, splitString);
    }

    private static int computeX(String str, int i, int i2) {
        switch (i2) {
            case VERTICALALIGN_BOTTOM /*2*/:
                return i;
            case VERTICALALIGN_CENTER /*3*/:
                return i / VERTICALALIGN_BOTTOM;
            default:
                return 0;
        }
    }

    private static int computeY(FontMetricsInt fontMetricsInt, int i, int i2, int i3) {
        int i4 = -((int) getFontTop(fontMetricsInt));
        if (i <= i2) {
            return i4;
        }
        switch (i3) {
            case VERTICALALIGN_TOP /*1*/:
                return -((int) getFontTop(fontMetricsInt));
            case VERTICALALIGN_BOTTOM /*2*/:
                return (-((int) getFontTop(fontMetricsInt))) + (i - i2);
            case VERTICALALIGN_CENTER /*3*/:
                return (-((int) getFontTop(fontMetricsInt))) + ((i - i2) / VERTICALALIGN_BOTTOM);
            default:
                return i4;
        }
    }

    public static void createTextBitmap(byte[] bArr, String str, int i, int i2, int i3, int i4, int i5) {
        int i6 = i2 & 15;
        String str2 = (bArr == null || bArr.length == 0) ? BuildConfig.VERSION_NAME : new String(bArr);
        str2 = refactorString(str2);
        Paint newPaint = newPaint(str, i, i6);
        newPaint.setColor(i5);
        TextProperty computeTextProperty = computeTextProperty(str2, i3, i4, newPaint);
        Bitmap createBitmap = Bitmap.createBitmap(computeTextProperty.mMaxWidth, i4 == 0 ? computeTextProperty.mTotalHeight : i4, Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        int computeY = computeY(newPaint.getFontMetricsInt(), i4, computeTextProperty.mTotalHeight, (i2 >> 4) & 15);
        String[] access$200 = computeTextProperty.mLines;
        int length = access$200.length;
        for (int i7 = 0; i7 < length; i7 += VERTICALALIGN_TOP) {
            String str3 = access$200[i7];
            canvas.drawText(str3, (float) computeX(str3, computeTextProperty.mMaxWidth, i6), (float) computeY, newPaint);
            computeY += computeTextProperty.mHeightPerLine;
        }
        initNativeObject(createBitmap);
    }

    private static LinkedList<String> divideStringWithMaxWidth(String str, int i, Paint paint) {
        int length = str.length();
        LinkedList<String> linkedList = new LinkedList();
        int i2 = VERTICALALIGN_TOP;
        int i3 = 0;
        while (i2 <= length) {
            int ceil = (int) Math.ceil(paint.measureText(str, i3, i2));
            if (ceil >= i) {
                int lastIndexOf = str.substring(0, i2).lastIndexOf(" ");
                if (lastIndexOf != -1 && lastIndexOf > i3) {
                    linkedList.add(str.substring(i3, lastIndexOf + VERTICALALIGN_TOP));
                    i2 = lastIndexOf + VERTICALALIGN_TOP;
                } else if (ceil <= i || i2 == i3 + VERTICALALIGN_TOP) {
                    linkedList.add(str.substring(i3, i2));
                } else {
                    linkedList.add(str.substring(i3, i2 - 1));
                    i2--;
                }
                while (str.indexOf(i2) == 32) {
                    i2 += VERTICALALIGN_TOP;
                }
                i3 = i2;
            }
            i2 += VERTICALALIGN_TOP;
        }
        if (i3 < length) {
            linkedList.add(str.substring(i3));
        }
        return linkedList;
    }

    private static double getFontBottom(FontMetricsInt fontMetricsInt) {
        return useWinBoard ? (double) fontMetricsInt.descent : (double) fontMetricsInt.bottom;
    }

    private static int getFontSizeAccordingHeight(int i) {
        Paint paint = new Paint();
        Rect rect = new Rect();
        paint.setTypeface(Typeface.DEFAULT);
        int i2 = VERTICALALIGN_TOP;
        int i3 = 0;
        while (i3 == 0) {
            paint.setTextSize((float) i2);
            paint.getTextBounds("SghMNy", 0, "SghMNy".length(), rect);
            i2 += VERTICALALIGN_TOP;
            if (i - rect.height() <= VERTICALALIGN_BOTTOM) {
                i3 = VERTICALALIGN_TOP;
            }
            Log.d("font size", "incr size:" + i2);
        }
        return i2;
    }

    private static double getFontTop(FontMetricsInt fontMetricsInt) {
        return useWinBoard ? (double) fontMetricsInt.ascent : (double) fontMetricsInt.top;
    }

    private static int getHeightPerLine(FontMetricsInt fontMetricsInt) {
        return ((int) Math.ceil(getFontBottom(fontMetricsInt) - getFontTop(fontMetricsInt))) + lineHeightFix;
    }

    private static byte[] getPixels(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        byte[] bArr = new byte[((bitmap.getWidth() * bitmap.getHeight()) * 4)];
        Buffer wrap = ByteBuffer.wrap(bArr);
        //wrap.order(ByteOrder.nativeOrder());
        bitmap.copyPixelsToBuffer(wrap);
        return bArr;
    }

    private static String getStringWithEllipsis(String str, float f, float f2) {
        if (TextUtils.isEmpty(str)) {
            return BuildConfig.VERSION_NAME;
        }
        TextPaint textPaint = new TextPaint();
        textPaint.setTypeface(Typeface.DEFAULT);
        textPaint.setTextSize(f2);
        return TextUtils.ellipsize(str, textPaint, f, TruncateAt.END).toString();
    }

    private static void initNativeObject(Bitmap bitmap) {
        byte[] pixels = getPixels(bitmap);
        if (pixels != null) {
            nativeInitBitmapDC(bitmap.getWidth(), bitmap.getHeight(), pixels);
        }
    }

    private static native void nativeInitBitmapDC(int i, int i2, byte[] bArr);

    private static Paint newPaint(String str, int i, int i2) {
        Paint paint = new Paint();
        paint.setColor(-1);
        paint.setTextSize((float) i);
        paint.setAntiAlias(true);
        if (str.endsWith(".ttf")) {
            try {
                paint.setTypeface(Cocos2dxTypefaces.get(sContext, str));
            } catch (Exception e) {
                Log.e("Cocos2dxBitmap", "error to create ttf type face: " + str);
                paint.setTypeface(Typeface.create(str, 0));
            }
        } else {
            paint.setTypeface(Typeface.create(str, 0));
        }
        switch (i2) {
            case VERTICALALIGN_BOTTOM /*2*/:
                paint.setTextAlign(Align.RIGHT);
                break;
            case VERTICALALIGN_CENTER /*3*/:
                paint.setTextAlign(Align.CENTER);
                break;
            default:
                paint.setTextAlign(Align.LEFT);
                break;
        }
        return paint;
    }

    private static java.lang.String refactorString(java.lang.String string2) {
    	if (string2.compareTo("") == 0) {
			return " ";
		}
		StringBuilder stringBuilder = new StringBuilder(string2);
		int n2 = 0;
		int n3 = stringBuilder.indexOf("\n");
		while (n3 != -1) {
			if (n3 == 0 || stringBuilder.charAt(n3 - 1) == '\n') {
				stringBuilder.insert(n2, " ");
				n2 = n3 + 2;
			} else {
				n2 = n3 + 1;
			}
			if (n2 > stringBuilder.length() || n3 == stringBuilder.length())
				break;
			n3 = stringBuilder.indexOf("\n", n2);
		}
		return stringBuilder.toString();
    }

    public static void setContext(Context context) {
        sContext = context;
    }

    public static void setLineHeightFix(int i) {
        lineHeightFix = i;
    }

    public static void setWinBoardFlag(boolean z) {
        useWinBoard = z;
    }

    private static String[] splitString(String str, int i, int i2, Paint paint) {
        int i3 = 0;
        String[] split = str.split("\\n");
        int heightPerLine = i2 / getHeightPerLine(paint.getFontMetricsInt());
        LinkedList linkedList;
        if (i != 0) {
            linkedList = new LinkedList();
            int length = split.length;
            while (i3 < length) {
                String str2 = split[i3];
                if (((int) Math.ceil(paint.measureText(str2))) > i) {
                    linkedList.addAll(divideStringWithMaxWidth(str2, i, paint));
                } else {
                    linkedList.add(str2);
                }
                if (heightPerLine > 0 && linkedList.size() >= heightPerLine) {
                    break;
                }
                i3 += VERTICALALIGN_TOP;
            }
            if (heightPerLine > 0 && linkedList.size() > heightPerLine) {
                while (linkedList.size() > heightPerLine) {
                    linkedList.removeLast();
                }
            }
            split = new String[linkedList.size()];
            linkedList.toArray(split);
            return split;
        } else if (i2 == 0 || split.length <= heightPerLine) {
            return split;
        } else {
            linkedList = new LinkedList();
            while (i3 < heightPerLine) {
                linkedList.add(split[i3]);
                i3 += VERTICALALIGN_TOP;
            }
            split = new String[linkedList.size()];
            linkedList.toArray(split);
            return split;
        }
    }
}
