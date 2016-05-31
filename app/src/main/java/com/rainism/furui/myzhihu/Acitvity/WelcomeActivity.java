package com.rainism.furui.myzhihu.Acitvity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.rainism.furui.myzhihu.R;
import com.rainism.furui.myzhihu.Tools.ImageTools;
import com.rainism.furui.myzhihu.Tools.URLModel;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;


public class WelcomeActivity extends Activity {

    ImageView welcomeImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weclome);

        welcomeImageView = (ImageView) findViewById(R.id.welcome_imageview);


        Log.d("长", "" + getWindowManager().getDefaultDisplay().getWidth());
        Log.d("宽", "" + getWindowManager().getDefaultDisplay().getHeight());

        OkHttpUtils.get().
                url(URLModel.URL_START_IMAGE + getWindowManager().getDefaultDisplay().getWidth() + "*" + getWindowManager().getDefaultDisplay().getHeight())
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                if (e != null) {
                    Log.e("Exception", e.toString());
                }
            }

            @Override
            public void onResponse(String s) {

                Log.d("返回结果", s);

                JSONObject result = null;
                try {
                    result = new JSONObject(s);
                    String imageUrl = result.getString("img");
                    ImageTools.downlandImageView(WelcomeActivity.this, welcomeImageView, imageUrl);

                    Animation animation = AnimationUtils.loadAnimation(WelcomeActivity.this, R.anim.change_big);
                    //开始执行动画
                    welcomeImageView.startAnimation(animation);
                    Timer timer = new Timer();
                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                            WelcomeActivity.this.startActivity(intent);
                        }
                    };
                    timer.schedule(timerTask, 3000);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Exception", e.toString());
                }

            }
        });
    }


}
