package co.netpar.Model;

import android.widget.ImageView;

/**
 * Created by promatics on 10/25/2017.
 */

public class SetMultipleImageDynamically {
    private ImageView image;
    private int ID;
    private String content_id;
    private String URL;
    private byte[] photo;

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public ImageView getImage() {
        return image;
    }

    public int getID() {
        return ID;
    }

    public String getURL() {
        return URL;
    }

    public SetMultipleImageDynamically(ImageView image, int ID, String URL) {
        this.image = image;
        this.ID = ID;
        this.URL = URL;
    }
}
