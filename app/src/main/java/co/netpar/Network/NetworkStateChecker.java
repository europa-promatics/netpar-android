package co.netpar.Network;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import co.netpar.Comman.Alert;

public class NetworkStateChecker extends BroadcastReceiver {
    public Context mContext;

    public void onReceive(Context context, Intent intent) {
        this.mContext = context;
        if (!checkActivation(this.mContext)) {
            return;
        }

        if (ConnectionDetector.isInternetAvailable(this.mContext))
        {
            if(checkActivation(mContext))
            {
              //  Intent msgrcv = new Intent("connected");
               // msgrcv.putExtra("NETON","NETON");
                //LocalBroadcastManager.getInstance(mContext).sendBroadcast(msgrcv);
            }
            return;
        }
    }

    public static boolean checkActivation(Context mContext) {
        if (((RunningTaskInfo) ((ActivityManager) mContext.getSystemService("activity")).getRunningTasks(Integer.MAX_VALUE).get(0)).topActivity.getPackageName().toString().equalsIgnoreCase(mContext.getPackageName().toString())) {
            return true;
        }
        return false;
    }
}
