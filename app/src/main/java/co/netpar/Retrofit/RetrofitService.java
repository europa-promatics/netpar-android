package co.netpar.Retrofit;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface RetrofitService {
    @GET
    Call<ResponseBody> callGetService(@Url String str);

    @GET
    Call<ResponseBody> callGetService(@Url String str, @Body RequestBody requestBody);

    @POST
    Call<ResponseBody> callPostService(@Url String str, @Body RequestBody requestBody);

    @GET
    Call<ResponseBody> download(@Url String str);

    @Multipart
    @POST
    Call<ResponseBody> uploadMedia(@Url String str,@Part MultipartBody.Part filePart);
}
