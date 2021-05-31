package com.example.music;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    MediaPlayer player;
    AudioManager audioManager;
    int n=0;
    int[] songs = {R.raw.on_my_way,R.raw.elefante,R.raw.friends,R.raw.perfect,R.raw.pretty_girl};
    int[] songs_img = {R.drawable.on_my_way,R.drawable.elefante,R.drawable.friends,R.drawable.perfect,R.drawable.pretty_girl};
    String[] songs_name = {"Alan Walker - On My Way","NK - Elefante","Marshmello & Annie Marie - Friends","Ed Sheran - Perfect", "Maggie Linderman - Pretty Girl"};
    ArrayList<Integer> times_played= new ArrayList<>(0);
    ArrayList<Integer> vol_tapped= new ArrayList<>(0);
    // for playing the music
    public void play(View view){
        player.start();
        if (times_played.size()%2==0){
            player.start();
            ImageView play = findViewById(R.id.playbut);
            play.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);

        }
        else{
            player.pause();
            ImageView play = findViewById(R.id.playbut);
            play.setImageResource(R.drawable.ic_baseline_play_circle_filled_24);
        }
        times_played.add(0);


    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SeekBar seekProg = findViewById(R.id.seekProg);

        ImageView btn =  findViewById(R.id.next);
        btn.setClickable(true);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (n<songs.length-1){
                    n++;
                }
                else{
                    n=0;
                }
                player.stop();
                player = MediaPlayer.create(getApplicationContext(),songs[n]);
                TextView song_name = (TextView) findViewById(R.id.song_name);
                song_name.setText(songs_name[n]);
                ImageView song_img= (ImageView) findViewById(R.id.song_img);
                song_img.setImageResource(songs_img[n]);
                player.start();
                seekProg.setMax(player.getDuration());
                player.seekTo(0);

            }
        });

        ImageView btn2 = (ImageView) findViewById(R.id.prev);
        btn2.setClickable(true);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (n>0){
                    n--;
                }
                else{
                    n=songs.length-1;
                }
                player.stop();
                player = MediaPlayer.create(getApplicationContext(),songs[n]);
                TextView song_name = (TextView) findViewById(R.id.song_name);
                song_name.setText(songs_name[n]);
                ImageView song_img= (ImageView) findViewById(R.id.song_img);
                song_img.setImageResource(songs_img[n]);
                player.start();
                seekProg.setMax(player.getDuration());
                player.seekTo(0);

            }
        });
        player = MediaPlayer.create(this,songs[n]);
        TextView song_name = (TextView) findViewById(R.id.song_name);
        song_name.setText(songs_name[n]);
        ImageView song_img= (ImageView) findViewById(R.id.song_img);
        song_img.setImageResource(songs_img[n]);
        seekProg.setMax(player.getDuration());


        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                btn.performClick();
            }
        });


        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);



        SeekBar seekVol = findViewById(R.id.seekVol);
        seekVol.setMax(maxVol);
        seekVol.setProgress(curVol);

        ImageView btn3 = (ImageView)findViewById(R.id.vol_but);
        btn3.setClickable(true);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vol_tapped.size()%2==0){
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                    seekVol.setProgress(0);
                    btn3.setImageResource(R.drawable.ic_baseline_volume_off_24);
                }
                else{
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVol/2, 0);
                    seekVol.setProgress(3*(maxVol/4));
                    btn3.setImageResource(R.drawable.ic_baseline_volume_up_24);

                }
                vol_tapped.add(0);
            }
        });

        seekVol.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });




        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                seekProg.setProgress(player.getCurrentPosition());

            }
        }, 0, 900);

        seekProg.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            double min,sec;
            String seconds="00";

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                player.seekTo(progress);
                sec=(double)progress/1000;
                min=progress/(1000*60);
                if (sec<60){
                    if (sec<10){
                        seconds="0"+(int)sec;
                    }
                    else{
                        seconds=""+(int)sec;
                    }
                }
                else{
                    int x=(int)sec-(((int)(sec/60))*60);
                    if (x<10){
                        seconds="0"+x;
                    }
                    else{
                        seconds=""+x;
                    }

                }
                String time= String.valueOf((int) min+":"+seconds);
                TextView time_lapse=(TextView)findViewById(R.id.time_lapse);
                time_lapse.setText(time);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
}

