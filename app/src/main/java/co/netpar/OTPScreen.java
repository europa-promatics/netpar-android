package co.netpar;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.captain_miao.optroundcardview.OptRoundCardView;
import com.reverie.customcomponent.RevEditText;
import com.reverie.manager.RevEditFocusChangeListener;
import com.reverie.manager.RevSDK;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import co.netpar.Comman.Alert;
import co.netpar.Comman.Constants;
import co.netpar.Network.SmsReceiver;
import co.netpar.Retrofit.Retrofit2;
import co.netpar.Retrofit.ServiceResponse;
import co.netpar.Syncronization.NetparDataBase.SharedPreference;
import okhttp3.ResponseBody;
import retrofit2.Response;


public class OTPScreen extends AppCompatActivity implements ServiceResponse,OnClickListener,RevEditFocusChangeListener, View.OnKeyListener, TextWatcher {
    private ImageView home;
    private boolean login = false;
    private boolean comeFromContentView=false;
    private boolean myAccount=false;
    private LinearLayout reset;
    private CardView send_card_view;
    private TextView title;

    private RevEditText mPinFirstDigitEditText;
    private RevEditText mPinSecondDigitEditText;
    private RevEditText mPinThirdDigitEditText;
    private RevEditText mPinForthDigitEditText;
    private RevEditText mPinFifthDigitEditText;
    private RevEditText mPinSixthDigitEditText;
    private RevEditText mPinHiddenEditText;

    private String OTP_ID="",NUMBER="",OLD_NUMBER="";
    private final int SEND_OTP=0,VERIFY_OTP=1,UPDATE_DEVICE=2;

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    /**
     * Hides soft keyboard.
     *
     * @param editText EditText which has focus
     */
    public void hideSoftKeyboard(EditText editText) {
        if (editText == null)
            return;
      //  InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
      //  imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        RevSDK.destroyKeypad();
    }

