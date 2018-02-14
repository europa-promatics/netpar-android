package co.netpar.Notification;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import com.github.arturogutierrez.Badges;
import com.github.arturogutierrez.BadgesNotSupportedException;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.List;
import java.util.Map;
import co.netpar.Comman.Alert;
import co.netpar.Comman.Constants;
import co.netpar.Home;
import co.netpar.R;
import co.netpar.Syncronization.NetparDataBase.SharedPreference;

/**
 * Created by promatics on 12/1/2017.
 */


public class MyFirebaseMessagingService extends FirebaseMessagingService
{
    private static final String TAG = "FCM";
    private Intent intent;
    public static int count=0;

    /*------- Push Notification Received  --------*/
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
      /*  for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Alert.showLog(key + " : ", value);
        }*/
        count++;
        try
        {
            Badges.setBadge(getApplicationContext(), count);
        } catch (BadgesNotSupportedException badgesNotSupportedException)
        {
            Alert.showLog(TAG, badgesNotSupportedException.getMessage());
        }
        setNotificationCount();
        if(!checkActivation())
        {
            sendNotification(remoteMessage.getData().get("message"));
        }
    }

    @Override
    public void handleIntent(Intent intent) {
        try
        {
            if (intent.getExtras() != null)
            {
                RemoteMessage.Builder builder = new RemoteMessage.Builder("MyFirebaseMessagingService");
                for (String key : intent.getExtras().keySet())
                {
                    builder.addData(key, intent.getExtras().get(key).toString());
                }
                onMessageReceived(builder.build());
            }
            else
            {
                super.handleIntent(intent);
            }
        }
        catch (Exception e)
        {
            super.handleIntent(intent);
        }
    }

    /*------- Set Unread Notification Count --------*/
    private void setNotificationCount()
    {
        if(SharedPreference.retrieveData(this,Constants.NCOUNT)==null)
        {
            SharedPreference.storeData(this,Constants.NCOUNT,"1");
        }
        else
        {
            int ct=Integer.parseInt(SharedPreference.retrieveData(this,Constants.NCOUNT));
            SharedPreference.storeData(this,Constants.NCOUNT,String.valueOf(ct+1));
        }
        Intent msgrcv = new Intent("connected");
        msgrcv.putExtra("NOTIFICATION","NOTIFICATION");
        LocalBroadcastManager.getInstance(this).sendBroadcast(msgrcv);
    }

    private void sendNotification(String messageBody)
    {
        intent = new Intent(this, Home.class).putExtra("NOTIFICATION","come");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder mBuilder = new NotificationCompat
                .Builder(this);
        Notification notification = mBuilder
                .setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(getString(R.string.app_name))
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(getNotificationIcon(mBuilder))
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentText(messageBody)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

    private static int getNotificationIcon(NotificationCompat.Builder notificationBuilder)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            int color = 0x008000;
            notificationBuilder.setColor(color);
            return R.drawable.notif_logo;
        } else {
            return R.drawable.notif_logo;
        }
    }

    /*----------- Check state of application --------*/
    public boolean checkActivation()
    {
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> services = activityManager
                .getRunningTasks(Integer.MAX_VALUE);
        boolean isActivityFound = false;

        if (services.get(0).topActivity.getPackageName()
                .equalsIgnoreCase(getApplicationContext().getPackageName()))
        {
            isActivityFound = true;
        }
        return isActivityFound;
    }

}
