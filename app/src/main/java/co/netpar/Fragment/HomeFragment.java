package co.netpar.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import co.netpar.Adapter.HorizontalSectionAdaapter;
import co.netpar.Adapter.NewContentListingAdapter;
import co.netpar.Adapter.VerticalSectionAdapter;
import co.netpar.App.Controller;
import co.netpar.Comman.Alert;
import co.netpar.Comman.Constants;
import co.netpar.ContentView;
import co.netpar.Home;
import co.netpar.Model.CategoryModel;
import co.netpar.Model.ContentListingModel;
import co.netpar.Model.SectionModel;
import co.netpar.Network.ConnectionDetector;
import co.netpar.PagerAdapter.HomePagerAdapter;
import co.netpar.R;
import co.netpar.Syncronization.NetparDataBase.DatabaseHelper;

/**
 * Created by promatics on 10/8/2017.
 */

public class HomeFragment extends Fragment implements View.OnClickListener{
    private Context context;
    private RecyclerView vertical_list,item_list;
    private String TAG = "HomeFragment";
    public  RecyclerView horizontal_recyler;
    private TextView mCenterIndicator;
    private Toolbar toolbar;
    private float dd_to_set_alpha=1;
    private ImageView left_arrow,right_arrow,bottom_arrow;
    private LinearLayoutManager lm;
    private HorizontalSectionAdaapter horizontalSectionAdaapter;
    private VerticalSectionAdapter verticalSectionAdapter;
    private DatabaseHelper helper;
    private List<SectionModel> section_data = new ArrayList<>();
    public static HomeFragment instance;
    private List<ContentListingModel> content_data = new ArrayList<>();
    private NewContentListingAdapter contentListingAdapter;
    private Parcelable sectionRecyclerViewState;
    public static final int CONTENT_VIEW=1000;
    public static synchronized HomeFragment getInstance()
    {
        return instance;
    }
    private RelativeLayout header_relative_layout;
    private int i=0;
    public static CoordinatorLayout main_lay;

