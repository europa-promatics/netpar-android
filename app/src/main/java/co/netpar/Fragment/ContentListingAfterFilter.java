package co.netpar.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import java.util.ArrayList;
import java.util.List;

import co.netpar.Adapter.ContentListingAdapter;
import co.netpar.Adapter.ContentListingAdapterAfterFilter;
import co.netpar.Adapter.ListingView_four_adapter;
import co.netpar.App.Controller;
import co.netpar.Comman.Alert;
import co.netpar.Comman.Constants;
import co.netpar.ContentView;
import co.netpar.Home;
import co.netpar.Model.CategoryModel;
import co.netpar.Model.ContentListingModel;
import co.netpar.Model.ContentListingModelThree;
import co.netpar.PagerAdapter.HomePagerAdapter;
import co.netpar.R;
import co.netpar.Syncronization.NetparDataBase.DatabaseHelper;

/**
 * Created by promatics on 11/18/2017.
 */
public class ContentListingAfterFilter extends Fragment{
    private Context context;
    private String TAG = "ContentListingAfterFilter";
    private RecyclerView item_list;
    private ContentListingAdapterAfterFilter adapter;
    public static ContentListingAfterFilter instance;
    public List<ContentListingModel> comeContent = new ArrayList<>();
    private CountDownTimer x;

    private SimpleDraweeView main_image, two_corner_main_image;
    private TextView title;
    private DatabaseHelper helper;
    private LinearLayout top_lay;

    public static final int Listing_view_Template_One = 0;
    public static final int Listing_view_Template_Two = 1;
    public static final int Listing_view_Template_Three = 2;
    public static final int Listing_view_Template_Four = 3;
    public static final int Listing_view_Template_Five = 4;
    public static final int Listing_view_Template_Six = 5;
    public static final int Listing_view_Template_Seven = 6;

    private int tenDP = 0, fiveDP = 0;
    private List<ContentListingModel> content_dataa_for_7_4 = new ArrayList<>();
    private boolean isVisibleToUser=false;

    private String listing_typee;
    public List<ContentListingModel> comeContentDumy = new ArrayList<>();

    public static synchronized ContentListingAfterFilter getInstance() {
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_listing_after_filter, container, false);
        instance = this;
        context = container.getContext();

        tenDP = getResources().getDimensionPixelSize(R.dimen.dp10);
        fiveDP = getResources().getDimensionPixelSize(R.dimen.dp5);

