package co.netpar.PagerAdapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;
import co.netpar.Model.DownloadedData;
import co.netpar.R;

/**
 * Created by promatics on 11/29/2017.
 * Used only to show gallery
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ReviewHolder> {
    private List<DownloadedData> mData;
    private Context context;
    private OnItemClickListener onDeleteListener;

    public GalleryAdapter(Context context,List<DownloadedData> data) {
        mData = data;
        this.context=context;
    }

    @Override
    public GalleryAdapter.ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v= LayoutInflater.from(context).inflate(R.layout.gallery_adapter,parent,false);
        return new GalleryAdapter.ReviewHolder(v);
    }

    @Override
    public void onBindViewHolder(final GalleryAdapter.ReviewHolder holder, final int position) {

        if (mData.get(position).getPOST_TAG().equalsIgnoreCase("audio")) {
            Uri audio_uri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                    .path(String.valueOf(R.drawable.audio_place))
                    .build();
            holder.content_image.setImageURI(audio_uri);
            holder.play_button_cl.setVisibility(View.GONE);
        }
        else if (mData.get(position).getPOST_TAG().equalsIgnoreCase("video"))
        {
            Uri uriOne= Uri.parse(mData.get(position).getDATA_PATH_URL());
            holder.content_image.setImageURI(uriOne);
            holder.play_button_cl.setVisibility(View.VISIBLE);
        }
        else if (mData.get(position).getPOST_TAG().equalsIgnoreCase("doc")) {
            Uri doc_uri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                    .path(String.valueOf(R.drawable.file))
                    .build();
            holder.content_image.setImageURI(doc_uri);
            holder.play_button_cl.setVisibility(View.GONE);
        }
        else
        {
            Uri uriOne= Uri.parse(mData.get(position).getDATA_PATH_URL());
            holder.content_image.setImageURI(uriOne);
            holder.play_button_cl.setVisibility(View.GONE);
        }


        holder.del_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteListener.onDelete(position,mData.get(position));
            }
        });

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteListener.onViewDetail(position,mData.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {
        protected ImageView del_download,play_button_cl;
        protected SimpleDraweeView content_image;
        protected RelativeLayout item;
        public ReviewHolder(View itemView) {
            super(itemView);
            del_download = (ImageView) itemView.findViewById(R.id.del_download);
            content_image = (SimpleDraweeView) itemView.findViewById(R.id.content_image);
            play_button_cl=(ImageView)itemView.findViewById(R.id.play_button_cl);
            item=(RelativeLayout)itemView.findViewById(R.id.item);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onDeleteListener)
    {
        this.onDeleteListener=onDeleteListener;
    }

    public interface OnItemClickListener
    {
        public void onViewDetail(int position,DownloadedData data);
        public void onDelete(int position,DownloadedData data);
    }
}
