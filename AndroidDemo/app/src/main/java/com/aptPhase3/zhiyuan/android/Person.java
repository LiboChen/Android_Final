package com.aptPhase3.zhiyuan.android;

/**
 * Created by libochen on 12/9/15.
 */
public class Person {
    public String userName;
    public String nickName;
    public String description;
    public String photo;

    public Person(){
        super();
    }

    public Person(String name, String photo){
        super();
        this.nickName = name;
        this.photo = photo;
    }

}
