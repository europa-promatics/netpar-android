package co.netpar.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import co.netpar.Adapter.FriendDetailAdapter;
import co.netpar.Adapter.SubCategoryAdapter;
import co.netpar.Comman.Alert;
import co.netpar.Home;
import co.netpar.Model.ContactsModel;
import co.netpar.R;
import co.netpar.Syncronization.NetparDataBase.DatabaseHelper;

/**
 * Created by promatics on 11/10/2017.
 */

public class ContactListFragment extends Fragment implements View.OnClickListener
{

    private Context context;
    private SubCategoryAdapter adapter;
    public static ContactListFragment instance;
    private LinearLayout header;
    private RelativeLayout netpar_friend,invite_friend;
    private RecyclerView friends_list;
    private TextView netpar_friend_txt,invite_friend_txt;
    private View netpar_friend_view,invite_friend_view;
    private DatabaseHelper helper;
    private List<ContactsModel> contact = new ArrayList<>();
    private FriendDetailAdapter friend_adapter;

    public static synchronized ContactListFragment getInstance()
    {
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.friend_fragment, container, false);
        instance=this;
        context=container.getContext();
        helper=((Home)context).helper;
        initializeView(v);
        return v;
    }

    private void initializeView(View v)
    {
        header = (LinearLayout)v.findViewById(R.id.header);
        netpar_friend = (RelativeLayout)v.findViewById(R.id.netpar_friend);
        invite_friend = (RelativeLayout)v.findViewById(R.id.invite_friend);

        netpar_friend_txt = (TextView) v.findViewById(R.id.netpar_friend_txt);
        invite_friend_txt = (TextView)v.findViewById(R.id.invite_friend_txt);

        netpar_friend_view = (View)v.findViewById(R.id.netpar_friend_view);
        invite_friend_view = (View)v.findViewById(R.id.invite_friend_view);

        friends_list=(RecyclerView)v.findViewById(R.id.friends_list);

        netpar_friend.setOnClickListener(this);
        invite_friend.setOnClickListener(this);

        selectFriendData();
        setFriendData();
    }

    /*------------ set contact list to send invitation ----------*/
    public void selectInviteData()
    {
        ViewGroup.LayoutParams invite_friend_view_params = invite_friend_view.getLayoutParams();
        invite_friend_view_params.height =4;
        invite_friend_view.setLayoutParams(invite_friend_view_params);

        ViewGroup.LayoutParams netpar_friend_view_params = netpar_friend_view.getLayoutParams();
        netpar_friend_view_params.height =1;
        netpar_friend_view.setLayoutParams(netpar_friend_view_params);

        invite_friend_txt.setTextColor(getResources().getColor(R.color.selectable_tab));
        invite_friend_view.setBackgroundResource(R.color.selectable_tab);
        netpar_friend_txt.setTextColor(getResources().getColor(R.color.unsel_selectable_tab));
        netpar_friend_view.setBackgroundResource(R.color.unsel_selectable_tab);
        setInviteFriendData();
    }

    /*------------ set contact appears on netpar ----------*/
    public void selectFriendData()
    {

        ViewGroup.LayoutParams invite_friend_view_params = invite_friend_view.getLayoutParams();
        invite_friend_view_params.height =1;
        invite_friend_view.setLayoutParams(invite_friend_view_params);

        ViewGroup.LayoutParams netpar_friend_view_params = netpar_friend_view.getLayoutParams();
        netpar_friend_view_params.height =4;
        netpar_friend_view.setLayoutParams(netpar_friend_view_params);

        netpar_friend_txt.setTextColor(getResources().getColor(R.color.selectable_tab));
        netpar_friend_view.setBackgroundResource(R.color.selectable_tab);
        invite_friend_txt.setTextColor(getResources().getColor(R.color.unsel_selectable_tab));
        invite_friend_view.setBackgroundResource(R.color.unsel_selectable_tab);
        setFriendData();
    }

    public void setFriendData()
    {
        if(friend_adapter==null)
        {
            contact.clear();
            contact.addAll(helper.getNetparFriendList());
            friends_list.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
            friend_adapter = new FriendDetailAdapter(context,contact);
            friend_adapter.setOnInviteClick(new FriendDetailAdapter.OnInvite() {
                @Override
                public void onClick(ContactsModel data) {
                    Alert.inviteFriendFromNumberDialog(context,data.getNumber());
                }
            });
            friends_list.setAdapter(friend_adapter);
        }
        else
        {
            contact.clear();
            friend_adapter.notifyDataSetChanged();
            contact.addAll(helper.getNetparFriendList());
            friend_adapter.notifyItemRangeInserted(0, contact.size());
        }
    }

    private void setInviteFriendData()
    {
        contact.clear();
        friend_adapter.notifyDataSetChanged();
        contact.addAll(helper.getInviteFriendList());
        friend_adapter.notifyItemRangeInserted(0, contact.size());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser)
        {
            setFriendData();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.invite_friend:
                selectInviteData();
                break;
            case R.id.netpar_friend:
                selectFriendData();
                break;
        }
    }
}


