package co.netpar.Comman;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import co.netpar.Home;
import co.netpar.Model.CategoryModel;
import co.netpar.Model.SubCategoryModel;
import co.netpar.R;
import co.netpar.Syncronization.NetparDataBase.SharedPreference;
import co.netpar.Widget.FloatingMenu.FloatingActionMenu;

public class Constants {
//http://18.217.230.32/

    /*Service URL's*/
    public static String BASE = "http://api.netpar.in/netpar/";
    public static String MEDIA_BASE = "http://api.netpar.in/api/";

    // public static String BASE = "https://www.netpar.in/netpar/";
   // public static String MEDIA_BASE = "https://www.netpar.in/api/";

    public static String DOWNLOAD_BASE = "https://i0.wp.com/www.netpar.co.in/";
   // public static String WEB_VIEW_URL_FOR_CONTENT_VIEW = "http://18.217.230.32/netpar_webview.php?content_id=";
    public static String WEB_VIEW_URL_FOR_CONTENT_VIEW = "https://www.netpar.in/netpar_webview.php?content_id=";

    public static String IMAGE_BASE = "http://18.217.230.32:3002/netpar/uploads/content/thumbnails/";//<id>.png

    public static String CHECK_MOBILE = "checkMobile";
    public static String REGISTER = "userSignup";
    public static String SEND_OTP = "https://2factor.in/API/V1/c9b948e1-8bb7-11e7-94da-0200cd936042/SMS/";
    public static String VERIFY_OTP = "https://2factor.in/API/V1/c9b948e1-8bb7-11e7-94da-0200cd936042/SMS/VERIFY/";
    public static String LOGIN = "checkMobile";
    public static String UPDATE_DEVICE = "updateContact";
    public static String SEQURITY_ANSWER = "securityCheck";
    public static String SEQURITY_ANSWER1 = "securityCheck-step1";
    public static String SEQURITY_ANSWER2 = "securityCheck-step2";
    public static String UPDATE_NUMBER = "updateMobile";
    public static String SECTION_DATA = "fetchSections";
    public static String ARTICLE_DATA = "allPosts";
    public static String SYNC_CONTACT_LIST = "syncContacts";
    public static String SAVED_DATA = "savedPosts";
    public static String LIKE_ARTICLE = "postLike";
    public static String SAVE_ARTICLE = "savePost";
    public static String POST_COMMENT = "postComment";
    public static String UPDATE_SESSION_COUNT = "updateSessionCount/";
    public static String UPDATE_SESSION_TIME = "updateTimeCount/";
    public static String UPLOAD_MEDIA= "uploadMedia";
    public static String DELETE_MEDIA= "deleteMedia";
    public static String UPLOAD_POST= "saveContribution";
    public static String GET_MY_POST = "savedContributions";
    public static String UPLOAD_PROFILE_PIC= "uploadProfilePic";
    public static String UPDATE_PROFILE= "updateProfile";
    public static String VIEW_ARTICLE= "getPostAndroid";
    public static String likeParticularSection="likeParticularSection";
    public static String onlineStatus="onlineStatus";
    public static String deleteProfilePic="deleteImage";
    public static String interested="interested";
    public static String call="call";
    public static String callmeback="callmeback";
    public static String apply="apply";
    public static String ADD_DEVICE_FCM = "registerGCMDevice";
    public static String REMOVE_DEVICE_FCM = "deactivateDevice";
    public static String GETALLNOTIFICATION = "getNotifications";

    public static String EDIT_SECTION_TOTAL_TIME= "editSectionTotalTime";
    public static String EDIT_CATEGORY_TOTAL_TIME= "editCategoryTotalTime";
    public static String EDIT_SUB_CATEGORY_TOTAL_TIME= "editSubCategoriesTotalTime";
    public static String templatateAnalayticUpdate="templatateAnalayticUpdate";

    public static String PAGED_VIEW= "pageViewed";
    public static String SECTION_PAGE= "editSectionsPageView";
    public static String CATEGORY_VIEW= "editCategoryPageView";
    public static String SUB_CATEGORY_VIEW= "editSubCategoriesPageView";
    public static String CONTINUE_CONTENT_VIEW= "continueReading";
    public static String ADD_DEVICE = "addDevice";



    /*Session DATA BASE NAME*/
    public static final String KEY = "Info";
    public static final String LANGUAGE_KEY = "language";

