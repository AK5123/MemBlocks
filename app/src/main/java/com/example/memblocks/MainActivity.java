package com.example.memblocks;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {
    ImageView[] img = new ImageView[12];
    String[] arr = {"p1","p1","p2","p2","p3","p3","p4","p4","p5","p5","p6","p6"};
    Integer[] imgids = {R.id.IV0,R.id.IV1,R.id.IV2,R.id.IV3,R.id.IV4,R.id.IV5,R.id.IV6,R.id.IV7,R.id.IV8,R.id.IV9,R.id.IV10,R.id.IV11};
    Integer[] global = {-1,-1};
    Integer score = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<String> strList = Arrays.asList(arr);
        Collections.shuffle(strList);
        arr = strList.toArray(new String[strList.size()]);
        for(int j=0;j<12;j++){
                 final Integer i = j;
                 img[i] = findViewById(imgids[i]);
                 img[i].setImageBitmap(setCurve("gen"));
                 img[i].setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                           new LongOperation(i).execute();
                     }
                 });
        }

    }
    public class LongOperation extends AsyncTask<Void, Void, String> {
        Integer i;
        public LongOperation(Integer i) {
            super();
            this.i = i;
        }
        @Override
        protected void onPreExecute() {
            toggle(img[this.i],arr[this.i]);
        }

        @Override
        protected String doInBackground(Void... params) {
                try{
                    sleep(600);
                } catch (Exception e){
                    System.out.println(e);
                }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            checkState(this.i);
        }
    }

    public void checkState(Integer i){
        if(global[0] == -1 &&global[1] == -1){
            global[0] = i;
        }
        else if(global[0] != -1 && global[1] == -1) {
            global[1] = i;
            if (arr[global[0]].equals(arr[global[1]])) {
                //inc score
                score++;
                img[global[0]].setVisibility(View.GONE);
                img[global[1]].setVisibility(View.GONE);
                global[0] = -1;
                global[1] = -1;
            } else {
                toggleGen(img[global[0]],img[global[1]]);
                global[1] = -1;
                global[0] = -1;
            }
        }
    }

    public void toggleGen(ImageView i1,ImageView i2){
        toggle(i1, "gen");
        toggle(i2, "gen");
    }

    public Bitmap setCurve(String a){
        int toggleID = getResources().getIdentifier(a , "drawable" , getPackageName()) ;
        Bitmap mbitmap=((BitmapDrawable) getResources().getDrawable(toggleID)).getBitmap();
        Bitmap imageRounded= Bitmap.createBitmap(mbitmap.getWidth(), mbitmap.getHeight(), mbitmap.getConfig());
        Canvas canvas=new Canvas(imageRounded);
        Paint mpaint=new Paint();
        mpaint.setAntiAlias(true);
        mpaint.setShader(new BitmapShader(mbitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        canvas.drawRoundRect((new RectF(0, 0, mbitmap.getWidth(), mbitmap.getHeight())), 100, 100, mpaint);
        return imageRounded;
    }
    public void toggle(ImageView imgx, String ax){
        final String a = ax;
        final ImageView img = imgx;

        final ObjectAnimator oa1 = ObjectAnimator.ofFloat(img, "scaleX", 1f, 0f);
        final ObjectAnimator oa2 = ObjectAnimator.ofFloat(img, "scaleX", 0f, 1f);
        oa1.setInterpolator(new DecelerateInterpolator());
        oa2.setInterpolator(new AccelerateDecelerateInterpolator());
        oa1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                int resID = getResources().getIdentifier(a , "drawable" , getPackageName()) ;
//                int toggleID = getResources().getIdentifier(a , "drawable" , getPackageName()) ;
////                int ratId = img.getId();
//                if(img.getId() == resID){
//                    img.setBackgroundResource(toggleID);
//                } else {
//                    Toast.makeText(MainActivity.this, resID+" "+img.getId(), Toast.LENGTH_SHORT).show();
//                    img.setBackgroundResource(resID);
//                    Toast.makeText(MainActivity.this, resID+" "+img.getId(), Toast.LENGTH_SHORT).show();
//
//                }

                Bitmap mbitmap=((BitmapDrawable) getResources().getDrawable(resID)).getBitmap();
                Bitmap imageRounded= Bitmap.createBitmap(mbitmap.getWidth(), mbitmap.getHeight(), mbitmap.getConfig());
                Canvas canvas=new Canvas(imageRounded);
                Paint mpaint=new Paint();
                mpaint.setAntiAlias(true);
                mpaint.setShader(new BitmapShader(mbitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                canvas.drawRoundRect((new RectF(0, 0, mbitmap.getWidth(), mbitmap.getHeight())), 100, 100, mpaint); // Round Image Corner 100 100 100 100
                img.setImageBitmap(imageRounded);
//                img.setImageResource(resID);
                oa2.start();
            }
        });
        oa1.start();
        oa1.setDuration(250);
        oa2.setDuration(250);

    }
}
