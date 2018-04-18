package com.yj.smartbutler.constant;

import android.os.Environment;

/**
 * Created by yj on 2018/3/30.
 * 功能 所有数据常量
 */

public class MyConstant {
    /**
     * 登陆信息 handler
     */
    public static final int LOGIN_MSG_HANDLER = 1000;
    /**
     * 更新用户信息 handler
     */
    public static final int UPDATA_USER_HANDLER = 1001;
    /**
     * 归属地查询handler
     */
    public static final int PHONE_SELECT_HANDLER = 1002;
    /**
     *
     */
    public static final int CHAT_SEND_HANDLER = 1003;
    /**
     * 闪屏延时标志
     */
    public static final int HANDLER_SPLASH = 2001;
    /**
     * 闪屏页面是否是第一次运行
     */
    public static final String IS_FIRST_RUN = "isFirst";
    /**
     * SharedPreferences是否记住密码key
     */
    public static final String REMEMBER_PASSWORD_KEY = "isRemember";
    /**
     * SharedPreferences，用户名key
     */
    public static final String USERNAME_KEY = "username";
    /**
     * SharedPreferences，密码key
     */
    public static final String PASSWORD_KEY = "password";
    /**
     * 照片文件名
     */
    public static final String PHOTO_IMAGE_FILE_NAME = "fileImg.jpg";

    /**
     * 照片路径
     */
    public static final String PHOTO_PATH = Environment.getExternalStorageDirectory() + MyConstant.PHOTO_IMAGE_FILE_NAME;
    /**
     * 裁剪后的对象
     */
    public static final String  CROP_IMAGE_FILE_NAME ="cropImg.jpg";
    /**
     * 跳转相机相机
     */
    public static final int CAMERA_REQUEST_CODE = 100;
    /**
     * 跳转图库
     */
    public static final int PHOTO_REQUEST_CODE = 101;
    /**
     * 照片裁剪后返回
     */
    public static final int RESULT_REQUEST_CODE = 102;
    /**
     * 7.0版本后的照片裁剪后返回
     */
    public static final int SELECT_PIC_KITKAT_CODE = 103;
}
