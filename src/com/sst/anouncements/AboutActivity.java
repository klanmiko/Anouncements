package com.sst.anouncements;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
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
        final TextView about = (TextView) findViewById(R.id.abouttext);
        Animation incanum = AnimationUtils.loadAnimation(this, R.anim.incanim);
        final ImageView inclogo = (ImageView) findViewById(R.id.imageView);
        final Animation leaveanim = new TranslateAnimation(0, 0, 0, -3000);
        leaveanim.setDuration(2000);
        leaveanim.setInterpolator(this, android.R.interpolator.accelerate_cubic);

        final ImageView ragul = (ImageView) findViewById(R.id.imageView3);
        ragul.setVisibility(View.INVISIBLE);
        final Animation ragulanim = AnimationUtils.loadAnimation(this, R.anim.ragul);
        final TextView ragultext = (TextView) findViewById(R.id.ragul);
        ragultext.setVisibility(View.INVISIBLE);
        final Animation ratextanum = AnimationUtils.loadAnimation(this, R.anim.titletext);
        Animation.AnimationListener endRagul = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ragul.setVisibility(View.INVISIBLE);
                ragultext.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
        ragulanim.setAnimationListener(endRagul);
        Animation.AnimationListener lister = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                image.setVisibility(View.INVISIBLE);

                about.setVisibility(View.INVISIBLE);
                inclogo.setVisibility(View.INVISIBLE);
                ragul.setVisibility(View.VISIBLE);

                ragul.startAnimation(ragulanim);
                ragultext.setVisibility(View.VISIBLE);

                ragultext.startAnimation(ratextanum);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
        leaveanim.setAnimationListener(lister);
        image.startAnimation(hyperspaceJump);
        about.startAnimation(textanum);
        Animation.AnimationListener inclist = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                image.startAnimation(leaveanim);
                about.startAnimation(leaveanim);
                inclogo.startAnimation(leaveanim);
                /*
                image.setVisibility(View.INVISIBLE);

                about.setVisibility(View.INVISIBLE);
                inclogo.setVisibility(View.INVISIBLE);

                ragul.startAnimation(ragulanim);
                ragul.setVisibility(View.VISIBLE);

                ragultext.startAnimation(ratextanum);
                ragultext.setVisibility(View.VISIBLE);
                */

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
        incanum.setAnimationListener(inclist);
        inclogo.startAnimation(incanum);


    }
}
