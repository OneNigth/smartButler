package com.yj.croptext;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView textView;
    private String mImagePath = Environment.getExternalStorageDirectory() + "/meta/";
    private static final int START_PHOTO_IMAGE_REQUEST = 1;
    private static final int START_CROP_IMAGE_REQUEST = 2;
    private static final int START_PHOTO_CAMERA_REQUEST = 3;

    private String mPhotoPath;
    private Bitmap mBitmap;
    private File mAvatarFile;
    private File fileUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.tv);
        textView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv:
                allPhoto();
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == START_PHOTO_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            //调用相册
            Cursor cursor = getContentResolver().query(data.getData(), new String[]{MediaStore.Images.Media.DATA}, null, null, null);
            //游标移到第一位，即从第一位开始读取
            if (cursor != null) {
                cursor.moveToFirst();
                mPhotoPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                cursor.close();
                //调用系统裁剪
                mAvatarFile = new File(mPhotoPath);
                final Uri uri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    uri = FileProvider.getUriForFile(this, "com.cloudminds.meta.provider", mAvatarFile);
                } else {
                    uri = Uri.fromFile(mAvatarFile);
                }
                startPhoneZoom(uri);
            }
        }

        if (requestCode == START_PHOTO_CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            //相机返回结果，调用系统裁剪
            mAvatarFile = new File(mPhotoPath);
            Uri uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(this, "com.***.***.fileprovider", mAvatarFile);
            } else {
                uri = Uri.fromFile(mAvatarFile);
            }
            startPhoneZoom(uri);
        }
        if (requestCode == START_CROP_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            //设置裁剪返回的位图
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                mBitmap = bundle.getParcelable("data");
                Log.d("--------", "--------------bitmap------------" + mBitmap);
            }
        }
    }

    /**
     * 调用系统裁剪的方法
     */
    private void startPhoneZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        //是否可裁剪
        intent.putExtra("corp", "true");
        //裁剪器高宽比
        intent.putExtra("aspectY", 1);
        intent.putExtra("aspectX", 1);
        //设置裁剪框高宽
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        //返回数据
        intent.putExtra("return-data", true);
        startActivityForResult(intent, START_CROP_IMAGE_REQUEST);
    }

    /**
     * 调用手机相册
     */
    private void allPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, START_PHOTO_IMAGE_REQUEST);
    }
    /**
     * 调用手机相机
     */
    private void getImageFromPhoto() {
        File file = new File(Environment.getExternalStorageDirectory()+"");
        if (!file.exists()) {
            file.mkdirs();
        }
        mPhotoPath = Environment.getExternalStorageDirectory()  + String.valueOf(System.currentTimeMillis()) + ".jpg";
        fileUri = new File(mPhotoPath);
        Intent intentCamera = new Intent();
        Uri imageUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(this, "com.***.***.fileprovider", fileUri);
        } else {
            imageUri = Uri.fromFile(fileUri);
        }
        intentCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intentCamera, START_PHOTO_CAMERA_REQUEST);
    }
}
