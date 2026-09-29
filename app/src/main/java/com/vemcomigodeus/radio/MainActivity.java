package com.vemcomigodeus.radio;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private WebView web;
    private AudioManager audioManager;
    private TextView clock;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);

        web = findViewById(R.id.web);
        clock = findViewById(R.id.clock);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setMediaPlaybackRequiresUserGesture(true);
        s.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        web.setWebChromeClient(new WebChromeClient());
        web.setBackgroundColor(0x00000000);
        web.loadDataWithBaseURL("https://www.caster.fm/", html(), "text/html", "UTF-8", null);

        findViewById(R.id.minus).setOnClickListener(v ->
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI)
        );
        findViewById(R.id.plus).setOnClickListener(v ->
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI)
        );

        findViewById(R.id.share).setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT, "Ouça a Web Rádio Vem Comigo Deus");
            startActivity(Intent.createChooser(i, "Compartilhar rádio"));
        });

        findViewById(R.id.bible).setOnClickListener(v -> toast("Bíblia Sagrada — vamos ligar esta parte depois."));
        findViewById(R.id.dev).setOnClickListener(v -> toast("Devocional — vamos ligar esta parte depois."));
        findViewById(R.id.word).setOnClickListener(v -> toast("Palavra do Dia — vamos ligar esta parte depois."));

        handler.post(clockLoop);
    }

    private String html() {
        return "<!doctype html><html><head>"
            + "<meta name='viewport' content='width=device-width,initial-scale=1,maximum-scale=1'>"
            + "<style>"
            + "html,body{margin:0;padding:0;background:transparent;color:#fff;font-family:sans-serif;overflow:hidden;}"
            + ".holder{width:100%;height:210px;overflow:hidden;display:flex;align-items:flex-start;justify-content:center;}"
            + ".scale{width:112%;transform:scale(.80);transform-origin:top center;}"
            + ".cstrEmbed{width:100%!important;}"
            + "</style></head><body>"
            + "<div class='holder'><div class='scale'>"
            + "<div data-type='newStreamPlayer' "
            + "data-publicToken='bf175b49-6917-4ae4-aef3-ddae6a7bcd28' "
            + "data-theme='dark' data-color='1FB86A' "
            + "data-channelId='a2d79e54-e2e1-45b1-b390-a1b861cce12c' "
            + "data-rendered='false' class='cstrEmbed'>"
            + "<a href='https://www.caster.fm'>Shoutcast Hosting</a> "
            + "<a href='https://www.caster.fm'>Stream Hosting</a> "
            + "<a href='https://www.caster.fm'>Radio Server Hosting</a>"
            + "</div>"
            + "<script src='https://cdn.cloud.caster.fm/widgets/embed.js'></script>"
            + "</div></div></body></html>";
    }

    private final Runnable clockLoop = new Runnable() {
        @Override public void run() {
            clock.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
            handler.postDelayed(this, 1000);
        }
    };

    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