        initializeView(v);
        return v;
    }

    private void initializeView(View v)
    {
        item_list = (RecyclerView) v.findViewById(R.id.item_list);
        item_list.setNestedScrollingEnabled(false);

        main_image = (SimpleDraweeView) v.findViewById(R.id.main_image);
        two_corner_main_image = (SimpleDraweeView) v.findViewById(R.id.two_corner_main_image);
        title = (TextView) v.findViewById(R.id.title);
        top_lay = (LinearLayout) v.findViewById(R.id.top_lay);
        helper = ((Home) context).helper;
        setLogoutManager("Listing-view Template One");
        adapter = new ContentListingAdapterAfterFilter(context, comeContent, ((Home) context).helper, Listing_view_Template_One);
        adapter.setOnClickListener(new ContentListingAdapterAfterFilter.OnClickListener() {
            @Override
            public void OnClick(int position, View view, ContentListingModel data)
            {
                startActivityForResult(new Intent(context, ContentView.class).putExtra("DATA", data), HomeFragment.CONTENT_VIEW);
            }
        });
        item_list.setAdapter(adapter);
    }

    /*------------- set filtered data with Listing View ------------------*/
    public void setData(List<ContentListingModel> list, String listing_type)
    {
        comeContent.clear();
        adapter.notifyDataSetChanged();
        comeContent.addAll(list);
        googleAnalystsEvent(listing_type);
        comeContentDumy.clear();
        comeContentDumy.addAll(comeContent);


        final ContentListingModel dt = comeContent.get(0);
        title.setText(dt.getHeadline());

        Uri imageUri = Uri.parse(dt.getHorizontalPicture());
        main_image.setImageURI(imageUri);
        two_corner_main_image.setImageURI(imageUri);

        main_image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(context, ContentView.class).putExtra("DATA", dt), HomeFragment.CONTENT_VIEW);
            }
        });

        two_corner_main_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(context, ContentView.class).putExtra("DATA", dt), HomeFragment.CONTENT_VIEW);
            }
        });
        comeContent.remove(0);
        if (listing_type.equalsIgnoreCase("Listing-view Template Six"))
        {
            two_corner_main_image.setVisibility(View.GONE);
            main_image.setVisibility(View.VISIBLE);
            top_lay.setPadding(0, 0, 0, 0);
            item_list.setPadding(fiveDP, fiveDP, fiveDP, 0);
            content_dataa_for_7_4.clear();
            content_dataa_for_7_4.addAll(parseListData(comeContent));
            ContentListingAdapter adapter = new ContentListingAdapter(context, content_dataa_for_7_4, helper);
            item_list.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
            item_list.setAdapter(adapter);
            adapter.setOnClickListener(new ContentListingAdapter.OnClickListener() {
                @Override
                public void OnClick(int position, View view, ContentListingModel data) {
                    startActivityForResult(new Intent(context, ContentView.class).putExtra("DATA", data), HomeFragment.CONTENT_VIEW);
                }
            });
        }
        else if (listing_type.equalsIgnoreCase("Listing-view Template Four"))
        {
            two_corner_main_image.setVisibility(View.GONE);
            main_image.setVisibility(View.VISIBLE);
            top_lay.setPadding(0, 0, 0, 0);
            item_list.setPadding(fiveDP, fiveDP, fiveDP, 0);
            content_dataa_for_7_4.clear();
            content_dataa_for_7_4.addAll(parseListData(comeContent));
            ListingView_four_adapter adapter = new ListingView_four_adapter(context, content_dataa_for_7_4, helper);
            item_list.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
            item_list.setAdapter(adapter);
            adapter.setOnClickListener(new ListingView_four_adapter.OnClickListener() {
                @Override
                public void OnClick(int position, View view, ContentListingModel data) {
                    startActivityForResult(new Intent(context, ContentView.class).putExtra("DATA", data), HomeFragment.CONTENT_VIEW);
                }
            });
        }

        else
        {
            item_list.removeAllViews();
            setLogoutManager(listing_type);
            adapter = new ContentListingAdapterAfterFilter(context, comeContent, ((Home) context).helper, Listing_view_Template_One);
            adapter.setOnClickListener(new ContentListingAdapterAfterFilter.OnClickListener() {
                @Override
                public void OnClick(int position, View view, ContentListingModel data)
                {
                    startActivityForResult(new Intent(context, ContentView.class).putExtra("DATA", data), HomeFragment.CONTENT_VIEW);
                }
            });
            item_list.setAdapter(adapter);
        }
        listing_typee=listing_type;
    }

    private void googleAnalystsEvent(String value)
    {
        if (comeContent.size()>0)
        {
            new Controller().trackEvent("Template","click",value,comeContent.get(0).getItem_id());
        }
    }

    private void setLogoutManager(String listing_type) {
        try {
            if (listing_type.equalsIgnoreCase("Listing-view Template Three")|| listing_type.equalsIgnoreCase("Listing-view Template Five") || listing_type.equalsIgnoreCase("Listing-view Template Six")) {
                item_list.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
            }
            else
            {
                item_list.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                item_list.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp8)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

          /* set Views */
        if (adapter != null) {

            Alert.showLog("listing_type","listing_type-- "+listing_type);

            if (listing_type.equalsIgnoreCase("Listing-view Template One")) {
                adapter.setViewType(Listing_view_Template_One);
                two_corner_main_image.setVisibility(View.GONE);
                main_image.setVisibility(View.VISIBLE);
                top_lay.setPadding(fiveDP, fiveDP, fiveDP, 0);
                item_list.setPadding(0, tenDP, 0, 0);
            } else if (listing_type.equalsIgnoreCase("Listing-view Template Two")) {
                top_lay.setPadding(0, 0, 0, 0);
                item_list.setPadding(fiveDP, tenDP, fiveDP, 0);
                two_corner_main_image.setVisibility(View.VISIBLE);
                main_image.setVisibility(View.GONE);

                adapter.setViewType(Listing_view_Template_Two);
            } else if (listing_type.equalsIgnoreCase("Listing-view Template Three")) {
                top_lay.setPadding(0, 0, 0, 0);
                item_list.setPadding(fiveDP, tenDP, fiveDP, 0);
                two_corner_main_image.setVisibility(View.VISIBLE);
                main_image.setVisibility(View.GONE);

                adapter.setViewType(Listing_view_Template_Three);
            } else if (listing_type.equalsIgnoreCase("Listing-view Template Five")) {
                two_corner_main_image.setVisibility(View.GONE);
                main_image.setVisibility(View.VISIBLE);
                top_lay.setPadding(0, 0, 0, 0);
                item_list.setPadding(fiveDP, tenDP, fiveDP, 0);
                adapter.setViewType(Listing_view_Template_Five);
            } else if (listing_type.equalsIgnoreCase("Listing-view Template Seven")) {
                adapter.setViewType(Listing_view_Template_Seven);
            }
        }
    }

    /*----------------- regfresh listing view -------------------------*/
    public void setAutoRefresh()
    {
        try
        {
            if(comeContent.size()>0)
            {
                if(CategoryFragment.sectionIDDDD!=null)
                {
                    List<ContentListingModel> comeContenttt = new ArrayList<>();
                    List<CategoryModel> comeCategory = ((Home) context).helper.getAllCategory(CategoryFragment.sectionIDDDD);
                    if(!listing_typee.equalsIgnoreCase(comeCategory.get(CategoryFragment.positionnn).getListViewFormat()))
                    {
                        listing_typee=comeCategory.get(CategoryFragment.positionnn).getListViewFormat();
                        comeContenttt.addAll(comeContentDumy);
                        Alert.showLog("listing_typee",listing_typee);
                        Alert.showLog("comeContenttt","comeContenttt.size()-- "+comeContenttt.size());
                        setData(comeContenttt, listing_typee);
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case HomeFragment.CONTENT_VIEW:
                if (resultCode == Activity.RESULT_OK) {
                    int result = data.getIntExtra("VALUE", 0);
                    switch (result) {
                        case ContentView.HOME:
                            break;
                        case ContentView.BACK:
                            break;
                        case ContentView.CONTRIBUTION:
                            ((Home) context).setShowCurrentPagerItem(HomePagerAdapter.contribution);
                            break;
                        case ContentView.ACCOUNT:
                            ((Home) context).expendBottomSheet();
                            break;
                        case ContentView.DOWNLOAD:
                            ((Home)context). openAccountWithScroll(2);
                            break;
                        case ContentView.FRIEND:
                            ((Home)context).openFriendList();
                            break;
                        case ContentView.MY:
                            ((Home)context).openMyContribution();
                            break;
                        case ContentView.PROFILE:
                            ((Home)context). openAccountWithScroll(0);
                            break;
                        case ContentView.SAVED:
                            ((Home)context).openSaveItem();
                            break;
                    }
                }
                break;
        }
    }

    private List<ContentListingModel> parseListData(List<ContentListingModel> dataBaseList) {
        List<ContentListingModel> updatedData = new ArrayList<>();
        List<ContentListingModelThree> three_updatedData = new ArrayList<>();

        for (int i = 0; i < dataBaseList.size(); i++) {
            switch (i) {
                case 0:
                    updatedData.add(dataBaseList.get(i));
                    break;
                case 1:
                    updatedData.add(dataBaseList.get(i));
                    break;
                case 2:
                    updatedData.add(dataBaseList.get(i));
                    break;
                case 3:
                    ContentListingModelThree dt = new ContentListingModelThree();
                    dt.setContentId(dataBaseList.get(i).getContentId());
                    dt.setHeadline(dataBaseList.get(i).getHeadline());
                    dt.setSlug(dataBaseList.get(i).getSlug());
                    dt.setItem_id(dataBaseList.get(i).getItem_id());
                    dt.setTagline(dataBaseList.get(i).getTagline());
                    dt.setFirstImage(dataBaseList.get(i).getFirstImage());
                    dt.setCategoryId(dataBaseList.get(i).getCategoryId());
                    dt.setCategoryName(dataBaseList.get(i).getCategoryName());
                    dt.setSectionName(dataBaseList.get(i).getSectionName());
                    dt.setSectionId(dataBaseList.get(i).getSectionId());
                    dt.setSubCategoryId(dataBaseList.get(i).getSubCategoryId());
                    dt.setSubCategoryName(dataBaseList.get(i).getSubCategoryName());
                    dt.setLanguage(dataBaseList.get(i).getLanguage());
                    dt.setContentLocation(dataBaseList.get(i).getContentLocation());
                    dt.setLike(dataBaseList.get(i).getLike());
                    dt.setTotal_like(dataBaseList.get(i).getTotal_like());
                    dt.setComment_count(dataBaseList.get(i).getComment_count());
                    dt.setShare_count(dataBaseList.get(i).getShare_count());
                    dt.setView_count(dataBaseList.get(i).getView_count());
                    dt.setCreator_first_name(dataBaseList.get(i).getCreator_first_name());
                    dt.setCreator_last_name(dataBaseList.get(i).getCreator_last_name());
                    dt.setDateOfCreation(dataBaseList.get(i).getDateOfCreation());
                    dt.setSuggestedArticleList(dataBaseList.get(i).getSuggestedArticleList());
                    three_updatedData.add(dt);

                    if (dataBaseList.size() == (i + 1)) {
                        ContentListingModel mod = new ContentListingModel();
                        mod.setThreeItem(three_updatedData);
                        updatedData.add(mod);
                    }

                    Alert.showLog(TAG, "three_updatedData 3-- " + three_updatedData.size());

                    break;
                case 4:
                    ContentListingModelThree dtt = new ContentListingModelThree();
                    dtt.setContentId(dataBaseList.get(i).getContentId());
                    dtt.setHeadline(dataBaseList.get(i).getHeadline());
                    dtt.setSlug(dataBaseList.get(i).getSlug());
                    dtt.setItem_id(dataBaseList.get(i).getItem_id());
                    dtt.setTagline(dataBaseList.get(i).getTagline());
                    dtt.setFirstImage(dataBaseList.get(i).getFirstImage());
                    dtt.setCategoryId(dataBaseList.get(i).getCategoryId());
                    dtt.setCategoryName(dataBaseList.get(i).getCategoryName());
                    dtt.setSectionName(dataBaseList.get(i).getSectionName());
                    dtt.setSectionId(dataBaseList.get(i).getSectionId());
                    dtt.setSubCategoryId(dataBaseList.get(i).getSubCategoryId());
                    dtt.setSubCategoryName(dataBaseList.get(i).getSubCategoryName());
                    dtt.setLanguage(dataBaseList.get(i).getLanguage());
                    dtt.setContentLocation(dataBaseList.get(i).getContentLocation());
                    dtt.setLike(dataBaseList.get(i).getLike());
                    dtt.setTotal_like(dataBaseList.get(i).getTotal_like());
                    dtt.setComment_count(dataBaseList.get(i).getComment_count());
                    dtt.setShare_count(dataBaseList.get(i).getShare_count());
                    dtt.setView_count(dataBaseList.get(i).getView_count());
                    dtt.setCreator_first_name(dataBaseList.get(i).getCreator_first_name());
                    dtt.setCreator_last_name(dataBaseList.get(i).getCreator_last_name());
                    dtt.setDateOfCreation(dataBaseList.get(i).getDateOfCreation());
                    dtt.setSuggestedArticleList(dataBaseList.get(i).getSuggestedArticleList());
                    three_updatedData.add(dtt);
                    if (dataBaseList.size() == (i + 1)) {
                        ContentListingModel mod = new ContentListingModel();
                        mod.setThreeItem(three_updatedData);
                        updatedData.add(mod);
                    }

                    Alert.showLog(TAG, "three_updatedData 4-- " + three_updatedData.size());

                    break;
                case 5:
                    ContentListingModelThree dttt = new ContentListingModelThree();
                    dttt.setContentId(dataBaseList.get(i).getContentId());
                    dttt.setHeadline(dataBaseList.get(i).getHeadline());
                    dttt.setSlug(dataBaseList.get(i).getSlug());
                    dttt.setItem_id(dataBaseList.get(i).getItem_id());
                    dttt.setTagline(dataBaseList.get(i).getTagline());
                    dttt.setFirstImage(dataBaseList.get(i).getFirstImage());
                    dttt.setCategoryId(dataBaseList.get(i).getCategoryId());
                    dttt.setCategoryName(dataBaseList.get(i).getCategoryName());
                    dttt.setSectionName(dataBaseList.get(i).getSectionName());
                    dttt.setSectionId(dataBaseList.get(i).getSectionId());
                    dttt.setSubCategoryId(dataBaseList.get(i).getSubCategoryId());
                    dttt.setSubCategoryName(dataBaseList.get(i).getSubCategoryName());
                    dttt.setLanguage(dataBaseList.get(i).getLanguage());
                    dttt.setContentLocation(dataBaseList.get(i).getContentLocation());
                    dttt.setLike(dataBaseList.get(i).getLike());
                    dttt.setTotal_like(dataBaseList.get(i).getTotal_like());
                    dttt.setComment_count(dataBaseList.get(i).getComment_count());
                    dttt.setShare_count(dataBaseList.get(i).getShare_count());
                    dttt.setView_count(dataBaseList.get(i).getView_count());
                    dttt.setCreator_first_name(dataBaseList.get(i).getCreator_first_name());
                    dttt.setCreator_last_name(dataBaseList.get(i).getCreator_last_name());
                    dttt.setDateOfCreation(dataBaseList.get(i).getDateOfCreation());
                    dttt.setSuggestedArticleList(dataBaseList.get(i).getSuggestedArticleList());
                    three_updatedData.add(dttt);

                    ContentListingModel mod = new ContentListingModel();
                    mod.setThreeItem(three_updatedData);
                    updatedData.add(mod);

                    Alert.showLog(TAG, "three_updatedData 5-- " + three_updatedData.size());

                    break;
                default:
                    switch (i % 6) {
                        case 0:
                            updatedData.add(dataBaseList.get(i));
                            Alert.showLog(TAG, "updatedData default 0-- " + updatedData.size());
                            break;
                        case 1:
                            updatedData.add(dataBaseList.get(i));
                            Alert.showLog(TAG, "updatedData default 1-- " + updatedData.size());
                            break;
                        case 2:
                            updatedData.add(dataBaseList.get(i));
                            Alert.showLog(TAG, "updatedData default 2-- " + updatedData.size());
                            break;
                        case 3:
                            Alert.showLog(TAG, "updatedData default 3-- " + updatedData.size());
                            if (three_updatedData.size() > 2) {
                                // three_updatedData.clear();
                                three_updatedData = new ArrayList<>();
                            }
                            ContentListingModelThree dtttt = new ContentListingModelThree();
                            dtttt.setContentId(dataBaseList.get(i).getContentId());
                            dtttt.setHeadline(dataBaseList.get(i).getHeadline());
                            dtttt.setSlug(dataBaseList.get(i).getSlug());
                            dtttt.setItem_id(dataBaseList.get(i).getItem_id());
                            dtttt.setTagline(dataBaseList.get(i).getTagline());
                            dtttt.setFirstImage(dataBaseList.get(i).getFirstImage());
                            dtttt.setCategoryId(dataBaseList.get(i).getCategoryId());
                            dtttt.setCategoryName(dataBaseList.get(i).getCategoryName());
                            dtttt.setSectionName(dataBaseList.get(i).getSectionName());
                            dtttt.setSectionId(dataBaseList.get(i).getSectionId());
                            dtttt.setSubCategoryId(dataBaseList.get(i).getSubCategoryId());
                            dtttt.setSubCategoryName(dataBaseList.get(i).getSubCategoryName());
                            dtttt.setLanguage(dataBaseList.get(i).getLanguage());
                            dtttt.setContentLocation(dataBaseList.get(i).getContentLocation());
                            dtttt.setLike(dataBaseList.get(i).getLike());
                            dtttt.setTotal_like(dataBaseList.get(i).getTotal_like());
                            dtttt.setComment_count(dataBaseList.get(i).getComment_count());
                            dtttt.setShare_count(dataBaseList.get(i).getShare_count());
                            dtttt.setView_count(dataBaseList.get(i).getView_count());
                            dtttt.setCreator_first_name(dataBaseList.get(i).getCreator_first_name());
                            dtttt.setCreator_last_name(dataBaseList.get(i).getCreator_last_name());
                            dtttt.setDateOfCreation(dataBaseList.get(i).getDateOfCreation());
                            dtttt.setSuggestedArticleList(dataBaseList.get(i).getSuggestedArticleList());
                            three_updatedData.add(dtttt);
                            if (dataBaseList.size() == (i + 1)) {
                                ContentListingModel modd = new ContentListingModel();
                                modd.setThreeItem(three_updatedData);
                                updatedData.add(modd);
                                // three_updatedData.clear();
                            }
                            break;
                        case 4:
                            Alert.showLog(TAG, "updatedData default 4-- " + updatedData.size());

                            ContentListingModelThree dttttt = new ContentListingModelThree();
                            dttttt.setContentId(dataBaseList.get(i).getContentId());
                            dttttt.setHeadline(dataBaseList.get(i).getHeadline());
                            dttttt.setSlug(dataBaseList.get(i).getSlug());
                            dttttt.setItem_id(dataBaseList.get(i).getItem_id());
                            dttttt.setTagline(dataBaseList.get(i).getTagline());
                            dttttt.setFirstImage(dataBaseList.get(i).getFirstImage());
                            dttttt.setCategoryId(dataBaseList.get(i).getCategoryId());
                            dttttt.setCategoryName(dataBaseList.get(i).getCategoryName());
                            dttttt.setSectionName(dataBaseList.get(i).getSectionName());
                            dttttt.setSectionId(dataBaseList.get(i).getSectionId());
                            dttttt.setSubCategoryId(dataBaseList.get(i).getSubCategoryId());
                            dttttt.setSubCategoryName(dataBaseList.get(i).getSubCategoryName());
                            dttttt.setLanguage(dataBaseList.get(i).getLanguage());
                            dttttt.setContentLocation(dataBaseList.get(i).getContentLocation());
                            dttttt.setLike(dataBaseList.get(i).getLike());
                            dttttt.setTotal_like(dataBaseList.get(i).getTotal_like());
                            dttttt.setComment_count(dataBaseList.get(i).getComment_count());
                            dttttt.setShare_count(dataBaseList.get(i).getShare_count());
                            dttttt.setView_count(dataBaseList.get(i).getView_count());
                            dttttt.setCreator_first_name(dataBaseList.get(i).getCreator_first_name());
                            dttttt.setCreator_last_name(dataBaseList.get(i).getCreator_last_name());
                            dttttt.setDateOfCreation(dataBaseList.get(i).getDateOfCreation());
                            dttttt.setSuggestedArticleList(dataBaseList.get(i).getSuggestedArticleList());
                            three_updatedData.add(dttttt);

                            if (dataBaseList.size() == (i + 1)) {
                                ContentListingModel moddd = new ContentListingModel();
                                moddd.setThreeItem(three_updatedData);
                                updatedData.add(moddd);
                                // three_updatedData.clear();
                            }
                            break;
                        case 5:
                            Alert.showLog(TAG, "updatedData default 5-- " + updatedData.size());

                            ContentListingModelThree dtttttt = new ContentListingModelThree();
                            dtttttt.setContentId(dataBaseList.get(i).getContentId());
                            dtttttt.setHeadline(dataBaseList.get(i).getHeadline());
                            dtttttt.setSlug(dataBaseList.get(i).getSlug());
                            dtttttt.setItem_id(dataBaseList.get(i).getItem_id());
                            dtttttt.setTagline(dataBaseList.get(i).getTagline());
                            dtttttt.setFirstImage(dataBaseList.get(i).getFirstImage());
                            dtttttt.setCategoryId(dataBaseList.get(i).getCategoryId());
                            dtttttt.setCategoryName(dataBaseList.get(i).getCategoryName());
                            dtttttt.setSectionName(dataBaseList.get(i).getSectionName());
                            dtttttt.setSectionId(dataBaseList.get(i).getSectionId());
                            dtttttt.setSubCategoryId(dataBaseList.get(i).getSubCategoryId());
                            dtttttt.setSubCategoryName(dataBaseList.get(i).getSubCategoryName());
                            dtttttt.setLanguage(dataBaseList.get(i).getLanguage());
                            dtttttt.setContentLocation(dataBaseList.get(i).getContentLocation());
                            dtttttt.setLike(dataBaseList.get(i).getLike());
                            dtttttt.setTotal_like(dataBaseList.get(i).getTotal_like());
                            dtttttt.setComment_count(dataBaseList.get(i).getComment_count());
                            dtttttt.setShare_count(dataBaseList.get(i).getShare_count());
                            dtttttt.setView_count(dataBaseList.get(i).getView_count());
                            dtttttt.setCreator_first_name(dataBaseList.get(i).getCreator_first_name());
                            dtttttt.setCreator_last_name(dataBaseList.get(i).getCreator_last_name());
                            dtttttt.setDateOfCreation(dataBaseList.get(i).getDateOfCreation());
                            dtttttt.setSuggestedArticleList(dataBaseList.get(i).getSuggestedArticleList());
                            three_updatedData.add(dtttttt);

                            ContentListingModel moddddd = new ContentListingModel();
                            moddddd.setThreeItem(three_updatedData);
                            updatedData.add(moddddd);
                            break;
                    }
                    break;
            }
        }
        return updatedData;
    }

    private static class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private final int space;

        public SpacesItemDecoration(int space) {
            this.space = 4;
        }

        @Override
        public void getItemOffsets(Rect outRect, final View view, final RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            try {
                int position = parent.getChildAdapterPosition(view);
                int spanIndex = ((StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams()).getSpanIndex();
                if (spanIndex == 0) {
                    outRect.left = 0;
                    outRect.right = space;
                } else if (spanIndex == 1) {
                    outRect.left = space;
                    outRect.right = 0;
                } else {
                    if (spanIndex % 2 == 1) {
                        outRect.left = 0;
                        outRect.right = space;
                    } else {
                        outRect.left = space;
                        outRect.right = 0;
                    }
                }
                outRect.bottom = space;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser=isVisibleToUser;
        if(isVisibleToUser)
        {
        }
        else
        {
            Constants.subCategoryId=null;
            if(x!=null)
            {
                x=null;
            }
        }
    }
}

