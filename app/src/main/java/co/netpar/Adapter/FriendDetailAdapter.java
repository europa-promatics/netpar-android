package co.netpar.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.util.List;
import co.netpar.Comman.Alert;
import co.netpar.Model.ContactsModel;
import co.netpar.R;

/**
 * Created by promatics on 11/10/2017.
 */
public class FriendDetailAdapter extends RecyclerView.Adapter<FriendDetailAdapter.ReviewHolder> {
    private List<ContactsModel> mData;
    private Context context;
    private OnInvite invite_man;

    public FriendDetailAdapter(Context context, List<ContactsModel> data) {
        mData = data;
        this.context = context;
    }

    @Override
    public FriendDetailAdapter.ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.friend_detail_card, parent, false);
        return new FriendDetailAdapter.ReviewHolder(v);
    }

    /*----------- set data in to components ----------------------*/
    @Override
    public void onBindViewHolder(FriendDetailAdapter.ReviewHolder holder, int position)
    {
        holder.name.setText(mData.get(position).getName());
        if(!mData.get(position).getImage().isEmpty())
        {
            Picasso.with(context).load(mData.get(position).getImage()).placeholder(R.drawable.friend_place).into(holder.friend_image);
        }

        if(mData.get(position).getInviteStatus().equalsIgnoreCase(ContactsModel.INVITE))
        {
            holder.invite.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.invite.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {
        protected TextView name;
        protected ImageView friend_image;
        protected TextView invite;
        public ReviewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            friend_image = (ImageView) itemView.findViewById(R.id.friend_image);
            invite=(TextView)itemView.findViewById(R.id.invite);
            invite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    invite_man.onClick(mData.get(getAdapterPosition()));
                }
            });
        }
    }

    public void setOnInviteClick(OnInvite invite_man)
    {
     this.invite_man=invite_man;
    }

    public interface OnInvite{
        public void onClick(ContactsModel data);
    }
}