    /*Session Keys*/
    public static final String SESSION_IN_SECOND = "second";

    public static final String PLATFORM = "android";
    public static final String SignUpTempFname = "SignUpTempFname";
    public static final String SignUpTempLname = "SignUpTempLname";
    public static final String SignUpTempMNum = "SignUpTempMNum";
    public static final String OTP_VERIFIED = "OTP_VERIFIED";

    public static final String USER_ID = "USER_ID";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String MOBILE_NUMBER = "mobileNumber";
    public static final String STATE = "state";
    public static final String DISTRICT = "district";
    public static final String BLOCK = "block";
    public static final String GENDER = "gender";
    public static final String DOB = "dateOfBirth";
    public static final String USER_IMAGE = "image";
    public static final String USER_REF_CODE = "referal_code";
    public static final String USER_YOGDAN = "totalSubmissions";
    public static final String USER_LEKH = "totalPublications";

    public static final String DD = "DD";
    public static final String MM = "MM";
    public static final String YY = "YY";


    public static final String REFERRAL = "referral";


    public static final String STATE_REGIONAL= "stateRegional";
    public static final String DISTRICT_REGIONAL = "districtRegional";
    public static final String BLOCK_REGIONAL = "blockRegional";


    public static final String STATE_POSITION_IN_SPINNER= "statePosition";
    public static final String DISTRICT_POSITION_IN_SPINNER= "districtPosition";


    public static final String FCM_SERVER_KEY="AAAASBo6jJA:APA91bE-hOIaDGoNRkldtKiLB0_s-MSfhPUhU3bjhK5SAaVa7BMpImZtQr1EYd4i1o0XMYvqHigWQpJ6H8TpvnglDKkvGYyDiDVLmG-tHQRzFyGGCXo9GcaiZ86XSll6l_dBW6hP1yDj";
    public static final String FCM_LEGACY_KEY="AIzaSyAne4TgN0sueKduHEkEvKzWD6fN0b5x3HM";
    public static final String FCM_SENDER_ID="309677690000";

    public static final String NCOUNT="NotificationCount";


    public static String sectionId;
    public static String categoryId;
    public static String subCategoryId;
    public static String list_view_type;

    public static void hideSoftKeyboard(Activity activity) {
        ((InputMethodManager) activity.getSystemService("input_method")).hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void showSoftKeyboard(Activity activity, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService("input_method");
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 2);
    }

    public static boolean isOdd(int position) {
        if (position == 0 || position % 2 == 0) {
            return false;
        }
        return true;
    }

    public static String getUniqueId(Context mContext) {
        String android_id = "";
        android_id = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        return android_id;
    }

    public static void setLayoutColor(int val, CardView card_view) {
        switch (val) {
            case 0:
                card_view.setBackgroundResource(R.drawable.list_color_1);
                break;
            case 1:
                card_view.setBackgroundResource(R.color.list_color2);
                break;
            case 2:
                card_view.setBackgroundResource(R.color.list_color3);
                break;
            case 3:
                card_view.setBackgroundResource(R.color.list_color4);
                break;
            case 4:
                card_view.setBackgroundResource(R.drawable.list_color_5);
                break;
            default:
                setBAckColor(val % 5, card_view);
                break;
        }
    }

    public static void setBAckColor(int t, CardView card_view) {
        switch (t) {
            case 0:
                card_view.setBackgroundResource(R.drawable.list_color_1);
                break;
            case 1:
                card_view.setBackgroundResource(R.color.list_color2);
                break;
            case 2:
                card_view.setBackgroundResource(R.color.list_color3);
                break;
            case 3:
                card_view.setBackgroundResource(R.color.list_color4);
                break;
            case 4:
                card_view.setBackgroundResource(R.drawable.list_color_5);
                break;
            default:
                break;
        }
    }

    public static void setLayoutColorWithRadios(int val, CardView card_view) {
        switch (val) {
            case 0:
                card_view.setBackgroundResource(R.drawable.radious_color1);
                break;
            case 1:
                card_view.setBackgroundResource(R.drawable.radious_color2);
                break;
            case 2:
                card_view.setBackgroundResource(R.drawable.radious_color3);
                break;
            case 3:
                card_view.setBackgroundResource(R.drawable.radious_color4);
                break;
            case 4:
                card_view.setBackgroundResource(R.drawable.radious_color5);
                break;
            default:
                setBActColorWithRadios(val % 5, card_view);
                break;
        }
    }

