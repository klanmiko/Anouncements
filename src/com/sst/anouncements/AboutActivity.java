package com.sst.anouncements;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by eternitysst on 10/9/13.
 */
public class AboutActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.about);
        final ImageView image = (ImageView) findViewById(R.id.imageView2);
        Animation hyperspaceJump = AnimationUtils.loadAnimation(this, R.anim.incog);
        Animation.AnimationListener imlist = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                image.setAlpha(0.3f);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
        hyperspaceJump.setAnimationListener(imlist);
        Animation textanum = AnimationUtils.loadAnimation(this, R.anim.textalpha);
        TextView about = (TextView) findViewById(R.id.abouttext);
        Animation incanum = AnimationUtils.loadAnimation(this, R.anim.incanim);
        ImageView inclogo = (ImageView) findViewById(R.id.imageView);
        image.startAnimation(hyperspaceJump);
        about.startAnimation(textanum);
        inclogo.startAnimation(incanum);
    }
}
