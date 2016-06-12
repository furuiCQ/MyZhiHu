package com.rainism.furui.myzhihu.Tools;

import android.content.Context;
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
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by furui on 16/5/24.
 */
public class ImageTools {
    public static SQLiteDatabase sqliteDatabase;

    /**
     *
     * @param context
     * @param imageView
     * @param imageUrl
     * @param imageType 0为首页图片 1为文章图片
     * @param title
     */
    public static void downlandImageView(final Context context, final ImageView imageView,
                                         String imageUrl, final int imageType, final String title) {
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
                Log.d("","图片下载失败");
            }
        });
    }
    public static void loadImageView(final Context context, final ImageView imageView,
                                     String imageUrl){
        Picasso.with(context).load("file:///"+imageUrl).into(imageView);
    }

    public static String downlandImageToSD(Context context, Bitmap bitmap, String title) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        try {
            FileOutputStream outputStream = context.openFileOutput(Environment.getExternalStorageDirectory().getPath()
                    +"/"+title + ".png", MODE_PRIVATE);
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Environment.getExternalStorageDirectory().getPath()
                +"/"+ title + ".png";
    }

    public static void saveImageFileToDataBase(String title, String imagePath, int imageType) {
        String inserSql="insert into imageCachData(type,imageUrl,imageName) values ('" + imageType
                + "','" + imagePath + "','" + title + "')";
        sqliteDatabase.execSQL(inserSql);
    }
    public static String searchImageFileFromDataBase(String title,int imageType){
        String searchSql="select * from  imageCachData where type="+imageType+" and imageName='"+title+"'";
        Cursor cursor=sqliteDatabase.rawQuery(searchSql,null);
        cursor.moveToFirst();
        String imageUrl="";
        if (cursor.moveToNext()){
            imageUrl=cursor.getString(cursor.getColumnIndex("imageUrl"));
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


    public static void donlandContentToDataBase() {

    }


    public static class ImageSqliteHepler extends SQLiteOpenHelper {
        public String creatSql = "create table if not exists  imageCachData(id int primary key ,type int ,imageUrl varchar(200),imageName varchar(200))";
        //create table tabname(col1 type1 [not null] [primary key],col2 type2 [not null],..)
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
            //execSQL用于执行SQL语句
            db.execSQL(creatSql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            System.out.println("upgrade a database");
        }

    }


}
