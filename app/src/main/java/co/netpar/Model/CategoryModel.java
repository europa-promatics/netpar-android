package co.netpar.Model;

/**
 * Created by promatics on 10/11/2017.
 */

public class CategoryModel {
    String category_id;
    String category_language;
    String category_sectionId;
    String categoryName;
    String sectionnName;
    String thumbnaillImage;
    String horigontallImage;
    String categoryFormat;
    String listViewFormat,item_id;

    public String getItem_id() {
        return item_id;
    }

    public String getHorigontallImage() {
        return horigontallImage;
    }

    public String getCategoryFormat() {
        return categoryFormat;
    }

    public String getListViewFormat() {
        return listViewFormat;
    }


    public String getCategory_id() {
        return category_id;
    }

    public String getCategory_language() {
        return category_language;
    }

    public String getCategory_sectionId() {
        return category_sectionId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getSectionnName() {
        return sectionnName;
    }

    public String getThumbnaillImage() {
        return thumbnaillImage;
    }

    public CategoryModel(String category_id, String category_language, String category_sectionId, String categoryName,
                         String sectionnName, String thumbnaillImage,String horigontallImage,String categoryFormat,
                         String listViewFormat,String item_id) {
        this.category_id = category_id;
        this.category_language = category_language;
        this.category_sectionId = category_sectionId;
        this.categoryName = categoryName;
        this.sectionnName = sectionnName;
        this.thumbnaillImage = thumbnaillImage;
        this.horigontallImage = horigontallImage;
        this.categoryFormat = categoryFormat;
        this.listViewFormat = listViewFormat;
        this.item_id=item_id;
    }


}
