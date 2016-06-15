package com.rainism.furui.myzhihu.Tools;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;


/**
 * Created by furui on 16/5/24.
 */
public class ImageTools {
    public static SQLiteDatabase sqliteDatabase;

    /**
     * @param context
     * @param imageView
     * @param imageUrl
     * @param imageType 0为首页图片 1为文章图片
     * @param title
     */
    public static void downlandImageView(final Context context, final ImageView imageView,
                                         String imageUrl, final int imageType, final String title) {
        String localimageUrl = searchImageFileFromDataBase(title, imageType);

        if (localimageUrl.equals("") || localimageUrl == null) {
            Picasso.with(context).load(imageUrl).into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    //图片存本地
                    //将图片地址存入数据库
                    saveImageFileToDataBase(title, downlandImageToSD(context,
                            drawableToBitmap(imageView.getDrawable()), title), imageType);
                }

                @Override
                public void onError() {
                    Log.d("", "图片下载失败");
                }
            });
        } else {
            Log.d("downlandImageView", "加载本地图片资源:" + localimageUrl);
            loadImageView(context, imageView, localimageUrl);
        }

    }

    public static void loadImageView(final Context context, final ImageView imageView,
                                     String imageUrl) {
        Log.d("imageUrl",imageUrl);
        Log.d("imageView",imageView.toString());

        Picasso.with(context).load("file:///" + imageUrl).into(imageView);
    }

    public static String downlandImageToSD(Context context, Bitmap bitmap, String title) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        String imageUrl = "";
        try {
            imageUrl = Environment.getExternalStorageDirectory().getPath()
                    + "/" + title + ".png";
            if (Environment.getExternalStorageDirectory().getPath().equals("")) {
                imageUrl = context.getFilesDir() + "/" + title + ".png";
            }
            File file = new File(imageUrl);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageUrl;
    }

    public static void saveImageFileToDataBase(String title, String imagePath, int imageType) {
        changeImageFileToDataBase(title, imagePath, imageType);
    }

    public static void changeImageFileToDataBase(String title, String imagePath, int imageType) {
        String imageUrl = searchImageFileFromDataBase(title, imageType);
        String sql = "";
        if (imageUrl.equals("") || imageUrl == null) {
            //insert
            sql = "insert into imageCachData(type,imageUrl,imageName) values ('" + imageType
                    + "','" + imagePath + "','" + title + "')";
        } else {
            sql = "update imageCachData set imageUrl = '" + imageUrl + "' where type = " + imageType + " and imageName='" + title + "'";
        }
        sqliteDatabase.execSQL(sql);

    }

    public static String searchImageFileFromDataBase(String title, int imageType) {
        String searchSql = "select * from  imageCachData where type=" + imageType + " and imageName='" + title + "'";
        Cursor cursor = sqliteDatabase.rawQuery(searchSql, null);
        cursor.moveToFirst();
        String imageUrl = "";
        if (cursor.moveToFirst()) {
            imageUrl = cursor.getString(cursor.getColumnIndex("imageUrl"));
        }
        return imageUrl;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }


    public static void donlandContentToDataBase(String title, String body, int type) {
        String bodyUrl = Environment.getExternalStorageDirectory().getPath()
                + "/" + title + ".txt";
          File file = new File(bodyUrl);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] bytes = body.getBytes();
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        changeContentFileToDataBase(title, bodyUrl, type);
    }

    public static void changeContentFileToDataBase(String date, String bodyUrl, int type) {
        String contentUrl = searchMainNewsFileFromDataBase(date, type);
        String sql = "";
        if (contentUrl.equals("") || contentUrl == null) {
            //insert
            sql = "insert into newsCachData(type,date,bodyUrl) values ('" + type
                    + "','" + date + "','" + bodyUrl + "')";
        } else {
            sql = "update newsCachData set bodyUrl = '" + contentUrl + "' where type = " + type + " and date='" + date + "'";
        }
        sqliteDatabase.execSQL(sql);
    }
    public static String getBeforeDate(String lastDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(lastDay.substring(0,4)),
                Integer.parseInt(lastDay.substring(4,6)),
                Integer.parseInt(lastDay.substring(6,8)));
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if (month < 10) {
            Log.d("beforeData", "" + year + "0" + month + day);
            lastDay = "" + year + "0" + month + day;
        } else {
            Log.d("beforeData", "" + year + month + day);
            lastDay = "" + year + month + day;
        }
        Log.i("getBeforeDate","时间减一:"+lastDay);
        return lastDay;
    }
    public static String searchMainNewsFileFromDataBase(String lastDay, int contentType) {
        if(contentType==1){
            lastDay=getBeforeDate(lastDay);
        }
        String searchSql = "select * from  newsCachData where type=" + contentType + " and date='" + lastDay + "'";
        Log.d("searchSql",searchSql);
        Cursor cursor = sqliteDatabase.rawQuery(searchSql, null);
        cursor.moveToFirst();
        Log.d("searchMainNewsFileFromDataBase", cursor.toString());
        String bodyUrl = "";
        if (cursor.moveToFirst()) {
            bodyUrl = cursor.getString(cursor.getColumnIndex("bodyUrl"));
        }
        return bodyUrl;
    }

    public static class ImageSqliteHepler extends SQLiteOpenHelper {
        public String creatImageSql = "create table if not exists  imageCachData(id INTEGER primary key ,type int ,imageUrl varchar(200),imageName varchar(200))";
        //type 为0 为欢迎页文件，type为1则为新闻相关图片

        public String creatContentSql = "create table if not exists newsCachData(id INTEGER primary key ,type int ,date varchar(200),bodyUrl varchar(200))";
        //type 为0则为今日新闻 type为1则为过去新闻 2为新闻内容 date用于存储当天的新闻时间标记 ，bodyUrl用于存储本地缓存的网络数据请求

        private static final int VERSION = 2;

        public ImageSqliteHepler(Context context, String name) {
            this(context, name, null, VERSION);
        }

        public ImageSqliteHepler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            System.out.println("create a database");
            db.execSQL(creatImageSql);
            db.execSQL(creatContentSql);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            System.out.println("upgrade a database");
        }

    }


}
