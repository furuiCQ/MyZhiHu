package com.rainism.furui.myzhihu.Model;

import org.json.JSONArray;

/**
 * Created by Administrator on 2016/5/31.
 */
public class NewsContent {

  /*  {
        body: "<div class="main-wrap content-wrap">...</div>",
            image_source: "Yestone.com 版权图片库",
            title: "深夜惊奇 · 朋友圈错觉",
            image: "http://pic3.zhimg.com/2d41a1d1ebf37fb699795e78db76b5c2.jpg",
            share_url: "http://daily.zhihu.com/story/4772126",
            js: [ ],
        recommenders": [
        { "avatar": "http://pic2.zhimg.com/fcb7039c1_m.jpg" },
        { "avatar": "http://pic1.zhimg.com/29191527c_m.jpg" },
        { "avatar": "http://pic4.zhimg.com/e6637a38d22475432c76e6c9e46336fb_m.jpg" },
        { "avatar": "http://pic1.zhimg.com/bd751e76463e94aa10c7ed2529738314_m.jpg" },
        { "avatar": "http://pic1.zhimg.com/4766e0648_m.jpg" }
        ],
        ga_prefix: "050615",
                section": {
        "thumbnail": "http://pic4.zhimg.com/6a1ddebda9e8899811c4c169b92c35b3.jpg",
            "id": 1,
            "name": "深夜惊奇"
    },
    type: 0,
    id: 4772126,
    css: [
            "http://news.at.zhihu.com/css/news_qa.auto.css?v=1edab"
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
    class Section{
        public String thumbnail;
        long id;
        String name;

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


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
