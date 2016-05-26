package com.rainism.furui.myzhihu.Acitvity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.rainism.furui.myzhihu.Model.News;
import com.rainism.furui.myzhihu.Model.TopNews;
import com.rainism.furui.myzhihu.R;
import com.rainism.furui.myzhihu.Tools.ImageTools;
import com.rainism.furui.myzhihu.Tools.URLModel;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;


public class MainActivity extends Activity {

    ListView mainListView;
    ConvenientBanner convenientBanner;

    ArrayList<News> newsList = new ArrayList<News>();
    ArrayList<TopNews> topNewsList = new ArrayList<TopNews>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainListView = (ListView) findViewById(R.id.main_listview);
        convenientBanner = new ConvenientBanner(MainActivity.this);
        convenientBanner.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,300));


        mainListView.addHeaderView(convenientBanner);
        OkHttpUtils.get().url(URLModel.URL_TODAY_NEWS).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                if (e != null) {
                    Log.e("Exception", e.toString());
                }
            }

            @Override
            public void onResponse(String response) {
                if (response != null) {
                    Log.d("response", response);
                    try {
                        JSONObject result = new JSONObject(response);
                        JSONArray stories = result.getJSONArray("stories");

                        for (int i = 0; i < stories.length(); i++) {
                            JSONObject item = stories.getJSONObject(i);
                            JSONArray images = item.getJSONArray("images");
                            ArrayList<String> imagesList = new ArrayList<String>();
                            for (int j = 0; j < images.length(); j++) {
                                String imageUrl = images.getString(j);
                                imagesList.add(imageUrl);
                            }
                            int type = item.getInt("type");
                            long id = item.getLong("id");
                            String ga_prefix = item.getString("ga_prefix");
                            String title = item.getString("title");
                            News news = new News(imagesList, type, id, ga_prefix, title);
                            newsList.add(news);
                        }
                        mainListView.setAdapter(null);



                        JSONArray top_stories = result.getJSONArray("top_stories");
                        for (int i = 0; i < top_stories.length(); i++) {
                            JSONObject item = top_stories.getJSONObject(i);
                            String image = item.getString("image");
                            int type = item.getInt("type");
                            long id = item.getLong("id");
                            String ga_prefix = item.getString("ga_prefix");
                            String title = item.getString("title");
                            TopNews topNews = new TopNews(image, type, id, ga_prefix, title);
                            topNewsList.add(topNews);


                        }
                        Log.d("topNewsList.size",""+topNewsList.size());

                        convenientBanner.setPages(
                                new CBViewHolderCreator<LocalImageHolderView>() {
                                    @Override
                                    public LocalImageHolderView createHolder() {
                                        return new LocalImageHolderView();
                                    }
                                }, topNewsList)
                                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);

                    } catch (JSONException e) {
                        if (e != null) {
                            Log.e("Exception", e.toString());
                        }
                    }

                }
            }
        });

    }

    public class LocalImageHolderView implements Holder<TopNews> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, TopNews data) {
            Log.d("data.getImageUrl():",data.getImageUrl());
            ImageTools.downlandImageView(MainActivity.this, imageView, data.getImageUrl());
        }
    }

}
