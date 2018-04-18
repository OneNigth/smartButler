package com.yj.smartbutler.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.yj.smartbutler.R;
import com.yj.smartbutler.view.CustomDialog;
import com.yj.smartbutler.view.fragment.ButlerFragment;
import com.yj.smartbutler.view.fragment.GirlsFragment;
import com.yj.smartbutler.view.fragment.SelectFragment;
import com.yj.smartbutler.view.fragment.UserFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    //界面选项卡
    private TabLayout mTabLayout;
    //滑动界面
    private ViewPager mViewPager;
    //浮点button
    private FloatingActionButton mFloatingBtn;
    //列表的标题
    private List<String> mTabTitles;
    //fragment
    private List<Fragment> mFragments;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();

    }

    private void initData() {
        //标签栏
        mTabTitles = new ArrayList();
        mTabTitles.add(getString(R.string.tab_title_one));
        mTabTitles.add(getString(R.string.tab_title_two));
        mTabTitles.add(getString(R.string.tab_title_three));
        mTabTitles.add(getString(R.string.tab_title_four));
        //fragment
        mFragments = new ArrayList<>();
        Fragment butlerFragment = new ButlerFragment();
        Fragment selectFragment = new SelectFragment();
        Fragment girlsFragment = new GirlsFragment();
        Fragment userrFragment = new UserFragment();
        mFragments.add(butlerFragment);
        mFragments.add(selectFragment);
        mFragments.add(girlsFragment);
        mFragments.add(userrFragment);

        mFloatingBtn = findViewById(R.id.main_floating_btn);
        //首页隐藏浮点button
        mFloatingBtn.setVisibility(View.GONE);
        mFloatingBtn.setOnClickListener(this);
    }

    private void initView() {
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.main_view_pager);
        //viewpager适配器
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTabTitles.get(position);
            }
        });
        //首页隐藏浮点button
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mFloatingBtn.setVisibility(View.GONE);
                } else {
                    mFloatingBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //预加载
        mViewPager.setOffscreenPageLimit(mFragments.size());
        //绑定viewpager
        mTabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_floating_btn:
                Intent intent = new Intent(context, SettingActivity.class);
                startActivity(intent);
                break;
        }
    }
}
