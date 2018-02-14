package co.netpar.Adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import java.util.List;
import co.netpar.Model.ContentListingDetailModel;
import co.netpar.R;
import co.netpar.Syncronization.NetparDataBase.DataBaseStrings;
import co.netpar.Widget.JustifyTextView;

/**
 * Created by promatics on 10/31/2017.
 */
public class ContentViewDynamicFrameAdapter extends  RecyclerView.Adapter<ContentViewDynamicFrameAdapter.ReviewHolder>  {
    private Context context;
    private final String TAG="ContentViewDynamicFrameAdapter";
    List<ContentListingDetailModel> content_data;
    private static final int text=0,image=1,audio=2,video=3;

    private boolean progress_bar = false;

    public ContentViewDynamicFrameAdapter(Context context,List<ContentListingDetailModel> content_data){
        this.content_data=content_data;
        this.context=context;
        progress_bar = false;
    }

    @Override
    public int getItemViewType(int position)
    {
        int viewType = text;
        String tag = content_data.get(position).getTag();
        if(tag.equalsIgnoreCase("text"))
        {
            viewType = text;
        }
        else if(tag.equalsIgnoreCase("image"))
        {
            viewType = image;
        }
        else if(tag.equalsIgnoreCase("audio"))
        {
            //viewType = audio;
            viewType = text;

        }
        else if(tag.equalsIgnoreCase("video"))
        {
           // viewType = video;
            viewType = text;

        }
        return viewType;
    }

    /*----------- set template as per listing view ----------------------*/
    @Override
    public ContentViewDynamicFrameAdapter.ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v;
        switch (viewType)
        {
            case text:
                v= LayoutInflater.from(context).inflate(R.layout.text,parent,false);
                break;
            case image:
                v=LayoutInflater.from(context).inflate(R.layout.image,parent,false);
                break;
            case audio:
                v=LayoutInflater.from(context).inflate(R.layout.audio_player,parent,false);
                break;
            case video:
                v=LayoutInflater.from(context).inflate(R.layout.video,parent,false);
                break;
            default:
                v=LayoutInflater.from(context).inflate(R.layout.text,parent,false);
                break;
        }
        return new ContentViewDynamicFrameAdapter.ReviewHolder(v);
    }

    /*----------- set data in components ----------------------*/
    @Override
    public void onBindViewHolder(final ContentViewDynamicFrameAdapter.ReviewHolder holder, final int position)
    {
        switch (holder.getItemViewType())
        {
            case text:
                try
                {
                    holder.text_view.setText(Html.fromHtml(content_data.get(position).getDescription_text()));
                    if(holder.text_view.getText().toString().equalsIgnoreCase(DataBaseStrings.DEFAULT_VALUE))
                    {
                        holder.text_view.setText("");
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    holder.text_view.setText("");
                }

                break;
            case image:
                Glide.with(context)
                        .load(content_data.get(position).getUrl()).asBitmap()
                        .override(650, 400)
                        .placeholder(R.drawable.detail_placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.image_view);
                break;
            case audio:
                String [] val = content_data.get(position).getUrl().split("/");
                holder.title.setText(val[val.length-1]);
                final MediaPlayer[] mp = {null};
                holder.play_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            mp[0] = new MediaPlayer();
                            mp[0].setDataSource(content_data.get(position).getUrl());
                            mp[0].prepare();

                            holder.play_button.setVisibility(View.GONE);
                            holder. pause_button.setVisibility(View.VISIBLE);
                            mp[0].start();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
                holder.pause_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if( mp[0]!=null)
                        {
                            holder. play_button.setVisibility(View.VISIBLE);
                            holder. pause_button.setVisibility(View.GONE);
                            mp[0].pause();
                        }
                    }
                });

                holder.stop_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if( mp[0]!=null)
                        {
                            holder. play_button.setVisibility(View.VISIBLE);
                            holder.pause_button.setVisibility(View.GONE);
                            mp[0].stop();
                            mp[0].release ();
                        }
                    }
                });

                holder.seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        if( mp[0]!=null)
                        {
                            mp[0].seekTo(i);
                        }
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

                if(mp[0]!=null)
                {
                    mp[0].setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.release ();
                            holder. play_button.setVisibility(View.VISIBLE);
                            holder.pause_button.setVisibility(View.GONE);
                        }
                    });
                }
                break;
            case video:
                try
                {

                    Uri uri= Uri.parse(content_data.get(position).getUrl());
                  //  mediaController.hide();
                   // holder.video_view.setMediaController(mediaController);
                    holder.video_view.setVideoURI(uri);
                  //  holder.video_view.requestFocus();
                    holder.play_button_cl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            holder.video_view.start();
                            if(!progress_bar)
                            {
                                holder.progressbar.setVisibility(View.VISIBLE);
                            }
                            holder.play_button_cl.setVisibility(View.GONE);
                        }
                    });

                    holder.video_view.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if(holder.video_view.isPlaying())
                            {
                                holder.video_view.pause();
                                holder.play_button_cl.setVisibility(View.VISIBLE);
                            }
                            return false;
                        }
                    });

                    holder.video_view.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            holder.play_button_cl.setVisibility(View.VISIBLE);
                        }
                    });

                    holder.video_view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp)
                        {
                            mp.start();
                            mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                                @Override
                                public void onVideoSizeChanged(MediaPlayer mp, int arg1, int arg2) {
                                    // TODO Auto-generated method stub
                                    holder.progressbar.setVisibility(View.GONE);
                                    progress_bar = true;
                                    mp.start();
                                }
                            });
                          //  holder.progressbar.setVisibility(View.GONE);
                         //   holder.video_view.start();
                        }
                    });
                }
                catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return content_data.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {
        private JustifyTextView text_view;
        private ImageView image_view,play_button,pause_button,stop_button,play_button_cl;
        private SeekBar seek_bar;
        private VideoView video_view;
        private ProgressBar progressbar;
        private TextView title;
        public ReviewHolder(View itemView)
        {
            super(itemView);
            try
            {
                text_view= (JustifyTextView) itemView.findViewById(R.id.text_view);
                image_view=(ImageView)itemView.findViewById(R.id.image_view);
                seek_bar =(SeekBar)itemView.findViewById(R.id.seek_bar);
                play_button=(ImageView)itemView.findViewById(R.id.play_button);
                pause_button=(ImageView)itemView.findViewById(R.id.pause_button);
                stop_button=(ImageView)itemView.findViewById(R.id.stop_button);
                video_view=(VideoView)itemView.findViewById(R.id.video_view);
                play_button_cl=(ImageView)itemView.findViewById(R.id.play_button_cl);
                progressbar = (ProgressBar)itemView.findViewById(R.id.progressbar);

                title=(TextView)itemView.findViewById(R.id.title);
                seek_bar.setVisibility(View.GONE);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
