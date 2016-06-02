package com.rainism.furui.myzhihu.Acitvity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainism.furui.myzhihu.Model.News;
import com.rainism.furui.myzhihu.Model.NewsContent;
import com.rainism.furui.myzhihu.R;
import com.rainism.furui.myzhihu.Tools.ImageTools;
import com.rainism.furui.myzhihu.Tools.URLModel;
import com.rainism.furui.myzhihu.View.ContentWebView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

public class ContentActivity extends Activity {
    NewsContent newsContent=new NewsContent();
    ContentWebView webView;

    ImageView headerViewImageView;
    TextView headerViewTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
         if(getIntent().getExtras()!=null){
             News news=(News)getIntent().getExtras().getSerializable("news");
             Log.d("news.title:", "" + news.getId());
             getNewsContent(news.getId());
         }

        initView();




    }
    public void initView(){
        webView=(ContentWebView)findViewById(R.id.content_webview);
        View headerView = getLayoutInflater().inflate(R.layout.main_banner_view, null, false);
        headerViewImageView = (ImageView) headerView.findViewById(R.id.banner_imageview);
        headerViewImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        headerViewTextView = (TextView) headerView.findViewById(R.id.banner_textview);
        webView.setEmbeddedTitleBar(headerView);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
    }



    public void getNewsContent(long newsId){
        OkHttpUtils.get().url(URLModel.URL_NEWS_CONTENT+newsId).build().execute(new StringCallback() {
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
                        JSONObject jsonObject = new JSONObject(response);
                        newsContent.setBody(jsonObject.getString("body"));
                        newsContent.setImageSource(jsonObject.getString("image_source"));
                        newsContent.setTitle(jsonObject.getString("title"));
                        newsContent.setImageUrl(jsonObject.getString("image"));
                        newsContent.setShareUrl(jsonObject.getString("share_url"));
                        newsContent.setJsArray(jsonObject.getJSONArray("js"));
                        //   newsContent.setRecommendersArray(jsonObject.getJSONArray("recommenders"));
                        newsContent.setGaPrefix(jsonObject.getString("ga_prefix"));
                 /*      JSONObject sectionObject=jsonObject.getJSONObject("section");
                        Section section=new Section();
                        section.setThumbnail(sectionObject.getString("thumbnail"));
                        section.setId(sectionObject.getLong("id"));
                        section.setName(sectionObject.getString("name"));
                     newsContent.setSection(section);*/
                        newsContent.setType(jsonObject.getInt("type"));
                        newsContent.setId(jsonObject.getLong("id"));
                        newsContent.setCss(jsonObject.getJSONArray("css"));

                        ImageTools.downlandImageView(ContentActivity.this, headerViewImageView, newsContent.getImageUrl());
                        headerViewTextView.setText(newsContent.getTitle());
                        Log.d("body", newsContent.getBody());


                        webView.loadData(newsContent.getBody(), "text/html; charset=UTF-8", null);//这种写法可以正确解码
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
