package com.tank171.qrgenerator;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Base64;
import java.io.OutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
        requestLegacyStoragePermissionIfNeeded();
        showSplash();
        new Handler(Looper.getMainLooper()).postDelayed(this::showApp, 2000);
    }


    private void requestLegacyStoragePermissionIfNeeded() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P &&
            checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 171);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 171) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                android.widget.Toast.makeText(this,
                    "Izin penyimpanan diberikan. Sekarang QR bisa diunduh.",
                    android.widget.Toast.LENGTH_LONG).show();
            } else {
                new android.app.AlertDialog.Builder(this)
                    .setTitle("Izin penyimpanan diperlukan")
                    .setMessage("Agar QR bisa disimpan ke album Pictures/QR Generator 171, izinkan akses penyimpanan. Jika sebelumnya ditolak, buka Pengaturan aplikasi > Izin.")
                    .setPositiveButton("Mengerti", (dialog, which) -> dialog.dismiss())
                    .show();
            }
        }
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
        webView.getSettings().setUserAgentString(webView.getSettings().getUserAgentString() + " QRGenerator171Android");
        webView.addJavascriptInterface(new QrImageSaver(), "QR171Android");
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        root.removeAllViews();
        root.addView(webView, new FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        webView.loadUrl(HOME_URL);
    }

    private class QrImageSaver {
        @JavascriptInterface
        public String savePng(String base64Png) {
            if (base64Png == null || base64Png.trim().isEmpty()) return "ERROR|Data gambar kosong";
            Uri uri = null;
            try {
                // Android 9 and older require runtime storage permission and direct file writing.
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P &&
                    checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    runOnUiThread(() -> requestPermissions(
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 171));
                    return "ERROR|Izinkan akses penyimpanan pada dialog Android, lalu tekan Unduh Gambar sekali lagi";
                }
                byte[] bytes = Base64.decode(base64Png, Base64.DEFAULT);
                String stamp = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.US).format(new Date());
                String fileName = "QR-Generator-171-" + stamp + ".png";
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    File album = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "QR Generator 171");
                    if (!album.exists() && !album.mkdirs()) return "ERROR|Tidak bisa membuat album QR Generator 171";
                    File imageFile = new File(album, fileName);
                    try (FileOutputStream stream = new FileOutputStream(imageFile)) {
                        stream.write(bytes);
                        stream.flush();
                    }
                    android.media.MediaScannerConnection.scanFile(MainActivity.this,
                        new String[]{imageFile.getAbsolutePath()}, new String[]{"image/png"}, null);
                } else {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
                    values.put(MediaStore.Images.Media.RELATIVE_PATH,
                        Environment.DIRECTORY_PICTURES + "/QR Generator 171");
                    values.put(MediaStore.Images.Media.IS_PENDING, 1);
                    uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    if (uri == null) return "ERROR|Android tidak dapat membuat file gambar";
                    try (OutputStream stream = getContentResolver().openOutputStream(uri)) {
                        if (stream == null) throw new IOException("Tidak bisa membuka file tujuan");
                        stream.write(bytes);
                        stream.flush();
                    }
                    ContentValues ready = new ContentValues();
                    ready.put(MediaStore.Images.Media.IS_PENDING, 0);
                    getContentResolver().update(uri, ready, null, null);
                }
                return "OK|" + fileName;
            } catch (Exception e) {
                if (uri != null) {
                    try { getContentResolver().delete(uri, null, null); } catch (Exception ignored) {}
                }
                String message = e.getMessage();
                return "ERROR|" + (message == null ? "Gagal menyimpan gambar" : message);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) webView.goBack();
        else super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (webView != null) webView.destroy();
        super.onDestroy();
    }
}
