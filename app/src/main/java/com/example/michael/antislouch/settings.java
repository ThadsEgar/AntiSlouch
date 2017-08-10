package com.example.michael.antislouch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.media.MediaPlayer;
import android.widget.Button;

public class settings extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //create media player
        final MediaPlayer soundEnable = MediaPlayer.create(this, R.raw.chimesup);

        //Enable sound button/Create Sound button
        Button playSoundButton = (Button)findViewById(R.id.Sound_Enable);
        playSoundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundEnable.start();
            }
        });



    }



}
