package co.netpar;

import android.app.Activity;
import android.app.Dialog;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import co.netpar.App.Controller;
import co.netpar.Comman.Alert;
import co.netpar.Comman.Constants;
import co.netpar.Model.Blocks_Data_Model;
import co.netpar.Model.Data_Model;
import co.netpar.Model.District_Data_Model;
import co.netpar.Network.ConnectionDetector;
import co.netpar.Retrofit.Retrofit2;
import co.netpar.Retrofit.ServiceResponse;
import co.netpar.Syncronization.NetparDataBase.SharedPreference;
import okhttp3.ResponseBody;
import retrofit2.Response;

import com.facebook.drawee.view.SimpleDraweeView;
import com.github.captain_miao.optroundcardview.OptRoundCardView;
import com.reverie.customcomponent.RevEditText;
import com.reverie.manager.RevSDK;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SignIn extends AppCompatActivity implements OnClickListener,ServiceResponse,View.OnTouchListener{
    private ImageView home;
    private CardView sign_in_card_view;
    private TextView title,sign_up;
    private TextInputLayout input_layout_mobile;
    private EditText input_mobile;
    private final int SEND_OTP = 1,LOGIN=0,SEQURITY_ANSWER_RESULT=2,UPDATE_NUMBER=3,SEQURITY_ANSWER_RESULT1=4,SEQURITY_ANSWER_RESULT2=5;
    private Dialog security_dialog,security_dialog2;
    private String q1,q2,q3;

    /*found in MOBILE_NOT_MATCH case of Login Response*/
    private String profilePic="",mobile_no="",first_name="",last_name="";


    private int INCORRECT_MOBILE=1,WRONG_ANSWER=1;
    private boolean UpdateNumber = false;
    private String MOBILE_NUMBER;


    /*Only for Seq 2 Alert Calender*/
    AutoCompleteTextView ddd,mmm,yyyy;
    int mo=10;
    int yer=2017;
    List<String> dt28= new ArrayList<>();
    List<String> dt29= new ArrayList<>();
    List<String> dt30= new ArrayList<>();
    List<String> dt31= new ArrayList<>();
    List<String> dt= new ArrayList<>();
    List<String> m= new ArrayList<>();
    List<String> y= new ArrayList<>();

    public static final int PERMISSION = 1;
    public static final String[] PERMISSIONS = {android.Manifest.permission.RECEIVE_SMS, android.Manifest.permission.READ_SMS, android.Manifest.permission.SEND_SMS, android.Manifest.permission.INTERNET};


    private AlertDialog sequrityAlertDialog1,sequrityAlertDialog2;


    private List<Data_Model> state_data=new ArrayList<>();
    private List<String> state=new ArrayList<>();

    private List<District_Data_Model> district_data =new ArrayList<>();
    private List<String>  distri=new ArrayList<>();

    private List<Blocks_Data_Model> block_data=new ArrayList<>();
    private List<String> blocs=new ArrayList<>();
    private AutoCompleteTextView que1,que2,que3;
    private String st="",dst="",blk="";


    private boolean comeFromContentView=false;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getWindow().setBackgroundDrawableResource(R.drawable.splash_back);
        setIntentData();
        initializeView();
    }

    private void setIntentData()
    {
        Bundle data=getIntent().getExtras();
        if(data!=null)
        {
            UpdateNumber=data.getBoolean("UPDATE_MOBILE");
            if(getIntent().hasExtra(ContentView.CONTENT_ID_FORM_CONTENT_VIEW))
            {
                comeFromContentView=true;
            }
        }
    }

    private void initializeView()
    {
        sign_in_card_view = (CardView) findViewById(R.id.sign_in_card_view);
        home = (ImageView) findViewById(R.id.home);
        title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.sign_in));

        input_layout_mobile = (TextInputLayout) findViewById(R.id.input_layout_mobile);
        input_mobile = (EditText) findViewById(R.id.input_mobile);
        sign_up=(TextView)findViewById(R.id.sign_up);
        if(UpdateNumber)
        {
            input_layout_mobile.setHint(getString(R.string.old_number));
        }

        sign_in_card_view.setBackgroundResource(R.drawable.button_radious);
        sign_in_card_view.setOnClickListener(this);
        home.setOnClickListener(this);
        sign_up.setOnClickListener(this);
        input_mobile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                RevSDK.initKeypad(SignIn.this,SharedPreference.retrieveLang(SignIn.this));

                //showSoftKeyboard(input_mobile);
