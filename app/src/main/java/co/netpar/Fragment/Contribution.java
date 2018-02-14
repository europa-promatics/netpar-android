package co.netpar.Fragment;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.reverie.customcomponent.RevEditText;
import com.reverie.customcomponent.RevTextView;
import com.reverie.manager.RevSDK;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import co.netpar.Adapter.ContributionMediaAdapter;
import co.netpar.App.Controller;
import co.netpar.Comman.Alert;
import co.netpar.Comman.Constants;
import co.netpar.Comman.DrowableFunction;
import co.netpar.ContentView;
import co.netpar.Home;
import co.netpar.Language;
import co.netpar.Model.CategoryModel;
import co.netpar.Model.MediaModel;
import co.netpar.Model.SectionModel;
import co.netpar.Model.SubCategoryModel;
import co.netpar.PagerAdapter.HomePagerAdapter;
import co.netpar.R;
import co.netpar.Retrofit.Retrofit2;
import co.netpar.Retrofit.RetrofitService;
import co.netpar.Retrofit.ServiceResponse;
import co.netpar.SignIn;
import co.netpar.Syncronization.CallService;
import co.netpar.Syncronization.NetparDataBase.DataBaseStrings;
import co.netpar.Syncronization.NetparDataBase.DatabaseHelper;
import co.netpar.Syncronization.NetparDataBase.SharedPreference;
import co.netpar.Widget.Croping.Croping;
import co.netpar.Widget.Croping.VedioTrimming;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;
import static com.isseiaoki.simplecropview.util.Utils.getDataColumn;
import static com.isseiaoki.simplecropview.util.Utils.isDownloadsDocument;
import static com.isseiaoki.simplecropview.util.Utils.isExternalStorageDocument;
import static com.isseiaoki.simplecropview.util.Utils.isGooglePhotosUri;
import static com.isseiaoki.simplecropview.util.Utils.isMediaDocument;

/**
 * Created by promatics on 10/8/2017.
 */

public class Contribution extends Fragment implements View.OnClickListener, ServiceResponse {
    private Context context;
    private String TAG = "Contribution";
    private TextView post_article, post_article1,close_article1;
    private RevEditText input_head_line, detail_line;
    private RevTextView input_mobile, input_firstName, input_last_name;
    private ImageView img, video, file, audio;
    private AutoCompleteTextView spinner_language, spinner_subcategory, spinner_category, spinner_section;
    private DatabaseHelper helper;
    private List<SectionModel> section_data = new ArrayList<>();
    private List<String> section = new ArrayList<>();
    private List<CategoryModel> categoryModels = new ArrayList<>();
    private List<String> categories = new ArrayList<>();
    private List<SubCategoryModel> sub_categoryModels = new ArrayList<>();
    private List<String> sub_categories = new ArrayList<>();
    private RecyclerView attached_data;
    public static final int PICK_PHOTO_FOR_NETPAR = 0, SELECT_VIDEO = 1, AUDIO_GALLERY_REQUEST_CODE = 3, REQUEST_CODE_DOC = 4;
    private List<MediaModel> mediaList = new ArrayList<>();
    private ContributionMediaAdapter media_adapter;
    private static final int UPLOAD_POST = 0;
    private static final int UPLOAD_MEDIA = 1;
    public static final int CROP_IMAGE = 500;
    public static final int CROP_VIDEO = 700;
    private boolean aBoolean = true;
    private CardView bottom_icon;

    private Uri picUri;
    private File pic;

    private static Contribution instance;
    public static boolean showDelete = true;

