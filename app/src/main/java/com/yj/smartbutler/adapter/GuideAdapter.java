package com.yj.smartbutler.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by yj on 2018/3/30.
 * 功能 引导页面的适配器 无特殊处理
 */

public class GuideAdapter extends PagerAdapter {
    public List<View> mPagers;

    public GuideAdapter(List<View> mPagers) {
        this.mPagers = mPagers;
    }

    @Override
    public int getCount() {
        return mPagers.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager)container).addView(mPagers.get(position));
        return mPagers.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager)container).removeView(mPagers.get(position));
    }
}