;                return false;
            }
        });

        if(comeFromContentView)
        {
            sign_up.setVisibility(View.GONE);
        }
    }


    public void showSoftKeyboard(EditText editText) {
        if (editText == null)
            return;
        InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
    }

    private void sequrityAlert()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SignIn.this, R.style.custom_alert_dialog);
        View dialogView = getLayoutInflater().inflate(R.layout.sequrity_question_lay, null);
         que1=(AutoCompleteTextView)dialogView.findViewById(R.id.que1);
         que2=(AutoCompleteTextView)dialogView.findViewById(R.id.que2);
         que3=(AutoCompleteTextView)dialogView.findViewById(R.id.que3);
        setStateData();
        setItemClick();
        Button dialog_button=(Button)dialogView.findViewById(R.id.dialog_button);
        dialogBuilder.setView(dialogView);
        sequrityAlertDialog1 = dialogBuilder.create();
        sequrityAlertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        sequrityAlertDialog1.getWindow().setLayout((int) (((double) getResources().getDisplayMetrics().widthPixels) * 0.9d), -2);
        sequrityAlertDialog1.getWindow().setBackgroundDrawableResource(R.drawable.alert_back);
        sequrityAlertDialog1.setCanceledOnTouchOutside(false);
        dialog_button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                q1=que1.getText().toString().trim();
                q2=que2.getText().toString().trim();
                q3=que3.getText().toString().trim();

                if(q1.length()==0)
                {
                    Alert.showToast(SignIn.this,getString(R.string.select_your_state));
                    return;}
                else if(q2.length()==0)
                {
                    Alert.showToast(SignIn.this,getString(R.string.select_your_district));
                    return;}
                else if(q3.length()==0)
                {
                    Alert.showToast(SignIn.this,getString(R.string.select_your_block));
                    return;}

                try
                {
                    JSONObject peram =new JSONObject();
                    peram.put("state", st);
                    peram.put("district",dst);
                    peram.put("block",blk);
                    peram.put("mobileNumber", input_mobile.getText().toString());
                    new Retrofit2(SignIn.this,SignIn.this,SEQURITY_ANSWER_RESULT1,Constants.SEQURITY_ANSWER1,peram).callService(true);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
              //  sequrityAlert2();
            }
        });
        sequrityAlertDialog1.show();
    }

    private void sequrityAlert2()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SignIn.this, R.style.custom_alert_dialog);
        View dialogView = getLayoutInflater().inflate(R.layout.sequrity_ques2, null);

        ddd= (AutoCompleteTextView) dialogView.findViewById(R.id.ddd);
        mmm= (AutoCompleteTextView) dialogView.findViewById(R.id.mmm);
        yyyy= (AutoCompleteTextView)dialogView.findViewById(R.id.yyy);

        setCalender();

        final RadioGroup gen_group=(RadioGroup)dialogView.findViewById(R.id.gen_group);
        Button submit=(Button)dialogView.findViewById(R.id.submit);
        dialogBuilder.setView(dialogView);
        sequrityAlertDialog2 = dialogBuilder.create();
        sequrityAlertDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        sequrityAlertDialog2.getWindow().setLayout((int) (((double) getResources().getDisplayMetrics().widthPixels) * 0.9d), -2);
        sequrityAlertDialog2.getWindow().setBackgroundDrawableResource(R.drawable.alert_back);
        sequrityAlertDialog2.setCanceledOnTouchOutside(false);
        submit.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String gen="";
                String d=ddd.getText().toString().toString().trim();
                String m=mmm.getText().toString().toString().trim();
                String y=yyyy.getText().toString().toString().trim();
                int selectedId = gen_group.getCheckedRadioButtonId();
                if(selectedId==R.id.male)
                {
                    gen="male";
                }
                else if(selectedId==R.id.female)
                {
                    gen="female";
                }

                if(d.equalsIgnoreCase("") || m.equalsIgnoreCase("") || y.equalsIgnoreCase(""))
                {
                    Alert.showToast(SignIn.this,getString(R.string.select_your_date_of_birth));
                    return;
                }
                else if(gen.equalsIgnoreCase(""))
                {
                    Alert.showToast(SignIn.this,getString(R.string.select_your_gender));
                    return;
                }

               /* if(Integer.parseInt(d)<10)
                {
                    d="0"+d;
                }
                if(Integer.parseInt(m)<10)
                {
                    m="0"+m;
                }*/
                try
                {
                    JSONObject peram =new JSONObject();
                    peram.put("state", st);
                    peram.put("district",dst);
                    peram.put("block",blk);
                    peram.put("dateOfBirth",d+"-"+m+"-"+y);
                    peram.put("gender", gen);
                    peram.put("mobileNumber", input_mobile.getText().toString());
                    new Retrofit2(SignIn.this,SignIn.this,SEQURITY_ANSWER_RESULT2,Constants.SEQURITY_ANSWER2,peram).callService(true);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        sequrityAlertDialog2.show();
    }

    private boolean checkValidation()
    {
        String i_mobile=input_mobile.getText().toString();
        if(i_mobile.trim().length()<1)
        {
            Alert.alertDialog(SignIn.this,getString(R.string.mobile_required),getString(R.string.sorry),false);
            return false;
        }
        else if(i_mobile.trim().replace(" ","").length()<10)
        {
            input_mobile.requestFocus();
            Alert.alertDialog(SignIn.this,getString(R.string.mobile_number_less_than_ten),getString(R.string.sorry),false);
            return false;
        }
     return true;
    }

    private void successAlert()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SignIn.this, R.style.custom_alert_dialog);
        View dialogView =getLayoutInflater().inflate(R.layout.simple_lay, null);
        ((TextView) dialogView.findViewById(R.id.title)).setText(getString(R.string.welcome_back));
        ((TextView) dialogView.findViewById(R.id.title)).setTextColor(ContextCompat.getColor(SignIn.this, R.color.green));
        ((TextView) dialogView.findViewById(R.id.msg)).setText(getString(R.string.you_have_successfull_sign_in_to_netpar));
        ImageView chk_error = (ImageView) dialogView.findViewById(R.id.chk_error);
        chk_error.setImageResource(R.drawable.check);
        OptRoundCardView ok_card_view = (OptRoundCardView) dialogView.findViewById(R.id.ok_card_view);
        ok_card_view.setBackgroundResource(R.drawable.button_radious);
        ok_card_view.setVisibility(View.GONE);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialogg = dialogBuilder.create();
        alertDialogg.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialogg.getWindow().setLayout((int) (((double) getResources().getDisplayMetrics().widthPixels) * 0.9d), -2);
        alertDialogg.getWindow().setBackgroundDrawableResource(R.drawable.alert_back);
        alertDialogg.setCancelable(false);
        alertDialogg.setCanceledOnTouchOutside(false);
        new CountDownTimer(5000, 1000)
        {
            public void onTick(long millisUntilFinished) {
                alertDialogg.show();
            }
            public void onFinish()
            {
                alertDialogg.dismiss();
                startHome();
            }
        }.start();
    }

    private void startHome()
    {
        finish();
        startActivity(new Intent(this,Home.class).putExtra("COME","APP").setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    private void isThisYouAlert()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SignIn.this, R.style.custom_alert_dialog);
        View dialogView =getLayoutInflater().inflate(R.layout.is_this_you_alert, null);
        SimpleDraweeView profile_pic=(SimpleDraweeView)dialogView.findViewById(R.id.profile_pic);//profilePic
        EditText input_firstName=(EditText)dialogView.findViewById(R.id.input_firstName);
        TextView title=(TextView)dialogView.findViewById(R.id.title);
       // title.setText(mobile_no+" "+getResources().getString(R.string.is_this_you));
        EditText input_last_name=(EditText)dialogView.findViewById(R.id.input_last_name);
        input_firstName.setText(first_name);
        input_last_name.setText(last_name);
        CardView yes_card_view = (CardView) dialogView.findViewById(R.id.yes_card_view);
        CardView no_card_view = (CardView) dialogView.findViewById(R.id.no_card_view);
        if(profilePic!=null)
        {
            Uri uriOne= Uri.parse(profilePic);
            profile_pic.setImageURI(uriOne);
        }
       yes_card_view.setBackgroundResource(R.drawable.button_radious);
       no_card_view.setBackgroundResource(R.drawable.button_radious);
       dialogBuilder.setView(dialogView);
       final AlertDialog alertDialogg = dialogBuilder.create();
       alertDialogg.getWindow().setBackgroundDrawable(new ColorDrawable(0));
       alertDialogg.getWindow().setLayout((int) (((double) getResources().getDisplayMetrics().widthPixels) * 0.9d), -2);
       alertDialogg.getWindow().setBackgroundDrawableResource(R.drawable.alert_back);
       alertDialogg.setCanceledOnTouchOutside(false);
       yes_card_view.setOnClickListener(new OnClickListener() {
           @Override
           public void onClick(View v) {
               alertDialogg.dismiss();
               sequrityAlert();
           }
       });
       no_card_view.setOnClickListener(new OnClickListener() {
           @Override
           public void onClick(View v) {
               alertDialogg.dismiss();
               Alert.switchActivityAlert(SignIn.this,SignUp.class,null,getString(R.string.sorry),getString(R.string.we_cant_find_your_account_please_sign_up),getString(R.string.sign_up),false);
           }
       });
       alertDialogg.show();
   }

    private void updateNumberAlert()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SignIn.this, R.style.custom_alert_dialog);
        View dialogView =getLayoutInflater().inflate(R.layout.yes_no_alert, null);
        ((TextView) dialogView.findViewById(R.id.title)).setText(R.string.sorry);
        ((TextView) dialogView.findViewById(R.id.msg)).setText(getString(R.string.do_you_want_to_update_your_mobile_no));
        CardView yes_card_view = (CardView) dialogView.findViewById(R.id.yes_card_view);
        CardView no_card_view = (CardView) dialogView.findViewById(R.id.no_card_view);
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
                isThisYouAlert();
            }
        });
        no_card_view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogg.dismiss();
                Alert.switchActivityAlert(SignIn.this,SignUp.class,null,getString(R.string.sorry),getString(R.string.we_cant_find_your_account_please_sign_up),getString(R.string.sign_up),false);
            }
        });
        alertDialogg.show();
    }

    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.home:
                onBackPressed();
                return;
            case R.id.sign_up:
                finish();
                startActivity(new Intent(this,SignUp.class));
                break;
            case R.id.sign_in_card_view:
                if (checkValidation())
                {
                    if(!ConnectionDetector.isInternetAvailable(SignIn.this))
                    {
                        Toast.makeText(this, getString(R.string.connect_internet_first), Toast.LENGTH_SHORT).show();
                        return;

                    }
                    if (!hasPermissions(PERMISSIONS))
                    {
                        ActivityCompat.requestPermissions(this, PERMISSIONS,PERMISSION);
                    }
                    else
                    {
                        loginService();
                    }
               }
                return;
            default:
                return;
        }
    }

    private void loginService()
    {
        try
        {
            RevSDK.destroyKeypad();
            JSONObject peram =new JSONObject();
            peram.put("mobileNumber",input_mobile.getText().toString().trim());
            new Retrofit2(this,this,LOGIN,Constants.LOGIN,peram).callService(true);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendOTP(String mobile)
    {
        if(mobile.trim().replace(" ","").isEmpty())
        {
            return;
        }
        MOBILE_NUMBER=mobile;//Send Via Intent to OTP Screen
        String url= Constants.SEND_OTP+"+91"+mobile.trim()+"/AUTOGEN";
        new Retrofit2(SignIn.this,this,SEND_OTP,url).callService(false);
    }

    @Override
    public void onServiceResponse(int i, Response<ResponseBody> response) {
        try
        {
            JSONObject object=new JSONObject(response.body().string());
            Alert.showLog("AAY","AAAY- "+object.toString());
            switch (i)
            {
                case LOGIN:
                    if(object.getString("success").equalsIgnoreCase("false"))
                    {
                        if(!UpdateNumber)
                        {
                            sendOTP(input_mobile.getText().toString());
                        }
                        else
                        {
                            if(object.has("info"))
                            {
                                JSONObject dataObj=object.getJSONObject("info");
                                Alert.showLog("NUM","NUM- "+dataObj.toString());
                                first_name=dataObj.getString("firstName");
                                last_name=dataObj.getString("lastName");
                                mobile_no=dataObj.getString("mobileNumber");
                                profilePic=dataObj.getString("userImage");
                            }
                            isThisYouAlert();
                        }
                    }
                    else
                    {
                        if(INCORRECT_MOBILE<2)
                        {
                            INCORRECT_MOBILE=INCORRECT_MOBILE+1;
                            switchActivityWithRecheckAlert();
                        }
                        else
                        {
                            wrongAnswer(getString(R.string.sorry),getString(R.string.your_mobile_number_is_not_registered_with_us_kindly_signup_as_a_new_user),getString(R.string.wrong_answer_signup));
                        }
                    }
                    break;
                case SEND_OTP:
                    if(object.getString("Status").equalsIgnoreCase("Success"))
                    {
                        String otp_id=object.getString("Details");

                        if(comeFromContentView)
                        {
                            startActivityForResult(new Intent(this,OTPScreen.class)
                                            .putExtra(ContentView.CONTENT_ID_FORM_CONTENT_VIEW,getIntent().getStringExtra(ContentView.CONTENT_ID_FORM_CONTENT_VIEW))
                                            .putExtra("SOURCE", "Login").putExtra("OTP_ID",otp_id).putExtra("NUMBER",MOBILE_NUMBER)
                                            .putExtra("OLD_NUMBER",input_mobile.getText().toString().trim())
                                    ,ContentView.REGISTER_FORM_CONTENT_VIEW);
                        }
                        else
                        {
                            finish();
                            startActivity(new Intent(this, OTPScreen.class).putExtra("SOURCE", "Login")
                                    .putExtra("OTP_ID",otp_id)
                                    .putExtra("NUMBER",MOBILE_NUMBER)
                                    .putExtra("OLD_NUMBER",input_mobile.getText().toString().trim()));
                        }
                    }
                    else
                    {
                        Alert.alertDialog(SignIn.this,getString(R.string.mobile_number_incorrect_retype_again),getString(R.string.sorry),false);
                    }
                    break;
                case SEQURITY_ANSWER_RESULT:
                    if(object.getString("success").equalsIgnoreCase("true"))
                    {
                        enterNewNumberAlert();
                    }
                    else
                    {
                        if(WRONG_ANSWER<2)
                        {
                            WRONG_ANSWER=WRONG_ANSWER+1;
                            sequrityAlert();
                            Alert.alertDialog(SignIn.this,getString(R.string.recheck_your_answer),getString(R.string.sorry1),false);
                        }
                        else
                        {
                            wrongAnswer(getString(R.string.sorry1),getString(R.string.wrong_answer),getString(R.string.wrong_answer_signup));
                        }
                    }
                    break;
                case SEQURITY_ANSWER_RESULT1:
                        if(object.getString("success").equalsIgnoreCase("true"))
                       {
                        if(sequrityAlertDialog1!=null)
                        {
                            if(sequrityAlertDialog1.isShowing())
                            {
                                sequrityAlertDialog1.dismiss();
                            }
                        }
                        sequrityAlert2();
                      }
                    else
                    {
                        String msg=getString(R.string.recheck_your_answer);
                        if(object.has("key"))
                        {
                            String key=object.getString("key");
                            if(key.equalsIgnoreCase("state"))
                            {
                                msg=getString(R.string.wrong_state);
                            }
                            else if(key.equalsIgnoreCase("district"))
                            {
                                msg=getString(R.string.wrong_dist);
                            }
                            else if(key.equalsIgnoreCase("block"))
                            {
                                msg=getString(R.string.wrong_block);
                            }
                        }

                        if(WRONG_ANSWER<2)
                        {
                            WRONG_ANSWER=WRONG_ANSWER+1;
                            Alert.alertDialog(SignIn.this,msg,getString(R.string.sorry1),false);
                        }
                        else
                        {
                            wrongAnswer(getString(R.string.sorry1),getString(R.string.wrong_answer),getString(R.string.wrong_answer_signup));
                        }
                    }
                    break;
                case SEQURITY_ANSWER_RESULT2:
                    if(object.getString("success").equalsIgnoreCase("true"))
                    {
                        if(sequrityAlertDialog2!=null)
                        {
                            if(sequrityAlertDialog2.isShowing())
                            {
                                sequrityAlertDialog2.dismiss();
                            }
                        }
                        enterNewNumberAlert();
                    }
                    else
                    {
                        String msg=getString(R.string.recheck_your_answer);
                        if(object.has("key"))
                        {
                            String key=object.getString("key");
                            if(key.equalsIgnoreCase("state"))
                            {
                                msg=getString(R.string.wrong_state);
                            }
                            else if(key.equalsIgnoreCase("district"))
                            {
                                msg=getString(R.string.wrong_dist);
                            }
                            else if(key.equalsIgnoreCase("block"))
                            {
                                msg=getString(R.string.wrong_block);
                            }
                            else if(key.equalsIgnoreCase("dateOfBirth"))
                            {
                                msg=getString(R.string.wrong_dob);
                            }
                            else if(key.equalsIgnoreCase("gender"))
                            {
                                msg=getString(R.string.wrong_gender);
                            }
                        }

                        if(WRONG_ANSWER<2)
                        {
                            WRONG_ANSWER=WRONG_ANSWER+1;
                            Alert.alertDialog(SignIn.this,msg,getString(R.string.sorry1),false);
                        }
                        else
                        {
                            wrongAnswer(getString(R.string.sorry1),getString(R.string.wrong_answer),getString(R.string.wrong_answer_signup));
                        }
                    }
                    break;
                case UPDATE_NUMBER:
                    if (object.getString("success").equalsIgnoreCase("true"))
                    {
                           if(object.has("info"))
                            {
                                JSONObject dataObj=object.getJSONObject("info");
                                SharedPreference.storeData(SignIn.this,Constants.USER_ID,dataObj.getString("_id"));
                                SharedPreference.storeData(SignIn.this,Constants.FIRST_NAME,dataObj.getString("firstName"));
                                SharedPreference.storeData(SignIn.this,Constants.LAST_NAME,dataObj.getString("lastName"));
                                SharedPreference.storeData(SignIn.this,Constants.MOBILE_NUMBER,dataObj.getString("mobileNumber"));
                                SharedPreference.storeData(SignIn.this,Constants.STATE,dataObj.getString("state"));
                                SharedPreference.storeData(SignIn.this,Constants.DISTRICT,dataObj.getString("district"));
                                SharedPreference.storeData(SignIn.this,Constants.BLOCK,dataObj.getString("block"));
                                SharedPreference.storeData(SignIn.this,Constants.GENDER,dataObj.getString("gender"));
                                SharedPreference.storeData(SignIn.this,Constants.DOB,dataObj.getString("dateOfBirth"));
                                SharedPreference.storeData(SignIn.this,Constants.USER_IMAGE,dataObj.getString("userImage"));
                                SharedPreference.storeData(SignIn.this,Constants.USER_REF_CODE,dataObj.getString("referralCode"));

                                if(dataObj.has("stateRegional")) {
                                    SharedPreference.storeData(SignIn.this, Constants.STATE_REGIONAL, dataObj.getString("stateRegional"));
                                    SharedPreference.storeData(SignIn.this, Constants.DISTRICT_REGIONAL, dataObj.getString("districtRegional"));
                                    SharedPreference.storeData(SignIn.this, Constants.BLOCK_REGIONAL, dataObj.getString("blockRegional"));
                                }

                                if(dataObj.has("totalSubmissions"))
                                {
                                    SharedPreference.storeData(SignIn.this,Constants.USER_YOGDAN,dataObj.getString("totalSubmissions"));
                                }

                                if(dataObj.has("totalPublications"))
                                {
                                    SharedPreference.storeData(SignIn.this,Constants.USER_LEKH,dataObj.getString("totalPublications"));
                                }
                            }
                        successAlert();
                    }
                    else
                    {
                        Alert.switchActivityAlert(SignIn.this,SignUp.class,null,getString(R.string.sorry),getString(R.string.we_cant_find_your_account_please_sign_up),getString(R.string.sign_up),false);
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

    public void switchActivityWithRecheckAlert()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SignIn.this, R.style.custom_alert_dialog);
        View dialogView = getLayoutInflater().inflate(R.layout.answer_incorrent, null);
        ((TextView) dialogView.findViewById(R.id.title)).setText(R.string.sorry);
        ((TextView) dialogView.findViewById(R.id.msg)).setText(R.string.recheck_detail);
         ImageView chk_error = (ImageView) dialogView.findViewById(R.id.chk_error);
        ((TextView) dialogView.findViewById(R.id.help_txt)).setText(R.string.recheck);
        ((TextView) dialogView.findViewById(R.id.signup_text_txt)).setText(R.string.new_user);
        CardView recheck = (CardView) dialogView.findViewById(R.id.help_card_view);
        recheck.setBackgroundResource(R.drawable.button_radious);
        CardView sign_up_card_view = (CardView) dialogView.findViewById(R.id.sign_up_card_view);
        sign_up_card_view.setBackgroundResource(R.drawable.button_radious);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.getWindow().setLayout((int) (((double)getResources().getDisplayMetrics().widthPixels) * 0.9d), -2);
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_back);
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        recheck.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        sign_up_card_view.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                finish();
                startActivity(new Intent(SignIn.this,SignUp.class));
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void wrongAnswer(String title,String message,String buttonText)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SignIn.this, R.style.custom_alert_dialog);
        View dialogView =getLayoutInflater().inflate(R.layout.answer_incorrent, null);
        ((TextView) dialogView.findViewById(R.id.title)).setText(title);
        ((TextView) dialogView.findViewById(R.id.msg)).setText(message);
        TextView signup_text_txt=(TextView)dialogView.findViewById(R.id.signup_text_txt);
        TextView help_txt=(TextView)dialogView.findViewById(R.id.help_txt);
        ImageView chk_error = (ImageView) dialogView.findViewById(R.id.chk_error);
        CardView help_card_view=(CardView)dialogView.findViewById(R.id.help_card_view);
        CardView sign_up_card_view=(CardView)dialogView.findViewById(R.id.sign_up_card_view);
        help_card_view.setBackgroundResource(R.drawable.button_radious);

        help_card_view.setVisibility(View.GONE);
        sign_up_card_view.setBackgroundResource(R.drawable.button_radious);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialogg = dialogBuilder.create();
        sign_up_card_view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogg.dismiss();
                SignIn.this.finish();
                startActivity(new Intent(SignIn.this,SignUp.class));
            }
        });
        help_card_view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogg.dismiss();
                Alert.helpDialog(SignIn.this);
            }
        });
        alertDialogg.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialogg.getWindow().setLayout((int) (((double) getResources().getDisplayMetrics().widthPixels) * 0.9d), -2);
        alertDialogg.getWindow().setBackgroundDrawableResource(R.drawable.alert_back);
        alertDialogg.setCancelable(false);
        alertDialogg.setCanceledOnTouchOutside(false);
        alertDialogg.show();
    }

    public void enterNewNumberAlert()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SignIn.this,R.style.custom_alert_dialog);
        View dialogView = getLayoutInflater().inflate(R.layout.enter_new_number, null);
        CardView update_card_view = (CardView) dialogView.findViewById(R.id.update_card_view);
        update_card_view.setBackgroundResource(R.drawable.button_radious);
        final RevEditText input_mobile=(RevEditText)dialogView.findViewById(R.id.input_mobile);
        input_mobile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                RevSDK.initKeypad(SignIn.this,SharedPreference.retrieveLang(SignIn.this));
                return false;
            }
        });
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.getWindow().setLayout((int) (((double)getResources().getDisplayMetrics().widthPixels) * 0.9d), -2);
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_back);
        alertDialog.setCanceledOnTouchOutside(false);
        update_card_view.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if(!input_mobile.getText().toString().trim().isEmpty())
                {
                    alertDialog.dismiss();
                    sendOTP(input_mobile.getText().toString());
                }
            }
        });
        alertDialog.show();
    }


    /*Set Calender For Data Of Birth*/
    private void setCalender()
    {
        dt28.clear();
        dt29.clear();
        dt30.clear();
        dt31.clear();
        m.clear();
        y.clear();

        for (int i=1;i<29;i++)
        {
            String d=String.valueOf(i);
            if(i<10)
            {
                d="0"+d;
            }
            dt28.add(d);
        }

        for (int i=1;i<30;i++)
        {
            String d=String.valueOf(i);
            if(i<10)
            {
                d="0"+d;
            }
            dt29.add(d);
        }

        for (int i=1;i<31;i++)
        {
            String d=String.valueOf(i);
            if(i<10)
            {
                d="0"+d;
            }
            dt30.add(d);
        }

        for (int i=1;i<32;i++)
        {
            String d=String.valueOf(i);
            if(i<10)
            {
                d="0"+d;
            }
            dt31.add(d);
        }


        for (int j=1;j<13;j++)
        {
            String mmmm=String.valueOf(j);
            if(j<10)
            {
                mmmm="0"+mmmm;
            }
            m.add(mmmm);
        }

        for (int i=1960;i<2018;i++)
        {
            y.add(String.valueOf(i));
        }

        ddd.setAdapter(new ArrayAdapter(this, R.layout.drop_item,dt));
        ddd.setKeyListener(null);
        ddd.setOnTouchListener(this);
        setDay(31);

        mmm.setAdapter(new ArrayAdapter(this, R.layout.drop_item,m));
        mmm.setKeyListener(null);
        mmm.setOnTouchListener(this);

        yyyy.setAdapter(new ArrayAdapter(this, R.layout.drop_item, y));
        this.yyyy.setKeyListener(null);
        this.yyyy.setOnTouchListener(this);

        mmm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mo=Integer.parseInt(((TextView)view).getText().toString());
                setDay(getDayInaMonth(yer, mo));
            }
        });

        yyyy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                yer=Integer.parseInt(((TextView)view).getText().toString());
                setDay(getDayInaMonth(yer, mo));
            }
        });
    }

    private void setDay(int day)
    {
        dt.clear();
        switch (day)
        {
            case 28:
                dt.addAll(dt28);
                break;
            case 29:
                dt.addAll(dt29);
                break;
            case 30:
                dt.addAll(dt30);
                break;
            case 31:
                dt.addAll(dt31);
                break;
            default:
                dt.addAll(dt31);
                break;
        }

        try
        {
            if(!ddd.getText().toString().isEmpty())
            {
                int dd_val = Integer.parseInt(ddd.getText().toString());
                if(dd_val > ddd.getAdapter().getCount())
                {
                    ddd.setText(dt.get(dt.size()-1));
                    ddd.setAdapter(new ArrayAdapter(this, R.layout.drop_item,dt));
                }
            }
        }
        catch (Exception e)
        {e.toString();}
    }

    private int getDayInaMonth(int yr, int mn)
    {
        int dayInaMonth = 0;
        if ((yr % 4 == 0 && yr % 100 != 0) || yr % 400 == 0) {
            if (mn == 2) {
                dayInaMonth = 29;
            } else {
                if (mn == 4 || mn == 6 || mn == 9 || mn == 11) {
                    dayInaMonth = 30;
                } else {
                    dayInaMonth = 31;
                }
            }
        }
        else {
            if (mn == 2) {
                dayInaMonth = 28;
            } else {
                if (mn == 4 || mn == 6 || mn == 9 || mn == 11) {
                    dayInaMonth = 30;
                } else {
                    dayInaMonth = 31;
                }
            }
        }
        return dayInaMonth;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (view.getId())
        {
            case R.id.ddd:
                ((AutoCompleteTextView) view).showDropDown();
                break;
            case R.id.mmm:
                ((AutoCompleteTextView) view).showDropDown();
                break;
            case R.id.yyy:
                ((AutoCompleteTextView) view).showDropDown();
                break;
            case R.id.que1:
                ((AutoCompleteTextView) view).showDropDown();
                break;
            case R.id.que2:
                ((AutoCompleteTextView) view).showDropDown();
                break;
            case R.id.que3:
                ((AutoCompleteTextView) view).showDropDown();
                break;
        }
        return false;
    }

    public boolean hasPermissions(String... permissions)
    {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && SignIn.this != null && permissions != null)
        {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(SignIn.this, permission) != PackageManager.PERMISSION_GRANTED) {
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
                    loginService();
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

    /*Function Related To State, District, Block*/
    private void setItemClick()
    {
        que1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                st=state_data.get(position).getState_name_english();
                dst="";
                blk="";
                que2.setText("");
                que3.setText("");
                getAllDistrict(state_data.get(position));
            }
        });

        que2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
            {
                dst=district_data.get(position).getDistrict_name_english();
                blk="";
                que3.setText("");
                getAllBlocks(district_data.get(position));
            }
        });

        que3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                blk=block_data.get(position).getBlock_name_english();
            }
        });
    }

    private void getAllDistrict(Data_Model state_data)
    {
        district_data.clear();
        distri.clear();
        district_data= state_data.getDistricts();
        for (District_Data_Model st:district_data)
        {
            distri.add(st.getDistrict_regional());
        }
        que2.setAdapter(new ArrayAdapter(this, R.layout.drop_item,distri));
    }

    private void getAllBlocks(District_Data_Model district_data)
    {
        block_data.clear();
        blocs.clear();
        block_data= district_data.getBlocks();
        for (Blocks_Data_Model st:block_data)
        {
            blocs.add(st.getBlock_name_regional());
        }
        que3.setAdapter(new ArrayAdapter(this, R.layout.drop_item, blocs));
    }

    private void setStateData()
    {
        que1.setKeyListener(null);
        que1.setOnTouchListener(this);

        que2.setKeyListener(null);
        que2.setOnTouchListener(this);

        que3.setKeyListener(null);
        que3.setOnTouchListener(this);

        state_data.clear();
        state.clear();
        state_data= Controller.getState_data();
        for (Data_Model st:state_data)
        {
            state.add(st.getState_name_regional());
        }
        que1.setAdapter(new ArrayAdapter(this, R.layout.drop_item,state));
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
