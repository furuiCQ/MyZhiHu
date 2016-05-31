package com.rainism.furui.myzhihu.Acitvity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.rainism.furui.myzhihu.Model.News;
import com.rainism.furui.myzhihu.Model.NewsContent;
import com.rainism.furui.myzhihu.R;
import com.rainism.furui.myzhihu.Tools.URLModel;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

public class ContentActivity extends Activity {
    NewsContent newsContent=new NewsContent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
         if(getIntent().getExtras()!=null){
             News news=(News)getIntent().getExtras().getSerializable("news");
             Log.d("news.title:", "" + news.getId());
             getNewsContent(news.getId());
         }



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
                    try{
                        JSONObject jsonObject=new JSONObject(response);
                        newsContent.setBody(jsonObject.getString("body"));
                        newsContent.setImageSource(jsonObject.getString("image_source"));
                        newsContent.setTitle(jsonObject.getString("title"));
                        newsContent.setImageUrl(jsonObject.getString("image"));
                        newsContent.setShareUrl(jsonObject.getString("share_url"));
                        newsContent.setJsArray(jsonObject.getJSONArray("js"));
                        newsContent.setRecommendersArray(jsonObject.getJSONArray("recommenders"));
                        newsContent.setGaPrefix(jsonObject.getString("ga_prefix"));

                        //  newsContent.setRecommendersArray(jsonObject.getJSONArray("section"));



                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
