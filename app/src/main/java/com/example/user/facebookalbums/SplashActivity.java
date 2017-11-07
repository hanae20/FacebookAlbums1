package com.example.user.facebookalbums;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by User on 07/11/2017.
 */

public class SplashActivity extends AppCompatActivity {
    private ImageView imageView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        imageView =(ImageView) findViewById(R.id.image);
        Animation anim = AnimationUtils.loadAnimation(this,R.anim.transition);
        imageView.startAnimation(anim);
        final Intent i = new Intent(this,MainActivity.class);
        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally{
                    startActivity(i);
                    finish();

                }
            }
        };
        timer.start();



    }
}
