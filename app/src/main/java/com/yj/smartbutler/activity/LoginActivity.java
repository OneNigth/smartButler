package com.yj.smartbutler.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.yj.smartbutler.R;
import com.yj.smartbutler.application.MyApplication;
import com.yj.smartbutler.constant.MyConstant;
import com.yj.smartbutler.empty.User;
import com.yj.smartbutler.service.BmobService;
import com.yj.smartbutler.utils.ShareUtil;
import com.yj.smartbutler.view.CustomDialog;

import cn.bmob.v3.BmobUser;

/**
 * Created by yj on 2018/4/4.
 * 功能
 */

public class LoginActivity extends BaseActivity {

    //忘记密码
    private TextView mForgotBt;
    //登陆按钮
    private Button mLoginBt;
    //用户名输入栏
    private EditText mUsernameEt;
    //密码输入栏
    private EditText mPasswordEt;
    //注册按钮
    private TextView mRegistBt;
    //记住密码选项
    private CheckBox mRememberCb;

    //登陆dailog
    private CustomDialog mLoginLoadingDialog;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MyConstant.LOGIN_MSG_HANDLER:
                    //成功接受返回，则跳转到主页的处理
                    jumpToMain();
                    //销毁加载动画
                    mLoginLoadingDialog.dismiss();
                    //销毁界面
                    finish();
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        mForgotBt = findViewById(R.id.forgot_password_bt);
        mLoginBt = findViewById(R.id.login_user_btn);
        mUsernameEt = findViewById(R.id.login_username_et);
        mPasswordEt = findViewById(R.id.login_password_et);
        mRegistBt = findViewById(R.id.regist_bt);
        mRememberCb = findViewById(R.id.remember_password_check);

        mForgotBt.setOnClickListener(this);
        mLoginBt.setOnClickListener(this);
        mPasswordEt.setOnClickListener(this);
        mUsernameEt.setOnClickListener(this);
        mRegistBt.setOnClickListener(this);
        //登陆加载动画
        mLoginLoadingDialog = new CustomDialog(this, 100, 100, R.layout.dialog_login_loading, R.style.Theme_dialog, Gravity.CENTER,R.style.pop_anim_style);
        mLoginLoadingDialog.setCancelable(false);
        //从文件读取并显示是否设置记住密码
        mRememberCb.setChecked(ShareUtil.getBoolean(context, MyConstant.REMEMBER_PASSWORD_KEY, false));
        //显示用户名密码
        mUsernameEt.setText(ShareUtil.getString(context,MyConstant.USERNAME_KEY,""));
        mPasswordEt.setText(ShareUtil.getString(context,MyConstant.PASSWORD_KEY,""));
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.forgot_password_bt://忘记密码
                forgotPassword();
                break;
            case R.id.regist_bt://注册
                intent = new Intent(context, RegistActivity.class);
                startActivity(intent);
                break;
            case R.id.login_user_btn:
                //显示登陆加载动画
                mLoginLoadingDialog.show();
                //发送登陆，保存信息到应用
                userLogin();
                break;
        }
    }

    //忘记密码
    private void forgotPassword() {
        Intent intent = new Intent(context, ForgotPasswrodActivity.class);
        startActivity(intent);
    }

    //跳转界面，存储记住密码，保存用户信息
    private void jumpToMain() {
        //存储是否记住密码
        ShareUtil.putBoolean(context, MyConstant.REMEMBER_PASSWORD_KEY, mRememberCb.isChecked());
        ShareUtil.putString(context,MyConstant.USERNAME_KEY,mUsernameEt.getText().toString().trim());
        //存储用户密码
        savePassword();
        //得到已加载的用户类信息，保存到应用
        MyApplication.setUser(BmobUser.getCurrentUser(User.class));
        //跳转
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
    }

    /**
     * 密码存储要做处理----待实现
     */
    private void savePassword() {
        ShareUtil.putString(context,MyConstant.PASSWORD_KEY,mPasswordEt.getText().toString().trim());
    }

    /**
     * 发送登陆请求
     */
    private void userLogin() {
        User user = new User();
        user.setUsername(mUsernameEt.getText().toString().trim());
        user.setPassword(mPasswordEt.getText().toString().trim());
        //发送
        BmobService.sendLoginRequest(user, mHandler);
    }
}
