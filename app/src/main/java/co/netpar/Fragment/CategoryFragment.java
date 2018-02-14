package co.netpar.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.netpar.Adapter.CategoryAdapter;
import co.netpar.App.Controller;
import co.netpar.Comman.Alert;
import co.netpar.Comman.Constants;
import co.netpar.Home;
import co.netpar.Model.CategoryModel;
import co.netpar.Model.ContentListingModel;
import co.netpar.Model.SubCategoryModel;
import co.netpar.PagerAdapter.HomePagerAdapter;
import co.netpar.R;
import co.netpar.Retrofit.Retrofit2;
import co.netpar.Retrofit.ServiceResponse;
import co.netpar.Syncronization.NetparDataBase.DataBaseStrings;
import co.netpar.Syncronization.NetparDataBase.DatabaseHelper;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by promatics on 10/12/2017.
 */

public class CategoryFragment extends Fragment implements ServiceResponse
{
    private Button logout;
    private Context context;
    public static final String TAG = "CategoryFragment";
    private RecyclerView category_list;
    private CategoryAdapter adapter;
    private int s=100;
    public static CategoryFragment instance;
    public List<CategoryModel> comeCategory = new ArrayList<>();
    private CardView parent_card;
    private DatabaseHelper helper;

    private ServiceResponse res;
    CountDownTimer x;
    private int temp=0;

    public static int positionnn=0;
    public static String sectionIDDDD=null;

