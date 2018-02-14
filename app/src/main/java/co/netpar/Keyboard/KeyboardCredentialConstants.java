package co.netpar.Keyboard;

import android.app.Activity;
import android.content.Context;
import co.netpar.Comman.Alert;
import co.netpar.Syncronization.NetparDataBase.SharedPreference;
import com.reverie.manager.LangResourceInitCompleteListener;
import com.reverie.manager.RevSDK;
import com.reverie.manager.RevStatus;
import java.util.ArrayList;

public class KeyboardCredentialConstants {
    public static final String LM_BASE_API_URL = "https://api-gw.revup.reverieinc.com/apiman-gateway/ReverieMobilitySdk";
    public static final String RESOURCE_DOWNLOAD_BASE_API_URL = "https://api-gw.revup.reverieinc.com/apiman-gateway/ReverieMobilitySdk";
    public static final String SDK_TEST_API_KEY = "6f25844e4377359a1a713571198ec67a";
    public static final String SDK_TEST_APP_ID = "co.netpar";
    public static final int Server_Lang_Assamese = 9;
    public static final int Server_Lang_Bengali = 8;
    public static final int Server_Lang_Bodo = 12;
    public static final int Server_Lang_Dogri = 13;
    public static final int Server_Lang_English = 20;
    public static final int Server_Lang_Gujarati = 1;
    public static final int Server_Lang_Hindi = 0;
    public static final int Server_Lang_Kannada = 6;
    public static final int Server_Lang_Kashmiri = 15;
    public static final int Server_Lang_Konkani = 14;
    public static final int Server_Lang_Maithili = 16;
    public static final int Server_Lang_Malayalam = 3;
    public static final int Server_Lang_Manipuri = 17;
    public static final int Server_Lang_Marathi = 10;
    public static final int Server_Lang_Nepali = 11;
    public static final int Server_Lang_Odia = 7;
    public static final int Server_Lang_Punjabi = 2;
    public static final int Server_Lang_Sanskrit = 18;
    public static final int Server_Lang_Sindhi = 19;
    public static final int Server_Lang_Tamil = 4;
    public static final int Server_Lang_Telugu = 5;
    public static final int[] langIdsArray = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 20};
    public static final String[] langNamesArray = new String[]{"Hindi", "Gujrati", "Punjabi", "Malayalam", "Tamil", "Telugu", "Kannad", "Odia", "Bengali", "Assamese", "Marathi", "English"};

    public static ArrayList<Lang> getLanguageList() {
        ArrayList<Lang> languages = new ArrayList();
        for (int i = 0; i <= 20; i++) {
            languages.add(new Lang(i, langNamesArray[i]));
        }
        return languages;
    }

    public static void inItKeyboard(final Context context, final String clss) {
        final int selectedLangId = SharedPreference.retrieveLang(context);
        Alert.showLog("TAG", "selectedLangId = " + selectedLangId);
        RevSDK.initLangResources("https://api-gw.revup.reverieinc.com/apiman-gateway/ReverieMobilitySdk", SharedPreference.retrieveLang(context), new LangResourceInitCompleteListener() {
            public void onLangResourceInitComplete(int i, RevStatus revStatus) {
                Alert.showLog("TAG", "INIT RESOURCE COMPLETE:  Lang code = " + i + " , Status = " + revStatus.getStatusMessage());
                if (revStatus.getStatusCode() == RevStatus.SUCCESS) {
                    boolean status = RevSDK.initKeypad((Activity) context, selectedLangId);
                    return;
                }
                Alert.showToast(context, revStatus.getStatusMessage());
            }
        });
    }
}
