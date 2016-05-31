package com.rainism.furui.myzhihu.Acitvity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.rainism.furui.myzhihu.Adapter.MainNewsAdpater;
import com.rainism.furui.myzhihu.Model.News;
import com.rainism.furui.myzhihu.Model.TopNews;
import com.rainism.furui.myzhihu.R;
import com.rainism.furui.myzhihu.Tools.ImageTools;
import com.rainism.furui.myzhihu.Tools.URLModel;
import com.rainism.furui.myzhihu.View.MainListview;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;


public class MainActivity extends Activity {

    MainListview mainListView;
    ConvenientBanner convenientBanner;

    ArrayList<News> newsList = new ArrayList<News>();
    ArrayList<TopNews> topNewsList = new ArrayList<TopNews>();
    MainNewsAdpater mainNewsAdpater;
    String lastDay;
    Date beforeDate = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainListView = (MainListview) findViewById(R.id.main_listview);
        convenientBanner = new ConvenientBanner(MainActivity.this);
        convenientBanner.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 450));

        mainListView.addHeaderView(convenientBanner);
        mainListView.initBottomView();
        mainListView.setMyPullUpListViewCallBack(new MainListview.MyPullUpListViewCallBack() {
            @Override
            public void scrollBottomState() {
                getBeforeNew();
                getBeforeDate();
            }
        });

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

                        String topTitle = "今日新闻";
                        News topNew = new News(topTitle);
                        newsList.add(topNew);

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
                        mainNewsAdpater = new MainNewsAdpater(MainActivity.this, newsList);
                        mainListView.setAdapter(mainNewsAdpater);


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
                        Log.d("topNewsList.size", "" + topNewsList.size());

                        convenientBanner.setPages(
                                new CBViewHolderCreator<LocalImageHolderView>() {
                                    @Override
                                    public LocalImageHolderView createHolder() {
                                        return new LocalImageHolderView();
                                    }
                                }, topNewsList)
                                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                                .startTurning(5000);

                    } catch (JSONException e) {
                        if (e != null) {
                            Log.e("Exception", e.toString());
                        }
                    }

                }
            }
        });
        getNowData();

    }

    public class LocalImageHolderView implements Holder<TopNews> {
        private ImageView imageView;
        private TextView textview;
        private View view;

        @Override
        public View createView(Context context) {
            LayoutInflater inflater = LayoutInflater.from(context);
        //    Log.d("createView", "createView");
            view = inflater.inflate(R.layout.main_banner_view, null, false);
            imageView = (ImageView) view.findViewById(R.id.banner_imageview);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            textview = (TextView) view.findViewById(R.id.banner_textview);
            return view;
        }

        @Override
        public void UpdateUI(Context context, final int position, TopNews data) {
          //  Log.d("data.getImageUrl():", data.getImageUrl());
            ImageTools.downlandImageView(MainActivity.this, imageView, data.getImageUrl());
            textview.setText(data.getTitle());
        }
    }
    public void getNowData(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(beforeDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        beforeDate = calendar.getTime();
        if (month < 10) {
            Log.d("beforeData", "" + year + "0" + month + day);
            lastDay = "" + year + "0" + month + day;
        } else {
            Log.d("beforeData", "" + year + month + day);
            lastDay = "" + year + month + day;
        }
    }
    public void getBeforeDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(beforeDate);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        beforeDate = calendar.getTime();
        if (month < 10) {
            Log.d("beforeData", "" + year + "0" + month + day);
            lastDay = "" + year + "0" + month + day;
        } else {
            Log.d("beforeData", "" + year + month + day);
            lastDay = "" + year + month + day;
        }
    }
    public String DateToString(String str){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");//小写的mm表示的是分钟
        try{
            Date date=sdf.parse(str);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
            String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
            String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
            if("1".equals(mWay)){
                mWay ="日";
            }else if("2".equals(mWay)){
                mWay ="一";
            }else if("3".equals(mWay)){
                mWay ="二";
            }else if("4".equals(mWay)){
                mWay ="三";
            }else if("5".equals(mWay)){
                mWay ="四";
            }else if("6".equals(mWay)){
                mWay ="五";
            }else if("7".equals(mWay)){
                mWay ="六";
            }
            return mMonth + "月" + mDay+"日"+" 星期"+mWay;
        }catch (ParseException e){
            if (e!=null){
                Log.e("ParseException",e.toString());
            }
        }
        return "";
    }

    public void getBeforeNew() {
        Log.d("请求地址：",URLModel.URL_BEFOR_NEWS + lastDay);
        OkHttpUtils.get().url(URLModel.URL_BEFOR_NEWS + lastDay).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                if (e != null) {
                    Log.e("Exception", e.toString());
                }
            }

            @Override
            public void onResponse(String response) {
                if (response != null) {
                    Log.d("getBeforeNew response", response);
                    try {
                        JSONObject result = new JSONObject(response);
                        JSONArray stories = result.getJSONArray("stories");

                        String topTitle = DateToString(result.getString("date"));
                        News topNew = new News(topTitle);
                        newsList.add(topNew);

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
                        mainNewsAdpater.setList(newsList);
                    } catch (JSONException e) {
                        if (e != null) {
                            Log.e("Exception", e.toString());
                        }
                    }
                }
            }


        });


    }

}