    public SwipeRefreshLayout swipe_verticle;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_fragment, container, false);
        instance=this;
        context=container.getContext();
        initializeView(v);
        return v;
    }

    /*-------------- initialize views --------------*/
    private void initializeView(View v)
    {
        main_lay=(CoordinatorLayout)v.findViewById(R.id.main_lay);
        left_arrow=(ImageView)v.findViewById(R.id.left_arrow);
        right_arrow=(ImageView)v.findViewById(R.id.right_arrow);
        bottom_arrow=(ImageView)v.findViewById(R.id.bottom_arrow);
        left_arrow.setOnClickListener(this);
        right_arrow.setOnClickListener(this);
        bottom_arrow.setOnClickListener(this);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)v.findViewById(R.id.collepse_tool);
        final AppBarLayout app_bar=(AppBarLayout)v.findViewById(R.id.app_bar);
        toolbar=(Toolbar)v.findViewById(R.id.toolbar);
        toolbar.setBackgroundResource(R.color.gray_back);

        app_bar.addOnOffsetChangedListener( new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float percentage = ((float) Math.abs(verticalOffset) / appBarLayout.getTotalScrollRange());
                if (percentage >=0.9)
                {
                    toolbar.setVisibility(View.VISIBLE);
                }
                else {
                    toolbar.setVisibility(View.GONE);
                }
            }
        });

        header_relative_layout=(RelativeLayout)v.findViewById(R.id.header_relative_layout);
        horizontal_recyler = (RecyclerView)v.findViewById(R.id.horizontal_recyler);
        mCenterIndicator = (TextView)v.findViewById(R.id.centerIndicator);
        vertical_list = (RecyclerView)v.findViewById(R.id.vertical_list);
        item_list=(RecyclerView)v.findViewById(R.id.item_list);
        helper = ((Home)context).helper;

        swipe_verticle=(SwipeRefreshLayout)v.findViewById(R.id.swipe_verticle);

        /*-------------- Refresh data on swap ----------*/
        swipe_verticle.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(ConnectionDetector.isInternetAvailable(context))
                {
                    ((Home)context).fatchData.callGetArticleService();
                    ((Home)context).fatchData.getSectionsFromServer();
                }
                else
                {
                    swipe_verticle.setRefreshing(false);
                }
            }
        });
        setData();
    }

    /*-------------- set section data called from Home --------------*/
    public void setData()
    {
        if(helper!=null)
        {
            section_data.clear();
            section_data.addAll(helper.getAllSections());
            Collections.sort(section_data, new Comparator<SectionModel>(){
                public int compare(SectionModel obj1, SectionModel obj2) {
                     return Integer.valueOf(obj1.getOrder_no()).compareTo(obj2.getOrder_no());
                }
            });
            setHorizontalSection();
            setVerticalSection();
            setItemList();

            if(section_data.size()<6)
            {
                bottom_arrow.setVisibility(View.GONE);
            }
            else {
                bottom_arrow.setVisibility(View.VISIBLE);
            }
        }

        if(swipe_verticle.isRefreshing())
        {
            swipe_verticle.setRefreshing(false);
        }
    }

    private List<ContentListingModel> parseListData(List<ContentListingModel> dataBaseList)
    {
        return dataBaseList;
    }

    /*-------------- set section horizontally --------------*/
    private void setHorizontalSection()
    {
        if(horizontalSectionAdaapter == null)
        {
            lm = new LinearLayoutManager(context);
            lm.setOrientation(LinearLayoutManager.HORIZONTAL);
            horizontal_recyler.setLayoutManager(lm);
            horizontalSectionAdaapter = new HorizontalSectionAdaapter(context,section_data);
            horizontal_recyler.setAdapter(horizontalSectionAdaapter);
            horizontalSectionAdaapter.setOnItemClick(new HorizontalSectionAdaapter.OnItemClick() {
                @Override
                public void getSection(int position, String sectionId) {
                    List<CategoryModel> comeCategory = helper.getAllCategory(sectionId);
                    if(comeCategory.size() > 0)
                    {
                         Constants.sectionId=sectionId;
                        ((Home)context).setShowCurrentPagerItem(HomePagerAdapter.categoryFragment);
                    }
                    else
                    {
                        Alert.showToast(context,getString(R.string.category_are_not_available_to_this_section));
                    }
                }
            });
        }
        else
        {
            horizontal_recyler.getRecycledViewPool().clear();
            horizontalSectionAdaapter.notifyDataSetChanged();
        }
    }

    /*-------------- set section vertically --------------*/
    private void setVerticalSection()
    {
        if(verticalSectionAdapter == null)
        {
            verticalSectionAdapter = new VerticalSectionAdapter(context,section_data);
            vertical_list.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
            vertical_list.setAdapter(verticalSectionAdapter);

            vertical_list.setHasFixedSize(true);
            vertical_list.setItemViewCacheSize(20);
            vertical_list.setDrawingCacheEnabled(true);
            vertical_list.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

            verticalSectionAdapter.setOnItemClick(new VerticalSectionAdapter.OnItemClick()
            {
                @Override
                public void getSection(int position, String sectionId)
                {

                    new Controller().trackEvent("Section","click",section_data.get(position).getSectionName(),
                            section_data.get(position).get_id());

                     List<CategoryModel> comeCategory = helper.getAllCategory(sectionId);
                     if(comeCategory.size() > 0)
                     {
                          Constants.sectionId=sectionId;
                         ((Home)context).setShowCurrentPagerItem(HomePagerAdapter.categoryFragment);
                          i=0;
                     }
                     else
                     {
                         if (i==0)
                         {
                             Alert.showToast(context,getString(R.string.category_are_not_available_to_this_section));
                             i++;
                         }
                     }
                }
            });
        }
        else
        {
            vertical_list.getRecycledViewPool().clear();
            verticalSectionAdapter.notifyDataSetChanged();

           /* section_data.clear();
            verticalSectionAdapter.notifyDataSetChanged();
            section_data.addAll(helper.getAllSections());
            verticalSectionAdapter.notifyItemRangeInserted(0, section_data.size());*/
           // notify adapter of new data
          /*  for (int i = 0; i < section_data.size(); i++) {
                verticalSectionAdapter.notifyItemChanged(i, section_data.get(i));
            }*/
        }

        if(section_data!=null && section_data.size()>0)
        {
            int dp12 = getResources().getDimensionPixelSize(R.dimen.dp12);
            int dp10 = getResources().getDimensionPixelSize(R.dimen.dp10);
            int dp5 = getResources().getDimensionPixelSize(R.dimen.dp5);
            if(section_data.get(0).getSection_lay_type().equalsIgnoreCase("Section Template Two"))
            {
                vertical_list.setBackgroundResource(R.drawable.solid_white_button);
                RelativeLayout.LayoutParams fabIconNew_starParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                fabIconNew_starParams.setMargins(dp5, dp5, dp5, dp12);
                vertical_list.setLayoutParams(fabIconNew_starParams);
            }
            else
            {
                RelativeLayout.LayoutParams fabIconNew_starParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                fabIconNew_starParams.setMargins(0, 0, 0, dp10);
                vertical_list.setLayoutParams(fabIconNew_starParams);
                vertical_list.setBackgroundResource(R.color.gray_back);
            }
        }

    }

    /*-------------- set Article data --------------*/
    private void setItemList()
    {
        if(contentListingAdapter == null)
        {
            List<ContentListingModel> dataBaseList = helper.getAllPost();

            Alert.showLog(TAG,"dataBaseList Come FromServer-- "+dataBaseList.size());

            content_data.clear();
            content_data.addAll(parseListData(dataBaseList));

            contentListingAdapter=new NewContentListingAdapter(context,content_data,helper);
            item_list.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
            item_list.setAdapter(contentListingAdapter);
            contentListingAdapter.setOnClickListener(new NewContentListingAdapter.OnClickListener() {
                @Override
                public void OnClick(int position, View view, ContentListingModel data) {
                    startActivityForResult(new Intent(context, ContentView.class).putExtra("DATA",data),CONTENT_VIEW);
                }
            });
        }
        else
        {
            List<ContentListingModel> dataBaseList = helper.getAllPost();
            content_data.clear();
            contentListingAdapter.notifyDataSetChanged();
            content_data.addAll(parseListData(dataBaseList));
            contentListingAdapter.notifyItemRangeInserted(0, content_data.size());// notify adapter of new data
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case CONTENT_VIEW:
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
                            ((Home)context).setShowCurrentPagerItem(HomePagerAdapter.contribution);
                            break;
                        case ContentView.ACCOUNT:
                           // ((Home)context).setShowCurrentPagerItem(HomePagerAdapter.myAccountFragment);
                            ((Home)context).expendBottomSheet();
                            break;
                        case ContentView.DOWNLOAD:
                            ((Home)context). openAccountWithScroll(1);
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

    private void notifyItems(List<ContentListingModel> element){
        for (int i = 0; i < element.size(); i++) {
            contentListingAdapter.notifyItemChanged(i, element.get(i));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.left_arrow:
                if(lm!=null)
                {
                    if(lm.findFirstCompletelyVisibleItemPosition()>0)
                    {
                        horizontal_recyler.smoothScrollToPosition(lm.findFirstCompletelyVisibleItemPosition()-1);
                    }
                }
                break;

            case R.id.right_arrow:
                if(lm!=null)
                {
                    if(lm.findFirstCompletelyVisibleItemPosition()< content_data.size())
                    {
                        horizontal_recyler.smoothScrollToPosition(lm.findLastVisibleItemPosition()+1);
                    }
                }
                break;
            case R.id.bottom_arrow:
                setSectionListDataFormBottoArrow();
                break;
        }
    }

    private void setSectionListDataFormBottoArrow()
    {
        if(verticalSectionAdapter!=null)
        {
            if(bottom_arrow.getRotation()==180)
            {
                verticalSectionAdapter.showAll(false);
                verticalSectionAdapter.notifyDataSetChanged();
                bottom_arrow.setRotation(0);
            }
            else
            {
                if(section_data.size()>5)
                {
                    verticalSectionAdapter.showAll(true);
                    verticalSectionAdapter.notifyDataSetChanged();
                    bottom_arrow.setRotation(180);
                }
                else
                {
                    verticalSectionAdapter.showAll(false);
                    verticalSectionAdapter.notifyDataSetChanged();
                    bottom_arrow.setRotation(0);
                }
            }
        }
    }

    public void stActivityOnBackPress()
    {
        startActivityForResult(new Intent(context, ContentView.class).putExtra("DATA",Home.LIST),CONTENT_VIEW);
    }
}
