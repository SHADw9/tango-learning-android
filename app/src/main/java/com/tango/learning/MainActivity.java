package com.tango.learning;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends Activity implements TextToSpeech.OnInitListener {

    private WebView web;
    private TextToSpeech tts;
    private boolean ttsInit = false, japaneseOk = false, installPrompted = false;
    private String pending;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        tts = new TextToSpeech(this, this);

        web = new WebView(this);
        setContentView(web);

        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);      // keeps progress (localStorage) between launches
        s.setAllowFileAccess(true);
        s.setCacheMode(WebSettings.LOAD_DEFAULT);

        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView v, WebResourceRequest r) {
                Uri u = r.getUrl();
                if ("file".equals(u.getScheme())) return false;
                try { startActivity(new Intent(Intent.ACTION_VIEW, u)); } catch (Exception ignored) {}
                return true;
            }
        });
        web.addJavascriptInterface(new Bridge(), "AndroidTTS");

        if (state != null) web.restoreState(state);
        else web.loadUrl("file:///android_asset/index.html");
    }

    @Override
    protected void onSaveInstanceState(Bundle out) {
        super.onSaveInstanceState(out);
        web.saveState(out);
    }

    /* ---- text-to-speech (Android native engine) ---- */
    @Override
    public void onInit(int status) {
        if (status != TextToSpeech.SUCCESS) {
            toast("No text-to-speech engine found on this device");
            return;
        }
        int r = tts.setLanguage(Locale.JAPAN);
        japaneseOk = r != TextToSpeech.LANG_MISSING_DATA && r != TextToSpeech.LANG_NOT_SUPPORTED;
        tts.setSpeechRate(0.85f);
        ttsInit = true;
        if (pending != null) { String p = pending; pending = null; speak(p); }
    }

    private void speak(String text) {
        if (!ttsInit) { pending = text; return; }
        if (!japaneseOk) {
            toast("Japanese voice not installed. Please install it, then try again.");
            if (!installPrompted) {
                installPrompted = true;
                try { startActivity(new Intent(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA)); }
                catch (ActivityNotFoundException ignored) {}
            }
            return;
        }
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tango");
    }

    /* ---- status/navigation bar colour follows the page theme ---- */
    private void applyBars(String hex) {
        try {
            int c = Color.parseColor(hex);
            getWindow().setStatusBarColor(c);
            getWindow().setNavigationBarColor(c);
            double lum = 0.299 * Color.red(c) + 0.587 * Color.green(c) + 0.114 * Color.blue(c);
            getWindow().getDecorView().setSystemUiVisibility(lum > 150
                    ? View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR : 0);
        } catch (IllegalArgumentException ignored) {}
    }

    private void toast(String m) { Toast.makeText(this, m, Toast.LENGTH_LONG).show(); }

    private class Bridge {
        @JavascriptInterface public void speak(String t) {
            if (t == null || t.isEmpty()) return;
            runOnUiThread(() -> MainActivity.this.speak(t));
        }
        @JavascriptInterface public void setBars(String hex) {
            runOnUiThread(() -> applyBars(hex));
        }
    }

    @Override
    public void onBackPressed() {
        if (web.canGoBack()) web.goBack(); else super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (tts != null) { tts.stop(); tts.shutdown(); }
        if (web != null) web.destroy();
        super.onDestroy();
    }
}
