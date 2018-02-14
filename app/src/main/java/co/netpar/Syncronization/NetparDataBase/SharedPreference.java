package co.netpar.Syncronization.NetparDataBase;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import co.netpar.Comman.Constants;

public class SharedPreference {
    private static Editor editor;
    private static SharedPreferences sharedPreference;

    public static void storeSessionCount(Context context, long value) {
        sharedPreference = context.getSharedPreferences(Constants.KEY, 0);
        editor = sharedPreference.edit();
        editor.putLong(Constants.SESSION_IN_SECOND, value);
        editor.commit();
    }

    public static long retrieveSessionCount(Context context) {
        sharedPreference = context.getSharedPreferences(Constants.KEY, 0);
        return sharedPreference.getLong(Constants.SESSION_IN_SECOND, 0);
    }

    public static void storeLang(Context context, int value) {
        sharedPreference = context.getSharedPreferences(Constants.LANGUAGE_KEY, 0);
        editor = sharedPreference.edit();
        editor.putInt(Constants.LANGUAGE_KEY, value);
        editor.commit();
    }

    public static int retrieveLang(Context context) {
        sharedPreference = context.getSharedPreferences(Constants.LANGUAGE_KEY, 0);
        return sharedPreference.getInt(Constants.LANGUAGE_KEY, 0);
    }

    public static void removeLang(Context context) {
        sharedPreference = context.getSharedPreferences(Constants.LANGUAGE_KEY, 0);
        editor = sharedPreference.edit();
        editor.remove(Constants.LANGUAGE_KEY);
        editor.commit();
    }

    public static void storeData(Context context, String key, String value) {
        sharedPreference = context.getSharedPreferences(Constants.KEY, 0);
        editor = sharedPreference.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String retrieveData(Context context, String Key) {
        sharedPreference = context.getSharedPreferences(Constants.KEY, 0);
        return sharedPreference.getString(Key, null);
    }

    public static void removeAll(Context context) {
        removeLang(context);
        sharedPreference = context.getSharedPreferences(Constants.KEY, 0);
        editor = sharedPreference.edit();
        editor.clear();
        editor.commit();
    }

    public static void removeKey(Context context, String Key) {
        sharedPreference = context.getSharedPreferences(Constants.KEY, 0);
        editor = sharedPreference.edit();
        editor.remove(Key);
        editor.commit();
    }

    public static void storeFcmDeviceId(Context context, String val) {
        sharedPreference = context.getSharedPreferences("FCM", 0);
        editor = sharedPreference.edit();
        editor.putString("DEVICE", val);
        editor.commit();
    }

    public static String retrieveFcmDeviceId(Context context) {
        sharedPreference = context.getSharedPreferences("FCM", 0);
        editor = sharedPreference.edit();
        return sharedPreference.getString("DEVICE", null);
    }
}
