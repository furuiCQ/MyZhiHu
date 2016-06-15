package com.rainism.furui.myzhihu.Acitvity;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.lzy.widget.HeaderViewPager;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/6/15.
 */
public class ContentFragment extends Fragment{
    NewsContent newsContent = new NewsContent();
    String contentId;

    ContentWebView webView;
    RelativeLayout headerView;
    ImageView headerViewImageView;
    TextView headerViewTextView;
    HeaderViewPager scrollableLayout;
    RelativeLayout cotentTitleView;
    RippleView backView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_content,null);
        initView();
        if (getArguments() != null) {
            News news = (News) getArguments().getSerializable("news");
            Log.d("news.title:", "" + news.getId());
            contentId=""+news.getId();
            loadNewsContent(news.getId() + "");
        }
        return view;
    }

    public void initView() {
        backView=(RippleView)getActivity().findViewById(R.id.content_back);
        cotentTitleView = (RelativeLayout)getActivity(). findViewById(R.id.content_title);
        webView = (ContentWebView)getActivity(). findViewById(R.id.content_webview);
        headerView = (RelativeLayout) getActivity().findViewById(R.id.content_banner);
        headerViewImageView = (ImageView) getActivity().findViewById(R.id.banner_imageview);
        headerViewImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        headerViewTextView = (TextView) getActivity().findViewById(R.id.banner_textview);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        scrollableLayout = (HeaderViewPager) getActivity().findViewById(R.id.scrollableLayout);
        scrollableLayout.setCurrentScrollableContainer(webView);
        scrollableLayout.setOnScrollListener(new HeaderViewPager.OnScrollListener() {
            @Override
            public void onScroll(int currentY, int maxY) {
                //让头部具有差速动画,如果不需要,可以不用设置
                headerView.setTranslationY(currentY / 2);
                //动态改变标题栏的透明度,注意转化为浮点型
                Log.d("currentY", "" + currentY);
                Log.d("maxY", "" + maxY);
                float alpha = 1.0f * (maxY - currentY) / maxY;
                cotentTitleView.setAlpha(alpha);
            }
        });
        backView.setOnClickListener(onClickListener);
    }
    View.OnClickListener onClickListener=new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.content_back:

                    break;
                default:
                    break;
            }
        }
    };
    public void loadNewsContent(String lastDay) {
        Log.d("MainActivity", "文章内容:" + ImageTools.searchMainNewsFileFromDataBase(lastDay, 2));
        if (ImageTools.searchMainNewsFileFromDataBase(lastDay, 2).equals("") ||
                ImageTools.searchMainNewsFileFromDataBase(lastDay, 2) == null) {
            getNewsContent(lastDay);
        } else {
            String bodyUrl = ImageTools.searchMainNewsFileFromDataBase(lastDay, 2);
            Log.d("MainActivity loadLocalBeforeData", "加载本地数据:" + bodyUrl);

            File file = new File(bodyUrl);
            String data = "";
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }
                bufferedReader.close();
                data = stringBuffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            loadData(data);
        }
    }

    public void loadData(String response) {
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

            ImageTools.downlandImageView(getActivity(), headerViewImageView,
                    newsContent.getImageUrl(), 1, newsContent.getId() + "");
            headerViewTextView.setText(newsContent.getTitle());
            String css = newsContent.getCss().toString();
            css = css.substring(1, css.length() - 1);
            String[] strs = css.split(",");
            String styleString = "";
            for (String str : strs) {
                styleString += "<link href=\"" + str.substring(1, str.length() - 1).replace("\\", "") + "\"/>";
            }
            Log.d("body", styleString);
            Log.d("shareUrl", newsContent.getShareUrl());
            String httpHeader = "<html lang=\"zh-CN\" class=\" js flexbox canvas canvastext webgl no-touch geolocation postmessage websqldatabase indexeddb hashchange history draganddrop websockets rgba hsla multiplebgs backgroundsize borderimage borderradius boxshadow textshadow opacity cssanimations csscolumns cssgradients cssreflections csstransforms csstransforms3d csstransitions fontface generatedcontent video audio localstorage sessionstorage webworkers applicationcache svg inlinesvg smil svgclippaths show-download-banner\" style=\"\">\n" +
                    "<head>\n" +
                    "<meta charset=\"utf-8\">\n" +
                    "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\">\n" +
                    "<title>" + newsContent.getTitle() + "</title>\n" +
                    "<meta name=\"apple-itunes-app\" content=\"app-id=639087967, app-argument=zhihudaily://story/" + newsContent.getId() + "\">\n" +
                    "<meta name=\"viewport\" content=\"user-scalable=no, width=device-width\">\n" +
                    "<link rel=\"stylesheet\" href=\"http://static.daily.zhihu.com/css/share.css?v=5956a\">\n" +
                    "<script async=\"\" src=\"http://www.google-analytics.com/analytics.js\"></script>\n" +
                    "<script src=\"http://static.daily.zhihu.com/js/modernizr-2.6.2.min.js\"></script>\n" +
                    "<link rel=\"canonical\" href=\"http://daily.zhihu.com/story/" + newsContent.getId() + "\">\n" +
                    "<base target=\"_blank\">\n";


            webView.loadData(httpHeader + styleString + "</head><body>" +
                    newsContent.getBody() + "<script src=\"http://static.daily.zhihu.com/js/jquery.1.9.1.js\"></script>\n" +
                    "<script src=\"/js/share.js?v=49768\"></script></body></html>", "text/html; charset=UTF-8", null);

            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/index2.html");
            Log.d("file.name", file.getAbsolutePath());
            try {
                file.createNewFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write(response);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getNewsContent(String newsId) {
        OkHttpUtils.get().url(URLModel.URL_NEWS_CONTENT + newsId).build().execute(new StringCallback() {
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
                    loadData(response);
                    ImageTools.donlandContentToDataBase(contentId, response, 2);

                }
            }


        });


    }


}
