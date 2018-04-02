package com.yj.smartbutler.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.yj.smartbutler.R;
import com.yj.smartbutler.adapter.GuideAdapter;
import com.yj.smartbutler.utils.UtilTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yj on 2018/3/30.
 * 功能
 */

public class GuideActivity extends BaseActivity  {
    //跳过按键
    private ImageView mJumpButton;
    //进入按键
    private Button mEnterButton;

    //导航的三个界面
    private List<View> mPagers;
    //导航viewpager
    private ViewPager mGuideViewPager;
    //导航的三个圆点
    private List<ImageView> mPoints;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
    }

    //界面初始化
    private void initView() {
        mJumpButton = findViewById(R.id.splash_jump_btn);
        mJumpButton.setOnClickListener(this);
        mGuideViewPager = findViewById(R.id.guide_view_pager);
        //viewpager数据
        mPagers = new ArrayList<>();
        View pager1 = View.inflate(context, R.layout.guide_pager_one, null);
        View pager2 = View.inflate(context, R.layout.guide_pager_two, null);
        View pager3 = View.inflate(context, R.layout.guide_pager_three, null);
        mEnterButton = pager3.findViewById(R.id.guide_enter_btn);
        UtilTools.SetFont(context,mEnterButton);//字体设置
        mEnterButton.setOnClickListener(this);
        mPagers.add(pager1);
        mPagers.add(pager2);
        mPagers.add(pager3);
        //三个圆点
        mPoints = new ArrayList<>();
        ImageView point1 = findViewById(R.id.guide_point1);
        ImageView point2 = findViewById(R.id.guide_point2);
        ImageView point3 = findViewById(R.id.guide_point3);
        mPoints.add(point1);
        mPoints.add(point2);
        mPoints.add(point3);
        point1.setImageResource(R.drawable.point_on);
        point2.setImageResource(R.drawable.point_off);
        point3.setImageResource(R.drawable.point_off);


        mGuideViewPager.setAdapter(new GuideAdapter(mPagers));
        //一二页显示跳过view显示进入view，最后一页隐藏跳过view显示进入view，和页面圆点的切换
        mGuideViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            //按当前页数显示相应的界面
            @Override
            public void onPageSelected(int position) {

                showViewByPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
            /**
             * 通过页数正确显示布局
             */
            private void showViewByPosition(int position) {
                for(int i = 0 ; i<mPoints.size();i++){
                    //显示当前页面的圆点
                    if(i==position){
                        mPoints.get(i).setImageResource(R.drawable.point_on);
                    }else {
                        mPoints.get(i).setImageResource(R.drawable.point_off);
                    }
                    //最后一页显示进入按键，隐藏跳过按键，否则相反
                    if(position==mPoints.size()-1){
                        mJumpButton.setVisibility(View.INVISIBLE);
                        mEnterButton.setVisibility(View.VISIBLE);
                    }else {
                        mJumpButton.setVisibility(View.VISIBLE);
                        mEnterButton.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }



    //点击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.splash_jump_btn://点击跳过导航按键
            case R.id.guide_enter_btn://点击进入按键
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                finish();
                break;

        }
    }
}
