package com.rainism.furui.myzhihu.Model;

/**
 * Created by Administrator on 2016/5/26.
 */
public class TopNews {
    /* {
         "image": "http://pic2.zhimg.com/56f52b579a98289e7e3a919ee9c8e8dd.jpg",
             "type": 0,
             "id": 8355498,
             "ga_prefix": "052607",
             "title": "读读日报 24 小时热门 TOP 5 · 和程序员打官司是一种怎样的体验"
     }*/
    private String imageUrl;
    private int type;
    private long id;
    private String gaPrefix;
    private String title;

    public TopNews() {

    }

    public TopNews(String imageUrl,
                   int type,
                   long id,
                   String gaPrefix,
                   String title) {
        setGaPrefix(gaPrefix);
        setId(id);
        setImageUrl(imageUrl);
        setTitle(title);
        setType(type);

    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGaPrefix() {
        return gaPrefix;
    }

    public void setGaPrefix(String gaPrefix) {
        this.gaPrefix = gaPrefix;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
