package com.vemcomigodeus.radio;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Button play;
    private TextView status, clock;
    private boolean playing = false;
    private AudioManager audioManager;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);

        play = findViewById(R.id.play);
        status = findViewById(R.id.status);
        clock = findViewById(R.id.clock);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        play.setOnClickListener(v -> {
            if (playing) stopRadio();
            else startRadio();
        });

        findViewById(R.id.minus).setOnClickListener(v ->
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI)
        );

        findViewById(R.id.plus).setOnClickListener(v ->
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI)
        );

        findViewById(R.id.share).setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT, "Ouça a Web Rádio Vem Comigo Deus: https://sapircast.caster.fm:11743/I3Pqo?token=8d4e69c8f08e4f85e822c0aa569a65d5");
            startActivity(Intent.createChooser(i, "Compartilhar rádio"));
        });

        findViewById(R.id.bible).setOnClickListener(v -> toast("Bíblia Sagrada — vamos ligar esta parte depois."));
        findViewById(R.id.dev).setOnClickListener(v -> toast("Devocional — vamos ligar esta parte depois."));
        findViewById(R.id.word).setOnClickListener(v -> toast("Palavra do Dia — vamos ligar esta parte depois."));

        handler.post(clockLoop);
    }

    private void startRadio() {
        Intent i = new Intent(this, RadioService.class);
        i.setAction("PLAY");
        if (Build.VERSION.SDK_INT >= 26) startForegroundService(i);
        else startService(i);
        playing = true;
        play.setText("⏸");
        status.setText("Conectando ao sinal...");
        handler.postDelayed(() -> status.setText("AO VIVO"), 1800);
    }

    private void stopRadio() {
        Intent i = new Intent(this, RadioService.class);
        i.setAction("STOP");
        startService(i);
        playing = false;
        play.setText("▶");
        status.setText("PAUSADA");
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
