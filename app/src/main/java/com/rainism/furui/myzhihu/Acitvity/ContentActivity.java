package com.rainism.furui.myzhihu.Acitvity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import okhttp3.Call;

public class ContentActivity extends Activity {
    NewsContent newsContent=new NewsContent();
    ContentWebView webView;

    ImageView headerViewImageView;
    TextView headerViewTextView;
    String str;

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
        webView.getSettings().setJavaScriptEnabled(true);
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
                        newsContent.setGaPrefix(jsonObject.getString("ga_prefix"));
                        newsContent.setType(jsonObject.getInt("type"));
                        newsContent.setId(jsonObject.getLong("id"));
                        newsContent.setCss(jsonObject.getJSONArray("css"));

                        ImageTools.downlandImageView(ContentActivity.this, headerViewImageView, newsContent.getImageUrl());
                        headerViewTextView.setText(newsContent.getTitle());
                        String css=newsContent.getCss().toString();
                        css=css.substring(1,css.length()-1);
                        String[] strs=css.split(",");
                        String styleString="";
                        for(String str:strs) {
                            styleString += "<link href=\"" + str.substring(1, str.length() - 1).replace("\\","") + "\"/>";
                        }
                        Log.d("body", styleString);
                        Log.d("shareUrl", newsContent.getShareUrl());
                        String httpHeader="<html lang=\"zh-CN\" class=\" js flexbox canvas canvastext webgl no-touch geolocation postmessage websqldatabase indexeddb hashchange history draganddrop websockets rgba hsla multiplebgs backgroundsize borderimage borderradius boxshadow textshadow opacity cssanimations csscolumns cssgradients cssreflections csstransforms csstransforms3d csstransitions fontface generatedcontent video audio localstorage sessionstorage webworkers applicationcache svg inlinesvg smil svgclippaths show-download-banner\" style=\"\">\n" +
                                "<head>\n" +
                                "<meta charset=\"utf-8\">\n" +
                                "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\">\n" +
                                "<title>"+ newsContent.getTitle()+"</title>\n" +
                                "<meta name=\"apple-itunes-app\" content=\"app-id=639087967, app-argument=zhihudaily://story/"+newsContent.getId()+"\">\n" +
                                "<meta name=\"viewport\" content=\"user-scalable=no, width=device-width\">\n" +
                                "<link rel=\"stylesheet\" href=\"http://static.daily.zhihu.com/css/share.css?v=5956a\">\n" +
                                "<script async=\"\" src=\"http://www.google-analytics.com/analytics.js\"></script>\n" +
                                "<script src=\"http://static.daily.zhihu.com/js/modernizr-2.6.2.min.js\"></script>\n" +
                                "<link rel=\"canonical\" href=\"http://daily.zhihu.com/story/"+newsContent.getId()+"\">\n" +
                                "<base target=\"_blank\">\n";



                        webView.loadData(httpHeader+ styleString+"</head><body>" +
                                newsContent.getBody()+"<script src=\"http://static.daily.zhihu.com/js/jquery.1.9.1.js\"></script>\n" +
                                "<script src=\"/js/share.js?v=49768\"></script></body></html>", "text/html; charset=UTF-8", null);

                       File file=new File(Environment.getExternalStorageDirectory().getPath()+"/index2.html");
                        Log.d("file.name",file.getAbsolutePath());
                        try{
                            file.createNewFile();
                            BufferedWriter writer=new BufferedWriter(new FileWriter(file));
                            writer.write(response);
                            writer.flush();
                            writer.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }

                } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
