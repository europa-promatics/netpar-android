package co.netpar.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.reverie.manager.RevSDK;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import co.netpar.Comman.Alert;
import co.netpar.Comman.Constants;
import co.netpar.Fragment.Contribution;
import co.netpar.Model.MediaModel;
import co.netpar.R;
import co.netpar.Retrofit.Retrofit2;
import co.netpar.Retrofit.ServiceResponse;
import co.netpar.Syncronization.NetparDataBase.DataBaseStrings;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by promatics on 10/31/2017.
 */
public class ContributionMediaAdapter extends RecyclerView.Adapter<ContributionMediaAdapter.ReviewHolder>{
    private Context context;
    private final String TAG = "ContributionMediaAdapter";
    List<MediaModel> content_data;
    private static final int text = 0, image = 1, audio = 2, video = 3,DELETE_MEDIA=0;

    public ContributionMediaAdapter(Context context, List<MediaModel> content_data) {
        this.content_data = content_data;
        this.context = context;
    }

    @Override
    public ContributionMediaAdapter.ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.media_adapter, parent, false);
        return new ContributionMediaAdapter.ReviewHolder(v);
    }

    /*----------- set data in components ----------------------*/
    @Override
    public void onBindViewHolder(final ContributionMediaAdapter.ReviewHolder holder, final int position) {
                try
                {
                    switch (content_data.get(position).getMediaType())
                    {
                        case Contribution.SELECT_VIDEO:
                            holder.thumbnel_image.setImageURI(Uri.fromFile(new File(content_data.get(position).getPathindevice())));
                            break;
                        case Contribution.PICK_PHOTO_FOR_NETPAR:
                            holder.thumbnel_image.setImageURI(Uri.fromFile(new File(content_data.get(position).getPathindevice())));
                            break;
                        case Contribution.AUDIO_GALLERY_REQUEST_CODE:
                            Uri uri = new Uri.Builder()
                                    .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                                    .path(String.valueOf(R.drawable.audio))
                                    .build();
                            holder.thumbnel_image.setImageURI(uri);
                            break;
                        case Contribution.REQUEST_CODE_DOC:
                            Uri doc_uri = new Uri.Builder()
                                    .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                                    .path(String.valueOf(R.drawable.file))
                                    .build();
                            holder.thumbnel_image.setImageURI(doc_uri);
                            break;
                    }

                    if(content_data.get(position).getServer_path().equalsIgnoreCase(DataBaseStrings.DEFAULT_VALUE))
                    {
                        holder.load_lay.setVisibility(View.VISIBLE);
                        holder.delete_icon.setVisibility(View.GONE);
                        holder.play_button_cl.setVisibility(View.GONE);
                    }
                    else
                    {
                        if(content_data.get(position).getMediaType()==Contribution.SELECT_VIDEO)
                        {
                            holder.play_button_cl.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            holder.play_button_cl.setVisibility(View.GONE);
                        }
                        holder.load_lay.setVisibility(View.GONE);
                        holder.delete_icon.setVisibility(View.VISIBLE);
                    }


                    if(!Contribution.showDelete)
                    {
                        holder.delete_icon.setVisibility(View.GONE);
                    }
                    else
                    {
                        holder.delete_icon.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
    }

    @Override
    public int getItemCount() {
        return content_data.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder implements ServiceResponse{

        private SimpleExoPlayerView video_view;
        private TextView title;
        private ImageView delete_icon,play_button_cl;
        private SimpleDraweeView thumbnel_image;
        private RelativeLayout load_lay;
        private ProgressBar progressBar;
        private ServiceResponse res;
        public ReviewHolder(View itemView) {
            super(itemView);
            try {
                res=this;
               // video_view = (SimpleExoPlayerView) itemView.findViewById(R.id.videoView);
                play_button_cl = (ImageView) itemView.findViewById(R.id.play_button_cl);
                delete_icon=(ImageView)itemView.findViewById(R.id.delete_icon);
                load_lay=(RelativeLayout)itemView.findViewById(R.id.load_lay);
                progressBar=(ProgressBar)itemView.findViewById(R.id.progressBar);

                thumbnel_image=(SimpleDraweeView)itemView.findViewById(R.id.thumbnel_image);
                delete_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                        View dialogView = ((Activity)context).getLayoutInflater().inflate(R.layout.del_download, null);
                        CardView yes_card_view = (CardView) dialogView.findViewById(R.id.yes_card_view);
                        CardView no = (CardView) dialogView.findViewById(R.id.no);
                        TextView title=(TextView)dialogView.findViewById(R.id.title);
                        String msg="";
                        switch (content_data.get(getAdapterPosition()).getMediaType())
                        {
                            case Contribution.PICK_PHOTO_FOR_NETPAR:
                                msg=context.getString(R.string.del_img);
                                break;
                            case Contribution.SELECT_VIDEO:
                                msg=context.getString(R.string.del_vdo);
                                break;
                            case Contribution.AUDIO_GALLERY_REQUEST_CODE:
                                msg=context.getString(R.string.del_audio);
                                break;
                            case Contribution.REQUEST_CODE_DOC:
                                msg=context.getString(R.string.del_document);
                                break;
                        }
                        title.setText(msg);
                        yes_card_view.setBackgroundResource(R.drawable.gradient_yello_radious);
                        no.setBackgroundResource(R.drawable.gradient_black_radious);
                        dialogBuilder.setView(dialogView);
                        final AlertDialog alertDialog = dialogBuilder.create();
                        alertDialog.getWindow().setLayout((int) (((double)context.getResources().getDisplayMetrics().widthPixels) * 0.9d), -2);
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        yes_card_view.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                alertDialog.dismiss();
                                try
                                {
                                    if(!content_data.get(getAdapterPosition()).getServer_path().equalsIgnoreCase(DataBaseStrings.DEFAULT_VALUE))
                                    {
                                        JSONObject peram =new JSONObject();
                                        peram.put("fileurl",content_data.get(getAdapterPosition()).getServer_path());
                                        new Retrofit2(context,res,DELETE_MEDIA, Constants.DELETE_MEDIA,peram).callService(false);
                                    }
                                }
                                catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                content_data.remove(getAdapterPosition());
                                notifyDataSetChanged();
                            }
                        });
                        no.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    }
                });
                load_lay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        content_data.remove(getAdapterPosition());
                        notifyDataSetChanged();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceResponse(int i, Response<ResponseBody> response) {
            try {
                Alert.showLog("RESPONSE- ","RESPONSE ON DEl-- "+response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
