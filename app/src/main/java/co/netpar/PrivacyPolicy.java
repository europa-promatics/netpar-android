package co.netpar;

import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import co.netpar.Comman.Alert;

public class PrivacyPolicy extends AppCompatActivity {

    private ImageView back_icon;
    private WebView privacy_policy;
    private AlertDialog progressBar1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= 21) {
            window.setStatusBarColor(getResources().getColor(R.color.top_bar_bg_color));
        }
        window.setBackgroundDrawableResource(R.color.gray_back);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        progressBar1= Alert.startDialog(this);
        privacy_policy=(WebView)findViewById(R.id.privacy_policy);
        back_icon=(ImageView)findViewById(R.id.back_icon);
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        loadData();
    }

    private void loadData()
    {
        String material_downloadable_url = "www.netpar.in/privacy/privacy-policy.pdf";
        privacy_policy.getSettings().setJavaScriptEnabled(true);
        privacy_policy.loadUrl("http://docs.google.com/gview?embedded=true&url="+material_downloadable_url);
        privacy_policy.getSettings().setBuiltInZoomControls(true);
        privacy_policy.getSettings().setSupportZoom(false);
        privacy_policy.setWebViewClient(new MyWebViewClient());
    }

    class MyWebViewClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon)
        {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            progressBar1.show();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url)
        {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            progressBar1.dismiss();
        }
    }

}
