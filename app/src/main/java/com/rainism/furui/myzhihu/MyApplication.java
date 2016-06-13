package com.rainism.furui.myzhihu;

import android.app.Application;

import com.rainism.furui.myzhihu.Tools.ImageTools;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by furui on 16/5/24.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);

        ImageTools.ImageSqliteHepler imageSqliteHepler = new
                ImageTools.ImageSqliteHepler(getApplicationContext(), "my_zhihu_data.db");
        ImageTools.sqliteDatabase = imageSqliteHepler.getReadableDatabase();
    }
}
