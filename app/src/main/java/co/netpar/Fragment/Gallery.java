package co.netpar.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import co.netpar.Adapter.NotificationAdapter;
import co.netpar.Comman.Constants;
import co.netpar.Comman.DrowableFunction;
import co.netpar.Home;
import co.netpar.MediaDetail;
import co.netpar.Model.DownloadedData;
import co.netpar.Model.NotificationModel;
import co.netpar.PagerAdapter.GalleryAdapter;
import co.netpar.R;
import co.netpar.Syncronization.NetparDataBase.DatabaseHelper;
import co.netpar.Syncronization.NetparDataBase.SharedPreference;
import co.netpar.Widget.GridSpacingItemDecoration;

/**
 * Created by promatics on 11/28/2017.
 */

public class Gallery extends Fragment
{
    private Context context;
    public static Gallery instance;
    private GalleryAdapter adapter;
    private DatabaseHelper helper;
    private List<DownloadedData> download_data=new ArrayList<>();
    private RecyclerView downloadData;

    public static synchronized Gallery getInstance()
    {
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.gallery_fragment, container, false);
        instance=this;
        context=container.getContext();
        helper=((Home)context).helper;
        initializeView(v);
        return v;
    }

    /*-------------- initialize components -------------------*/
    private void initializeView(View v)
    {
        downloadData=(RecyclerView)v.findViewById(R.id.downloadData);
        downloadData.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
        adapter=new GalleryAdapter(context,download_data);
        downloadData.setAdapter(adapter);
        adapter.setOnItemClickListener(new GalleryAdapter.OnItemClickListener() {
            @Override
            public void onViewDetail(int position, DownloadedData data) {
                //openMediaDetail(data);
            }

            @Override
            public void onDelete(int position, DownloadedData data) {
                deleteOption(context, data);
            }
        });
        int spanCount = 3;
        int spacing = 5;
        boolean includeEdge = false;
        downloadData.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        downloadData.setNestedScrollingEnabled(false);
    }

    /*-------------- set data in to gallery -------------------*/
    public void setGalleryData()
    {
        download_data.clear();
        download_data.addAll(helper.getAllDownloads(SharedPreference.retrieveData(getActivity(), Constants.USER_ID)));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser)
        {
            setGalleryData();
        }
    }

    private void openMediaDetail(DownloadedData data) {
        if (DrowableFunction.getAllFilesName(data.getDATA_PATH_DEVICE(), data.getMEDIA_NAME())) {
            startActivity(new Intent(context, MediaDetail.class).putExtra("DATA", data));
        } else {
            /*Need to download From URL*/
        }
    }

    /*---------------- Delete Media ---------------------------*/
    public void deleteOption(Context context, final DownloadedData data)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View dialogView = ((Activity) context).getLayoutInflater().inflate(R.layout.del_download, null);
        CardView yes_card_view = (CardView) dialogView.findViewById(R.id.yes_card_view);
        CardView no = (CardView) dialogView.findViewById(R.id.no);
        yes_card_view.setBackgroundResource(R.drawable.gradient_yello_radious);
        no.setBackgroundResource(R.drawable.gradient_black_radious);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setLayout((int) (((double) context.getResources().getDisplayMetrics().widthPixels) * 0.9d), -2);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        yes_card_view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alertDialog.dismiss();
                helper.deleteDownload(data);
                setGalleryData();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

}





