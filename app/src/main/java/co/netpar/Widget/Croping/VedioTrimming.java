package co.netpar.Widget.Croping;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import co.netpar.App.Controller;
import co.netpar.Fragment.Contribution;
import co.netpar.R;
import co.netpar.Syncronization.NetparDataBase.DataBaseStrings;
import life.knowledge4.videotrimmer.K4LVideoTrimmer;
import life.knowledge4.videotrimmer.interfaces.OnTrimVideoListener;

public class VedioTrimming extends AppCompatActivity {

    private K4LVideoTrimmer videoTrimmer;
    private String [] media_path;
    private String media_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vedio_trimming);

        videoTrimmer = ((K4LVideoTrimmer) findViewById(R.id.timeLine));
        try
        {
            if (videoTrimmer != null) {
                videoTrimmer.setVideoURI(Uri.parse(getIntent().getStringExtra("VIDEO")));
                media_path=getIntent().getStringExtra("VIDEO").split("/");
                media_name=media_path[media_path.length-1];
            }
            videoTrimmer.setDestinationPath(Controller.getOther_path()+"/");
            videoTrimmer.setMaxDuration(30);
            videoTrimmer.setOnTrimVideoListener(new OnTrimVideoListener() {
                @Override
                public void getResult(final Uri uri)
                {
                    Intent intent = new Intent();
                    intent.putExtra("PATH",uri.getPath());
                    setResult(Contribution.CROP_VIDEO,intent);
                    finish();
                }

                @Override
                public void cancelAction() {
                    onBackPressed();
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    @Override
    public void onBackPressed() {
        if(videoTrimmer!=null)
        {
            videoTrimmer.destroy();
        }
        Intent intent = new Intent();
        intent.putExtra("PATH", DataBaseStrings.DEFAULT_VALUE);
        setResult(Contribution.CROP_VIDEO,intent);
        finish();
    }
}
