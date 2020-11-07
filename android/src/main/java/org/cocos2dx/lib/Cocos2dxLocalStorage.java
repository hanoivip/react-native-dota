package org.cocos2dx.lib;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.loopj.android.http.BuildConfig;

public class Cocos2dxLocalStorage {
    private static String DATABASE_NAME = null;
    private static final int DATABASE_VERSION = 1;
    private static String TABLE_NAME = null;
    private static final String TAG = "Cocos2dxLocalStorage";
    private static SQLiteDatabase mDatabase;
    private static DBOpenHelper mDatabaseOpenHelper;

    private static class DBOpenHelper extends SQLiteOpenHelper {
        DBOpenHelper(Context context) {
            super(context, Cocos2dxLocalStorage.DATABASE_NAME, null, Cocos2dxLocalStorage.DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + Cocos2dxLocalStorage.TABLE_NAME + "(key TEXT PRIMARY KEY,value TEXT);");
        }

        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
            Log.w(Cocos2dxLocalStorage.TAG, "Upgrading database from version " + i + " to " + i2 + ", which will destroy all old data");
        }
    }

    static {
        DATABASE_NAME = "jsb.sqlite";
        TABLE_NAME = "data";
        mDatabaseOpenHelper = null;
        mDatabase = null;
    }

    public static void destory() {
        if (mDatabase != null) {
            mDatabase.close();
        }
    }

    public static String getItem(String str) {
        Exception exception;
        Exception exception2;
        String str2 = null;
        String str3;
        try {
            str3 = "select value from " + TABLE_NAME + " where key=?";
            SQLiteDatabase sQLiteDatabase = mDatabase;
            String[] strArr = new String[DATABASE_VERSION];
            strArr[0] = str;
            Cursor rawQuery = sQLiteDatabase.rawQuery(str3, strArr);
            while (rawQuery.moveToNext()) {
                try {
                    if (str2 != null) {
                        Log.e(TAG, "The key contains more than one value.");
                        break;
                    }
                    str2 = rawQuery.getString(rawQuery.getColumnIndex("value"));
                } catch (Exception e) {
                    exception = e;
                    str3 = str2;
                    exception2 = exception;
                }
            }
            rawQuery.close();
        } catch (Exception e2) {
            exception = e2;
            str3 = null;
            exception2 = exception;
            exception2.printStackTrace();
            str2 = str3;   
        }
        return str2 != null ? str2 : BuildConfig.VERSION_NAME;
    }

    public static boolean init(String str, String str2) {
        if (Cocos2dxActivity.getContext() == null) {
            return false;
        }
        DATABASE_NAME = str;
        TABLE_NAME = str2;
        mDatabaseOpenHelper = new DBOpenHelper(Cocos2dxActivity.getContext());
        mDatabase = mDatabaseOpenHelper.getWritableDatabase();
        return true;
    }

    public static void removeItem(String str) {
        try {
            String str2 = "delete from " + TABLE_NAME + " where key=?";
            SQLiteDatabase sQLiteDatabase = mDatabase;
            Object[] objArr = new Object[DATABASE_VERSION];
            objArr[0] = str;
            sQLiteDatabase.execSQL(str2, objArr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setItem(String str, String str2) {
        try {
            String str3 = "replace into " + TABLE_NAME + "(key,value)values(?,?)";
            mDatabase.execSQL(str3, new Object[]{str, str2});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
