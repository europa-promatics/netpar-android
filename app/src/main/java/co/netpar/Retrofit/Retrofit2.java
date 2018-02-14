package co.netpar.Retrofit;

import android.app.Activity;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import co.netpar.R;
import co.netpar.Comman.Alert;
import co.netpar.Comman.Constants;
import co.netpar.Network.ConnectionDetector;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.TlsVersion;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class Retrofit2 {
    public static Context mContext;
    private Call<ResponseBody> call;
    private String name;
    private Builder notificationBuilder;
    private Integer notificationID = Integer.valueOf(100);
    private NotificationManager notificationManager;
    private AlertDialog pd;
    private JSONObject postParam;
    private int requestCode;
    private ServiceResponse result;
    private String storagePath;
    private String url;
    public static final int UNSUCCESSFUL =5000;
    class C04692 implements ProgressListener {
        C04692() {
        }

        public void update(long bytesRead, long contentLength, boolean done) {
            Retrofit2.this.ProgressBarNotification((int) ((100 * bytesRead) / contentLength));
        }
    }

    /*------------- Used To GET Type data-------------*/
    public Retrofit2(Context mContext, ServiceResponse result, int requestCode, String url) {
        this.mContext = mContext;
        this.result = result;
        this.requestCode = requestCode;
        this.url = url;
    }

    /*------------- Used To POST Type data-------------*/
    public Retrofit2(Context mContext, ServiceResponse result, int requestCode, String url, JSONObject postParam) {
        this.mContext = mContext;
        this.result = result;
        this.requestCode = requestCode;
        this.url = url;
        this.postParam = postParam;
    }

    /*------------- Used To download data-------------*/
    public Retrofit2(String url, Context mContext, String storagePath, String name) {
        this.url = url;
        this.mContext = mContext;
        this.storagePath = storagePath;
        this.name = name;
    }

    /*------------- Hit Get Post Service-------------*/
    public void callService(boolean dialog) {
        if (ConnectionDetector.isInternetAvailable(mContext))
        {
            if (dialog) {
              pd=Alert.startDialog(mContext);
            }

                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .readTimeout(3, TimeUnit.MINUTES)
                        .connectTimeout(2, TimeUnit.MINUTES)
                        .build();

                System.setProperty("http.keepAlive", "false");
                RetrofitService retrofitService = (RetrofitService) new Retrofit.Builder().
                        baseUrl(Constants.BASE)
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(RetrofitService.class);
                Alert.showLog("URL", "URL- " + this.url);
                if (postParam == null) {
                    Alert.showLog("Get", "Get- ");
                    call = retrofitService.callGetService(this.url);
                } else {
                    Alert.showLog("Params", "Params- " + this.postParam.toString());
                    call = retrofitService.callPostService(url, RequestBody.create(MediaType.parse("application/json"), this.postParam.toString()));
                }
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try
                        {
                            if (pd!=null)
                            {
                                if(pd.isShowing())
                                    pd.cancel();
                            }

                            if (response.isSuccessful()) {
                                result.onServiceResponse(Retrofit2.this.requestCode, response);
                            }
                            else
                            {
                                result.onServiceResponse(UNSUCCESSFUL, response);
                                Alert.showLog("UN","unSuccessful- "+response.message());
                            }
                        }
                        catch (Exception e)
                        {e.printStackTrace();}
                    }
                    public void onFailure(Call<ResponseBody> call, Throwable t)
                    {
                        Alert.showLog("onFailure","onFailure- "+t.getMessage()+" Exception- "+t.toString());
                        Alert.showLog("getCause","getCause- "+t.getCause().getMessage());
                        try
                        {
                            if (pd!=null)
                            {
                                if(pd.isShowing())
                                    pd.cancel();
                            }
                        }
                        catch (Exception e)
                        {e.printStackTrace();}
                        call.cancel();
                    }
                });
                return;
        }
        else
        {
           // Alert.showToast(mContext, mContext.getString(R.string.no_connection));
        }
    }

    /*------------- Used To download data-------------*/
    public void download()
    {
        if (!ConnectionDetector.isInternetAvailable(mContext)) {
            Alert.showToast(mContext, mContext.getString(R.string.no_connection));
        }
        else if (VERSION.SDK_INT < 23 || ContextCompat.checkSelfPermission(mContext, "android.permission.WRITE_EXTERNAL_STORAGE") == 0)
        {
            ProgressBarNotification(0);
            final ProgressListener progressListener = new C04692();
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.MINUTES)
                    .connectTimeout(5, TimeUnit.MINUTES)
                    .addNetworkInterceptor(new Interceptor()
                    {
                      public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException
                      {okhttp3.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder().body(new ProgressResponseBody(originalResponse.body(), progressListener)).build();
                }
            }).build();
            System.setProperty("http.keepAlive", "false");

            call = ((RetrofitService) new Retrofit.Builder()
                    .baseUrl(Constants.DOWNLOAD_BASE)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create()).build().create(RetrofitService.class))
                    .download(url);
            Alert.showLog("URL","Downloaded Url-- "+url);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
                {
                    if (response.isSuccessful())
                    {
                        try
                        {
                            File file = new File(storagePath, name);
                            file.createNewFile();
                            if (file.exists())
                            {
                                OutputStream fileOutputStream = new FileOutputStream(file);
                                IOUtils.write(((ResponseBody) response.body()).bytes(), fileOutputStream);
                                fileOutputStream.close();
                                try {
                                    Alert.showToast(mContext, mContext.getString(R.string.article_downloaded));
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                            Alert.showLog("", ex.toString());
                        }
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    call.cancel();
                    Alert.showToast(mContext, mContext.getString(R.string.download_failed));
                }
            });
        }
        else
        {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        }
    }

    private void ProgressBarNotification(int prog)
    {
        notificationManager = (NotificationManager) mContext.getSystemService("notification");
        notificationBuilder = new Builder(mContext);
        notificationBuilder.setContentTitle(name)
                .setContentText(name)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.notif_logo)
                .setProgress(100, prog, false);
        if (VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
              if (prog == 100)
              {
                notificationBuilder.setContentText( mContext.getString(R.string.article_downloaded)).setProgress(0, 0, false);
                notificationManager.notify(this.notificationID.intValue(), this.notificationBuilder.build());
              }
            this.notificationManager.notify(this.notificationID.intValue(), this.notificationBuilder.build());
        }
    }


    public static OkHttpClient getUnsafeOkHttpClient()
    {
        try
        {
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };
            //SSLContext sslContext = SSLContext.getInstance("SSL");
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                    .tlsVersions(TlsVersion.TLS_1_2)
                    .cipherSuites(CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                            CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384)
                    .build();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.readTimeout(3, TimeUnit.MINUTES);
            builder.connectTimeout(2, TimeUnit.MINUTES);
            builder.connectionSpecs(Collections.singletonList(spec));
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    Alert.showLog("hostname","hostname-- "+hostname+"  SSLSession getProtocol"+session.getProtocol());
                    Alert.showLog("hostname","session.getCipherSuite()-- "+session.getCipherSuite());
                    Alert.showLog("hostname","session.getPeerHost()-- "+session.getPeerHost());
                    Alert.showLog("hostname","session.getValueNames()-- "+session.getValueNames().toString());
                    return true;
                }
            });
            return builder.build();
        }
        catch (Exception e)
        {
            Alert.showLog("OkHttpClient","OkHttpClient-- "+e.toString()+" "+e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
