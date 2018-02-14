package co.netpar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.reverie.customcomponent.RevEditText;
import com.reverie.manager.LangResourceInitCompleteListener;
import com.reverie.manager.RevSDK;
import com.reverie.manager.RevStatus;
import com.reverie.manager.ValidationCompleteListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import co.netpar.App.Controller;
import co.netpar.Comman.Alert;
import co.netpar.Comman.Constants;
import co.netpar.Keyboard.KeyboardCredentialConstants;
import co.netpar.Model.Blocks_Data_Model;
import co.netpar.Model.Data_Model;
import co.netpar.Model.District_Data_Model;
import co.netpar.Network.ConnectionDetector;
import co.netpar.Retrofit.Retrofit2;
import co.netpar.Retrofit.ServiceResponse;
import co.netpar.Syncronization.NetparDataBase.SharedPreference;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class Profile extends AppCompatActivity implements OnClickListener, OnTouchListener, OnItemSelectedListener, ServiceResponse{
    private TextInputLayout block_layout;
    private List<String> dd_list = new ArrayList();
    private AutoCompleteTextView spinner_state,district,input_block;
    private TextInputLayout district_layout_firstName;
    private ImageView home;
    private TextInputLayout spinner_state_layout;
    private CardView submit_card_view;
    private TextView title;
    private RadioGroup radioSex;
    private int year, month, day;
    private final int REGISTER=0;
    private TextInputLayout input_layout_firstName, input_layout_last_name;
    private RevEditText input_firstName, input_last_name,referal_code;
    private ImageView state_drop_arrow;
    private RelativeLayout state_lay;

    /*Only for calender*/
    private AutoCompleteTextView ddd,mmm,yyyy;
    int mo=10;
    int yer=2017;
    List<String> dt28= new ArrayList<>();
    List<String> dt29= new ArrayList<>();
    List<String> dt30= new ArrayList<>();
    List<String> dt31= new ArrayList<>();
    List<String> dt= new ArrayList<>();
    List<String> m= new ArrayList<>();
    List<String> y= new ArrayList<>();


    private List<Data_Model> state_data=new ArrayList<>();
    private List<String> state=new ArrayList<>();

    private List<District_Data_Model> district_data =new ArrayList<>();
    private List<String>  distri=new ArrayList<>();

    private List<Blocks_Data_Model> block_data=new ArrayList<>();
    private List<String> blocs=new ArrayList<>();


    private String st="",dst="",blk="";
    private boolean comeFromContentView=false;


    private int selectState=0;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_profile);
        validateLicense();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN|WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().setBackgroundDrawableResource(R.drawable.splash_back);
        if(getIntent().getExtras()!=null)
        {
            if(getIntent().hasExtra(ContentView.CONTENT_ID_FORM_CONTENT_VIEW))
            {
                comeFromContentView=true;
            }
        }
        initializeView();
    }

    /*Validate Revere Licence*/
    public void validateLicense()
    {
        if(ConnectionDetector.isInternetAvailable(Profile.this))
        {
            initialiseRevereSDKS();
        }
        else
        {
            Alert.showToast(Profile.this,getString(R.string.reveri_not_initial));
        }
    }

    private void initializeView()
    {
        radioSex=(RadioGroup)findViewById(R.id.radioSex);
        home = (ImageView) findViewById(R.id.home);
        title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.back));
        submit_card_view = (CardView) findViewById(R.id.submit_card_view);
        input_layout_firstName = (TextInputLayout) findViewById(R.id.input_layout_firstName);
        input_layout_last_name = (TextInputLayout) findViewById(R.id.input_layout_last_name);
        input_firstName = (RevEditText) findViewById(R.id.input_firstName);
        input_last_name = (RevEditText) findViewById(R.id.input_last_name);
        referal_code=(RevEditText)findViewById(R.id.referal_code);
        spinner_state_layout = (TextInputLayout) findViewById(R.id.spinner_state_layout);
        district_layout_firstName = (TextInputLayout) findViewById(R.id.district_layout_firstName);
        block_layout = (TextInputLayout) findViewById(R.id.block_layout);
        spinner_state = (AutoCompleteTextView) findViewById(R.id.spinner_state);
        district = (AutoCompleteTextView) findViewById(R.id.district);
        input_block = (AutoCompleteTextView) findViewById(R.id.input_block);


        if(SharedPreference.retrieveData(Profile.this,Constants.REFERRAL)!=null)
        {
            referal_code.setText(SharedPreference.retrieveData(Profile.this,Constants.REFERRAL));
            referal_code.setEnabled(false);
            RevSDK.destroyKeypad();
        }

        ddd= (AutoCompleteTextView) findViewById(R.id.ddd);
        mmm= (AutoCompleteTextView) findViewById(R.id.mmm);
        yyyy= (AutoCompleteTextView) findViewById(R.id.yyy);
        ddd.setFocusable(false);
        ddd.setFocusableInTouchMode(false);
        mmm.setFocusable(false);
        mmm.setFocusableInTouchMode(false);
        yyyy.setFocusable(false);
        yyyy.setFocusableInTouchMode(false);

        state_drop_arrow=(ImageView)findViewById(R.id.state_drop_arrow);
       // setCalender();

        this.submit_card_view.setBackgroundResource(R.drawable.button_radious);
        this.submit_card_view.setOnClickListener(this);
        this.home.setOnClickListener(this);

        setStateData();
        this.spinner_state.setAdapter(new ArrayAdapter(this, R.layout.drop_item, state));
        this.spinner_state.setKeyListener(null);
        this.spinner_state.setOnTouchListener(this);
        state_lay=(RelativeLayout)findViewById(R.id.state_lay);
        state_lay.setOnTouchListener(this);
        spinner_state.setFocusable(false);
        spinner_state.setFocusableInTouchMode(false);

        //this.district.setAdapter(new ArrayAdapter(this, R.layout.drop_item, getResources().getStringArray(R.array.city_array)));
        this.district.setKeyListener(null);
        this.district.setOnTouchListener(this);
        district.setFocusable(false);
        district.setFocusableInTouchMode(false);

      //  this.input_block.setAdapter(new ArrayAdapter(this, R.layout.drop_item, getResources().getStringArray(R.array.block_array)));
        this.input_block.setKeyListener(null);
        this.input_block.setOnTouchListener(this);
        input_block.setFocusable(false);
        input_block.setFocusableInTouchMode(false);

        input_firstName.setOnTouchListener(this);
        input_last_name.setOnTouchListener(this);
        state_drop_arrow.setOnTouchListener(this);

        spinner_state.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(v.getWindowToken(), 0);
                spinner_state.showDropDown();
            }
        });

        district.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(v.getWindowToken(), 0);
                if(!spinner_state.getText().toString().toString().isEmpty())
                {
                    district.showDropDown();
                }
            }
        });

        input_block.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(v.getWindowToken(), 0);
                if(!district.getText().toString().toString().isEmpty())
                {
                    input_block.showDropDown();
                }
            }
        });
        setItemClick();
        ddd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(v.getWindowToken(), 0);
                ddd.showDropDown();
            }
        });

        mmm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(v.getWindowToken(), 0);
                mmm.showDropDown();
            }
        });

        yyyy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(v.getWindowToken(), 0);
                yyyy.showDropDown();
            }
        });


        input_firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SharedPreference.storeData(Profile.this,Constants.FIRST_NAME,input_firstName.getText().toString().trim());
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        input_last_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SharedPreference.storeData(Profile.this,Constants.LAST_NAME,input_last_name.getText().toString().trim());
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        referal_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SharedPreference.storeData(Profile.this,Constants.USER_REF_CODE,referal_code.getText().toString().trim());
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        radioSex.setOnCheckedChangeListener(new  RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1)
                {
                    switch (checkedId)
                    {
                        case R.id.radioFemale:
                            SharedPreference.storeData(Profile.this,Constants.GENDER,"female");
                            break;
                        case R.id.radioMale:
                            SharedPreference.storeData(Profile.this,Constants.GENDER,"male");
                            break;
                    }
                }
            }
        });

        setInitialData();
    }

    /*Set Initial Data*/
    private void setInitialData()
    {
        if(SharedPreference.retrieveData(Profile.this,Constants.FIRST_NAME)!=null)
        {
            if(SharedPreference.retrieveData(Profile.this,Constants.FIRST_NAME).trim().length()>0)
            {
                input_firstName.setText(SharedPreference.retrieveData(Profile.this,Constants.FIRST_NAME));
            }
        }

        if(SharedPreference.retrieveData(Profile.this,Constants.LAST_NAME)!=null)
        {
            if(SharedPreference.retrieveData(Profile.this,Constants.LAST_NAME).trim().length()>0)
            {
                input_last_name.setText(SharedPreference.retrieveData(Profile.this,Constants.LAST_NAME));
            }
        }

        if(SharedPreference.retrieveData(Profile.this,Constants.STATE_REGIONAL)!=null)
        {
            if(SharedPreference.retrieveData(Profile.this,Constants.STATE_REGIONAL).trim().length()>0)
            {
                spinner_state.setText(SharedPreference.retrieveData(Profile.this,Constants.STATE_REGIONAL));
                st=SharedPreference.retrieveData(Profile.this,Constants.STATE);
                selectState=1;
            }
        }

        if(SharedPreference.retrieveData(Profile.this,Constants.DISTRICT_REGIONAL)!=null)
        {
            if(SharedPreference.retrieveData(Profile.this,Constants.DISTRICT_REGIONAL).trim().length()>0)
            {
                district.setText(SharedPreference.retrieveData(Profile.this,Constants.DISTRICT_REGIONAL));
                dst=SharedPreference.retrieveData(Profile.this,Constants.DISTRICT);
            }
        }

        if(SharedPreference.retrieveData(Profile.this,Constants.BLOCK_REGIONAL)!=null)
        {
            if(SharedPreference.retrieveData(Profile.this,Constants.BLOCK_REGIONAL).trim().length()>0)
            {
                input_block.setText(SharedPreference.retrieveData(Profile.this,Constants.BLOCK_REGIONAL));
                blk=SharedPreference.retrieveData(Profile.this,Constants.BLOCK);
            }
        }

        setCalender();
        if(SharedPreference.retrieveData(Profile.this,Constants.YY)!=null)
        {
            if(SharedPreference.retrieveData(Profile.this,Constants.YY).trim().length()>0)
            {
                yyyy.setText(SharedPreference.retrieveData(Profile.this,Constants.YY));
            }
        }

        if(SharedPreference.retrieveData(Profile.this,Constants.MM)!=null)
        {
            if(SharedPreference.retrieveData(Profile.this,Constants.MM).trim().length()>0)
            {
                mmm.setText(SharedPreference.retrieveData(Profile.this,Constants.MM));
            }
        }

        if(SharedPreference.retrieveData(Profile.this,Constants.DD)!=null)
        {
            if(SharedPreference.retrieveData(Profile.this,Constants.DD).trim().length()>0)
            {
                ddd.setText(SharedPreference.retrieveData(Profile.this,Constants.DD));
            }
        }

        if(SharedPreference.retrieveData(Profile.this,Constants.USER_REF_CODE)!=null)
        {
            if(SharedPreference.retrieveData(Profile.this,Constants.USER_REF_CODE).trim().length()>0)
            {
                referal_code.setText(SharedPreference.retrieveData(Profile.this,Constants.USER_REF_CODE));
            }
        }

        if(SharedPreference.retrieveData(Profile.this,Constants.GENDER)!=null)
        {
            if(SharedPreference.retrieveData(Profile.this,Constants.GENDER).trim().length()>0)
            {
                RadioButton radioFemale = (RadioButton) radioSex.findViewById(R.id.radioFemale);
                RadioButton radioMale = (RadioButton) radioSex.findViewById(R.id.radioMale);
                if(SharedPreference.retrieveData(Profile.this,Constants.GENDER).equalsIgnoreCase("female"))
                {
                    radioFemale.setChecked(true);
                }
                else
                {
                    radioMale.setChecked(true);
                }
            }
        }

        if(SharedPreference.retrieveData(Profile.this,Constants.STATE_POSITION_IN_SPINNER)!=null) {
            getAllDistrict(state_data.get(Integer.parseInt(SharedPreference.retrieveData(Profile.this, Constants.STATE_POSITION_IN_SPINNER))));
        }
        if(SharedPreference.retrieveData(Profile.this,Constants.DISTRICT_POSITION_IN_SPINNER)!=null) {
            getAllBlocks(district_data.get(Integer.parseInt(SharedPreference.retrieveData(Profile.this, Constants.DISTRICT_POSITION_IN_SPINNER))));
        }
        setCalender();
    }


    /*Set Calendar For Date Of Birth*/
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


        ddd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreference.storeData(Profile.this,Constants.DD,ddd.getText().toString());
            }
        });

        mmm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mo=Integer.parseInt(((TextView)view).getText().toString());
                setDay(getDayInaMonth(yer, mo));

                SharedPreference.storeData(Profile.this,Constants.MM,mmm.getText().toString());
            }
        });

        yyyy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                yer=Integer.parseInt(((TextView)view).getText().toString());
                setDay(getDayInaMonth(yer, mo));

                SharedPreference.storeData(Profile.this,Constants.YY,yyyy.getText().toString());
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

                    SharedPreference.storeData(Profile.this,Constants.DD,ddd.getText().toString());
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
    public void onBackPressed() {
        if(comeFromContentView)
        {
            cancelResultForContentView();
        }
        else
        {
            SharedPreference.removeKey(Profile.this, Constants.OTP_VERIFIED);
            SharedPreference.removeKey(Profile.this, Constants.SignUpTempFname);
            SharedPreference.removeKey(Profile.this, Constants.SignUpTempLname);
            SharedPreference.removeKey(Profile.this, Constants.SignUpTempMNum);
            finish();
            startActivity(new Intent(this, SignUp.class));
        }
    }

    private boolean checkValidation()
    {
        String f_name = input_firstName.getText().toString();
        String l_name = input_last_name.getText().toString();
        if (f_name.trim().length() < 2) {
            input_firstName.requestFocus();
            Alert.alertDialog(Profile.this, getString(R.string.enter_first_name), getString(R.string.sorry), false);
            return false;
        } else if (l_name.trim().length() < 2) {
            input_last_name.requestFocus();
            Alert.alertDialog(Profile.this, getString(R.string.enter_last_name), getString(R.string.sorry), false);
            return false;
        }
        else if(spinner_state.getText().toString().trim().isEmpty())
        {
            Alert.showToast(Profile.this,getString(R.string.select_your_state));
            return false;
        }
        else if(district.getText().toString().trim().isEmpty())
        {
            Alert.showToast(Profile.this,getString(R.string.select_your_district));
            return false;
        }
        else if(input_block.getText().toString().trim().isEmpty())
        {
            Alert.showToast(Profile.this,getString(R.string.select_your_block));
            return false;
        }
        else if(ddd.getText().toString().trim().isEmpty() || mmm.getText().toString().trim().isEmpty() || yyyy.getText().toString().trim().isEmpty())
        {
            Alert.showToast(Profile.this,getString(R.string.select_your_date_of_birth));
            return false;
        }
        return true;
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        Calendar c = Calendar.getInstance();
        DatePickerDialog dpd = new DatePickerDialog(Profile.this,R.style.DatePickerDialogTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int yr, int monthOfYear, int dayOfMonth) {
                        year=Calendar.getInstance().get(Calendar.YEAR);
                        if(year < yr|| yr > year-10)
                        {
                            Alert.alertDialog(Profile.this,getString(R.string.sel_cor_dob),getString(R.string.sorry),false);
                            return;
                        }
                        // TODO Auto-generated method stub
                        showDate(yr, monthOfYear+1, dayOfMonth);
                    }
                }, c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DATE));
        dpd.show();
    }

    private void showDate(int year, int month, int day)
    {
        String d=String.valueOf(day);
        String m=String.valueOf(month);
        if(month<10)
        {
            m="0"+m;
        }

        if(day<10)
        {
            d="0"+d;
        }
       // dd.setText(d);
       // mm.setText(m);
       // yy.setText(String.valueOf(year));
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home:
                onBackPressed();
                return;
            case R.id.submit_card_view:
                if(checkValidation())
                {
                  //  RevSDK.destroyKeypad();
                    try
                    {
                        String gen="male";
                        int selectedId = radioSex.getCheckedRadioButtonId();
                        if(selectedId==R.id.radioFemale)
                        {
                            gen="female";
                        }
                        JSONObject peram =new JSONObject();
                        peram.put("firstName",input_firstName.getText().toString().trim());
                        peram.put("lastName",input_last_name.getText().toString().trim());
                        peram.put("mobileNumber",SharedPreference.retrieveData(Profile.this, Constants.SignUpTempMNum));
                        peram.put("platform",Constants.PLATFORM);
                        peram.put("deviceId", Constants.getUniqueId(Profile.this));
                        peram.put("state", st);
                        peram.put("district",dst);
                        peram.put("block", blk);
                        peram.put("stateRegional", spinner_state.getText().toString());
                        peram.put("districtRegional",district.getText().toString());
                        peram.put("blockRegional", input_block.getText().toString());
                        peram.put("dateOfBirth", ddd.getText().toString().trim()+"-"+mmm.getText().toString().trim()+"-"+yyyy.getText().toString().trim());
                        peram.put("gender", gen);
                        peram.put("inviteCode", referal_code.getText().toString().replace(" ","").trim());
                        peram.put("language", getAppLanguage());
                        new Retrofit2(this,this,REGISTER,Constants.REGISTER,peram).callService(true);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return;
            default:
                return;
        }
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        ((TextView) view).setTextColor(-1);
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId())
        {
            case R.id.input_firstName:
                RevSDK.initKeypad(Profile.this,SharedPreference.retrieveLang(Profile.this));
                break;
            case R.id.input_last_name:
                RevSDK.initKeypad(Profile.this,SharedPreference.retrieveLang(Profile.this));
                break;
        }
        return false;
    }

    @Override
    public void onServiceResponse(int i, Response<ResponseBody> response) {
        try
        {
            JSONObject object=new JSONObject(response.body().string());
            switch (i)
            {
                case REGISTER:
                    SharedPreference.removeKey(Profile.this, Constants.OTP_VERIFIED);
                    SharedPreference.removeKey(Profile.this, Constants.SignUpTempFname);
                    SharedPreference.removeKey(Profile.this, Constants.SignUpTempLname);
                    SharedPreference.removeKey(Profile.this, Constants.SignUpTempMNum);
                    if(object.getString("success").equalsIgnoreCase("true"))
                    {
                        JSONObject response1=object.getJSONObject("response");

                        if(SharedPreference.retrieveData(Profile.this,Constants.FIRST_NAME)!=null)
                        {
                            SharedPreference.removeKey(Profile.this,Constants.FIRST_NAME);
                            SharedPreference.removeKey(Profile.this,Constants.LAST_NAME);
                            SharedPreference.removeKey(Profile.this,Constants.MOBILE_NUMBER);
                            SharedPreference.removeKey(Profile.this,Constants.STATE);
                            SharedPreference.removeKey(Profile.this,Constants.DISTRICT);
                            SharedPreference.removeKey(Profile.this,Constants.BLOCK);
                            SharedPreference.removeKey(Profile.this,Constants.GENDER);
                            SharedPreference.removeKey(Profile.this,Constants.DOB);
                            SharedPreference.removeKey(Profile.this,Constants.USER_REF_CODE);

                            SharedPreference.removeKey(Profile.this,Constants.YY);
                            SharedPreference.removeKey(Profile.this,Constants.MM);
                            SharedPreference.removeKey(Profile.this,Constants.DD);
                            SharedPreference.removeKey(Profile.this,Constants.STATE_POSITION_IN_SPINNER);
                            SharedPreference.removeKey(Profile.this,Constants.DISTRICT_POSITION_IN_SPINNER);
                        }

                        SharedPreference.storeData(Profile.this,Constants.USER_ID,response1.getString("_id"));
                        SharedPreference.storeData(Profile.this,Constants.FIRST_NAME,response1.getString("firstName"));
                        SharedPreference.storeData(Profile.this,Constants.LAST_NAME,response1.getString("lastName"));
                        SharedPreference.storeData(Profile.this,Constants.MOBILE_NUMBER,response1.getString("mobileNumber"));
                        SharedPreference.storeData(Profile.this,Constants.STATE,response1.getString("state"));
                        SharedPreference.storeData(Profile.this,Constants.DISTRICT,response1.getString("district"));
                        SharedPreference.storeData(Profile.this,Constants.BLOCK,response1.getString("block"));
                        SharedPreference.storeData(Profile.this,Constants.GENDER,response1.getString("gender"));
                        SharedPreference.storeData(Profile.this,Constants.DOB,response1.getString("dateOfBirth"));
                        SharedPreference.storeData(Profile.this,Constants.USER_IMAGE,response1.getString("userImage"));
                        SharedPreference.storeData(Profile.this,Constants.USER_REF_CODE,response1.getString("referralCode"));
                        if(response1.has("stateRegional")) {
                            SharedPreference.storeData(Profile.this, Constants.STATE_REGIONAL, response1.getString("stateRegional"));
                            SharedPreference.storeData(Profile.this, Constants.DISTRICT_REGIONAL, response1.getString("districtRegional"));
                            SharedPreference.storeData(Profile.this, Constants.BLOCK_REGIONAL, response1.getString("blockRegional"));
                        }

                        if(response1.has("totalSubmissions"))
                        {
                            SharedPreference.storeData(Profile.this,Constants.USER_YOGDAN,response1.getString("totalSubmissions"));
                        }

                        if(response1.has("totalPublications"))
                        {
                            SharedPreference.storeData(Profile.this,Constants.USER_LEKH,response1.getString("totalPublications"));
                        }
                    }
                    if(comeFromContentView)
                    {
                        Intent intent = new Intent();
                        intent.putExtra(ContentView.CONTENT_ID_FORM_CONTENT_VIEW, getIntent().getStringExtra(ContentView.CONTENT_ID_FORM_CONTENT_VIEW));
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                    else
                    {
                        startActivity(new Intent(this, Home.class).putExtra("COME","APP").setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void initialiseRevereSDKS() {
       // new AsyncCaller().execute();
    }

    private class AsyncCaller extends AsyncTask<Void, Void, Boolean> {
        boolean status = false;
         private AlertDialog alert;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                   alert = Alert.startDialog(Profile.this);
            } catch (Exception e) {
                Alert.showLog("Exception", "Exception- " + e.toString());
            }
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            final int selectedLangId = SharedPreference.retrieveLang(Profile.this);
            Alert.showLog("TAG", "selectedLangId = " + selectedLangId);
            RevSDK.initLangResources("https://api-gw.revup.reverieinc.com/apiman-gateway/ReverieMobilitySdk", selectedLangId, new LangResourceInitCompleteListener() {
                public void onLangResourceInitComplete(int i, RevStatus revStatus) {
                    Alert.showLog("TAG", "INIT RESOURCE COMPLETE:  Lang code = " + i + " , Status = " + revStatus.getStatusMessage());
                    if (revStatus.getStatusCode() == RevStatus.SUCCESS) {
                        try {
                            // status = RevSDK.initKeypad(ContentView.this, selectedLangId);
                            Alert.showLog("TAG", "status = " + status);
                        }
                        catch (Exception e)
                        {
                            Alert.showLog("DO IN BAck", "Exception = " + e.toString());
                        }
                    }
                }
            });
            return status;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            Alert.showLog("TAG", "INIT RESOURCE COMPLETE: " + aBoolean);
            if (!aBoolean)
            {
                final int selectedLangId = SharedPreference.retrieveLang(Profile.this);
                RevSDK.initLangResources("https://api-gw.revup.reverieinc.com/apiman-gateway/ReverieMobilitySdk", selectedLangId, new LangResourceInitCompleteListener() {
                    public void onLangResourceInitComplete(int i, RevStatus revStatus) {
                        Alert.showLog("TAG", "INIT RESOURCE COMPLETE:  Lang code = " + i + " , Status = " + revStatus.getStatusMessage());
                        if (revStatus.getStatusCode() == RevStatus.SUCCESS) {
                            Alert.showLog("TAG", "status = " + status);
                            if (alert != null) {
                                alert.dismiss();
                            }
                        }
                    }
                });
                if (alert != null)
                {
                    alert.dismiss();
                }
            }
            else
            {
                if (alert != null) {
                    alert.dismiss();
                }
            }
            super.onPostExecute(aBoolean);
        }
    }

    /*Function Related To State, District, Block*/
    private void setItemClick()
    {
        spinner_state.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(selectState==0)
                {
                    st=state_data.get(position).getState_name_english();
                    dst="";
                    blk="";
                    district.setText("");
                    input_block.setText("");
                    getAllDistrict(state_data.get(position));

                    SharedPreference.storeData(Profile.this,Constants.STATE_REGIONAL,spinner_state.getText().toString().trim());
                    SharedPreference.storeData(Profile.this,Constants.STATE,st);
                    SharedPreference.storeData(Profile.this,Constants.STATE_POSITION_IN_SPINNER,String.valueOf(position));
                }
                selectState=1;
            }
        });

        district.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
            {
                dst=district_data.get(position).getDistrict_name_english();
                blk="";
                input_block.setText("");
                getAllBlocks(district_data.get(position));

                SharedPreference.storeData(Profile.this,Constants.DISTRICT_REGIONAL,district.getText().toString().trim());
                SharedPreference.storeData(Profile.this,Constants.DISTRICT,dst);
                SharedPreference.storeData(Profile.this,Constants.DISTRICT_POSITION_IN_SPINNER,String.valueOf(position));
            }
        });

        input_block.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                blk=block_data.get(position).getBlock_name_english();


                SharedPreference.storeData(Profile.this,Constants.BLOCK_REGIONAL,input_block.getText().toString().trim());
                SharedPreference.storeData(Profile.this,Constants.BLOCK,blk);
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
        district.setAdapter(new ArrayAdapter(this, R.layout.drop_item,distri));
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
        input_block.setAdapter(new ArrayAdapter(this, R.layout.drop_item, blocs));
    }

    private void setStateData()
    {
        state_data.clear();
        state.clear();
        state_data= Controller.getState_data();
       // spinner_state.setText(state_data.get(0).getState_name_regional());
       // spinner_state.setHint(state_data.get(0).getState_name_english());
        for (Data_Model st:state_data)
        {
            state.add(st.getState_name_regional());
        }
    }

    private void cancelResultForContentView()
    {
        Intent intent = new Intent();
        intent.putExtra(ContentView.CONTENT_ID_FORM_CONTENT_VIEW, getIntent().getStringExtra(ContentView.CONTENT_ID_FORM_CONTENT_VIEW));
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

    private String getAppLanguage()
    {
        String language="Marathi";
        if(SharedPreference.retrieveLang(Profile.this)!=0)
        {
            language= KeyboardCredentialConstants.langNamesArray[SharedPreference.retrieveLang(Profile.this)];
        }
        return language;
    }
}
