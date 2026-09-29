package com.vemcomigodeus.radio;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private WebView web;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);

        web = findViewById(R.id.web);
        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setMediaPlaybackRequiresUserGesture(false);
        s.setAllowFileAccess(true);
        s.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        web.setWebChromeClient(new WebChromeClient());
        web.loadDataWithBaseURL(
            "https://www.caster.fm/",
            html(),
            "text/html",
            "UTF-8",
            null
        );
    }

    private String html() {
        return "<!doctype html><html><head>"
            + "<meta name='viewport' content='width=device-width,initial-scale=1,maximum-scale=1'>"
            + "<style>"
            + "html,body{margin:0;padding:0;background:#04120C;color:#fff;font-family:sans-serif;}"
            + ".wrap{padding:14px;text-align:center}.card{background:#0B2418;border:1px solid #24583D;border-radius:20px;padding:16px;}"
            + "h2{margin:6px 0;color:#EFC35A}.live{color:#1FB86A;font-weight:700;margin-bottom:12px}"
            + ".hint{color:#A8BBB0;font-size:14px;margin-top:12px}"
            + "</style></head><body>"
            + "<div class='wrap'><div class='card'>"
            + "<div class='live'>● AO VIVO</div>"
            + "<h2>Web Rádio Vem Comigo Deus</h2>"
            + "<div data-type='newStreamPlayer' "
            + "data-publicToken='bf175b49-6917-4ae4-aef3-ddae6a7bcd28' "
            + "data-theme='light' data-color='e81e4d' "
            + "data-channelId='a2d79e54-e2e1-45b1-b390-a1b861cce12c' "
            + "data-rendered='false' class='cstrEmbed'>"
            + "<a href='https://www.caster.fm'>Shoutcast Hosting</a> "
            + "<a href='https://www.caster.fm'>Stream Hosting</a> "
            + "<a href='https://www.caster.fm'>Radio Server Hosting</a>"
            + "</div>" 
            + "<script src='https://cdn.cloud.caster.fm/widgets/embed.js'></script>"
            + "<div class='hint'>Toque no play do player para ouvir a transmissão.</div>"
            + "</div></div></body></html>";
    }

    @Override
    public void onBackPressed() {
        if (web != null && web.canGoBack()) web.goBack();
        else super.onBackPressed();
    }
}
