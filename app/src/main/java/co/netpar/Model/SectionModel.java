package co.netpar.Model;

import co.netpar.Syncronization.NetparDataBase.DataBaseStrings;

/**
 * Created by promatics on 10/11/2017.
 */

public class SectionModel {
    String _id;
    String sectionName;
    String thumbnailImage,item_id;
    String language;
    String horigontalImage;
    String section_lay_type;//sectionViewFormat, Section Template One, Section Template Two
     int order_no;



    public String getHorigontalImage() {
        return horigontalImage;
    }

    public String getSection_lay_type() {
        return section_lay_type;
    }

    public byte[] getDecodedByte() {
        return decodedByte;
    }

    public void setDecodedByte(byte[] decodedByte) {
        this.decodedByte = decodedByte;
    }

    byte[] decodedByte;

    public String get_id() {
        return _id;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getThumbnailImage() {
        return thumbnailImage;
    }

    public String getLanguage() {
        return language;
    }

    public int getOrder_no() {
        return order_no;
    }

    public SectionModel(String _id, String sectionName, String language, String thumbnailImage, String horigontalImage, String section_lay_type, int order_no,String item_id) {
        this._id = _id;
        this.sectionName = sectionName;
        this.language = language;
        this.thumbnailImage=thumbnailImage;
        this.horigontalImage=horigontalImage;
        this.section_lay_type=section_lay_type;
        this.order_no=order_no;
        this.item_id=item_id;
    }


    public String getItem_id() {
        return item_id;
    }
}



