package co.netpar.Adapter;


import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;;
import co.netpar.Comman.Alert;
import co.netpar.Comman.Constants;
import co.netpar.Model.ContactsModel;
import co.netpar.R;
import co.netpar.Syncronization.NetparDataBase.SharedPreference;

/**
 * Created by promatics on 11/1/2017.
 */

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ReviewHolder> {
    private List<ContactsModel> mData;
    private Context context;

    public FriendsAdapter(Context context, List<ContactsModel> data) {
        mData = data;
        this.context = context;
    }

    /*---------- set data in to component ------------*/
    @Override
    public FriendsAdapter.ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.friend_card, parent, false);
        return new FriendsAdapter.ReviewHolder(v);
    }

    @Override
    public void onBindViewHolder(FriendsAdapter.ReviewHolder holder, int position)
    {
        holder.name.setText(mData.get(position).getName());
        Uri uriOne = Uri.parse(mData.get(position).getImage());
        holder.friend_image.setImageURI(uriOne);
    }

    @Override
    public int getItemCount() {
        int size=mData.size();
        if(mData.size()>6)
        {
            size=6;
        }
        return size;
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {
        protected TextView name;
        private SimpleDraweeView friend_image;
        public ReviewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            friend_image = (SimpleDraweeView) itemView.findViewById(R.id.friend_image);
        }
    }
}