    public static void setBActColorWithRadios(int t, CardView card_view) {
        switch (t) {
            case 0:
                card_view.setBackgroundResource(R.drawable.radious_color1);
                break;
            case 1:
                card_view.setBackgroundResource(R.drawable.radious_color2);
                break;
            case 2:
                card_view.setBackgroundResource(R.drawable.radious_color3);
                break;
            case 3:
                card_view.setBackgroundResource(R.drawable.radious_color4);
                break;
            case 4:
                card_view.setBackgroundResource(R.drawable.radious_color5);
                break;
            default:
                break;
        }
    }

    public static void startWhatsAppForHelp(Context context, String id, FloatingActionMenu menu)
    {
        Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse("https://api.whatsapp.com/send?phone=+918879732726"));
        i.setPackage("com.whatsapp");
        if (i.resolveActivity(context.getPackageManager()) != null)
        {
            if (menu != null) {
                menu.close(true);
            }
            context.startActivity(i);
        } else {
            Alert.showToast(context, context.getString(R.string.install_whatsapp));
        }
    }

    public static void shareAppLinkViaFacebook(Context context, String data, FloatingActionMenu menu) {
        String urlToShare = Alert.INVITE_LINK + "/" + SharedPreference.retrieveData(context,Constants.USER_REF_CODE);
        if (menu != null) {
            menu.close(true);
        }
        try {
            Intent intent1 = new Intent();
            intent1.setClassName("com.facebook.katana", "com.facebook.katana.activity.composer.ImplicitShareIntentHandler");
            intent1.setAction("android.intent.action.SEND");
            intent1.setType("text/plain");
            intent1.putExtra("android.intent.extra.TEXT", data);
            context.startActivity(intent1);
        } catch (Exception e) {
            // If we failed (not native FB app installed), try share through SEND
            String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + urlToShare;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
            context.startActivity(intent);
        }

    }

    public static void shareAppLinkViaMessage(Context context, String id, FloatingActionMenu menu) {
        String urlToShare = Alert.INVITE_LINK + "/" + SharedPreference.retrieveData(context,Constants.USER_REF_CODE);
        try {
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.putExtra("sms_body", urlToShare);
            sendIntent.setType("vnd.android-dir/mms-sms");
            context.startActivity(sendIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startHelpCall(Context context, FloatingActionMenu menu) {
        if (menu != null) {
            menu.close(true);
        }
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:+918437162320"));
        context.startActivity(intent);
    }

    public static byte[] getBytesFromBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
            return stream.toByteArray();
        }
        return null;
    }

    public static String changeDateFormat(Context context, String date) {
        String dt = date;
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd-MMM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        Date datee = null;
        try {
            datee = inputFormat.parse(dt);
            dt = outputFormat.format(datee);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dt;
    }

    /*Page Name*/
    public static final String HOME="Home";
    public static final String CONTRIBUTION="Contribution";
    public static final String MY_ACCOUNT="My Account";
    public static final String CATEGORY="Category";
    public static final String SUB_CATEGORY="Sub Category";
    public static final String CONTENT_LISTING="Content Listing";
    public static final String CONTACT="Contacts";
    public static final String CONTENT_VIEW="Content View";
    public static final String SAVED_ITEM="Saved Item";
    public static final String DOWNLOAD_ITEM="Download Item";
    public static final String NOTIFICATION="Notification";


    /*To generate device id for fcm Used on Home Screen*/
    public static String getFcmDeviceID(Context mContext)
    {
        String refreshedToken = "";
        if (SharedPreference.retrieveFcmDeviceId(mContext) == null) {
            refreshedToken = FirebaseInstanceId.getInstance().getToken();
            SharedPreference.storeFcmDeviceId(mContext, refreshedToken);
            return refreshedToken;
        } else {
            refreshedToken = SharedPreference.retrieveFcmDeviceId(mContext);
        }
        return refreshedToken;
    }


    /*
    * Google Analytics:
      Email id: netpar.analytics@gmail.com
      Password: xbtl1netpar

       Flurry Analytics:
       Email id: netpar.it@gmail.com
       Password: xbtl1netpar*/

    public static final String FACEBOOK_APP_ID="152805198711320";
}
