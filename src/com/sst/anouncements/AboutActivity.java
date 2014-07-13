package com.sst.anouncements;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by eternitysst on 10/9/13.
 */
public class AboutActivity extends Activity {
    boolean swagmode = false;
    SharedPreferences mpref;
    int counter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.about);
        final ImageView image = (ImageView) findViewById(R.id.imageView2);
        Animation hyperspaceJump = AnimationUtils.loadAnimation(this, R.anim.incog);
        final TextView swag = (TextView) findViewById(R.id.swag);

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
        mpref = getPreferences(Context.MODE_PRIVATE);
        swagmode = mpref.getBoolean("Swag", false);
        hyperspaceJump.setAnimationListener(imlist);
        final Animation textanum = AnimationUtils.loadAnimation(this, R.anim.textalpha);
        final ImageView ragulswag = (ImageView) findViewById(R.id.imageView6);
        final Animation swaganim = AnimationUtils.loadAnimation(this, R.anim.ragul);
        final ObjectAnimator anim = ObjectAnimator.ofFloat(image, "alpha", 0f, 1f);
        anim.setDuration(1000);
        Animation.AnimationListener endswag = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ragulswag.setVisibility(View.INVISIBLE);
                swag.setVisibility(View.INVISIBLE);
                image.setVisibility(View.VISIBLE);
                anim.start();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
        swaganim.setAnimationListener(endswag);
        final TextView about = (TextView) findViewById(R.id.abouttext);
        Animation incanum = AnimationUtils.loadAnimation(this, R.anim.incanim);
        final ImageView inclogo = (ImageView) findViewById(R.id.imageView);
        final Animation leaveanim = new TranslateAnimation(0, 0, 0, -3000);
        leaveanim.setDuration(2000);
        leaveanim.setInterpolator(this, android.R.interpolator.accelerate_cubic);
        final ImageView klan = (ImageView) findViewById(R.id.imageView4);
        final ImageView dieklan = (ImageView) findViewById(R.id.imageView5);
        klan.setVisibility(View.INVISIBLE);
        dieklan.setVisibility(View.INVISIBLE);
        final ImageView ragul = (ImageView) findViewById(R.id.imageView3);
        ragul.setVisibility(View.INVISIBLE);
        final Animation ragulanim = AnimationUtils.loadAnimation(this, R.anim.ragul);
        final TextView ragultext = (TextView) findViewById(R.id.ragul);
        final TextView yolo = (TextView) findViewById(R.id.yolo);
        final TextView klantext = (TextView) findViewById(R.id.klan);
        ragultext.setVisibility(View.INVISIBLE);
        final Animation ratextanum = AnimationUtils.loadAnimation(this, R.anim.titletext);
        final Animation klananim = AnimationUtils.loadAnimation(this, R.anim.ragul);
        final Animation dieklananim = AnimationUtils.loadAnimation(this, R.anim.ragul);
        final Animation kltextanum = AnimationUtils.loadAnimation(this, R.anim.titletext);
        final Animation yolotextanum = AnimationUtils.loadAnimation(this, R.anim.titletext);
        final Animation swagtextanum = AnimationUtils.loadAnimation(this, R.anim.titletext);

        Animation.AnimationListener enddklan = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dieklan.setVisibility(View.INVISIBLE);
                yolo.setVisibility(View.INVISIBLE);
                ragul.setVisibility(View.VISIBLE);
                ragul.startAnimation(ragulanim);
                ragultext.setVisibility(View.VISIBLE);
                ragultext.startAnimation(ratextanum);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
        dieklananim.setAnimationListener(enddklan);
        Animation.AnimationListener endRagul = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ragul.setVisibility(View.INVISIBLE);
                ragultext.setVisibility(View.INVISIBLE);
                if (swagmode) {
                    ragulswag.setVisibility(View.VISIBLE);
                    ragulswag.startAnimation(swaganim);
                    swag.setVisibility(View.VISIBLE);
                    swag.startAnimation(swagtextanum);
                } else {
                    image.setVisibility(View.VISIBLE);
                    anim.start();
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
        ragulanim.setAnimationListener(endRagul);
        final Animation.AnimationListener endklan = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                klan.setVisibility(View.INVISIBLE);
                klantext.setVisibility(View.INVISIBLE);
                if (swagmode) {
                    dieklan.setVisibility(View.VISIBLE);
                    dieklan.startAnimation(dieklananim);
                    yolo.setVisibility(View.VISIBLE);
                    yolo.startAnimation(yolotextanum);
                } else {
                    ragul.setVisibility(View.VISIBLE);
                    ragul.startAnimation(ragulanim);
                    ragultext.setVisibility(View.VISIBLE);

                    ragultext.startAnimation(ratextanum);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
        klananim.setAnimationListener(endklan);
        Animation.AnimationListener lister = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                image.setVisibility(View.INVISIBLE);

                about.setVisibility(View.INVISIBLE);
                inclogo.setVisibility(View.INVISIBLE);
                klan.setVisibility(View.VISIBLE);
                klan.startAnimation(klananim);
                klantext.setVisibility(View.VISIBLE);
                klantext.startAnimation(kltextanum);

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

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        int action = e.getActionMasked();
        if (action == MotionEvent.ACTION_UP) {
            counter++;
            if (counter > 5) {
                Toast toast = Toast.makeText(this, "Swag mode activated", Toast.LENGTH_SHORT);
                toast.show();
                swagmode = true;
                SharedPreferences.Editor editor = mpref.edit();
                editor.putBoolean("Swag", true);
                editor.commit();
            }
        }
        return true;
    }
}
