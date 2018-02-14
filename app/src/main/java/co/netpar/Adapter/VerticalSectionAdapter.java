package co.netpar.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import co.netpar.Comman.Constants;
import co.netpar.Model.SectionModel;
import co.netpar.R;
import co.netpar.Syncronization.NetparDataBase.DataBaseStrings;

public class VerticalSectionAdapter extends RecyclerView.Adapter<VerticalSectionAdapter.ReviewHolder> {
    private List<SectionModel> mData;
    private Context context;
    private OnItemClick onItemClick;
    private final int Section_Template_One=0;
    private final int Section_Template_Two=1;
    private boolean showAll=false;

    public VerticalSectionAdapter(Context context,List<SectionModel> data) {
        mData = data;
        this.context=context;
        showAll=false;
    }

    /*-------------- Set layout as per template ------------------*/
    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v ;
        switch (viewType)
        {
            case Section_Template_One:
                v=LayoutInflater.from(context).inflate(R.layout.item_vertiz_sec,parent,false);
                break;
            case Section_Template_Two:
                v=LayoutInflater.from(context).inflate(R.layout.c_tv_3,parent,false);
                break;
            default:
                v=LayoutInflater.from(context).inflate(R.layout.item_vertiz_sec,parent,false);
                break;
        }
        return new ReviewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = Section_Template_One;
        if(mData.get(0).getSection_lay_type().equalsIgnoreCase("Section Template Two"))
        {
            viewType = Section_Template_Two;
        }
        return viewType;
    }

    /*-------------- Set data in to components ------------------*/
    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        holder.tvName.setText(mData.get(position).getSectionName());
        switch (holder.getItemViewType())
        {
            case Section_Template_One:
                Constants.setLayoutColor(position,holder.card_view);
                if(!mData.get(position).getThumbnailImage().equalsIgnoreCase(DataBaseStrings.DEFAULT_VALUE))
                {
                    Uri uriOne= Uri.parse(mData.get(position).getThumbnailImage());
                    holder.categ_image.setImageURI(uriOne);
                }
                else
                {
                    Uri uriOne = Uri.parse("res:///" + R.drawable.catag1_top_transparent);
                    holder.categ_image.setImageURI(uriOne);
                }
                break;
            case Section_Template_Two:
                if(!mData.get(position).getHorigontalImage().equalsIgnoreCase(DataBaseStrings.DEFAULT_VALUE))
                {
                    Uri uriOne= Uri.parse(mData.get(position).getHorigontalImage());
                    holder.categ_image.setImageURI(uriOne);
                }
                else
                {
                    Uri uriOne = Uri.parse("res:///" + R.drawable.catag1_top_transparent);
                    holder.categ_image.setImageURI(uriOne);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        if(!showAll)
        {
            if(mData.size()>4)
            {
                return 5;
            }
            else
            {
                return mData.size();
            }
        }
        else
        {
            return mData.size();
        }
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {
        protected TextView tvName;
        protected SimpleDraweeView categ_image;
        protected LinearLayout color_lay;
        protected CardView card_view;
        public ReviewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.text);
            categ_image = (SimpleDraweeView) itemView.findViewById(R.id.categ_image);
            color_lay=(LinearLayout)itemView.findViewById(R.id.color_lay);
            card_view=(CardView)itemView.findViewById(R.id.card_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        onItemClick.getSection(getAdapterPosition(),mData.get(getAdapterPosition()).get_id());
                    }
                    catch (Exception e)
                    {e.printStackTrace();}
                }
            });
        }
    }

    public void showAll(boolean showAll)
    {
        this.showAll=showAll;
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
