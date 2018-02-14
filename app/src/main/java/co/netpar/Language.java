package co.netpar;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import co.netpar.Comman.Alert;
import co.netpar.Comman.Constants;
import co.netpar.Keyboard.KeyboardCredentialConstants;
import co.netpar.Syncronization.NetparDataBase.SharedPreference;
import com.facebook.drawee.backends.pipeline.Fresco;


public class Language extends AppCompatActivity {
    private static final int READ_PHONE_STATE_REQUEST_CODE = 101;
    private Button button;
    private Spinner keypadLangSpinner;
    private int selectedLangId =0;// KeyboardCredentialConstants.langIdsArray[9];
    private String selectedLangName = "";
    private final int SEND_OTP=0;
    public static final int PERMISSION = 1;
    public static final String[] PERMISSIONS = {android.Manifest.permission.INTERNET, android.Manifest.permission.READ_PHONE_STATE};
    private boolean activity_running=false;
    private boolean first_time=true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setScreenAccordingToSession();
        setContentView(R.layout.activity_language);
        getWindow().setBackgroundDrawableResource(R.drawable.louncher_screen);
        initializeView();
    }

    /*Add on verify otp sign up process and remove on Profile after successful registration*/
    private void setScreenAccordingToSession()
    {
        if(SharedPreference.retrieveData(getApplicationContext(),Constants.OTP_VERIFIED)!=null)
        {
            startActivity(new Intent(getApplicationContext(),Profile.class).putExtra("COME","APP").setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
            return;
        }

        if(SharedPreference.retrieveData(getApplicationContext(),Constants.USER_ID)!=null)
        {
            startActivity(new Intent(getApplicationContext(),Home.class).putExtra("COME","APP").setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
            return;
        }
        else if(SharedPreference.retrieveLang(getApplicationContext())!= 0)
        {
            startActivity(new Intent(getApplicationContext(),StartScreen.class).putExtra("COME","DIRECT").setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
            return;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        activity_running=true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        activity_running=false;
    }

    private void initializeView()
    {
        keypadLangSpinner = (Spinner) findViewById(R.id.keypadLangSpinner);
        initLanguageSpinner();
    }

    public void open_languageSpinner(View v) {
        try {
            this.keypadLangSpinner.performClick();
        }
        catch (Exception e)
        {
            Alert.showToast(Language.this,getString(R.string.connect_internet_first));
            e.printStackTrace();
        }
    }

    public void initLanguageSpinner()
    {
       keypadLangSpinner.setAdapter(new ArrayAdapter(this, R.layout.drop_item, getResources().getStringArray(R.array.language_array)));
       keypadLangSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
       {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                try
                {
                    ((TextView)view).setTextColor(ContextCompat.getColor(Language.this, R.color.white));
                    if(!first_time)
                    {
                        if (position != 0)
                        {
                            onSpinnerLangSelected(position - 1);
                        }
                        else {
                            Alert.showToast(Language.this, Language.this.getString(R.string.choose_language));
                        }
                    }
                    else
                    {
                        first_time=false;
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void onSpinnerLangSelected(int pos)
    {
        selectedLangId = KeyboardCredentialConstants.langIdsArray[pos];
        selectedLangName = KeyboardCredentialConstants.langNamesArray[pos];

        /*only for marathi yet*/
        selectedLangId=10;
        selectedLangName="Marathi";

        Alert.showLog("LANGUAGE", this.selectedLangName + " - " + this.selectedLangId);
        SharedPreference.storeLang(this, this.selectedLangId);
        finish();
        startActivity(new Intent(Language.this, StartScreen.class));
    }
}
