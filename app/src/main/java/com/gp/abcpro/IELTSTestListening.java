package com.gp.abcpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class IELTSTestListening extends AppCompatActivity {
    MediaPlayer mPlayer;
    SeekBar seekBar;
    Handler handler;
    Runnable runnable;
    Button next;
    FloatingActionButton stop, repeat, play, pause;
    int track;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ielts_test_listening);
        next = findViewById(R.id.btnNext);
        play = findViewById(R.id.play);
        pause = findViewById(R.id.pause);
        stop = findViewById(R.id.stop);
        repeat = findViewById(R.id.repeat);
        track = R.raw.track;
        seekBar = findViewById(R.id.seekBar);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPlayer != null) {
                    mPlayer.stop();
                }
                Intent intent = new Intent(IELTSTestListening.this, IELTSTestListeningQus.class);
                startActivity(intent);
                finish();
            }
        });

        handler = new Handler();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void playCycle() {
        seekBar.setProgress(mPlayer.getCurrentPosition());
        if (mPlayer.isPlaying()) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    playCycle();
                }
            };
            handler.postDelayed(runnable, 200);
        }
    }

    public void Pause(View view) {
        if (mPlayer != null) {
            pause.setVisibility(View.GONE);
            play.setVisibility(View.VISIBLE);
            mPlayer.pause();
        }
    }

    public void Play(View view) {
        if (mPlayer == null) {
            mPlayer = MediaPlayer.create(this, track);
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            seekBar.setMax(mPlayer.getDuration());
            mPlayer.start();
            playCycle();
            pause.setVisibility(View.VISIBLE);
            play.setVisibility(View.GONE);
            stop.setVisibility(View.VISIBLE);
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stop.setVisibility(View.GONE);
                    play.setVisibility(View.GONE);
                    pause.setVisibility(View.GONE);
                    repeat.setVisibility(View.VISIBLE);
                }
            });
        }
        pause.setVisibility(View.VISIBLE);
        stop.setVisibility(View.VISIBLE);
        play.setVisibility(View.GONE);
        mPlayer.start();
    }

    public void Stop(View view) {
        if (mPlayer != null) {
            mPlayer.pause();
            mPlayer.seekTo(0);
            seekBar.setProgress(mPlayer.getCurrentPosition());
            pause.setVisibility(View.GONE);
            play.setVisibility(View.VISIBLE);
            stop.setVisibility(View.GONE);
        }

    }

    public void repeat(View view) {
        mPlayer.seekTo(0);
        seekBar.setProgress(mPlayer.getCurrentPosition());
        stop.setVisibility(View.VISIBLE);
        pause.setVisibility(View.VISIBLE);
        repeat.setVisibility(View.GONE);
        mPlayer.start();
    }

    @Override
    public void onBackPressed() {
    }
}