    /**
     * Initialize EditText fields.
     */
    private void init() {
        mPinFirstDigitEditText = (RevEditText) findViewById(R.id.pin_first_edittext);
        mPinSecondDigitEditText = (RevEditText) findViewById(R.id.pin_second_edittext);
        mPinThirdDigitEditText = (RevEditText) findViewById(R.id.pin_third_edittext);
        mPinForthDigitEditText = (RevEditText) findViewById(R.id.pin_forth_edittext);
        mPinFifthDigitEditText = (RevEditText) findViewById(R.id.pin_fifth_edittext);
        mPinSixthDigitEditText = (RevEditText) findViewById(R.id.pin_sixth_edittext);
        mPinHiddenEditText = (RevEditText) findViewById(R.id.pin_hidden_edittext);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MainLayout(this, null));
        getWindow().setBackgroundDrawableResource(R.drawable.splash_back);
        setBundleData();
        initializeView();
        SmsReceiver.bindListener(new SmsReceiver.SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                Alert.showLog("OTP Screen","OTP-- "+messageText);
                mPinHiddenEditText.setText(messageText);
                setFocusedPinBackground(mPinFirstDigitEditText);
                setFocusedPinBackground(mPinSecondDigitEditText);
                setFocusedPinBackground(mPinThirdDigitEditText);
                setFocusedPinBackground(mPinForthDigitEditText);
                setFocusedPinBackground(mPinFifthDigitEditText);
                setFocusedPinBackground(mPinSixthDigitEditText);
                mPinFirstDigitEditText.setText(messageText.charAt(0) + "");
                mPinSecondDigitEditText.setText(messageText.charAt(1) + "");
                mPinThirdDigitEditText.setText(messageText.charAt(2) + "");
                mPinForthDigitEditText.setText(messageText.charAt(3) + "");
                mPinFifthDigitEditText.setText(messageText.charAt(4) + "");
                mPinSixthDigitEditText.setText(messageText.charAt(5) + "");
            }
        });
    }

    private void setBundleData()
    {
        Bundle data = getIntent().getExtras();
        if (data == null) {
            return;
        }
        if (data.getString("SOURCE").equalsIgnoreCase("Login")) {
            this.login = true;
            OLD_NUMBER=data.getString("OLD_NUMBER");

            if(getIntent().hasExtra(ContentView.CONTENT_ID_FORM_CONTENT_VIEW))
            {
                comeFromContentView=true;
            }
        }
        else if(data.getString("SOURCE").equalsIgnoreCase("MY_FRAGMENT"))
        {
            myAccount=true;
        }
        else
        {
            if(getIntent().hasExtra(ContentView.CONTENT_ID_FORM_CONTENT_VIEW))
            {
                comeFromContentView=true;
            }
            this.login = false;
        }
        OTP_ID=data.getString("OTP_ID");
        NUMBER=data.getString("NUMBER");
    }

    private void initializeView() {
        title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.otp));
        send_card_view = (CardView) findViewById(R.id.send_card_view);
        home = (ImageView) findViewById(R.id.home);
        send_card_view.setBackgroundResource(R.drawable.button_radious);

        init();
        setPINListeners();

        reset = (LinearLayout) findViewById(R.id.reset);
        send_card_view.setOnClickListener(this);
        home.setOnClickListener(this);
        reset.setOnClickListener(this);
    }

    private void resend()
    {
        String url= Constants.SEND_OTP+"+91"+NUMBER+"/AUTOGEN";
        new Retrofit2(OTPScreen.this,this,SEND_OTP,url).callService(true);
    }

    private void verifyOTP()
    {
        if(mPinHiddenEditText.length()<6)
        {
            return;
        }
        String urll=Constants.VERIFY_OTP+OTP_ID+"/"+mPinHiddenEditText.getText().toString();
        new Retrofit2(OTPScreen.this,this,VERIFY_OTP,urll).callService(false);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home:
                onBackPressed();
                return;
            case R.id.reset:
                resend();
                break;
            case R.id.send_card_view:
                verifyOTP();
                return;
            default:
                return;
        }
    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            final int id = v.getId();
            switch (id) {
                case R.id.pin_hidden_edittext:
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        if(mPinHiddenEditText.getText().length() == 6)
                        {mPinSixthDigitEditText.setText("");}
                       else if (mPinHiddenEditText.getText().length() == 5)
                            mPinFifthDigitEditText.setText("");
                        else if (mPinHiddenEditText.getText().length() == 4)
                            mPinForthDigitEditText.setText("");
                        else if (mPinHiddenEditText.getText().length() == 3)
                            mPinThirdDigitEditText.setText("");
                        else if (mPinHiddenEditText.getText().length() == 2)
                            mPinSecondDigitEditText.setText("");
                        else if (mPinHiddenEditText.getText().length() == 1)
                            mPinFirstDigitEditText.setText("");

                        if (mPinHiddenEditText.length() > 0)
                            mPinHiddenEditText.setText(mPinHiddenEditText.getText().subSequence(0, mPinHiddenEditText.length() - 1));

                        return true;
                    }

                    break;

                default:
                    return false;
            }
        }

        return false;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        setDefaultPinBackground(mPinFirstDigitEditText);
        setDefaultPinBackground(mPinSecondDigitEditText);
        setDefaultPinBackground(mPinThirdDigitEditText);
        setDefaultPinBackground(mPinForthDigitEditText);
        setDefaultPinBackground(mPinFifthDigitEditText);
        setDefaultPinBackground(mPinSixthDigitEditText);

        if (s.length() == 0) {
            setFocusedPinBackground(mPinFirstDigitEditText);
            mPinFirstDigitEditText.setText("");
        } else if (s.length() == 1) {
            setFocusedPinBackground(mPinSecondDigitEditText);
            mPinFirstDigitEditText.setText(s.charAt(0) + "");
            mPinSecondDigitEditText.setText("");
            mPinThirdDigitEditText.setText("");
            mPinForthDigitEditText.setText("");
            mPinFifthDigitEditText.setText("");
            mPinSixthDigitEditText.setText("");
        } else if (s.length() == 2) {
            setFocusedPinBackground(mPinThirdDigitEditText);
            mPinSecondDigitEditText.setText(s.charAt(1) + "");
            mPinThirdDigitEditText.setText("");
            mPinForthDigitEditText.setText("");
            mPinFifthDigitEditText.setText("");
            mPinSixthDigitEditText.setText("");
        } else if (s.length() == 3) {
            setFocusedPinBackground(mPinForthDigitEditText);
            mPinThirdDigitEditText.setText(s.charAt(2) + "");
            mPinForthDigitEditText.setText("");
            mPinFifthDigitEditText.setText("");
            mPinSixthDigitEditText.setText("");
        } else if (s.length() == 4) {
            setFocusedPinBackground(mPinFifthDigitEditText);
            mPinForthDigitEditText.setText(s.charAt(3) + "");
            mPinFifthDigitEditText.setText("");
            mPinSixthDigitEditText.setText("");
        } else if (s.length() == 5) {
            setDefaultPinBackground(mPinFifthDigitEditText);
            mPinFifthDigitEditText.setText(s.charAt(4) + "");
            mPinSixthDigitEditText.setText("");
        }
        else if (s.length() == 6) {
            setDefaultPinBackground(mPinFifthDigitEditText);
            mPinSixthDigitEditText.setText(s.charAt(5) + "");
            hideSoftKeyboard(mPinFifthDigitEditText);
            verifyOTP();
        }
    }

    /**
     * Sets default PIN background.
     *
     * @param editText edit text to change
     */
    private void setDefaultPinBackground(EditText editText) {
       // setViewBackground(editText, getResources().getDrawable(R.drawable.textfield_focused));
    }

    /**
     * Sets focus on a specific EditText field.
     *
     * @param editText EditText to set focus on
     */
    public static void setFocus(EditText editText) {
        if (editText == null)
            return;
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }

    /**
     * Sets focused PIN field background.
     *
     * @param editText edit text to change
     */
    private void setFocusedPinBackground(EditText editText) {
        //setViewBackground(editText, getResources().getDrawable(R.drawable.textfield_focused));
    }

    /**
     * Sets listeners for EditText fields.
     */
    private void setPINListeners() {
        mPinHiddenEditText.addTextChangedListener(this);

        mPinFirstDigitEditText.setRevEditFocusChangeListener(this);
        mPinSecondDigitEditText.setRevEditFocusChangeListener(this);
        mPinThirdDigitEditText.setRevEditFocusChangeListener(this);
        mPinForthDigitEditText.setRevEditFocusChangeListener(this);
        mPinFifthDigitEditText.setRevEditFocusChangeListener(this);
        mPinSixthDigitEditText.setRevEditFocusChangeListener(this);

        mPinFirstDigitEditText.setOnKeyListener(this);
        mPinSecondDigitEditText.setOnKeyListener(this);
        mPinThirdDigitEditText.setOnKeyListener(this);
        mPinForthDigitEditText.setOnKeyListener(this);
        mPinFifthDigitEditText.setOnKeyListener(this);
        mPinSixthDigitEditText.setOnKeyListener(this);
        mPinHiddenEditText.setOnKeyListener(this);
    }


    @Override
    public void onRevEditFocusChanged(View v, boolean hasFocus) {
        final int id = v.getId();
        switch (id) {
            case R.id.pin_first_edittext:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;

            case R.id.pin_second_edittext:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;

            case R.id.pin_third_edittext:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;

            case R.id.pin_forth_edittext:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;

            case R.id.pin_fifth_edittext:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;
            case R.id.pin_sixth_edittext:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;
            default:
                break;
        }
    }


    @SuppressWarnings("deprecation")
    public void setViewBackground(View view, Drawable background) {
        if (view == null || background == null)
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(background);
        } else {
            view.setBackgroundDrawable(background);
        }
    }

    /**
     * Shows soft keyboard.
     *
     * @param editText EditText which has focus
     */
    public void showSoftKeyboard(EditText editText) {
        if (editText == null)
            return;

        boolean val=RevSDK.initKeypad(OTPScreen.this,SharedPreference.retrieveLang(OTPScreen.this));
        if(!val)
        {
            InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, 0);
        }
    }


    /**
     * Custom LinearLayout with overridden onMeasure() method
     * for handling software keyboard show and hide events.
     */
    public class MainLayout extends LinearLayout {

        public MainLayout(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.activity_otpscreen, this);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            final int proposedHeight = MeasureSpec.getSize(heightMeasureSpec);
            final int actualHeight = getHeight();
            Log.d("TAG", "proposed: " + proposedHeight + ", actual: " + actualHeight);
            if (actualHeight >= proposedHeight) {
                // Keyboard is shown
                if (mPinHiddenEditText.length() == 0)
                    setFocusedPinBackground(mPinFirstDigitEditText);
                else
                    setDefaultPinBackground(mPinFirstDigitEditText);
            }

            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    public void onServiceResponse(int i, Response<ResponseBody> response) {
        try {
            switch (i) {
                case SEND_OTP:
                        JSONObject object = new JSONObject(response.body().string());
                        if (object.getString("Status").equalsIgnoreCase("Success")) {
                            String otp_id = object.getString("Details");
                            OTP_ID = otp_id;
                            Alert.showToast(OTPScreen.this, getString(R.string.otp_has_send));

                            mPinFirstDigitEditText.setText("");
                            mPinSecondDigitEditText.setText("");
                            mPinThirdDigitEditText.setText("");
                            mPinForthDigitEditText.setText("");
                            mPinFifthDigitEditText.setText("");
                            mPinSixthDigitEditText.setText("");
                            mPinHiddenEditText.setText("");
                        }
                    break;
                case VERIFY_OTP:
                        JSONObject objectt = new JSONObject(response.body().string());
                        if (objectt.getString("Status").equalsIgnoreCase("Success"))
                        {
                            if(myAccount)
                            {
                                Intent intent=new Intent();
                                intent.putExtra("MOBILE",NUMBER);
                                setResult(RESULT_OK,intent);
                                finish();
                            }
                            else
                            {
                                if (login)
                                {
                                    JSONObject obj=new JSONObject();
                                    obj.put("mobileNumber",OLD_NUMBER);
                                    obj.put("mobileNumberNew",NUMBER);
                                    new Retrofit2(OTPScreen.this,this,UPDATE_DEVICE,Constants.UPDATE_DEVICE,obj).callService(true);
                                    return;
                                }

                                if(comeFromContentView)
                                {
                                    startActivityForResult(new Intent(this,Profile.class).putExtra(ContentView.CONTENT_ID_FORM_CONTENT_VIEW,getIntent().getStringExtra(ContentView.CONTENT_ID_FORM_CONTENT_VIEW)),ContentView.REGISTER_FORM_CONTENT_VIEW);
                                    return;
                                }
                                SharedPreference.storeData(OTPScreen.this,Constants.OTP_VERIFIED,"true");
                                finish();
                                startActivity(new Intent(this, Profile.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                            }
                        }
                        else
                        {
                            Alert.showToast(OTPScreen.this, getString(R.string.otp_not_match));
                        }
                    break;
                case UPDATE_DEVICE:
                    JSONObject objecttt = new JSONObject(response.body().string());
                    if (objecttt.getString("success").equalsIgnoreCase("true"))
                    {
                        if(objecttt.has("info"))
                        {
                            JSONObject dataObj=objecttt.getJSONObject("info");
                            SharedPreference.storeData(OTPScreen.this,Constants.USER_ID,dataObj.getString("_id"));
                            SharedPreference.storeData(OTPScreen.this,Constants.FIRST_NAME,dataObj.getString("firstName"));
                            SharedPreference.storeData(OTPScreen.this,Constants.LAST_NAME,dataObj.getString("lastName"));
                            SharedPreference.storeData(OTPScreen.this,Constants.MOBILE_NUMBER,dataObj.getString("mobileNumber"));
                            SharedPreference.storeData(OTPScreen.this,Constants.STATE,dataObj.getString("state"));
                            SharedPreference.storeData(OTPScreen.this,Constants.DISTRICT,dataObj.getString("district"));
                            SharedPreference.storeData(OTPScreen.this,Constants.BLOCK,dataObj.getString("block"));
                            SharedPreference.storeData(OTPScreen.this,Constants.GENDER,dataObj.getString("gender"));
                            SharedPreference.storeData(OTPScreen.this,Constants.DOB,dataObj.getString("dateOfBirth"));
                            SharedPreference.storeData(OTPScreen.this,Constants.USER_IMAGE,dataObj.getString("userImage"));
                            SharedPreference.storeData(OTPScreen.this,Constants.USER_REF_CODE,dataObj.getString("referralCode"));
                            if(dataObj.has("stateRegional")) {
                                SharedPreference.storeData(OTPScreen.this, Constants.STATE_REGIONAL, dataObj.getString("stateRegional"));
                                SharedPreference.storeData(OTPScreen.this, Constants.DISTRICT_REGIONAL, dataObj.getString("districtRegional"));
                                SharedPreference.storeData(OTPScreen.this, Constants.BLOCK_REGIONAL, dataObj.getString("blockRegional"));
                            }

                            if(dataObj.has("totalSubmissions"))
                            {
                                SharedPreference.storeData(OTPScreen.this,Constants.USER_YOGDAN,dataObj.getString("totalSubmissions"));
                            }

                            if(dataObj.has("totalPublications"))
                            {
                                SharedPreference.storeData(OTPScreen.this,Constants.USER_LEKH,dataObj.getString("totalPublications"));
                            }

                        }

                        if(comeFromContentView)
                        {
                            Intent intent = new Intent();
                            intent.putExtra(ContentView.CONTENT_ID_FORM_CONTENT_VIEW, getIntent().getStringExtra(ContentView.CONTENT_ID_FORM_CONTENT_VIEW));
                            setResult(Activity.RESULT_OK, intent);
                            finish();                            return;
                        }
                        else
                        {
                            finish();
                            startActivity(new Intent(this, Home.class).putExtra("COME","APP").setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                    }
                    else
                    {
                        Alert.showToast(OTPScreen.this,objecttt.getString("msg"));
                    }
                    break;
                case Retrofit2.UNSUCCESSFUL:
                    helpResendAlert();
                    break;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void helpResendAlert()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(OTPScreen.this, R.style.custom_alert_dialog);
        View dialogView =getLayoutInflater().inflate(R.layout.yes_no_alert, null);
        ((TextView) dialogView.findViewById(R.id.title)).setText(R.string.sorry);
        ((TextView) dialogView.findViewById(R.id.msg)).setText(getString(R.string.otp_not_match));
        OptRoundCardView yes_card_view = (OptRoundCardView) dialogView.findViewById(R.id.yes_card_view);
        OptRoundCardView no_card_view = (OptRoundCardView) dialogView.findViewById(R.id.no_card_view);
        TextView yes_card_view_text=(TextView)dialogView.findViewById(R.id.yes_card_view_text);
        TextView no_card_view_text=(TextView)dialogView.findViewById(R.id.no_card_view_text);

        yes_card_view_text.setText(R.string.re_send);
        no_card_view_text.setText(R.string.help);

        yes_card_view.setBackgroundResource(R.drawable.button_radious);
        no_card_view.setBackgroundResource(R.drawable.button_radious);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialogg = dialogBuilder.create();
        alertDialogg.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialogg.getWindow().setLayout((int) (((double) getResources().getDisplayMetrics().widthPixels) * 0.9d), -2);
        alertDialogg.getWindow().setBackgroundDrawableResource(R.drawable.alert_back);
        alertDialogg.setCancelable(false);
        alertDialogg.setCanceledOnTouchOutside(false);
        yes_card_view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogg.dismiss();
                resend();
            }
        });
        no_card_view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogg.dismiss();
                Alert.helpDialog(OTPScreen.this);
            }
        });
        alertDialogg.show();
    }

    @Override
    public void onBackPressed() {
        if(myAccount)
        {
            Intent intent=new Intent();
            intent.putExtra("MOBILE",NUMBER);
            setResult(RESULT_CANCELED,intent);
            finish();
        }
        if(comeFromContentView)
        {
            cancelResultForContentView();
        }
        else
        {
            super.onBackPressed();
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
