package co.netpar.Syncronization.NetparDataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.webkit.DownloadListener;

import java.util.ArrayList;
import java.util.List;

import co.netpar.Comman.Alert;
import co.netpar.Model.CategoryModel;
import co.netpar.Model.CommentListModel;
import co.netpar.Model.ContactsModel;
import co.netpar.Model.ContentListingDetailModel;
import co.netpar.Model.ContentListingModel;
import co.netpar.Model.DownloadedData;
import co.netpar.Model.NotificationModel;
import co.netpar.Model.SectionModel;
import co.netpar.Model.SubCategoryModel;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static volatile DatabaseHelper instance;
    private final SQLiteDatabase db;
    private final String TAG = getClass().getName();

    public DatabaseHelper(Context context, SQLiteDatabase db) {
        super(context, DataBaseStrings.DB_NAME, null, 1);
        this.db = db;
    }

    public static DatabaseHelper getInstance(Context c) {
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null) {
                    instance = new DatabaseHelper(c);
                }
            }
        }
        return instance;
    }

    public DatabaseHelper(Context c) {
        super(c, DataBaseStrings.DB_NAME, null, DataBaseStrings.DB_VERSION);
        this.db = getWritableDatabase();
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    /*
    *
    * Create all tables
    *
    * */
    private void createTable(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE [" + DataBaseStrings.SECTION + "] ([" +
                DataBaseStrings.SECTION_ID + "] TEXT,[" +
                DataBaseStrings.POST_ITEM_ID + "] TEXT,[" +
                DataBaseStrings.SECTION_NAME + "] TEXT,[" +
                DataBaseStrings.SECTION_LANGUAGE + "] TEXT,[" +
                DataBaseStrings.SECTION_IMAGE + "] TEXT,[" +
                DataBaseStrings.SECTION_HORIZONTAL_IMAGE + "] TEXT,[" +
                DataBaseStrings.SECTION_LAYOUT_TYPE + "] TEXT,[" +
                DataBaseStrings.SECTION_ORDER + "] INTEGER DEFAULT 0);");

        db.execSQL("CREATE TABLE [" + DataBaseStrings.CATEGORY + "] ([" +
                DataBaseStrings.CATEGORY_ID + "] TEXT,[" +
                DataBaseStrings.POST_ITEM_ID + "] TEXT,[" +
                DataBaseStrings.CATEGORY_NAME + "] TEXT,[" +
                DataBaseStrings.CATEGORY_LANGUAGE + "] TEXT,[" +
                DataBaseStrings.CATEGORY_IMAGE + "] TEXT,[" +
                DataBaseStrings.CATEGORY_SECTION_ID + "] TEXT,[" +
                DataBaseStrings.CATEGORY_SECTION_NAME + "] TEXT,[" +
                DataBaseStrings.CATEGORY_HORIZONTAL_IMAGE + "] TEXT,[" +
                DataBaseStrings.CATEGORY_FORMAT + "] TEXT,[" +
                DataBaseStrings.LIST_VIEW_FORMAT + "] TEXT);");

        db.execSQL("CREATE TABLE [" + DataBaseStrings.SUBCATEGORY + "] ([" +
                DataBaseStrings.SUB_CATEGORY_ID + "] TEXT,[" +
                DataBaseStrings.POST_ITEM_ID + "] TEXT,[" +
                DataBaseStrings.SUB_CATEGORY_NAME + "] TEXT,[" +
                DataBaseStrings.SUB_CATEGORY_LANGUAGE + "] TEXT,[" +
                DataBaseStrings.SUB_CATEGORY_IMAGE + "] TEXT,[" +
                DataBaseStrings.SUB_CATEGORY_CATEGORY_ID + "] TEXT,[" +
                DataBaseStrings.SUB_CATEGORY_CATEGORY_NAME + "] TEXT,[" +
                DataBaseStrings.SUB_CATEGORY_SECTION_ID + "] TEXT,[" +
                DataBaseStrings.SUB_CATEGORY_SECTION_NAME + "] TEXT,[" +
                DataBaseStrings.SUB_CATEGORY_HORIZONTAL_IMAGE + "] TEXT,[" +
                DataBaseStrings.SUB_CATEGORY_FORMAT + "] TEXT);");

        db.execSQL("CREATE TABLE [" + DataBaseStrings.POST_MAIN + "] ([" +
                DataBaseStrings.POST_ID + "] TEXT,[" +
                DataBaseStrings.POST_ITEM_ID + "] TEXT,[" +
                DataBaseStrings.POST_HEADLINE + "] TEXT,[" +
                DataBaseStrings.POST_SLUG + "] TEXT,[" +
                DataBaseStrings.POST_TAGLINE + "] TEXT,[" +
                DataBaseStrings.POST_LANGUAGE + "] TEXT,[" +
                DataBaseStrings.POST_IMAGE + "] TEXT,[" +
                DataBaseStrings.POST_HORIZENTIAL_IMAGE + "] TEXT,[" +
                DataBaseStrings.POST_IMAGE_BLOB + "] BLOB,[" +
                DataBaseStrings.POST_SECTION + "] TEXT,[" +
                DataBaseStrings.POST_CATEGORY + "] TEXT,[" +
                DataBaseStrings.POST_SUBCATEGORY + "] TEXT,[" +
                DataBaseStrings.POST_SECTION_ID + "] TEXT,[" +
                DataBaseStrings.POST_CATEGORY_ID + "] TEXT,[" +
                DataBaseStrings.POST_SUBCATEGORY_ID + "] TEXT,[" +
                DataBaseStrings.POST_LIKE + "] TEXT,[" +
                DataBaseStrings.POST_SAVE + "] TEXT,[" +
                DataBaseStrings.POST_LIKE_COUNT + "] TEXT,[" +
                DataBaseStrings.POST_COMMENT_COUNT + "] TEXT,[" +
                DataBaseStrings.POST_VIEW_COUNT + "] TEXT,[" +
                DataBaseStrings.POST_SHARE_COUNT + "] TEXT,[" +
                DataBaseStrings.POST_CREATOR_FNAME + "] TEXT,[" +
                DataBaseStrings.POST_CREATOR_LNAME + "] TEXT,[" +
                DataBaseStrings.POST_DELETE_STATUS + "] TEXT,[" +
                DataBaseStrings.POST_DATE + "] TEXT,[" +
                DataBaseStrings.POST_SUGGESTED_ARTICLES + "] TEXT);");


        db.execSQL("CREATE TABLE [" + DataBaseStrings.POST_COMMENT + "] ([" +
                DataBaseStrings.POST_COMMENT_ID + "] TEXT,[" +
                DataBaseStrings.POST_DATE_OF_COMMENT + "] TEXT,[" +
                DataBaseStrings.POST_COMMENT_USER_ID + "] TEXT,[" +
                DataBaseStrings.POST_COMENT_ARTICLE_NAME + "] TEXT,[" +
                DataBaseStrings.POST_COMMENT_ARTICLE_ID + "] TEXT,[" +
                DataBaseStrings.POST_COMMENT_USER_PHONE + "] TEXT,[" +
                DataBaseStrings.POST_COMMENT_DETAIL + "] TEXT,[" +
                DataBaseStrings.POST_COMMENT_USER_NAME + "] TEXT,[" +
                DataBaseStrings.POST_COMMENT_STATUS + "] TEXT,[" +
                DataBaseStrings.POST_COMMENT_DELETE_STATUS + "] TEXT,[" +
                DataBaseStrings.POST_COMMENT_USER_IMAGE_URL + "] TEXT,[" +
                DataBaseStrings.POST_COMMENT_USER_IMAGE + "] BLOB);");

        db.execSQL("CREATE TABLE [" + DataBaseStrings.POST_DOWNLOAD + "] ([" +
                DataBaseStrings.POST_ID + "] TEXT,[" +
                DataBaseStrings.POST_USER_ID + "] TEXT,[" +
                DataBaseStrings.POST_TAG + "] TEXT,[" +
                DataBaseStrings.DATA_PATH_URL + "] TEXT,[" +
                DataBaseStrings.DATA_PATH_DEVICE + "] TEXT,[" +
                DataBaseStrings.MEDIA_NAME + "] TEXT,[" +
                DataBaseStrings.MEDIA_INDEX + "] INTEGER DEFAULT 0);");

        db.execSQL("CREATE TABLE [" + DataBaseStrings.FRIENDS + "] ([" +
                DataBaseStrings.FRIENDS_ID_IN_DEVICE_STORAGE + "] TEXT,[" +
                DataBaseStrings.FRIENDS_NAME + "] TEXT,[" +
                DataBaseStrings.FRIENDS_MOBILE_NUMBER + "] TEXT,[" +
                DataBaseStrings.FRIENDS_IMAGE_URI + "] TEXT,[" +
                DataBaseStrings.FRIENDS_IMAGE + "] TEXT,[" +
                DataBaseStrings.FRIENDS_ID_ON_SERVER + "] TEXT,[" +
                DataBaseStrings.INVITE_STATUS + "] TEXT);");

        db.execSQL("CREATE TABLE [" + DataBaseStrings.SAVED_POST + "] ([" +
                DataBaseStrings.POST_ID + "] TEXT,[" +
                DataBaseStrings.POST_HEADLINE + "] TEXT,[" +
                DataBaseStrings.POST_DETAIL + "] TEXT,[" +
                DataBaseStrings.POST_SECTION + "] TEXT,[" +
                DataBaseStrings.POST_CATEGORY + "] TEXT,[" +
                DataBaseStrings.POST_SUBCATEGORY + "] TEXT,[" +
                DataBaseStrings.POST_IMAGE + "] TEXT);");

        db.execSQL("CREATE TABLE [" + DataBaseStrings.NOTIFICATION + "] ([" +
                DataBaseStrings.NOTIFICATION_ID + "] TEXT,[" +
                DataBaseStrings.NOTIFICATION_TITLE + "] TEXT,[" +
                DataBaseStrings.NOTIFICATION_MESSAGE + "] TEXT,[" +
                DataBaseStrings.NOTIFICATION_TIME + "] TEXT);");
    }

    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    /*
  *
  * Upgrade data base
  *
  * */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS [" + DataBaseStrings.SECTION + "];");
        db.execSQL("DROP TABLE IF EXISTS [" + DataBaseStrings.CATEGORY + "];");
        db.execSQL("DROP TABLE IF EXISTS [" + DataBaseStrings.SUBCATEGORY + "];");
        db.execSQL("DROP TABLE IF EXISTS [" + DataBaseStrings.POST_MAIN + "];");
        db.execSQL("DROP TABLE IF EXISTS [" + DataBaseStrings.POST_COMMENT + "];");
        db.execSQL("DROP TABLE IF EXISTS [" + DataBaseStrings.POST_DOWNLOAD + "];");
        db.execSQL("DROP TABLE IF EXISTS [" + DataBaseStrings.FRIENDS + "];");
        db.execSQL("DROP TABLE IF EXISTS [" + DataBaseStrings.SAVED_POST + "];");
        db.execSQL("DROP TABLE IF EXISTS [" + DataBaseStrings.NOTIFICATION + "];");
        onCreate(db);
    }


    /*------------Total Section Count---------------------*/
    public int getSectionCount() {
        int count;
        String countQuery = "SELECT * FROM " + DataBaseStrings.SECTION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();
        return count;
    }

    /*------------Total Category Count---------------------*/
    public int getCategoryCount() {
        int count;
        String countQuery = "SELECT * FROM " + DataBaseStrings.CATEGORY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();
        return count;
    }

    /*------------Total Sub Category Count---------------------*/
    public int getSubCategoryCount() {
        int count;
        String countQuery = "SELECT * FROM " + DataBaseStrings.SUBCATEGORY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();
        return count;
    }

    /*------------Total Article Count---------------------*/
    public int getPostCount() {
        int count;
        String countQuery = "SELECT * FROM " + DataBaseStrings.POST_MAIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();
        return count;
    }

    /*------------Total Comment Count---------------------*/
    public int getPostCommentCount() {
        int count;
        String countQuery = "SELECT * FROM " + DataBaseStrings.POST_COMMENT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();
        return count;
    }

    /*------------Total Comment of a single user---------------------*/
    public int getCommentCount(String post_id) {
        int count;
        String countQuery = "SELECT * FROM " + DataBaseStrings.POST_COMMENT + " WHERE  " + DataBaseStrings.POST_COMMENT_ARTICLE_ID + " = ?";
        String[] args = {post_id};
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, args);
        count = cursor.getCount();
        cursor.close();
        return count;
    }

    /*------------Total Download of a post---------------------*/
    public int getDownloadCount(String post_id, String media_name)
    {
        int count;
        String countQuery = "SELECT * FROM " + DataBaseStrings.POST_DOWNLOAD + " WHERE  " + DataBaseStrings.POST_ID + "=?"+ "AND "+DataBaseStrings.MEDIA_NAME+"=?" ;
        String[] args = {post_id,media_name};
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, args);
        count = cursor.getCount();
        cursor.close();
        return count;
    }

    /*------------Total Download ---------------------*/
    public int getAllDownloadCount()
    {
        int count;
        String countQuery = "SELECT * FROM " + DataBaseStrings.POST_DOWNLOAD;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();
        return count;
    }

    /*------------Total Friend Count ---------------------*/
    public int getFriendCount() {
        int count;
        String countQuery = "SELECT * FROM " + DataBaseStrings.FRIENDS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();
        return count;
    }


    /*------------Total My Post Count ---------------------*/
    public int getMyPostCount()
    {
        int count;
        String countQuery = "SELECT * FROM " + DataBaseStrings.SAVED_POST;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();
        return count;
    }

    /*------------Total Referral friend count ---------------------*/
    public int getRefFriendCount(String usr_id) {
        int count;
        String countQuery = "SELECT * FROM " + DataBaseStrings.FRIENDS + " WHERE  " + DataBaseStrings.FRIENDS_ID_ON_SERVER + " = ?";
        String[] args = {usr_id};
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, args);
        count = cursor.getCount();
        cursor.close();
        return count;
    }

    /*Database Empty*/
    public void clearDataBase() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(DataBaseStrings.SECTION, null, null);
        db.delete(DataBaseStrings.CATEGORY, null, null);
        db.delete(DataBaseStrings.SUBCATEGORY, null, null);
    }

    public void clearPostData() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(DataBaseStrings.POST_MAIN, null, null);
        db.delete(DataBaseStrings.POST_COMMENT, null, null);
    }

    public void clearMyPost()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(DataBaseStrings.SAVED_POST, null, null);
    }

    public void clearNotification()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(DataBaseStrings.NOTIFICATION, null, null);
    }

    public void clearFriend() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(DataBaseStrings.FRIENDS, null, null);
    }

    public void logout() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(DataBaseStrings.POST_MAIN, null, null);
        db.delete(DataBaseStrings.POST_COMMENT, null, null);
        db.delete(DataBaseStrings.SAVED_POST, null, null);

        db.delete(DataBaseStrings.SECTION, null, null);
        db.delete(DataBaseStrings.CATEGORY, null, null);
        db.delete(DataBaseStrings.SUBCATEGORY, null, null);
        db.delete(DataBaseStrings.FRIENDS,null,null);
        //db.delete(DataBaseStrings.POST_DOWNLOAD,null,null);
    }

    /*Store Data in tables*/

    public boolean storeSection(List<SectionModel> sectionList) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (SectionModel sec : sectionList) {
            ContentValues values = new ContentValues();
            values.put(DataBaseStrings.SECTION_ID, sec.get_id());
            values.put(DataBaseStrings.POST_ITEM_ID, sec.getItem_id());
            values.put(DataBaseStrings.SECTION_NAME, sec.getSectionName());
            values.put(DataBaseStrings.SECTION_LANGUAGE, sec.getLanguage());
            values.put(DataBaseStrings.SECTION_IMAGE, sec.getThumbnailImage());
            values.put(DataBaseStrings.SECTION_HORIZONTAL_IMAGE, sec.getHorigontalImage());
            values.put(DataBaseStrings.SECTION_LAYOUT_TYPE, sec.getSection_lay_type());
            values.put(DataBaseStrings.SECTION_ORDER, sec.getOrder_no());
            db.insert(DataBaseStrings.SECTION, null, values);
        }
        db.close();
        return true;
    }

    public boolean storeCategory(List<CategoryModel> categoryList) {
        Alert.showLog(TAG, "storeCategory- " + categoryList.size());
        SQLiteDatabase db = getWritableDatabase();
        for (CategoryModel cat : categoryList) {
            ContentValues values = new ContentValues();
            values.put(DataBaseStrings.CATEGORY_ID, cat.getCategory_id());
            values.put(DataBaseStrings.POST_ITEM_ID, cat.getItem_id());
            values.put(DataBaseStrings.CATEGORY_NAME, cat.getCategoryName());
            values.put(DataBaseStrings.CATEGORY_LANGUAGE, cat.getCategory_language());
            values.put(DataBaseStrings.CATEGORY_IMAGE, cat.getThumbnaillImage());
            values.put(DataBaseStrings.CATEGORY_SECTION_ID, cat.getCategory_sectionId());
            values.put(DataBaseStrings.CATEGORY_SECTION_NAME, cat.getSectionnName());
            values.put(DataBaseStrings.CATEGORY_HORIZONTAL_IMAGE, cat.getHorigontallImage());
            values.put(DataBaseStrings.CATEGORY_FORMAT, cat.getCategoryFormat());
            values.put(DataBaseStrings.LIST_VIEW_FORMAT, cat.getListViewFormat());
            db.insert(DataBaseStrings.CATEGORY, null, values);
        }
        db.close();
        return true;
    }

    public boolean storeSubCategory(List<SubCategoryModel> subCategoryList) {
        Alert.showLog(TAG, "storeSubCategory- " + subCategoryList.size());
        SQLiteDatabase db = getWritableDatabase();
        for (SubCategoryModel sub_cat : subCategoryList) {
            ContentValues values = new ContentValues();
            values.put(DataBaseStrings.SUB_CATEGORY_ID, sub_cat.getSub_category_id());
            values.put(DataBaseStrings.POST_ITEM_ID, sub_cat.getItem_id());
            values.put(DataBaseStrings.SUB_CATEGORY_NAME, sub_cat.getSub_category_Name());
            values.put(DataBaseStrings.SUB_CATEGORY_LANGUAGE, sub_cat.getSub_category_languagee());
            values.put(DataBaseStrings.SUB_CATEGORY_IMAGE, sub_cat.getSub_category_thumbnaillImage());
            values.put(DataBaseStrings.SUB_CATEGORY_CATEGORY_ID, sub_cat.getSub_category_categoryId());
            values.put(DataBaseStrings.SUB_CATEGORY_CATEGORY_NAME, sub_cat.getSub_category_categoryName());
            values.put(DataBaseStrings.SUB_CATEGORY_SECTION_ID, sub_cat.getSub_category_sectionId());
            values.put(DataBaseStrings.SUB_CATEGORY_SECTION_NAME, sub_cat.getSub_category_sectionName());
            values.put(DataBaseStrings.SUB_CATEGORY_HORIZONTAL_IMAGE, sub_cat.getSub_category_horigontalImage());
            values.put(DataBaseStrings.SUB_CATEGORY_FORMAT, sub_cat.getSubCategoryFormat());
            db.insert(DataBaseStrings.SUBCATEGORY, null, values);
        }
        db.close();
        return true;
    }

    public boolean storeAllPost(List<ContentListingModel> post_data)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        for (ContentListingModel post_d : post_data)
        {
            ContentValues values = new ContentValues();
            values.put(DataBaseStrings.POST_ID, post_d.getContentId());
            values.put(DataBaseStrings.POST_ITEM_ID, post_d.getItem_id());
            values.put(DataBaseStrings.POST_HEADLINE, post_d.getHeadline());
            values.put(DataBaseStrings.POST_SLUG, post_d.getSlug());
            values.put(DataBaseStrings.POST_TAGLINE, post_d.getTagline());
            values.put(DataBaseStrings.POST_LANGUAGE, post_d.getLanguage());
            values.put(DataBaseStrings.POST_IMAGE, post_d.getFirstImage());
            values.put(DataBaseStrings.POST_HORIZENTIAL_IMAGE, post_d.getHorizontalPicture());
            values.put(DataBaseStrings.POST_SECTION, post_d.getSectionName());
            values.put(DataBaseStrings.POST_CATEGORY, post_d.getCategoryName());
            values.put(DataBaseStrings.POST_SUBCATEGORY, post_d.getSubCategoryName());
            values.put(DataBaseStrings.POST_SECTION_ID, post_d.getSectionId());
            values.put(DataBaseStrings.POST_CATEGORY_ID, post_d.getCategoryId());
            values.put(DataBaseStrings.POST_SUBCATEGORY_ID, post_d.getSubCategoryId());
            values.put(DataBaseStrings.POST_LIKE, post_d.getLike());
            values.put(DataBaseStrings.POST_SAVE, post_d.getSave());
            values.put(DataBaseStrings.POST_LIKE_COUNT, post_d.getTotal_like());
            values.put(DataBaseStrings.POST_COMMENT_COUNT, post_d.getComment_count());
            values.put(DataBaseStrings.POST_VIEW_COUNT, post_d.getView_count());
            values.put(DataBaseStrings.POST_SHARE_COUNT, post_d.getShare_count());
            values.put(DataBaseStrings.POST_CREATOR_FNAME, post_d.getCreator_first_name());
            values.put(DataBaseStrings.POST_CREATOR_LNAME, post_d.getCreator_last_name());
            values.put(DataBaseStrings.POST_DATE, post_d.getDateOfCreation());
            values.put(DataBaseStrings.POST_DELETE_STATUS, post_d.getDeleteStatus());
            values.put(DataBaseStrings.POST_SUGGESTED_ARTICLES,post_d.getSuggestedArticleList());
            db.insert(DataBaseStrings.POST_MAIN, null, values);
        }
        return true;
    }

    public boolean storeAllPostComments(List<CommentListModel> comments) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (CommentListModel post_d : comments)
        {
            if(post_d.getDeleteStatus().equalsIgnoreCase("false"))
            {
                ContentValues values = new ContentValues();
                values.put(DataBaseStrings.POST_COMMENT_ID, post_d.getComment_id());
                values.put(DataBaseStrings.POST_DATE_OF_COMMENT, post_d.getDateOfComment());
                values.put(DataBaseStrings.POST_COMMENT_USER_ID, post_d.getUserId());
                values.put(DataBaseStrings.POST_COMENT_ARTICLE_NAME, post_d.getArticleName());
                values.put(DataBaseStrings.POST_COMMENT_ARTICLE_ID, post_d.getArticleId());
                values.put(DataBaseStrings.POST_COMMENT_USER_PHONE, post_d.getUserPhone());
                values.put(DataBaseStrings.POST_COMMENT_DETAIL, post_d.getUserComment());
                values.put(DataBaseStrings.POST_COMMENT_USER_NAME, post_d.getUserName());
                values.put(DataBaseStrings.POST_COMMENT_STATUS, post_d.getStatus());
                values.put(DataBaseStrings.POST_COMMENT_DELETE_STATUS, post_d.getDeleteStatus());
                values.put(DataBaseStrings.POST_COMMENT_USER_IMAGE_URL, post_d.getUser_image());
                db.insert(DataBaseStrings.POST_COMMENT, null, values);
            }
        }
        return true;
    }

    public boolean downloadPost(String POST_ID, String POST_TAG, String DATA_PATH_URL, String DATA_PATH_DEVICE, String MEDIA_NAME, String POST_USER_ID, int INDEX)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DataBaseStrings.POST_USER_ID, POST_USER_ID);
        values.put(DataBaseStrings.POST_ID, POST_ID);
        values.put(DataBaseStrings.POST_TAG, POST_TAG);
        values.put(DataBaseStrings.DATA_PATH_URL, DATA_PATH_URL);
        values.put(DataBaseStrings.DATA_PATH_DEVICE, DATA_PATH_DEVICE);
        values.put(DataBaseStrings.MEDIA_NAME, MEDIA_NAME);
        values.put(DataBaseStrings.MEDIA_INDEX, INDEX);
        db.insert(DataBaseStrings.POST_DOWNLOAD, null, values);
        db.close();
        return true;
    }

    public boolean storeFriendList(List<ContactsModel> friend_list)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        for (ContactsModel sec : friend_list) {
            ContentValues values = new ContentValues();
            values.put(DataBaseStrings.FRIENDS_ID_IN_DEVICE_STORAGE, sec.getDevice_id());
            values.put(DataBaseStrings.FRIENDS_NAME, sec.getName());
            values.put(DataBaseStrings.FRIENDS_MOBILE_NUMBER, sec.getNumber());
            values.put(DataBaseStrings.FRIENDS_IMAGE_URI, sec.getImageuri());
            values.put(DataBaseStrings.FRIENDS_IMAGE, sec.getImage());
            values.put(DataBaseStrings.FRIENDS_ID_ON_SERVER, sec.getServer_id());
            values.put(DataBaseStrings.INVITE_STATUS, sec.getInviteStatus());
            db.insert(DataBaseStrings.FRIENDS, null, values);
        }
       return true;
    }

    public boolean addRefferalFriend(String name,String number,String inviteStatus,String image,String server_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DataBaseStrings.FRIENDS_ID_IN_DEVICE_STORAGE, DataBaseStrings.DEFAULT_VALUE);
        values.put(DataBaseStrings.FRIENDS_NAME, name);
        values.put(DataBaseStrings.FRIENDS_MOBILE_NUMBER, number);
        values.put(DataBaseStrings.FRIENDS_IMAGE_URI, DataBaseStrings.DEFAULT_VALUE);
        values.put(DataBaseStrings.FRIENDS_IMAGE, image);
        values.put(DataBaseStrings.FRIENDS_ID_ON_SERVER, server_id);
        values.put(DataBaseStrings.INVITE_STATUS, inviteStatus);
        db.insert(DataBaseStrings.FRIENDS, null, values);
        return true;
    }

    public boolean storeMyPost(List<ContentListingModel> post_data)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        for (ContentListingModel post_d : post_data)
        {
            ContentValues values = new ContentValues();
            values.put(DataBaseStrings.POST_ID, post_d.getContentId());
            values.put(DataBaseStrings.POST_HEADLINE, post_d.getHeadline());
            values.put(DataBaseStrings.POST_DETAIL, post_d.getDescription());
            values.put(DataBaseStrings.POST_SECTION, post_d.getSectionName());
            values.put(DataBaseStrings.POST_CATEGORY, post_d.getCategoryName());
            values.put(DataBaseStrings.POST_SUBCATEGORY, post_d.getSubCategoryName());
            values.put(DataBaseStrings.POST_IMAGE, post_d.getFirstImage());
            db.insert(DataBaseStrings.SAVED_POST, null, values);
        }
        db.close();
        return true;
    }

    public boolean storeNotificationList(List<NotificationModel> notification_list)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        for (NotificationModel itm : notification_list) {
            ContentValues values = new ContentValues();
            values.put(DataBaseStrings.NOTIFICATION_ID, itm.getNotification_id());
            values.put(DataBaseStrings.NOTIFICATION_TITLE, itm.getTitle());
            values.put(DataBaseStrings.NOTIFICATION_MESSAGE, itm.getDetail());
            values.put(DataBaseStrings.NOTIFICATION_TIME, itm.getDate());
            db.insert(DataBaseStrings.NOTIFICATION, null, values);
        }
        return true;
    }

    /*Retrieve Data from tables*/
    public List<SectionModel> getAllSections() {
        List<SectionModel> section_list = new ArrayList<>();
        try {
            String selectQuery = "SELECT  * FROM " + DataBaseStrings.SECTION;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            if (c != null && c.moveToFirst()) {
                do {
                    section_list.add(new SectionModel(c.getString(c.getColumnIndex(DataBaseStrings.SECTION_ID)),
                            c.getString(c.getColumnIndex(DataBaseStrings.SECTION_NAME)),
                            c.getString(c.getColumnIndex(DataBaseStrings.SECTION_LANGUAGE)),
                            c.getString(c.getColumnIndex(DataBaseStrings.SECTION_IMAGE)),
                            c.getString(c.getColumnIndex(DataBaseStrings.SECTION_HORIZONTAL_IMAGE)),
                            c.getString(c.getColumnIndex(DataBaseStrings.SECTION_LAYOUT_TYPE)),
                            c.getInt(c.getColumnIndex(DataBaseStrings.SECTION_ORDER)),
                            c.getString(c.getColumnIndex(DataBaseStrings.POST_ITEM_ID))));
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return section_list;
    }

    public List<CategoryModel> getAllCategory(String section_id) {
        List<CategoryModel> category_list = new ArrayList<>();
        try {
            String selectQuery = "SELECT * FROM " + DataBaseStrings.CATEGORY + " WHERE  " + DataBaseStrings.CATEGORY_SECTION_ID + " = ?";
            String[] args = {section_id};
            Cursor c = db.rawQuery(selectQuery, args);
            if (c != null && c.moveToFirst()) {
                do {
                    category_list.add(new CategoryModel(c.getString(c.getColumnIndex(DataBaseStrings.CATEGORY_ID)),
                            c.getString(c.getColumnIndex(DataBaseStrings.CATEGORY_LANGUAGE)),
                            c.getString(c.getColumnIndex(DataBaseStrings.CATEGORY_SECTION_ID)),
                            c.getString(c.getColumnIndex(DataBaseStrings.CATEGORY_NAME)),
                            c.getString(c.getColumnIndex(DataBaseStrings.CATEGORY_SECTION_NAME)),
                            c.getString(c.getColumnIndex(DataBaseStrings.CATEGORY_IMAGE)),
                            c.getString(c.getColumnIndex(DataBaseStrings.CATEGORY_HORIZONTAL_IMAGE)),
                            c.getString(c.getColumnIndex(DataBaseStrings.CATEGORY_FORMAT)),
                            c.getString(c.getColumnIndex(DataBaseStrings.LIST_VIEW_FORMAT)),
                            c.getString(c.getColumnIndex(DataBaseStrings.POST_ITEM_ID))
                    ));
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return category_list;
    }

    public List<SubCategoryModel> getAllSubCategory(String section_id, String category_id) {
        List<SubCategoryModel> sub_category_list = new ArrayList<>();
        try {
            String selectQuery = "SELECT * FROM " + DataBaseStrings.SUBCATEGORY + " WHERE  " + DataBaseStrings.SUB_CATEGORY_CATEGORY_ID + " = ?";
            String[] args = {category_id};
            Cursor c = db.rawQuery(selectQuery, args);
            if (c != null && c.moveToFirst()) {
                do {
                    Alert.showLog(TAG, "AYI_CATEG- " + c.getString(c.getColumnIndex(DataBaseStrings.SUB_CATEGORY_NAME)));
                    sub_category_list.add(new SubCategoryModel(c.getString(c.getColumnIndex(DataBaseStrings.SUB_CATEGORY_ID)),
                            c.getString(c.getColumnIndex(DataBaseStrings.SUB_CATEGORY_LANGUAGE)),
                            c.getString(c.getColumnIndex(DataBaseStrings.SUB_CATEGORY_SECTION_ID)),
                            c.getString(c.getColumnIndex(DataBaseStrings.SUB_CATEGORY_CATEGORY_ID)),
                            c.getString(c.getColumnIndex(DataBaseStrings.SUB_CATEGORY_NAME)),
                            c.getString(c.getColumnIndex(DataBaseStrings.SUB_CATEGORY_SECTION_NAME)),
                            c.getString(c.getColumnIndex(DataBaseStrings.SUB_CATEGORY_CATEGORY_NAME)),
                            c.getString(c.getColumnIndex(DataBaseStrings.SUB_CATEGORY_IMAGE)),
                            c.getString(c.getColumnIndex(DataBaseStrings.SUB_CATEGORY_HORIZONTAL_IMAGE)),
                            c.getString(c.getColumnIndex(DataBaseStrings.SUB_CATEGORY_FORMAT)),
                            c.getString(c.getColumnIndex(DataBaseStrings.POST_ITEM_ID))
                    ));

                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sub_category_list;
    }

    public List<SubCategoryModel> getAllSubCategory() {
        List<SubCategoryModel> sub_category_list = new ArrayList<>();
        try {
            String selectQuery = "SELECT * FROM " + DataBaseStrings.SUBCATEGORY;
            // SQLiteDatabase db = this.getWritableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            if (c != null && c.moveToFirst()) {
                do {
                    Alert.showLog(TAG, "AYI_CATEG- " + c.getString(c.getColumnIndex(DataBaseStrings.SUB_CATEGORY_ID)));
                    Alert.showLog(TAG, "AYI_CATEG- " + c.getString(c.getColumnIndex(DataBaseStrings.SUB_CATEGORY_LANGUAGE)));
                    Alert.showLog(TAG, "AYI_CATEG- " + c.getString(c.getColumnIndex(DataBaseStrings.SUB_CATEGORY_SECTION_ID)));
                    Alert.showLog(TAG, "AYI_CATEG- " + c.getString(c.getColumnIndex(DataBaseStrings.SUB_CATEGORY_CATEGORY_ID)));
                    Alert.showLog(TAG, "AYI_CATEG- " + c.getString(c.getColumnIndex(DataBaseStrings.SUB_CATEGORY_NAME)));
                    Alert.showLog(TAG, "AYI_CATEG- " + c.getString(c.getColumnIndex(DataBaseStrings.SUB_CATEGORY_SECTION_NAME)));
                    Alert.showLog(TAG, "AYI_CATEG- " + c.getString(c.getColumnIndex(DataBaseStrings.SUB_CATEGORY_CATEGORY_NAME)));
                    Alert.showLog(TAG, "AYI_CATEG- " + c.getString(c.getColumnIndex(DataBaseStrings.SUB_CATEGORY_IMAGE)));

                    Alert.showLog(TAG, "AYI_CATEG- -------------------------------------------------------");

                    sub_category_list.add(new SubCategoryModel(c.getString(c.getColumnIndex(DataBaseStrings.SUB_CATEGORY_ID)),
                            c.getString(c.getColumnIndex(DataBaseStrings.SUB_CATEGORY_LANGUAGE)),
                            c.getString(c.getColumnIndex(DataBaseStrings.SUB_CATEGORY_SECTION_ID)),
                            c.getString(c.getColumnIndex(DataBaseStrings.SUB_CATEGORY_CATEGORY_ID)),
                            c.getString(c.getColumnIndex(DataBaseStrings.SUB_CATEGORY_NAME)),
                            c.getString(c.getColumnIndex(DataBaseStrings.SUB_CATEGORY_SECTION_NAME)),
                            c.getString(c.getColumnIndex(DataBaseStrings.SUB_CATEGORY_CATEGORY_NAME)),
                            c.getString(c.getColumnIndex(DataBaseStrings.SUB_CATEGORY_IMAGE)),
                            c.getString(c.getColumnIndex(DataBaseStrings.CATEGORY_HORIZONTAL_IMAGE)),
                            c.getString(c.getColumnIndex(DataBaseStrings.SUB_CATEGORY_FORMAT)),
                            c.getString(c.getColumnIndex(DataBaseStrings.POST_ITEM_ID))
                    ));
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sub_category_list;
    }

    public List<ContentListingModel> getAllPost()
    {
        List<ContentListingModel> contentListingModel = new ArrayList<>();
        try {
            String selectQuery = "SELECT * FROM " + DataBaseStrings.POST_MAIN;
            Cursor c = db.rawQuery(selectQuery, null);
            if (c != null && c.moveToFirst()) {
                do {
                    if(c.getString(c.getColumnIndex(DataBaseStrings.POST_DELETE_STATUS)).equalsIgnoreCase("false"))
                    {
                        ContentListingModel data = new ContentListingModel();
                        data.setContentId(c.getString(c.getColumnIndex(DataBaseStrings.POST_ID)));
                        data.setItem_id(c.getString(c.getColumnIndex(DataBaseStrings.POST_ITEM_ID)));
                        data.setLanguage(c.getString(c.getColumnIndex(DataBaseStrings.POST_LANGUAGE)));
                        data.setSectionId(c.getString(c.getColumnIndex(DataBaseStrings.POST_SECTION_ID)));
                        data.setCategoryId(c.getString(c.getColumnIndex(DataBaseStrings.POST_CATEGORY_ID)));
                        data.setSubCategoryId(c.getString(c.getColumnIndex(DataBaseStrings.POST_SUBCATEGORY_ID)));
                        data.setHeadline(c.getString(c.getColumnIndex(DataBaseStrings.POST_HEADLINE)));
                        data.setSlug(c.getString(c.getColumnIndex(DataBaseStrings.POST_SLUG)));
                        data.setTagline(c.getString(c.getColumnIndex(DataBaseStrings.POST_TAGLINE)));
                        data.setSectionName(c.getString(c.getColumnIndex(DataBaseStrings.POST_SECTION)));
                        data.setCategoryName(c.getString(c.getColumnIndex(DataBaseStrings.POST_CATEGORY)));
                        data.setSubCategoryName(c.getString(c.getColumnIndex(DataBaseStrings.POST_SUBCATEGORY)));
                        data.setFirstImage(c.getString(c.getColumnIndex(DataBaseStrings.POST_IMAGE)));
                        data.setHorizontalPicture(c.getString(c.getColumnIndex(DataBaseStrings.POST_HORIZENTIAL_IMAGE)));
                        data.setLike(c.getString(c.getColumnIndex(DataBaseStrings.POST_LIKE)));//Only to check that item has liked by user or not 1,0
                        data.setSave(c.getString(c.getColumnIndex(DataBaseStrings.POST_SAVE)));//Only to check that item has save by user or not 1,0
                        data.setTotal_like(c.getString(c.getColumnIndex(DataBaseStrings.POST_LIKE_COUNT)));
                        data.setComment_count(c.getString(c.getColumnIndex(DataBaseStrings.POST_COMMENT_COUNT)));
                        data.setView_count(c.getString(c.getColumnIndex(DataBaseStrings.POST_VIEW_COUNT)));
                        data.setShare_count(c.getString(c.getColumnIndex(DataBaseStrings.POST_SHARE_COUNT)));
                        data.setCreator_first_name(c.getString(c.getColumnIndex(DataBaseStrings.POST_CREATOR_FNAME)));
                        data.setCreator_last_name(c.getString(c.getColumnIndex(DataBaseStrings.POST_CREATOR_LNAME)));
                        data.setDateOfCreation(c.getString(c.getColumnIndex(DataBaseStrings.POST_DATE)));
                        data.setDeleteStatus(c.getString(c.getColumnIndex(DataBaseStrings.POST_DELETE_STATUS)));
                        data.setSuggestedArticleList(c.getString(c.getColumnIndex(DataBaseStrings.POST_SUGGESTED_ARTICLES)));
                        contentListingModel.add(data);
                    }
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contentListingModel;
    }

    public List<ContentListingModel> getAllSavePost()
    {
        List<ContentListingModel> contentListingModel = new ArrayList<>();
        try {
            String selectQuery = "SELECT * FROM " + DataBaseStrings.POST_MAIN;
            Cursor c = db.rawQuery(selectQuery, null);
            if (c != null && c.moveToFirst()) {
                do {
                    if(c.getString(c.getColumnIndex(DataBaseStrings.POST_SAVE)).equalsIgnoreCase("1"))
                    {
                        ContentListingModel data = new ContentListingModel();
                        data.setContentId(c.getString(c.getColumnIndex(DataBaseStrings.POST_ID)));
                        data.setLanguage(c.getString(c.getColumnIndex(DataBaseStrings.POST_LANGUAGE)));
                        data.setSectionId(c.getString(c.getColumnIndex(DataBaseStrings.POST_SECTION_ID)));
                        data.setCategoryId(c.getString(c.getColumnIndex(DataBaseStrings.POST_CATEGORY_ID)));
                        data.setSubCategoryId(c.getString(c.getColumnIndex(DataBaseStrings.POST_SUBCATEGORY_ID)));
                        data.setHeadline(c.getString(c.getColumnIndex(DataBaseStrings.POST_HEADLINE)));
                        data.setSlug(c.getString(c.getColumnIndex(DataBaseStrings.POST_SLUG)));
                        data.setTagline(c.getString(c.getColumnIndex(DataBaseStrings.POST_TAGLINE)));
                        data.setSectionName(c.getString(c.getColumnIndex(DataBaseStrings.POST_SECTION)));
                        data.setCategoryName(c.getString(c.getColumnIndex(DataBaseStrings.POST_CATEGORY)));
                        data.setSubCategoryName(c.getString(c.getColumnIndex(DataBaseStrings.POST_SUBCATEGORY)));
                        data.setFirstImage(c.getString(c.getColumnIndex(DataBaseStrings.POST_IMAGE)));
                        data.setHorizontalPicture(c.getString(c.getColumnIndex(DataBaseStrings.POST_HORIZENTIAL_IMAGE)));
                        data.setLike(c.getString(c.getColumnIndex(DataBaseStrings.POST_LIKE)));//Only to check that item has liked by user or not 1,0
                        data.setSave(c.getString(c.getColumnIndex(DataBaseStrings.POST_SAVE)));//Only to check that item has save by user or not 1,0
                        data.setTotal_like(c.getString(c.getColumnIndex(DataBaseStrings.POST_LIKE_COUNT)));
                        data.setComment_count(c.getString(c.getColumnIndex(DataBaseStrings.POST_COMMENT_COUNT)));
                        data.setView_count(c.getString(c.getColumnIndex(DataBaseStrings.POST_VIEW_COUNT)));
                        data.setShare_count(c.getString(c.getColumnIndex(DataBaseStrings.POST_SHARE_COUNT)));
                        data.setCreator_first_name(c.getString(c.getColumnIndex(DataBaseStrings.POST_CREATOR_FNAME)));
                        data.setCreator_last_name(c.getString(c.getColumnIndex(DataBaseStrings.POST_CREATOR_LNAME)));
                        data.setDateOfCreation(c.getString(c.getColumnIndex(DataBaseStrings.POST_DATE)));
                        data.setDeleteStatus(c.getString(c.getColumnIndex(DataBaseStrings.POST_DELETE_STATUS)));
                        data.setSuggestedArticleList(c.getString(c.getColumnIndex(DataBaseStrings.POST_SUGGESTED_ARTICLES)));
                        contentListingModel.add(data);
                    }
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contentListingModel;
    }

    public List<ContentListingModel> getMyPost()
    {
        List<ContentListingModel> contentListingModel = new ArrayList<>();
        try {
            String selectQuery = "SELECT * FROM " + DataBaseStrings.SAVED_POST;
            Cursor c = db.rawQuery(selectQuery, null);
            if (c != null && c.moveToFirst())
            {
                do {
                        ContentListingModel data = new ContentListingModel();
                        data.setContentId(c.getString(c.getColumnIndex(DataBaseStrings.POST_ID)));
                        data.setHeadline(c.getString(c.getColumnIndex(DataBaseStrings.POST_HEADLINE)));
                        data.setSectionName(c.getString(c.getColumnIndex(DataBaseStrings.POST_SECTION)));
                        data.setCategoryName(c.getString(c.getColumnIndex(DataBaseStrings.POST_CATEGORY)));
                        data.setSubCategoryName(c.getString(c.getColumnIndex(DataBaseStrings.POST_SUBCATEGORY)));
                        data.setDescription(c.getString(c.getColumnIndex(DataBaseStrings.POST_DETAIL)));
                        data.setFirstImage(c.getString(c.getColumnIndex(DataBaseStrings.POST_IMAGE)));
                        contentListingModel.add(data);

                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contentListingModel;
    }

    public ContentListingModel getPost(String id)
    {
        ContentListingModel data = new ContentListingModel();
        try
        {
            String selectQuery = "SELECT * FROM " + DataBaseStrings.POST_MAIN + " WHERE  " + DataBaseStrings.POST_ID + " = ?";
            String[] args = {id};
            Cursor c = db.rawQuery(selectQuery, args);
            if (c != null && c.moveToFirst()) {
                do
                {
                    if(c.getString(c.getColumnIndex(DataBaseStrings.POST_DELETE_STATUS)).equalsIgnoreCase("false"))
                    {
                        data.setContentId(c.getString(c.getColumnIndex(DataBaseStrings.POST_ID)));
                        data.setLanguage(c.getString(c.getColumnIndex(DataBaseStrings.POST_LANGUAGE)));
                        data.setSectionId(c.getString(c.getColumnIndex(DataBaseStrings.POST_SECTION_ID)));
                        data.setCategoryId(c.getString(c.getColumnIndex(DataBaseStrings.POST_CATEGORY_ID)));
                        data.setSubCategoryId(c.getString(c.getColumnIndex(DataBaseStrings.POST_SUBCATEGORY_ID)));
                        data.setHeadline(c.getString(c.getColumnIndex(DataBaseStrings.POST_HEADLINE)));
                        data.setSlug(c.getString(c.getColumnIndex(DataBaseStrings.POST_SLUG)));
                        data.setTagline(c.getString(c.getColumnIndex(DataBaseStrings.POST_TAGLINE)));
                        data.setSectionName(c.getString(c.getColumnIndex(DataBaseStrings.POST_SECTION)));
                        data.setCategoryName(c.getString(c.getColumnIndex(DataBaseStrings.POST_CATEGORY)));
                        data.setSubCategoryName(c.getString(c.getColumnIndex(DataBaseStrings.POST_SUBCATEGORY)));
                        data.setFirstImage(c.getString(c.getColumnIndex(DataBaseStrings.POST_IMAGE)));
                        data.setHorizontalPicture(c.getString(c.getColumnIndex(DataBaseStrings.POST_HORIZENTIAL_IMAGE)));
                        data.setLike(c.getString(c.getColumnIndex(DataBaseStrings.POST_LIKE)));//Only to check that item has liked by user or not 1,0
                        data.setSave(c.getString(c.getColumnIndex(DataBaseStrings.POST_SAVE)));//Only to check that item has save by user or not 1,0
                        data.setTotal_like(c.getString(c.getColumnIndex(DataBaseStrings.POST_LIKE_COUNT)));
                        data.setComment_count(c.getString(c.getColumnIndex(DataBaseStrings.POST_COMMENT_COUNT)));
                        data.setView_count(c.getString(c.getColumnIndex(DataBaseStrings.POST_VIEW_COUNT)));
                        data.setShare_count(c.getString(c.getColumnIndex(DataBaseStrings.POST_SHARE_COUNT)));
                        data.setCreator_first_name(c.getString(c.getColumnIndex(DataBaseStrings.POST_CREATOR_FNAME)));
                        data.setCreator_last_name(c.getString(c.getColumnIndex(DataBaseStrings.POST_CREATOR_LNAME)));
                        data.setDateOfCreation(c.getString(c.getColumnIndex(DataBaseStrings.POST_DATE)));
                        data.setDeleteStatus(c.getString(c.getColumnIndex(DataBaseStrings.POST_DELETE_STATUS)));
                        data.setSuggestedArticleList(c.getString(c.getColumnIndex(DataBaseStrings.POST_SUGGESTED_ARTICLES)));
                    }
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public List<CommentListModel> getPostComments(String postId)
    {
        List<CommentListModel> comment_list = new ArrayList<>();
        try {
            String selectQuery = "SELECT * FROM " + DataBaseStrings.POST_COMMENT + " WHERE  " + DataBaseStrings.POST_COMMENT_ARTICLE_ID + " = ?";
            String[] args = {postId};
            Cursor c = db.rawQuery(selectQuery, args);
            if (c != null && c.moveToFirst()) {
                do {
                    CommentListModel des_dat = new CommentListModel();
                    des_dat.setComment_id(c.getString(c.getColumnIndex(DataBaseStrings.POST_COMMENT_ID)));
                    des_dat.setDateOfComment(c.getString(c.getColumnIndex(DataBaseStrings.POST_DATE_OF_COMMENT)));
                    des_dat.setUserId(c.getString(c.getColumnIndex(DataBaseStrings.POST_COMMENT_USER_ID)));
                    des_dat.setArticleName(c.getString(c.getColumnIndex(DataBaseStrings.POST_COMENT_ARTICLE_NAME)));
                    des_dat.setArticleId(c.getString(c.getColumnIndex(DataBaseStrings.POST_COMMENT_ARTICLE_ID)));
                    des_dat.setUserPhone(c.getString(c.getColumnIndex(DataBaseStrings.POST_COMMENT_USER_PHONE)));
                    des_dat.setUserComment(c.getString(c.getColumnIndex(DataBaseStrings.POST_COMMENT_DETAIL)));
                    des_dat.setUserName(c.getString(c.getColumnIndex(DataBaseStrings.POST_COMMENT_USER_NAME)));
                    des_dat.setStatus(c.getString(c.getColumnIndex(DataBaseStrings.POST_COMMENT_STATUS)));
                    des_dat.setDeleteStatus(c.getString(c.getColumnIndex(DataBaseStrings.POST_COMMENT_DELETE_STATUS)));
                    des_dat.setUser_image(c.getString(c.getColumnIndex(DataBaseStrings.POST_COMMENT_USER_IMAGE_URL)));
                    comment_list.add(des_dat);
                }
                while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Alert.showLog("getPostComments","getPostComments SIZE--  "+comment_list.size());

        return comment_list;
    }

    public List<DownloadedData> getAllDownloads(String user_id)
    {
        List<DownloadedData> datas = new ArrayList<>();
        try {
            String selectQuery = "SELECT * FROM " + DataBaseStrings.POST_DOWNLOAD + " WHERE  " + DataBaseStrings.POST_USER_ID + " = ?";
            String[] args = {user_id};;
            Cursor c = db.rawQuery(selectQuery, args);
            if (c != null && c.moveToFirst())
            {
                do
                {
                    datas.add(new DownloadedData(c.getString(c.getColumnIndex(DataBaseStrings.POST_ID)),c.getString(c.getColumnIndex(DataBaseStrings.POST_TAG)),
                            c.getString(c.getColumnIndex(DataBaseStrings.DATA_PATH_URL)),c.getString(c.getColumnIndex(DataBaseStrings.DATA_PATH_DEVICE)),
                            c.getString(c.getColumnIndex(DataBaseStrings.MEDIA_NAME)),c.getString(c.getColumnIndex(DataBaseStrings.MEDIA_INDEX))
                            ,c.getString(c.getColumnIndex(DataBaseStrings.POST_USER_ID))));
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       return datas;
    }

    public String getPostLike(String postId) {
        String val = DataBaseStrings.DEFAULT_VALUE;
        try {
            String selectQuery = "SELECT * FROM " + DataBaseStrings.POST_MAIN + " WHERE  " + DataBaseStrings.POST_ID + " = ?";
            String[] args = {postId};
            Cursor c = db.rawQuery(selectQuery, args);
            if (c != null && c.moveToFirst()) {
                do {
                    val = c.getString(c.getColumnIndex(DataBaseStrings.POST_LIKE));
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return val;
    }

    public String getPostSave(String postId) {
        String val = DataBaseStrings.DEFAULT_VALUE;
        try {
            String selectQuery = "SELECT * FROM " + DataBaseStrings.POST_MAIN + " WHERE  " + DataBaseStrings.POST_ID + " = ?";
            String[] args = {postId};
            Cursor c = db.rawQuery(selectQuery, args);
            if (c != null && c.moveToFirst()) {
                do {
                    val = c.getString(c.getColumnIndex(DataBaseStrings.POST_SAVE));
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return val;
    }

    public String getPostLikeCount(String postId) {
        String val = DataBaseStrings.DEFAULT_VALUE;
        try {
            String selectQuery = "SELECT * FROM " + DataBaseStrings.POST_MAIN + " WHERE  " + DataBaseStrings.POST_ID + " = ?";
            String[] args = {postId};
            Cursor c = db.rawQuery(selectQuery, args);
            if (c != null && c.moveToFirst()) {
                do {
                    val = c.getString(c.getColumnIndex(DataBaseStrings.POST_LIKE_COUNT));
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return val;
    }

    public String getPostCommentCount(String postId) {
        String val = DataBaseStrings.DEFAULT_VALUE;
        try {
            String selectQuery = "SELECT * FROM " + DataBaseStrings.POST_MAIN + " WHERE  " + DataBaseStrings.POST_ID + " = ?";
            String[] args = {postId};
            Cursor c = db.rawQuery(selectQuery, args);
            if (c != null && c.moveToFirst()) {
                do {
                    val = c.getString(c.getColumnIndex(DataBaseStrings.POST_COMMENT_COUNT));
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return val;
    }

    public String getPostSaveCount(String postId) {
        String val = DataBaseStrings.DEFAULT_VALUE;
        try {
            String selectQuery = "SELECT * FROM " + DataBaseStrings.POST_MAIN + " WHERE  " + DataBaseStrings.POST_ID + " = ?";
            String[] args = {postId};
            Cursor c = db.rawQuery(selectQuery, args);
            if (c != null && c.moveToFirst()) {
                do {
                    val = c.getString(c.getColumnIndex(DataBaseStrings.POST_SHARE_COUNT));
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return val;
    }

    public String getPostViewCount(String postId) {
        String val = DataBaseStrings.DEFAULT_VALUE;
        try {
            String selectQuery = "SELECT * FROM " + DataBaseStrings.POST_MAIN + " WHERE  " + DataBaseStrings.POST_ID + " = ?";
            String[] args = {postId};
            Cursor c = db.rawQuery(selectQuery, args);
            if (c != null && c.moveToFirst()) {
                do {
                    val = c.getString(c.getColumnIndex(DataBaseStrings.POST_VIEW_COUNT));
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return val;
    }

    public List<ContactsModel> getInviteFriendList()
    {
        List<ContactsModel> contentListingModel = new ArrayList<>();
        try {
            String selectQuery = "SELECT * FROM " + DataBaseStrings.FRIENDS;
            Cursor c = db.rawQuery(selectQuery, null);
            if (c != null && c.moveToFirst()) {
                do {
                    if(c.getString(c.getColumnIndex(DataBaseStrings.INVITE_STATUS)).equalsIgnoreCase(ContactsModel.INVITE))
                    {
                        contentListingModel.add(new ContactsModel(c.getString(c.getColumnIndex(DataBaseStrings.FRIENDS_ID_IN_DEVICE_STORAGE)),
                                c.getString(c.getColumnIndex(DataBaseStrings.FRIENDS_NAME)),
                                c.getString(c.getColumnIndex(DataBaseStrings.FRIENDS_MOBILE_NUMBER)),
                                c.getString(c.getColumnIndex(DataBaseStrings.FRIENDS_IMAGE_URI)),
                                c.getString(c.getColumnIndex(DataBaseStrings.FRIENDS_IMAGE)),
                                c.getString(c.getColumnIndex(DataBaseStrings.INVITE_STATUS)),
                                c.getString(c.getColumnIndex(DataBaseStrings.FRIENDS_ID_ON_SERVER))));
                    }
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contentListingModel;
    }

    public List<ContactsModel> getNetparFriendList()
    {
        List<ContactsModel> contentListingModel = new ArrayList<>();
        try {
            String selectQuery = "SELECT * FROM " + DataBaseStrings.FRIENDS;
            Cursor c = db.rawQuery(selectQuery, null);
            if (c != null && c.moveToFirst()) {
                do {
                    if(c.getString(c.getColumnIndex(DataBaseStrings.INVITE_STATUS)).equalsIgnoreCase(ContactsModel.FRIEND))
                    {
                        contentListingModel.add(new ContactsModel(c.getString(c.getColumnIndex(DataBaseStrings.FRIENDS_ID_IN_DEVICE_STORAGE)),
                                c.getString(c.getColumnIndex(DataBaseStrings.FRIENDS_NAME)),
                                c.getString(c.getColumnIndex(DataBaseStrings.FRIENDS_MOBILE_NUMBER)),
                                c.getString(c.getColumnIndex(DataBaseStrings.FRIENDS_IMAGE_URI)),
                                c.getString(c.getColumnIndex(DataBaseStrings.FRIENDS_IMAGE)),
                                c.getString(c.getColumnIndex(DataBaseStrings.INVITE_STATUS)),
                                c.getString(c.getColumnIndex(DataBaseStrings.FRIENDS_ID_ON_SERVER))));
                    }
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contentListingModel;
    }

    public List<NotificationModel> getAllNotification()
    {
        List<NotificationModel> notificationListingModel = new ArrayList<>();
        try {
            String selectQuery = "SELECT * FROM " + DataBaseStrings.NOTIFICATION;
            Cursor c = db.rawQuery(selectQuery, null);
            if (c != null && c.moveToFirst()) {
                do
                {
                    notificationListingModel.add(new NotificationModel(c.getString(c.getColumnIndex(DataBaseStrings.NOTIFICATION_ID)),
                            c.getString(c.getColumnIndex(DataBaseStrings.NOTIFICATION_TITLE)),
                            c.getString(c.getColumnIndex(DataBaseStrings.NOTIFICATION_MESSAGE)),
                            c.getString(c.getColumnIndex(DataBaseStrings.NOTIFICATION_TIME))));

                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return notificationListingModel;
    }

    /*UPDATE DATA*/
    public boolean updatePostImageBlob(String post_id, byte[] imageBytes) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseStrings.POST_IMAGE_BLOB, imageBytes);
        int val = db.update(DataBaseStrings.POST_MAIN, contentValues, DataBaseStrings.POST_ID + "='" + post_id + "'", null);
        Alert.showLog("OKKKK", "STORE VAL--- " + val);
        // db.close();
        return true;
    }

    public boolean updateCommentUserImageBlob(String comment_id, byte[] imageBytes) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseStrings.POST_COMMENT_USER_IMAGE, imageBytes);
        int val = db.update(DataBaseStrings.POST_COMMENT, contentValues, DataBaseStrings.POST_COMMENT_ID + "='" + comment_id + "'", null);
        return true;
    }

    public boolean updatePostLike(String post_id, String val) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseStrings.POST_LIKE, val);
        db.update(DataBaseStrings.POST_MAIN, contentValues, DataBaseStrings.POST_ID + "='" + post_id + "'", null);
        // db.close();
        return true;
    }

    public boolean updatePostSave(String post_id, String val) {

        Alert.showLog("updatePostSave","updatePostSave-- "+post_id+" val-- "+val);

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseStrings.POST_SAVE, val);
        db.update(DataBaseStrings.POST_MAIN, contentValues, DataBaseStrings.POST_ID + "='" + post_id + "'", null);
        // db.close();
        return true;
    }

    public boolean updateContactStatus(String device_id,String number,String inviteStatus,String image,String server_id)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseStrings.INVITE_STATUS, inviteStatus);
        contentValues.put(DataBaseStrings.FRIENDS_IMAGE, image);
        contentValues.put(DataBaseStrings.FRIENDS_ID_ON_SERVER, server_id);
        int val = db.update(DataBaseStrings.FRIENDS, contentValues, DataBaseStrings.FRIENDS_ID_IN_DEVICE_STORAGE + "=" + device_id, null);
        return true;
    }

    /*DELETE DATA*/
    public void deleteDownload(DownloadedData data)
    {
        db.delete(DataBaseStrings.POST_DOWNLOAD,DataBaseStrings.POST_ID + "=?"+ "AND "+DataBaseStrings.MEDIA_INDEX+"=?"+"AND "+DataBaseStrings.MEDIA_NAME+"=?", new String[]{data.getPOST_ID(),data.getMEDIA_INDEX(),data.getMEDIA_NAME()});
    }
}
