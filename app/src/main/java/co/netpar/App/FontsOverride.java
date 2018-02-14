package co.netpar.App;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import java.lang.reflect.Field;

public final class FontsOverride {
    public static void setDefaultFont(Context context, String staticTypefaceFieldName, String fontAssetName) {
        replaceFont(staticTypefaceFieldName, Typeface.createFromAsset(context.getAssets(), fontAssetName));
    }

    private static void replaceFont(String staticTypefaceFieldName, Typeface newTypeface) {
        try {
            Field staticField = Typeface.class.getDeclaredField(staticTypefaceFieldName);
            staticField.setAccessible(true);
            staticField.set(null, newTypeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            Log.e("error in setting font", e.getMessage());
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
            Log.e("error in setting font", e2.getMessage());
        }
    }
}