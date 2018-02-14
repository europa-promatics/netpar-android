package co.netpar.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.reverie.customcomponent.RevEditText;
import com.reverie.manager.RevSDK;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import co.netpar.Adapter.FriendDetailAdapter;
import co.netpar.Adapter.FriendsAdapter;
import co.netpar.Adapter.MyPostItemAdapter;
import co.netpar.Adapter.SavedItemAdapter;
import co.netpar.App.Controller;
import co.netpar.Comman.Alert;
import co.netpar.Comman.Constants;
import co.netpar.Comman.DrowableFunction;
import co.netpar.ContentView;
import co.netpar.Home;
import co.netpar.Language;
import co.netpar.MediaDetail;
import co.netpar.Model.Blocks_Data_Model;
import co.netpar.Model.ContactsModel;
import co.netpar.Model.ContentListingModel;
import co.netpar.Model.Data_Model;
import co.netpar.Model.District_Data_Model;
import co.netpar.Model.DownloadedData;
import co.netpar.Model.MediaModel;
import co.netpar.Network.ConnectionDetector;
import co.netpar.OTPScreen;
import co.netpar.PagerAdapter.HomePagerAdapter;
import co.netpar.Profile;
import co.netpar.R;
import co.netpar.Retrofit.Retrofit2;
import co.netpar.Retrofit.RetrofitService;
import co.netpar.Retrofit.ServiceResponse;
import co.netpar.SignIn;
import co.netpar.Syncronization.NetparDataBase.DataBaseStrings;
import co.netpar.Syncronization.NetparDataBase.DatabaseHelper;
import co.netpar.Syncronization.NetparDataBase.SharedPreference;
import co.netpar.Widget.Croping.CirculerCrop;
import co.netpar.Widget.Croping.Croping;
import co.netpar.Widget.RoundRectCornerImageView;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;

public class MyAccountFragment extends Fragment implements ServiceResponse, View.OnClickListener, View.OnTouchListener, AdapterView.OnItemSelectedListener {
    private Context context;

    private LinearLayout profile_lay;
    private TextView user_name;
    private EditText dob, address, sex;
    private RevEditText phone;
    private SimpleDraweeView profile_image_back;
    private View bg;
    private LinearLayout download_list_lay;
    private RelativeLayout download_lay1, download_lay2, download_lay3, download_lay4, download_lay5;
    private SimpleDraweeView download_image1, download_image2, download_image3, download_image4, download_image5;
    private ImageView del_download_1, del_download_2, del_download_3, del_download_4, del_download_5;
    private LinearLayout see_more_download;
    private List<DownloadedData> download_data = new ArrayList<>();
    private DatabaseHelper helper;

    private LinearLayout see_more_friend, friend_list_lay;
    private RecyclerView friend_list_recycler;
    private ImageView add_friend;
    private FriendsAdapter friend_adapter;

    private LinearLayout save_list_lay, see_more_save;
    private RelativeLayout save_click, contribute_click;
    private RecyclerView savedData, myPostData;
    private TextView saved_post_txt, contribution_post_txt;
    private View saved_post_view, saved_contribution_view;


    private NestedScrollView parent_scrollView;
    private List<ContentListingModel> saved_Data = new ArrayList<>();
    private List<ContentListingModel> my_post = new ArrayList<>();

    private SavedItemAdapter adapter;

    private MyPostItemAdapter post_adapter;

    private final int PICK_PHOTO_FOR_PROFILE = 0;
    public static final int CROP_IMAGE = 500;

    private CircleImageView imageView;
    private ProgressBar progressBar;
    private List<ContactsModel> contact_list = new ArrayList<>();

    private ImageView editMobile, editsex, editaddress, editdob, edit_image,delet_image;
    private LinearLayout edit_dob_lay, address_lay;
    private AutoCompleteTextView district, input_block, spinner_state, ddd, mmm, yyy;
    private RadioGroup gen_group;


    private TextView total_friend, total_lekh, total_yogdan;


    public static MyAccountFragment instance;

    List<String> dt28 = new ArrayList<>();
    List<String> dt29 = new ArrayList<>();
    List<String> dt30 = new ArrayList<>();
    List<String> dt31 = new ArrayList<>();
    List<String> dt = new ArrayList<>();
    List<String> m = new ArrayList<>();
    List<String> y = new ArrayList<>();
    int mo = 10;
    int yer = 2017;

    private final int SEND_OTP = 0, UPDATE_DEVICE = 1, UPDATE_PROFILE = 2,DELETE_PROFILE_PIC=50;

    private final int OTP_ACTIVITY_RESULT = 100;

    private String oldMobileNumber = "";


    private List<Data_Model> state_data = new ArrayList<>();
    private List<String> state = new ArrayList<>();

    private List<District_Data_Model> district_data = new ArrayList<>();
    private List<String> distri = new ArrayList<>();

    private List<Blocks_Data_Model> block_data = new ArrayList<>();
    private List<String> blocs = new ArrayList<>();


    private String st = "", dst = "", blk = "";
    private ServiceResponse res;

    public static final int REQUEST_PERMISSIONS_CODE_WRITE_STORAGE=1000;

