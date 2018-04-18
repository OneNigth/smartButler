package com.yj.smartbutler.utils;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;

/**
 * Created by yj on 2018/4/16.
 * 功能 网络请求发送
 */

public class RxVolleyUtils {
    //发送get请求
    public static void sendGetRequest(String url, HttpCallback callback) {
        RxVolley.get(url, callback);
    }
    //发送psot请求
    public static void sendPostRequest(String url, HttpParams params, HttpCallback callback) {
        RxVolley.post(url, params, callback);
    }
}
