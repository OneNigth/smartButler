package com.yj.smartbutler.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yj.smartbutler.R;

/**
 * Created by yj on 2018/3/29.
 */

public class GirlsFragment extends BaseFragment {
    //找id的
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_girl, container, false);
        return view;
    }
}
