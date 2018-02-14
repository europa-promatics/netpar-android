package co.netpar;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import co.netpar.Comman.Alert;
import co.netpar.Comman.Constants;
import co.netpar.Keyboard.KeyboardCredentialConstants;
import co.netpar.Network.ConnectionDetector;
import co.netpar.Retrofit.Retrofit2;
import co.netpar.Retrofit.ServiceResponse;
import co.netpar.Syncronization.NetparDataBase.SharedPreference;
import okhttp3.ResponseBody;
import retrofit2.Response;
import com.reverie.manager.LangResourceInitCompleteListener;
import com.reverie.manager.RevSDK;
import com.reverie.manager.RevStatus;
import com.reverie.manager.ValidationCompleteListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class SignUp extends AppCompatActivity implements OnClickListener,ServiceResponse {
    private ImageView home;
    private CardView sign_up_card_view;
    private TextView title,sign_in;
    private TextInputLayout input_layout_mobile;
    private EditText input_mobile;
    private final int SEND_OTP = 0;
    public static final int PERMISSION = 1;
    public static final String[] PERMISSIONS = {android.Manifest.permission.RECEIVE_SMS, android.Manifest.permission.READ_SMS, android.Manifest.permission.SEND_SMS, android.Manifest.permission.INTERNET};

    private static final int VERIFY_NUMBER=100;
    AlertDialog alertDialog;

    private boolean comeFromContentView=false;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().setBackgroundDrawableResource(R.drawable.splash_back);
        initializeView();
        setIntentData();
    }

    private void setIntentData()
    {
        if(getIntent().getExtras()!=null)
        {
            /*Check that comes from Content View Activity*/
            if(getIntent().hasExtra(ContentView.CONTENT_ID_FORM_CONTENT_VIEW))
            {
                comeFromContentView=true;
                initialiseRevereSDKS();
            }
        }
    }

    private void initializeView() {
        sign_up_card_view = (CardView) findViewById(R.id.sign_up_card_view);
        home = (ImageView) findViewById(R.id.home);
        title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.sign_up));

        input_layout_mobile = (TextInputLayout) findViewById(R.id.input_layout_mobile);
        input_mobile = (EditText) findViewById(R.id.input_mobile);
        sign_in=(TextView)findViewById(R.id.sign_in);
        this.sign_up_card_view.setBackgroundResource(R.drawable.button_radious);
        this.sign_up_card_view.setOnClickListener(this);
        this.home.setOnClickListener(this);
        sign_in.setOnClickListener(this);
        input_mobile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //showSoftKeyboard(input_mobile);
                RevSDK.initKeypad(SignUp.this,SharedPreference.retrieveLang(SignUp.this));
                return false;
            }
        });
    }

    public void showSoftKeyboard(EditText editText) {
        if (editText == null)
            return;
        InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
    }

    /*UI Validate*/
    private boolean checkValidation() {
        String i_mobile = input_mobile.getText().toString();
        if(i_mobile.trim().length()<1)
        {
            Alert.alertDialog(SignUp.this,getString(R.string.mobile_required),getString(R.string.sorry),false);
            return false;
        }
        else if(i_mobile.trim().replace(" ","").length()<10)
        {
            input_mobile.requestFocus();
            Alert.alertDialog(SignUp.this,getString(R.string.mobile_number_less_than_ten),getString(R.string.sorry),false);
            return false;
        }
        return true;
    }

    private void check_already_registered()
    {
        alertDialog=Alert.startDialog(SignUp.this);
        try
        {
            JSONObject peram =new JSONObject();
            peram.put("mobileNumber",input_mobile.getText().toString().trim());
            new Retrofit2(this,this,VERIFY_NUMBER,Constants.CHECK_MOBILE,peram).callService(false);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendOTP()
    {
        String url= Constants.SEND_OTP+"+91"+input_mobile.getText().toString().trim()+"/AUTOGEN";
        new Retrofit2(SignUp.this,this,SEND_OTP,url).callService(false);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home:
               onBackPressed();
                return;
            case R.id.sign_in:
                if(comeFromContentView)
                {
                    Alert.loginOption(SignUp.this,false,true,getIntent().getStringExtra(ContentView.CONTENT_ID_FORM_CONTENT_VIEW));
                }
                else
                {
                    Alert.loginOption(SignUp.this,true,false,null);
                }
                break;
            case R.id.sign_up_card_view:
                if (checkValidation())
                {
                    if(!ConnectionDetector.isInternetAvailable(SignUp.this))
                    {
                        Toast.makeText(this, getString(R.string.connect_internet_first), Toast.LENGTH_SHORT).show();
                        return;

                    }
                    if (!hasPermissions(PERMISSIONS)) {
                        ActivityCompat.requestPermissions(this, PERMISSIONS,PERMISSION);
                    }
                    else
                    {
                        RevSDK.destroyKeypad();
                        check_already_registered();
                    }
                }
                return;
            default:
                return;
        }
    }

    @Override
    public void onServiceResponse(int i, Response<ResponseBody> response) {
        try {
            JSONObject object=new JSONObject(response.body().string());
            switch (i)
        {
            case SEND_OTP:
                if(alertDialog!=null)
                {
                    alertDialog.dismiss();
                }
                        if(object.getString("Status").equalsIgnoreCase("Success"))
                        {
                            SharedPreference.storeData(SignUp.this,Constants.SignUpTempMNum,input_mobile.getText().toString().trim());

                            String otp_id=object.getString("Details");
                            if(comeFromContentView)
                            {
                                startActivityForResult(new Intent(SignUp.this,OTPScreen.class)
                                        .putExtra(ContentView.CONTENT_ID_FORM_CONTENT_VIEW,getIntent().getStringExtra(ContentView.CONTENT_ID_FORM_CONTENT_VIEW)).putExtra("SOURCE", "SignUp").putExtra("OTP_ID",otp_id).putExtra("NUMBER",input_mobile.getText().toString().trim())
                                        ,ContentView.REGISTER_FORM_CONTENT_VIEW);
                            }
                            else
                            {
                                finish();
                                startActivity(new Intent(this, OTPScreen.class).putExtra("SOURCE", "SignUp").putExtra("OTP_ID",otp_id).putExtra("NUMBER",input_mobile.getText().toString().trim()));
                            }
                        }
                        else
                        {
                            Alert.alertDialog(SignUp.this,getString(R.string.mobile_number_incorrect_retype_again),getString(R.string.sorry),false);
                        }
                break;
            case VERIFY_NUMBER:
                if(object.getString("success").equalsIgnoreCase("true"))
                {
                    sendOTP();
                }
                else
                {
                    if(alertDialog!=null)
                    {
                        alertDialog.dismiss();
                    }
                    Alert.alertDialog(SignUp.this,getString(R.string.mobile_number_already_registered),getString(R.string.sorry),false);
                }
                break;
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean hasPermissions(String... permissions)
    {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && SignUp.this != null && permissions != null)
        {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(SignUp.this, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION:
                {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        check_already_registered();
                    }
                    else
                    {
                        check_already_registered();
                        // permission denied, boo! Disable the
                        if (!hasPermissions(PERMISSIONS)) {
                            ActivityCompat.requestPermissions(this, PERMISSIONS,PERMISSION);
                        }
                    }
                    return;
                }
        }
    }

    private void initialiseRevereSDKS()
    {
        RevSDK.validateKey(getApplicationContext(), "https://api-gw.revup.reverieinc.com/apiman-gateway/ReverieMobilitySdk", KeyboardCredentialConstants.SDK_TEST_API_KEY, "co.netpar", new ValidationCompleteListener() {
            @Override
            public void onValidationComplete(int statusCode, String statusMessage) {
                Alert.showLog("Language", "LICENSE VALIDATION COMPLETE : " + statusCode + " ," + statusMessage);
                if (statusCode != 1)
                {
                    Alert.showToast(SignUp.this,getString(R.string.reveri_not_initial));
                }
                else if(statusCode == 3)
                {
                    Alert.showToast(SignUp.this,getString(R.string.reveri_not_initial));
                }
                else
                {
                    new AsyncCaller().execute();
                }
            }
        });
    }

    private class AsyncCaller extends AsyncTask<String, Boolean, Boolean> {
        boolean status = false;
        private AlertDialog alert;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                alert=Alert.startDialog(SignUp.this);
            }
            catch (Exception e)
            {
                Alert.showLog("Exception","Exception- "+ e.toString());
            }
        }
        @Override
        protected Boolean doInBackground(String... args) {
            final int selectedLangId = SharedPreference.retrieveLang(SignUp.this);
            RevSDK.initLangResources("https://api-gw.revup.reverieinc.com/apiman-gateway/ReverieMobilitySdk", selectedLangId, new LangResourceInitCompleteListener() {
                public void onLangResourceInitComplete(int i, RevStatus revStatus) {
                    if (revStatus.getStatusCode() == RevStatus.SUCCESS) {
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
                if(alert!=null)
                {
                    if(alert.isShowing())
                    {
                        alert.dismiss();
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // Do nothing now
        }
    }

    @Override
    public void onBackPressed() {
        if(comeFromContentView)
        {
            cancelResultForContentView();
        }
        else
        {
            finish();
        }
    }

    private void cancelResultForContentView()
    {
        Intent intent = new Intent();
        intent.putExtra(ContentView.CONTENT_ID_FORM_CONTENT_VIEW, getIntent().getStringExtra(ContentView.CONTENT_ID_FORM_CONTENT_VIEW));
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case ContentView.REGISTER_FORM_CONTENT_VIEW:
                if(resultCode==RESULT_OK)
                {
                    Intent intent = new Intent();
                    intent.putExtra(ContentView.CONTENT_ID_FORM_CONTENT_VIEW, getIntent().getStringExtra(ContentView.CONTENT_ID_FORM_CONTENT_VIEW));
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
                else
                {
                    cancelResultForContentView();
                }
                break;
        }
    }
}
