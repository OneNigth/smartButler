package com.yj.smartbutler.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.yj.smartbutler.R;
import com.yj.smartbutler.adapter.CourierListAdapter;
import com.yj.smartbutler.api.APIConstant;
import com.yj.smartbutler.constant.NetConstant;
import com.yj.smartbutler.empty.CouriserData;
import com.yj.smartbutler.utils.LogUtils;
import com.yj.smartbutler.utils.RxVolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by yj on 2018/4/11.
 * 功能 快递界面
 */

public class CourierActivity extends BaseActivity {
    //快递公司
    private EditText mCourierCompanyTv;
    //快递单号
    private EditText mCourierNumEt;
    //查询按钮
    private Button mSelectBt;
    //物流显示
    private ListView mCourierLv;
    //物流详情
    private List<CouriserData> mList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier);
        initView();
    }

    private void initView() {
        mCourierCompanyTv = findViewById(R.id.courier_com_et);
        mCourierNumEt = findViewById(R.id.courier_num_et);
        mCourierLv = findViewById(R.id.courier_listview);
        mSelectBt = findViewById(R.id.courier_select_bt);

        mCourierCompanyTv.setText("yt");
        mCourierNumEt.setText("888994257878899251");
        mList = new ArrayList<>();

        mSelectBt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.courier_select_bt://查询
                selectCourier();//快递查询
                break;
        }
    }

    /**
     * 快递查询
     */
    private void selectCourier() {
        String com = mCourierCompanyTv.getText().toString().trim();
        String no = mCourierNumEt.getText().toString().trim();
        //快递查询参数
        HttpParams params = new HttpParams();
        params.put("com" , com);
        params.put("no" ,no);
        params.put("key", APIConstant.JUHE_APPKEY);
        //发送请求
        RxVolleyUtils.sendPostRequest(NetConstant.JUHE_COURISER_URL ,params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                jsonResolve(t);//json解析
            }
        });
    }
    //json解析
    private void jsonResolve(String s) {
        try {
            LogUtils.d(s);
            JSONObject jsonObject = new JSONObject(s);
            JSONObject jsonResult = jsonObject.getJSONObject("result");
            JSONArray jsonArray = jsonResult.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = (JSONObject) jsonArray.get(i);

                CouriserData data = new CouriserData();
                data.remark=json.getString("remark");
                data.zone=json.getString("zone");
                data.datetime=json.getString("datetime");
                mList.add(data);
            }
            //倒序
            Collections.reverse(mList);
            CourierListAdapter adapter = new CourierListAdapter(context,mList);
            mCourierLv.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
