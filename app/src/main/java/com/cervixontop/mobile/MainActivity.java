package com.cervixontop.mobile;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class MainActivity extends Activity {
    private WebView web;
    private ProgressBar loading;
    private final Handler handler = new Handler();

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);
        web = findViewById(R.id.web); loading = findViewById(R.id.loading);
        WebSettings s = web.getSettings(); s.setJavaScriptEnabled(true); s.setDomStorageEnabled(true); s.setDatabaseEnabled(true);
        s.setAllowFileAccess(true); s.setAllowContentAccess(true);
        web.setWebViewClient(new WebViewClient());
        startNodeService();
        waitForServer(0);
    }

    private void startNodeService() {
        Intent i = new Intent(this, NodeService.class);
        if (Build.VERSION.SDK_INT >= 26) startForegroundService(i); else startService(i);
    }

    private void waitForServer(int attempt) {
        new Thread(() -> {
            boolean ok = false;
            try { java.net.HttpURLConnection c=(java.net.HttpURLConnection)new java.net.URL("http://127.0.0.1:8787/").openConnection(); c.setConnectTimeout(500); c.setReadTimeout(500); c.setRequestMethod("GET"); ok=c.getResponseCode()==200; c.disconnect(); } catch(Exception ignored) {}
            final boolean ready=ok;
            handler.post(() -> { if(ready){ loading.setVisibility(View.GONE); web.loadUrl("http://127.0.0.1:8787/"); } else if(attempt<80) waitForServer(attempt+1); });
        }).start();
    }

    @Override public void onBackPressed(){ if(web.canGoBack()) web.goBack(); else super.onBackPressed(); }
}
