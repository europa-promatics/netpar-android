package co.netpar.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import co.netpar.Adapter.NotificationAdapter;
import co.netpar.Home;
import co.netpar.Model.NotificationModel;
import co.netpar.R;
import co.netpar.Syncronization.NetparDataBase.DatabaseHelper;

/**
 * Created by promatics on 11/28/2017.
 */

public class Notifications extends Fragment
{
    private Context context;
    public static Notifications instance;
    private RecyclerView notification_list;
    private List<NotificationModel> not_data = new ArrayList<>();
    private CardView no_notif;
    private NotificationAdapter adapter;
    private DatabaseHelper helper;

    public static synchronized Notifications getInstance()
    {
        return instance;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.notification_fragment, container, false);
        instance=this;
        context=container.getContext();
        helper=((Home)context).helper;
        initializeView(v);
        return v;
    }

    private void initializeView(View v)
    {
        notification_list=(RecyclerView)v.findViewById(R.id.notification_list);
        no_notif=(CardView)v.findViewById(R.id.no_notif);
        notification_list.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        adapter=new NotificationAdapter(context,not_data);
        notification_list.setAdapter(adapter);
        notification_list.setNestedScrollingEnabled(false);
    }

    /*---------------- Set Notification Data ---------------------------*/
    public void setNotificationData()
    {
        not_data.clear();
        not_data.addAll(helper.getAllNotification());
        if(not_data.size()>0)
        {
            adapter.notifyDataSetChanged();
            no_notif.setVisibility(View.GONE);
            notification_list.setVisibility(View.VISIBLE);
        }
        else
        {
            no_notif.setVisibility(View.VISIBLE);
            notification_list.setVisibility(View.GONE);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser)
        {
            setNotificationData();
        }
    }
}




