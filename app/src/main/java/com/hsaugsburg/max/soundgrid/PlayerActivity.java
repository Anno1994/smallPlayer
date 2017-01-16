package com.hsaugsburg.max.soundgrid;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import java.util.Timer;
import java.util.TimerTask;

public class PlayerActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    AudioManager audioManager;
    boolean isPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        Intent intent = getIntent();

        // get the filename from the mainActivity and detect the resourceId with this info
        String message  = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        int resourceId  = getResources().getIdentifier(message, "raw", "com.hsaugsburg.max.soundgrid");
        mediaPlayer     = MediaPlayer.create(this, resourceId);
        mediaPlayer.start();

        // set the thumbnail image to the imageView
        int imageId     = getResources().getIdentifier(message, "drawable", "com.hsaugsburg.max.soundgrid");
        ImageView image = (ImageView) findViewById(R.id.imageView);
        image.setImageResource(imageId);

        // === VOLUME CONTROL ===
        // use this lines of code to access the android system volume and detect the current & max value
        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        // set the detect values to our volumeControlBar now
        final SeekBar volumeControl = (SeekBar) findViewById(R.id.volumeControl);
        volumeControl.setMax(maxVolume);
        volumeControl.setProgress(currentVolume);

        volumeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        // === SCRUBBER CONTROL ===
        final SeekBar scrubber = (SeekBar) findViewById(R.id.scrubber);
        scrubber.setMax(mediaPlayer.getDuration());

        // use this timerTask to update the process to the current state where the music is playing
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                scrubber.setProgress(mediaPlayer.getCurrentPosition());
            }
        }, 0, 300);

        scrubber.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mediaPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.start();
            }
        });

    }

    // === CLICK FUNCTIONS ===
    public void restart(View view) {

        mediaPlayer.seekTo(0);
        mediaPlayer.start();

    }

    public void pauseResume(View view) {
        if (isPaused) {

            mediaPlayer.start();
            isPaused = false;
            ((Button)view).setText("Pause");

        } else {

            mediaPlayer.pause();
            isPaused = true;
            ((Button)view).setText("Resume");

        }
    }

    public void mainMenu(View view) {

        mediaPlayer.stop();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

}
