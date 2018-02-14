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

import co.netpar.Comman.Alert;
import co.netpar.Model.ContentListingModel;
import co.netpar.Model.ContentListingModelThree;
import co.netpar.R;
import co.netpar.Syncronization.NetparDataBase.DatabaseHelper;

/**
 * Created by promatics on 10/9/2017.
 */

public class ContentListingAdapter extends RecyclerView.Adapter<ContentListingAdapter.ReviewHolder> {
    private List<ContentListingModel> content_data;
    private Context context;
    private OnClickListener onClickListener;
    private DatabaseHelper hlpr;
    private final String TAG = "ContentListingAdapter";

    public ContentListingAdapter(Context context, List<ContentListingModel> content_data, DatabaseHelper hlpr) {
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

    /*----------- set template as per listing view ----------------------*/
    @Override
    public ContentListingAdapter.ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v;
        switch (viewType) {
            case 0:
                v = LayoutInflater.from(context).inflate(R.layout.content_listing_item_l, parent, false);
                break;
            case 1:
                v = LayoutInflater.from(context).inflate(R.layout.content_listing_item_r, parent, false);
                break;
            case 3:
                v = LayoutInflater.from(context).inflate(R.layout.content_listing_item_middle, parent, false);
                break;
            default:
                v = LayoutInflater.from(context).inflate(R.layout.content_listing_item_l, parent, false);
                break;
        }
        return new ContentListingAdapter.ReviewHolder(v);
    }

    /*----------- set data in components ----------------------*/
    @Override
    public void onBindViewHolder(ContentListingAdapter.ReviewHolder holder, final int position) {
        switch (position) {
            case 0:
                holder.next_image.setImageResource(R.drawable.arrow1);
                holder.bottom_view.setBackgroundResource(R.drawable.radious_color1);
                holder.location.setTextColor(context.getResources().getColor(R.color.list_color1_end));
                break;
            case 1:
                holder.next_image.setImageResource(R.drawable.arrow2);
                holder.bottom_view.setBackgroundResource(R.drawable.radious_color2);
                holder.location.setTextColor(context.getResources().getColor(R.color.list_color2));
                break;
            case 2:
                holder.next_image.setImageResource(R.drawable.arrow3);
                holder.bottom_view.setBackgroundResource(R.drawable.radious_color3);
                holder.location.setTextColor(context.getResources().getColor(R.color.list_color3));
                break;
            default:
                switch (position % 3) {
                    case 0:
                        holder.next_image.setImageResource(R.drawable.arrow1);
                        holder.bottom_view.setBackgroundResource(R.drawable.radious_color1);
                        holder.location.setTextColor(context.getResources().getColor(R.color.list_color1_end));
                        break;
                    case 1:
                        holder.next_image.setImageResource(R.drawable.arrow2);
                        holder.bottom_view.setBackgroundResource(R.drawable.radious_color2);
                        holder.location.setTextColor(context.getResources().getColor(R.color.list_color2));
                        break;
                    case 2:
                        holder.next_image.setImageResource(R.drawable.arrow3);
                        holder.bottom_view.setBackgroundResource(R.drawable.radious_color3);
                        holder.location.setTextColor(context.getResources().getColor(R.color.list_color3));
                        break;
                }
                break;
        }

        if (holder.getItemViewType() == 3) {
            List<ContentListingModelThree> data = content_data.get(position).getThreeItem();
            Alert.showLog(TAG, "SIZZZZ data-- " + data.size());

            if (data != null)
            {
                if (data.size() > 0)
                {
                    for (int h = 0; h < data.size(); h++)
                    {
                        switch (h)
                        {
                            case 0:
                                Uri uriOne= Uri.parse(data.get(h).getFirstImage());
                                holder.content_image1.setImageURI(uriOne);
                                if(data.get(h).getFirstImage().contains("gif"))
                                {
                                    DraweeController controller = Fresco.newDraweeControllerBuilder()
                                                    .setUri(data.get(h).getFirstImage())
                                                    .setAutoPlayAnimations(true)
                                                    .build();
                                    holder.content_image1.setController(controller);
                                }
                                else
                                {
                                    Uri uritwo= Uri.parse(data.get(h).getFirstImage());
                                    holder.content_image1.setImageURI(uritwo);
                                }
                                holder.title1.setText(data.get(h).getHeadline());
                                holder.detail1.setText(data.get(h).getTagline());
                                holder.lay1.setVisibility(View.VISIBLE);
                                break;
                            case 1:
                              //  Uri uritwo= Uri.parse(data.get(h).getFirstImage());
                              //  holder.content_image2.setImageURI(uritwo);
                                if(data.get(h).getFirstImage().contains("gif"))
                                {
                                    DraweeController controller =
                                            Fresco.newDraweeControllerBuilder()
                                                    .setUri(data.get(h).getFirstImage())
                                                    .setAutoPlayAnimations(true)
                                                    .build();
                                    holder.content_image2.setController(controller);
                                }
                                else
                                {
                                    Uri uritwo= Uri.parse(data.get(h).getFirstImage());
                                    holder.content_image2.setImageURI(uritwo);
                                }

                                holder.title2.setText(data.get(h).getHeadline());
                                holder.detail2.setText(data.get(h).getTagline());
                                holder.lay2.setVisibility(View.VISIBLE);
                                break;
                            case 2:
                              //  Uri uriThree= Uri.parse(data.get(h).getFirstImage());
                              //  holder.content_image3.setImageURI(uriThree);
                                if(data.get(h).getFirstImage().contains("gif"))
                                {
                                    DraweeController controller =
                                            Fresco.newDraweeControllerBuilder()
                                                    .setUri(data.get(h).getFirstImage())
                                                    .setAutoPlayAnimations(true)
                                                    .build();
                                    holder.content_image3.setController(controller);
                                }
                                else
                                {
                                    Uri uritwo= Uri.parse(data.get(h).getFirstImage());
                                    holder.content_image3.setImageURI(uritwo);
                                }

                                holder.title3.setText(data.get(h).getHeadline());
                                holder.detail3.setText(data.get(h).getTagline());
                                holder.lay3.setVisibility(View.VISIBLE);
                                break;
                            default:
                                Alert.showLog("ContentListingAdapter", "ContentListingAdapter Default");
                                break;
                        }
                    }
                    holder.m_main_lay.setVisibility(View.VISIBLE);
                } else {
                    holder.m_main_lay.setVisibility(View.GONE);
                }
            }
        }
        else
        {
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

            if (content_data.get(position).getSectionName() != null) {
                Alert.showLog("section_name","section_name-- "+content_data.get(position).getSectionName());
                holder.location.setText(content_data.get(position).getSectionName());
            }

            if (content_data.get(position).getTagline() != null) {
                holder.detail.setText(content_data.get(position).getTagline());
            }
        }
    }


