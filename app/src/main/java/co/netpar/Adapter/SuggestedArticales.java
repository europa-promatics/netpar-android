package co.netpar.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import co.netpar.Model.ContentListingModel;
import co.netpar.R;
import co.netpar.Syncronization.NetparDataBase.DatabaseHelper;

/**
 * Created by promatics on 10/9/2017.
 */

public class SuggestedArticales extends  RecyclerView.Adapter<SuggestedArticales.ReviewHolder>  {
    private List<String> content_data;
    private Context context;
    private OnClickListener onClickListener;
    private DatabaseHelper db;
    public SuggestedArticales(Context context, List<String> content_data, DatabaseHelper db){
        this.content_data=content_data;
        this.context=context;
        this.db=db;
    }

    @Override
    public SuggestedArticales.ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v=LayoutInflater.from(context).inflate(R.layout.suggested_articals,parent,false);
        return new SuggestedArticales.ReviewHolder(v);
    }

    /*-------------- Set data in to components ------------------*/
    @Override
    public void onBindViewHolder(SuggestedArticales.ReviewHolder holder, final int position) {
        switch (position)
        {
            case 0:
                holder.next_image.setImageResource(R.drawable.arrow1);
                holder.bottom_view.setBackgroundResource(R.drawable.radious_color1);

                holder.content_image.setImageResource(R.drawable.dummy_e_study);
                break;
            case 1:
                holder.next_image.setImageResource(R.drawable.arrow2);
                holder.bottom_view.setBackgroundResource(R.drawable.radious_color2);

                holder.content_image.setImageResource(R.drawable.dummy_food);
                break;
            case 2:
                holder.next_image.setImageResource(R.drawable.arrow3);
                holder.bottom_view.setBackgroundResource(R.drawable.radious_color3);

                holder.content_image.setImageResource(R.drawable.dummy_education);
                break;
            case 4:
                holder.next_image.setImageResource(R.drawable.arrow4);
                holder.bottom_view.setBackgroundResource(R.drawable.radious_color4);

                holder.content_image.setImageResource(R.drawable.dummy_newss);
                break;
            case 5:
                holder.next_image.setImageResource(R.drawable.arrow5);
                holder.bottom_view.setBackgroundResource(R.drawable.radious_color5);

                holder.content_image.setImageResource(R.drawable.dummy_jobss);
                break;
            case 6:
                holder.next_image.setImageResource(R.drawable.arrow6);
                holder.bottom_view.setBackgroundResource(R.drawable.radious_color6);

                holder.content_image.setImageResource(R.drawable.dummy_football);
                break;
            default:
                switch (position%7)
                {
                    case 0:
                        holder.next_image.setImageResource(R.drawable.arrow1);
                        holder.bottom_view.setBackgroundResource(R.drawable.radious_color1);

                        holder.content_image.setImageResource(R.drawable.dummy_e_study);
                        break;
                    case 1:
                        holder.next_image.setImageResource(R.drawable.arrow2);
                        holder.bottom_view.setBackgroundResource(R.drawable.radious_color2);

                        holder.content_image.setImageResource(R.drawable.dummy_food);
                        break;
                    case 2:
                        holder.next_image.setImageResource(R.drawable.arrow3);
                        holder.bottom_view.setBackgroundResource(R.drawable.radious_color3);

                        holder.content_image.setImageResource(R.drawable.dummy_education);
                        break;
                    case 4:
                        holder.next_image.setImageResource(R.drawable.arrow4);
                        holder.bottom_view.setBackgroundResource(R.drawable.radious_color4);

                        holder.content_image.setImageResource(R.drawable.dummy_newss);
                        break;
                    case 5:
                        holder.next_image.setImageResource(R.drawable.arrow5);
                        holder.bottom_view.setBackgroundResource(R.drawable.radious_color5);

                        holder.content_image.setImageResource(R.drawable.dummy_jobss);
                        break;
                    case 6:
                        holder.next_image.setImageResource(R.drawable.arrow6);
                        holder.bottom_view.setBackgroundResource(R.drawable.radious_color6);

                        holder.content_image.setImageResource(R.drawable.dummy_football);
                        break;
                }
                break;
        }


        if(db!=null)
        {
            try
            {
                final ContentListingModel content =db.getPost(content_data.get(position));
                holder.title.setText(content.getHeadline());
                holder.detail.setText(content.getTagline());
                Uri uriOne= Uri.parse(content.getFirstImage());
                holder.content_image.setImageURI(uriOne);

                holder.main_lay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickListener.OnClick(content.getContentId(),content,position,v);
                    }
                });
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return content_data.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {
        private TextView location,title,detail;
        private ImageView next_image;
        private SimpleDraweeView content_image;
        private View bottom_view;
        private LinearLayout main_lay;
        public ReviewHolder(View itemView) {
            super(itemView);
            detail= (TextView) itemView.findViewById(R.id.detail);
            title= (TextView) itemView.findViewById(R.id.title);
            content_image=(SimpleDraweeView)itemView.findViewById(R.id.content_image);
            next_image=(ImageView)itemView.findViewById(R.id.next_image);
            bottom_view=(View)itemView.findViewById(R.id.bottom_view);
            main_lay=(LinearLayout)itemView.findViewById(R.id.main_lay);
        }
    }

    public void setOnClickListener(OnClickListener onClickListener)
    {
        this.onClickListener=onClickListener;
    }

    public interface OnClickListener
    {
        public void OnClick(String articleId,ContentListingModel content,int position,View view);
    }
}
