package co.netpar.Model;

import java.io.Serializable;

/**
 * Created by promatics on 11/2/2017.
 */

public class DownloadedData implements Serializable{
    public String POST_ID ;
    public String POST_TAG ;
    public String DATA_PATH_URL;
    public String DATA_PATH_DEVICE;
    public String MEDIA_NAME;
    public String MEDIA_INDEX;
    public String POST_USER_ID ;

    public DownloadedData(String POST_ID, String POST_TAG, String DATA_PATH_URL, String DATA_PATH_DEVICE, String MEDIA_NAME, String MEDIA_INDEX,String POST_USER_ID) {
        this.POST_ID = POST_ID;
        this.POST_TAG = POST_TAG;
        this.DATA_PATH_URL = DATA_PATH_URL;
        this.DATA_PATH_DEVICE = DATA_PATH_DEVICE;
        this.MEDIA_NAME = MEDIA_NAME;
        this.MEDIA_INDEX = MEDIA_INDEX;
        this.POST_USER_ID=POST_USER_ID;
    }

    public String getPOST_USER_ID() {
        return POST_USER_ID;
    }

    public String getPOST_ID() {
        return POST_ID;
    }

    public String getPOST_TAG() {
        return POST_TAG;
    }

    public String getDATA_PATH_URL() {
        return DATA_PATH_URL;
    }

    public String getDATA_PATH_DEVICE() {
        return DATA_PATH_DEVICE;
    }

    public String getMEDIA_NAME() {
        return MEDIA_NAME;
    }

    public String getMEDIA_INDEX() {
        return MEDIA_INDEX;
    }

}
