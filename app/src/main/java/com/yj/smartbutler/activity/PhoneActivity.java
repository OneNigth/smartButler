package com.yj.smartbutler.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alibaba.cloudapi.sdk.model.ApiCallback;
import com.alibaba.cloudapi.sdk.model.ApiRequest;
import com.alibaba.cloudapi.sdk.model.ApiResponse;
import com.yj.smartbutler.R;
import com.yj.smartbutler.api.alibaba.SelectPhoneNumberSDK;
import com.yj.smartbutler.constant.MyConstant;
import com.yj.smartbutler.utils.LogUtils;
import com.yj.smartbutler.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by yj on 2018/4/17.
 * 功能
 */

public class PhoneActivity extends BaseActivity {

    //输入框
    private EditText et_number;
    //公司logo
    private ImageView iv_company;
    //结果
    private TextView tv_result;

    private Button btn_0, btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_del, btn_query;

    //标记位
    private boolean flag = false;


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MyConstant.PHONE_SELECT_HANDLER://得到返回结果
                    Bundle bundle = msg.getData();//拿到数据
                    String s =  bundle.getString("phone_select" ,"");
                    ToastUtils.showShortToast("查询返回(不登陆阿里云就返回错误):" + s);
                    parsingJson(s);//json解析
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        initView();
    }

    //初始化View
    private void initView() {
        et_number = findViewById(R.id.phone_select_et);
        et_number.setText("15633585699");
        iv_company = findViewById(R.id.phone_company_iv);
        tv_result = findViewById(R.id.phone_result_tv);

        btn_0 = findViewById(R.id.btn_0);
        btn_0.setOnClickListener(this);
        btn_1 = findViewById(R.id.btn_1);
        btn_1.setOnClickListener(this);
        btn_2 = findViewById(R.id.btn_2);
        btn_2.setOnClickListener(this);
        btn_3 = findViewById(R.id.btn_3);
        btn_3.setOnClickListener(this);
        btn_4 = findViewById(R.id.btn_4);
        btn_4.setOnClickListener(this);
        btn_5 = findViewById(R.id.btn_5);
        btn_5.setOnClickListener(this);
        btn_6 = findViewById(R.id.btn_6);
        btn_6.setOnClickListener(this);
        btn_7 = findViewById(R.id.btn_7);
        btn_7.setOnClickListener(this);
        btn_8 = findViewById(R.id.btn_8);
        btn_8.setOnClickListener(this);
        btn_9 = findViewById(R.id.btn_9);
        btn_9.setOnClickListener(this);
        btn_del = findViewById(R.id.btn_del);
        btn_del.setOnClickListener(this);
        btn_query = findViewById(R.id.btn_query);
        btn_query.setOnClickListener(this);

        //长按事件
        btn_del.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                et_number.setText("");
                return false;
            }
        });
    }

    //点击事件
    @Override
    public void onClick(View v) {
        /**
         * 逻辑
         * 1.获取输入框的内容
         * 2.判断是否为空
         * 3.网络请求
         * 4.解析Json
         * 5.结果显示
         *
         * ------
         * 键盘逻辑
         */

        //获取到输入框的内容
        String str = et_number.getText().toString();

        switch (v.getId()) {
            case R.id.btn_0:
            case R.id.btn_1:
            case R.id.btn_2:
            case R.id.btn_3:
            case R.id.btn_4:
            case R.id.btn_5:
            case R.id.btn_6:
            case R.id.btn_7:
            case R.id.btn_8:
            case R.id.btn_9:
                if (flag) {
                    flag = false;
                    str = "";
                    et_number.setText("");
                }
                //每次结尾添加1
                et_number.setText(str + ((Button) v).getText());
                //移动光标
                et_number.setSelection(str.length() + 1);
                break;
            case R.id.btn_del:
                if (TextUtils.isEmpty(str) && str.length() > 0) {
                    //每次结尾减去1
                    et_number.setText(str.substring(0, str.length() - 1));
                    //移动光标
                    et_number.setSelection(str.length() - 1);
                }
                break;
            case R.id.btn_query:
                if (!TextUtils.isEmpty(str)) {
                    getPhone(str);
                }
                break;
        }

    }

    //获取归属地
    private void getPhone(String str) {
        //查询号码
        SelectPhoneNumberSDK.selectPhoneNumberHttpTest(str, new ApiCallback() {
            @Override
            public void onFailure(ApiRequest apiRequest, Exception e) {
                ToastUtils.showShortToast("有问题");
                e.printStackTrace();
            }

            @Override
            public void onResponse(ApiRequest apiRequest, ApiResponse apiResponse) {
                try {
                    //获取归属地查询json对象
                    String s = SelectPhoneNumberSDK.getResultString(apiResponse);
                    LogUtils.d("电话查询结果:" + s);
                    //传入json对象通知线程更新ui
                    Message message = new Message();
                    message.what = MyConstant.PHONE_SELECT_HANDLER;
                    Bundle bundle = new Bundle();
                    bundle.putString("phone_select",new String(apiResponse.getBody(), SdkConstant.CLOUDAPI_ENCODING));
                    message.setData(bundle);
                    mHandler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * ResultCode:200
     * ResultBody:
     * {"status":"0",
     * "msg":"ok",
     * "result":{"shouji":"15622585255","province":"广东","city":"韶关","company":"中国联通","cardtype":"GSM\/3G","areacode":"0751"}}
     */

    //解析Json
    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONObject jsonResult = jsonObject.getJSONObject("result");
            String province = jsonResult.getString("province");
            String city = jsonResult.getString("city");
            String areacode = jsonResult.getString("areacode");
//            String zip = jsonResult.getString("zip");
            String company = jsonResult.getString("company");
            String card = jsonResult.getString("cardtype");

            tv_result.setText("归属地:" + province + city + "\n"
                    + "区号:" + areacode + "\n"
                    + "运营商:" + company + "\n"
                    + "类型:" + card);

//                    + "邮编:" + zip + "\n"
            //图片显示
            switch (company) {
                case "中国移动":
                    iv_company.setBackgroundResource(R.drawable.china_mobile);
                    break;
                case "中国联通":
                    iv_company.setBackgroundResource(R.drawable.china_unicom);
                    break;
                case "中国电信":
                    iv_company.setBackgroundResource(R.drawable.china_telecom);
                    break;
            }
            flag = true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
