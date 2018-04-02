package com.yj.smartbutler.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.yj.smartbutler.R;
import com.yj.smartbutler.constant.MyConstant;
import com.yj.smartbutler.utils.LogUtils;
import com.yj.smartbutler.utils.ShareUtil;
import com.yj.smartbutler.utils.UtilTools;

/**
 * Created by yj on 2018/3/30.
 */

public class SplashActivity extends BaseActivity {
    //handler延时一秒
    private static final int HANDLER_LATE_TIME = 1000;

    private TextView mSplashTextView;

    //延时处理
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MyConstant.HANDLER_SPLASH:
                    jumpActivity(isFirst());
                    break;
            }
        }
    };


    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
    }

    private void initView() {
        //设置字体
        mSplashTextView = findViewById(R.id.splash_text_view);
        UtilTools.SetFont(context,mSplashTextView);

        //延时一秒
        mHandler.sendEmptyMessageDelayed(MyConstant.HANDLER_SPLASH, HANDLER_LATE_TIME);

    }

    /**
     * 跳入引导页面还是主页
     *
     * @param isFirst
     */
    private void jumpActivity(boolean isFirst) {
        Intent intent;

        if (isFirst) {//跳入引导页面
            intent = new Intent(context, GuideActivity.class);
            ShareUtil.putBoolean(context, MyConstant.IS_FIRST_RUN, false);
            LogUtils.d("GuideActivity");
        } else {//跳到主页
            intent = new Intent(context, MainActivity.class);
            LogUtils.d("MainActivity");
        }
        startActivity(intent);
        finish();
    }

    /**
     * 是否第一次运行
     * @return
     */
    public boolean isFirst() {
        return ShareUtil.getBoolean(context, MyConstant.IS_FIRST_RUN, true);
    }

    //禁止返回键
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
