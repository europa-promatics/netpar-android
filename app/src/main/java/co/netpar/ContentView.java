package co.netpar;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.service.textservice.SpellCheckerService;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareMediaContent;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.widget.ShareDialog;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.analytics.HitBuilders;
import com.reverie.customcomponent.RevEditText;
import com.reverie.manager.LangResourceInitCompleteListener;
import com.reverie.manager.RevSDK;
import com.reverie.manager.RevStatus;
import com.reverie.manager.ValidationCompleteListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import co.netpar.Adapter.AllCommentAdapter;
import co.netpar.Adapter.ContentViewDynamicFrameAdapter;
import co.netpar.Adapter.SuggestedArticales;
import co.netpar.App.Controller;
import co.netpar.Comman.Alert;
import co.netpar.Comman.Constants;
import co.netpar.Fragment.HomeFragment;
import co.netpar.Keyboard.KeyboardCredentialConstants;
import co.netpar.Model.CommentListModel;
import co.netpar.Model.ContentListingModel;
import co.netpar.Model.SetMultipleImageDynamically;
import co.netpar.Network.ConnectionDetector;
import co.netpar.Retrofit.Retrofit2;
import co.netpar.Retrofit.ServiceResponse;
import co.netpar.Syncronization.CallService;
import co.netpar.Syncronization.NetparDataBase.DataBaseStrings;
import co.netpar.Syncronization.NetparDataBase.DatabaseHelper;
import co.netpar.Syncronization.NetparDataBase.SharedPreference;
import co.netpar.Widget.FloatingMenu.FloatingActionButton;
import co.netpar.Widget.FloatingMenu.FloatingActionMenu;
import co.netpar.Widget.FloatingMenu.SubActionButton;
import co.netpar.Widget.JustifyTextView;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class ContentView extends AppCompatActivity implements View.OnClickListener, ServiceResponse {
    private LinearLayout like_lay, comment_lay, save_lay, download_lay, view_more_lay, bottom_like_lay, bottom_comment_lay, bottom_save_lay, bottom_view_lay;
    private JustifyTextView tag_line;
    private TextView title, publisher, published_date, like_txt, comment_txt, save_txt, download_txt, views, likes, comments, saves, send_comment;
    private ImageView like_img, bottom_icon_like, comment_img, botton_save, save_img, download_img;
    private boolean activity_running = false;
    private Toolbar toolbar;
    private RevEditText comment_box;
    private RecyclerView suggetions;
    private List<String> content_data = new ArrayList<>();
    private SuggestedArticales contentListingAdapter;
    private LinearLayout single_comment_lay, write_comment_lay;
    private LinearLayout.LayoutParams param1, param2;
    public static final int HOME = 1, CONTRIBUTION = 2, ACCOUNT = 3, BACK = 0, PROFILE = 4, DOWNLOAD = 5, FRIEND = 6, SAVED = 7, MY = 8;
    private DatabaseHelper helper;
    private LinearLayout last_lay;
    private LinearLayout continue_reading, continue_reading_lay;
    byte[] accImage;
    byte[] photo, data;
    private List<SetMultipleImageDynamically> images = new ArrayList<>();
    private TextView tol_title;
    private ImageView back_icon;
    private ContentListingModel dataAs;
    private CircleImageView fabIconNew;
    private ScrollView scroll;
    private boolean isLike = false;
    private boolean isSave = false;
    private boolean isDownloaded = false;
    private final int LIKE = 0, COMMENT = 1, SAVE = 2, CONTENT_VIEW = 4, CALL = 5, APPLY = 6, CALL_BACK = 7, INTEREST = 8, LIKE_PARTICULAR = 19;
    private RelativeLayout bac_dim_layout;
    private List<CommentListModel> cm_model = new ArrayList<>();
    private FloatingActionButton rightLowerHelpButton, rightLowerShareButton;
    private FloatingActionMenu rightLowerMenu, rightLowerShareMenu;
    private ContentViewDynamicFrameAdapter fram_adapter;
    private RelativeLayout top_count_head;
    private TextView head_comment_like, head_comment_count;
    private LinearLayout related_suggetions;
    private RecyclerView list;
    private WebView contrabution_frame_webView;
    private AlertDialog alert, search_alert;
    private boolean startWebLoader = false;
    private AllCommentAdapter all_commentadapter;
    private String sharedLink = "https://www.netpar.in/#/Story/";
    private String extensionRemoved;
    private TextView suggest_text;
    private RelativeLayout suggest_way;
    private boolean suggestForDownload = true;
    private boolean suggestForSave = true;
    private boolean isSavedStatus = true;

    private boolean directCome = false;
    public static final int REGISTER_FORM_CONTENT_VIEW = 100;
    public static final int OPEN_SUGGESTED_ARTICLES_CONTENT_VIEW = 101;

    public static final String CONTENT_ID_FORM_CONTENT_VIEW = "CONTENT_ID_FORM_CONTENT_VIEW";
    private List<CommentListModel> CommentList = new ArrayList<>();

    private View bg;
    private LinearLayout sheet_profile_lay, sheet_save, sheet_download, sheet_friends, sheet_my_contribution, sheet_logout;
    private static BottomSheetBehavior bottomSheetBehavior;
    private TextView view_count;
    private String contentId;
    private Toast toast;

    private boolean isComment=false;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_view);
        helper = new DatabaseHelper(ContentView.this);
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= 21) {
            window.setStatusBarColor(getResources().getColor(R.color.top_bar_bg_color));
        }
        window.setBackgroundDrawableResource(R.color.gray_back);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Intent intent = getIntent();
        setBottomSheet();
        Home.LIST = null;
        if (Intent.ACTION_VIEW.equals(intent.getAction()))
        {
            /*Come via deepLinking*/
            initialiseRevereSDKS();
            Uri data = intent.getData();
            String providerUrl = intent.getData().toString();
            contentId = providerUrl.split("/")[providerUrl.split("/").length - 1];
            if (providerUrl.contains("registerationStepOne"))
            {
                if (SharedPreference.retrieveData(ContentView.this, Constants.USER_ID) != null) {
                    finish();
                    startActivity(new Intent(ContentView.this, Home.class));
                    return;
                }
                SharedPreference.storeData(ContentView.this, Constants.REFERRAL, contentId);
                finish();
                startActivity(new Intent(ContentView.this, SignUp.class));
            }
            else
            {
                if (SharedPreference.retrieveData(ContentView.this, Constants.USER_ID) == null) {
                    SharedPreference.storeLang(this, 10);
                    startActivityForResult(new Intent(ContentView.this, SignUp.class).putExtra(CONTENT_ID_FORM_CONTENT_VIEW, contentId), REGISTER_FORM_CONTENT_VIEW);
                }
                else
                    {
                    directCome = true;
                    functionToOpenArticleDirectly(contentId);
                }
            }
        }
        else
            {
                /*Open directly*/
                dataAs = (ContentListingModel) getIntent().getExtras().getSerializable("DATA");
            contentId = dataAs.getContentId();
            initial();
            if (!ConnectionDetector.isInternetAvailable(ContentView.this)) {
                showAllData();
            } else {
                setContainFarm();
            }

            if (HomeFragment.main_lay != null) {
                HomeFragment.main_lay.setVisibility(View.VISIBLE);
            }
        }

        try {
            new Controller().trackEvent("Article", "click", dataAs.getHeadline(), dataAs.getItem_id());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void functionToOpenArticleDirectly(String contentId) {
        callGetContentService(contentId);
        //new SearchArticleAsyncCaller().execute(contentId);
    }

    public void callGetContentService(String contentId) {
        new Retrofit2(ContentView.this, this, CONTENT_VIEW, Constants.VIEW_ARTICLE + "/" + contentId).callService(false);
    }

    private void initial() {
        setToolbar();
        LocalBroadcastManager.getInstance(ContentView.this).registerReceiver(onNotice, new IntentFilter("connected"));
        startWebLoader = true;
        initializeView();
        setFloatMenus();
        sendDataToGoogleAnalytics();
    }

    private void sendDataToGoogleAnalytics() {
        // TODO: 17/1/18   /*callContentViewPageCountService*/
        Controller.getInstance().trackScreenView(Constants.CONTENT_VIEW);

        /*For Flurry*/
        FlurryAgent.onPageView();
        Map<String, String> articleParams = new HashMap<String, String>();
        articleParams.put("Author", dataAs.getCreator_first_name() + " " + dataAs.getCreator_last_name());
        articleParams.put("User_Status", "Registered");
        FlurryAgent.logEvent("Article_Read", articleParams, true);
    }

    private void setEndTimeToFlurry() {
        FlurryAgent.endTimedEvent("Article_Read");
    }

    private void initialiseRevereSDKS() {

        RevSDK.validateKey(getApplicationContext(), "https://api-gw.revup.reverieinc.com/apiman-gateway/ReverieMobilitySdk", KeyboardCredentialConstants.SDK_TEST_API_KEY, "co.netpar", new ValidationCompleteListener() {
            @Override
            public void onValidationComplete(int statusCode, String statusMessage) {
                Alert.showLog("Language", "LICENSE VALIDATION COMPLETE : " + statusCode + " ," + statusMessage);
                if (statusCode != 1) {
                } else if (statusCode == 3) {
                } else {
                    new AsyncCaller().execute();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (dataAs != null) {
            tol_title = (TextView) findViewById(R.id.tol_title);
            back_icon = (ImageView) findViewById(R.id.back_icon);
            back_icon.setOnClickListener(this);
            tol_title.setText(dataAs.getSubCategoryName());
            getSupportActionBar().setTitle("");
            sharedLink = sharedLink + dataAs.getSlug();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        activity_running = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        activity_running = false;
    }

    /*---------------------- initialize views -------------------------*/
    private void initializeView() {
        list = (RecyclerView) findViewById(R.id.list);

        bac_dim_layout = (RelativeLayout) findViewById(R.id.bac_dim_layout);
        scroll = (ScrollView) findViewById(R.id.scroll);
        last_lay = (LinearLayout) findViewById(R.id.last_lay);
        continue_reading = (LinearLayout) findViewById(R.id.continue_reading);
        continue_reading_lay = (LinearLayout) findViewById(R.id.continue_reading_lay);
        like_lay = (LinearLayout) findViewById(R.id.like_lay);
        comment_lay = (LinearLayout) findViewById(R.id.comment_lay);
        save_lay = (LinearLayout) findViewById(R.id.save_lay);
        download_lay = (LinearLayout) findViewById(R.id.download_lay);

        bottom_view_lay = (LinearLayout) findViewById(R.id.bottom_view_lay);
        bottom_like_lay = (LinearLayout) findViewById(R.id.bottom_like_lay);
        bottom_comment_lay = (LinearLayout) findViewById(R.id.bottom_comment_lay);
        bottom_save_lay = (LinearLayout) findViewById(R.id.bottom_save_lay);

        tag_line = (JustifyTextView) findViewById(R.id.tag_line);
        title = (TextView) findViewById(R.id.title);
        publisher = (TextView) findViewById(R.id.publisher);
        published_date = (TextView) findViewById(R.id.published_date);

        top_count_head = (RelativeLayout) findViewById(R.id.top_count_head);
        head_comment_like = (TextView) findViewById(R.id.head_comment_like);
        head_comment_count = (TextView) findViewById(R.id.head_comment_count);

        view_count = (TextView) findViewById(R.id.view_count);
        like_txt = (TextView) findViewById(R.id.like_txt);
        comment_txt = (TextView) findViewById(R.id.comment_txt);
        save_txt = (TextView) findViewById(R.id.save_txt);
        download_txt = (TextView) findViewById(R.id.download_txt);

        comment_img = (ImageView) findViewById(R.id.comment_img);
        save_img = (ImageView) findViewById(R.id.save_img);
        download_img = (ImageView) findViewById(R.id.download_img);
        like_img = (ImageView) findViewById(R.id.like_img);

        bottom_icon_like = (ImageView) findViewById(R.id.bottom_icon_like);
        botton_save = (ImageView) findViewById(R.id.botton_save);

        views = (TextView) findViewById(R.id.views);
        likes = (TextView) findViewById(R.id.likes);
        comments = (TextView) findViewById(R.id.comments);
        saves = (TextView) findViewById(R.id.saves);

        comment_box = (RevEditText) findViewById(R.id.comment_box);
        send_comment = (TextView) findViewById(R.id.send_comment);
        single_comment_lay = (LinearLayout) findViewById(R.id.single_comment_lay);
        write_comment_lay = (LinearLayout) findViewById(R.id.write_comment_lay);
        view_more_lay = (LinearLayout) findViewById(R.id.view_more_lay);

        contrabution_frame_webView = (WebView) findViewById(R.id.contrabution_frame_webView);

        suggest_way = (RelativeLayout) findViewById(R.id.suggest_way);
        suggest_text = (TextView) findViewById(R.id.suggest_text);
        suggest_way.setVisibility(View.GONE);

        initializeClickListener();
        setBottomBar();

        publisher.setText(dataAs.getCreator_first_name() + " " + dataAs.getCreator_last_name());
        published_date.setText(Constants.changeDateFormat(ContentView.this, dataAs.getDateOfCreation()));

        setData();
        comment_box.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                RevSDK.initKeypad(ContentView.this, SharedPreference.retrieveLang(ContentView.this));
                return false;
            }
        });
    }

    /*---------------------- set data -------------------------*/
    private void setData() {
        if (dataAs != null) {
            title.setText(dataAs.getHeadline());
            tag_line.setText(dataAs.getTagline());
        }
        suggetions = (RecyclerView) findViewById(R.id.suggetions);
        related_suggetions = (LinearLayout) findViewById(R.id.related_suggetions);
        related_suggetions.setVisibility(View.GONE);

        content_data.clear();
        String suggested_article_id = dataAs.getSuggestedArticleList();
        try
        {
            if (suggested_article_id.contains(","))
            {
                String[] string_id = suggested_article_id.split(",");
                for (int i = 0; i < string_id.length; i++) {
                    content_data.add(string_id[i]);
                }
            }
            else
            {
                if(!suggested_article_id.isEmpty() && !suggested_article_id.equalsIgnoreCase(DataBaseStrings.DEFAULT_VALUE))
                {
                    content_data.add(suggested_article_id);
                }
            }
            if (content_data.size() > 0)
            {
                related_suggetions.setVisibility(View.VISIBLE);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        setItemList();

        Alert.showLog("STATUS", "getPostLike-- " + helper.getPostLike(dataAs.getContentId()));
        if (helper.getPostLike(dataAs.getContentId()).equalsIgnoreCase("1")) {
            isLike = true;
        }
        if (isLike) {
            like_img.setImageResource(R.drawable.like_select);
            bottom_icon_like.setImageResource(R.drawable.like_select);
        } else {
            like_img.setImageResource(R.drawable.like_unselect);
            bottom_icon_like.setImageResource(R.drawable.like_unselect);
        }

        Alert.showLog("STATUS", "getPostSave-- " + helper.getPostSave(dataAs.getContentId()));
        if (helper.getPostSave(dataAs.getContentId()).equalsIgnoreCase("1")) {
            isSavedStatus = true;
        } else {
            isSavedStatus = false;
        }

        if (isSavedStatus) {
            save_img.setImageResource(R.drawable.save_select);
            botton_save.setImageResource(R.drawable.save_select);
        } else {
            save_img.setImageResource(R.drawable.save_unselect);
            botton_save.setImageResource(R.drawable.save_unselect);
        }

        like_txt.setText(helper.getPostLikeCount(dataAs.getContentId()) + " " + getString(R.string.simple_likes));
        likes.setText(helper.getPostLikeCount(dataAs.getContentId()) + " " + getString(R.string.simple_likes));
        comments.setText(helper.getPostCommentCount(dataAs.getContentId()) + " " + getString(R.string.commen));
        saves.setText(helper.getPostSaveCount(dataAs.getContentId()) + " " + getString(R.string.saves));
        save_txt.setText(helper.getPostSaveCount(dataAs.getContentId()) + " " + getString(R.string.saves));
        views.setText(helper.getPostViewCount(dataAs.getContentId()) + " " + getString(R.string.views));

        head_comment_like.setText(helper.getPostLikeCount(dataAs.getContentId()) + " " + getString(R.string.likes) + " ");
        head_comment_count.setText(helper.getPostCommentCount(dataAs.getContentId()) + " " + getString(R.string.commen) + " ");
        view_count.setText(helper.getPostViewCount(dataAs.getContentId()) + " " + getString(R.string.views) + " ");

        setComment();
    }

    /*---------------------- set comments -------------------------*/
    private void setComment() {
        if (helper.getCommentCount(dataAs.getContentId()) > 0) {
            try {
                single_comment_lay.setVisibility(View.VISIBLE);
                cm_model.clear();
                cm_model.addAll(helper.getPostComments(dataAs.getContentId()));
                Collections.reverse(cm_model);
                showAllCommentAlert();
                if (cm_model.size() > 1) {
                    view_more_lay.setVisibility(View.VISIBLE);
                } else {
                    view_more_lay.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            single_comment_lay.setVisibility(View.GONE);
            view_more_lay.setVisibility(View.GONE);
        }
    }

    private void setBottomBar() {
        LinearLayout contabution_lay = (LinearLayout) findViewById(R.id.contabution_lay);
        LinearLayout my_account_lay = (LinearLayout) findViewById(R.id.my_account_lay);
        LinearLayout home_lay = (LinearLayout) findViewById(R.id.home_lay);

        contabution_lay.setOnClickListener(this);
        my_account_lay.setOnClickListener(this);
        home_lay.setOnClickListener(this);
    }

    private void setItemList()
    {
        if (contentListingAdapter == null)
        {
            Alert.showLog("SIZE", "SIZE--  " + content_data.size());
            contentListingAdapter = new SuggestedArticales(ContentView.this, content_data, helper);
            suggetions.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            suggetions.setAdapter(contentListingAdapter);
            contentListingAdapter.setOnClickListener(new SuggestedArticales.OnClickListener() {
                @Override
                public void OnClick(String id,ContentListingModel content,int position, View view)
                {
                    try
                    {
                        if(content.getContentId().equalsIgnoreCase(dataAs.getContentId()))
                        {

                            if(toast==null)
                            {
                                toast = Toast.makeText(ContentView.this,getString(R.string.article_aready_open), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                            else
                            {
                                toast.cancel();
                                toast = Toast.makeText(ContentView.this,getString(R.string.article_aready_open), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                            return;
                        }
                    }
                    catch (Exception e)
                    {
                        Alert.showLog("Exception", "Exception--  " +  e.toString());
                        e.printStackTrace();
                    }
                    startActivityForResult(new Intent(ContentView.this, ContentView.class).putExtra("DATA",content),OPEN_SUGGESTED_ARTICLES_CONTENT_VIEW);
                  //  callGetContentService(id);
                }
            });
        } else {
            notifyItems(content_data);
        }
    }

    private void notifyItems(List<String> element) {
        for (int i = 0; i < element.size(); i++) {
            contentListingAdapter.notifyItemChanged(i, element.get(i));
        }
    }

    private void initializeClickListener() {
        like_lay.setOnClickListener(this);
        comment_lay.setOnClickListener(this);
        save_lay.setOnClickListener(this);
        download_lay.setOnClickListener(this);
        view_more_lay.setOnClickListener(this);
        send_comment.setOnClickListener(this);
        continue_reading.setOnClickListener(this);

        bottom_view_lay.setOnClickListener(this);
        bottom_like_lay.setOnClickListener(this);
        bottom_comment_lay.setOnClickListener(this);
        bottom_save_lay.setOnClickListener(this);
    }

    /*---------------------- Like Post -------------------------*/
    private void setLike() {
        callLikeService();
        sendDownloadData("Like");
        long val = 0;
        String likesss = head_comment_like.getText().toString();
        if (likesss.contains(" ")) {
            try {
                String[] countt = likesss.split(" ");
                if (countt[0].contains("Not")) {
                    countt[0] = "0";
                }
                val = Long.parseLong(countt[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (isLike) {
            isLike = false;
            like_img.setImageResource(R.drawable.like_unselect);
            bottom_icon_like.setImageResource(R.drawable.like_unselect);
            helper.updatePostLike(dataAs.getContentId(), "0");

            if (val != 0) {
                val = val - 1;
            }
        } else {
            isLike = true;
            like_img.setImageResource(R.drawable.like_select);
            bottom_icon_like.setImageResource(R.drawable.like_select);
            helper.updatePostLike(dataAs.getContentId(), "1");
            val = val + 1;
        }
        like_txt.setText(String.valueOf(val) + " " + getString(R.string.simple_likes));
        likes.setText(String.valueOf(val) + " " + getString(R.string.simple_likes));
        head_comment_like.setText(String.valueOf(val) + " " + getString(R.string.likes));
        callUpdateDataService();
    }

    /*---------------------- SAVE Post -------------------------*/
    private void setSaveData() {
        callSaveService();
        sendDownloadData("Save");
        long val = 0;
        String likesss = saves.getText().toString();
        if (likesss.contains(" ")) {
            try {
                String[] countt = likesss.split(" ");
                if (countt[0].contains("Not")) {
                    countt[0] = "0";
                }
                val = Long.parseLong(countt[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (suggestForSave) {
            suggestForSave = false;
            suggest_way.setVisibility(View.VISIBLE);
            bac_dim_layout.setVisibility(View.VISIBLE);
            suggest_text.setText(getString(R.string.save_suggestion));
        }

        if (isSavedStatus) {
            isSavedStatus = false;
            save_img.setImageResource(R.drawable.save_unselect);
            botton_save.setImageResource(R.drawable.save_unselect);
            helper.updatePostSave(dataAs.getContentId(), "0");

            if (val != 0) {
                val = val - 1;
            }
        } else {
            isSavedStatus = true;
            save_img.setImageResource(R.drawable.save_select);
            botton_save.setImageResource(R.drawable.save_select);
            helper.updatePostSave(dataAs.getContentId(), "1");
            val = val + 1;
        }

        saves.setText(String.valueOf(val) + " " + getString(R.string.saves));
        save_txt.setText(String.valueOf(val) + " " + getString(R.string.saves));
        callUpdateDataService();
    }

    private void setCommentClick() {
        sendDownloadData("Comment");
        continue_reading_lay.setVisibility(View.GONE);
        last_lay.setVisibility(View.VISIBLE);
        top_count_head.setVisibility(View.VISIBLE);
        RevSDK.initKeypad(ContentView.this, SharedPreference.retrieveLang(ContentView.this));
        comment_box.requestFocus();
        ViewTreeObserver vto = scroll.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                int centreX = (int) (comment_box.getX() + comment_box.getWidth() / 2);
                int centreY = (int) (comment_box.getY() + comment_box.getHeight() / 2);
                scroll.smoothScrollBy(centreX, centreY);

                scroll.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    private void callUpdateDataService() {
        CallService ser = new CallService(ContentView.this);
        ser.callGetArticleService();
        ser.setOnUpdate(new CallService.ResponseSynchronous() {
            @Override
            public void UpdateData(int service_val) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.like_lay:
                if (!ConnectionDetector.isInternetAvailable(ContentView.this)) {
                    return;
                }
                setLike();

                break;
            case R.id.bottom_like_lay:
                if (!ConnectionDetector.isInternetAvailable(ContentView.this)) {
                    return;
                }
                setLike();

                break;
            case R.id.comment_lay:
                setCommentClick();

                break;
            case R.id.bottom_comment_lay:
                setCommentClick();

                break;
            case R.id.save_lay:
                if (!ConnectionDetector.isInternetAvailable(ContentView.this)) {
                    return;
                }
                suggestForSave = true;
                setSaveData();
                break;
            case R.id.bottom_save_lay:
                if (!ConnectionDetector.isInternetAvailable(ContentView.this)) {
                    return;
                }
                suggestForSave = true;
                setSaveData();
                break;
            case R.id.bottom_view_lay:
                break;
            case R.id.download_lay:
                // setDownloadData();
                break;
            case R.id.view_more_lay:
                if (all_commentadapter != null) {
                    all_commentadapter.setReturnType();
                    all_commentadapter.notifyDataSetChanged();
                    view_more_lay.setVisibility(View.GONE);
                }
                break;
            case R.id.send_comment:
                if (comment_box.getText().toString().trim().length() == 0) {
                    Alert.showToast(ContentView.this, getString(R.string.type_your_comment));
                    comment_box.requestFocus();
                    return;
                }
                callSendCommentService();

                break;
            case R.id.home_lay:
                back(HOME);
                break;
            case R.id.contabution_lay:
                back(CONTRIBUTION);
                break;
            case R.id.my_account_lay:
                expendBottomSheet();
                break;
            case R.id.continue_reading:
                showAllData();
                break;
            case R.id.back_icon:
                onBackPressed();
                break;
            case R.id.bg:
                try {

                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.sheet_profile_lay:
                try {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    backSheet(PROFILE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.sheet_save:
                try {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    backSheet(SAVED);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.sheet_download:
                try {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    backSheet(DOWNLOAD);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.sheet_friends:
                try {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    backSheet(FRIEND);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.sheet_my_contribution:
                try {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    backSheet(MY);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /*---------------------- Download Media -------------------------*/
    private void setDownloadData(String url) {
        if (Build.VERSION.SDK_INT > 21 && ContextCompat.checkSelfPermission(ContentView.this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            ActivityCompat.requestPermissions(ContentView.this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
            return;
        }
        sendDownloadData("Download");
        download_img.setImageResource(R.drawable.download_select);
        download_lay.setEnabled(false);

        if (suggestForDownload) {
            suggestForDownload = false;
            suggest_way.setVisibility(View.VISIBLE);
            bac_dim_layout.setVisibility(View.VISIBLE);
            suggest_text.setText(getString(R.string.download_suggestion));
        }

        try {

            String media_name = url.substring(url.lastIndexOf("/")+1);
            if(media_name.contains("?"))
            {
                media_name=media_name.split(Pattern.quote("?"))[0];
              //  url=media_name.split("?")[0];
            }
            if (media_name.contains("jpeg") || media_name.contains("jpg") || media_name.contains("png")
                    || media_name.contains("bmp") || media_name.contains("gif")) {
                new Retrofit2(url, ContentView.this, Controller.getImages_path(), media_name).download();

                if (helper.getDownloadCount(dataAs.getContentId(), media_name) < 1) {
                    helper.downloadPost(dataAs.getContentId(), "image",url, Controller.getImages_path(), media_name,SharedPreference.retrieveData(ContentView.this, Constants.USER_ID) ,0);
                }
            } else {
                new Retrofit2(url, ContentView.this, Controller.getOther_path(), media_name).download();

                if (helper.getDownloadCount(dataAs.getContentId(), media_name) < 1) {
                    helper.downloadPost(dataAs.getContentId(), "other", url, Controller.getOther_path(), media_name,SharedPreference.retrieveData(ContentView.this, Constants.USER_ID) ,1);
                }
            }
        } catch (Exception e) {
            Alert.showLog("Exception","Exception ... "+e.toString()+", "+e.toString());
        }

        //  new Retrofit2(url, ContentView.this, Controller.getAudio_path(), media_name).download();
        //  new Retrofit2(url, ContentView.this, Controller.getVideo_path(), media_name).download();
    }

    private void showAllData() {
        continue_reading_lay.setVisibility(View.GONE);
        last_lay.setVisibility(View.VISIBLE);
        top_count_head.setVisibility(View.VISIBLE);
    }


    private void initialAllData() {
        continue_reading_lay.setVisibility(View.GONE);
        last_lay.setVisibility(View.GONE);
        top_count_head.setVisibility(View.GONE);
    }

    /*---------------------- POST Comment -------------------------*/
    private void callSendCommentService() {
        try {
            JSONObject peram = new JSONObject();
            peram.put("userId", SharedPreference.retrieveData(ContentView.this, Constants.USER_ID));
            peram.put("articleName", title.getText().toString());
            peram.put("articleId", dataAs.getContentId());
            peram.put("userPhone", SharedPreference.retrieveData(ContentView.this, Constants.MOBILE_NUMBER));
            peram.put("userName", SharedPreference.retrieveData(ContentView.this, Constants.FIRST_NAME) +
                    " " + SharedPreference.retrieveData(ContentView.this, Constants.LAST_NAME));
            peram.put("userComment", comment_box.getText().toString().toString());
            peram.put("sectionName", dataAs.getSectionName());
            peram.put("categoryName", dataAs.getCategoryName());
            peram.put("subCategoryName", dataAs.getSubCategoryName());
            peram.put("language", "Marathi");
            new Retrofit2(this, this, COMMENT, Constants.POST_COMMENT, peram).callService(true);

            /*[4:39 AM] Promatics Inshad:
<<< {'language':"Hindi"},
    {'language':"Marathi"},
    {'language':"Assamese" },
    {'language':"Bengali" },
    {'language':"Dogri" },
    {'language':"Gujarati" },
    {'language':" Punjabi" },
    {'language':"Bodo" },
    {'language':"Kannada" },
    {'language':"Kashmiri" },
    {'language':"Urdu" },
    {'language':"Telegu" },
    {'language':"Konkani" },
    {'language':"Malayalam" },
    {'language':"Manipuri or Meithei" },
    {'language':"English" },
    {'language':"Nepali" },
    {'language':"Oriya" },
    {'language':"Sanskrit" },
    {'language':"Santali" },
    {'language':"Sindhi" },
    {'language':"Tamil"}Promatics Inshad, Today 4:39 AM*/
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void back(int val) {
        if (contrabution_frame_webView != null) {
            contrabution_frame_webView.destroy();
            contrabution_frame_webView = null;
        }
        setEndTimeToFlurry();
        Intent intent = new Intent();
        intent.putExtra("VALUE", val);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void backSheet(int val) {
        if (contrabution_frame_webView != null) {
            contrabution_frame_webView.destroy();
            contrabution_frame_webView = null;
        }
        setEndTimeToFlurry();
        Intent intent = new Intent();
        intent.putExtra("VALUE", val);
        intent.putExtra("LIST", dataAs);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (suggest_way.isShown()) {
            suggest_way.setVisibility(View.GONE);
            bac_dim_layout.setVisibility(View.GONE);
        } else if (directCome) {
            finish();
            startActivity(new Intent(this, Home.class).putExtra("COME", "CONTENT_VIEW"));
        }
        if (rightLowerShareMenu.isOpen()) {
            rightLowerShareMenu.close(true);
        } else if (rightLowerMenu.isOpen()) {
            rightLowerMenu.close(true);
        } else {
            back(BACK);
        }
    }

    private BroadcastReceiver onNotice = new BroadcastReceiver() {
        @Override
        public void onReceive(Context mContext, Intent intent) {
            if (activity_running) {
                // setData();
                if (intent.hasExtra("NETON")) {
                    Alert.showLog("Content View", "NETON NETON");
                    showAllData();
                }
            }
        }
    };

    @Override
    public void onServiceResponse(int i, Response<ResponseBody> response) {
        try
        {
            JSONObject object = new JSONObject(response.body().string());
            switch (i) {
                case LIKE_PARTICULAR:
                    break;
                case APPLY:
                    Toast.makeText(this, "" + object.getString("message"), Toast.LENGTH_SHORT).show();
                    break;
                case CALL_BACK:
                    Toast.makeText(this, "" + object.getString("message"), Toast.LENGTH_SHORT).show();
                    break;
                case CALL:
                    Toast.makeText(this, "" + object.getString("message"), Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT > 21 && ContextCompat.checkSelfPermission(ContentView.this, "android.permission.CALL_PHONE") != 0) {
                        ActivityCompat.requestPermissions(ContentView.this, new String[]{"android.permission.CALL_PHONE"}, 1);
                        return;
                    }
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.fromParts(
                            "tel", extensionRemoved, null));
                    startActivity(intent);
                    break;
                case INTEREST:
                    Toast.makeText(this, "" + object.getString("message"), Toast.LENGTH_SHORT).show();
                    break;
                case LIKE:
                    if (object.has("success")) {
                        if (object.getString("success").equalsIgnoreCase("true")) {
                            helper.updatePostLike(dataAs.getContentId(), "0");
                        } else {
                            helper.updatePostLike(dataAs.getContentId(), DataBaseStrings.DEFAULT_VALUE);
                        }
                    }
                    if (object.has("msg")) {
                        //Alert.showToast(ContentView.this,object.getString("msg"));
                    }
                    break;
                case COMMENT:
                    Alert.showToast(ContentView.this, "आपण यशस्वीरित्या एक टिप्पणी पोस्ट केले!");
                    comment_box.setText("");
                    RevSDK.destroyKeypad();
                    isComment=true;
                    callGetContentService(contentId);
                    break;
                case SAVE:
                    Alert.showLog("object", "RESPONSE-- " + object.toString());
                    break;
                case CONTENT_VIEW:
                    if (object.has("success"))
                    {
                        if (object.getString("success").equalsIgnoreCase("true")) {
                            dataAs = new ContentListingModel();
                            CommentList.clear();
                           /*0 used for unselect and 1 use for select in like,save*/
                            String _id = DataBaseStrings.DEFAULT_VALUE, language = DataBaseStrings.DEFAULT_VALUE, sectionId = DataBaseStrings.DEFAULT_VALUE,
                                    categoryId = DataBaseStrings.DEFAULT_VALUE, subCategoryId = DataBaseStrings.DEFAULT_VALUE, headline = DataBaseStrings.DEFAULT_VALUE,slug=DataBaseStrings.DEFAULT_VALUE,
                                    tagline = DataBaseStrings.DEFAULT_VALUE, sectionName = DataBaseStrings.DEFAULT_VALUE,
                                    description = DataBaseStrings.DEFAULT_VALUE, firstImage = DataBaseStrings.DEFAULT_VALUE,
                                    categoryName = DataBaseStrings.DEFAULT_VALUE, location = DataBaseStrings.DEFAULT_VALUE,
                                    subCategoryName = DataBaseStrings.DEFAULT_VALUE, like = "0", save = "0", totalLike = "0",
                                    commentCount = "0", shareCount = "0", viewCount = "0", creator_f_name = DataBaseStrings.DEFAULT_VALUE,
                                    creator_l_name = DataBaseStrings.DEFAULT_VALUE, dateOfCreation = DataBaseStrings.DEFAULT_VALUE, deleteStatus = "false", suggestedArticalList = DataBaseStrings.DEFAULT_VALUE;

                            JSONArray itemm = object.getJSONArray("response");

                            JSONObject item = itemm.getJSONObject(0);

                            if (item.has("_id")) {
                                _id = item.getString("_id");
                            }
                            if (item.has("language")) {
                                language = item.getString("language");
                            }
                            if (item.has("sectionId")) {
                                sectionId = item.getString("sectionId");
                            }
                            if (item.has("categoryId")) {
                                categoryId = item.getString("categoryId");
                            }
                            if (item.has("subCategoryId")) {
                                subCategoryId = item.getString("subCategoryId");
                            }
                            if (item.has("headline")) {
                                headline = item.getString("headline");
                            }
                            if (item.has("slug")) {
                                slug = item.getString("slug");
                            }
                            if (item.has("tagline")) {
                                tagline = item.getString("tagline");
                            }
                            if (item.has("sectionName")) {
                                sectionName = item.getString("sectionName");
                            }
                            if (item.has("categoryName")) {
                                categoryName = item.getString("categoryName");
                            }
                            if (item.has("subCategoryName")) {
                                subCategoryName = item.getString("subCategoryName");
                            }

                            if (item.has("thumbnailPicture")) {
                                firstImage = item.getString("thumbnailPicture");
                            }

                            if (item.has("liked")) {
                                if (item.getString("liked").equalsIgnoreCase("true")) {
                                    like = "1";
                                } else {
                                    like = "0";
                                }
                            }

                            if (item.has("likeCount")) {
                                totalLike = item.getString("likeCount");
                            }

                            if (item.has("commentCount")) {
                                commentCount = item.getString("commentCount");
                            }

                            if (item.has("pageView")) {
                                viewCount = item.getString("pageView");
                            }

                            if (item.has("saved")) {
                                if (item.getString("saved").equalsIgnoreCase("true")) {
                                    save = "1";
                                } else {
                                    save = "0";
                                }
                            }

                            if (item.has("saveCount")) {
                                shareCount = item.getString("saveCount");
                            }

                            if (item.has("deleteStatus")) {
                                deleteStatus = item.getString("deleteStatus");
                            }

                            if (item.has("userList"))
                            {
                                if (item.getJSONArray("userList").length() > 0) {
                                    JSONObject user = item.getJSONArray("userList").getJSONObject(0);
                                    if (user.has("firstName")) {
                                        creator_f_name = user.getString("firstName");
                                    }

                                    if (user.has("lastName")) {
                                        creator_l_name = user.getString("lastName");
                                    }
                                }
                            }

                            if (item.has("dateOfCreation")) {
                                dateOfCreation = item.getString("dateOfCreation");
                            }
                            dataAs.setContentId(_id);
                            dataAs.setLanguage(language);
                            dataAs.setSectionId(sectionId);
                            dataAs.setCategoryId(categoryId);
                            dataAs.setSubCategoryId(subCategoryId);
                            dataAs.setHeadline(headline);
                            dataAs.setSlug(slug);
                            dataAs.setTagline(tagline);
                            dataAs.setSectionName(sectionName);
                            dataAs.setCategoryName(categoryName);
                            dataAs.setSubCategoryName(subCategoryName);
                            dataAs.setContentLocation(location);
                            dataAs.setDescription(description);
                            dataAs.setLike(like);
                            dataAs.setTotal_like(totalLike);
                            dataAs.setSave(save);
                            dataAs.setShare_count(shareCount);
                            dataAs.setComment_count(commentCount);
                            dataAs.setView_count(viewCount);
                            dataAs.setCreator_first_name(creator_f_name);
                            dataAs.setCreator_last_name(creator_l_name);
                            dataAs.setDeleteStatus(deleteStatus);
                            dataAs.setDateOfCreation(dateOfCreation);
                            if (item.has("suggestedArticleList")) {
                                JSONArray suggestedArticleList = item.getJSONArray("suggestedArticleList");
                                StringBuffer result = new StringBuffer();
                                for (int sg = 0; sg < suggestedArticleList.length(); sg++) {
                                    if (sg == 0) {
                                        result.append(suggestedArticleList.getJSONObject(sg).getString("_id"));
                                    } else {
                                        result.append(",").append(suggestedArticleList.getJSONObject(sg).getString("_id"));
                                    }
                                }
                                suggestedArticalList = result.toString();
                            }
                            dataAs.setSuggestedArticleList(suggestedArticalList);
                            dataAs.setFirstImage(firstImage);
                            if (item.has("user_comments"))
                            {
                                String comment_id = DataBaseStrings.DEFAULT_VALUE,
                                        dateOfComment = DataBaseStrings.DEFAULT_VALUE,
                                        userId = DataBaseStrings.DEFAULT_VALUE,
                                        articleName = DataBaseStrings.DEFAULT_VALUE,
                                        articleId = DataBaseStrings.DEFAULT_VALUE,
                                        userPhone = DataBaseStrings.DEFAULT_VALUE,
                                        userComment = DataBaseStrings.DEFAULT_VALUE,
                                        userName = DataBaseStrings.DEFAULT_VALUE,
                                        status = DataBaseStrings.DEFAULT_VALUE,
                                        deleteeStatus = DataBaseStrings.DEFAULT_VALUE,
                                        user_image = DataBaseStrings.DEFAULT_VALUE;

                                JSONArray com_array1 = item.getJSONArray("user_comments");
                                Alert.showLog("SIZEEE","SIZE-- "+com_array1.toString());

                                if (com_array1.length() > 0) {
                                    for (int co = 0; co < com_array1.length(); co++) {
                                        JSONObject comment = com_array1.getJSONObject(co);
                                        if (comment.has("_id")) {
                                            comment_id = comment.getString("_id");
                                        }

                                        if (comment.has("dateOfComment")) {
                                            dateOfComment = comment.getString("dateOfComment");
                                        }

                                        if (comment.has("userId")) {
                                            userId = comment.getString("userId");
                                        }

                                        if (comment.has("articleName")) {
                                            articleName = comment.getString("articleName");
                                        }

                                        if (comment.has("articleId")) {
                                            articleId = comment.getString("articleId");
                                        }

                                        if (comment.has("userPhone")) {
                                            userPhone = comment.getString("userPhone");
                                        }

                                        if (comment.has("userComment")) {
                                            userComment = comment.getString("userComment");
                                        }

                                        if (comment.has("userName")) {
                                            userName = comment.getString("userName");
                                        }

                                        if (comment.has("status")) {
                                            status = comment.getString("status");
                                        }

                                        if (comment.has("deleteStatus")) {
                                            deleteeStatus = comment.getString("deleteStatus");
                                        }
                                        CommentListModel comm = new CommentListModel();
                                        comm.setComment_id(comment_id);
                                        comm.setDateOfComment(dateOfComment);
                                        comm.setUserId(userId);
                                        comm.setArticleName(articleName);
                                        comm.setArticleId(articleId);
                                        comm.setUserPhone(userPhone);
                                        comm.setUserComment(userComment);
                                        Alert.showLog("userComment","userComment-- "+userComment);

                                        comm.setUserName(userName);
                                        comm.setStatus(status);
                                        comm.setDeleteStatus(deleteeStatus);
                                        comm.setUser_image(user_image);
                                        CommentList.add(comm);
                                    }
                                }
                            }
                            directCome = true;
                            List<ContentListingModel> articleList = new ArrayList<>();
                            articleList.add(dataAs);
                            helper.storeAllPost(articleList);
                            helper.storeAllPostComments(CommentList);
                            if(isComment)
                            {
                                isComment=false;
                                setComment();
                            }
                            else
                            {
                                initial();
                                setContainFarm();
                            }
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getDataSource(String path) throws IOException {
        if (!URLUtil.isNetworkUrl(path)) {
            return path;
        } else {
            URL url = new URL(path);
            URLConnection cn = url.openConnection();
            cn.connect();
            InputStream stream = cn.getInputStream();
            if (stream == null)
                throw new RuntimeException("stream is null");
            File temp = File.createTempFile("mediaplayertmp", "dat");
            temp.deleteOnExit();
            String tempPath = temp.getAbsolutePath();
            FileOutputStream out = new FileOutputStream(temp);
            byte buf[] = new byte[128];
            do {
                int numread = stream.read(buf);
                if (numread <= 0)
                    break;
                out.write(buf, 0, numread);
            } while (true);
            try {
                stream.close();
            } catch (IOException ex) {
                Log.e("ContentView ved", "error: " + ex.getMessage(), ex);
            }
            return tempPath;
        }
    }


    private class AsyncCaller extends AsyncTask<String, Boolean, Boolean> {
        boolean status = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(String... args) {
            final int selectedLangId = SharedPreference.retrieveLang(ContentView.this);
            RevSDK.initLangResources("https://api-gw.revup.reverieinc.com/apiman-gateway/ReverieMobilitySdk", selectedLangId, new LangResourceInitCompleteListener() {
                public void onLangResourceInitComplete(int i, RevStatus revStatus) {
                    Alert.showLog("TAG", "INIT RESOURCE COMPLETE:  Lang code = " + i + " , Status = " + revStatus.getStatusMessage());
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
            Alert.showLog("LANGUAGE", "LANGUAGE status" + status);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // Do nothing now
        }
    }


    /*Set Bottom Floating Button*/
    private void setFloatMenus() {
        fabIconNew = new CircleImageView(this);
        fabIconNew.setImageDrawable(getResources().getDrawable(R.drawable.share));

        int redActionButtonSize = getResources().getDimensionPixelSize(R.dimen.red_action_button_size);
        int redActionButtonMargin = getResources().getDimensionPixelOffset(R.dimen.action_button_margin);
        int redActionButtonContentSize = getResources().getDimensionPixelSize(R.dimen.red_action_button_content_size);
        int redActionButtonContentMargin = getResources().getDimensionPixelSize(R.dimen.red_action_button_content_margin);
        int boottomMargin = getResources().getDimensionPixelSize(R.dimen.detail_bottom_fab_margin);


        FloatingActionButton.LayoutParams fabIconNew_starParams = new FloatingActionButton.LayoutParams(redActionButtonSize, redActionButtonSize);
        fabIconNew_starParams.setMargins(redActionButtonMargin,
                redActionButtonMargin,
                redActionButtonMargin,
                boottomMargin);
        fabIconNew.setLayoutParams(fabIconNew_starParams);

        FloatingActionButton.LayoutParams fabIconParams = new FloatingActionButton.LayoutParams(redActionButtonContentSize, redActionButtonContentSize);
        fabIconParams.setMargins(redActionButtonContentMargin,
                redActionButtonContentMargin,
                redActionButtonContentMargin,
                redActionButtonContentMargin);

        rightLowerShareButton = new FloatingActionButton.Builder(this)
                .setContentView(fabIconNew, fabIconParams)
                .setPosition(FloatingActionButton.POSITION_BOTTOM_RIGHT)
                .setLayoutParams(fabIconNew_starParams)
                .build();

        SubActionButton.Builder rLSubBuilder = new SubActionButton.Builder(this);
        FrameLayout lay1 = new FrameLayout(this);
        FrameLayout lay2 = new FrameLayout(this);
        FrameLayout lay3 = new FrameLayout(this);

        LayoutInflater li1 = LayoutInflater.from(getApplicationContext());
        View cv1 = li1.inflate(R.layout.fab_menu_item, null);
        ImageView fv_img1 = (ImageView) cv1.findViewById(R.id.image_option);
        TextView fv_txt1 = (TextView) cv1.findViewById(R.id.text_option);
        fv_img1.setImageDrawable(getResources().getDrawable(R.drawable.whatsapp_option));
        fv_txt1.setText(R.string.share_on_whatsapp);
        cv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareArticleSendDataToGoogle();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, sharedLink);
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.whatsapp");
                if (sendIntent.resolveActivity(getPackageManager()) != null) {
                    if (rightLowerShareMenu != null) {
                        rightLowerShareMenu.close(true);
                    }
                    startActivity(sendIntent);
                } else {
                    Alert.showToast(ContentView.this, getString(R.string.install_whatsapp));
                }
            }
        });

        LayoutInflater li2 = LayoutInflater.from(getApplicationContext());
        View cv2 = li2.inflate(R.layout.fab_menu_item, null);
        ImageView fv_img2 = (ImageView) cv2.findViewById(R.id.image_option);
        TextView fv_txt2 = (TextView) cv2.findViewById(R.id.text_option);
        fv_img2.setImageDrawable(getResources().getDrawable(R.drawable.mail_option));
        fv_txt2.setText(R.string.share_via_text_message);
        cv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    shareArticleSendDataToGoogle();
                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    sendIntent.putExtra("sms_body", sharedLink);
                    sendIntent.setType("vnd.android-dir/mms-sms");
                    startActivity(sendIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        LayoutInflater li3 = LayoutInflater.from(getApplicationContext());
        View cv3 = li3.inflate(R.layout.fab_menu_item, null);
        ImageView fv_img3 = (ImageView) cv3.findViewById(R.id.image_option);
        TextView fv_txt3 = (TextView) cv3.findViewById(R.id.text_option);
        fv_img3.setImageDrawable(getResources().getDrawable(R.drawable.fb_option));
        fv_txt3.setText(R.string.share_on_facebook);
        cv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareArticleSendDataToGoogle();

                Alert.showLog("link","link-- "+sharedLink);
                ShareOpenGraphObject object = new ShareOpenGraphObject.Builder()
                        .putString("og:title", dataAs.getHeadline())
                        .putString("og:url",sharedLink)
                        .putString("og:image",dataAs.getFirstImage())
                        .putString("fb:app_id", getString(R.string.app_id))
                        .putString("og:description", dataAs.getTagline())
                        .build();

                ShareOpenGraphAction action = new ShareOpenGraphAction.Builder()
                        .setActionType("og.shares")
                        .putObject("object", object)
                        .build();

                ShareOpenGraphContent content = new ShareOpenGraphContent.Builder()
                        .setPreviewPropertyName("object")
                        .setAction(action)
                        .build();

                ShareDialog dialog=new ShareDialog(ContentView.this);
               /* callbackManager=init(ContentView.this);
                dialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        Toast.makeText(ContentView.this, "You shared this post", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        Log.e("e","onCancel-- ");
                    }

                    @Override
                    public void onError(FacebookException e) {
                        Log.e("e","FacebookException-- "+e.getMessage());
                        e.printStackTrace();
                    }
                });*/
                dialog.show(content);
            }
        });

        lay1.addView(cv1);
        lay2.addView(cv2);
        lay3.addView(cv3);

        lay1.setLayoutParams(fabIconNew_starParams);
        lay2.setLayoutParams(fabIconNew_starParams);
        lay3.setLayoutParams(fabIconNew_starParams);

        rightLowerShareMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(rLSubBuilder.setContentView(lay1, fabIconParams).setBackgroundDrawable(getResources().getDrawable(R.drawable.dim_back)).setLayoutParams(fabIconNew_starParams).build())
                .addSubActionView(rLSubBuilder.setContentView(lay2, fabIconParams).setBackgroundDrawable(getResources().getDrawable(R.drawable.dim_back)).setLayoutParams(fabIconNew_starParams).build())
                .addSubActionView(rLSubBuilder.setContentView(lay3, fabIconParams).setBackgroundDrawable(getResources().getDrawable(R.drawable.dim_back)).setLayoutParams(fabIconNew_starParams).build())
                .attachTo(rightLowerShareButton)
                .build();

        shareContent();
        setHelpFloat();
    }

    public static CallbackManager init(Activity c) {
        if (!FacebookSdk.isInitialized()) {
            FacebookSdk.sdkInitialize(c);
        }
        return CallbackManager.Factory.create();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(rightLowerShareMenu!=null)
        {
            if(rightLowerShareMenu.isOpen())
            {
                rightLowerShareMenu.close(true);
            }
        }
    }

    private void shareContent() {
        rightLowerShareMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu menu) {
                sendDownloadData("Share");
                // Rotate the icon of rightLowerButton 45 degrees clockwise
                fabIconNew.setRotation(0);
                bac_dim_layout.setVisibility(View.VISIBLE);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 360);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fabIconNew, pvhR);
                animation.start();

                if (rightLowerMenu != null) {
                    if (rightLowerMenu.isOpen()) {
                        rightLowerMenu.close(false);
                        bac_dim_layout.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onMenuClosed(FloatingActionMenu menu) {
                fabIconNew.setRotation(360);
                bac_dim_layout.setVisibility(View.GONE);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 0);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fabIconNew, pvhR);
                animation.start();
            }
        });
    }

    private void setHelpFloat() {
        final CircleImageView fabIconNew = new CircleImageView(this);
        fabIconNew.setImageDrawable(getResources().getDrawable(R.drawable.help));

        int redActionButtonSize = getResources().getDimensionPixelSize(R.dimen.red_action_button_size);
        int redActionButtonMargin = getResources().getDimensionPixelOffset(R.dimen.action_button_margin);
        int redActionButtonContentSize = getResources().getDimensionPixelSize(R.dimen.red_action_button_content_size);
        int redActionButtonContentMargin = getResources().getDimensionPixelSize(R.dimen.red_action_button_content_margin);
        int boottomMargin = getResources().getDimensionPixelSize(R.dimen.bottom_fab_margin);

        FloatingActionButton.LayoutParams fabIconNew_starParams = new FloatingActionButton.LayoutParams(redActionButtonSize, redActionButtonSize);
        fabIconNew_starParams.setMargins(redActionButtonMargin,
                redActionButtonMargin,
                redActionButtonMargin,
                boottomMargin);
        fabIconNew.setLayoutParams(fabIconNew_starParams);

        FloatingActionButton.LayoutParams fabIconParams = new FloatingActionButton.LayoutParams(redActionButtonContentSize, redActionButtonContentSize);
        fabIconParams.setMargins(redActionButtonContentMargin,
                redActionButtonContentMargin,
                redActionButtonContentMargin,
                redActionButtonContentMargin);

        rightLowerHelpButton = new FloatingActionButton.Builder(this)
                .setContentView(fabIconNew, fabIconParams)
                .setPosition(FloatingActionButton.POSITION_BOTTOM_RIGHT)
                .setLayoutParams(fabIconNew_starParams)
                .build();

        SubActionButton.Builder rLSubBuilder = new SubActionButton.Builder(this);
        FrameLayout lay1 = new FrameLayout(this);
        FrameLayout lay2 = new FrameLayout(this);
        FrameLayout lay3 = new FrameLayout(this);

        LayoutInflater li1 = LayoutInflater.from(getApplicationContext());
        View cv1 = li1.inflate(R.layout.fab_menu_item, null);
        ImageView fv_img1 = (ImageView) cv1.findViewById(R.id.image_option);
        TextView fv_txt1 = (TextView) cv1.findViewById(R.id.text_option);
        fv_img1.setImageDrawable(getResources().getDrawable(R.drawable.help_option));
        fv_txt1.setText(R.string.go_to_our_help_section);
        cv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightLowerMenu.close(true);
                startActivity(new Intent(ContentView.this, FAQSection.class));
            }
        });

        LayoutInflater li2 = LayoutInflater.from(getApplicationContext());
        View cv2 = li2.inflate(R.layout.fab_menu_item, null);
        ImageView fv_img2 = (ImageView) cv2.findViewById(R.id.image_option);
        TextView fv_txt2 = (TextView) cv2.findViewById(R.id.text_option);
        fv_img2.setImageDrawable(getResources().getDrawable(R.drawable.whatsapp_option));
        fv_txt2.setText(R.string.whatsapp_for_help);
        cv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightLowerMenu.close(true);
                Constants.startWhatsAppForHelp(ContentView.this, "NETPAR", rightLowerMenu);
            }
        });

        LayoutInflater li3 = LayoutInflater.from(getApplicationContext());
        View cv3 = li3.inflate(R.layout.fab_menu_item, null);
        ImageView fv_img3 = (ImageView) cv3.findViewById(R.id.image_option);
        TextView fv_txt3 = (TextView) cv3.findViewById(R.id.text_option);
        fv_img3.setImageDrawable(getResources().getDrawable(R.drawable.call_option));
        fv_txt3.setText(R.string.call_for_help);
        cv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.startHelpCall(ContentView.this, rightLowerMenu);
            }
        });

        lay1.addView(cv1);
        lay2.addView(cv2);
        lay3.addView(cv3);

        lay1.setLayoutParams(fabIconNew_starParams);
        lay2.setLayoutParams(fabIconNew_starParams);
        lay3.setLayoutParams(fabIconNew_starParams);

        lay3.setVisibility(View.INVISIBLE);

        rightLowerMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(rLSubBuilder.setContentView(lay1, fabIconParams).setBackgroundDrawable(getResources().getDrawable(R.drawable.dim_back)).setLayoutParams(fabIconNew_starParams).build())
                .addSubActionView(rLSubBuilder.setContentView(lay2, fabIconParams).setBackgroundDrawable(getResources().getDrawable(R.drawable.dim_back)).setLayoutParams(fabIconNew_starParams).build())
                .addSubActionView(rLSubBuilder.setContentView(lay3, fabIconParams).setBackgroundDrawable(getResources().getDrawable(R.drawable.dim_back)).setLayoutParams(fabIconNew_starParams).build())
                .attachTo(rightLowerHelpButton)
                .build();

        rightLowerMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu menu) {
                if (rightLowerShareButton != null) {
                    if (rightLowerShareMenu != null) {
                        if (rightLowerShareMenu.isOpen()) {
                            rightLowerShareMenu.close(false);
                        }
                    }
                    rightLowerShareButton.setVisibility(View.GONE);
                }
                fabIconNew.setRotation(0);
                bac_dim_layout.setVisibility(View.VISIBLE);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 360);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fabIconNew, pvhR);
                animation.start();
            }

            @Override
            public void onMenuClosed(FloatingActionMenu menu) {
                fabIconNew.setRotation(360);
                bac_dim_layout.setVisibility(View.GONE);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 0);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fabIconNew, pvhR);
                animation.start();
                if (rightLowerShareButton != null) {
                    rightLowerShareButton.setVisibility(View.VISIBLE);
                }
            }
        });

        bac_dim_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (rightLowerShareMenu.isOpen()) {
                        rightLowerShareMenu.close(true);
                    } else if (rightLowerMenu.isOpen()) {
                        rightLowerMenu.close(true);
                    } else {
                        bac_dim_layout.setVisibility(View.GONE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                suggest_way.setVisibility(View.GONE);
            }
        });
    }

    /*Show All Comments*/
    private void showAllCommentAlert() {
        if (cm_model.size() > 0) {
            list.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
            all_commentadapter = new AllCommentAdapter(ContentView.this, cm_model);
            list.setAdapter(all_commentadapter);
            list.setNestedScrollingEnabled(false);
        }
    }

    private void callLikeService() {
      /*  Tracker t = ((AnalyticsSampleApp) getActivity().getApplication()).getTracker(TrackerName.APP_TRACKER);
        t.send(new HitBuilders.EventBuilder()
                .setCategory(getString(categoryId))
                .setAction(getString(actionId))
                .setLabel(getString(labelId))
                .build());

       // https://developers.google.com/analytics/devguides/collection/android/v4/events
        t.send(new HitBuilders.EventBuilder()
                .setCategory(getString(categoryId))
                .setAction(getString(actionId))
                .setNonInteraction(true)
                .build());
     */
        try {
            JSONObject peram = new JSONObject();
            peram.put("userId", SharedPreference.retrieveData(ContentView.this, Constants.USER_ID));
            peram.put("articleName", title.getText().toString());
            peram.put("articleId", dataAs.getContentId());
            peram.put("userPhone", SharedPreference.retrieveData(ContentView.this, Constants.MOBILE_NUMBER));
            new Retrofit2(this, this, LIKE, Constants.LIKE_ARTICLE, peram).callService(false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void callWebViewServiceContent(String url, int code) {
        new Retrofit2(this, this, code, url + "/" + SharedPreference.retrieveData(ContentView.this, Constants.USER_ID) + "/" + dataAs.getContentId()).callService(false);
    }

    private void callSaveService() {
        try {
            JSONObject peram = new JSONObject();
            peram.put("user_id", SharedPreference.retrieveData(ContentView.this, Constants.USER_ID));
            peram.put("content_name", dataAs.getHeadline());
            peram.put("content_id", dataAs.getContentId());
            new Retrofit2(this, this, SAVE, Constants.SAVE_ARTICLE, peram).callService(false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void callSaveLiked(String postion, String orderNo, String tag) {
        try {
            JSONObject peram = new JSONObject();
            peram.put("orderNo", orderNo);
            peram.put("tag", tag);
            peram.put("contentId", dataAs.getContentId());
            peram.put("postion", postion);
            new Retrofit2(this, this, LIKE_PARTICULAR, Constants.likeParticularSection, peram).callService(false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class MyClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (ConnectionDetector.isInternetAvailable(ContentView.this)) {
                contrabution_frame_webView.setVisibility(View.VISIBLE);

                if (!directCome) {
                    if (startWebLoader) {
                        startWebLoader = false;
                        alert = Alert.startDialog(ContentView.this);
                    }
                }
            } else {
                contrabution_frame_webView.setVisibility(View.GONE);
                Alert.showToast(ContentView.this, getString(R.string.connect_internet_first));
            }
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.e("CurrentUrlByOVERRIDE", "CurrentUrlByOVERRIDE-- " + url + "");
            showAllData();
            if (url.contains("like")) {
                String urli = url.toString();
                String[] separated = urli.split("/");
                String tag = separated[3];
                String orderNo1 = separated[4];
                String postio1n = separated[5];
                callSaveLiked(postio1n, orderNo1, tag);
            } else if (url.contains("comment")) {
                setCommentClick();
            } else if (url.contains("share")) {
                //shareContent();
                rightLowerShareMenu.open(true);
                //inviteMultipleFriendDialog(ContentView.this);
            } else if (url.contains("tel")) {
                extensionRemoved = url.split(":")[1];
                callWebViewServiceContent(Constants.call, CALL);
                sendDownloadData("Call");
            } else if (url.contains("interested")) {
                callWebViewServiceContent(Constants.interested, INTEREST);
                sendDownloadData("Call-Me-Back");

            } else if (url.contains("callmeback")) {
                callWebViewServiceContent(Constants.callmeback, CALL_BACK);
                sendDownloadData("Interested");
            } else if (url.contains("apply")) {
                callWebViewServiceContent(Constants.apply, APPLY);
                sendDownloadData("Apply");
            }
            else if(url.contains("abc"))
            {
                //Click on view more
            }
            else
                {
                    Log.e("DOWNOAD","OK URL-- "+url);
                setDownloadData(url);
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (view.getProgress() == 100) {
                if (alert != null) {
                    try {
                        alert.cancel();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (search_alert != null) {
                    try {
                        search_alert.cancel();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public class MyChromeClient extends WebChromeClient {
        // TODO: 3/28/17 get Java Script ConsoleMessage
        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            Log.e("onConsoleMessage", consoleMessage.message());
            return true;
        }
    }

    public class JavaScriptInterface {
        Context mContext;

        JavaScriptInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void jumpToItem(int toast) {
            Toast.makeText(mContext, toast + "", Toast.LENGTH_SHORT).show();
        }
    }

    private void setContainFarm() {
        updateWebView();
    }

    /*Update WebView Data*/
    private void updateWebView() {
        String urlMain = Constants.WEB_VIEW_URL_FOR_CONTENT_VIEW + dataAs.getContentId();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            contrabution_frame_webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        }
        contrabution_frame_webView.getSettings().setAppCacheMaxSize(5 * 1024 * 1024);
        contrabution_frame_webView.getSettings().setAppCachePath(getCacheDir().getAbsolutePath());

        contrabution_frame_webView.getSettings().setDatabaseEnabled(true);
        contrabution_frame_webView.getSettings().setDatabasePath(getFilesDir().getAbsolutePath() + "/databases");

        contrabution_frame_webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        contrabution_frame_webView.getSettings().setDomStorageEnabled(true);
        contrabution_frame_webView.getSettings().setAllowFileAccess(true);
        contrabution_frame_webView.getSettings().setAppCacheEnabled(true);
        contrabution_frame_webView.getSettings().setJavaScriptEnabled(true);
        contrabution_frame_webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);

        contrabution_frame_webView.getSettings().setUseWideViewPort(true);
        contrabution_frame_webView.setScrollbarFadingEnabled(false);
        contrabution_frame_webView.getSettings().setLoadWithOverviewMode(true);
        contrabution_frame_webView.setWebChromeClient(new MyChromeClient());
        contrabution_frame_webView.setVerticalScrollBarEnabled(false);
        contrabution_frame_webView.setHorizontalScrollBarEnabled(false);

        final JavaScriptInterface myJavaScriptInterface = new JavaScriptInterface(this);
        contrabution_frame_webView.getSettings().setLightTouchEnabled(true);
        contrabution_frame_webView.addJavascriptInterface(myJavaScriptInterface, "AndroidFunction");
        contrabution_frame_webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        // TODO: 1/9/17 By Dheeraj
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            contrabution_frame_webView.getSettings().setAllowFileAccessFromFileURLs(true);
        }
        contrabution_frame_webView.getSettings().setLoadsImagesAutomatically(true);
        contrabution_frame_webView.setWebViewClient(new MyClient());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            contrabution_frame_webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        }
        contrabution_frame_webView.loadUrl(urlMain);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //callbackManager.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REGISTER_FORM_CONTENT_VIEW:
                if (resultCode == RESULT_OK) {
                    functionToOpenArticleDirectly(data.getStringExtra(CONTENT_ID_FORM_CONTENT_VIEW));
                } else {
                    finish();
                }
                break;
            case OPEN_SUGGESTED_ARTICLES_CONTENT_VIEW:
                if(resultCode==Activity.RESULT_OK)
                {
                    int result = data.getIntExtra("VALUE",0);
                    if(data.hasExtra("LIST"))
                    {
                        Home.LIST= (ContentListingModel) data.getSerializableExtra("LIST");
                    }
                    switch (result)
                    {
                        case ContentView.HOME:
                            break;
                        case ContentView.BACK:
                            break;
                        case ContentView.CONTRIBUTION:
                            backSheet(result);
                            break;
                        case ContentView.ACCOUNT:
                            break;
                        case ContentView.DOWNLOAD:
                            backSheet(result);
                            break;
                        case ContentView.FRIEND:
                            backSheet(result);
                            break;
                        case ContentView.MY:
                            backSheet(result);
                            break;
                        case ContentView.PROFILE:
                            backSheet(result);
                            break;
                        case ContentView.SAVED:
                            backSheet(result);
                            break;
                    }
                }
                break;
        }
    }

    /*Only to update data of Google Analytics*/
    private void shareArticleSendDataToGoogle() {
        Controller.getInstance().getGoogleAnalyticsTracker().send(new HitBuilders.EventBuilder()
                .setCategory(dataAs.getCategoryName())
                .setAction("Share")
                .build());
    }

    private void sendDownloadData(String value) {
        Controller.getInstance().trackEvent("Engagement", value, dataAs.getHeadline(), dataAs.getItem_id());
    }

    /*Search Article*/
    private class SearchArticleAsyncCaller extends AsyncTask<String, Boolean, Boolean> {
        boolean status = false;
        boolean isFound = false;
        private String contentId;
        private List<ContentListingModel> dataBaseList;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                search_alert = Alert.startDialog(ContentView.this);
                dataBaseList = helper.getAllPost();
            } catch (Exception e) {
                Alert.showLog("Exception", "Exception- " + e.toString());
            }
        }

        @Override
        protected Boolean doInBackground(String... args) {
            contentId = args[0];
            Alert.showLog("DATA SearchArticleAsyncCaller", "SearchArticleAsyncCaller-- " + contentId);//5a0ee2d2373738e62635399b
            for (ContentListingModel data_val : dataBaseList) {
                if (contentId.equalsIgnoreCase(data_val.getContentId())) {
                    dataAs = data_val;
                    status = true;
                    break;
                }
            }
            return status;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                initial();
                setContainFarm();
            } else {
                callGetContentService(contentId);
            }
        }
    }

    public void inviteMultipleFriendDialog(final Context context) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.custom_alert_dialog);
        View dialogView = ((AppCompatActivity) context).getLayoutInflater().inflate(R.layout.alert_invite_friend, null);
        ImageView fb_option = (ImageView) dialogView.findViewById(R.id.fb_option);
        ImageView whats_app_option = (ImageView) dialogView.findViewById(R.id.whats_app_option);
        ImageView message_option = (ImageView) dialogView.findViewById(R.id.message_option);
        TextView title = (TextView) dialogView.findViewById(R.id.title);
        title.setText("");
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.getWindow().setLayout((int) (((double) context.getResources().getDisplayMetrics().widthPixels) * 0.9d), -2);
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_back);
        alertDialog.setCanceledOnTouchOutside(false);
        fb_option.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alertDialog.dismiss();
                Constants.shareAppLinkViaFacebook(ContentView.this, sharedLink, rightLowerShareMenu);
            }
        });
        whats_app_option.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alertDialog.dismiss();
                shareArticleSendDataToGoogle();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, sharedLink);
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.whatsapp");
                if (sendIntent.resolveActivity(getPackageManager()) != null) {
                    if (rightLowerShareMenu != null) {
                        rightLowerShareMenu.close(true);
                    }
                    startActivity(sendIntent);
                } else {
                    Alert.showToast(ContentView.this, getString(R.string.install_whatsapp));
                }
            }
        });
        message_option.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alertDialog.dismiss();
                try {
                    shareArticleSendDataToGoogle();
                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    sendIntent.putExtra("sms_body", sharedLink);
                    sendIntent.setType("vnd.android-dir/mms-sms");
                    startActivity(sendIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        alertDialog.show();
    }

    private void setBottomSheet() {
        /*Bottom Sheet Widget*/
        bg = (View) findViewById(R.id.bg);
        sheet_profile_lay = (LinearLayout) findViewById(R.id.sheet_profile_lay);
        sheet_save = (LinearLayout) findViewById(R.id.sheet_save);
        sheet_download = (LinearLayout) findViewById(R.id.sheet_download);
        sheet_friends = (LinearLayout) findViewById(R.id.sheet_friends);
        sheet_my_contribution = (LinearLayout) findViewById(R.id.sheet_my_contribution);
        LinearLayout bottomSheetLayout = (LinearLayout) findViewById(R.id.design_bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    bg.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                bg.setVisibility(View.VISIBLE);
                bg.setAlpha(slideOffset);
            }
        });

        bg.setOnClickListener(this);
        sheet_profile_lay.setOnClickListener(this);
        sheet_save.setOnClickListener(this);
        sheet_download.setOnClickListener(this);
        sheet_friends.setOnClickListener(this);
        sheet_my_contribution.setOnClickListener(this);

        if (bottomSheetBehavior != null) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    public void expendBottomSheet() {
        if (bottomSheetBehavior != null) {
            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }
    }

    public void closeBottomSheet() {
        if (bottomSheetBehavior != null) {
            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }
    }


}
