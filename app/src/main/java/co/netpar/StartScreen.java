package co.netpar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.reverie.manager.LangResourceInitCompleteListener;
import com.reverie.manager.RevSDK;
import com.reverie.manager.RevStatus;
import com.reverie.manager.ValidationCompleteListener;

import co.netpar.Comman.Alert;
import co.netpar.Keyboard.KeyboardCredentialConstants;
import co.netpar.Network.ConnectionDetector;
import co.netpar.Syncronization.NetparDataBase.SharedPreference;

public class StartScreen extends AppCompatActivity implements OnClickListener
{
    private TextView start;

    public static final int PERMISSION = 1;
    public static final String[] PERMISSIONS = {android.Manifest.permission.RECEIVE_SMS, android.Manifest.permission.READ_SMS, android.Manifest.permission.SEND_SMS, android.Manifest.permission.INTERNET,
            android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.READ_CONTACTS, android.Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};


    public int selectedLangId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_start_screen);
        getWindow().setBackgroundDrawableResource(R.drawable.louncher_screen);
        selectedLangId = SharedPreference.retrieveLang(StartScreen.this);
        this.start = (TextView) findViewById(R.id.start);

        if (!hasPermissions(PERMISSIONS))
        {
            ActivityCompat.requestPermissions(this, PERMISSIONS,PERMISSION);
        }

        this.start.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start:
                validateLicense();
                return;
            default:
                return;
        }
    }

    private void startMainScreen()
    {
        finish();
        startActivity(new Intent(this, Splash.class));
    }


    /* Use to validate Reverie Licence */
    public void validateLicense()
    {
        if(ConnectionDetector.isInternetAvailable(StartScreen.this))
        {

            RevSDK.validateKey(getApplicationContext(), "https://api-gw.revup.reverieinc.com/apiman-gateway/ReverieMobilitySdk", KeyboardCredentialConstants.SDK_TEST_API_KEY, "co.netpar", new ValidationCompleteListener() {
                @Override
                public void onValidationComplete(int statusCode, String statusMessage) {
                    Alert.showLog("Language", "LICENSE VALIDATION COMPLETE : " + statusCode + " ," + statusMessage);
                    if (statusCode != 1)
                    {
                        Alert.showToast(StartScreen.this,getString(R.string.reveri_not_initial));
                        startMainScreen();
                    }
                    else if(statusCode == 3)
                    {
                        Alert.showToast(StartScreen.this,getString(R.string.reveri_not_initial));
                        startMainScreen();
                    }
                    else if(statusCode == 10)
                    {
                        Alert.showToast(StartScreen.this,getString(R.string.reveri_not_initial));
                        startMainScreen();
                    }
                    else
                    {
                        new AsyncCaller().execute();
                    }
                }
            });
        }
        else
        {
            Alert.showToast(StartScreen.this,getString(R.string.reveri_not_initial));
            startMainScreen();
        }
    }

    private class AsyncCaller extends AsyncTask<String, Boolean, Boolean> {
        boolean status = false;
        private AlertDialog alert;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                alert=Alert.startDialog(StartScreen.this);
            }
            catch (Exception e)
            {
                Alert.showLog("Exception","Exception- "+ e.toString());
            }
        }
        @Override
        protected Boolean doInBackground(String... args) {
            RevSDK.initLangResources("https://api-gw.revup.reverieinc.com/apiman-gateway/ReverieMobilitySdk", selectedLangId, new LangResourceInitCompleteListener() {
                public void onLangResourceInitComplete(int i, RevStatus revStatus)
                {
                    Alert.showLog("TAG", "INIT RESOURCE COMPLETE:  Lang code = " + i + " , Status = " + revStatus.getStatusMessage());
                    if (revStatus.getStatusCode() == RevStatus.SUCCESS) {
                        publishProgress(true);
                    }
                    else
                    {
                        publishProgress(true);
                    }
                }
            });
            return status;
        }

        @Override
        protected void onProgressUpdate(Boolean... progress) {
            boolean status = progress[0];
            Alert.showLog("LANGUAGE","LANGUAGE status"+status);
            if(status)
            {
                boolean b = RevSDK.initKeypad(StartScreen.this, selectedLangId);
                try
                {
                    if(alert!=null)
                    {
                        if(alert.isShowing())
                        alert.dismiss();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                startMainScreen();
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // Do nothing now
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {

                }
                else
                {
                    // permission denied, boo! Disable the
                    if (!hasPermissions(PERMISSIONS)) {
                        ActivityCompat.requestPermissions(this, PERMISSIONS,PERMISSION);
                    }
                }
                return;
            }
        }
    }

    /*Check runtime permissions*/
    public boolean hasPermissions(String... permissions)
    {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && StartScreen.this != null && permissions != null)
        {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(StartScreen.this, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

}
