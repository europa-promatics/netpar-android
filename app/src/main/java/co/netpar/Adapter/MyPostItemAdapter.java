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

import java.util.List;

import co.netpar.Comman.Alert;
import co.netpar.Model.ContentListingModel;
import co.netpar.R;
import co.netpar.Syncronization.NetparDataBase.DatabaseHelper;
/**
 * Created by promatics on 11/3/2017.
 */
public class MyPostItemAdapter extends RecyclerView.Adapter<MyPostItemAdapter.ReviewHolder>
{
    private List<ContentListingModel> content_data;
    private Context context;
    private OnClickListener onClickListener;
    private DatabaseHelper hlpr;
    private final String TAG="MyPostItemAdapter";

    public MyPostItemAdapter(Context context,List<ContentListingModel> content_data){
        this.content_data=content_data;
        this.context=context;
    }

    @Override
    public MyPostItemAdapter.ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
         View v= LayoutInflater.from(context).inflate(R.layout.save_my_post,parent,false);
        return new MyPostItemAdapter.ReviewHolder(v);
    }

    /*-------------- Set Data in to components ----------------*/
    @Override
    public void onBindViewHolder(MyPostItemAdapter.ReviewHolder holder, final int position)
    {
        switch (position)
        {
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
            case 3:
                holder.next_image.setImageResource(R.drawable.arrow4);
                holder.bottom_view.setBackgroundResource(R.drawable.radious_color4);
                break;
            case 4:
                holder.next_image.setImageResource(R.drawable.arrow5);
                holder.bottom_view.setBackgroundResource(R.drawable.radious_color5);
                break;
            case 5:
                holder.next_image.setImageResource(R.drawable.arrow6);
                holder.bottom_view.setBackgroundResource(R.drawable.radious_color6);
                break;
            default:
                switch (position%5)
                {
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
                    case 3:
                        holder.next_image.setImageResource(R.drawable.arrow4);
                        holder.bottom_view.setBackgroundResource(R.drawable.radious_color4);
                        break;
                    case 4:
                        holder.next_image.setImageResource(R.drawable.arrow5);
                        holder.bottom_view.setBackgroundResource(R.drawable.radious_color5);
                        break;
                    case 5:
                        holder.next_image.setImageResource(R.drawable.arrow6);
                        holder.bottom_view.setBackgroundResource(R.drawable.radious_color6);
                        break;
                    default:
                        break;
                }
                break;
        }

        Uri uriOne= Uri.parse(content_data.get(position).getFirstImage());
        holder.content_image.setImageURI(uriOne);
        if(content_data.get(position).getHeadline()!=null)
        {
            holder.title.setText(content_data.get(position).getHeadline());
        }

        if(content_data.get(position).getDescription()!=null)
        {
            holder.detail.setText(content_data.get(position).getDescription());
        }

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alert.showLog("position","position-- "+position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return content_data.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder
    {
        private TextView location,title,detail;
        private ImageView content_image,next_image;
        private View bottom_view;
        private LinearLayout main_lay;
        public ReviewHolder(View itemView)
        {
            super(itemView);
            detail= (TextView) itemView.findViewById(R.id.detail);
            title= (TextView) itemView.findViewById(R.id.title);
            location= (TextView) itemView.findViewById(R.id.location);
            content_image=(ImageView)itemView.findViewById(R.id.content_image);
            next_image=(ImageView)itemView.findViewById(R.id.next_image);
            bottom_view=(View)itemView.findViewById(R.id.bottom_view);
            main_lay=(LinearLayout)itemView.findViewById(R.id.main_lay);
            main_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.OnClick(getAdapterPosition(),v,content_data.get(getAdapterPosition()));
                }
            });
        }
    }

    public void setOnClickListener(OnClickListener onClickListener)
    {
        this.onClickListener=onClickListener;
    }

    public interface OnClickListener
    {
        public void OnClick(int position,View view,ContentListingModel data);
    }
}
