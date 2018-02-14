package co.netpar.Syncronization;

import android.content.Context;
import co.netpar.Comman.Alert;
import co.netpar.Comman.Constants;
import co.netpar.Model.CategoryModel;
import co.netpar.Model.CommentListModel;
import co.netpar.Model.ContactsModel;
import co.netpar.Model.ContentListingModel;
import co.netpar.Model.NotificationModel;
import co.netpar.Model.SectionModel;
import co.netpar.Model.SubCategoryModel;
import co.netpar.Retrofit.Retrofit2;
import co.netpar.Retrofit.ServiceResponse;
import co.netpar.Syncronization.NetparDataBase.DataBaseStrings;
import co.netpar.Syncronization.NetparDataBase.DatabaseHelper;
import co.netpar.Syncronization.NetparDataBase.SharedPreference;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Response;

public class CallService implements ServiceResponse {
    private Context context;
    private ResponseSynchronous updateData;
    public static final int SECTION_LIST=0;
    public static final int ARTICLE_LIST=1;
    public static final int SYNC_CONTACT_LIST=2;
    public static final int GETALLNOTIFICATION=4;
    private final int SAVE_LIST=2;
    private final int MY_POST_LIST=3;
    private final String TAG="CallService";
    private List <SectionModel> sectionList=new ArrayList<>();
    private List <ContentListingModel> articleList=new ArrayList<>();
    private List <CommentListModel> CommentList=new ArrayList<>();
    private List <ContentListingModel> myPost=new ArrayList<>();
    public interface ResponseSynchronous {
        void UpdateData(int service_val);
    }
    public CallService(Context context) {
        this.context = context;
    }

    /*-------------Get All Section From Server--------------------*/
    public void getSectionsFromServer() {
        new Retrofit2(this.context, this, SECTION_LIST, Constants.SECTION_DATA).callService(false);
    }

    /*-------------Get All Articles From Server--------------------*/
    public void callGetArticleService() {
        if (SharedPreference.retrieveData(context, Constants.USER_ID) != null) {
            new Retrofit2(this.context, this, ARTICLE_LIST, Constants.ARTICLE_DATA + "/" + SharedPreference.retrieveData(context, Constants.USER_ID)).callService(false);
        }
    }

    /*-------------Get Seved Articles From Server--------------------*/
    private void callAllSavedItemService()
    {
        if(SharedPreference.retrieveData(context,Constants.USER_ID)!=null)
        {
            new Retrofit2(this.context, this,SAVE_LIST, Constants.SAVED_DATA+"/"+ SharedPreference.retrieveData(context,Constants.USER_ID)).callService(false);
        }
    }

    /*-------------Get My Contribution From Server--------------------*/
    public void callMyPostItemService()
    {
        if(SharedPreference.retrieveData(context,Constants.USER_ID)!=null)
        {
            new Retrofit2(this.context, this,MY_POST_LIST, Constants.GET_MY_POST+"/"+ SharedPreference.retrieveData(context,Constants.USER_ID)).callService(false);
        }
    }

    public void runAllGetService() {
        getSectionsFromServer();
        callGetArticleService();
        callAllNotificationService();
    }

    /*-------------Get Notification From Server--------------------*/
    public void callAllNotificationService() {
        if (SharedPreference.retrieveData(context, Constants.USER_ID) != null) {
            new Retrofit2(this.context, this, GETALLNOTIFICATION, Constants.GETALLNOTIFICATION + "/" + SharedPreference.retrieveData(context, Constants.USER_ID)).callService(false);
        }
    }

    public void callServiceToUpdateSQLiteData() {
        runAllGetService();
    }

