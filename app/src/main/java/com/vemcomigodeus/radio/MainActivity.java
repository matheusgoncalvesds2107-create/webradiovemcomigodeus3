package com.vemcomigodeus.radio;

import android.content.*;import android.media.AudioManager;import android.os.*;import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;import org.json.*;import java.io.*;import java.net.*;import java.text.SimpleDateFormat;import java.util.*;

public class MainActivity extends AppCompatActivity{
 private static final String BASE="https://webradiovemcomigodeus.onrender.com"; private boolean playing=false;private Button play;private TextView title,status,clock;private AudioManager am;private final Handler h=new Handler(Looper.getMainLooper());
 @Override public void onCreate(Bundle b){super.onCreate(b);setContentView(R.layout.activity_main);play=findViewById(R.id.play);title=findViewById(R.id.title);status=findViewById(R.id.status);clock=findViewById(R.id.clock);am=(AudioManager)getSystemService(AUDIO_SERVICE);
 play.setOnClickListener(v->{if(playing)stop();else start();});findViewById(R.id.minus).setOnClickListener(v->am.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_LOWER,AudioManager.FLAG_SHOW_UI));findViewById(R.id.plus).setOnClickListener(v->am.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_RAISE,AudioManager.FLAG_SHOW_UI));findViewById(R.id.share).setOnClickListener(v->share());
 findViewById(R.id.bible).setOnClickListener(v->toast("Bíblia Sagrada — próxima etapa."));findViewById(R.id.dev).setOnClickListener(v->toast("Devocional — próxima etapa."));findViewById(R.id.word).setOnClickListener(v->toast("Palavra do Dia — próxima etapa."));
 h.post(loop);}
 private void start(){Intent i=new Intent(this,RadioService.class);i.setAction("PLAY");if(Build.VERSION.SDK_INT>=26)startForegroundService(i);else startService(i);playing=true;play.setText("⏸");status.setText("AO VIVO");}
 private void stop(){Intent i=new Intent(this,RadioService.class);i.setAction("STOP");startService(i);playing=false;play.setText("▶");status.setText("PAUSADA");}
 private void share(){Intent i=new Intent(Intent.ACTION_SEND);i.setType("text/plain");i.putExtra(Intent.EXTRA_TEXT,"Ouça a Web Rádio Vem Comigo Deus: http://sapircast.caster.fm:11743/I3Pqo");startActivity(Intent.createChooser(i,"Compartilhar rádio"));}
 private final Runnable loop=new Runnable(){public void run(){clock.setText(new SimpleDateFormat("HH:mm",Locale.getDefault()).format(new Date()));new Thread(()->{try{HttpURLConnection c=(HttpURLConnection)new URL(BASE+"/api/status").openConnection();c.setConnectTimeout(5000);c.setReadTimeout(5000);String x=read(c.getInputStream());JSONObject j=new JSONObject(x);JSONObject p=j.optJSONObject("current");runOnUiThread(()->title.setText(p!=null?p.optString("nome","Web Rádio Vem Comigo Deus"):"Louvores que Edificam"));}catch(Exception ignored){}}).start();h.postDelayed(this,10000);}};
 private String read(InputStream i)throws Exception{ByteArrayOutputStream b=new ByteArrayOutputStream();byte[] x=new byte[2048];int n;while((n=i.read(x))!=-1)b.write(x,0,n);return b.toString("UTF-8");}
 private void toast(String s){Toast.makeText(this,s,Toast.LENGTH_SHORT).show();}
}