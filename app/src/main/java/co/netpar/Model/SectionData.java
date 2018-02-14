package co.netpar.Model;

/**
 * Created by promatics on 10/9/2017.
 */

public class SectionData {
    String title,image;
    boolean normal=true;

    public String getTitle() {
        return title;
    }

    public SectionData setTitle(String title) {
        this.title = title; return this;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public boolean isNormal() {
        return normal;
    }

    public SectionData setNormal(boolean normal) {
        this.normal = normal; return this;
    }
}
