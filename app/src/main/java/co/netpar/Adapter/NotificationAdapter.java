package co.netpar.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

import co.netpar.Comman.Constants;
import co.netpar.ContentView;
import co.netpar.Model.NotificationModel;
import co.netpar.R;

/**
 * Created by promatics on 10/12/2017.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ReviewHolder> {
    private List<NotificationModel> mData;
    private Context context;
    public NotificationAdapter(Context context,List<NotificationModel> data) {
        mData = data;
        this.context=context;
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v= LayoutInflater.from(context).inflate(R.layout.notification_item,parent,false);
        return new ReviewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ReviewHolder holder, int position) {
        holder.title.setText(mData.get(position).getTitle());
        holder.notification.setText(mData.get(position).getDetail());
        try {
            holder.time.setText(Constants.changeDateFormat(context,mData.get(position).getDate().split("T")[0]));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {
        protected TextView title,notification,time;
        protected CardView card_view;
        public ReviewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            notification = (TextView) itemView.findViewById(R.id.notification);
            time = (TextView) itemView.findViewById(R.id.time);
        }
    }
}
