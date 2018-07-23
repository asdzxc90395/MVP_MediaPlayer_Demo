package com.example.cjcucsie.musicplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.cjcucsie.musicplayer.Model.MainModel;
import com.example.cjcucsie.musicplayer.Presenter.MainPresenter;
import com.example.cjcucsie.musicplayer.View.MainView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements MainView{

    private MainPresenter mainPresenter;

    Button playBtn;
    SeekBar positionBar;
    SeekBar volumeBar;
    TextView elapsedTimeLadel;
    TextView remainingTimeLadel;
    MediaPlayer mp;
    int totalTime;
    SerVice ss ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playBtn = (Button) findViewById(R.id.playBtn);
        elapsedTimeLadel = (TextView) findViewById(R.id.elapsedTimeLadel);
        remainingTimeLadel = (TextView) findViewById(R.id.remainingTimeLadel);

        Intent intent = new Intent(this.getApplicationContext(),SerVice.class);
        startService(intent);

        ServiceConnection ServiceConnection =new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
               ss = ((SerVice.MyBinder)iBinder).getService();
                //Toast.makeText(MainActivity.this,"connect",Toast.LENGTH_LONG).show(); show word
                try {
                   ss.SVbinder(null,10);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        boolean b = bindService(intent, ServiceConnection, Context.BIND_AUTO_CREATE);

        //Position Bar
        positionBar = (SeekBar) findViewById(R.id.positionBar);
        positionBar.setMax(totalTime);
        positionBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mp.seekTo(progress);
                    positionBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        //Volume Bar
        volumeBar = (SeekBar)  findViewById((R.id.volumeBar));
        volumeBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volumNum = progress /100f;
                mp.setVolume(volumNum,volumNum);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Thread (Update positionBar &timeLabel)
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(mp!=null){
                    try {
                        Message msg = new Message();
                        msg.what = mp.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    }catch (InterruptedException e){

                    }
                }
            }
        }).start();
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;
            //Update positionBar
            positionBar.setProgress(currentPosition);

            //Update Labels
            String elapsedTime = mainPresenter.returncreateTimeLabel(currentPosition);
            elapsedTimeLadel.setText(elapsedTime);

            String remainingTime = mainPresenter.returncreateTimeLabel(totalTime - currentPosition);
            remainingTimeLadel.setText("- " + remainingTime);
        }
    };

   public void playBtnClick(View view) {
       mainPresenter.checkBtnClick(view);
   }


}
