package com.rainism.furui.myzhihu;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.rainism.furui.myzhihu.Tools.ImageTools;
import com.tencent.bugly.crashreport.CrashReport;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by furui on 16/5/24.
 */
public class MyApplication extends Application {
    public static String CRASH_APPID="900034416";
    @Override
    public void onCreate() {
        super.onCreate();
      
        CrashReport.initCrashReport(getApplicationContext(), CRASH_APPID, false);
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
