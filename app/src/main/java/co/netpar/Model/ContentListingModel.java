package co.netpar.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by promatics on 10/15/2017.
 */

public class ContentListingModel implements Serializable {
    String contentId;
    String language;
    String sectionId;
    String categoryId;
    String subCategoryId;
    String headline;
    String slug;
    String description;
    String tagline;
    String sectionName;
    String categoryName;
    String subCategoryName;
    String contentLocation;
    String like;//Only to check like status of user for a item 0,1
    String save;//Only to check save status of user for a item 0,1
    String total_like;
    String comment_count;
    String view_count;
    String share_count;
    String creator_first_name;
    String creator_last_name;
    String dateOfCreation;
    String deleteStatus,item_id,horizontalPicture;//Only to showing visibility of item true,false

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getSuggestedArticleList() {
        return suggestedArticleList;
    }

    public void setSuggestedArticleList(String suggestedArticleList) {
        this.suggestedArticleList = suggestedArticleList;
    }

    String suggestedArticleList;

    public String getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(String deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public String getSave() {
        return save;
    }

    public void setSave(String save) {
        this.save = save;
    }

    public String getCreator_first_name() {
        return creator_first_name;
    }

    public void setCreator_first_name(String creator_first_name) {
        this.creator_first_name = creator_first_name;
    }

    public String getCreator_last_name() {
        return creator_last_name;
    }

    public void setCreator_last_name(String creator_last_name) {
        this.creator_last_name = creator_last_name;
    }

    public String getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(String dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    public String getView_count() {
        return view_count;
    }

    public void setView_count(String view_count) {
        this.view_count = view_count;
    }

    public String getShare_count() {
        return share_count;
    }

    public void setShare_count(String share_count) {
        this.share_count = share_count;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getTotal_like() {
        return total_like;
    }

    public void setTotal_like(String total_like) {
        this.total_like = total_like;
    }

    public List<ContentListingModelThree> getThreeItem() {
        return threeItem;
    }

    public void setThreeItem(List<ContentListingModelThree> threeItem) {
        this.threeItem = threeItem;
    }

    public List<ContentListingModelThree> threeItem;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFirstImage() {
        return firstImage;
    }

    public void setFirstImage(String firstImage) {
        this.firstImage = firstImage;
    }

    String firstImage;

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getHeadline() {
        return headline;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getContentLocation() {
        return contentLocation;
    }

    public void setContentLocation(String contentLocation) {
        this.contentLocation = contentLocation;
    }


    public String getHorizontalPicture() {
        return horizontalPicture;
    }

    public void setHorizontalPicture(String horizontalPicture) {
        this.horizontalPicture = horizontalPicture;
    }
}