    @Override
    public int getItemCount() {
        return content_data.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {
        private TextView location, title, detail;
        private ImageView  next_image;
        private View bottom_view;
        private LinearLayout main_lay;
        private SimpleDraweeView content_image1, content_image2, content_image3;
        private TextView title1, title2, title3, detail1, detail2, detail3;
        private LinearLayout m_main_lay, lay1, lay2, lay3;

        private CardView card_img_1, card_img_2, card_img_3;

        private SimpleDraweeView content_image;
        public ReviewHolder(View itemView) {
            super(itemView);
            detail = (TextView) itemView.findViewById(R.id.detail);
            title = (TextView) itemView.findViewById(R.id.title);
            location = (TextView) itemView.findViewById(R.id.location);
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
            try {
                m_main_lay = (LinearLayout) itemView.findViewById(R.id.m_main_lay);
                lay1 = (LinearLayout) itemView.findViewById(R.id.lay1);
                lay2 = (LinearLayout) itemView.findViewById(R.id.lay2);
                lay3 = (LinearLayout) itemView.findViewById(R.id.lay3);

                content_image1 = (SimpleDraweeView) itemView.findViewById(R.id.content_image1);
                content_image2 = (SimpleDraweeView) itemView.findViewById(R.id.content_image2);
                content_image3 = (SimpleDraweeView) itemView.findViewById(R.id.content_image3);

                title1 = (TextView) itemView.findViewById(R.id.title1);
                title2 = (TextView) itemView.findViewById(R.id.title2);
                title3 = (TextView) itemView.findViewById(R.id.title3);

                detail1 = (TextView) itemView.findViewById(R.id.detail1);
                detail2 = (TextView) itemView.findViewById(R.id.detail2);
                detail3 = (TextView) itemView.findViewById(R.id.detail3);

                lay1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        try
                        {
                        List<ContentListingModelThree> val = content_data.get(getAdapterPosition()).getThreeItem();
                        ContentListingModel dt = new ContentListingModel();
                        dt.setContentId(val.get(0).getContentId());
                        dt.setHeadline(val.get(0).getHeadline());
                            dt.setItem_id(val.get(0).getItem_id());

                            dt.setTagline(val.get(0).getTagline());
                        dt.setSectionName(val.get(0).getSectionName());
                        dt.setSectionId(val.get(0).getSectionId());
                        dt.setCategoryName(val.get(0).getCategoryName());
                        dt.setCategoryId(val.get(0).getCategoryId());
                        dt.setSubCategoryName(val.get(0).getSubCategoryName());
                        dt.setSubCategoryId(val.get(0).getSubCategoryId());
                        dt.setLanguage(val.get(0).getLanguage());
                        dt.setFirstImage(val.get(0).getFirstImage());
                        dt.setContentLocation(val.get(0).getContentLocation());
                        dt.setLike(val.get(0).getLike());
                        dt.setTotal_like(val.get(0).getTotal_like());
                        dt.setComment_count(val.get(0).getComment_count());
                        dt.setShare_count(val.get(0).getShare_count());
                        dt.setView_count(val.get(0).getView_count());
                        dt.setCreator_first_name(val.get(0).getCreator_first_name());
                        dt.setCreator_last_name(val.get(0).getCreator_last_name());
                        dt.setDateOfCreation(val.get(0).getDateOfCreation());
                        dt.setSuggestedArticleList(val.get(0).getSuggestedArticleList());
                        onClickListener.OnClick(getAdapterPosition(), v, dt);
                        }catch (ArrayIndexOutOfBoundsException e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
                lay2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try
                        {
                        List<ContentListingModelThree> val = content_data.get(getAdapterPosition()).getThreeItem();
                        ContentListingModel dt = new ContentListingModel();
                        dt.setContentId(val.get(1).getContentId());
                        dt.setHeadline(val.get(1).getHeadline());
                            dt.setItem_id(val.get(1).getItem_id());
                        dt.setTagline(val.get(1).getTagline());
                        dt.setSectionName(val.get(1).getSectionName());
                        dt.setSectionId(val.get(1).getSectionId());
                        dt.setCategoryName(val.get(1).getCategoryName());
                        dt.setCategoryId(val.get(1).getCategoryId());
                        dt.setSubCategoryName(val.get(1).getSubCategoryName());
                        dt.setSubCategoryId(val.get(1).getSubCategoryId());
                        dt.setLanguage(val.get(1).getLanguage());
                        dt.setFirstImage(val.get(1).getFirstImage());
                        dt.setContentLocation(val.get(1).getContentLocation());
                        dt.setLike(val.get(1).getLike());
                        dt.setTotal_like(val.get(1).getTotal_like());
                        dt.setComment_count(val.get(1).getComment_count());
                        dt.setShare_count(val.get(1).getShare_count());
                        dt.setView_count(val.get(1).getView_count());
                        dt.setCreator_first_name(val.get(1).getCreator_first_name());
                        dt.setCreator_last_name(val.get(1).getCreator_last_name());
                        dt.setDateOfCreation(val.get(1).getDateOfCreation());
                        dt.setSuggestedArticleList(val.get(1).getSuggestedArticleList());
                        onClickListener.OnClick(getAdapterPosition(), v, dt);
                        }catch (ArrayIndexOutOfBoundsException e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
                lay3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try
                        {
                            List<ContentListingModelThree> val = content_data.get(getAdapterPosition()).getThreeItem();
                            ContentListingModel dt = new ContentListingModel();
                            dt.setContentId(val.get(2).getContentId());
                            dt.setHeadline(val.get(2).getHeadline());
                            dt.setItem_id(val.get(2).getItem_id());

                            dt.setTagline(val.get(2).getTagline());
                            dt.setSectionName(val.get(2).getSectionName());
                            dt.setSectionId(val.get(2).getSectionId());
                            dt.setCategoryName(val.get(2).getCategoryName());
                            dt.setCategoryId(val.get(2).getCategoryId());
                            dt.setSubCategoryName(val.get(2).getSubCategoryName());
                            dt.setSubCategoryId(val.get(2).getSubCategoryId());
                            dt.setLanguage(val.get(2).getLanguage());
                            dt.setFirstImage(val.get(2).getFirstImage());
                            dt.setContentLocation(val.get(2).getContentLocation());
                            dt.setLike(val.get(2).getLike());
                            dt.setTotal_like(val.get(2).getTotal_like());
                            dt.setComment_count(val.get(2).getComment_count());
                            dt.setShare_count(val.get(2).getShare_count());
                            dt.setView_count(val.get(2).getView_count());
                            dt.setCreator_first_name(val.get(2).getCreator_first_name());
                            dt.setCreator_last_name(val.get(2).getCreator_last_name());
                            dt.setDateOfCreation(val.get(2).getDateOfCreation());
                            dt.setSuggestedArticleList(val.get(2).getSuggestedArticleList());
                            onClickListener.OnClick(getAdapterPosition(), v, dt);

                        }catch (ArrayIndexOutOfBoundsException e)
                        {
                            e.printStackTrace();
                        }

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        public void OnClick(int position, View view, ContentListingModel data);
    }
}
