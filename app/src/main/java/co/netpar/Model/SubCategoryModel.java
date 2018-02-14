package co.netpar.Model;

/**
 * Created by promatics on 10/11/2017.
 */

public class SubCategoryModel {
    String sub_category_id;
    String sub_category_languagee;
    String sub_category_sectionId;
    String sub_category_categoryId;
    String sub_category_Name;
    String sub_category_sectionName;
    String sub_category_categoryName;
    String sub_category_horigontalImage;
    String subCategoryFormat,item_id;

    public String getItem_id() {
        return item_id;
    }

    public String getSub_category_id() {
        return sub_category_id;
    }

    public String getSub_category_languagee() {
        return sub_category_languagee;
    }

    public String getSub_category_sectionId() {
        return sub_category_sectionId;
    }

    public String getSub_category_categoryId() {
        return sub_category_categoryId;
    }

    public String getSub_category_Name() {
        return sub_category_Name;
    }

    public String getSub_category_sectionName() {
        return sub_category_sectionName;
    }

    public String getSub_category_categoryName() {
        return sub_category_categoryName;
    }

    public String getSub_category_thumbnaillImage() {
        return sub_category_thumbnaillImage;
    }

    public String getSub_category_horigontalImage() {
        return sub_category_horigontalImage;
    }

    public String getSubCategoryFormat() {
        return subCategoryFormat;
    }

    public SubCategoryModel(String sub_category_id, String sub_category_languagee, String sub_category_sectionId,
                            String sub_category_categoryId, String sub_category_Name, String sub_category_sectionName,
                            String sub_category_categoryName, String sub_category_thumbnaillImage,
                            String sub_category_horigontalImage, String subCategoryFormat,String item_id)
    {
        this.sub_category_id = sub_category_id;
        this.sub_category_languagee = sub_category_languagee;
        this.sub_category_sectionId = sub_category_sectionId;
        this.sub_category_categoryId = sub_category_categoryId;
        this.sub_category_Name = sub_category_Name;
        this.sub_category_sectionName = sub_category_sectionName;
        this.sub_category_categoryName = sub_category_categoryName;
        this.sub_category_thumbnaillImage = sub_category_thumbnaillImage;
        this.sub_category_horigontalImage=sub_category_horigontalImage;
        this.subCategoryFormat=subCategoryFormat;
        this.item_id=item_id;
    }

    String sub_category_thumbnaillImage;

}
