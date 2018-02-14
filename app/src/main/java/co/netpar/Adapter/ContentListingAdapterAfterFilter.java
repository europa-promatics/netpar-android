package co.netpar.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.List;
import co.netpar.Comman.Alert;
import co.netpar.Fragment.ContentListingAfterFilter;
import co.netpar.Model.ContentListingModel;
import co.netpar.Model.ContentListingModelThree;
import co.netpar.R;
import co.netpar.Syncronization.NetparDataBase.DatabaseHelper;

import static co.netpar.Fragment.ContentListingAfterFilter.Listing_view_Template_Six;
import static co.netpar.Fragment.ContentListingAfterFilter.Listing_view_Template_Three;

/**
 * Created by promatics on 10/9/2017.
 */

public class ContentListingAdapterAfterFilter extends RecyclerView.Adapter<ContentListingAdapterAfterFilter.ReviewHolder> {
    private List<ContentListingModel> content_data;
    private Context context;
    private OnClickListener onClickListener;
    private DatabaseHelper hlpr;
    private final String TAG = "ContentListingAdapterAfterFilter";
    private int viewType;

    public ContentListingAdapterAfterFilter(Context context, List<ContentListingModel> content_data, DatabaseHelper hlpr,int viewType)
    {
        this.content_data = content_data;
        this.context = context;
        this.hlpr = hlpr;
        this.viewType=viewType;
    }

    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    /*----------- set template as per listing view ----------------------*/
    @Override
    public ContentListingAdapterAfterFilter.ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v;
        switch (viewType)
        {
            case ContentListingAfterFilter.Listing_view_Template_One:
                v = LayoutInflater.from(context).inflate(R.layout.listing_view_1_item, parent, false);
                break;
            case ContentListingAfterFilter.Listing_view_Template_Two:
                v = LayoutInflater.from(context).inflate(R.layout.listing_view_2_item, parent, false);
                break;
            case ContentListingAfterFilter.Listing_view_Template_Three:
                v = LayoutInflater.from(context).inflate(R.layout.listing_view_3_item, parent, false);
                break;
            case ContentListingAfterFilter.Listing_view_Template_Five:
                v = LayoutInflater.from(context).inflate(R.layout.listing_view_6_item, parent, false);
                break;
            case ContentListingAfterFilter.Listing_view_Template_Seven:
                v = LayoutInflater.from(context).inflate(R.layout.listing_view_1_item, parent, false);
                break;
            default:
                v = LayoutInflater.from(context).inflate(R.layout.listing_view_1_item, parent, false);
                break;
        }
        return new ContentListingAdapterAfterFilter.ReviewHolder(v);
    }

    /*----------- set data in components ----------------------*/
    @Override
    public void onBindViewHolder(ContentListingAdapterAfterFilter.ReviewHolder holder, final int position) {
        switch (position) {
            case 0:
                holder.next_image.setImageResource(R.drawable.arrow1);
                holder.bottom_view.setBackgroundResource(R.drawable.radious_color1);
                break;
            case 1:
                holder.next_image.setImageResource(R.drawable.arrow2);
                holder.bottom_view.setBackgroundResource(R.drawable.radious_color2);
                break;
            case 2:
                holder.next_image.setImageResource(R.drawable.arrow3);
                holder.bottom_view.setBackgroundResource(R.drawable.radious_color3);
                break;
            default:
                switch (position % 3) {
                    case 0:
                        holder.next_image.setImageResource(R.drawable.arrow1);
                        holder.bottom_view.setBackgroundResource(R.drawable.radious_color1);
                        break;
                    case 1:
                        holder.next_image.setImageResource(R.drawable.arrow2);
                        holder.bottom_view.setBackgroundResource(R.drawable.radious_color2);
                        break;
                    case 2:
                        holder.next_image.setImageResource(R.drawable.arrow3);
                        holder.bottom_view.setBackgroundResource(R.drawable.radious_color3);
                        break;
                }
                break;
        }

            Uri uriOne= Uri.parse(content_data.get(position).getFirstImage());
            holder.content_image.setImageURI(uriOne);

            if (content_data.get(position).getHeadline() != null) {
                holder.title.setText(content_data.get(position).getHeadline());
            }
            if (content_data.get(position).getTagline() != null) {
                holder.detail.setText(content_data.get(position).getTagline());
            }
    }

    @Override
    public int getItemCount() {
        return content_data.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {
        private TextView title, detail;
        private ImageView next_image;
        private View bottom_view;
        private LinearLayout main_lay;
        private SimpleDraweeView content_image;
        public ReviewHolder(View itemView) {
            super(itemView);
            next_image = (ImageView) itemView.findViewById(R.id.next_image);
            detail = (TextView) itemView.findViewById(R.id.detail);
            title = (TextView) itemView.findViewById(R.id.title);
            bottom_view = (View) itemView.findViewById(R.id.bottom_view);
            content_image = (SimpleDraweeView) itemView.findViewById(R.id.content_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        onClickListener.OnClick(getAdapterPosition(), v, content_data.get(getAdapterPosition()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setViewType(int viewType)
    {
        this.viewType=viewType;
    }

    public interface OnClickListener {
        public void OnClick(int position, View view, ContentListingModel data);
    }
}