    public void onServiceResponse(int requestCode, Response<ResponseBody> response)
    {
        try
        {
            JSONObject object =new JSONObject(response.body().string());
            Alert.showLog(TAG,requestCode+"- RESPONSE OBJECT- "+object.toString());
            switch (requestCode)
            {
                case SECTION_LIST:
                    if(object.getString("success").equalsIgnoreCase("true"))
                    {
                        parseSectionData(object);
                    }

                    if(updateData!=null)
                    {
                        updateData.UpdateData(requestCode);
                    }
                    break;
                case ARTICLE_LIST:
                    /*All articles are coming delete or not is depends on deleteStatus*/
                    if(object.getString("success").equalsIgnoreCase("true"))
                    {
                        parseArticleData(object);
                        //callAllSavedItemService();
                    }

                    if(updateData!=null)
                    {
                        updateData.UpdateData(requestCode);
                    }
                    break;
                case SYNC_CONTACT_LIST:
                    if(object.getString("success").equalsIgnoreCase("true"))
                    {
                        parseSyncContactData(object);
                    }
                    if(updateData!=null)
                    {
                        updateData.UpdateData(requestCode);
                    }
                    break;
                case MY_POST_LIST:
                    if(object.getString("success").equalsIgnoreCase("true"))
                    {
                        parseMyPostData(object);
                    }
                    if(updateData!=null)
                    {
                        updateData.UpdateData(requestCode);
                    }
                    break;
                case GETALLNOTIFICATION:
                    if(object.getString("success").equalsIgnoreCase("true"))
                    {
                        parseAllNotification(object);
                        if(updateData!=null)
                        {
                            updateData.UpdateData(requestCode);
                        }
                    }
                    break;
               /* case SAVE_LIST:
                    if(object.getString("success").equalsIgnoreCase("true"))
                    {
                        parseSaveAndUpdateSaveStatus(object);
                    }
                    break;*/
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setOnUpdate(ResponseSynchronous updateData) {
        this.updateData = updateData;
    }

    /*-------------Section data parsing--------------------*/
    private void parseSectionData(JSONObject object)
    {
        try
        {
            if(object.has("FinalArray"))
            {
                JSONArray array = object.getJSONArray("FinalArray");
                DatabaseHelper instance=new DatabaseHelper(context);
                instance.clearDataBase();
                if(array.length()>0)
                {
                    sectionList.clear();
                    for (int i=0; i<array.length();i++)
                    {
                        String section_id = DataBaseStrings.DEFAULT_VALUE,
                                sectionName= DataBaseStrings.DEFAULT_VALUE,
                                categoryView= DataBaseStrings.DEFAULT_VALUE,
                                language= DataBaseStrings.DEFAULT_VALUE,
                                thumbnailImage= DataBaseStrings.DEFAULT_VALUE,
                                horigontalImage= DataBaseStrings.DEFAULT_VALUE,
                                section_lay_type= DataBaseStrings.DEFAULT_VALUE,
                                item_id=DataBaseStrings.DEFAULT_VALUE;

                        int orderNo=0;

                        JSONObject section=array.getJSONObject(i);
                        section_id = section.getString("_id");
                        sectionName = section.getString("sectionName");
                        language = section.getString("language");
                        if(section.has("thumbnailImage"))
                        {
                            thumbnailImage = section.getString("thumbnailImage");
                        }
                        if(section.has("horigontalImage"))
                        {
                            horigontalImage = section.getString("horigontalImage");
                        }

                        if(section.has("sectionViewFormat"))
                        {
                            section_lay_type = section.getString("sectionViewFormat");
                        }

                        if (section.has("itemId"))
                        {
                            item_id = section.getString("itemId");
                        }

                        if(section.has("orderNo"))
                        {
                            if(!section.getString("orderNo").equalsIgnoreCase("null"))
                                orderNo=section.getInt("orderNo");
                            else
                                orderNo=0;
                        }

                        sectionList.add(new SectionModel(section_id, sectionName, language, thumbnailImage,
                                horigontalImage,section_lay_type,orderNo,item_id));

                        if(section.has("section_categories"))
                        {
                            JSONArray section_categories=section.getJSONArray("section_categories");
                            List <CategoryModel> categoryList=new ArrayList<>();
                            if(section_categories.length()>0)
                            {
                                for (int j=0;j<section_categories.length();j++)
                                {
                                    String category_id = DataBaseStrings.DEFAULT_VALUE,
                                            category_sectionId= DataBaseStrings.DEFAULT_VALUE,
                                            sectionnName= DataBaseStrings.DEFAULT_VALUE,
                                            categoryName= DataBaseStrings.DEFAULT_VALUE,
                                            category_language= DataBaseStrings.DEFAULT_VALUE,
                                            thumbnaillImage=DataBaseStrings.DEFAULT_VALUE,
                                            horigontallImage= DataBaseStrings.DEFAULT_VALUE,
                                            categoryFormat= DataBaseStrings.DEFAULT_VALUE,
                                            listViewFormat=DataBaseStrings.DEFAULT_VALUE,
                                            item_id_sub=DataBaseStrings.DEFAULT_VALUE;

                                    JSONObject categories = section_categories.getJSONObject(j);
                                    category_id = categories.getString("_id");
                                    category_language = categories.getString("language");
                                    category_sectionId = categories.getString("sectionId");
                                    categoryName = categories.getString("categoryName");
                                    sectionnName = categories.getString("sectionName");
                                    if(categories.has("thumbnailImage"))
                                    {
                                        thumbnaillImage = categories.getString("thumbnailImage");
                                    }
                                    if(categories.has("horigontalImage"))
                                    {
                                        horigontallImage = categories.getString("horigontalImage");
                                    }

                                    if(categories.has("categoryFormat"))
                                    {
                                        categoryFormat = categories.getString("categoryFormat");
                                    }

                                    if(categories.has("listViewFormat"))
                                    {
                                        listViewFormat = categories.getString("listViewFormat");
                                    }

                                    if(categories.has("itemId"))
                                    {
                                        item_id_sub = categories.getString("itemId");
                                    }

                                    categoryList.add(new CategoryModel(category_id,category_language,category_sectionId,categoryName,
                                            sectionnName,thumbnaillImage,horigontallImage,categoryFormat,listViewFormat,item_id_sub));

                                    if(categories.has("section_subcategories"))
                                    {
                                        String sub_category_id = DataBaseStrings.DEFAULT_VALUE,
                                                sub_category_languagee= DataBaseStrings.DEFAULT_VALUE,
                                                sub_category_sectionId= DataBaseStrings.DEFAULT_VALUE,
                                                sub_category_categoryId= DataBaseStrings.DEFAULT_VALUE,
                                                sub_category_Name= DataBaseStrings.DEFAULT_VALUE,
                                                sub_category_sectionName = DataBaseStrings.DEFAULT_VALUE,
                                                sub_category_categoryName= DataBaseStrings.DEFAULT_VALUE,
                                                sub_category_thumbnaillImage=DataBaseStrings.DEFAULT_VALUE,
                                                sub_category_horigontalImage=DataBaseStrings.DEFAULT_VALUE,
                                                subCategoryFormat=DataBaseStrings.DEFAULT_VALUE;
                                                item_id=DataBaseStrings.DEFAULT_VALUE;
                                        List <SubCategoryModel> subCategoryList=new ArrayList<>();

                                        JSONArray sub_categories=categories.getJSONArray("section_subcategories");
                                        for (int k=0;k<sub_categories.length();k++)
                                        {
                                            JSONObject sub_category= sub_categories.getJSONObject(k);
                                            sub_category_id = sub_category.getString("_id");
                                            item_id = sub_category.getString("itemId");
                                            sub_category_languagee = sub_category.getString("language");
                                            sub_category_sectionId = sub_category.getString("sectionId");
                                            sub_category_categoryId = sub_category.getString("categoryId");
                                            sub_category_Name = sub_category.getString("subCategoryName");
                                            sub_category_sectionName= sub_category.getString("sectionName");
                                            sub_category_categoryName = sub_category.getString("categoryName");
                                             if(sub_category.has("thumbnailImage"))
                                             {
                                                sub_category_thumbnaillImage = sub_category.getString("thumbnailImage");
                                             }
                                             if(sub_category.has("horigontalImage"))
                                             {
                                                 sub_category_horigontalImage = sub_category.getString("horigontalImage");
                                             }


                                             if(sub_category.has("subCategoryFormat"))
                                             {
                                                subCategoryFormat = sub_category.getString("subCategoryFormat");
                                             }

                                            if(sub_category.has("itemId"))
                                            {
                                                item_id = sub_category.getString("itemId");
                                            }

                                            subCategoryList.add(new SubCategoryModel(sub_category_id,sub_category_languagee,sub_category_sectionId,sub_category_categoryId,sub_category_Name,
                                                    sub_category_sectionName,sub_category_categoryName,
                                                    sub_category_thumbnaillImage,sub_category_horigontalImage,
                                                    subCategoryFormat,item_id));
                                        }
                                        instance.storeSubCategory(subCategoryList);
                                    }

                                }
                                instance.storeCategory(categoryList);
                            }
                        }
                    }
                    instance.storeSection(sectionList);
                }
            }
        }
         catch (Exception e)
            {
                Alert.showLog(TAG,e.getMessage());
                Alert.showLog(TAG,e.toString());
                e.printStackTrace();
            }
    }

    /*-------------Article data parsing--------------------*/
    private void parseArticleData(JSONObject object)
    {
        try
        {
            if(object.has("response"))
            {
                JSONArray array = object.getJSONArray("response");
                if(array.length()>0)
                {
                    articleList.clear();
                    CommentList.clear();
                    /*0 used for unselect and 1 use for select in like,save*/
                    String _id=DataBaseStrings.DEFAULT_VALUE,language=DataBaseStrings.DEFAULT_VALUE,sectionId=DataBaseStrings.DEFAULT_VALUE,
                            categoryId=DataBaseStrings.DEFAULT_VALUE,subCategoryId=DataBaseStrings.DEFAULT_VALUE,headline=DataBaseStrings.DEFAULT_VALUE,slug=DataBaseStrings.DEFAULT_VALUE,
                            tagline=DataBaseStrings.DEFAULT_VALUE,sectionName=DataBaseStrings.DEFAULT_VALUE,
                            description=DataBaseStrings.DEFAULT_VALUE,
                            categoryName=DataBaseStrings.DEFAULT_VALUE,location=DataBaseStrings.DEFAULT_VALUE,
                            subCategoryName=DataBaseStrings.DEFAULT_VALUE,like="0",save="0",totalLike="0",
                            commentCount="0",shareCount="0",viewCount="0", creator_f_name=DataBaseStrings.DEFAULT_VALUE,
                            creator_l_name=DataBaseStrings.DEFAULT_VALUE,dateOfCreation=DataBaseStrings.DEFAULT_VALUE,
                            deleteStatus="false",suggestedArticalList=DataBaseStrings.DEFAULT_VALUE,item_id=DataBaseStrings.DEFAULT_VALUE,horizential=DataBaseStrings.DEFAULT_VALUE;
                    for (int i=0; i<array.length();i++)
                    {
                        String firstImage=DataBaseStrings.DEFAULT_VALUE;
                        JSONObject item=array.getJSONObject(i);
                        if(item.has("_id"))
                        {
                           _id=item.getString("_id");
                        }
                        if(item.has("language"))
                        {
                            language=item.getString("language");
                        }
                        if(item.has("sectionId"))
                        {
                            sectionId=item.getString("sectionId");
                        }
                        if(item.has("categoryId"))
                        {
                            categoryId=item.getString("categoryId");
                        }
                        if(item.has("subCategoryId"))
                        {
                            subCategoryId=item.getString("subCategoryId");
                        }
                        if(item.has("itemId"))
                        {
                            item_id=item.getString("itemId");
                        }
                        if(item.has("headline"))
                        {
                            headline=item.getString("headline");
                        }
                        if(item.has("slug"))
                        {
                            slug=item.getString("slug");
                        }
                        if(item.has("tagline"))
                        {
                            tagline=item.getString("tagline");
                        }
                        if(item.has("sectionName"))
                        {
                            sectionName=item.getString("sectionName");
                        }
                        if(item.has("categoryName"))
                        {
                            categoryName=item.getString("categoryName");
                        }
                        if(item.has("subCategoryName"))
                        {
                            subCategoryName=item.getString("subCategoryName");
                        }

                        if(item.has("thumbnailPicture"))
                        {
                            firstImage=item.getString("thumbnailPicture");
                        }

                        if(item.has("horizontalPicture"))
                        {
                            horizential=item.getString("horizontalPicture");
                        }

                        if(item.has("liked"))
                        {
                            if(item.getString("liked").equalsIgnoreCase("true"))
                            {
                                like="1";
                            }
                            else
                            {
                                like="0";
                            }
                        }

                        if(item.has("likeCount"))
                        {
                            totalLike=item.getString("likeCount");
                        }

                        if(item.has("commentCount"))
                        {
                            commentCount=item.getString("commentCount");
                        }

                        if(item.has("pageView"))
                        {
                            viewCount=item.getString("pageView");
                        }

                        if(item.has("saved"))
                        {
                            if(item.getString("saved").equalsIgnoreCase("true"))
                            {
                                save="1";
                            }
                            else
                            {
                                save="0";
                            }
                        }

                        if(item.has("saveCount"))
                        {
                            shareCount=item.getString("saveCount");
                        }

                        if(item.has("deleteStatus"))
                        {
                            deleteStatus=item.getString("deleteStatus");
                        }

                        if(item.has("userList"))
                        {
                            if(item.getJSONArray("userList").length()>0)
                            {
                                JSONObject user = item.getJSONArray("userList").getJSONObject(0);
                                if(user.has("firstName"))
                                {
                                    creator_f_name=user.getString("firstName");
                                }

                                if(user.has("lastName"))
                                {
                                    creator_l_name=user.getString("lastName");
                                }
                            }
                        }

                        if(item.has("dateOfCreation"))
                        {
                            dateOfCreation=item.getString("dateOfCreation");
                        }

                        ContentListingModel data=new ContentListingModel();
                        data.setContentId(_id);
                        data.setItem_id(item_id);
                        data.setLanguage(language);
                        data.setSectionId(sectionId);
                        data.setCategoryId(categoryId);
                        data.setSubCategoryId(subCategoryId);
                        data.setHeadline(headline);
                        data.setSlug(slug);
                        data.setTagline(tagline);
                        data.setSectionName(sectionName);
                        data.setCategoryName(categoryName);
                        data.setSubCategoryName(subCategoryName);
                        data.setContentLocation(location);
                        data.setDescription(description);
                        data.setLike(like);
                        data.setTotal_like(totalLike);
                        data.setSave(save);
                        data.setHorizontalPicture(horizential);
                        data.setShare_count(shareCount);
                        data.setComment_count(commentCount);
                        data.setView_count(viewCount);
                        data.setCreator_first_name(creator_f_name);
                        data.setCreator_last_name(creator_l_name);
                        data.setDeleteStatus(deleteStatus);
                        data.setDateOfCreation(dateOfCreation);
                        if(item.has("suggestedArticleList"))
                        {
                            JSONArray suggestedArticleList = item.getJSONArray("suggestedArticleList");
                            StringBuffer result = new StringBuffer();
                            for (int sg=0;sg<suggestedArticleList.length();sg++)
                            {
                                if(sg==0)
                                {
                                    result.append(suggestedArticleList.getJSONObject(sg).getString("_id") );
                                }
                                else
                                {
                                    result.append(",").append(suggestedArticleList.getJSONObject(sg).getString("_id") );
                                }
                            }
                            suggestedArticalList = result.toString();
                        }
                        else
                        {
                            suggestedArticalList="";
                        }
                        data.setSuggestedArticleList(suggestedArticalList);
                        data.setFirstImage(firstImage);
                        articleList.add(data);

                        if(item.has("user_comments"))
                        {
                            String comment_id=DataBaseStrings.DEFAULT_VALUE,
                                    dateOfComment=DataBaseStrings.DEFAULT_VALUE,
                                    userId=DataBaseStrings.DEFAULT_VALUE,
                                    articleName=DataBaseStrings.DEFAULT_VALUE,
                                    articleId=DataBaseStrings.DEFAULT_VALUE,
                                    userPhone=DataBaseStrings.DEFAULT_VALUE,
                                    userComment=DataBaseStrings.DEFAULT_VALUE,
                                    userName=DataBaseStrings.DEFAULT_VALUE,
                                    status=DataBaseStrings.DEFAULT_VALUE,
                                    deleteeStatus=DataBaseStrings.DEFAULT_VALUE,
                                    user_image=DataBaseStrings.DEFAULT_VALUE;

                            JSONArray com_array1 = item.getJSONArray("user_comments");
                            if(com_array1.length()>0)
                            {
                                for (int co=0;co<com_array1.length();co++)
                                {
                                    JSONObject comment = com_array1.getJSONObject(co);
                                    if(comment.has("_id"))
                                    {
                                        comment_id=comment.getString("_id");
                                    }

                                    if(comment.has("dateOfComment"))
                                    {
                                        dateOfComment=comment.getString("dateOfComment");
                                    }

                                    if(comment.has("userId"))
                                    {
                                        userId=comment.getString("userId");
                                    }

                                    if(comment.has("articleName"))
                                    {
                                        articleName=comment.getString("articleName");
                                    }

                                    if(comment.has("articleId"))
                                    {
                                        articleId=comment.getString("articleId");
                                    }

                                    if(comment.has("userPhone"))
                                    {
                                        userPhone=comment.getString("userPhone");
                                    }

                                    if(comment.has("userComment"))
                                    {
                                        userComment=comment.getString("userComment");
                                    }

                                    if(comment.has("userName"))
                                    {
                                        userName=comment.getString("userName");
                                    }

                                    if(comment.has("status"))
                                    {
                                        status=comment.getString("status");
                                    }

                                    if(comment.has("deleteStatus"))
                                    {
                                        deleteeStatus=comment.getString("deleteStatus");
                                    }
                                    CommentListModel comm = new CommentListModel();
                                    comm.setComment_id(comment_id);
                                    comm.setDateOfComment(dateOfComment);
                                    comm.setUserId(userId);
                                    comm.setArticleName(articleName);
                                    comm.setArticleId(articleId);
                                    comm.setUserPhone(userPhone);
                                    comm.setUserComment(userComment);
                                    comm.setUserName(userName);
                                    comm.setStatus(status);
                                    comm.setDeleteStatus(deleteeStatus);
                                    comm.setUser_image(user_image);
                                    CommentList.add(comm);
                                }
                            }
                        }
                    }
                    DatabaseHelper instance=new DatabaseHelper(context);
                    instance.clearPostData();
                    instance.storeAllPost(articleList);
                    instance.storeAllPostComments(CommentList);

                    Alert.showLog("POST_COUNT","POST_COUNT- "+instance.getPostCount());
                    Alert.showLog("POST_COMMENT_COUNT","POST_COMMENT_COUNT- "+instance.getPostCommentCount());
                }
            }
        }
        catch (Exception e)
        {
            Alert.showLog(TAG,e.getMessage());
            Alert.showLog(TAG,e.toString());
            e.printStackTrace();
        }
    }

    /*-------------Mobile Contact Sync From Netpar User--------------------*/
    private void parseSyncContactData(JSONObject object)
    {
        Alert.showLog("RES","parseSyncContactData RESPONSE-- "+object.toString());
        try
        {
            DatabaseHelper helper=new DatabaseHelper(context);
            JSONArray array = object.getJSONArray("contacts");
                if(array.length()>0)
                {
                    for (int cont=0;cont<array.length();cont++)
                    {
                        JSONObject dt=array.getJSONObject(cont);
                        String val=ContactsModel.INVITE;
                        String user_image=DataBaseStrings.DEFAULT_VALUE;
                        String user_id=DataBaseStrings.DEFAULT_VALUE;
                        if(dt.has("userImage"))
                        {
                            user_image=dt.getString("userImage");
                        }
                        if(dt.has("userId"))
                        {
                            user_id=dt.getString("userId");
                        }
                        if(dt.getString("status").equalsIgnoreCase("true"))
                        {
                            val=ContactsModel.FRIEND;
                            helper.updateContactStatus(dt.getString("device_id"),dt.getString("number"),val,user_image,user_id);
                        }
                    }
                }

            if(object.has("ref")) {
                JSONArray ref_array = object.getJSONArray("ref");
                if (ref_array.length() > 0) {
                    for (int cont = 0; cont < ref_array.length(); cont++) {
                        JSONObject dt = ref_array.getJSONObject(cont);
                        String val = ContactsModel.FRIEND;
                        String user_image = DataBaseStrings.DEFAULT_VALUE;
                        String user_id = DataBaseStrings.DEFAULT_VALUE;
                        String user_name = DataBaseStrings.DEFAULT_VALUE;
                        String mobileNumber = DataBaseStrings.DEFAULT_VALUE;


                        if (dt.has("_id")) {
                            user_id = dt.getString("_id");
                        }

                        if(helper.getRefFriendCount(user_id)<1)
                        {
                            if (dt.has("firstName")) {
                                user_name = dt.getString("firstName");
                            }
                            if (dt.has("lastName")) {
                                user_name = user_name + " " + dt.getString("lastName");
                            }
                            if (dt.has("mobileNumber")) {
                                mobileNumber = user_name + " " + dt.getString("mobileNumber");
                            }
                            if (dt.has("userImage")) {
                                user_image = dt.getString("userImage");
                            }
                            helper.addRefferalFriend(user_name, mobileNumber, val, user_image, user_id);
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            Alert.showLog(TAG,e.getMessage());
            Alert.showLog(TAG,e.toString());
            e.printStackTrace();
        }
    }

    /*-------------Saved Article data parsing--------------------*/
    private void parseSaveAndUpdateSaveStatus(JSONObject object)
    {
        try
        {
            if(object.has("response"))
            {
                JSONArray response=object.getJSONArray("response");
                if(response.length()>0)
                {
                    JSONObject objecttttt=response.getJSONObject(0);
                    if(objecttttt.has("saved_articles"))
                    {
                        JSONArray saved_articles=objecttttt.getJSONArray("saved_articles");
                        if(saved_articles.length()>0)
                        {
                            DatabaseHelper instance=new DatabaseHelper(context);
                            for (int td=0;td < saved_articles.length();td++)
                            {
                                Alert.showLog("_id","_id+ parseSaveAndUpdateSaveStatus  "+saved_articles.getJSONObject(td).getString("_id"));
                                instance.updatePostSave(saved_articles.getJSONObject(td).getString("_id"),"1");
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            Alert.showLog("EXCEPTION","EXCEPTION-- "+e.toString()+" MESSAGE-- "+e.getMessage());
            e.printStackTrace();
        }
    }

    /*-------------Sync Mobile Contact--------------------*/
    public void syncContactFromServer()
    {
        try
        {
            DatabaseHelper instance=new DatabaseHelper(context);
            List<ContactsModel> contact=instance.getInviteFriendList();
            if(contact.size()>0)
            {
                if(SharedPreference.retrieveData(context, Constants.USER_ID)!=null)
                {
                    JSONObject object = new JSONObject();
                    JSONArray contact_data = new JSONArray();
                    for (int cn=0;cn<contact.size();cn++)
                    {
                        JSONObject dt=new JSONObject();
                        dt.put("device_id",contact.get(cn).getDevice_id());
                        dt.put("number",contact.get(cn).getNumber().replace("(","").replace(")","").replace("-","").replace(" ","").trim());
                        contact_data.put(cn,dt);
                    }
                    object.put("userId",SharedPreference.retrieveData(context,Constants.USER_ID));
                    object.put("data",contact_data);
                    new Retrofit2(this.context, this, SYNC_CONTACT_LIST, Constants.SYNC_CONTACT_LIST,object).callService(false);
                }
            }
        }
        catch (Exception e)
        {e.printStackTrace();}
    }

    /*-------------Parse My Contribution--------------------*/
    private void parseMyPostData(JSONObject object)
    {
        try
        {
            if (object.has("data"))
            {
                myPost.clear();
                JSONArray array = object.getJSONArray("data");
                if(array.length()>0)
                {
                    /*0 used for unselect and 1 use for select in like,save*/
                    String _id=DataBaseStrings.DEFAULT_VALUE,
                            headline=DataBaseStrings.DEFAULT_VALUE,
                            sectionName=DataBaseStrings.DEFAULT_VALUE,
                            description=DataBaseStrings.DEFAULT_VALUE,
                            firstImage=DataBaseStrings.DEFAULT_VALUE,
                            categoryName=DataBaseStrings.DEFAULT_VALUE,
                            subCategoryName=DataBaseStrings.DEFAULT_VALUE;

                    for (int i=0; i<array.length();i++)
                    {
                        JSONObject item = array.getJSONObject(i);
                        if (item.has("_id")) {
                            _id = item.getString("_id");
                        }
                        if (item.has("title")) {
                            headline = item.getString("title");
                        }
                        if (item.has("description")) {
                            description = item.getString("description");
                        }
                        if (item.has("sectionName")) {
                            sectionName = item.getString("sectionName");
                        }
                        if (item.has("categoryName")) {
                            categoryName = item.getString("categoryName");
                        }
                        if (item.has("subCategoryName")) {
                            subCategoryName = item.getString("subCategoryName");
                        }

                        if(item.has("media"))
                        {
                            JSONArray media=item.getJSONArray("media");
                            if(media.length()>0)
                            {
                                firstImage = media.getJSONObject(0).getString("url");
                                for (int im=0;im<media.length();im++)
                                {
                                    if(media.getJSONObject(im).getString("type").equalsIgnoreCase("image"))
                                    {
                                        firstImage = media.getJSONObject(im).getString("url");
                                    }
                                }
                            }
                        }

                        ContentListingModel data=new ContentListingModel();
                        data.setContentId(_id);
                        data.setHeadline(headline);
                        data.setDescription(description);
                        data.setSectionName(sectionName);
                        data.setCategoryName(categoryName);
                        data.setSubCategoryName(subCategoryName);
                        data.setFirstImage(firstImage);
                        myPost.add(data);
                    }
                }
                DatabaseHelper instance=new DatabaseHelper(context);
                instance.clearMyPost();
                instance.storeMyPost(myPost);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    /*-------------Notification Data Parsing--------------------*/
    private void parseAllNotification(JSONObject object)
    {
        try
        {
            if(object.has("success"))
            {
                if(object.getString("success").equalsIgnoreCase("true"))
                {
                    List<NotificationModel> not_data = new ArrayList<>();
                    if(object.has("notifications"))
                    {
                        JSONArray notson=object.getJSONArray("notifications");
                        for (int i=0;i<notson.length();i++)
                        {
                            JSONObject nt=notson.getJSONObject(i);
                            not_data.add(new NotificationModel(DataBaseStrings.DEFAULT_VALUE, nt.getString("title"),nt.getString("message"),nt.getString("created")));
                        }
                    }
                    DatabaseHelper instance=new DatabaseHelper(context);
                    instance.clearNotification();
                    instance.storeNotificationList(not_data);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
