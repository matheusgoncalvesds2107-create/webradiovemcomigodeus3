package com.vemcomigodeus.radio;

import android.app.*;import android.content.*;import android.media.*;import android.os.*;import androidx.core.app.NotificationCompat;

public class RadioService extends Service{
 private static final String STREAM="http://sapircast.caster.fm:11743/I3Pqo",CH="radio";private MediaPlayer mp;private boolean stopping=false;
 @Override public void onCreate(){super.onCreate();if(Build.VERSION.SDK_INT>=26){NotificationChannel c=new NotificationChannel(CH,"Web Rádio Vem Comigo Deus",NotificationManager.IMPORTANCE_LOW);getSystemService(NotificationManager.class).createNotificationChannel(c);}}
 @Override public int onStartCommand(Intent i,int f,int id){if(i!=null&&"STOP".equals(i.getAction())){stopping=true;stopPlay();stopSelf();return START_NOT_STICKY;}stopping=false;startForeground(9,new NotificationCompat.Builder(this,CH).setSmallIcon(android.R.drawable.ic_media_play).setContentTitle("Web Rádio Vem Comigo Deus").setContentText("Transmissão ao vivo").setOngoing(true).build());play();return START_STICKY;}
 private void play(){try{stopPlay();mp=new MediaPlayer();mp.setAudioStreamType(AudioManager.STREAM_MUSIC);mp.setDataSource(STREAM);mp.setWakeMode(this,PowerManager.PARTIAL_WAKE_LOCK);mp.setOnPreparedListener(MediaPlayer::start);mp.setOnErrorListener((m,w,e)->{if(!stopping)new Handler().postDelayed(this::play,5000);return true;});mp.prepareAsync();}catch(Exception e){if(!stopping)new Handler().postDelayed(this::play,5000);}}
 private void stopPlay(){try{if(mp!=null){mp.stop();mp.release();}}catch(Exception ignored){}mp=null;}
 @Override public void onDestroy(){stopping=true;stopPlay();super.onDestroy();}@Override public android.os.IBinder onBind(Intent i){return null;}
}