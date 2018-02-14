package co.netpar.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import co.netpar.Comman.Constants;
import co.netpar.ContentView;
import co.netpar.Model.CommentListModel;
import co.netpar.R;

/**
 * Created by promatics on 11/1/2017.
 */

public class AllCommentAdapter extends RecyclerView.Adapter<AllCommentAdapter.ReviewHolder> {
    private List<CommentListModel> mData;
    private Context context;

    private int val=1;

    public AllCommentAdapter(Context context, List<CommentListModel> data) {
        mData = data;
        this.context = context;
    }

    @Override
    public AllCommentAdapter.ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.comment_card, parent, false);
        return new AllCommentAdapter.ReviewHolder(v);
    }

    @Override
    public void onBindViewHolder(AllCommentAdapter.ReviewHolder holder, int position)
    {
        try
        {
        holder.name.setText(mData.get(position).getUserName());
        holder.comment.setText(mData.get(position).getUserComment());

        Glide.with(context)
                .load(mData.get(position).getUser_image()).asBitmap()
                .placeholder(R.drawable.detail_placeholder)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.profile_image);

            String [] time= mData.get(position).getDateOfComment().split("T");
            holder.time.setText(Constants.changeDateFormat(context,time[0]));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return val;
    }

    public void setReturnType()
    {
        this.val=mData.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {
        protected TextView name, comment, time;
        protected ImageView profile_image;
        public ReviewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            comment = (TextView) itemView.findViewById(R.id.comment);
            time = (TextView) itemView.findViewById(R.id.time);
            profile_image = (ImageView) itemView.findViewById(R.id.profile_image);
        }
    }
}

