package co.netpar.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import co.netpar.Adapter.MyPostItemAdapter;
import co.netpar.Adapter.SavedItemAdapter;
import co.netpar.ContentView;
import co.netpar.Home;
import co.netpar.Model.ContentListingModel;
import co.netpar.R;
import co.netpar.Syncronization.NetparDataBase.DatabaseHelper;

/**
 * Created by promatics on 11/24/2017.
 */

public class SavedItems extends Fragment implements View.OnClickListener
{
    private Context context;
    public static SavedItems instance;
    private RelativeLayout save_click,contribute_click;
    private RecyclerView savedData,myPostData;
    private TextView saved_post_txt,contribution_post_txt;
    private View saved_post_view,saved_contribution_view;
    private DatabaseHelper helper;
    private List<ContentListingModel> saved_Data = new ArrayList<>();
    private List<ContentListingModel> my_post = new ArrayList<>();
    private SavedItemAdapter adapter;
    private MyPostItemAdapter post_adapter;

    public static synchronized SavedItems getInstance()
    {
        return instance;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.saved_items, container, false);
        instance=this;
        context=container.getContext();
        helper=((Home)context).helper;
        initializeView(v);
        return v;
    }

    private void initializeView(View v)
    {
        save_click=(RelativeLayout)v.findViewById(R.id.save_click);
        contribute_click=(RelativeLayout)v.findViewById(R.id.contribute_click);

        savedData=(RecyclerView)v.findViewById(R.id.savedData);
        myPostData=(RecyclerView)v.findViewById(R.id.myPostData);

        savedData.setNestedScrollingEnabled(false);
        myPostData.setNestedScrollingEnabled(false);

        saved_post_txt=(TextView)v.findViewById(R.id.saved_post_txt);
        contribution_post_txt=(TextView)v.findViewById(R.id.contribution_post_txt);
        saved_post_view=(View)v.findViewById(R.id.saved_post_view);
        saved_contribution_view=(View)v.findViewById(R.id.saved_contribution_view);

        setSelectedSavedItemLay();

        save_click.setOnClickListener(this);
        contribute_click.setOnClickListener(this);
    }

    /*---------------- Set Seved Item ---------------------------*/
    public void setSelectedSavedItemLay()
    {
        setSaveData();

        saved_post_txt.setTextColor(getResources().getColor(R.color.selectable_tab));
        saved_post_view.setBackgroundResource(R.color.selectable_tab);

        ViewGroup.LayoutParams params = saved_post_view.getLayoutParams();
        params.height =4;
        saved_post_view.setLayoutParams(params);

        contribution_post_txt.setTextColor(getResources().getColor(R.color.unsel_selectable_tab));

        ViewGroup.LayoutParams params1 = saved_contribution_view.getLayoutParams();
        params1.height =1;
        saved_contribution_view.setLayoutParams(params1);

        saved_contribution_view.setBackgroundResource(R.color.unsel_selectable_tab);
        savedData.setVisibility(View.VISIBLE);
        myPostData.setVisibility(View.INVISIBLE);
    }

    private void setSaveData()
    {
        saved_Data.clear();
        saved_Data.addAll(helper.getAllSavePost());
        if(saved_Data.size()>0)
        {
            if(adapter==null)
            {
                savedData.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
                adapter = new SavedItemAdapter(context,saved_Data);
                adapter.setOnClickListener(new SavedItemAdapter.OnClickListener() {
                    @Override
                    public void OnClick(int position, View view, ContentListingModel data) {
                        startActivityForResult(new Intent(context, ContentView.class).putExtra("DATA",data),50);
                    }
                });
                savedData.setAdapter(adapter);
            }
            else
            {
                for (int i = 0; i < saved_Data.size(); i++) {
                    adapter.notifyItemChanged(i, saved_Data.get(i));
                }
            }
        }
    }

    /*---------------- Set Contribution ---------------------------*/
    public void setSelectedContributItemLay()
    {

        ViewGroup.LayoutParams params = saved_post_view.getLayoutParams();
        params.height =1;
        saved_post_view.setLayoutParams(params);

        ViewGroup.LayoutParams params1 = saved_contribution_view.getLayoutParams();
        params1.height =4;
        saved_contribution_view.setLayoutParams(params1);

        contribution_post_txt.setTextColor(getResources().getColor(R.color.selectable_tab));
        saved_contribution_view.setBackgroundResource(R.color.selectable_tab);
        saved_post_txt.setTextColor(getResources().getColor(R.color.unsel_selectable_tab));
        saved_post_view.setBackgroundResource(R.color.unsel_selectable_tab);
        savedData.setVisibility(View.INVISIBLE);
        myPostData.setVisibility(View.VISIBLE);

        my_post.clear();
        my_post.addAll(helper.getMyPost());
        if(post_adapter==null)
        {
            post_adapter=new MyPostItemAdapter(context,my_post);
            myPostData.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
            post_adapter.setOnClickListener(new MyPostItemAdapter.OnClickListener() {
                @Override
                public void OnClick(int position, View view, ContentListingModel data) {
                    //startActivityForResult(new Intent(context, ContentView.class).putExtra("DATA",data),50);
                }
            });
            myPostData.setAdapter(post_adapter);
        }
        else
        {
            post_adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser)
        {
            //setFriendData();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.save_click:
                setSelectedSavedItemLay();
                break;
            case R.id.contribute_click:
                setSelectedContributItemLay();
                break;
        }
    }
}



