package co.netpar.Model;

import android.widget.ImageView;
import android.widget.VideoView;

/**
 * Created by promatics on 10/31/2017.
 */

public class SetMultipleVedioDynamically
{
    private VideoView videoView;
    private int ID;
    private String content_id;
    private String URL;

    public VideoView getVideoView() {
        return videoView;
    }

    public int getID() {
        return ID;
    }

    public String getContent_id() {
        return content_id;
    }

    public String getURL() {
        return URL;
    }

    public SetMultipleVedioDynamically(VideoView videoView, int ID, String content_id, String URL) {
        this.videoView = videoView;
        this.ID = ID;
        this.content_id = content_id;
        this.URL = URL;
    }
}
