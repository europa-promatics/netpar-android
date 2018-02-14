package co.netpar;

import android.Manifest;
import android.accounts.Account;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.flurry.android.FlurryAgent;
import com.github.arturogutierrez.Badges;
import com.github.arturogutierrez.BadgesNotSupportedException;
import com.reverie.manager.LangResourceInitCompleteListener;
import com.reverie.manager.RevSDK;
import com.reverie.manager.RevStatus;
import com.reverie.manager.ValidationCompleteListener;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import co.netpar.Adapter.NVSectionExpandableListAdapter;
import co.netpar.App.Controller;
import co.netpar.Comman.Alert;
import co.netpar.Comman.Constants;
import co.netpar.Fragment.CategoryFragment;
import co.netpar.Fragment.ContactListFragment;
import co.netpar.Fragment.ContentListingAfterFilter;
import co.netpar.Fragment.Contribution;
import co.netpar.Fragment.HomeFragment;
import co.netpar.Fragment.MyAccountFragment;
import co.netpar.Fragment.Notifications;
import co.netpar.Fragment.SavedItems;
import co.netpar.Fragment.SubCategoryFragment;
import co.netpar.Keyboard.KeyboardCredentialConstants;
import co.netpar.Model.CategoryModel;
import co.netpar.Model.ContactsModel;
import co.netpar.Model.ContentListingModel;
import co.netpar.Model.SectionModel;
import co.netpar.Model.SubCategoryModel;
import co.netpar.Notification.MyFirebaseMessagingService;
import co.netpar.PagerAdapter.HomePagerAdapter;
import co.netpar.Retrofit.Retrofit2;
import co.netpar.Retrofit.ServiceResponse;
import co.netpar.Syncronization.CallService;
import co.netpar.Syncronization.NetparDataBase.DataBaseStrings;
import co.netpar.Syncronization.NetparDataBase.DatabaseHelper;
import co.netpar.Syncronization.NetparDataBase.SharedPreference;
import co.netpar.Widget.CustomViewPager;
import co.netpar.Widget.FloatingMenu.FloatingActionButton;
import co.netpar.Widget.FloatingMenu.FloatingActionMenu;
import co.netpar.Widget.FloatingMenu.SubActionButton;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class Home extends AppCompatActivity implements ServiceResponse,AdapterView.OnItemClickListener,View.OnClickListener{
    private LinearLayout contabution_lay,my_account_lay,home_lay;
    private ImageView home_image,contrabution_image,my_account_image;
    private TextView my_account_text,home_text,user_name;
    private EditText dob,address,sex,phone;
    private CustomViewPager viewpager;
    private Animation show_fab_1,hide_fab_1 ;
    private RelativeLayout profile_lay,section_lay;
    private LinearLayout nv_home_lay,contribute_lay,invite_lay,rate_lay,about_lay,privacy_lay,faq_lay;
    public ExpandableListView section_list;
    private ListView profile_list;
    private NVSectionExpandableListAdapter sectionAdapter;
    private List<SectionModel> listDataHeader;
    private HashMap<String, List<CategoryModel>> listDataChild;
    private DrawerLayout drawer;
    private ImageView home_img,left_pro_img,cont_img,inv_img,left_sec_img,rate_img,about_img,privacy_img,faq_img,right_sec_img,right_pro_img;
    private TextView home_txt,pro_txt,cont_txt,inv_txt,sec_txt,rate_txt,about_txt,privacy_txt,faq_txt;
    public boolean activity_running=false;
    private final int PAGER_ITEMS=10;
    public DatabaseHelper helper;
    private RelativeLayout bac_dim_layout;
    private FloatingActionMenu rightLowerMenu;
    private static final int session_count=0,AddDevice=1,REMOVE_DEVICE=1;
    private Object mSyncMonitor;
    private List<ContactsModel> contactsList = new ArrayList<>();
    private final int PERMISSION_REQUEST_CONTACT=100;

    private ScrollView side_nested_scroll;

    /*Top Corner Icon*/
    private ImageView logout,search,menu_items;
    private RelativeLayout notification;
    private TextView count;

    private View bg;;
    private LinearLayout sheet_profile_lay,sheet_save,sheet_download,sheet_friends,sheet_my_contribution,sheet_logout;
    private static BottomSheetBehavior bottomSheetBehavior;
    private  MyAccountFragment instance;
    public static String back_temp="0";
    public Account account;
    public static ContentListingModel LIST=null;
    public CallService fatchData;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        sendInItDataToFlurry();
        setContentView(R.layout.activity_home);
       // LocalBroadcastManager.getInstance(Home.this).registerReceiver(onNotice, new IntentFilter("connected"));

        if(getIntent().getExtras()!=null)
        {
            /*Add Device For Notification*/
            JSONObject object=new JSONObject();
            try {
                object.put("id",SharedPreference.retrieveData(this,Constants.USER_ID));
                object.put("deviceToken",Constants.getFcmDeviceID(this));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new Retrofit2(this,this,AddDevice,Constants.ADD_DEVICE_FCM,object).callService(false);

            /*Validate Keyboard Licence*/
            RevSDK.validateKey(getApplicationContext(), "https://api-gw.revup.reverieinc.com/apiman-gateway/ReverieMobilitySdk", KeyboardCredentialConstants.SDK_TEST_API_KEY, "co.netpar", new ValidationCompleteListener() {
                @Override
                public void onValidationComplete(int statusCode, String statusMessage)
                {
                    Alert.showLog("Language", "LICENSE VALIDATION COMPLETE : " + statusCode + " ," + statusMessage);
                    if (statusCode != 1)
                    {
                        //Alert.showToast(Language.this, statusMessage);
                    }
                    else if(statusCode == 3)
                    {
                      //  Alert.showToast(Home.this,getString(R.string.connect_internet_first));
                    }
                    else if(statusCode == 10)
                    {
                        //  Alert.showToast(Home.this,getString(R.string.connect_internet_first));
                    }
                    else
                    {
                        new AsyncCaller().execute();
                    }
                }
            });
        }

        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= 21)
        {
            window.setStatusBarColor(getResources().getColor(R.color.top_bar_bg_color));
        }
        window.setBackgroundDrawableResource(R.color.gray_back);
        helper = new DatabaseHelper(Home.this);
        setToolWithNavigation();
        initializeView();
        setHelpFloat();

         /* Fatch My Post*/
        fatchData = new CallService(Home.this);
        fatchData.setOnUpdate(new CallService.ResponseSynchronous() {
            @Override
            public void UpdateData(int service_val) {
                if(service_val==CallService.SECTION_LIST || service_val==CallService.ARTICLE_LIST)
                {
                    HomeFragment.getInstance().setData();
                    if(CategoryFragment.getInstance()!=null)
                    {
                        CategoryFragment.getInstance().setData();
                    }
                    if(SubCategoryFragment.getInstance()!=null)
                    {
                        SubCategoryFragment.getInstance().setData();
                    }
                    if(ContentListingAfterFilter.getInstance()!=null)
                    {
                        ContentListingAfterFilter.getInstance().setAutoRefresh();
                    }
                    prepareSectionListData();
                }
                else if(service_val==CallService.SYNC_CONTACT_LIST)
                {
                    allFriendFromList();
                }
                else if(service_val==CallService.GETALLNOTIFICATION)
                {
                    if(Notifications.getInstance()!=null)
                    {
                        Notifications.getInstance().setNotificationData();
                    }
                }
            }
        });
        fatchData.runAllGetService();
        fatchData.callMyPostItemService();
    }

    /*-------------- set help button option ---------------------*/
    private void setHelpFloat()
    {
        final CircleImageView fabIconNew=new CircleImageView(this);
        fabIconNew.setImageDrawable(getResources().getDrawable(R.drawable.help));

        int redActionButtonSize = getResources().getDimensionPixelSize(R.dimen.red_action_button_size);
        int redActionButtonMargin = getResources().getDimensionPixelOffset(R.dimen.action_button_margin);
        int redActionButtonContentSize = getResources().getDimensionPixelSize(R.dimen.red_action_button_content_size);
        int redActionButtonContentMargin = getResources().getDimensionPixelSize(R.dimen.red_action_button_content_margin);
        int boottomMargin=getResources().getDimensionPixelSize(R.dimen.bottom_fab_margin);

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

        final FloatingActionButton rightLowerButton = new FloatingActionButton.Builder(this)
                .setContentView(fabIconNew, fabIconParams)
                .setPosition(FloatingActionButton.POSITION_BOTTOM_RIGHT)
                .setLayoutParams(fabIconNew_starParams)
                .build();

        SubActionButton.Builder rLSubBuilder = new SubActionButton.Builder(this);
        FrameLayout lay1= new FrameLayout(this);
        FrameLayout lay2= new FrameLayout(this);
        FrameLayout lay3= new FrameLayout(this);

        LayoutInflater li1 = LayoutInflater.from(getApplicationContext());
        View cv1 = li1.inflate(R.layout.fab_menu_item, null);
        ImageView fv_img1=(ImageView)cv1.findViewById(R.id.image_option);
        TextView fv_txt1=(TextView)cv1.findViewById(R.id.text_option);
        fv_img1.setImageDrawable(getResources().getDrawable(R.drawable.help_option));
        fv_txt1.setText(R.string.go_to_our_help_section);
        cv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightLowerMenu.close(true);
             startActivity(new Intent(Home.this,FAQSection.class));
            }
        });

        LayoutInflater li2 = LayoutInflater.from(getApplicationContext());
        View cv2 = li2.inflate(R.layout.fab_menu_item, null);
        ImageView fv_img2=(ImageView)cv2.findViewById(R.id.image_option);
        TextView fv_txt2=(TextView)cv2.findViewById(R.id.text_option);
        fv_img2.setImageDrawable(getResources().getDrawable(R.drawable.whatsapp_option));
        fv_txt2.setText(R.string.whatsapp_for_help);
        cv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightLowerMenu.close(true);
                Constants.startWhatsAppForHelp(Home.this,"NETPAR", rightLowerMenu);
            }
        });

        LayoutInflater li3 = LayoutInflater.from(getApplicationContext());
        View cv3 = li3.inflate(R.layout.fab_menu_item, null);
        ImageView fv_img3=(ImageView)cv3.findViewById(R.id.image_option);
        TextView fv_txt3=(TextView)cv3.findViewById(R.id.text_option);
        fv_img3.setImageDrawable(getResources().getDrawable(R.drawable.call_option));
        fv_txt3.setText(R.string.call_for_help);
        cv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightLowerMenu.close(true);
             Constants.startHelpCall(Home.this,rightLowerMenu);
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
                .addSubActionView(rLSubBuilder.setContentView(lay1,fabIconParams).setBackgroundDrawable(getResources().getDrawable(R.drawable.dim_back)).setLayoutParams(fabIconNew_starParams).build())
                .addSubActionView(rLSubBuilder.setContentView(lay2,fabIconParams).setBackgroundDrawable(getResources().getDrawable(R.drawable.dim_back)).setLayoutParams(fabIconNew_starParams).build())
                .addSubActionView(rLSubBuilder.setContentView(lay3,fabIconParams).setBackgroundDrawable(getResources().getDrawable(R.drawable.dim_back)).setLayoutParams(fabIconNew_starParams).build())
                .attachTo(rightLowerButton)
                .build();

        rightLowerMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu menu) {
                // Rotate the icon of rightLowerButton 45 degrees clockwise
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
            }
        });

        bac_dim_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightLowerMenu.close(true);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        activity_running=true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        activity_running=false;
    }

    /*---------- set tool bar with navigation view ---------*/
    private void setToolWithNavigation()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        side_nested_scroll=(ScrollView)navigationView.findViewById(R.id.side_nested_scroll);

        profile_lay=(RelativeLayout)navigationView.findViewById(R.id.profile_lay);
        section_lay=(RelativeLayout)navigationView.findViewById(R.id.section_lay);
        nv_home_lay=(LinearLayout) navigationView.findViewById(R.id.nv_home_lay);
        contribute_lay=(LinearLayout) navigationView.findViewById(R.id.contribute_lay);
        invite_lay=(LinearLayout) navigationView.findViewById(R.id.invite_lay);
        rate_lay=(LinearLayout) navigationView.findViewById(R.id.rate_lay);
        about_lay=(LinearLayout) navigationView.findViewById(R.id.about_lay);
        privacy_lay=(LinearLayout) navigationView.findViewById(R.id.privacy_lay);
        faq_lay=(LinearLayout)navigationView.findViewById(R.id.faq_lay);

        home_img=(ImageView) navigationView.findViewById(R.id.home_img);
        left_pro_img=(ImageView) navigationView.findViewById(R.id.left_pro_img);
        cont_img=(ImageView) navigationView.findViewById(R.id.cont_img);
        inv_img=(ImageView) navigationView.findViewById(R.id.inv_img);
        left_sec_img=(ImageView) navigationView.findViewById(R.id.left_sec_img);
        rate_img=(ImageView) navigationView.findViewById(R.id.rate_img);
        about_img=(ImageView) navigationView.findViewById(R.id.about_img);
        privacy_img=(ImageView) navigationView.findViewById(R.id.privacy_img);
        faq_img=(ImageView)navigationView.findViewById(R.id.faq_img);

        right_sec_img=(ImageView) navigationView.findViewById(R.id.right_sec_img);
        right_pro_img=(ImageView) navigationView.findViewById(R.id.right_pro_img);

        home_txt=(TextView) navigationView.findViewById(R.id.home_txt);
        pro_txt=(TextView) navigationView.findViewById(R.id.pro_txt);
        cont_txt=(TextView) navigationView.findViewById(R.id.cont_txt);
        inv_txt=(TextView) navigationView.findViewById(R.id.inv_txt);
        sec_txt=(TextView) navigationView.findViewById(R.id.sec_txt);
        rate_txt=(TextView) navigationView.findViewById(R.id.rate_txt);
        about_txt=(TextView) navigationView.findViewById(R.id.about_txt);
        privacy_txt=(TextView) navigationView.findViewById(R.id.privacy_txt);
        faq_txt=(TextView)navigationView.findViewById(R.id.faq_txt);

        profile_list=(ListView)navigationView.findViewById(R.id.profile_list);
        String[] number = getResources().getStringArray(R.array.prof_option);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.listitem, number);
        profile_list.setAdapter(adapter);
        profile_list.setOnItemClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            profile_list.setNestedScrollingEnabled(true);
        }

        section_list=(ExpandableListView)navigationView.findViewById(R.id.section_list);
        prepareSectionListData();
        setSectionListener();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            section_list.setNestedScrollingEnabled(true);
        }

        profile_lay.setOnClickListener(this);
        section_lay.setOnClickListener(this);
        nv_home_lay.setOnClickListener(this);
        contribute_lay.setOnClickListener(this);
        invite_lay.setOnClickListener(this);
        rate_lay.setOnClickListener(this);
        about_lay.setOnClickListener(this);
        privacy_lay.setOnClickListener(this);
        faq_lay.setOnClickListener(this);
    }

    private void setSectionListener()
    {
        section_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                setListViewHeight(parent, groupPosition);
                return false;
            }
        });
    }

    /*------- set section list height -------------*/
    private void prepareSectionListData() {
        if(listDataHeader == null)
        {
            listDataHeader = new ArrayList<SectionModel>();
            listDataChild = new HashMap<String, List<CategoryModel>>();
        }
        listDataHeader.clear();
        listDataChild.clear();
        listDataHeader.addAll(helper.getAllSections());

        Collections.sort(listDataHeader, new Comparator<SectionModel>(){
            public int compare(SectionModel obj1, SectionModel obj2) {
                return Integer.valueOf(obj1.getOrder_no()).compareTo(obj2.getOrder_no());
            }
        });

        for (int i=0; i<listDataHeader.size();i++)
        {
            listDataChild.put(listDataHeader.get(i).getSectionName(), helper.getAllCategory(listDataHeader.get(i).get_id()));
        }

        if(sectionAdapter==null)
        {
            sectionAdapter = new NVSectionExpandableListAdapter(this, section_list,listDataHeader, listDataChild);
            section_list.setAdapter(sectionAdapter);
            setListViewHeight(section_list);
            sectionAdapter.setOnExpendItemClickListener(new NVSectionExpandableListAdapter.OnExpendItemClickListener() {
                @Override
                public void onSectionItemClick(SectionModel data) {}

                @Override
                public void onCategoryItemClick(CategoryModel data)
                {
                    String haha=data.getCategory_id();
                    List<ContentListingModel> list_dt=helper.getAllPost();
                    List<ContentListingModel> array_list = new ArrayList<ContentListingModel>();
                    for (ContentListingModel dttt:list_dt)
                    {
                        if(haha.equalsIgnoreCase(dttt.getCategoryId()))
                        {
                            array_list.add(dttt);
                        }
                    }

                    if(array_list.size()>0)
                    {
                        if (drawer.isDrawerOpen(GravityCompat.START)) {
                            closeDrower();
                        }
                        setShowCurrentPagerItem(HomePagerAdapter.contentListingAfterFilter);
                        ContentListingAfterFilter ob=ContentListingAfterFilter.getInstance();
                        if(ob!=null)
                        {
                            ob.setData(array_list,data.getListViewFormat());
                        }
                    }
                    else
                    {
                        Alert.showToast(Home.this,getString(R.string.sub_category_are_not_available_to_this_category));
                    }
                }

                @Override
                public void onSubCategoryItemClick(SubCategoryModel data,CategoryModel data1) {
                    String haha=data.getSub_category_id();
                    List<ContentListingModel> list_dt=helper.getAllPost();
                    List<ContentListingModel> array_list = new ArrayList<ContentListingModel>();
                    for (ContentListingModel dttt:list_dt)
                    {
                        if(haha.equalsIgnoreCase(dttt.getSubCategoryId()))
                        {
                            array_list.add(dttt);
                        }
                    }
                    if(array_list.size()>0)
                    {
                        if (drawer.isDrawerOpen(GravityCompat.START)) {
                            closeDrower();
                        }
                        setShowCurrentPagerItem(HomePagerAdapter.contentListingAfterFilter);
                        ContentListingAfterFilter ob=ContentListingAfterFilter.getInstance();
                        if(ob!=null)
                        {
                            ob.setData(array_list,data1.getListViewFormat());
                        }
                    }
                    else
                    {
                        Alert.showToast(Home.this,getString(R.string.not_available_article));
                    }
                }
            });
        }
        else
        {
            sectionAdapter.notifyDataSetChanged();
        }
    }

    public static void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    /*------- set extendable list height -------------*/
    public static void setListViewHeight(ExpandableListView listView, int group) {
        ExpandableListAdapter listAdapter = listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();
                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

    /*------- set defaoult navigation -------------*/
    private void setNavDefoultSelection()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            profile_lay.setBackground(null);
            section_lay.setBackground(null);
            nv_home_lay.setBackground(null);
            contribute_lay.setBackground(null);
            invite_lay.setBackground(null);
            rate_lay.setBackground(null);
            about_lay.setBackground(null);
            privacy_lay.setBackground(null);
            faq_lay.setBackground(null);
        }

        home_img.setImageResource(R.drawable.nv_unsel_home);
        left_pro_img.setImageResource(R.drawable.unsel_pr);
        cont_img.setImageResource(R.drawable.unsel_contribution);
        inv_img.setImageResource(R.drawable.unsel_invite);
        left_sec_img.setImageResource(R.drawable.unsel_section);
        rate_img.setImageResource(R.drawable.unsel_star);
        about_img.setImageResource(R.drawable.unsel_about);
        privacy_img.setImageResource(R.drawable.unsel_privacy);
        faq_img.setImageResource(R.drawable.unsel_faq);

        right_sec_img.setImageResource(R.drawable.unsel_drop);
        right_pro_img.setImageResource(R.drawable.unsel_drop);

        home_txt.setTextColor(ContextCompat.getColor(Home.this, R.color.white));
        pro_txt.setTextColor(ContextCompat.getColor(Home.this, R.color.white));
        cont_txt.setTextColor(ContextCompat.getColor(Home.this, R.color.white));
        inv_txt.setTextColor(ContextCompat.getColor(Home.this, R.color.white));
        sec_txt.setTextColor(ContextCompat.getColor(Home.this, R.color.white));
        rate_txt.setTextColor(ContextCompat.getColor(Home.this, R.color.white));
        about_txt.setTextColor(ContextCompat.getColor(Home.this, R.color.white));
        privacy_txt.setTextColor(ContextCompat.getColor(Home.this, R.color.white));
        faq_txt.setTextColor(ContextCompat.getColor(Home.this, R.color.white));
    }

    /*----- initialize view components -----------*/
    private void initializeView()
    {
        setBottomSheet();
        bac_dim_layout=(RelativeLayout)findViewById(R.id.bac_dim_layout);
        contabution_lay=(LinearLayout)findViewById(R.id.contabution_lay);
        my_account_lay=(LinearLayout)findViewById(R.id.my_account_lay);
        home_lay=(LinearLayout)findViewById(R.id.home_lay);
        home_image=(ImageView)findViewById(R.id.home_image);
        menu_items=(ImageView)findViewById(R.id.menu_items);
        contrabution_image=(ImageView)findViewById(R.id.contrabution_image);
        my_account_image=(ImageView)findViewById(R.id.my_account_image);

        my_account_text=(TextView)findViewById(R.id.my_account_text);
        home_text=(TextView)findViewById(R.id.home_text);

        contabution_lay.setOnClickListener(this);
        my_account_lay.setOnClickListener(this);
        home_lay.setOnClickListener(this);

        home_image.setImageResource(R.drawable.sel_home);
        contrabution_image.setImageResource(R.drawable.middle_plus);
        my_account_image.setImageResource(R.drawable.unsel_my_account);

        home_text.setTextColor(ContextCompat.getColor(Home.this, R.color.white));
        my_account_text.setTextColor(ContextCompat.getColor(Home.this, R.color.logo));


        viewpager=(CustomViewPager)findViewById(R.id.viewpager);
        HomePagerAdapter viewPagerAdapter = new HomePagerAdapter(getSupportFragmentManager(), PAGER_ITEMS);
        viewpager.setAdapter(viewPagerAdapter);
        viewpager.setOffscreenPageLimit(PAGER_ITEMS-1);
        viewpager.setPagingEnabled(false);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                logout.setVisibility(View.GONE);
                search.setVisibility(View.GONE);

                setContabution_lay();
                switch (position)
                {
                    case HomePagerAdapter.homeFragment:
                        home_image.setImageResource(R.drawable.sel_home);
                        contrabution_image.setImageResource(R.drawable.middle_plus);
                        my_account_image.setImageResource(R.drawable.unsel_my_account);

                        home_text.setTextColor(ContextCompat.getColor(Home.this, R.color.white));
                        my_account_text.setTextColor(ContextCompat.getColor(Home.this, R.color.logo));

                        sendScreenImageName(Constants.HOME);
                        break;

                    case HomePagerAdapter.contribution:
                        setContabution_lay();
                        sendScreenImageName(Constants.CONTRIBUTION);
                        break;
                    case HomePagerAdapter.myAccountFragment:
                        home_image.setImageResource(R.drawable.unsel_home);
                        contrabution_image.setImageResource(R.drawable.middle_plus);
                        my_account_image.setImageResource(R.drawable.sel_my_account);

                        my_account_text.setTextColor(ContextCompat.getColor(Home.this, R.color.white));
                        home_text.setTextColor(ContextCompat.getColor(Home.this, R.color.logo));

                        logout.setVisibility(View.VISIBLE);
                        search.setVisibility(View.GONE);
                        sendScreenImageName(Constants.MY_ACCOUNT);
                        break;
                    case HomePagerAdapter.categoryFragment:
                        sendScreenImageName(Constants.CATEGORY);
                        break;
                    case HomePagerAdapter.subCategoryFragment:
                        sendScreenImageName(Constants.SUB_CATEGORY);
                        break;
                    case HomePagerAdapter.contentListingAfterFilter:
                        sendScreenImageName(Constants.CONTENT_LISTING);
                        break;
                    case HomePagerAdapter.ContactListFragment:
                        sendScreenImageName(Constants.CONTACT);
                        break;
                    case HomePagerAdapter.savedItems:
                        sendScreenImageName(Constants.SAVED_ITEM);
                        break;
                    case HomePagerAdapter.gallery:
                        sendScreenImageName(Constants.DOWNLOAD_ITEM);
                        break;
                    case HomePagerAdapter.notification:
                        sendScreenImageName(Constants.NOTIFICATION);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setShowCurrentPagerItem(HomePagerAdapter.homeFragment);

        /*Set Navigation Selection initially*/
        setNavDefoultSelection();
        nv_home_lay.setBackgroundResource(R.drawable.solid_yello);
        home_img.setImageResource(R.drawable.nv_sel_home);
        home_txt.setTextColor(ContextCompat.getColor(Home.this, R.color.black));

        logout=(ImageView)findViewById(R.id.logout);
        search=(ImageView)findViewById(R.id.search);
        notification=(RelativeLayout) findViewById(R.id.notification);
        count=(TextView)findViewById(R.id.count);

        logout.setOnClickListener(this);
        search.setOnClickListener(this);
        menu_items.setOnClickListener(this);
        notification.setOnClickListener(this);
    }

    public void setShowCurrentPagerItem(int position)
    {
        viewpager.setCurrentItem(position,false);
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            closeDrower();
        }
        else if (rightLowerMenu.isOpen())
        {
            rightLowerMenu.close(true);
        }
        else if(Home.LIST!=null)
        {
            HomeFragment.main_lay.setVisibility(View.GONE);
            setShowCurrentPagerItem(HomePagerAdapter.homeFragment);
            HomeFragment.getInstance().stActivityOnBackPress();
        }
        else
        {
            if(bottomSheetBehavior!=null)
            {
                if(bottomSheetBehavior.getState()==BottomSheetBehavior.STATE_EXPANDED)
                {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                else if(viewpager.getCurrentItem()==HomePagerAdapter.subCategoryFragment)
                {
                    viewpager.setCurrentItem(HomePagerAdapter.categoryFragment,false);
                }
                else if (viewpager.getCurrentItem()==HomePagerAdapter.ContactListFragment)
                {
                    viewpager.setCurrentItem(HomePagerAdapter.myAccountFragment,false);
                }
                else if(viewpager.getCurrentItem()!= HomePagerAdapter.homeFragment)
                {
                    viewpager.setCurrentItem(0,false);
                }
                else
                {
                    super.onBackPressed();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
      //  getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }


    private void closeDrower()
    {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    private void setContabution_lay()
    {
        home_image.setImageResource(R.drawable.unsel_home);
        contrabution_image.setImageResource(R.drawable.middle_plus);
        my_account_image.setImageResource(R.drawable.unsel_my_account);

        my_account_text.setTextColor(ContextCompat.getColor(Home.this, R.color.logo));
        home_text.setTextColor(ContextCompat.getColor(Home.this, R.color.logo));
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.home_lay:
                closeDrower();
                home_image.setImageResource(R.drawable.sel_home);
                contrabution_image.setImageResource(R.drawable.middle_plus);
                my_account_image.setImageResource(R.drawable.unsel_my_account);

                my_account_text.setTextColor(ContextCompat.getColor(Home.this, R.color.logo));
                home_text.setTextColor(ContextCompat.getColor(Home.this, R.color.white));
                setShowCurrentPagerItem(HomePagerAdapter.homeFragment);
                closeBottomSheet();
                break;

            case R.id.contabution_lay:
                closeDrower();
                setContabution_lay();
                setShowCurrentPagerItem(HomePagerAdapter.contribution);
                Contribution.getInstance().resetLay();
                closeBottomSheet();
                break;

            case R.id.my_account_lay:
                closeDrower();
                expendBottomSheet();
                break;

            case R.id.nv_home_lay:
                setNavDefoultSelection();
                nv_home_lay.setBackgroundResource(R.drawable.solid_yello);
                home_img.setImageResource(R.drawable.nv_sel_home);
                home_txt.setTextColor(ContextCompat.getColor(Home.this, R.color.black));
                closeDrower();
                setShowCurrentPagerItem(HomePagerAdapter.homeFragment);
                break;

            case R.id.profile_lay:
                setNavDefoultSelection();
                profile_lay.setBackgroundResource(R.drawable.solid_yello);
                left_pro_img.setImageResource(R.drawable.sel_pr);
                pro_txt.setTextColor(ContextCompat.getColor(Home.this, R.color.black));
                right_pro_img.setImageResource(R.drawable.sel_drop);
                if(section_list.isShown())
                {
                    section_list.setVisibility(View.GONE);
                }
                if(profile_list.isShown())
                {
                    profile_list.setVisibility(View.GONE);
                }
                else
                {
                    profile_list.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.section_lay:
                setNavDefoultSelection();
                section_lay.setBackgroundResource(R.drawable.solid_yello);
                left_sec_img.setImageResource(R.drawable.sel_section);
                sec_txt.setTextColor(ContextCompat.getColor(Home.this, R.color.black));
                right_sec_img.setImageResource(R.drawable.sel_drop);
                if(profile_list.isShown())
                {
                    profile_list.setVisibility(View.GONE);
                }
                if(section_list.isShown())
                {
                    section_list.setVisibility(View.GONE);
                }
                else
                {
                    section_list.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.contribute_lay:
                setNavDefoultSelection();
                contribute_lay.setBackgroundResource(R.drawable.solid_yello);
                cont_img.setImageResource(R.drawable.sel_contribution);
                cont_txt.setTextColor(ContextCompat.getColor(Home.this, R.color.black));
                closeDrower();
                setShowCurrentPagerItem(HomePagerAdapter.contribution);
                Contribution.getInstance().resetLay();
                break;
            case R.id.invite_lay:
                setNavDefoultSelection();
                invite_lay.setBackgroundResource(R.drawable.solid_yello);
                inv_img.setImageResource(R.drawable.sel_invite);
                inv_txt.setTextColor(ContextCompat.getColor(Home.this, R.color.black));
                closeDrower();
                Alert.inviteMultipleFriendDialog(Home.this);
                setNavDefoultSelection();
                //setShowCurrentPagerItem(HomePagerAdapter.ContactListFragment);
                //inviteFriendFromList();
                break;
            case R.id.rate_lay:
                setNavDefoultSelection();
                rate_lay.setBackgroundResource(R.drawable.solid_yello);
                rate_img.setImageResource(R.drawable.sel_star);
                rate_txt.setTextColor(ContextCompat.getColor(Home.this, R.color.black));
                closeDrower();
                try
                {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + this.getPackageName())));
                }
                catch (android.content.ActivityNotFoundException e)
                {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
                }
                //Alert.rateAlert(Home.this);
                setNavDefoultSelection();
                 break;
            case R.id.about_lay:
                setNavDefoultSelection();
                about_lay.setBackgroundResource(R.drawable.solid_yello);
                about_img.setImageResource(R.drawable.sel_about);
                about_txt.setTextColor(ContextCompat.getColor(Home.this, R.color.black));
                closeDrower();
                setNavDefoultSelection();
                startActivity(new Intent(Home.this,AboutUs.class));
                break;
            case R.id.privacy_lay:
                setNavDefoultSelection();
                privacy_lay.setBackgroundResource(R.drawable.solid_yello);
                privacy_img.setImageResource(R.drawable.sel_privacy);
                privacy_txt.setTextColor(ContextCompat.getColor(Home.this, R.color.black));
                closeDrower();
                setNavDefoultSelection();
                startActivity(new Intent(Home.this,PrivacyPolicy.class));
                break;
            case R.id.faq_lay:
                setNavDefoultSelection();
                faq_lay.setBackgroundResource(R.drawable.solid_yello);
                faq_img.setImageResource(R.drawable.sel_faq);
                faq_txt.setTextColor(ContextCompat.getColor(Home.this, R.color.black));
                closeDrower();
                startActivity(new Intent(Home.this,FAQSection.class));
                setNavDefoultSelection();
                break;
            case R.id.logout:
                logout();
                break;
            case R.id.notification:
                fatchData.callAllNotificationService();
                try {
                    Badges.removeBadge(Home.this);
                    MyFirebaseMessagingService.count=0;
                    Badges.setBadge(Home.this, MyFirebaseMessagingService.count);
                } catch (BadgesNotSupportedException badgesNotSupportedException) {
                    Alert.showLog("remove", badgesNotSupportedException.getMessage());
                }

                if(SharedPreference.retrieveData(Home.this,Constants.NCOUNT)!=null)
                {
                    SharedPreference.removeKey(Home.this, Constants.NCOUNT);
                }
                count.setText("");
                count.setVisibility(View.INVISIBLE);
                setShowCurrentPagerItem(HomePagerAdapter.notification);
                break;
            case R.id.search:
                break;
            case R.id.bg:
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.sheet_profile_lay:
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                openAccountWithScroll(0);
                break;
            case R.id.sheet_save:
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                openSaveItem();
                //openAccountWithScroll(1);
                break;
            case R.id.sheet_download:
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                openAccountWithScroll(1);
                break;
            case R.id.sheet_friends:
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                openFriendList();
                break;
            case R.id.sheet_my_contribution:
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                openMyContribution();
                //openAccountWithScroll(4);
                break;
            case R.id.menu_items:

                drawer.openDrawer(Gravity.LEFT);
                closeBottomSheet();
                break;
        }
    }

    public void openSaveItem()
    {
        setShowCurrentPagerItem(HomePagerAdapter.savedItems);
        SavedItems ins=SavedItems.getInstance();
        if(ins!=null)
        {
            ins.setSelectedSavedItemLay();
        }
    }

    public void openMyContribution()
    {
        setShowCurrentPagerItem(HomePagerAdapter.savedItems);
        SavedItems inss=SavedItems.getInstance();
        if(inss!=null)
        {
            inss.setSelectedContributItemLay();
        }
    }

    public void openFriendList()
    {
        setShowCurrentPagerItem(HomePagerAdapter.ContactListFragment);
        if(helper.getNetparFriendList().size()>0)
        {
            allFriendFromList();
        }
        else
        {
            inviteFriendFromList();
        }
    }

    private BroadcastReceiver onNotice= new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context mContext, final Intent intent) {
            if(activity_running)
            {
               runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(intent.hasExtra("NOTIFICATION"))
                        {
                            setNotificationCount();
                        }
                        else
                        {
                            HomeFragment.getInstance().setData();
                            if(CategoryFragment.getInstance()!=null)
                            {
                                CategoryFragment.getInstance().setData();
                            }
                            if(SubCategoryFragment.getInstance()!=null)
                            {
                                SubCategoryFragment.getInstance().setData();
                            }
                            if(ContentListingAfterFilter.getInstance()!=null)
                            {
                                ContentListingAfterFilter.getInstance().setAutoRefresh();
                            }
                            if(Notifications.getInstance()!=null)
                            {
                                Notifications.getInstance().setNotificationData();
                            }
                            prepareSectionListData();
                        }

                    }
                });
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            closeDrower();
        }
        setShowCurrentPagerItem(HomePagerAdapter.myAccountFragment);
        MyAccountFragment instance=MyAccountFragment.getInstance();
        if(instance!=null)
        {
            switch (position)
            {
                case 0:
                    openAccountWithScroll(0);
                    break;
                case 1:
                    openSaveItem();
                    break;
                case 2:
                    instance.scrollTillDownload();
                    break;
                case 3:
                    openMyContribution();
                    break;
                case 4:
                    openFriendList();
                    break;
            }
        }
    }

    @Override
    public void onServiceResponse(int i, Response<ResponseBody> response) {
        try
        {
            JSONObject object = new JSONObject(response.body().string());
            switch (i)
            {
                case session_count:
                    SharedPreference.storeSessionCount(getApplicationContext(),object.getLong("lastUpdated"));
                    break;
                case AddDevice:
                    Alert.showLog("HOME","AddDevice RESPONSE- "+object.toString());
                    break;
            }
        }
        catch (Exception e)
        {e.printStackTrace();}
    }

    /*--------------- fatch all phone contacts ---------------*/
    public class GettingContacts extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... voids)
        {
            setContacts();
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            List<ContactsModel> tmp=new ArrayList<>();
            try {
                for (ContactsModel contact:contactsList)
                {
                    if(!contact.getNumber().equalsIgnoreCase(DataBaseStrings.DEFAULT_VALUE))
                    {
                        tmp.add(contact);
                        Alert.showLog("dt","Contact: "+contact.getName()+"  "+contact.getNumber());
                    }
                }
                helper.clearFriend();
                helper.storeFriendList(tmp);

                fatchData.syncContactFromServer();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if(contactsList.size()<1)
        {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_CONTACTS}, PERMISSION_REQUEST_CONTACT);
            }
            else
            {
                new GettingContacts().execute();//Retrieve all contacts
            }
        }

    }

    private void setContacts()
    {
        try {
            String image_uri = "";
            String phone = "";
            contactsList.clear();
            Cursor c = getContentResolver().query(android.provider.ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            if (c != null && c.moveToFirst())
            {
                while (c.moveToNext()) {
                    String id = c.getString(c.getColumnIndex("_id"));
                    String name = c.getString(c.getColumnIndex("display_name"));
                    image_uri = c.getString(c.getColumnIndex("photo_uri"));
                    if (Integer.parseInt(c.getString(c.getColumnIndexOrThrow("has_phone_number"))) > 0) {
                        contactsList.add(new ContactsModel(id, name, retrieveContactNumber(id), image_uri,DataBaseStrings.DEFAULT_VALUE,ContactsModel.INVITE,DataBaseStrings.DEFAULT_VALUE));
                    }
                }
                c.close();
                if (contactsList.size() > 0) {
                    Collections.sort(contactsList, new Comparator<ContactsModel>() {
                        @Override
                        public int compare(ContactsModel o1, ContactsModel o2) {
                            return o1.getName().compareTo(o2.getName());
                        }

                        @Override
                        public boolean equals(Object obj) {
                            return false;
                        }
                    });
                }
            }
        }
        catch (Exception e)
        {
            Alert.showLog("Exception","Contact Exception- "+e.toString());
        }
    }

    private String retrieveContactNumber(String contactID)
    {
        String contactNumber = DataBaseStrings.DEFAULT_VALUE;
        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
                new String[]{contactID}, null);
        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }
        cursorPhone.close();
        return contactNumber;
    }

    private void getContact()
    {
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }
        phones.close();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CONTACT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new GettingContacts().execute();
                }
                return;
            }
        }
    }

    private void logout()
    {
        logout(Home.this,helper);
    }

    public void logout(final Context context, final DatabaseHelper helper) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.custom_alert_dialog);
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
        ok_card_view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alertDialog.dismiss();
                new Retrofit2(context,Home.this,REMOVE_DEVICE,Constants.REMOVE_DEVICE_FCM+"/"+SharedPreference.retrieveData(context,Constants.USER_ID)).callService(false);
                SharedPreference.removeAll(context);
                helper.logout();
                ((Activity) context).finish();
                context.startActivity(new Intent(context, Language.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        no_card_view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private class AsyncCaller extends AsyncTask<String, Boolean, Boolean> {
        boolean status = false;
        private AlertDialog alert;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Boolean doInBackground(String... args) {
            final int selectedLangId = SharedPreference.retrieveLang(Home.this);
            RevSDK.initLangResources("https://api-gw.revup.reverieinc.com/apiman-gateway/ReverieMobilitySdk", selectedLangId, new LangResourceInitCompleteListener() {
                public void onLangResourceInitComplete(int i, RevStatus revStatus)
                {
                    Alert.showLog("TAG", "INIT RESOURCE COMPLETE:  Lang code = " + i + " , Status = " + revStatus.getStatusMessage());
                    if (revStatus.getStatusCode() == RevStatus.SUCCESS) {
                        // Control pass to main thread
                        publishProgress(true);
                    }
                    else  {
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
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // Do nothing now
        }
    }

    public void inviteFriendFromList()
    {
        ContactListFragment fragment = ContactListFragment.getInstance();
        fragment.selectInviteData();
    }

    public void allFriendFromList()
    {
        ContactListFragment fragment = ContactListFragment.getInstance();
        fragment.selectFriendData();
    }

    private void setBottomSheet()
    {
        /*Bottom Sheet Widget*/
        bg=(View)findViewById(R.id.bg);
        sheet_profile_lay=(LinearLayout)findViewById(R.id.sheet_profile_lay);
        sheet_save=(LinearLayout)findViewById(R.id.sheet_save);
        sheet_download=(LinearLayout)findViewById(R.id.sheet_download);
        sheet_friends=(LinearLayout)findViewById(R.id.sheet_friends);
        sheet_my_contribution=(LinearLayout)findViewById(R.id.sheet_my_contribution);
        LinearLayout bottomSheetLayout = (LinearLayout) findViewById(R.id.design_bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED)
                {
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

        if(bottomSheetBehavior!=null)
        {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    public void expendBottomSheet()
    {
        if(bottomSheetBehavior!=null)
        {
            if(bottomSheetBehavior.getState()==BottomSheetBehavior.STATE_COLLAPSED)
            {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
            else
            {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }
    }

    public void closeBottomSheet()
    {
        if(bottomSheetBehavior!=null)
        {
            if(bottomSheetBehavior.getState()==BottomSheetBehavior.STATE_EXPANDED)
            {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }
    }

    public void openAccountWithScroll(int position)
    {
        home_image.setImageResource(R.drawable.unsel_home);
        contrabution_image.setImageResource(R.drawable.middle_plus);
        my_account_image.setImageResource(R.drawable.sel_my_account);

        my_account_text.setTextColor(ContextCompat.getColor(Home.this, R.color.white));
        home_text.setTextColor(ContextCompat.getColor(Home.this, R.color.logo));

        setShowCurrentPagerItem(HomePagerAdapter.myAccountFragment);
        if(instance==null)
        {
            instance=MyAccountFragment.getInstance();
        }
        if(instance!=null)
        {
            switch (position)
            {
                case 0:
                    instance.scrollTillProfile();
                    break;
                case 1:
                    instance.scrollTillDownload();
                    break;
                case 2:
                    instance.scrollTillFriendList();
                    break;
                case 3:
                    instance.scrollTillSave(0);
                    break;
                case 4:
                    instance.scrollTillSave(1);
                    break;
            }
        }
    }

    /*For analytic tool*/
    private void sendScreenImageName(String name) {
        FlurryAgent.onPageView();
        Controller.getInstance().trackScreenView(name);
    }

    private void sendInItDataToFlurry()
    {
        FlurryAgent.setReportLocation(false);
        FlurryAgent.setUserId(SharedPreference.retrieveData(Home.this,Constants.USER_ID));
    }

    private void setNotificationCount()
    {
       /* if(SharedPreference.retrieveData(Home.this,Constants.NCOUNT)!=null)
        {
            callAllNotification();
            if(!SharedPreference.retrieveData(Home.this,Constants.NCOUNT).equalsIgnoreCase("0"))
            {
                count.setText(SharedPreference.retrieveData(Home.this,Constants.NCOUNT));
                count.setVisibility(View.VISIBLE);
            }
            else
            {
                count.setVisibility(View.INVISIBLE);
            }
        }*/
    }

    private void callAllNotification()
    {
        CallService noti=new CallService(this);
        noti.setOnUpdate(new CallService.ResponseSynchronous() {
            @Override
            public void UpdateData(int service_val) {
                //No need to do anything on here
            }
        });
        noti.callAllNotificationService();
    }
}
