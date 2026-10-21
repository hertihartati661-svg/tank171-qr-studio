package com.tank171.qrgenerator;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity {
    private static final String HOME_URL = "https://hertihartati661-svg.github.io/tank171-qr-studio/";
    private FrameLayout root;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.rgb(6, 20, 45));
        getWindow().setNavigationBarColor(Color.rgb(6, 20, 45));
        root = new FrameLayout(this);
        root.setBackgroundColor(Color.rgb(6, 20, 45));
        setContentView(root);
        showSplash();
        new Handler(Looper.getMainLooper()).postDelayed(this::showApp, 2000);
    }

    private void showSplash() {
        LinearLayout splash = new LinearLayout(this);
        splash.setOrientation(LinearLayout.VERTICAL);
        splash.setGravity(Gravity.CENTER);
        splash.setPadding(28, 24, 28, 24);
        splash.setBackgroundColor(Color.rgb(6, 20, 45));

        ImageView logo = new ImageView(this);
        logo.setImageResource(R.drawable.ic_qr171);
        LinearLayout.LayoutParams logoParams = new LinearLayout.LayoutParams(220, 220);
        logoParams.bottomMargin = 24;
        splash.addView(logo, logoParams);

        TextView title = new TextView(this);
        title.setText("QR GENERATOR 171");
        title.setTextColor(Color.rgb(235, 250, 255));
        title.setTextSize(25);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        splash.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText("Buat QR Code dengan mudah");
        subtitle.setTextColor(Color.rgb(0, 217, 255));
        subtitle.setTextSize(14);
        subtitle.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams subParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        subParams.topMargin = 10;
        splash.addView(subtitle, subParams);

        ProgressBar progress = new ProgressBar(this);
        progress.setIndeterminate(true);
        LinearLayout.LayoutParams progressParams = new LinearLayout.LayoutParams(48, 48);
        progressParams.topMargin = 28;
        splash.addView(progress, progressParams);

        root.removeAllViews();
        root.addView(splash, new FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    }

    private void showApp() {
        webView = new WebView(this);
        webView.setBackgroundColor(Color.rgb(6, 20, 45));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowFileAccess(false);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        root.removeAllViews();
        root.addView(webView, new FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        webView.loadUrl(HOME_URL);
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) webView.destroy();
        super.onDestroy();
    }
}
