package co.netpar.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import co.netpar.Comman.Alert;
import co.netpar.Comman.Constants;
import co.netpar.Fragment.HomeFragment;
import co.netpar.Model.SectionModel;
import co.netpar.R;
import co.netpar.Syncronization.NetparDataBase.DataBaseStrings;

/**
 * Created by promatics on 10/9/2017.
 */

/*---------------- Adapter used to Horizontal Section ----------------*/
public class HorizontalSectionAdaapter extends  RecyclerView.Adapter<HorizontalSectionAdaapter.ReviewHolder>  {
    List<SectionModel> mData;
    private OnItemClick onItemClick;

    public static int last_position=0;
    private Context context;
    public HorizontalSectionAdaapter(Context context,List<SectionModel> data){
        mData=data;
        this.context=context;
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v= LayoutInflater.from(context).inflate(R.layout.item_horz_sec,parent,false);
        return new ReviewHolder(v);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        last_position=position;

        Uri uriOne= Uri.parse(mData.get(position).getThumbnailImage());
        holder.categ_image.setImageURI(uriOne);

        holder.tvName.setText(mData.get(position).getSectionName());
        holder.tvName.setGravity(Gravity.CENTER);
        Constants.setLayoutColorWithRadios(position,holder.card_view);
        }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {
        protected TextView tvName;
        protected SimpleDraweeView categ_image;
        protected CardView card_view;
        View container;
        public ReviewHolder(View itemView) {
            super(itemView);
            container=itemView;
            tvName= (TextView) itemView.findViewById(R.id.text);
            categ_image=(SimpleDraweeView)itemView.findViewById(R.id.categ_image);
            card_view=(CardView)itemView.findViewById(R.id.card_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick.getSection(getAdapterPosition(),mData.get(getAdapterPosition()).get_id());
                }
            });
        }
    }

    public void setOnItemClick(OnItemClick onItemClick)
    {
        this.onItemClick=onItemClick;
    }
    public interface OnItemClick
    {
        public void getSection(int position,String sectionId);
    }

}
