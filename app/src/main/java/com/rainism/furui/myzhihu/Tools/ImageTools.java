package com.rainism.furui.myzhihu.Tools;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


/**
 * Created by furui on 16/5/24.
 */
public class ImageTools {

    public static void downlandImageView(Context context,ImageView imageView,String imageUrl){
        Picasso.with(context).load(imageUrl).into(imageView);
    }
}
