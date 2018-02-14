package co.netpar;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.squareup.picasso.Picasso;

import java.io.File;

import co.netpar.App.Controller;
import co.netpar.Comman.Alert;
import co.netpar.Model.DownloadedData;

public class MediaDetail extends AppCompatActivity implements View.OnClickListener
{
    private static final int PICK_VIDEO_REQUEST = 1001;
    private MediaPlayer mMediaPlayer;
    private SurfaceHolder mVideo_Surface;
    private Uri mVideoUri;
    private final String TAG="MediaDetail Activity";
    private  DownloadedData data_val;
    private RelativeLayout video_lay;
    private ImageView image_view;
    private RelativeLayout image_lay;

    private ImageView toolbar_home;
    private TextView tol_title;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_detail);
        Bundle data=getIntent().getExtras();
        video_lay=(RelativeLayout)findViewById(R.id.video_lay);
        video_lay.setVisibility(View.GONE);
        image_lay=(RelativeLayout)findViewById(R.id.image_lay);
        image_lay.setVisibility(View.GONE);

        /*Come from MyAccount Fragment*/
        if(data!=null)
        {
            data_val = (DownloadedData) data.getSerializable("DATA");
            if(data_val.getDATA_PATH_DEVICE()!=null)
            {
                if(data_val.getDATA_PATH_DEVICE().contains("Video"))
                {
                    setVideoView();
                }
                else if(data_val.getDATA_PATH_DEVICE().contains("Audio"))
                {
                    setAudioView();
                }
                else if(data_val.getDATA_PATH_DEVICE().contains("Image"))
                {
                    setImageView();
                }
            }
            toolbar_home=(ImageView)findViewById(R.id.back_icon);
            toolbar_home.setOnClickListener(this);
            tol_title=(TextView)findViewById(R.id.tol_title);
            tol_title.setText(data_val.getMEDIA_NAME());
        }
       // getAllFilesName();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void setVideoView()
    {
        SurfaceView first = (SurfaceView) findViewById(R.id.video_Surface);
        first.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                mVideo_Surface = surfaceHolder;
                if (mVideoUri != null) {
                    mMediaPlayer = MediaPlayer.create(getApplicationContext(), mVideoUri, mVideo_Surface);
                    mMediaPlayer.start();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                Alert.showLog(TAG, "First surface destroyed!");
            }
        });
        video_lay.setVisibility(View.VISIBLE);
    }

    private void setAudioView()
    {
        SurfaceView first = (SurfaceView) findViewById(R.id.video_Surface);
        first.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                mVideo_Surface = surfaceHolder;
                if (mVideoUri != null) {
                    mMediaPlayer = MediaPlayer.create(getApplicationContext(), mVideoUri, mVideo_Surface);
                    mMediaPlayer.start();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                Alert.showLog(TAG, "First surface destroyed!");
            }
        });
    }

    private void setImageView()
    {
        Alert.showLog("DETAIL","setImageView- "+data_val.getDATA_PATH_DEVICE()+"/"+data_val.getMEDIA_NAME());
        image_view = (ImageView) findViewById(R.id.image_view);
        image_lay.setVisibility(View.VISIBLE);
        Picasso.with(this).load(new File(data_val.getDATA_PATH_DEVICE()+"/"+data_val.getMEDIA_NAME())).into(image_view);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoUri = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK) {
            mVideoUri = data.getData();
        }
    }

    private void getAllFilesName()
    {
        File sdCardRoot = Environment.getExternalStorageDirectory();
        File yourDir = new File(sdCardRoot, Controller.APP_NAME);
        for (File f : yourDir.listFiles())
        {
            if (f.isFile())
            {
                String name = f.getName();
                Alert.showLog("file names", name);
            }
        }
    }

    public void doStartStop(View view) {
        if (mMediaPlayer == null) {
            Intent pickVideo = new Intent(Intent.ACTION_PICK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                pickVideo.setTypeAndNormalize("video/*");
            }
            startActivityForResult(pickVideo, PICK_VIDEO_REQUEST);
        }
        else
            {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back_icon:
                onBackPressed();
                break;
        }
    }
}
