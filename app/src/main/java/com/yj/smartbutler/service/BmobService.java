package com.yj.smartbutler.service;

import android.os.Handler;

import com.yj.smartbutler.application.MyApplication;
import com.yj.smartbutler.constant.MyConstant;
import com.yj.smartbutler.empty.User;
import com.yj.smartbutler.utils.LogUtils;
import com.yj.smartbutler.utils.ToastUtils;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by yj on 2018/4/6.
 * 功能 Bmob二次封装
 */

public class BmobService {
    //邮件发送成功标志
    private static boolean EMAIL_SEND_FLAG;

    public static void sendSignUpRequest(BmobUser user) {
        //发送注册请求
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    //注册成功则设置全局用户
                    MyApplication.setUser(user);
                } else {//否则显示错误日志
                    LogUtils.d(e.getMessage());
                }
            }
        });
    }

    /**
     * 发送邮箱认证
     *
     * @param email
     */
    public static void sendEmailRequest(final String email) {
        //发送邮箱认证
        BmobUser.requestEmailVerify(email, new UpdateListener() {

            @Override
            public void done(BmobException e) {
                if (e == null) {//提示发送成功并毁当前界面
                    ToastUtils.showShortToast("注册成功，请到" + email + "邮箱中进行激活。");
                } else {//否则显示错误日志
                    LogUtils.d(e.getMessage());
                }
            }
        });
    }

    /**
     * 发送登陆请求
     */
    public static void sendLoginRequest(BmobUser user, final Handler handler) {
        user.login(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser user, BmobException e) {
                if (e == null) {
                    handler.sendEmptyMessage(MyConstant.LOGIN_MSG_HANDLER);
                    ToastUtils.showShortToast("登录成功:");
                    //通过BmobUser user = BmobUser.getCurrentUser()获取登录成功后的本地用户信息
                    //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(MyUser.class)获取自定义用户信息
                } else {
                    ToastUtils.showShortToast("登陆出错");
                    LogUtils.d(e.getMessage());
                }
            }
        });
    }

    /**
     * 用户信息更新
     */
    public static void updataUserInfo(User newUser, final Handler mHandler) {
//        final User newUser = new User();
//        newUser.setEmail(user.getEmail());
        BmobUser bmobUser = BmobUser.getCurrentUser();
        newUser.update(bmobUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    MyApplication.setUser(BmobUser.getCurrentUser(User.class));
                    mHandler.sendEmptyMessage(MyConstant.UPDATA_USER_HANDLER);
                    ToastUtils.showShortToast("更新用户信息成功");
                } else {
                    ToastUtils.showShortToast("更新用户信息失败:" + e.getMessage());
                }
            }
        });
    }

    /**
     * 退出登陆
     */
    public static void sendLogoutRequest(){
        User.logOut();   //清除缓存用户对象
        BmobUser currentUser = User.getCurrentUser(); // 现在的currentUser是null了S
    }
    /**
     * 发送重置密码邮箱
     */
    public static boolean resetPasswordByEmail(final String email) {
        BmobUser.resetPasswordByEmail(email, new UpdateListener() {

            @Override
            public void done(BmobException e) {
                boolean flag;
                if (e == null) {
                    flag = true;
                    ToastUtils.showShortToast("重置密码请求成功，请到" + email + "邮箱进行密码重置操作");
                } else {
                    flag = false;
                    ToastUtils.showShortToast("重置密码失败");
                    LogUtils.d("失败:" + e.getMessage());
                }
                setEmailSuccessFlag(flag);
            }
        });
        return EMAIL_SEND_FLAG;
    }

    private static void setEmailSuccessFlag(boolean flag) {
        EMAIL_SEND_FLAG = flag;
    }

}
