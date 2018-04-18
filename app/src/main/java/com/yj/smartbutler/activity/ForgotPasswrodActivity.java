package com.yj.smartbutler.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.yj.smartbutler.R;
import com.yj.smartbutler.service.BmobService;

/**
 * Created by yj on 2018/4/6.
 * 功能
 */

public class ForgotPasswrodActivity extends BaseActivity {
    //邮箱
    private EditText mEmailEt;
    //提交
    private Button mCommitBt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initView();
    }

    private void initView() {
        mEmailEt = findViewById(R.id.forgot_email_et);
        mCommitBt = findViewById(R.id.forgot_commit_bt);

        mCommitBt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.forgot_commit_bt://点击提交
                String email = mEmailEt.getText().toString().trim();
                if(checkEmail(email)) {//检查格式正确(未实现)则发送重置密码邮箱
                    boolean isSuccess = BmobService.resetPasswordByEmail(email);
                    if(isSuccess){//发送成功则销毁当前界面
                        finish();
                    }else {//否则无处理

                    }
                }
                break;
        }
    }

    /**
     * 可作为以后处理邮箱格式
     * @param email
     * @return
     */
    private boolean checkEmail(String email) {
        if(!TextUtils.isEmpty(email)){
            return false;
        }
        return true;
    }
}