    public static synchronized MyAccountFragment getInstance() {
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.myaccount_fragment, container, false);
        context = container.getContext();
        instance = this;
        res=this;
        initializeView(v);
        return v;
    }

    /*-------------- initialize views --------------*/
    private void initializeView(View v) {
        total_friend = (TextView) v.findViewById(R.id.total_friend);
        total_lekh = (TextView) v.findViewById(R.id.total_lekh);
        total_yogdan = (TextView) v.findViewById(R.id.total_yogdan);
        parent_scrollView = (NestedScrollView) v.findViewById(R.id.parent_scrollView);

        /*Profile  Widget*/
        profile_lay = (LinearLayout) v.findViewById(R.id.profile_lay);
        user_name = (TextView) v.findViewById(R.id.user_name);
        phone = (RevEditText) v.findViewById(R.id.phone);
        address = (EditText) v.findViewById(R.id.address);
        dob = (EditText) v.findViewById(R.id.dob);
        sex = (EditText) v.findViewById(R.id.sex);
        bg = (View) v.findViewById(R.id.bg);
        imageView = (CircleImageView) v.findViewById(R.id.imageView);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        edit_image = (ImageView) v.findViewById(R.id.edit_image);
        delet_image=(ImageView)v.findViewById(R.id.delet_image);
        profile_image_back = (SimpleDraweeView) v.findViewById(R.id.profile_back_image);

        edit_dob_lay = (LinearLayout) v.findViewById(R.id.edit_dob_lay);
        address_lay = (LinearLayout) v.findViewById(R.id.address_lay);
        gen_group = (RadioGroup) v.findViewById(R.id.gen_group);

        district = (AutoCompleteTextView) v.findViewById(R.id.district);
        input_block = (AutoCompleteTextView) v.findViewById(R.id.input_block);
        spinner_state = (AutoCompleteTextView) v.findViewById(R.id.spinner_state);
        // spinner_state.setText(R.string.maharastra);

        setAddress();

        ddd = (AutoCompleteTextView) v.findViewById(R.id.ddd);
        mmm = (AutoCompleteTextView) v.findViewById(R.id.mmm);
        yyy = (AutoCompleteTextView) v.findViewById(R.id.yyy);

        ddd.setFocusable(false);
        ddd.setFocusableInTouchMode(false);
        mmm.setFocusable(false);
        mmm.setFocusableInTouchMode(false);
        yyy.setFocusable(false);
        yyy.setFocusableInTouchMode(false);
        setCalender();

        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                .path(String.valueOf(R.drawable.profile))
                .build();
        profile_image_back.setImageURI(uri);

        editMobile = (ImageView) v.findViewById(R.id.editMobile);
        editsex = (ImageView) v.findViewById(R.id.editsex);
        editaddress = (ImageView) v.findViewById(R.id.editaddress);
        editdob = (ImageView) v.findViewById(R.id.editdob);

        user_name.setEnabled(false);
        phone.setEnabled(false);
        address.setEnabled(false);
        dob.setEnabled(false);
        sex.setEnabled(false);

        /*Downloaded Widget*/
        download_list_lay = (LinearLayout) v.findViewById(R.id.download_list_lay);
        download_lay1 = (RelativeLayout) v.findViewById(R.id.download_lay1);
        download_lay2 = (RelativeLayout) v.findViewById(R.id.download_lay2);
        download_lay3 = (RelativeLayout) v.findViewById(R.id.download_lay3);
        download_lay4 = (RelativeLayout) v.findViewById(R.id.download_lay4);
        download_lay5 = (RelativeLayout) v.findViewById(R.id.download_lay5);

        download_image1 = (SimpleDraweeView) v.findViewById(R.id.download_image1);
        download_image2 = (SimpleDraweeView) v.findViewById(R.id.download_image2);
        download_image3 = (SimpleDraweeView) v.findViewById(R.id.download_image3);
        download_image4 = (SimpleDraweeView) v.findViewById(R.id.download_image4);
        download_image5 = (SimpleDraweeView) v.findViewById(R.id.download_image5);

        del_download_1 = (ImageView) v.findViewById(R.id.del_download_1);
        del_download_2 = (ImageView) v.findViewById(R.id.del_download_2);
        del_download_3 = (ImageView) v.findViewById(R.id.del_download_3);
        del_download_4 = (ImageView) v.findViewById(R.id.del_download_4);
        del_download_5 = (ImageView) v.findViewById(R.id.del_download_5);

        see_more_download = (LinearLayout) v.findViewById(R.id.see_more_download);

        /*Friend Widget*/
        friend_list_lay = (LinearLayout) v.findViewById(R.id.friend_list_lay);
        see_more_friend = (LinearLayout) v.findViewById(R.id.see_more_friend);
        friend_list_recycler = (RecyclerView) v.findViewById(R.id.friend_list_recycler);
        add_friend = (ImageView) v.findViewById(R.id.add_friend);

        /*Saved Widget*/
        save_list_lay = (LinearLayout) v.findViewById(R.id.save_list_lay);
        save_click = (RelativeLayout) v.findViewById(R.id.save_click);
        contribute_click = (RelativeLayout) v.findViewById(R.id.contribute_click);

        savedData = (RecyclerView) v.findViewById(R.id.savedData);
        myPostData = (RecyclerView) v.findViewById(R.id.myPostData);

        savedData.setNestedScrollingEnabled(false);
        myPostData.setNestedScrollingEnabled(false);

        saved_post_txt = (TextView) v.findViewById(R.id.saved_post_txt);
        contribution_post_txt = (TextView) v.findViewById(R.id.contribution_post_txt);
        saved_post_view = (View) v.findViewById(R.id.saved_post_view);
        saved_contribution_view = (View) v.findViewById(R.id.saved_contribution_view);

        see_more_save = (LinearLayout) v.findViewById(R.id.see_more_save);

        setUpListener();
        setProfileData();
        helper = ((Home) context).helper;
        setDownloadData();
        setSaveData();
        setSelectedSavedItemLay();
        setFriends();
    }

    /*-------------- show contact's available on netpar --------------*/
    private void setFriends() {
        contact_list.clear();
        contact_list.addAll(helper.getNetparFriendList());
        if (friend_adapter == null) {
            friend_adapter = new FriendsAdapter(context, contact_list);
            friend_list_recycler.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
            friend_list_recycler.setAdapter(friend_adapter);
        } else {
            friend_adapter.notifyDataSetChanged();
        }

        if (contact_list.size() > 6) {
            see_more_friend.setVisibility(View.VISIBLE);
        } else {
            see_more_friend.setVisibility(View.GONE);
        }

        if (total_friend != null)
            total_friend.setText(String.valueOf(contact_list.size()));
    }

    /*-------------- show already saved items --------------*/
    private void setSelectedSavedItemLay() {
        saved_post_txt.setTextColor(getResources().getColor(R.color.selectable_tab));
        saved_post_view.setBackgroundResource(R.color.selectable_tab);

        ViewGroup.LayoutParams params = saved_post_view.getLayoutParams();
        params.height = 4;
        saved_post_view.setLayoutParams(params);

        contribution_post_txt.setTextColor(getResources().getColor(R.color.unsel_selectable_tab));

        ViewGroup.LayoutParams params1 = saved_contribution_view.getLayoutParams();
        params1.height = 1;
        saved_contribution_view.setLayoutParams(params1);

        saved_contribution_view.setBackgroundResource(R.color.unsel_selectable_tab);
        savedData.setVisibility(View.VISIBLE);
        myPostData.setVisibility(View.INVISIBLE);
    }

    /*-------------- show contribution --------------*/
    private void setSelectedContributItemLay() {

        ViewGroup.LayoutParams params = saved_post_view.getLayoutParams();
        params.height = 1;
        saved_post_view.setLayoutParams(params);

        ViewGroup.LayoutParams params1 = saved_contribution_view.getLayoutParams();
        params1.height = 4;
        saved_contribution_view.setLayoutParams(params1);

        contribution_post_txt.setTextColor(getResources().getColor(R.color.selectable_tab));
        saved_contribution_view.setBackgroundResource(R.color.selectable_tab);
        saved_post_txt.setTextColor(getResources().getColor(R.color.unsel_selectable_tab));
        saved_post_view.setBackgroundResource(R.color.unsel_selectable_tab);
        savedData.setVisibility(View.INVISIBLE);
        myPostData.setVisibility(View.VISIBLE);

        my_post.clear();
        my_post.addAll(helper.getMyPost());
        if (post_adapter == null)
        {
            post_adapter = new MyPostItemAdapter(context, my_post);
            myPostData.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
            post_adapter.setOnClickListener(new MyPostItemAdapter.OnClickListener() {
                @Override
                public void OnClick(int position, View view, ContentListingModel data) {
                    //startActivityForResult(new Intent(context, ContentView.class).putExtra("DATA",data),50);
                }
            });
            myPostData.setAdapter(post_adapter);
        }
        else {

            post_adapter = new MyPostItemAdapter(context, my_post);
            myPostData.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
            post_adapter.setOnClickListener(new MyPostItemAdapter.OnClickListener() {
                @Override
                public void OnClick(int position, View view, ContentListingModel data) {
                    //startActivityForResult(new Intent(context, ContentView.class).putExtra("DATA",data),50);
                }
            });
            myPostData.setAdapter(post_adapter);
           // post_adapter.notifyDataSetChanged();
        }
    }

    private void setSaveData() {
        saved_Data.clear();
        saved_Data.addAll(helper.getAllSavePost());
        if (saved_Data.size() > 0)
        {
            if (adapter == null)
            {
                save_list_lay.setVisibility(View.VISIBLE);
                savedData.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
                adapter = new SavedItemAdapter(context, saved_Data);
                adapter.setOnClickListener(new SavedItemAdapter.OnClickListener() {
                    @Override
                    public void OnClick(int position, View view, ContentListingModel data) {
                        startActivityForResult(new Intent(context, ContentView.class).putExtra("DATA", data), 50);
                    }
                });
                savedData.setAdapter(adapter);
            }
            else
            {

                adapter = new SavedItemAdapter(context, saved_Data);
                adapter.setOnClickListener(new SavedItemAdapter.OnClickListener() {
                    @Override
                    public void OnClick(int position, View view, ContentListingModel data) {
                        startActivityForResult(new Intent(context, ContentView.class).putExtra("DATA", data), 50);
                    }
                });
                savedData.setAdapter(adapter);

                 //   int initialSize = saved_Data.size();
                  //  mChannelItemList.addAll(saved_Data);
                  //  adapter.notifyItemRangeInserted(initialSize, saved_Data.size()-1); //Correct position

             /*   for (int i = 0; i < saved_Data.size(); i++) {
                    adapter.notifyItemChanged(i, saved_Data.get(i));
                }*/
            }
        }
        else
        {
            save_list_lay.setVisibility(View.GONE);
        }

        if(saved_Data.size()<1 && helper.getMyPost().size()>0)
        {
            save_list_lay.setVisibility(View.VISIBLE);
            setSelectedContributItemLay();
        }
    }

    /*-------------- show profile --------------*/
    private void setProfileData() {
        if (SharedPreference.retrieveData(context, Constants.USER_ID) != null) {
            String FIRST_NAME = SharedPreference.retrieveData(context, Constants.FIRST_NAME);
            String LAST_NAME = SharedPreference.retrieveData(context, Constants.LAST_NAME);
            String MOBILE_NUMBER = SharedPreference.retrieveData(context, Constants.MOBILE_NUMBER);

            String ADDRESS = SharedPreference.retrieveData(context, Constants.BLOCK_REGIONAL) + ", " +
                    SharedPreference.retrieveData(context, Constants.DISTRICT_REGIONAL) + ", " +
                    SharedPreference.retrieveData(context, Constants.STATE_REGIONAL);

            String GENDER = SharedPreference.retrieveData(context, Constants.GENDER);
            String DOB = SharedPreference.retrieveData(context, Constants.DOB);

            oldMobileNumber = SharedPreference.retrieveData(context, Constants.MOBILE_NUMBER);
            if(SharedPreference.retrieveData(context, Constants.USER_IMAGE)!=null)
            {
                if(!SharedPreference.retrieveData(context, Constants.USER_IMAGE).equalsIgnoreCase("")) {
                    Picasso.with(getActivity()).load(SharedPreference.retrieveData(context, Constants.USER_IMAGE)).into(imageView);
                }
            }

            user_name.setText(FIRST_NAME + " " + LAST_NAME);
            total_lekh.setText(SharedPreference.retrieveData(context, Constants.USER_LEKH));
            total_yogdan.setText(SharedPreference.retrieveData(context, Constants.USER_YOGDAN));

            phone.setText(MOBILE_NUMBER);
            address.setText(ADDRESS);
            dob.setText(DOB);
            if (GENDER != null) {
                if (GENDER.equalsIgnoreCase("male")) {
                    sex.setText(getString(R.string.male));
                    gen_group.check(R.id.male);
                } else {
                    sex.setText(getString(R.string.female));
                    gen_group.check(R.id.female);
                }
            }
        }
    }


    /*-------------- show downloads --------------*/
    private void setDownloadData() {
        int totalDownloaded_item = helper.getAllDownloadCount();
        download_lay1.setVisibility(View.GONE);
        download_lay2.setVisibility(View.GONE);
        download_lay3.setVisibility(View.GONE);
        download_lay4.setVisibility(View.GONE);
        download_lay5.setVisibility(View.GONE);
        if (totalDownloaded_item > 0) {
            download_list_lay.setVisibility(View.VISIBLE);
            if (totalDownloaded_item > 5) {
                see_more_download.setVisibility(View.VISIBLE);
            } else {
                see_more_download.setVisibility(View.INVISIBLE);
            }
            download_data = helper.getAllDownloads(SharedPreference.retrieveData(getActivity(), Constants.USER_ID));
            for (int down = 0; down < download_data.size(); down++) {
                int placeHolderId = R.drawable.detail_placeholder;
                if (download_data.get(down).getPOST_TAG().equalsIgnoreCase("audio")) {
                    placeHolderId = R.drawable.audio_place;
                } else if (download_data.get(down).getPOST_TAG().equalsIgnoreCase("video")) {
                    placeHolderId = R.drawable.video_place;
                }
                else if (download_data.get(down).getPOST_TAG().equalsIgnoreCase("doc")) {
                    placeHolderId = R.drawable.file;
                }
                switch (down) {
                    case 0:
                        download_lay1.setVisibility(View.VISIBLE);
                        Uri uriZero = Uri.parse(download_data.get(down).getDATA_PATH_URL());
                        download_image1.setImageURI(uriZero);
                        break;
                    case 1:
                        download_lay2.setVisibility(View.VISIBLE);
                        Uri uriOne = Uri.parse(download_data.get(down).getDATA_PATH_URL());
                        download_image2.setImageURI(uriOne);
                        break;
                    case 2:
                        download_lay3.setVisibility(View.VISIBLE);
                        Uri uriTwo = Uri.parse(download_data.get(down).getDATA_PATH_URL());
                        download_image3.setImageURI(uriTwo);
                        break;
                    case 3:
                        download_lay4.setVisibility(View.VISIBLE);
                        Uri uriThree = Uri.parse(download_data.get(down).getDATA_PATH_URL());
                        download_image4.setImageURI(uriThree);
                        break;
                    case 4:
                        download_lay5.setVisibility(View.VISIBLE);
                        Uri urifour = Uri.parse(download_data.get(down).getDATA_PATH_URL());
                        download_image5.setImageURI(urifour);
                        break;
                    default:
                        break;
                }
            }
        } else {
            download_list_lay.setVisibility(View.GONE);
        }
    }

    private void setUpListener()
    {
        imageView.setOnClickListener(this);
        edit_image.setOnClickListener(this);
        delet_image.setOnClickListener(this);

        see_more_friend.setOnClickListener(this);
        add_friend.setOnClickListener(this);

        download_lay1.setOnClickListener(this);
        download_lay2.setOnClickListener(this);
        download_lay3.setOnClickListener(this);
        download_lay4.setOnClickListener(this);
        download_lay5.setOnClickListener(this);
        del_download_1.setOnClickListener(this);
        del_download_2.setOnClickListener(this);
        del_download_3.setOnClickListener(this);
        del_download_4.setOnClickListener(this);
        del_download_5.setOnClickListener(this);
        see_more_download.setOnClickListener(this);


        save_click.setOnClickListener(this);
        contribute_click.setOnClickListener(this);
        see_more_save.setOnClickListener(this);

        editMobile.setOnClickListener(this);

        editsex.setOnClickListener(this);
        editaddress.setOnClickListener(this);
        editdob.setOnClickListener(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            RevSDK.destroyKeypad();
            RevSDK.clearLMCache();
        }
        else
            {
            if (phone != null) {
                phone.setEnabled(false);
                parent_scrollView.setScrollY(0);
            }
            getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setDownloadData();
                        setSaveData();
                        setFriends();
                    }
                });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.see_more_download:
                ((Home)context).setShowCurrentPagerItem(HomePagerAdapter.gallery);
                break;
            case R.id.download_lay1:
                openMediaDetail(download_data.get(0));
                break;
            case R.id.download_lay2:
                openMediaDetail(download_data.get(1));
                break;
            case R.id.download_lay3:
                openMediaDetail(download_data.get(2));
                break;
            case R.id.download_lay4:
                openMediaDetail(download_data.get(3));
                break;
            case R.id.download_lay5:
                openMediaDetail(download_data.get(4));
                break;
            case R.id.del_download_1:
                deleteOption(context, download_data.get(0));
               break;
            case R.id.del_download_2:
                deleteOption(context, download_data.get(1));
                break;
            case R.id.del_download_3:
                deleteOption(context, download_data.get(2));
                break;
            case R.id.del_download_4:
                deleteOption(context, download_data.get(3));
                break;
            case R.id.del_download_5:
                deleteOption(context, download_data.get(4));
                break;
            case R.id.save_click:
                setSelectedSavedItemLay();
                break;
            case R.id.contribute_click:
                setSelectedContributItemLay();
                break;
            case R.id.see_more_save:
                break;
            case R.id.imageView:
                picProfileImageOnly();
                break;
            case R.id.edit_image:
                picProfileImageOnly();
                break;
            case R.id.delet_image:
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                View dialogView = ((Activity)context).getLayoutInflater().inflate(R.layout.del_download, null);
                CardView yes_card_view = (CardView) dialogView.findViewById(R.id.yes_card_view);
                CardView no = (CardView) dialogView.findViewById(R.id.no);
                TextView title=(TextView)dialogView.findViewById(R.id.title);
                String msg=context.getString(R.string.del_img);
                title.setText(msg);
                yes_card_view.setBackgroundResource(R.drawable.gradient_yello_radious);
                no.setBackgroundResource(R.drawable.gradient_black_radious);
                dialogBuilder.setView(dialogView);
                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.getWindow().setLayout((int) (((double)context.getResources().getDisplayMetrics().widthPixels) * 0.9d), -2);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                yes_card_view.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        imageView.setImageResource(R.drawable.detail_placeholder);
                        SharedPreference.storeData(context, Constants.USER_IMAGE, "");
                        delet_image.setVisibility(View.GONE);
                        new Retrofit2(context,res,DELETE_PROFILE_PIC,Constants.deleteProfilePic+"/"+SharedPreference.retrieveData(context,Constants.USER_ID)).callService(false);
                        alertDialog.dismiss();
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
                break;
            case R.id.see_more_friend:
                ((Home) context).setShowCurrentPagerItem(HomePagerAdapter.ContactListFragment);
                break;

            case R.id.add_friend:
                ((Home) context).setShowCurrentPagerItem(HomePagerAdapter.ContactListFragment);
                ((Home) context).inviteFriendFromList();
                break;
            case R.id.editMobile:
                if (editMobile.getTag() != null) {
                    if (checkMobileValidation())
                    {
                        if (!ConnectionDetector.isInternetAvailable(context)) {
                            return;
                        }
                        sendOTP(phone.getText().toString().trim());
                    }
                }
                else
                    {
                    phone.setEnabled(true);
                    editMobile.setImageResource(R.drawable.send);
                    RevSDK.initKeypad((Activity) context, SharedPreference.retrieveLang(context));
                    editMobile.setTag("f");
                }
                break;
            case R.id.editsex:
                if (gen_group.isShown()) {
                    if (!ConnectionDetector.isInternetAvailable(context)) {
                        return;
                    }
                    sex.setVisibility(View.VISIBLE);
                    gen_group.setVisibility(View.INVISIBLE);
                    editsex.setImageResource(R.drawable.edit);
                    callUpdateProfileData("gender");
                    setNotification(getString(R.string.gendar_updated));
                } else {
                    editsex.setImageResource(R.drawable.send);
                    sex.setVisibility(View.INVISIBLE);
                    gen_group.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.editaddress:

                if (address_lay.isShown()) {
                    if (!ConnectionDetector.isInternetAvailable(context)) {
                        return;
                    }
                    address.setVisibility(View.VISIBLE);
                    address_lay.setVisibility(View.INVISIBLE);
                    editaddress.setImageResource(R.drawable.edit);
                    callUpdateProfileData("address");
                    setNotification(getString(R.string.address_updated));
                } else {
                    editaddress.setImageResource(R.drawable.send);
                    address.setVisibility(View.INVISIBLE);
                    address_lay.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.editdob:
                if (edit_dob_lay.isShown()) {
                    if (!ConnectionDetector.isInternetAvailable(context)) {
                        return;
                    }
                    if (ddd.getText().toString().trim().length() < 1 || mmm.getText().toString().trim().length() < 1 || yyy.getText().toString().trim().length() < 1) {
                        return;
                    }
                    dob.setVisibility(View.VISIBLE);
                    edit_dob_lay.setVisibility(View.INVISIBLE);
                    editdob.setImageResource(R.drawable.edit);
                    callUpdateProfileData("dateOfBirth");
                    setNotification(getString(R.string.dob_updated));
                } else {
                    editdob.setImageResource(R.drawable.send);
                    dob.setVisibility(View.INVISIBLE);
                    edit_dob_lay.setVisibility(View.VISIBLE);
                }

                break;
        }
    }

    private boolean checkMobileValidation() {
        String i_mobile = phone.getText().toString();
        if (i_mobile.trim().length() < 1) {
            Alert.alertDialog(context, getString(R.string.mobile_required), getString(R.string.sorry), false);
        } else if (i_mobile.trim().replace(" ", "").length() < 10) {
            editMobile.requestFocus();
            Alert.alertDialog(context, getString(R.string.mobile_number_less_than_ten), getString(R.string.sorry), false);
            return false;
        }
        return true;
    }

    private void sendOTP(String mobile) {
        if (mobile.trim().replace(" ", "").isEmpty()) {
            return;
        }
        String url = Constants.SEND_OTP + "+91" + mobile.trim() + "/AUTOGEN";
        new Retrofit2(context, this, SEND_OTP, url).callService(false);
    }

    private void openMediaDetail(DownloadedData data)
    {
        if (DrowableFunction.getAllFilesName(data.getDATA_PATH_DEVICE(), data.getMEDIA_NAME())) {
            startActivity(new Intent(context, MediaDetail.class).putExtra("DATA", data));
        } else {
            /*If client wants than need to download from URL*/
        }
    }

    public void scrollTillProfile() {
        parent_scrollView.setScrollY(0);
    }

    public void scrollTillDownload()
    {
        final ViewTreeObserver vto = parent_scrollView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                int centreX = (int) (download_list_lay.getX() + download_list_lay.getWidth() / 8);
                int centreY = (int) (download_list_lay.getY() + download_list_lay.getHeight() / 8);
                parent_scrollView.smoothScrollBy(centreX, centreY);
                parent_scrollView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    public void scrollTillFriendList() {
        final ViewTreeObserver vto = parent_scrollView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                int centreX = (int) (friend_list_lay.getX() + friend_list_lay.getWidth() / 2);
                int centreY = (int) (friend_list_lay.getY() + friend_list_lay.getHeight() / 2);
                parent_scrollView.smoothScrollBy(centreX, centreY);
                parent_scrollView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    public void scrollTillSave(int sel_tab) {
        final ViewTreeObserver vto = parent_scrollView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                int centreX = (int) (save_list_lay.getX() + save_list_lay.getWidth() / 2);
                int centreY = (int) (save_list_lay.getY() + save_list_lay.getHeight() / 2);
                parent_scrollView.smoothScrollBy(centreX, centreY);

                parent_scrollView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        if (sel_tab == 1) {
            setSelectedContributItemLay();
        } else {
            setSaveData();
        }
    }

    private void picProfileImageOnly() {
        if (Build.VERSION.SDK_INT > 21 && ContextCompat.checkSelfPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"}, REQUEST_PERMISSIONS_CODE_WRITE_STORAGE);
            return;
        }
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FOR_PROFILE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        picProfileImageOnly();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_PHOTO_FOR_PROFILE:
                if (resultCode == RESULT_OK) {
                    Uri picUri = data.getData();
                    cropImage(picUri);
                }
                break;
            case CROP_IMAGE:
                String imagepath = data.getExtras().get("PATH").toString();
                if (!imagepath.equalsIgnoreCase(DataBaseStrings.DEFAULT_VALUE)) {
                    imageView.setImageURI(Uri.fromFile(new File(imagepath)));
                    if (!imagepath.equalsIgnoreCase(DataBaseStrings.DEFAULT_VALUE)) {
                        uploadFile(imagepath);
                    }
                }
                break;
            case OTP_ACTIVITY_RESULT:
                if (resultCode == RESULT_OK) {
                    Alert.showLog("AO", "AYOOOO");
                    phone.setEnabled(false);
                    editMobile.setImageResource(R.drawable.edit);
                    RevSDK.destroyKeypad();
                    editMobile.setTag(null);
                    callUpdateMobile(phone.getText().toString());
                    setNotification(getString(R.string.update_mobile));
                }
                break;
        }
    }

    private void callUpdateMobile(String mobile) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("mobileNumber", oldMobileNumber);
            obj.put("mobileNumberNew", mobile);
            new Retrofit2(context, this, UPDATE_DEVICE, Constants.UPDATE_DEVICE, obj).callService(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callUpdateProfileData(String key) {
        try {
            JSONObject obj = new JSONObject();
            if (key.equalsIgnoreCase("dateOfBirth")) {
                obj.put("dateOfBirth", ddd.getText().toString() + "-" + mmm.getText().toString() + "-" + yyy.getText().toString());
            } else if (key.equalsIgnoreCase("address")) {
                obj.put("state", st);
                obj.put("district", dst);
                obj.put("block", blk);
                obj.put("stateRegional", spinner_state.getText().toString());
                obj.put("districtRegional",district.getText().toString());
                obj.put("blockRegional", input_block.getText().toString());
            } else if (key.equalsIgnoreCase("name")) {
                obj.put("firstName", SharedPreference.retrieveData(context, Constants.FIRST_NAME));
                obj.put("lastName", SharedPreference.retrieveData(context, Constants.LAST_NAME));
            } else if (key.equalsIgnoreCase("gender")) {
                int selectedId = gen_group.getCheckedRadioButtonId();
                String gen = "";
                if (selectedId == R.id.male) {
                    gen = "male";
                } else if (selectedId == R.id.female) {
                    gen = "female";
                }
                obj.put("gender", gen);
            }
            new Retrofit2(context, this, UPDATE_PROFILE, Constants.UPDATE_PROFILE + "/" + SharedPreference.retrieveData(context, Constants.USER_ID), obj).callService(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setNotification(String msg) {
        Alert.showToast(context, msg);
    }

    private void cropImage(Uri path) {
        String img_path = DrowableFunction.getRealPathFromURI(context, path);
        if (img_path == null) {
            img_path = DrowableFunction.getPath(context, path);
        }
        Intent intent = new Intent(context, CirculerCrop.class);
        intent.putExtra("imageUri", img_path);
        startActivityForResult(intent, CROP_IMAGE);
    }

    private void uploadFile(final String path) {
        edit_image.setVisibility(View.GONE);
        delet_image.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        imageView.setAlpha((float) 0.5);
        Alert.showLog("Path", "path- " + path);
        if (path != null) {
            File file = new File(path);
            RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("photos", file.getName(), reqFile);
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(3, TimeUnit.MINUTES)
                    .connectTimeout(2, TimeUnit.MINUTES).build();

            RetrofitService retrofitService = (RetrofitService) new Retrofit.Builder().
                    baseUrl(Constants.MEDIA_BASE)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create()).build()
                    .create(RetrofitService.class);
            String url = Constants.UPLOAD_PROFILE_PIC + "/" + SharedPreference.retrieveData(context, Constants.USER_ID);
            Alert.showLog("UPLOAD", " URL-- " + url);
            Call<ResponseBody> call = retrofitService.uploadMedia(url, filePart);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try
                    {
                       JSONObject data = new JSONObject(response.body().string());
                        if (data.has("url")) {
                            Alert.showLog("COME", "COME URL-- " + data.getString("url"));
                            SharedPreference.storeData(context, Constants.USER_IMAGE, data.getString("url"));
                        }
                      /*   Uri uriOne = Uri.parse(data.getString("url"));
                        imageView.setImageURI(uriOne);
                        */
                        progressBar.setVisibility(View.GONE);
                        edit_image.setVisibility(View.VISIBLE);
                        delet_image.setVisibility(View.VISIBLE);
                        imageView.setAlpha((float) 1.0);
                        // Picasso.with(getActivity()).load(data.getString("url")).into(imageView);
                        Alert.showToast(context, getString(R.string.update_profile_image));

                      /*  CountDownTimer x = new CountDownTimer(6000, 1000) {
                            public void onTick(long millisUntilFinished) {
                            }
                            public void onFinish() {
                                File pro = new File(path);
                                if (pro.exists()) {
                                    pro.delete();
                                }
                            }
                        };
                        x.start();*/
                    } catch (Exception e) {
                        Alert.showLog("Exception", "OKKKKK Exception-- " + e.toString());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Alert.showLog("NOOOO", "NOOOO-- " + t.getLocalizedMessage());
                }
            });
        }
    }

    private void setAddress() {
        setStateData();
        this.spinner_state.setAdapter(new ArrayAdapter(context, R.layout.drop_item, state));
        this.spinner_state.setKeyListener(null);
        this.spinner_state.setOnTouchListener(this);
        spinner_state.setFocusable(false);
        spinner_state.setFocusableInTouchMode(false);

        //  this.district.setAdapter(new ArrayAdapter(context, R.layout.drop_item, getResources().getStringArray(R.array.city_array)));
        this.district.setKeyListener(null);
        this.district.setOnTouchListener(this);
        district.setFocusable(false);
        district.setFocusableInTouchMode(false);

        //   this.input_block.setAdapter(new ArrayAdapter(context, R.layout.drop_item, getResources().getStringArray(R.array.block_array)));
        this.input_block.setKeyListener(null);
        this.input_block.setOnTouchListener(this);
        input_block.setFocusable(false);
        input_block.setFocusableInTouchMode(false);
        setItemClick();


        spinner_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner_state.showDropDown();
            }
        });

        district.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!spinner_state.getText().toString().toString().isEmpty()) {
                    district.showDropDown();
                }
            }
        });

        input_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!district.getText().toString().toString().isEmpty()) {
                    input_block.showDropDown();
                }
            }
        });

    }

    /*-------------- set dob calender ------------*/
    private void setCalender() {
        dt28.clear();
        dt29.clear();
        dt30.clear();
        dt31.clear();
        m.clear();
        y.clear();

        for (int i = 1; i < 29; i++) {
            String d = String.valueOf(i);
            if (i < 10) {
                d = "0" + d;
            }
            dt28.add(d);
        }

        for (int i = 1; i < 30; i++) {
            String d = String.valueOf(i);
            if (i < 10) {
                d = "0" + d;
            }
            dt29.add(d);
        }

        for (int i = 1; i < 31; i++) {
            String d = String.valueOf(i);
            if (i < 10) {
                d = "0" + d;
            }
            dt30.add(d);
        }

        for (int i = 1; i < 32; i++) {
            String d = String.valueOf(i);
            if (i < 10) {
                d = "0" + d;
            }
            dt31.add(d);
        }


        for (int j = 1; j < 13; j++) {
            String mmmm = String.valueOf(j);
            if (j < 10) {
                mmmm = "0" + mmmm;
            }
            m.add(mmmm);
        }

        for (int i = 1960; i < 2018; i++) {
            y.add(String.valueOf(i));
        }

        ddd.setAdapter(new ArrayAdapter(context, R.layout.drop_item, dt));
        ddd.setKeyListener(null);
        ddd.setOnTouchListener(this);
        setDay(31);

        mmm.setAdapter(new ArrayAdapter(context, R.layout.drop_item, m));
        mmm.setKeyListener(null);
        mmm.setOnTouchListener(this);

        yyy.setAdapter(new ArrayAdapter(context, R.layout.drop_item, y));
        yyy.setKeyListener(null);
        yyy.setOnTouchListener(this);

        mmm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mo = Integer.parseInt(((TextView) view).getText().toString());
                setDay(getDayInaMonth(yer, mo));
            }
        });

        yyy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                yer = Integer.parseInt(((TextView) view).getText().toString());
                setDay(getDayInaMonth(yer, mo));
            }
        });


        ddd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ddd.showDropDown();
            }
        });

        mmm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mmm.showDropDown();
            }
        });

        yyy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yyy.showDropDown();
            }
        });
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        ((TextView) view).setTextColor(-1);
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    private void setDay(int day) {
        dt.clear();
        switch (day) {
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

        try {
            if (!ddd.getText().toString().isEmpty()) {
                int dd_val = Integer.parseInt(ddd.getText().toString());
                if (dd_val > ddd.getAdapter().getCount()) {
                    ddd.setText(dt.get(dt.size() - 1));
                    ddd.setAdapter(new ArrayAdapter(context, R.layout.drop_item, dt));
                }
            }
        } catch (Exception e) {
            e.toString();
        }
    }

    private int getDayInaMonth(int yr, int mn) {
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
        } else {
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

    public void deleteOption(Context context, final DownloadedData data) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View dialogView = ((Activity) context).getLayoutInflater().inflate(R.layout.del_download, null);
        CardView yes_card_view = (CardView) dialogView.findViewById(R.id.yes_card_view);
        CardView no = (CardView) dialogView.findViewById(R.id.no);
        yes_card_view.setBackgroundResource(R.drawable.gradient_yello_radious);
        no.setBackgroundResource(R.drawable.gradient_black_radious);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setLayout((int) (((double) context.getResources().getDisplayMetrics().widthPixels) * 0.9d), -2);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        yes_card_view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alertDialog.dismiss();
                helper.deleteDownload(data);
                setDownloadData();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onServiceResponse(int i, Response<ResponseBody> response) {
        try {
            switch (i) {
                case SEND_OTP:
                    JSONObject object = new JSONObject(response.body().string());
                    if (object.getString("Status").equalsIgnoreCase("Success")) {
                        String otp_id = object.getString("Details");
                        startActivityForResult(new Intent(context, OTPScreen.class).putExtra("SOURCE", "MY_FRAGMENT").putExtra("OTP_ID", otp_id).putExtra("NUMBER", phone.getText().toString().trim()).putExtra("OLD_NUMBER", phone.getText().toString().trim()), OTP_ACTIVITY_RESULT);
                    }
                    break;
                case UPDATE_DEVICE:
                    JSONObject objecttt = new JSONObject(response.body().string());
                    Alert.showLog("MYA", "MY_ACCOUNT-- " + objecttt.toString());

                    if (objecttt.getString("success").equalsIgnoreCase("true")) {
                        if (objecttt.has("info")) {
                            JSONObject dataObj = objecttt.getJSONObject("info");
                            SharedPreference.storeData(context, Constants.USER_ID, dataObj.getString("_id"));
                            SharedPreference.storeData(context, Constants.FIRST_NAME, dataObj.getString("firstName"));
                            SharedPreference.storeData(context, Constants.LAST_NAME, dataObj.getString("lastName"));
                            SharedPreference.storeData(context, Constants.MOBILE_NUMBER, dataObj.getString("mobileNumber"));
                            SharedPreference.storeData(context, Constants.STATE, dataObj.getString("state"));
                            SharedPreference.storeData(context, Constants.DISTRICT, dataObj.getString("district"));
                            SharedPreference.storeData(context, Constants.BLOCK, dataObj.getString("block"));
                            SharedPreference.storeData(context, Constants.GENDER, dataObj.getString("gender"));
                            SharedPreference.storeData(context, Constants.DOB, dataObj.getString("dateOfBirth"));
                            SharedPreference.storeData(context, Constants.USER_IMAGE, dataObj.getString("userImage"));
                            SharedPreference.storeData(context, Constants.USER_REF_CODE, dataObj.getString("referralCode"));

                            if(dataObj.has("stateRegional")) {
                                SharedPreference.storeData(context, Constants.STATE_REGIONAL, dataObj.getString("stateRegional"));
                                SharedPreference.storeData(context, Constants.DISTRICT_REGIONAL, dataObj.getString("districtRegional"));
                                SharedPreference.storeData(context, Constants.BLOCK_REGIONAL, dataObj.getString("blockRegional"));
                            }

                            if (dataObj.has("totalSubmissions")) {
                                SharedPreference.storeData(context, Constants.USER_YOGDAN, dataObj.getString("totalSubmissions"));
                            }

                            if (dataObj.has("totalPublications")) {
                                SharedPreference.storeData(context, Constants.USER_LEKH, dataObj.getString("totalPublications"));
                            }
                        }
                    } else {
                        Alert.showToast(context, objecttt.getString("msg"));
                    }
                    break;
                case UPDATE_PROFILE:
                    JSONObject objectttt = new JSONObject(response.body().string());
                    if (objectttt.has("info")) {
                        JSONObject dataObj = objectttt.getJSONObject("info");
                        SharedPreference.storeData(context, Constants.USER_ID, dataObj.getString("_id"));
                        SharedPreference.storeData(context, Constants.FIRST_NAME, dataObj.getString("firstName"));
                        SharedPreference.storeData(context, Constants.LAST_NAME, dataObj.getString("lastName"));
                        SharedPreference.storeData(context, Constants.MOBILE_NUMBER, dataObj.getString("mobileNumber"));
                        SharedPreference.storeData(context, Constants.STATE, dataObj.getString("state"));
                        SharedPreference.storeData(context, Constants.DISTRICT, dataObj.getString("district"));
                        SharedPreference.storeData(context, Constants.BLOCK, dataObj.getString("block"));
                        SharedPreference.storeData(context, Constants.GENDER, dataObj.getString("gender"));
                        SharedPreference.storeData(context, Constants.DOB, dataObj.getString("dateOfBirth"));
                        SharedPreference.storeData(context, Constants.USER_IMAGE, dataObj.getString("userImage"));
                        SharedPreference.storeData(context, Constants.USER_REF_CODE, dataObj.getString("referralCode"));
                        if(dataObj.has("stateRegional")) {
                            SharedPreference.storeData(context, Constants.STATE_REGIONAL, dataObj.getString("stateRegional"));
                            SharedPreference.storeData(context, Constants.DISTRICT_REGIONAL, dataObj.getString("districtRegional"));
                            SharedPreference.storeData(context, Constants.BLOCK_REGIONAL, dataObj.getString("blockRegional"));
                        }
                        if (dataObj.has("totalSubmissions")) {
                            SharedPreference.storeData(context, Constants.USER_YOGDAN, dataObj.getString("totalSubmissions"));
                        }
                        if (dataObj.has("totalPublications")) {
                            SharedPreference.storeData(context, Constants.USER_LEKH, dataObj.getString("totalPublications"));
                        }
                    }
                    setProfileData();
                    break;
                case DELETE_PROFILE_PIC:
                    Alert.showLog("Response","Response-- "+response.body().string());
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*Function Related To State, District, Block*/
    private void setItemClick() {
        spinner_state.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                st = state_data.get(position).getState_name_english();
                dst = "";
                blk = "";
                district.setText("");
                input_block.setText("");
                getAllDistrict(state_data.get(position));
            }
        });

        district.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                dst = district_data.get(position).getDistrict_name_english();
                blk = "";
                input_block.setText("");
                getAllBlocks(district_data.get(position));
            }
        });

        input_block.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                blk = block_data.get(position).getBlock_name_english();
            }
        });
    }

    private void getAllDistrict(Data_Model state_data) {
        district_data.clear();
        distri.clear();
        district_data = state_data.getDistricts();
        for (District_Data_Model st : district_data) {
            distri.add(st.getDistrict_regional());
        }
        district.setAdapter(new ArrayAdapter(context, R.layout.drop_item, distri));
    }

    private void getAllBlocks(District_Data_Model district_data) {
        block_data.clear();
        blocs.clear();
        block_data = district_data.getBlocks();
        for (Blocks_Data_Model st : block_data) {
            blocs.add(st.getBlock_name_regional());
        }
        input_block.setAdapter(new ArrayAdapter(context, R.layout.drop_item, blocs));
    }

    private void setStateData() {
        state_data.clear();
        state.clear();
        state_data = Controller.getState_data();
        for (Data_Model st : state_data) {
            state.add(st.getState_name_regional());
        }
    }
}