package com.yj.smartbutler.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.yj.smartbutler.R;
import com.yj.smartbutler.empty.User;
import com.yj.smartbutler.service.BmobService;
import com.yj.smartbutler.utils.LogUtils;
import com.yj.smartbutler.utils.ToastUtils;

/**
 * Created by yj on 2018/4/5.
 * 功能
 */

public class RegistActivity extends BaseActivity {
    //注册按钮
    private TextView mRegistBt;
    //用户名
    private EditText mUsernameEt;
    //密码
    private EditText mPasswordEt;
    //email
    private EditText mEmailEt;
    //性别为男为ture
    private RadioButton mSexBoyBt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        initView();
    }

    private void initView() {
        mRegistBt = findViewById(R.id.regist_user_btn);
        mUsernameEt = findViewById(R.id.regist_username_et);
        mPasswordEt = findViewById(R.id.regist_password_et);
        mEmailEt = findViewById(R.id.regist_email_et);
        mSexBoyBt = findViewById(R.id.sex_boy_ischeck);
        text();//方便做事的,可以删
        mRegistBt.setOnClickListener(this);
    }

    private void text() {
        if (deBug) {
            mUsernameEt.setText("喜羊羊");
            mPasswordEt.setText("123456");
            mEmailEt.setText("994162411@qq.com");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.regist_user_btn://点击注册
                registUser();
                //跳转主页
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                finish();//销毁注册界面
                break;
        }
    }

    /**
     * 注册用户  ----逻辑不对劲，需要留有判断文本格式的方法
     */
    private void registUser() {
        String email = getTextByEditText(mEmailEt);
        String username = getTextByEditText(mUsernameEt);
        String password = getTextByEditText(mPasswordEt);
        boolean sex = mSexBoyBt.isChecked();

        boolean canRegist = true;
        //文本为空弹窗提示，不做处理
        if (TextUtils.isEmpty(username)) {
            ToastUtils.showLongToast(getString(R.string.username_no_null));
            canRegist = false;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtils.showShortToast(getString(R.string.passworld_no_null));
            canRegist = false;
        }
        if (TextUtils.isEmpty(email)) {
            ToastUtils.showLongToast(getString(R.string.eamil_no_null));
            LogUtils.d(email.toString() + "2222");
            canRegist = false;
        }

        //文本非空则注册用户，但需要邮箱验证后才能登陆
        if (canRegist) {
            User user = new User();
            user.setEmail(email);
            user.setEmailVerified(false);//邮箱未认证
            user.setIntroduce(getString(R.string.introduce_lazy_people));
            user.setUsername(username);
            user.setPassword(password);
            user.setSex(sex);
            //发送登陆请求
            BmobService.sendSignUpRequest(user);
            //发送邮箱认证
            BmobService.sendEmailRequest(email);
        }

    }

    //获取输入文本文字
    private String getTextByEditText(TextView textView) {
        String text = textView.getText().toString().trim();
        return text;
    }
}
