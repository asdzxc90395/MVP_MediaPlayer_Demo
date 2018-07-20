package com.example.cjcucsie.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;

import java.io.IOException;

public class SerVice extends Service {
    MediaPlayer mp;
    int totalTime;
    private Environment Environmet;
    String s1[] = new String[]{
            Environmet.getExternalStorageDirectory()+"/music/runsky.mp3",
            Environmet.getExternalStorageDirectory()+"/music/15year.mp3",
            Environmet.getExternalStorageDirectory()+"/music/CHIHIRO.mp3"
    };
    @Override
    public IBinder onBind(Intent intent) {
       return new MyBinder();
    }

    @Override
    public void onCreate() {
        //Media Player
        mp = MediaPlayer.create(this,R.raw.music);
        mp.setLooping(true);
        try {
            mp.setDataSource(s1[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mp.seekTo(0);
        mp.setVolume(0.5f,0.5f);
        totalTime = mp.getDuration();
        super.onCreate();
    }

    public MediaPlayer SVbinder(MediaPlayer sp, int totalTime) throws IOException {
        sp = mp;
        sp.setLooping(true);
        sp.seekTo(0);
        sp.setVolume(0.5f,0.5f);
        totalTime = sp.getDuration();
        sp.start();
        return sp;
    }
class MyBinder extends Binder {
        public SerVice getService(){
            return SerVice.this;
        }
}
}
