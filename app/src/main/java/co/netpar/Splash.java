package co.netpar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.reverie.manager.LangResourceInitCompleteListener;
import com.reverie.manager.RevSDK;
import com.reverie.manager.RevStatus;
import co.netpar.Comman.Alert;
import co.netpar.Syncronization.NetparDataBase.SharedPreference;


public class Splash extends AppCompatActivity implements AnimationListener,OnClickListener {
    private Animation arrow_regular;
    private Animation center_top;
    private Animation left_to_place;
    private Animation left_to_place1;
    private ImageView logo;
    private ImageView mainlogo;
    private CardView sign_in_card_view;
    private CardView sign_up_card_view;
    private ImageView signin_arrow;
    private ImageView signup_arrow;
    private Animation small_place_top;
    private TextView take_tour;
    private TextView wel_txt;
    private Animation zoomin;
    private boolean login_click;
    private Bundle data;
    private boolean keyboardHasInitialised=false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_splash);
        getWindow().setBackgroundDrawableResource(R.drawable.splash_back);
        initializeView();
    }

    /*Use to initialize views and set data*/
    private void initializeView() {
        this.take_tour = (TextView) findViewById(R.id.take_tour);
        this.wel_txt = (TextView) findViewById(R.id.wel_txt);
        this.mainlogo = (ImageView) findViewById(R.id.mainlogo);
        this.logo = (ImageView) findViewById(R.id.logo);
        this.sign_in_card_view = (CardView) findViewById(R.id.sign_in_card_view);
        this.sign_up_card_view = (CardView) findViewById(R.id.sign_up_card_view);
        this.sign_in_card_view.setBackgroundResource(R.drawable.gradient_back_sign_in);
        this.sign_up_card_view.setBackgroundResource(R.drawable.gradient_back_signup);
        this.signin_arrow = (ImageView) findViewById(R.id.signin_arrow);
        this.signup_arrow = (ImageView) findViewById(R.id.signup_arrow);
        setListner();
    }

    /*Use to set Listeners*/
    private void setListner() {
        this.sign_in_card_view.setOnClickListener(this);
        this.sign_up_card_view.setOnClickListener(this);
        this.take_tour.setOnClickListener(this);

        data=getIntent().getExtras();
        if(data!=null)
        {
            zoomin = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
            center_top = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.centar_top);
            left_to_place = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_to_place);
            left_to_place1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_to_place1);
            small_place_top = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.small_place_top);
            arrow_regular = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.arrow_anim);
            mainlogo.startAnimation(this.zoomin);

            zoomin.setAnimationListener(this);
            center_top.setAnimationListener(this);
            left_to_place.setAnimationListener(this);
            left_to_place1.setAnimationListener(this);
            small_place_top.setAnimationListener(this);
        }
        else
        {
            mainlogo.setVisibility(View.GONE);
           // take_tour.setVisibility(View.VISIBLE);
            wel_txt.setVisibility(View.VISIBLE);
            logo.setVisibility(View.VISIBLE);
            sign_in_card_view.setVisibility(View.VISIBLE);
            sign_up_card_view.setVisibility(View.VISIBLE);
            signin_arrow.setVisibility(View.VISIBLE);
            signup_arrow.setVisibility(View.VISIBLE);

            arrow_regular = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.arrow_anim);
            signin_arrow.startAnimation(arrow_regular);
            signup_arrow.startAnimation(arrow_regular);
        }
    }

    public void onAnimationStart(Animation animation) {
        if (animation == this.small_place_top) {
            this.wel_txt.setAlpha(0.2f);
            this.take_tour.setAlpha(0.2f);
        }
    }

    public void onAnimationEnd(Animation animation) {
        if (animation == this.zoomin) {
            this.mainlogo.startAnimation(this.center_top);
        } else if (animation == this.center_top) {
            this.mainlogo.setVisibility(View.GONE);
            this.logo.setVisibility(View.VISIBLE);
            this.take_tour.startAnimation(this.small_place_top);
            this.wel_txt.startAnimation(this.small_place_top);
            this.wel_txt.setAlpha(0.0f);
            this.take_tour.setAlpha(0.0f);
          //  this.take_tour.setVisibility(View.VISIBLE);
            this.wel_txt.setVisibility(View.VISIBLE);
            this.sign_in_card_view.startAnimation(this.left_to_place);
            this.sign_in_card_view.setVisibility(View.VISIBLE);
            this.sign_up_card_view.startAnimation(this.left_to_place1);
            this.sign_up_card_view.setVisibility(View.VISIBLE);
        } else if (animation == this.left_to_place) {
            this.wel_txt.setAlpha(0.4f);
            this.take_tour.setAlpha(0.4f);
            this.signin_arrow.startAnimation(this.arrow_regular);
        } else if (animation == this.left_to_place1) {
            this.wel_txt.setAlpha(0.7f);
            this.take_tour.setAlpha(0.7f);
            this.signup_arrow.startAnimation(this.arrow_regular);
        } else if (animation == this.small_place_top) {
            this.wel_txt.setAlpha(1.0f);
            this.take_tour.setAlpha(1.0f);
        }
    }

    public void onAnimationRepeat(Animation animation) {
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_card_view:
                login_click=true;

                /*Come From App.Controller and has already click first time on any one*/
                //if(data!=null && !keyboardHasInitialised)
                  //  new AsyncCaller().execute();
              //else
                    switchActivity();
                return;
            case R.id.sign_up_card_view:
                login_click=false;

                /*Come From App.Controller and has already click first time on any one*/
                //if(data!=null && !keyboardHasInitialised)
                    //new AsyncCaller().execute();
               // else
                    switchActivity();
                return;
            default:
                return;
        }
    }

    private void switchActivity()
    {
        keyboardHasInitialised=true;//Keyboard has initialised
        if(login_click)
        {
            Alert.loginOption(Splash.this,false,false,null);
        }
        else
        {
            startActivity(new Intent(this, SignUp.class));
        }
    }

    private class AsyncCaller extends AsyncTask<String, Boolean, Boolean> {
        boolean status = false;
        private AlertDialog alert;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                alert=Alert.startDialog(Splash.this);
            }
            catch (Exception e)
            {
                Alert.showLog("Exception","Exception- "+ e.toString());
            }
        }
        @Override
        protected Boolean doInBackground(String... args) {
            final int selectedLangId = SharedPreference.retrieveLang(Splash.this);
            RevSDK.initLangResources("https://api-gw.revup.reverieinc.com/apiman-gateway/ReverieMobilitySdk", selectedLangId, new LangResourceInitCompleteListener() {
                public void onLangResourceInitComplete(int i, RevStatus revStatus) {
                    if (revStatus.getStatusCode() == RevStatus.SUCCESS) {
                        // Control pass to main thread
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
                    alert.dismiss();
                }
                switchActivity();
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // Do nothing now
        }
    }


}
