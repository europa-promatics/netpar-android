package co.netpar.Model;

import java.io.Serializable;

/**
 * Created by promatics on 11/8/2017.
 */

public class ContactsModel implements Serializable
{
    private String device_id;
    private String name;
    private String number;
    private String image;
    private String imageuri;
    private String server_id;
    private String inviteStatus; //0(friend),1(invite)

    public static final String FRIEND="0";
    public static final String INVITE="1";

    public String getDevice_id() {
        return device_id;
    }

    public String getImageuri() {
        return imageuri;
    }

    public String getServer_id() {
        return server_id;
    }

    public String getInviteStatus() {
        return inviteStatus;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getImage() {
        return image;
    }

    public ContactsModel(String device_id, String name, String number,String imageuri, String image,String inviteStatus,String server_id) {
        this.device_id = device_id;
        this.name = name;
        this.number = number;
        this.imageuri=imageuri;
        this.image = image;
        this.inviteStatus=inviteStatus;
        this.server_id=server_id;
    }
}
