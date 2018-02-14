package co.netpar.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import co.netpar.Comman.Alert;
import co.netpar.Comman.Constants;
import co.netpar.Model.SubCategoryModel;
import co.netpar.R;

/**
 * Created by promatics on 10/13/2017.
 */

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ReviewHolder> {
    private List<SubCategoryModel> mData;
    private Context context;
    private SubCategoryAdapter.OnItemClick onItemClick;
    private final int SubCategory_view_Template_One=0;
    private final int SubCategory_view_Template_Two=1;
    private final int SubCategory_view_Template_Three=2;
    private final int SubCategory_view_Template_Four=3;

    public SubCategoryAdapter(Context context,List<SubCategoryModel> data) {
        mData = data;
        this.context=context;
    }

    @Override
    public int getItemViewType(int position)
    {
        int viewType = SubCategory_view_Template_Four;
        if(mData.get(0).getSubCategoryFormat().equalsIgnoreCase("SubCategory-view Template One"))
        {
            viewType = SubCategory_view_Template_One;
        }
        else if(mData.get(0).getSubCategoryFormat().equalsIgnoreCase("SubCategory-view Template Two"))
        {
            viewType = SubCategory_view_Template_Two;
        }
        else if(mData.get(0).getSubCategoryFormat().equalsIgnoreCase("SubCategory-view Template Three"))
        {
            viewType = SubCategory_view_Template_Three;
        }
        return viewType;
    }

    /*-------------- Set layout according to view ------------------*/
    @Override
    public SubCategoryAdapter.ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v ;
        switch (viewType)
        {
            case SubCategory_view_Template_One:
                v=LayoutInflater.from(context).inflate(R.layout.c_tv_1,parent,false);
                break;
            case SubCategory_view_Template_Two:
                v=LayoutInflater.from(context).inflate(R.layout.c_tv_2,parent,false);
                break;
            case SubCategory_view_Template_Three:
                v=LayoutInflater.from(context).inflate(R.layout.c_tv_3,parent,false);
                break;
            case SubCategory_view_Template_Four:
                v=LayoutInflater.from(context).inflate(R.layout.c_tv_4,parent,false);
                break;
            default:
                v=LayoutInflater.from(context).inflate(R.layout.c_tv_4,parent,false);
                break;
        }
        return new SubCategoryAdapter.ReviewHolder(v);
    }

    /*-------------- Set data in to components ------------------*/
    @Override
    public void onBindViewHolder(SubCategoryAdapter.ReviewHolder holder, int position) {
        holder.tvName.setText(mData.get(position).getSub_category_Name());
        switch (holder.getItemViewType())
        {
            case SubCategory_view_Template_One:
                Uri uriOne= Uri.parse(mData.get(position).getSub_category_thumbnaillImage());
                holder.categ_image.setImageURI(uriOne);
                break;
            case SubCategory_view_Template_Two:
                Uri uriTwo= Uri.parse(mData.get(position).getSub_category_thumbnaillImage());
                holder.categ_image.setImageURI(uriTwo);
                break;
            case SubCategory_view_Template_Three:
                Uri uriThree= Uri.parse(mData.get(position).getSub_category_horigontalImage());
                holder.categ_image.setImageURI(uriThree);
                break;
            case SubCategory_view_Template_Four:
                Constants.setLayoutColor(position,holder.card_view);
               /* Uri uriFour= Uri.parse(mData.get(position).getSub_category_thumbnaillImage());
                holder.categ_image.setImageURI(uriFour);*/
                holder.categ_image.setVisibility(View.GONE);
                break;
        }

    }

    @Override
    public int getItemCount() {
            return mData.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {
        protected TextView tvName;
        protected SimpleDraweeView categ_image;
        protected CardView card_view;
        public ReviewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.text);
            categ_image = (SimpleDraweeView) itemView.findViewById(R.id.categ_image);
            card_view=(CardView)itemView.findViewById(R.id.card_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick.getSection(getAdapterPosition(),mData.get(getAdapterPosition()).getSub_category_sectionId()
                            ,mData.get(getAdapterPosition()).getSub_category_categoryId(),mData.get(getAdapterPosition()).getSub_category_id());
                }
            });
        }
    }

    public void setOnItemClick(SubCategoryAdapter.OnItemClick onItemClick)
    {
        this.onItemClick=onItemClick;
    }

    public interface OnItemClick
    {
        public void getSection(int position,String sectionId,String categoryId,String subCategoryId);
    }}

