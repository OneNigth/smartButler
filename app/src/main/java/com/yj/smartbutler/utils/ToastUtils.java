package com.yj.smartbutler.utils;

import android.widget.Toast;

import com.yj.smartbutler.application.MyApplication;

/**
 * Created by yj on 2018/4/5.
 * 功能 吐司弹窗工具类
 */

public class ToastUtils {
    private Toast toast;

    private ToastUtils() {
    }
    public static void showShortToast(String text){
        Toast.makeText(MyApplication.getContext(),text,Toast.LENGTH_SHORT ).show();
    }
    public static void showLongToast(String text){
        Toast.makeText(MyApplication.getContext(),text,Toast.LENGTH_LONG ).show();
    }
}