    public static synchronized Contribution getInstance() {
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.contribution_fragment, container, false);
        instance = this;
        context = container.getContext();
        helper = ((Home) context).helper;
        initializeView(v);
        return v;
    }

    private void initializeView(View v) {
        img = (ImageView) v.findViewById(R.id.img);
        video = (ImageView) v.findViewById(R.id.video);
        file = (ImageView) v.findViewById(R.id.file);
        audio = (ImageView) v.findViewById(R.id.audio);
        post_article = (TextView) v.findViewById(R.id.post_article);
        post_article1 = (TextView) v.findViewById(R.id.post_article1);
        close_article1=(TextView)v.findViewById(R.id.close_article1);
        bottom_icon = (CardView) v.findViewById(R.id.bottom_icon);

        setUpInitialData(v);
        setUpListener();
    }

    private void setUpInitialData(View v) {
        attached_data = (RecyclerView) v.findViewById(R.id.attached_data);
        {
            attached_data.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
            media_adapter = new ContributionMediaAdapter(context, mediaList);
            attached_data.setAdapter(media_adapter);
            attached_data.setVisibility(View.GONE);
        }

        input_mobile = (RevTextView) v.findViewById(R.id.input_mobile);
        input_firstName = (RevTextView) v.findViewById(R.id.input_firstName);
        input_last_name = (RevTextView) v.findViewById(R.id.input_last_name);

        input_head_line = (RevEditText) v.findViewById(R.id.input_head_line);
        detail_line = (RevEditText) v.findViewById(R.id.detail_line);

        spinner_section = (AutoCompleteTextView) v.findViewById(R.id.spinner_section);
        spinner_category = (AutoCompleteTextView) v.findViewById(R.id.spinner_category);
        spinner_subcategory = (AutoCompleteTextView) v.findViewById(R.id.spinner_subcategory);
        spinner_language = (AutoCompleteTextView) v.findViewById(R.id.spinner_language);

        spinner_section.setFocusable(false);
        spinner_section.setFocusableInTouchMode(false);

        spinner_category.setFocusable(false);
        spinner_category.setFocusableInTouchMode(false);

        spinner_subcategory.setFocusable(false);
        spinner_subcategory.setFocusableInTouchMode(false);

        spinner_language.setFocusable(false);
        spinner_language.setFocusableInTouchMode(false);


        input_mobile.setText(SharedPreference.retrieveData(context, Constants.MOBILE_NUMBER));
        input_firstName.setText(SharedPreference.retrieveData(context, Constants.FIRST_NAME));
        input_last_name.setText(SharedPreference.retrieveData(context, Constants.LAST_NAME));
        input_head_line.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                RevSDK.initKeypad((Activity) context, SharedPreference.retrieveLang(context));
                return false;
            }
        });
        detail_line.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                RevSDK.initKeypad((Activity) context, SharedPreference.retrieveLang(context));
                return false;
            }
        });
    }

    private void setUpListener() {
        img.setOnClickListener(this);
        video.setOnClickListener(this);
        file.setOnClickListener(this);
        audio.setOnClickListener(this);
        post_article.setOnClickListener(this);
        post_article1.setOnClickListener(this);
        close_article1.setOnClickListener(this);

        spinner_section.setOnClickListener(this);
        spinner_category.setOnClickListener(this);
        spinner_subcategory.setOnClickListener(this);
        spinner_language.setOnClickListener(this);


        initLanguageSpinner();
    }

    public void resetLay() {
        showDelete = true;
        input_head_line.setEnabled(true);
        detail_line.setEnabled(true);
        spinner_section.setEnabled(true);
        spinner_category.setEnabled(true);
        spinner_subcategory.setEnabled(true);
        spinner_language.setEnabled(true);

        mediaList.clear();
        media_adapter.notifyDataSetChanged();
        input_head_line.setText("");
        detail_line.setText("");
        spinner_section.setText("");
        spinner_category.setText("");
        spinner_subcategory.setText("");
        spinner_language.setText("");
        bottom_icon.setVisibility(View.VISIBLE);
        post_article.setVisibility(View.VISIBLE);
        post_article1.setVisibility(View.VISIBLE);
    }

    private void disableLay() {
        input_head_line.setEnabled(false);
        detail_line.setEnabled(false);
        spinner_section.setEnabled(false);
        spinner_category.setEnabled(false);
        spinner_subcategory.setEnabled(false);
        spinner_language.setEnabled(false);
        bottom_icon.setVisibility(View.GONE);
        post_article.setVisibility(View.GONE);
        post_article1.setVisibility(View.GONE);
        showDelete = false;
        media_adapter.notifyDataSetChanged();
    }

    private void eraseFileFromMemoryCard() {
        try {
            if (mediaList.size() > 0) {
                for (int dt = 0; dt < mediaList.size(); dt++) {
                    MediaModel media = mediaList.get(dt);
                    String path = media.getPathindevice();
                    File file_path = new File(path);
                    if (file_path.exists()) {
                        file_path.delete();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDropDown(View v) {
        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(v.getWindowToken(), 0);
        ((AutoCompleteTextView) v).showDropDown();
    }

    public void initLanguageSpinner() {
        spinner_language.setAdapter(new ArrayAdapter(context, R.layout.drop_item, getResources().getStringArray(R.array.language_array_contribution)));
        spinner_language.setKeyListener(null);

        spinner_section.setKeyListener(null);
        spinner_section.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                spinner_category.setText("");
                spinner_subcategory.setText("");
                categoryModels.clear();
                sub_categories.clear();
                sub_categoryModels.clear();

                Alert.showLog("section_data get_id", "get_id- " + section_data.get(position).get_id());


                categoryModels.addAll(helper.getAllCategory(section_data.get(position).get_id()));

                Alert.showLog("section_data categoryModels", "categoryModels- " + categoryModels.size());

                categories.clear();
                for (int ct = 0; ct < categoryModels.size(); ct++) {
                    categories.add(ct, categoryModels.get(ct).getCategoryName());
                }

                spinner_category.setAdapter(new ArrayAdapter<String>(context, R.layout.drop_item, categories));
            }
        });

        spinner_category.setKeyListener(null);
        spinner_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Alert.showLog("position get_id", "position- " + position);

                spinner_subcategory.setText("");
                sub_categoryModels.clear();
                sub_categoryModels.addAll(helper.getAllSubCategory(categoryModels.get(position).getCategory_sectionId(), categoryModels.get(position).getCategory_id()));
                sub_categories.clear();
                for (int sct = 0; sct < sub_categoryModels.size(); sct++) {
                    sub_categories.add(sct, sub_categoryModels.get(sct).getSub_category_Name());
                }
                spinner_subcategory.setAdapter(new ArrayAdapter(context, R.layout.drop_item, sub_categories));
            }
        });
        spinner_subcategory.setKeyListener(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img:
                checkPermission();
                picImageOnly();
                break;
            case R.id.video:
                checkPermission();
                picVideoOnly();
                break;
            case R.id.file:
                checkPermission();
                picFileOnly();
                break;
            case R.id.audio:
                checkPermission();
                picAudioOnly();
                break;
            case R.id.post_article:
                if (validation()) {
                    if (aBoolean) {
                        upLoadPostData();
                    }
                }
                break;
            case R.id.post_article1:
                if (validation()) {
                    if (aBoolean) {
                        upLoadPostData();
                    }
                }
                break;
            case R.id.close_article1:
                resetLay();
                ((Home) context).setShowCurrentPagerItem(HomePagerAdapter.homeFragment);
                break;
            case R.id.spinner_section:
                showDropDown(v);
                break;
            case R.id.spinner_category:
                if (categoryModels.size() > 0) {
                    showDropDown(v);
                }
                break;
            case R.id.spinner_subcategory:
                if (sub_categoryModels.size() > 0) {
                    showDropDown(v);
                }
                break;
            case R.id.spinner_language:
                showDropDown(v);
                break;
        }
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT > 21 && ContextCompat.checkSelfPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
            return;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            section_data.clear();
            section_data.addAll(helper.getAllSections());
            section.clear();
            for (int j = 0; j < section_data.size(); j++) {
                section.add(j, section_data.get(j).getSectionName());
            }
            if (spinner_section != null) {
                spinner_section.setAdapter(new ArrayAdapter(context, R.layout.drop_item, section));
            }
        }
    }

    private void picImageOnly() {
     /*   Intent intent = new Intent();
        intent.setType("image*//*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent, PICK_PHOTO_FOR_NETPAR);*/
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FOR_NETPAR);
    }

    private void picVideoOnly() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        i.setType("video/*");
        startActivityForResult(i, SELECT_VIDEO);
        // startActivityForResult(Intent.createChooser(i, "Select Video"), SELECT_VIDEO);
    }

    private void picAudioOnly() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, AUDIO_GALLERY_REQUEST_CODE);
    }

    private void picFileOnly() {
        String[] mimeTypes =
                {"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                        "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                        "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                        "text/plain",
                        "application/pdf"};
        /*MORE mime https://stackoverflow.com/questions/4212861/what-is-a-correct-mime-type-for-docx-pptx-etc/4212908#4212908*/

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";
            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }
            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }
        startActivityForResult(Intent.createChooser(intent, "ChooseFile"), REQUEST_CODE_DOC);

    }

    public File getPathForPdf(Intent data, Context context) {
        File file = null;
        if (data != null) {
            Uri uri = data.getData();
            String path = null;
            try {
                path = DrowableFunction.getRealPathFromURI(context, uri);
                if (path == null) {
                    ContentResolver cR = context.getContentResolver();
                    MimeTypeMap mime = MimeTypeMap.getSingleton();
                    String type = mime.getExtensionFromMimeType(cR.getType(uri));
                    try {
                        file = File.createTempFile("Temp", "." + type);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String fileName = "MyFile.txt";
                    FileOutputStream outputStream = null;
                    try {
                        outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
                        OutputStreamWriter outputWriter = new OutputStreamWriter(outputStream);
                        outputWriter.write("Hello Just Test");
                        outputStream.close();
                        InputStream inputStream = context.openFileInput(fileName);
                        if (inputStream != null) {
                            FileInputStream fileIn = context.openFileInput(fileName);
                            InputStreamReader InputRead = new InputStreamReader(fileIn);
                            char[] inputBuffer = new char[100];
                            String s = "";
                            int charRead;

                            while ((charRead = InputRead.read(inputBuffer)) > 0) {
                                String readstring = String.copyValueOf(inputBuffer, 0, charRead);
                                s += readstring;
                            }
                            InputRead.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    file = new File(path);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_PHOTO_FOR_NETPAR:
                if (resultCode == RESULT_OK) {
                    if (data == null) {
                        return;
                    }
                    picUri = data.getData();
                    cropImage(picUri);
                }
                break;
            case SELECT_VIDEO:
                if (resultCode == RESULT_OK) {
                    String selectedVideoPath = DrowableFunction.getMediaPath(context, data.getData());
                    Intent intent = new Intent(context, VedioTrimming.class);
                    intent.putExtra("VIDEO", selectedVideoPath);
                    startActivityForResult(intent, CROP_VIDEO);
                }
                break;
            case AUDIO_GALLERY_REQUEST_CODE:
                try {
                    String path = DrowableFunction.getRealPathFromURI(context, data.getData());
                    aBoolean = false;
                    addMediaInToList(AUDIO_GALLERY_REQUEST_CODE, path);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case REQUEST_CODE_DOC:
                if (resultCode == RESULT_OK) {
                    if (data == null) {
                        return;
                    }
                    File da = getPathForPdf(data, context);
                    String Fpath = da.getPath();
                    aBoolean = false;
                    addMediaInToList(REQUEST_CODE_DOC, Fpath);
                }
                break;
            case CROP_IMAGE:
                String imagepath = data.getExtras().get("PATH").toString();
                if (!imagepath.equalsIgnoreCase(DataBaseStrings.DEFAULT_VALUE)) {
                    aBoolean = false;
                    addMediaInToList(PICK_PHOTO_FOR_NETPAR, imagepath);
                }
                break;
            case CROP_VIDEO:
                String videopath = data.getExtras().get("PATH").toString();
                if (!videopath.equalsIgnoreCase(DataBaseStrings.DEFAULT_VALUE)) {
                    aBoolean = false;
                    addMediaInToList(SELECT_VIDEO, videopath);
                }
                break;

        }
    }

    private void cropImage(Uri path) {
        String img_path = DrowableFunction.getRealPathFromURI(context, path);
        if (img_path == null) {
            img_path = DrowableFunction.getPath(context, path);
        }
        Intent intent = new Intent(context, Croping.class);
        intent.putExtra("imageUri", img_path);
        startActivityForResult(intent, CROP_IMAGE);
    }

    public Bitmap CompressResizeImage(Bitmap bm) {
        int bmWidth = bm.getWidth();
        int bmHeight = bm.getHeight();
        int ivWidth = 550;//imgViewCamera.getWidth();
        int ivHeight = 500;//imgViewCamera.getHeight();
        int new_height = (int) Math.floor((double) bmHeight * ((double) ivWidth / (double) bmWidth));
        Bitmap newbitMap = Bitmap.createScaledBitmap(bm, ivWidth, new_height, true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        newbitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        Bitmap bm1 = BitmapFactory.decodeByteArray(b, 0, b.length);
        return bm1;
    }

    private void addMediaInToList(int media_type, String path) {
        String media_name_in_device = DataBaseStrings.DEFAULT_VALUE;
        String[] fileName = path.split("/");
        try {
            media_name_in_device = fileName[fileName.length - 1];
        } catch (Exception e) {
        }

        mediaList.add(new MediaModel(media_type, path, media_name_in_device));
        media_adapter.notifyDataSetChanged();
        attached_data.setVisibility(View.VISIBLE);
        switch (media_type) {
            case PICK_PHOTO_FOR_NETPAR: {
                uploadFile(path, "image");
            }
            break;
            case SELECT_VIDEO: {
                uploadFile(path, "video");
            }
            break;
            case AUDIO_GALLERY_REQUEST_CODE: {
                uploadFile(path, "audio");
            }
            break;
            case REQUEST_CODE_DOC: {
                uploadFile(path, "doc");
            }
            break;
        }
    }

    private boolean validation() {
        String mobile = input_mobile.getText().toString().trim();
        String input_firstNam = input_firstName.getText().toString().trim();
        String input_last_nam = input_last_name.getText().toString().trim();
        String section_name = spinner_section.getText().toString().trim();
        String category_name = spinner_category.getText().toString().trim();
        String sub_category_name = spinner_subcategory.getText().toString().trim();
        String langu = spinner_language.getText().toString().trim();
        String title = input_head_line.getText().toString().trim();
        String description = detail_line.getText().toString().trim();
        if (section_name.isEmpty()) {
            Alert.alertDialog(context, getString(R.string.select_section_msg), getString(R.string.sorry), false);
            return false;
        } else if (categories.size() > 0 && category_name.isEmpty()) {
            Alert.alertDialog(context, getString(R.string.select_category_msg), getString(R.string.sorry), false);
            return false;
        } else if (sub_categories.size() > 0 && sub_category_name.isEmpty()) {
            Alert.alertDialog(context, getString(R.string.sub_category_msg), getString(R.string.sorry), false);
            return false;
        } else if (langu.isEmpty()) {
            Alert.alertDialog(context, getString(R.string.languag_msg), getString(R.string.sorry), false);
            return false;
        } else if (title.isEmpty()) {
            Alert.alertDialog(context, getString(R.string.enter_headline_msg), getString(R.string.sorry), false);
            return false;
        } else if (description.isEmpty()) {
            Alert.alertDialog(context, getString(R.string.enter_description_msg), getString(R.string.sorry), false);
            return false;
        }
        return true;
    }

    private void upLoadPostData() {
        try {
            String section_id = "";
            String category_id = "";
            String sub_category_id = "";
            String section_name = spinner_section.getText().toString().trim();
            String category_name = spinner_category.getText().toString().trim();
            String sub_category_name = spinner_subcategory.getText().toString().trim();
            for (int secs = 0; secs < section_data.size(); secs++) {
                SectionModel section_obj = section_data.get(secs);
                if (section_obj.getSectionName().equalsIgnoreCase(section_name)) {
                    section_id = section_obj.get_id();
                }
            }
            for (int cat = 0; cat < categoryModels.size(); cat++) {
                CategoryModel categ_obj = categoryModels.get(cat);
                if (categ_obj.getCategoryName().equalsIgnoreCase(category_name)) {
                    category_id = categ_obj.getCategory_id();
                }
            }
            for (int sub_cat = 0; sub_cat < sub_categoryModels.size(); sub_cat++) {
                SubCategoryModel sub_cat_obj = sub_categoryModels.get(sub_cat);
                if (sub_cat_obj.getSub_category_Name().equalsIgnoreCase(sub_category_name)) {
                    sub_category_id = sub_cat_obj.getSub_category_id();
                }
            }
            JSONObject peram = new JSONObject();
            peram.put("language", spinner_language.getText().toString().trim());
            peram.put("sectionId", section_id);
            peram.put("sectionName", section_name);
            peram.put("categoryName", category_name);
            peram.put("categoryId", category_id);
            peram.put("subCategoryId", sub_category_id);
            peram.put("subCategoryName", sub_category_name);
            peram.put("title", input_head_line.getText().toString().trim());
            peram.put("userId", SharedPreference.retrieveData(context, Constants.USER_ID));
            peram.put("userName", input_firstName.getText().toString().trim() + " " + input_last_name.getText().toString().trim());
            peram.put("description", detail_line.getText().toString().trim());
            peram.put("mobile", input_mobile.getText().toString().trim());
            peram.put("thumbnailPicture", "");
            peram.put("userImage", SharedPreference.retrieveData(context, Constants.USER_IMAGE));

            JSONArray medi = new JSONArray();
            int put_index = 0;
            if (mediaList.size() > 0) {
                for (int md = 0; md < mediaList.size(); md++) {
                    MediaModel mdd = mediaList.get(md);
                    if (!mdd.getServer_path().equalsIgnoreCase(DataBaseStrings.DEFAULT_VALUE)) {
                        JSONObject object = new JSONObject();
                        object.put("url", mdd.getServer_path());
                        object.put("type", mdd.getServer_type());
                        object.put("size", mdd.getServer_size());
                        medi.put(put_index, object);
                        put_index = put_index + 1;
                    }
                }
            }
            peram.put("media", medi);
            new Retrofit2(context, this, UPLOAD_POST, Constants.UPLOAD_POST, peram).callService(true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void uploadFile(String path, String type) {
        Alert.showLog("Path", "path- " + path);
        if (path != null) {
            File file = new File(path);
            RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("photos", file.getName(), reqFile);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(3, TimeUnit.MINUTES)
                    .connectTimeout(2, TimeUnit.MINUTES).build();

            RetrofitService retrofitService = (RetrofitService) new Retrofit.Builder().
                    baseUrl(Constants.MEDIA_BASE)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create()).build()
                    .create(RetrofitService.class);

            String url = Constants.UPLOAD_MEDIA + "/" + type;
            Alert.showLog("UPLOAD", " URL-- " + url);
            Call<ResponseBody> call = retrofitService.uploadMedia(url, filePart);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        JSONObject data = new JSONObject(response.body().string());
                        if (data.getString("success").equalsIgnoreCase("true")) {
                            aBoolean = true;
                            String file_path = data.getString("filepath");
                            String file_type = data.getString("type");
                            String file_size = data.getString("size");
                            String[] name = file_path.split("/");
                            String name_on_server = name[name.length - 1];
                            for (int dt = 0; dt < mediaList.size(); dt++) {
                                MediaModel media = mediaList.get(dt);
                                if (media.getMedia_name_in_device().equalsIgnoreCase(name_on_server)) {
                                    media.setServer_path(file_path);
                                    media.setServer_type(file_type);
                                    media.setServer_size(file_size);
                                    media_adapter.notifyDataSetChanged();
                                }
                            }
                        }
                    } catch (Exception e) {
                        Alert.showLog("Exception", "OKKKKK Exception-- " + e.toString());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Alert.showLog("NOOOO", "NOOOO-- " + t.getLocalizedMessage());
                }
            });
        }
    }

    @Override
    public void onServiceResponse(int i, Response<ResponseBody> response) {
        try {
            JSONObject res = new JSONObject(response.body().string());
            switch (i) {
                case UPLOAD_POST:
                    if (res.getBoolean("success")) {
                        successAlert();
                        final CallService fatchData = new CallService(context);
                        fatchData.setOnUpdate(new CallService.ResponseSynchronous() {
                            @Override
                            public void UpdateData(int val) {
                            }
                        });
                        fatchData.callMyPostItemService();
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*---------------- Contribution send successfully --------------*/
    private void successAlert() {
        eraseFileFromMemoryCard();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.custom_alert_dialog);
        View dialogView = ((Activity) context).getLayoutInflater().inflate(R.layout.sucess_post, null);
        ((TextView) dialogView.findViewById(R.id.title)).setText(getString(R.string.thanks));
        ((TextView) dialogView.findViewById(R.id.title)).setTextColor(ContextCompat.getColor(context, R.color.green));
        ((TextView) dialogView.findViewById(R.id.msg)).setText(getString(R.string.thanks_to_contribute));
        ImageView chk_error = (ImageView) dialogView.findViewById(R.id.chk_error);
        TextView one_more_post = (TextView) dialogView.findViewById(R.id.one_more_post);
        TextView continue_browsing = (TextView) dialogView.findViewById(R.id.continue_browsing);
        TextView view_post = (TextView) dialogView.findViewById(R.id.view_post);
        chk_error.setImageResource(R.drawable.check);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialogg = dialogBuilder.create();
        alertDialogg.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialogg.getWindow().setLayout((int) (((double) getResources().getDisplayMetrics().widthPixels) * 0.9d), -2);
        alertDialogg.getWindow().setBackgroundDrawableResource(R.drawable.alert_back);
        alertDialogg.setCancelable(false);
        alertDialogg.setCanceledOnTouchOutside(false);
        alertDialogg.show();
        one_more_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetLay();
                alertDialogg.dismiss();
            }
        });
        continue_browsing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetLay();
                ((Home) context).setShowCurrentPagerItem(HomePagerAdapter.homeFragment);
                alertDialogg.dismiss();
            }
        });
        view_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableLay();
                alertDialogg.dismiss();
            }
        });
    }
}
