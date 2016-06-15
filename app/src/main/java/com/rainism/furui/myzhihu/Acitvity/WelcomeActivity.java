package com.rainism.furui.myzhihu.Acitvity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
        if (ContextCompat.checkSelfPermission(WelcomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(WelcomeActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
        welcomeImageView = (ImageView) findViewById(R.id.welcome_imageview);


        Log.d("长", "" + getWindowManager().getDefaultDisplay().getWidth());
        Log.d("宽", "" + getWindowManager().getDefaultDisplay().getHeight());
        Log.d("首页图片地址", "" + ImageTools.searchImageFileFromDataBase("首页", 0));

        if(!ImageTools.searchImageFileFromDataBase("首页",0).equals("")){
            ImageTools.loadImageView(this,welcomeImageView,ImageTools.searchImageFileFromDataBase("首页",0));
            goNextActivity();
        }else{
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
                    Log.d("首页返回结果", s);
                    JSONObject result = null;
                    try {
                        result = new JSONObject(s);
                        String imageUrl = result.getString("img");
                        ImageTools.downlandImageView(WelcomeActivity.this, welcomeImageView, imageUrl,0,"首页");
                        goNextActivity();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("Exception", e.toString());
                    }

                }
            });
        }
    }
    public void goNextActivity(){
        Animation animation = AnimationUtils.loadAnimation(WelcomeActivity.this, R.anim.change_big);
        //开始执行动画
        welcomeImageView.startAnimation(animation);
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                WelcomeActivity.this.startActivity(intent);
                finish();
            }
        };
        timer.schedule(timerTask, 3000);

    }


}
