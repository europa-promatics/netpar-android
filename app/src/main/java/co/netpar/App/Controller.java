package co.netpar.App;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;

import io.fabric.sdk.android.Fabric;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import co.netpar.Comman.AppVisibilityDetector;
import co.netpar.Comman.Constants;
import co.netpar.Home;
import co.netpar.Model.Data_Model;
import co.netpar.Profile;
import co.netpar.R;
import co.netpar.Retrofit.Retrofit2;
import co.netpar.Retrofit.ServiceResponse;
import co.netpar.Splash;
import co.netpar.StartScreen;
import co.netpar.Syncronization.NetparDataBase.SharedPreference;
import co.netpar.Syncronization.StateData;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static android.util.Log.VERBOSE;

public class Controller extends Application
{
    public static String audio_path = null,
            images_path = null,
            video_path = null,
            other_path = null;

    public static final String APP_NAME="Netpar";

    private static Controller mInstance;

    public static List<Data_Model> state_data=new ArrayList<>();

    public static List<Data_Model> getState_data() {
        return state_data;
    }

    public void onCreate()
    {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        mInstance=this;
        new FlurryAgent.Builder()
                .withLogEnabled(true)
                .withCaptureUncaughtExceptions(true)
                .withContinueSessionMillis(10)
                .withLogLevel(VERBOSE)
                .build(this, "22GNQ62SV5MJJ2N42YZM");

            AnalyticsTrackers.initialize(this);
            AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);
            makeDirectory();
        ImagePipelineConfig imagePipelineConfig = ImagePipelineConfig.newBuilder(getApplicationContext())
                .setDownsampleEnabled(true)
                .build();
        Fresco.initialize(this,imagePipelineConfig);
            FacebookSdk.sdkInitialize(getApplicationContext());

        state_data= StateData.getAllState(getApplicationContext());
    }

    private void notif()
    {
        NotificationCompat.Builder mBuilder = new NotificationCompat
                .Builder(this);
        Notification notification = mBuilder
                .setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(getString(R.string.app_name))
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Netpar’s power consumption in the background is high"))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.drawable.notif_logo)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentText("Netpar’s power consumption in the background is high")
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

    public static synchronized Controller getInstance() {
        return mInstance;
    }

    public synchronized Tracker getGoogleAnalyticsTracker() {
        AnalyticsTrackers analyticsTrackers = AnalyticsTrackers.getInstance();
        return analyticsTrackers.get(AnalyticsTrackers.Target.APP);
    }

    /***
     * Tracking screen view
     *
     * @param screenName screen name to be displayed on GA dashboard
     */
    public void trackScreenView(String screenName) {
        Tracker t = getGoogleAnalyticsTracker();
        // Set screen name.
        t.setScreenName(screenName);
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        GoogleAnalytics.getInstance(this).dispatchLocalHits();
    }

    /***
     * Tracking exception
     */
    public void trackException(Exception e) {
        if (e != null) {
            Tracker t = getGoogleAnalyticsTracker();

            t.send(new HitBuilders.ExceptionBuilder()
                    .setDescription(
                            new StandardExceptionParser(this, null)
                                    .getDescription(Thread.currentThread().getName(), e))
                    .setFatal(false)
                    .build()
            );
        }
    }

    /***
     * Tracking event
     */
    public void trackEvent(String category, String action, String label,String id)
    {
        try {
            Tracker t = getGoogleAnalyticsTracker();
            t.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).setValue(Long.parseLong(id)).build());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /***
     * Aake directories
     */
    private void makeDirectory()
    {
        File directory = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            directory = new File(Environment.getExternalStorageDirectory() + File.separator + APP_NAME);
            if (!directory.exists())
                directory.mkdirs();
        }
        else
        {
            directory = getApplicationContext().getDir(APP_NAME, Context.MODE_PRIVATE);
            if (!directory.exists())
                directory.mkdirs();
        }

        if (directory != null)
        {
            File audio = new File(directory + File.separator + "Audio");
            File images = new File(directory + File.separator + "Image");
            File video = new File(directory + File.separator + "Video");
            File others = new File(directory + File.separator + "Others");

            if (!audio.exists())
                audio.mkdirs();

            if (!images.exists())
                images.mkdirs();

            if (!video.exists())
                video.mkdirs();

            if (!others.exists())
                others.mkdirs();

            audio_path = directory + File.separator + "Audio";
            images_path = directory + File.separator + "Image";
            video_path = directory + File.separator + "Video";
            other_path = directory + File.separator + "Others";
        }
    }

    public static String getAudio_path() {
        return audio_path;
    }

    public static String getImages_path() {
        return images_path;
    }

    public static String getVideo_path() {
        return video_path;
    }

    public static String getOther_path() {
        return other_path;
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /*Used to manage session if language has selected than switch to Splash And if already login than switch to dashboard*/
    private void setScreenAccordingToSession()
    {
        /*Add on verify otp sign up process and remove on Profile after successful registration*/
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
}
