package co.netpar.Model;

import co.netpar.Syncronization.NetparDataBase.DataBaseStrings;

/**
 * Created by promatics on 11/15/2017.
 */

public class MediaModel {
    int mediaType;
    String pathindevice;
    String media_name_in_device;
    String server_path = DataBaseStrings.DEFAULT_VALUE;
    String server_type = DataBaseStrings.DEFAULT_VALUE;
    String server_size = DataBaseStrings.DEFAULT_VALUE;

    public String getServer_path() {
        return server_path;
    }

    public void setServer_path(String server_path) {
        this.server_path = server_path;
    }

    public String getServer_type() {
        return server_type;
    }

    public void setServer_type(String server_type) {
        this.server_type = server_type;
    }

    public String getServer_size() {
        return server_size;
    }

    public void setServer_size(String server_size) {
        this.server_size = server_size;
    }

    public int getMediaType() {
        return mediaType;
    }

    public String getPathindevice() {
        return pathindevice;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public void setPathindevice(String pathindevice) {
        this.pathindevice = pathindevice;
    }

    public String getMedia_name_in_device() {
        return media_name_in_device;
    }

    public void setMedia_name_in_device(String media_name_in_device) {
        this.media_name_in_device = media_name_in_device;
    }

    public MediaModel(int mediaType, String pathindevice, String media_name_in_device) {
        this.mediaType = mediaType;
        this.pathindevice = pathindevice;
        this.media_name_in_device=media_name_in_device;
    }

}
