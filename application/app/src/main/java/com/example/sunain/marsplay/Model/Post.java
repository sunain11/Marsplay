package com.example.sunain.marsplay.Model;

import java.util.List;

public class Post
{
    String auth,title,content,key;
    List<String> uriList;
    public Post(){}

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Post(String key, String auth, String title, String content, List<String> uriList)
    {
        this.key=key;
        this.auth=auth;
        this.title=title;
        this.content=content;
        this.uriList=uriList;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getUriList() {
        return uriList;
    }

    public void setUriList(List<String> uriList) {
        this.uriList = uriList;
    }
}
