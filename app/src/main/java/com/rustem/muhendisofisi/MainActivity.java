package com.rustem.muhendisofisi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
    private WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        w.setStatusBarColor(Color.parseColor("#1F2937"));
        w.setNavigationBarColor(Color.parseColor("#FFFFFF"));

        web = new WebView(this);
        setContentView(web);
        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);   // oyun kaydı (localStorage)
        s.setTextZoom(100);
        s.setMediaPlaybackRequiresUserGesture(false);
        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri u = request.getUrl();
                if ("file".equals(u.getScheme())) return false;
                try { startActivity(new Intent(Intent.ACTION_VIEW, u)); } catch (Exception ignored) { }
                return true;
            }
        });
        web.loadUrl("file:///android_asset/www/index.html");
    }

    @Override
    protected void onPause() {
        super.onPause();
        // arka plana geçerken oyunu kaydet ve durdur
        if (web != null) web.evaluateJavascript("window.appPause && window.appPause()", null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (web != null) web.evaluateJavascript("window.appResume && window.appResume()", null);
    }

    @Override
    public void onBackPressed() {
        web.evaluateJavascript("(window.appBack ? window.appBack() : false)", value -> {
            if ("true".equals(value)) return;
            finish();
        });
    }
}
