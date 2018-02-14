package co.netpar.Comman;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import co.netpar.ContentView;
import co.netpar.FAQSection;
import co.netpar.Language;
import co.netpar.R;
import co.netpar.Retrofit.ServiceResponse;
import co.netpar.SignIn;
import co.netpar.SignUp;
import co.netpar.Syncronization.NetparDataBase.DatabaseHelper;
import co.netpar.Syncronization.NetparDataBase.SharedPreference;
import okhttp3.ResponseBody;
import retrofit2.Response;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.reverie.customcomponent.RevEditText;

import org.json.JSONObject;

import java.util.List;

import static android.content.ContentValues.TAG;

public class Alert implements ServiceResponse{
    private static final int REMOVE_DEVICE=0;
    public static String INVITE_LINK = "http://18.217.230.32/netpar-pwa-dev/#/registerationStepOne/";


    public static void showToast(Context context, String msg) {
        try {
            Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*-------------Simple error alert---------------*/
    public static void alertDialog(Context context, String msg, String title, boolean check) {
        Builder dialogBuilder = new Builder(context, R.style.custom_alert_dialog);
        View dialogView = ((AppCompatActivity) context).getLayoutInflater().inflate(R.layout.simple_lay, null);
        TextView ttl = (TextView) dialogView.findViewById(R.id.title);
        ImageView chk_error = (ImageView) dialogView.findViewById(R.id.chk_error);
        ((TextView) dialogView.findViewById(R.id.msg)).setText(msg);
        ttl.setText(title);
        CardView ok_card_view = (CardView) dialogView.findViewById(R.id.ok_card_view);
        ok_card_view.setBackgroundResource(R.drawable.button_radious);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.getWindow().setLayout((int) (((double) context.getResources().getDisplayMetrics().widthPixels) * 0.9d), -2);
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_back);
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        ok_card_view.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    /*-------------Show loader---------------*/
    public static AlertDialog startDialog(Context context) {
        Builder dialogBuilder = new Builder(context, R.style.custom_alert_dialog);
        View dialogView = ((AppCompatActivity) context).getLayoutInflater().inflate(R.layout.dialog_alert, null);
        Uri loader = Uri.parse("res:///" + R.drawable.loader_3);
        SimpleDraweeView loader_gif = (SimpleDraweeView) dialogView.findViewById(R.id.loader_gif);
        DraweeController controllerOne = Fresco.newDraweeControllerBuilder()
                .setUri(loader)
                .setAutoPlayAnimations(true)
                .build();
        loader_gif.setController(controllerOne);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.getWindow().setLayout((int) (((double) context.getResources().getDisplayMetrics().widthPixels) * 0.9d), -2);
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        alertDialog.show();
        return alertDialog;
    }

    /*-------------Switch activity after dialog---------------*/
    public static void switchActivityAlert(final Context context, final Class activity, Bundle arg, String title, String msg, String button_text, boolean success) {
        Builder dialogBuilder = new Builder(context, R.style.custom_alert_dialog);
        View dialogView = ((AppCompatActivity) context).getLayoutInflater().inflate(R.layout.simple_lay, null);
        ((TextView) dialogView.findViewById(R.id.title)).setText(title);
        ((TextView) dialogView.findViewById(R.id.msg)).setText(msg);
        ((TextView) dialogView.findViewById(R.id.btn_txt)).setText(button_text);
        ImageView chk_error = (ImageView) dialogView.findViewById(R.id.chk_error);
        if (success) {
            chk_error.setImageResource(R.drawable.check);
        } else {
            chk_error.setImageResource(R.drawable.close);
        }
        CardView ok_card_view = (CardView) dialogView.findViewById(R.id.ok_card_view);
        ok_card_view.setBackgroundResource(R.drawable.button_radious);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.getWindow().setLayout((int) (((double) context.getResources().getDisplayMetrics().widthPixels) * 0.9d), -2);
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_back);
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        ok_card_view.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ((Activity) context).finish();
                context.startActivity(new Intent(context, activity));
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public static void showLog(String title, String msg) {
        Log.e(title, msg);
    }

    public static void inviteFriendFromNumberDialog(final Context context, final String number) {
        final String inviteText = "install netpar from " + INVITE_LINK + SharedPreference.retrieveData(context, Constants.USER_REF_CODE);
        Builder dialogBuilder = new Builder(context, R.style.custom_alert_dialog);
        View dialogView = ((AppCompatActivity) context).getLayoutInflater().inflate(R.layout.alert_invite_friend, null);
        ImageView fb_option = (ImageView) dialogView.findViewById(R.id.fb_option);
        ImageView whats_app_option = (ImageView) dialogView.findViewById(R.id.whats_app_option);
        ImageView message_option = (ImageView) dialogView.findViewById(R.id.message_option);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.getWindow().setLayout((int) (((double) context.getResources().getDisplayMetrics().widthPixels) * 0.9d), -2);
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_back);
        alertDialog.setCanceledOnTouchOutside(false);
        fb_option.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                alertDialog.dismiss();
                inviteLinkViaFacebook(context, inviteText);
            }
        });
        whats_app_option.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                alertDialog.dismiss();
                Uri uri = Uri.parse("smsto:" + number);
                Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                i.putExtra("sms_body", inviteText);
                i.setPackage("com.whatsapp");
                if (i.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(i);
                } else {
                    Alert.showToast(context, context.getString(R.string.install_whatsapp));
                }
            }
        });
        message_option.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                alertDialog.dismiss();
                try {
                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    sendIntent.putExtra("address",number);
                    sendIntent.putExtra("sms_body", inviteText);
                    sendIntent.setType("vnd.android-dir/mms-sms");
                    context.startActivity(sendIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        alertDialog.show();
    }

    public static void inviteMultipleFriendDialog(final Context context) {
        final String inviteText = "install netpar from " + INVITE_LINK + SharedPreference.retrieveData(context, Constants.USER_REF_CODE);
        Builder dialogBuilder = new Builder(context, R.style.custom_alert_dialog);
        View dialogView = ((AppCompatActivity) context).getLayoutInflater().inflate(R.layout.alert_invite_friend, null);
        ImageView fb_option = (ImageView) dialogView.findViewById(R.id.fb_option);
        ImageView whats_app_option = (ImageView) dialogView.findViewById(R.id.whats_app_option);
        ImageView message_option = (ImageView) dialogView.findViewById(R.id.message_option);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.getWindow().setLayout((int) (((double) context.getResources().getDisplayMetrics().widthPixels) * 0.9d), -2);
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_back);
        alertDialog.setCanceledOnTouchOutside(false);
        fb_option.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                alertDialog.dismiss();
                inviteLinkViaFacebook(context, inviteText);
            }
        });
        whats_app_option.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, inviteText);
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.whatsapp");
                if (sendIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(sendIntent);
                } else {
                    Alert.showToast(context, context.getString(R.string.install_whatsapp));
                }
            }
        });
        message_option.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                alertDialog.dismiss();
                try {
                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    sendIntent.putExtra("sms_body", inviteText);
                    sendIntent.setType("vnd.android-dir/mms-sms");
                    context.startActivity(sendIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        alertDialog.show();
    }

    public static void logout(final Context context, final DatabaseHelper helper) {
        Builder dialogBuilder = new Builder(context, R.style.custom_alert_dialog);
        View dialogView = ((AppCompatActivity) context).getLayoutInflater().inflate(R.layout.logout_alert, null);
        TextView ttl = (TextView) dialogView.findViewById(R.id.title);
        ImageView chk_question = (ImageView) dialogView.findViewById(R.id.chk_question);
        CardView ok_card_view = (CardView) dialogView.findViewById(R.id.yes_card_view);
        CardView no_card_view = (CardView) dialogView.findViewById(R.id.no_card_view);
        ok_card_view.setBackgroundResource(R.drawable.button_radious);
        no_card_view.setBackgroundResource(R.drawable.button_radious);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setLayout((int) (((double) context.getResources().getDisplayMetrics().widthPixels) * 0.9d), -2);
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_back);
        ok_card_view.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                alertDialog.dismiss();
             //   new Retrofit2(context,res,REMOVE_DEVICE,Constants.REMOVE_DEVICE_FCM+"/"+SharedPreference.retrieveData(context,Constants.USER_ID)).callService(false);
                SharedPreference.removeAll(context);
                helper.logout();
                ((Activity) context).finish();
                context.startActivity(new Intent(context, Language.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        no_card_view.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    /*------------- login option ---------------*/
    public static void loginOption(final Context context, final boolean finishActivity, final boolean comeFromContentView, final String CONTENT_ID_FORM_CONTENT_VIEW) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View dialogView = ((Activity) context).getLayoutInflater().inflate(R.layout.login_option, null);
        TextView continue_to_sign_in = (TextView) dialogView.findViewById(R.id.continue_to_sign_in);
        TextView do_you_have_a_new_number_update = (TextView) dialogView.findViewById(R.id.do_you_have_a_new_number_update);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setLayout((int) (((double) context.getResources().getDisplayMetrics().widthPixels) * 0.9d), -2);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_back);
        continue_to_sign_in.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                alertDialog.dismiss();
                if (comeFromContentView) {
                    ((SignUp) context).startActivityForResult(new Intent(context, SignIn.class)
                            .putExtra(ContentView.CONTENT_ID_FORM_CONTENT_VIEW, CONTENT_ID_FORM_CONTENT_VIEW).putExtra("UPDATE_MOBILE", false), ContentView.REGISTER_FORM_CONTENT_VIEW);
                } else {
                    if (finishActivity) {
                        ((Activity) context).finish();
                    }
                    context.startActivity(new Intent(context, SignIn.class).putExtra("UPDATE_MOBILE", false));
                }

            }
        });
        do_you_have_a_new_number_update.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                alertDialog.dismiss();
                if (comeFromContentView) {
                    ((SignUp) context).startActivityForResult(new Intent(context, SignIn.class)
                            .putExtra(ContentView.CONTENT_ID_FORM_CONTENT_VIEW, CONTENT_ID_FORM_CONTENT_VIEW).putExtra("UPDATE_MOBILE", true), ContentView.REGISTER_FORM_CONTENT_VIEW);
                } else {
                    if (finishActivity) {
                        ((Activity) context).finish();
                    }
                    context.startActivity(new Intent(context, SignIn.class).putExtra("UPDATE_MOBILE", true));
                }
            }
        });
        alertDialog.show();
    }

    /*------------- help option ---------------*/
    public static void helpDialog(final Context context) {
        Builder dialogBuilder = new Builder(context, R.style.custom_alert_dialog);
        View dialogView = ((AppCompatActivity) context).getLayoutInflater().inflate(R.layout.help_dialog, null);
        ImageView help_option = (ImageView) dialogView.findViewById(R.id.help_option);
        ImageView whats_app_option = (ImageView) dialogView.findViewById(R.id.whats_app_option);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.getWindow().setLayout((int) (((double) context.getResources().getDisplayMetrics().widthPixels) * 0.9d), -2);
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_back);
        alertDialog.setCanceledOnTouchOutside(false);
        help_option.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                alertDialog.dismiss();
                context.startActivity(new Intent(context, FAQSection.class));
            }
        });
        whats_app_option.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                alertDialog.dismiss();
                startWhatsAppForHelp(context);
            }
        });
        alertDialog.show();
    }

    /*------------- help via whats app ---------------*/
    public static void startWhatsAppForHelp(Context context) {
        Uri uri = Uri.parse("smsto:+919821031697");
        Intent i = new Intent(Intent.ACTION_SENDTO, uri);
        i.putExtra("sms_body", "Hi");
        i.setPackage("com.whatsapp");
        if (i.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(i);
        } else {
            Alert.showToast(context, context.getString(R.string.install_whatsapp));
        }
    }

    /*------------- invite via facebook ---------------*/
    public static void inviteLinkViaFacebook(Context context, String data) {

        try {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, data);
            share.setPackage("com.facebook.katana"); //Facebook App package
            context.startActivity(Intent.createChooser(share, "Share"));
        } catch (Exception e) {
            // If we failed (not native FB app installed), try share through SEND
            String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + data;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
            context.startActivity(intent);
        }
    }

    /*------------- rate via play store---------------*/
    public static void rateAlert(final Context context) {
        Builder dialogBuilder = new Builder(context, R.style.custom_alert_dialog);
        View dialogView = ((AppCompatActivity) context).getLayoutInflater().inflate(R.layout.rate, null);
        CardView rate_card = (CardView) dialogView.findViewById(R.id.rate_card);
        final RevEditText comment = (RevEditText) dialogView.findViewById(R.id.comment);
        final RatingBar ratingBar = (RatingBar) dialogView.findViewById(R.id.ratingBar);
        rate_card.setBackgroundResource(R.drawable.button_radious);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.getWindow().setLayout((int) (((double) context.getResources().getDisplayMetrics().widthPixels) * 0.9d), -2);
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_back);
        rate_card.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (ratingBar.getRating() < 1) {
                    return;
                }
                Alert.showToast(context, "This will be work when app will be on play store!");
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onServiceResponse(int i, Response<ResponseBody> response) {
        try
        {
            JSONObject object=new JSONObject(response.body().string());
            switch (i)
            {
                case REMOVE_DEVICE:
                    showLog("REMOVE_DEVICE","REMOVE_DEVICE- "+object.toString());
                    break;
            }
        }
        catch (Exception e)
        {
         e.printStackTrace();
        }
    }
}
