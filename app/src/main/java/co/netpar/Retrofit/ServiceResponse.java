package co.netpar.Retrofit;

import okhttp3.ResponseBody;
import retrofit2.Response;

public interface ServiceResponse {
    void onServiceResponse(int i, Response<ResponseBody> response);
}
