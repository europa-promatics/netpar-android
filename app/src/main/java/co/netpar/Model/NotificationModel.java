package co.netpar.Model;

/**
 * Created by promatics on 11/28/2017.
 */

public class NotificationModel {
    String notification_id;
    String title;
    String detail;
    String date;

    public NotificationModel(String notification_id, String title, String detail, String date) {
        this.notification_id = notification_id;
        this.title = title;
        this.detail = detail;
        this.date = date;
    }


    public String getNotification_id() {
        return notification_id;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public String getDate() {
        return date;
    }
}
