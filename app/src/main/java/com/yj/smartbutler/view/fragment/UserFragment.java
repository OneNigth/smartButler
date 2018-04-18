package com.yj.smartbutler.view.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yj.smartbutler.R;
import com.yj.smartbutler.activity.CourierActivity;
import com.yj.smartbutler.activity.LoginActivity;
import com.yj.smartbutler.activity.PhoneActivity;
import com.yj.smartbutler.application.MyApplication;
import com.yj.smartbutler.constant.MyConstant;
import com.yj.smartbutler.empty.User;
import com.yj.smartbutler.service.BmobService;
import com.yj.smartbutler.utils.GetImagePath;
import com.yj.smartbutler.utils.LogUtils;
import com.yj.smartbutler.utils.ToastUtils;
import com.yj.smartbutler.view.CustomDialog;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yj on 2018/3/29.
 * 裁剪部分未完成
 */

public class UserFragment extends BaseFragment {
    //找id的
    private View view;
    //修改信息按钮
    private TextView mChangeInfoBt;
    //背景
    RelativeLayout mUserBackground;
    //圆角头像
    private CircleImageView mUserImageView;
    //头像的弹窗
    private CustomDialog mUserImageDialog;
    //用户名
    private TextView mUsernameTv;
    //性别
    private RadioButton mBoyBt, mGirlBt;
    //年龄
    private EditText mAgeEt;
    //简介
    private EditText mIntroduceEt;
    //提交按钮
    private Button mCommitBt;
    //快递查询
    private TextView mCourierSelectBt;
    //归属地查询
    private TextView mPhoneSelectBt;
    //退出按钮
    private Button mLogoutBt;
    //用户对象
    private User mUser;
    //拍照
    private Button mCamereBt;
    //图库
    private Button mPictureBt;
    //取消
    private Button mCancelBt;
    //相册图片
//    private File mPhotoFile;
    //裁剪图片
    private File mCropFile;
    //相机图片的路径
//    private File fileUri;
    private Uri mPhotoUri;

