package co.netpar.Network;

/**
 * Created by promatics on 10/5/2017.
 */

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import co.netpar.Comman.Alert;

/*------------ Broadcast used to get OTP from Message------------------*/
public class SmsReceiver extends BroadcastReceiver {
    private static SmsListener mListener;
    Boolean b;
    String abcd,xyz;
    @Override
    public void onReceive(Context context, Intent intent) {
        if(!checkActivation(context))
        {
            return;
        }

        Bundle data  = intent.getExtras();
        for (String key : data.keySet())
        {
            Alert.showLog("Bundle Debug", key + " = \"" + data.get(key) + "\"");
        }
        if (!NetworkStateChecker.checkActivation(context)) {
            return;
        }

        Object[] pdus = (Object[]) data.get("pdus");
        for(int i=0;i<pdus.length;i++){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
            String sender = smsMessage.getDisplayOriginatingAddress();
            String messageBody = smsMessage.getMessageBody();
            abcd=messageBody.replaceAll("[^0-9]","");
            try {
                mListener.messageReceived(abcd);  // attach value to interface
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }

    public interface SmsListener{
        public void messageReceived(String messageText);}

    public static boolean checkActivation(Context mContext) {
        if (((ActivityManager.RunningTaskInfo) ((ActivityManager) mContext.getSystemService("activity")).getRunningTasks(Integer.MAX_VALUE).get(0)).topActivity.getPackageName().toString().equalsIgnoreCase(mContext.getPackageName().toString())) {
            return true;
        }
        return false;
    }
}
