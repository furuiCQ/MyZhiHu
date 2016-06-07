package com.rainism.furui.myzhihu.Model;

import org.json.JSONArray;

/**
 * Created by Administrator on 2016/5/31.
 */
public class NewsContent {

  /* {
    "body": "",
    "image_source": "《海贼王》",
    "title": "为什么大部分人觉得肉比菜好吃？",
    "image": "http://pic2.zhimg.com/630ba6de900285c34e2163855ae32f89.jpg",
    "share_url": "http://daily.zhihu.com/story/8344289",
    "js": [],
    "ga_prefix": "060718",
    "images": [
        "http://pic3.zhimg.com/2e4c73ef12905fdef0c92029431ca12e.jpg"
    ],
    "type": 0,
    "id": 8344289,
    "css": [
        "http://news-at.zhihu.com/css/news_qa.auto.css?v=4b3e3"
    ]
}*/

    private String body;
    private String imageSource;
    private String title;
    private String imageUrl;
    private String shareUrl;
    private JSONArray jsArray;
    private JSONArray recommendersArray;
    private String gaPrefix;
    private Section section=new Section();
    private int type;
    private long id;
    private JSONArray css;


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public JSONArray getJsArray() {
        return jsArray;
    }

    public void setJsArray(JSONArray jsArray) {
        this.jsArray = jsArray;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }


    public JSONArray getRecommendersArray() {
        return recommendersArray;
    }

    public void setRecommendersArray(JSONArray recommendersArray) {
        this.recommendersArray = recommendersArray;
    }

    public String getGaPrefix() {
        return gaPrefix;
    }

    public void setGaPrefix(String gaPrefix) {
        this.gaPrefix = gaPrefix;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
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

    public JSONArray getCss() {
        return css;
    }

    public void setCss(JSONArray css) {
        this.css = css;
    }
}