    //更新用户信息
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MyConstant.UPDATA_USER_HANDLER://用户信息已更新
                    //显示非修改界面
                    showNotEditView();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user, container, false);
        initData();
        initView();

        return view;
    }

    private void initData() {
        //获取全局的用户对象
        mUser = MyApplication.getUser();
    }

    //界面初始化
    private void initView() {
        mChangeInfoBt = view.findViewById(R.id.change_user_info);
        mUserBackground = view.findViewById(R.id.user_backgroud_image);
        mUserImageView = view.findViewById(R.id.user_image_view);
        mUsernameTv = view.findViewById(R.id.user_name);
        mBoyBt = view.findViewById(R.id.user_sex_boy_bt);
        mGirlBt = view.findViewById(R.id.user_sex_girl_bt);
        mAgeEt = view.findViewById(R.id.user_age_et);
        mIntroduceEt = view.findViewById(R.id.user_introduce_et);
        mCommitBt = view.findViewById(R.id.user_commit_bt);
        mPhoneSelectBt = view.findViewById(R.id.phone_select_tv);
        mCourierSelectBt = view.findViewById(R.id.user_courier_tv);
        mLogoutBt = view.findViewById(R.id.logout_user_bt);

        //点击图片的弹窗---dialog只有activity才能加载，content为fragment
        mUserImageDialog = new CustomDialog(getActivity(), 100, 100, R.layout.dialog_user_image, R.style.pop_anim_style, Gravity.BOTTOM, 0);
        //通过dialog获取dialog的布局
        mCancelBt = mUserImageDialog.findViewById(R.id.cancel_bt);
        mCamereBt = mUserImageDialog.findViewById(R.id.camera_bt);
        mPictureBt = mUserImageDialog.findViewById(R.id.picture_bt);

        mCancelBt.setOnClickListener(this);
        mCamereBt.setOnClickListener(this);
        mPictureBt.setOnClickListener(this);
        mPhoneSelectBt.setOnClickListener(this);
        mCourierSelectBt.setOnClickListener(this);
        mChangeInfoBt.setOnClickListener(this);
        mCommitBt.setOnClickListener(this);
        mUserImageView.setOnClickListener(this);
        //加载、显示用户数据
        showViewByUserInfo();
        //界面不可编辑
        isEditUser(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != getActivity().RESULT_CANCELED) {//返回码不为0
            switch (requestCode) {
                case MyConstant.CAMERA_REQUEST_CODE://相机
                    LogUtils.d("图片：：：：：" + data.getData().toString());
                    cutPhoto(data.getData());//裁剪
                    break;
                case MyConstant.PHOTO_REQUEST_CODE://图册
                    //相机返回结果，调用系统裁剪
//                    File imgUri = new File(GetImagePath.getPath(getContext(), data.getData()));
                    newUriByVersion();
                    cutPhoto(mPhotoUri);
                    break;
                case MyConstant.RESULT_REQUEST_CODE://最后得图片结果
                    setImageView(data);
                    break;
//                case MyConstant.SELECT_PIC_KITKAT_CODE://7.0图片裁剪
//                    File imgUri = new File(GetImagePath.getPath(getContext(), data.getData()));
//                    Uri dataUri = FileProvider.getUriForFile(getActivity(), "com.renwohua.conch.fileprovider", imgUri);
//                    setImageView(data);
//                    break;
            }
        }
    }

    //设置图片
    private void setImageView(Intent data) {
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            Bitmap bitmap = bundle.getParcelable("data");//规定
//            Bitmap bitmap = BitmapFactory.decodeFile(PhotoService.selectImage(context,data));
            mUserImageView.setImageBitmap(bitmap);
        }
    }

    /**
     * 图片裁剪处理
     *
     * @param inputUri
     */
    private void cutPhoto(Uri inputUri) {
        if (inputUri == null) {
            LogUtils.d("图片为空");
            return;
        }
//        mCropFile = new File(MyConstant.PHOTO_PATH);//裁剪后的File对象
        Intent intent = new Intent("com.android.camera.action.CROP");
        //sdk>=24
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            Uri outPutUri = Uri.fromFile(mCropFile);
            intent.setDataAndType(inputUri, "image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
            intent.putExtra("noFaceDetection", false);//去除默认的人脸识别，否则和剪裁匡重叠
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        } else {
            Uri outPutUri = Uri.fromFile(mCropFile);
            if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                String url = GetImagePath.getPath(getContext(), inputUri);//这个方法是处理4.4以上图片返回的Uri对象不同的处理方法
                intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
            } else {
                intent.setDataAndType(inputUri, "image/*");
            }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
        }
                if (inputUri == null) {
            LogUtils.d("图片为空");
        } else {
//            Intent intent = new Intent("com.android.camera.action.CROP");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        intent.setDataAndType(mPhotoUri, "image/*");
        //设置裁剪
        intent.putExtra("crop", "true");
        //裁剪宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //裁剪图片的质量
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        //发送数据
        intent.putExtra("return-data", true);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, MyConstant.RESULT_REQUEST_CODE);
        }

    }

    /**
     * 显示用户数据
     */
    private void showViewByUserInfo() {
        mBoyBt.setChecked(mUser.getSex());
        mAgeEt.setText(mUser.getAge() + "");
        //介绍
        String introduce = mUser.getIntroduce();
        if (TextUtils.isEmpty(introduce)) {//如果没有就显示什么都没留
            mIntroduceEt.setText(getString(R.string.introduce_lazy_people));
        } else {
            mIntroduceEt.setText(introduce);
        }
    }

    /**
     * 是否是编辑用户状态
     *
     * @param isEdit
     */
    private void isEditUser(boolean isEdit) {
        if (isEdit) {//显示编辑界面
            showEditView();
        } else {//显示非编辑界面
            showNotEditView();
        }
    }

    /**
     * 显示编辑界面
     */
    private void showEditView() {
        /**
         * 可编辑,hint为空，显示text
         */
        mBoyBt.setText(getString(R.string.sex_is_boy));
        mBoyBt.setEnabled(true);
        mGirlBt.setText(getString(R.string.sex_is_girl));
        mGirlBt.setEnabled(true);
        mAgeEt.setEnabled(true);
        mIntroduceEt.setEnabled(true);
        //显示提交按钮
        mCommitBt.setVisibility(View.VISIBLE);
        /**
         * 登陆，归属地查询，物流查询功能不可用
         */
        mCourierSelectBt.setEnabled(false);
        mPhoneSelectBt.setEnabled(false);
        mLogoutBt.setEnabled(false);
    }

    /**
     * 显示非编辑界面
     */
    private void showNotEditView() {
        /**
         * 不可编辑,显示hint，text为空
         */
        mBoyBt.setHint(getString(R.string.sex_is_boy));
        mBoyBt.setEnabled(false);
        mGirlBt.setHint(getString(R.string.sex_is_girl));
        mGirlBt.setEnabled(false);
        mAgeEt.setHint(mUser.getAge() + "");
        mAgeEt.setEnabled(false);
        mIntroduceEt.setHint(mUser.getIntroduce());
        mIntroduceEt.setEnabled(false);

        //隐藏提交按钮
        mCommitBt.setVisibility(View.GONE);
        /**
         * 登陆，归属地查询，物流查询功能可用
         */
        mCourierSelectBt.setEnabled(true);
        mPhoneSelectBt.setEnabled(true);
        mLogoutBt.setEnabled(true);
    }

    /**
     * 点击事件
     */
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.change_user_info://修改个人信息
                //显示修改界面
                showEditView();
                break;
            case R.id.user_commit_bt://提交修改个人信息
                //更新用户信息
                updataUserInfo();
                break;
            case R.id.user_image_view://点击头像
                mUserImageDialog.show();//显示头像设置框
                break;
            case R.id.user_courier_tv://物流查询
                goToCourier();
                break;
            case R.id.phone_select_tv://归属地查询
                goToPhont();
                break;
            case R.id.camera_bt://点击拍照
                goToCamera();//转入拍照
                break;
            case R.id.cancel_bt://点击取消
                //销毁dialog
                mUserImageDialog.dismiss();
                break;
            case R.id.picture_bt://点击图库
                goToPicture();
                break;
            case R.id.logout_user_bt://退出登陆
                userLogout();
                break;
        }

    }

    private void goToPhont() {
        Intent intent = new Intent(context, PhoneActivity.class);
        startActivity(intent);
    }

    /**
     * 物流查询
     */
    private void goToCourier() {
        Intent intent = new Intent(context, CourierActivity.class);
        startActivity(intent);
    }

    /**
     * 进入图库
     */
    private void goToPicture() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MyConstant.PHOTO_REQUEST_CODE);
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//如果大于等于7.0使用FileProvider
            mCropFile = new File(MyConstant.PHOTO_PATH);//裁剪后的File对象
