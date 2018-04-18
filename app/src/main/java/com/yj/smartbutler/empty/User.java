package com.yj.smartbutler.empty;

import cn.bmob.v3.BmobUser;

/**
 * Created by yj on 2018/4/5.
 * 功能 User实体类，单例
 */

public class User extends BmobUser {
    /**
     * 性别 boy ：true ，girl ： false
     */
    private boolean sex;
    //年龄
    private int age;
    //介绍
    private String introduce;
    //头像
    private String image;

    public User() {
    }


    public boolean getSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
