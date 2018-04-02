package com.yj.smartbutler.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.yj.smartbutler.application.MyApplication;

/**
 * Created by yj on 2018/3/29.
 */

public class BaseFragment extends Fragment {
    public Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = MyApplication.getContext();
    }
}