//            Uri uriForFile = FileProvider.getUriForFile(getActivity(), "com.yj.smartbutler.provider", mCropFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, MyConstant.PHOTO_REQUEST_CODE);
        } else {
            startActivityForResult(intent, MyConstant.PHOTO_REQUEST_CODE);
        }
        mUserImageDialog.dismiss();
    }

    /**
     * 进入拍照
     */
    private void goToCamera() {
        //        checkSelfPermission 检测有没有 权限
//        PackageManager.PERMISSION_GRANTED 有权限
//        PackageManager.PERMISSION_DENIED  拒绝权限
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //权限发生了改变 true  //  false 小米
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                new AlertDialog.Builder(context).setTitle("title")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 请求授权
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, MyConstant.CAMERA_REQUEST_CODE);
                            }
                        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, MyConstant.PHOTO_REQUEST_CODE);
            }
        } else {
            camear();
        }
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        //判断内存是否可用,可用则存储---先判断版本
//        if(Build.VERSION.SDK_INT<24){
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory()+"/data/",MyConstant.PHOTO_IMAGE_FILE_NAME)));
//        }else {
//            // 中间的参数 authority 可以随意设置.
//            Uri uri = FileProvider.getUriForFile(context,"org.diql.fileprovider", new File(Environment.getExternalStorageDirectory()+"/data/",MyConstant.PHOTO_IMAGE_FILE_NAME));
//            intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
//        }
//        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent,MyConstant.PHOTO_REQUEST_CODE);
//        mUserImageDialog.dismiss();
    }

    /**
     * 退出登陆
     */
    private void userLogout() {
        BmobService.sendLogoutRequest();//退出登陆
        MyApplication.setUser(null);//全局用户为空
        startActivity(new Intent(context, LoginActivity.class));
        getActivity().finish();//销毁界面
    }

    /**
     * 更新用户信息
     */
    private void updataUserInfo() {
        User newUser = new User();
        newUser = mUser;
        newUser.setAge(Integer.parseInt(mAgeEt.getText().toString().trim()));
        newUser.setIntroduce(mIntroduceEt.getText().toString().trim());
        newUser.setSex(mBoyBt.isChecked());
        BmobService.updataUserInfo(newUser, mHandler);
    }

    /**
     * @param requestCode
     * @param permissions  请求的权限
     * @param grantResults 请求权限返回的结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            // camear 权限回调
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 表示用户授权
                ToastUtils.showShortToast("已授权");
                camear();
                //用户拒绝权限
                ToastUtils.showShortToast("未授权");
            }
        }
    }

    /**
     * 相机
     */
    private void camear() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT < 24) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, newUriByVersion());
            } else {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, newUriByVersion());
            }
            startActivityForResult(intent, MyConstant.PHOTO_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //判断内存是否可用,可用则存储---先判断版本
    private Uri newUriByVersion() {
        mCropFile = new File(MyConstant.PHOTO_PATH);
        if (Build.VERSION.SDK_INT < 24) {
            mPhotoUri = Uri.fromFile(mCropFile);
        } else {
            // 中间的参数 authority 可以随意设置.
            mPhotoUri = FileProvider.getUriForFile(getActivity(), "com.yj.smartbutler.provider", mCropFile);
        }
        return mPhotoUri;
    }

}
