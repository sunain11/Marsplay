package com.example.sunain.marsplay.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.sunain.marsplay.R;
import com.hanks.htextview.base.AnimationListener;
import com.hanks.htextview.base.HTextView;
import com.hanks.htextview.line.LineTextView;

public class SplashScreenActivity extends AppCompatActivity {
    Handler handler;
    LineTextView hTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashScreenActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);
        hTextView=findViewById(R.id.h_textview);
        hTextView.setAnimationListener(new SimpleAnimationListener(this));
        hTextView.setProgress(50);
    }

//    class ClickListener implements View.OnClickListener {
//        @Override
//        public void onClick(View v) {
//            if (v instanceof HTextView) {
//                if (index + 1 >= sentences.length) {
//                    index = 0;
//                }
//                ((HTextView) v).animateText(sentences[index++]);
//            }
//        }
//    }

    class SimpleAnimationListener implements AnimationListener {

        private Context context;

        public SimpleAnimationListener(Context context) {
            this.context = context;
        }
        @Override
        public void onAnimationEnd(HTextView hTextView) {
            }
    }
}
