package com.rainism.furui.myzhihu.Model;

/**
 * Created by Administrator on 2016/6/1.
 */
public class  Section{
    private String thumbnail;
    private long id;
    private String name;
    public Section(){

    }
    public Section(String thumbnail,
            long id,
            String name){

    }

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
