package co.netpar.Syncronization.NetparDataBase;

import android.net.Uri;

public class DataBaseStrings {

    public static final String DEFAULT_VALUE="Not Found";

    /*DB VERSION AND NAME*/
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "NetparDB";



    public static final Uri BASE_CONTENT_URI = Uri.parse("content://co.netpar.provider");
    public static final String PATH_ARTICLES = "articles";
    /*ContentProvider information for articles*/
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ARTICLES).build();
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_ARTICLES;
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_ARTICLES;


    /*TABLE NAME*/
    public static final String SECTION = "section";
    public static final String CATEGORY = "category";
    public static final String SUBCATEGORY = "subcategory";

    public static final String POST_MAIN = "all_post";
    public static final String POST_DETAIL = "post_detail";
    public static final String POST_COMMENT = "post_comment";

    public static final String POST_DOWNLOAD = "post_download";

    public static final String FRIENDS = "friends";

    public static final String SAVED_POST = "mypost";
    public static final String NOTIFICATION = "notification";

    /*COLUMN NAME*/
    public static final String SECTION_ID = "id";
    public static final String SECTION_NAME = "sectionName";
    public static final String SECTION_LANGUAGE = "language";
    public static final String SECTION_IMAGE = "thumbnailImage";
    public static final String SECTION_HORIZONTAL_IMAGE = "horigontalImage";
    public static final String SECTION_LAYOUT_TYPE = "layout_type";
    public static final String SECTION_ORDER = "order_number";


    public static final String CATEGORY_ID = "id";
    public static final String CATEGORY_LANGUAGE = "language";
    public static final String CATEGORY_NAME = "categoryName";
    public static final String CATEGORY_SECTION_ID = "category_sectionId";
    public static final String CATEGORY_SECTION_NAME = "sectionnName";
    public static final String CATEGORY_IMAGE = "thumbnaillImage";
    public static final String CATEGORY_HORIZONTAL_IMAGE = "horigontallImage";
    public static final String CATEGORY_FORMAT = "categoryFormat";
    public static final String LIST_VIEW_FORMAT = "listViewFormat";


    public static final String SUB_CATEGORY_ID = "id";
    public static final String SUB_CATEGORY_LANGUAGE = "language";
    public static final String SUB_CATEGORY_SECTION_ID = "category_sectionId";
    public static final String SUB_CATEGORY_CATEGORY_ID = "sub_category_categoryId";
    public static final String SUB_CATEGORY_NAME = "sub_category_Name";
    public static final String SUB_CATEGORY_SECTION_NAME = "sub_category_sectionName";
    public static final String SUB_CATEGORY_CATEGORY_NAME = "sub_category_categoryName";
    public static final String SUB_CATEGORY_IMAGE = "thumbnaillImage";
    public static final String SUB_CATEGORY_HORIZONTAL_IMAGE = "horigontallImage";
    public static final String SUB_CATEGORY_FORMAT = "categoryFormat";


    public static final String POST_ID = "post_id";
    public static final String POST_ITEM_ID = "post_item_id";
    public static final String POST_HEADLINE = "headline";
    public static final String POST_SLUG = "slug";
    public static final String POST_TAGLINE = "tagline";
    public static final String POST_LANGUAGE = "language";
    public static final String POST_IMAGE = "row_data";
    public static final String POST_HORIZENTIAL_IMAGE = "row_hor_data";
    public static final String POST_IMAGE_BLOB = "post_image_blob";
    public static final String POST_SECTION = "section";
    public static final String POST_CATEGORY = "category";
    public static final String POST_SUBCATEGORY = "subcategory";
    public static final String POST_SECTION_ID = "section_id";
    public static final String POST_CATEGORY_ID = "category_id";
    public static final String POST_SUBCATEGORY_ID = "subcategory_id";
    public static final String POST_LIKE_COUNT = "like_count";
    public static final String POST_COMMENT_COUNT = "comment_count";
    public static final String POST_VIEW_COUNT = "view_count";
    public static final String POST_SHARE_COUNT = "share_count";
    public static final String POST_CREATOR_FNAME = "first_name";
    public static final String POST_CREATOR_LNAME = "last_name";
    public static final String POST_DATE = "creation_date";
    public static final String POST_LIKE= "like";
    public static final String POST_SAVE = "save_post";
    public static final String POST_DELETE_STATUS = "delete_status";
    public static final String POST_SUGGESTED_ARTICLES = "suggested_article_list";
    public static final String POST_USER_ID="user_id";


    public static final String POST_ITEM_TAG = "tag";
    public static final String POST_ITEM_BACK_COLOR = "backgroundColor";
    public static final String POST_ITEM_MARGIN_TOP = "top";
    public static final String POST_ITEM_MARGIN_BOTTOM = "bottom";
    public static final String POST_ITEM_MARGIN_RIGHT = "right";
    public static final String POST_ITEM_MARGIN_LEFT = "left";
    public static final String POST_ITEM_TEXT = "buttonText";
    public static final String POST_ITEM_WIDTH = "width";
    public static final String POST_ITEM_DESCRIPTION = "description_text";
    public static final String POST_ITEM_INDEX = "index_val";
    public static final String POST_ITEM_IMAGE_BLOB= "image_blob";
    public static final String URL = "url";
    public static final String POST_ITEM_DOWNLOADABLE = "downloadable";

    public static final String POST_COMMENT_ID = "comment_id";
    public static final String POST_DATE_OF_COMMENT = "dateOfComment";
    public static final String POST_COMMENT_USER_ID = "userId";
    public static final String POST_COMENT_ARTICLE_NAME = "articleName";
    public static final String POST_COMMENT_ARTICLE_ID = "articleId";
    public static final String POST_COMMENT_USER_PHONE= "userPhone";
    public static final String POST_COMMENT_DETAIL= "userComment";
    public static final String POST_COMMENT_USER_NAME = "userName";
    public static final String POST_COMMENT_STATUS = "status";
    public static final String POST_COMMENT_DELETE_STATUS = "deleteStatus";
    public static final String POST_COMMENT_USER_IMAGE_URL = "user_image_url";
    public static final String POST_COMMENT_USER_IMAGE = "user_image";

    public static final String POST_TAG = "post_tag";
    public static final String DATA_PATH_URL= "url_path";
    public static final String DATA_PATH_DEVICE= "device_path";
    public static final String MEDIA_NAME= "media_name";
    public static final String MEDIA_INDEX= "media_index";

    public static final String FRIENDS_ID_IN_DEVICE_STORAGE= "device_id";
    public static final String FRIENDS_NAME = "name";
    public static final String FRIENDS_MOBILE_NUMBER = "friends_mobile";
    public static final String FRIENDS_IMAGE_URI = "image_uri";
    public static final String FRIENDS_IMAGE = "image";
    public static final String FRIENDS_ID_ON_SERVER= "server_id";
    public static final String INVITE_STATUS= "inviteStatus";

    public static final String NOTIFICATION_ID= "notification_id";
    public static final String NOTIFICATION_TITLE= "notification_title";
    public static final String NOTIFICATION_MESSAGE= "notification_message";
    public static final String NOTIFICATION_TIME= "notification_time";
}
