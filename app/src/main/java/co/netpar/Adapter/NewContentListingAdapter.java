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
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.List;
import co.netpar.Model.ContentListingModel;
import co.netpar.R;
import co.netpar.Syncronization.NetparDataBase.DatabaseHelper;

/**
 * Created by pro25 on 20/1/18.
 */

public class NewContentListingAdapter extends RecyclerView.Adapter<NewContentListingAdapter.ReviewHolder> {
    private List<ContentListingModel> content_data;
    private Context context;
    private NewContentListingAdapter.OnClickListener onClickListener;
    private DatabaseHelper hlpr;
    private final String TAG = "NewContentListingAdapter";

    public NewContentListingAdapter(Context context, List<ContentListingModel> content_data, DatabaseHelper hlpr) {
        this.content_data = content_data;
        this.context = context;
        this.hlpr = hlpr;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 0;
        switch (position) {
            case 0:
                viewType = 0;
                break;
            case 1:
                viewType = 1;
                break;
            case 2:
                viewType = 0;
                break;
            case 3:
                viewType = 3;
                break;
            default: {
                switch (position % 4)
                {
                    case 0:
                        viewType = 0;
                        break;
                    case 1:
                        viewType = 1;
                        break;
                    case 2:
                        viewType = 0;
                        break;
                    case 3:
                        viewType = 3;
                        break;
                }
                break;
            }
        }
        return viewType;
    }

    @Override
    public NewContentListingAdapter.ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.simple_listing, parent, false);
        return new NewContentListingAdapter.ReviewHolder(v);
    }

    /*---------------- set data in to componants -------------*/
    @Override
    public void onBindViewHolder(NewContentListingAdapter.ReviewHolder holder, final int position) {
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
            if(content_data.get(position).getFirstImage().contains("gif"))
            {
                DraweeController controller =
                        Fresco.newDraweeControllerBuilder()
                                .setUri(content_data.get(position).getFirstImage())
                                .setAutoPlayAnimations(true)
                                .build();
                holder.content_image.setController(controller);
            }
            else
            {
                Uri uriOne= Uri.parse(content_data.get(position).getFirstImage());
                holder.content_image.setImageURI(uriOne);
            }

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
        private TextView  title, detail;
        private ImageView next_image;
        private View bottom_view;
        private LinearLayout main_lay;
        private SimpleDraweeView content_image;
        public ReviewHolder(View itemView) {
            super(itemView);
            detail = (TextView) itemView.findViewById(R.id.detail);
            title = (TextView) itemView.findViewById(R.id.title);
            next_image = (ImageView) itemView.findViewById(R.id.next_image);
            bottom_view = (View) itemView.findViewById(R.id.bottom_view);
            main_lay = (LinearLayout) itemView.findViewById(R.id.main_lay);
            content_image=(SimpleDraweeView)itemView.findViewById(R.id.content_image);
            main_lay.setOnClickListener(new View.OnClickListener() {
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

    public void setOnClickListener(NewContentListingAdapter.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        public void OnClick(int position, View view, ContentListingModel data);
    }
}

