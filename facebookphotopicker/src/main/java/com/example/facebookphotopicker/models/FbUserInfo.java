package com.example.facebookphotopicker.models;

/**
 * Created by ZhuMingren on 2017/5/31.
 */

public class FbUserInfo {
    public int age;
    public String gender;
    public String name;

    @Override
    public String toString() {
        return "FbUserInfo: age = " + age + " gender = " + gender + " name = " + name;
    }
}
