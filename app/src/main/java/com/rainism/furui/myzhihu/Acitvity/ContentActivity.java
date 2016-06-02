package com.rainism.furui.myzhihu.Acitvity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;

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
                        Log.d("js", newsContent.getJsArray().toString());
                        String css=newsContent.getCss().toString();
                        css=css.substring(1,css.length()-1);
                        Log.d("css", css);
                        String[] strs=css.split(",");
                        String styleString="";
                        for(String str:strs) {
                            styleString += "<link href='" + str.substring(1, str.length() - 1).replace("\\","") + "'/>";
                        }


                       // webView.loadUrl(newsContent.getShareUrl());

                         webView.loadData("<html><head> <meta charset='utf-8'/>"+styleString+"</head><body>"+newsContent.getBody()+"</body></html>", "text/html; charset=UTF-8", null);
                        File file=new File(Environment.getExternalStorageDirectory().getPath()+"/index.txt");
                        try{
                            file.createNewFile();
                            BufferedWriter writer=new BufferedWriter(new FileWriter(file));
                            writer.write("<html><head> <meta charset='utf-8'/>"+styleString+"</head><body>"+newsContent.getBody()+"</body></html>");
                            writer.flush();
                            writer.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }


                        webView.setWebViewClient(new WebViewClient() {
                            @Override
                            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                super.onPageStarted(view, url, favicon);
                            }

                            @Override
                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                view.loadUrl(url);
                                return true;
                            }

                            @Override
                            public void onPageFinished(WebView view, String url) {
                                super.onPageFinished(view, url);
                                view.loadUrl("javascript:window.local_obj.showSource('<head>'+"
                                        + "document.getElementsByTagName('html')[0].innerHTML='123'+'</head>');");

                            }

                            @Override
                            public void onReceivedError(WebView view, int errorCode,
                                                        String description, String failingUrl) {
                                super.onReceivedError(view, errorCode, description, failingUrl);
                            }

                        });


                } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
