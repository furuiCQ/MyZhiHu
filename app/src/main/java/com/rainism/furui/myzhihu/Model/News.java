package com.rainism.furui.myzhihu.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/26.
 */
public class News implements Serializable {
    private ArrayList<String> imageUrl;
    private int type;
    private long id;
    private String gaPrefix;
    private String title;
    private boolean isTopTitle=false;

    public News() {

    }

    public boolean isTopTitle() {
        return isTopTitle;
    }

    public void setIsTopTitle(boolean isTopTitle) {
        this.isTopTitle = isTopTitle;
    }
    public News(String title) {
        setTitle(title);
        setIsTopTitle(true);
    }
    public News(ArrayList<String> imageUrl,
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

    public ArrayList<String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(ArrayList<String> imageUrl) {
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
