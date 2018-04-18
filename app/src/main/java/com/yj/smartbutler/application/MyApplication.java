package com.yj.smartbutler.application;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.tencent.bugly.crashreport.CrashReport;
import com.yj.smartbutler.api.APIConstant;
import com.yj.smartbutler.empty.User;

import cn.bmob.v3.Bmob;

/**
 * Created by yj on 2018/3/28.
 */

public class MyApplication extends MultiDexApplication {
    //上下文
    private static Context context;
    //用户
    private static User user;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        //bmob初始化
        Bmob.initialize(context, APIConstant.BMOB_APP_KEY);
        //bugly配置
        CrashReport.initCrashReport(context, APIConstant.BUGLY_CONTEXT, true);
    }

    public static Context getContext() {
        return context;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User mUser) {
        user = mUser;
    }


}
