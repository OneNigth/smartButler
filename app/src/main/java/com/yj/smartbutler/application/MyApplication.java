package com.yj.smartbutler.application;

import android.app.Application;
import android.content.Context;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by yj on 2018/3/28.
 */

public class MyApplication extends Application {
    //上下文
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        //bugly配置
        CrashReport.initCrashReport(getApplicationContext(), "c47516da53", true);
    }
    public static Context getContext(){
        return context;
    }
}