    private boolean isVisibleToUser=false;
    public static synchronized CategoryFragment getInstance()
    {
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.category_fragment, container, false);
        instance=this;
        res=this;
        context=container.getContext();
        initializeView(v);
        return v;
    }

    private void initializeView(View v)
    {
        helper=((Home) context).helper;
        category_list=(RecyclerView)v.findViewById(R.id.category_list);
        parent_card=(CardView)v.findViewById(R.id.parent_card);
        setData();
    }

    // TODO: 17/1/18  Google analytics
    private void googleAnalytic(String value,int pos)
    {
        new Controller().trackEvent("Template","click",value,comeCategory.get(pos).getItem_id());
    }

    /*---------------- Set Data also called from Home Activity -----------------*/
    public void setData()
    {
        if(adapter==null)
        {
            comeCategory = ((Home) context).helper.getAllCategory(Constants.sectionId);
            setLogoutManager();
            adapter = new CategoryAdapter(context, comeCategory);
            category_list.setAdapter(adapter);
            adapter.setOnItemClick(new CategoryAdapter.OnItemClick()
            {
                @Override
                public void getSection(int position, String sectionId, String categoryId)
                {
                    if (comeCategory.get(position).getCategoryFormat().equalsIgnoreCase("Not Found"))
                    {
                        googleAnalytic("Default",position);
                    }
                    else
                    {
                        googleAnalytic(comeCategory.get(position).getCategoryFormat(),position);
                    }

                    List<SubCategoryModel> comeSubCategory = ((Home) context).helper.getAllSubCategory(sectionId, categoryId);
                    Log.e("categoryId",""+categoryId);
                    Log.e("comeSubCategory",""+comeSubCategory.size());
                    callClickService(categoryId);

                    /*Used to refresh filter*/
                    positionnn=position;
                    sectionIDDDD=sectionId;

                    if (comeSubCategory.size() > 0)
                    {
                         Constants.categoryId = categoryId;
                         Constants.list_view_type=comeCategory.get(position).getListViewFormat();
                        ((Home) context).setShowCurrentPagerItem(HomePagerAdapter.subCategoryFragment);
                    }
                    else
                    {
                        List<ContentListingModel> list_dt=helper.getAllPost();
                        List<ContentListingModel> array_list = new ArrayList<ContentListingModel>();
                        array_list.clear();
                        for (ContentListingModel dt:list_dt)
                        {
                            Alert.showLog(TAG,"come:-- "+dt.getCategoryId()+" "+"here:-- "+categoryId);
                            if(categoryId.equalsIgnoreCase(dt.getCategoryId()))
                            {
                                array_list.add(dt);
                            }
                        }

                       if(array_list.size()>0)
                       {
                           temp=0;
                           ((Home)context).setShowCurrentPagerItem(HomePagerAdapter.contentListingAfterFilter);
                            ContentListingAfterFilter ob=ContentListingAfterFilter.getInstance();
                            if(ob!=null)
                            {
                               ob.setData(array_list,comeCategory.get(position).getListViewFormat());
                            }
                       }
                       else
                       {
                           if (temp==0)
                           {
                               Alert.showToast(context, getString(R.string.sub_category_are_not_available_to_this_category));
                               temp++;
                           }
                       }
                    }
                }
            });
        }
        else
        {
            comeCategory.clear();
            adapter.notifyDataSetChanged();
            comeCategory.addAll(((Home) context).helper.getAllCategory(Constants.sectionId));
            setLogoutManager();
            adapter.notifyItemRangeInserted(0, comeCategory.size());
        }
    }

    private void setLogoutManager()
    {
        try
        {
            if(comeCategory.get(0).getCategoryFormat().equalsIgnoreCase("Category-view Template One") || comeCategory.get(0).getCategoryFormat().equalsIgnoreCase("Category-view Template Two"))
            {
                category_list.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            }
            else
            {
                category_list.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
            }

        if(comeCategory.get(0).getCategoryFormat().equalsIgnoreCase("Category-view Template Four") || comeCategory.get(0).getCategoryFormat().equalsIgnoreCase(DataBaseStrings.DEFAULT_VALUE))
        {
            LinearLayout.LayoutParams parent_cardParams = new LinearLayout.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);
            parent_cardParams.setMargins(0, 0, 0, 0);
            parent_card.setLayoutParams(parent_cardParams);

            parent_card.setContentPadding(0,0,0,0);
            parent_card.setBackgroundResource(R.color.gray_back);
            parent_card.setRadius((float)0);
        }
        else if(comeCategory.get(0).getCategoryFormat().equalsIgnoreCase("Category-view Template Three"))
        {
                LinearLayout.LayoutParams parent_cardParams = new LinearLayout.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);
                parent_cardParams.setMargins(10,10,10,10);
                parent_card.setLayoutParams(parent_cardParams);

                parent_card.setContentPadding(5,5,5,5);
                parent_card.setBackgroundResource(R.drawable.white_redious_back);
                parent_card.setRadius((float) 3.0);
        }
        else if(comeCategory.get(0).getCategoryFormat().equalsIgnoreCase("Category-view Template One"))
        {
            LinearLayout.LayoutParams parent_cardParams = new LinearLayout.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);
            parent_cardParams.setMargins(10,10,10,10);
            parent_card.setLayoutParams(parent_cardParams);
            parent_card.setContentPadding(5,5,5,5);

            parent_card.setBackgroundResource(R.drawable.white_redious_back);
            parent_card.setRadius((float) 10.0);
        }
        else
        {
            LinearLayout.LayoutParams parent_cardParams = new LinearLayout.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);
            parent_cardParams.setMargins(10,10,10,10);
            parent_card.setLayoutParams(parent_cardParams);
            parent_card.setContentPadding(5,5,5,5);

            parent_card.setBackgroundResource(R.drawable.white_redious_back);
            parent_card.setRadius((float) 10.0);
        }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void callVisibleService(String id)
    {
        try
        {
            JSONObject object=new JSONObject();
            object.put("totalViews","1");
            new Retrofit2(context,res,2,Constants.templatateAnalayticUpdate+"/"+id,object).callService(false);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void callClickService(String id)
    {
        try
        {
            JSONObject object=new JSONObject();
            object.put("totalClick","1");
            new Retrofit2(context,res,2,Constants.templatateAnalayticUpdate+"/"+id,object).callService(false);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser=isVisibleToUser;
        if(isVisibleToUser)
        {
            setData();
        }
        else
        {
           if(x!=null)
           {
              x=null;
           }
        }
    }

    @Override
    public void onServiceResponse(int i, Response<ResponseBody> response) {
        try
        {
            Alert.showLog("RES","RES- callServiceForTimeAnalytics"+response.body().string());
        }
        catch (Exception e)
        {}
    }
}

