package com.aptPhase3.zhiyuan.android;

import java.util.HashMap;

/**
 * Created by libochen on 12/12/15.
 */
public class Message {
    public String name;
    public String photo1;
    public String photo2;
    public String content;
    public HashMap<String, String> mymap = new HashMap<>();

    public Message(){
        super();
    }

    public Message(String name, String content, String photo1, String photo2){
        super();
        this.name = name;
        this.content = content;
        this.photo1 = photo1;
        this.photo2 = photo2;
    }

    public String getPhoto(String name){
        return mymap.get(name);
    }

}
