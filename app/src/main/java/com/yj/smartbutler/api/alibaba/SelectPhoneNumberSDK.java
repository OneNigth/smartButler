package com.yj.smartbutler.api.alibaba;

import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alibaba.cloudapi.sdk.model.ApiCallback;
import com.alibaba.cloudapi.sdk.model.ApiResponse;
import com.alibaba.cloudapi.sdk.model.HttpClientBuilderParams;
import com.yj.smartbutler.api.APIConstant;

import java.io.IOException;


/**
 * Created by yj on 2018/4/5.
 * 功能 智能机器人
 */
public class SelectPhoneNumberSDK {


    static {
        //HTTP Client init
        HttpClientBuilderParams httpParam = new HttpClientBuilderParams();
        httpParam.setAppKey(APIConstant.ALIBABA_APP_KEY);
        httpParam.setAppSecret(APIConstant.ALIBABA_APP_SECRET);
        HttpApiClientSelectPhoneNumber.getInstance().init(httpParam);

    }

    /**
     * 手机号码归属地查询接口
     */
    public static void selectPhoneNumberHttpTest(String phoneNum, ApiCallback callback) {
        HttpApiClientSelectPhoneNumber.getInstance().sendSelectPhoneNumber(phoneNum, callback);
    }


    public static String getResultString(ApiResponse response) throws IOException {
        StringBuilder result = new StringBuilder();
        result.append("Response from backend server").append(SdkConstant.CLOUDAPI_LF).append(SdkConstant.CLOUDAPI_LF);
        result.append("ResultCode:").append(SdkConstant.CLOUDAPI_LF).append(response.getCode()).append(SdkConstant.CLOUDAPI_LF).append(SdkConstant.CLOUDAPI_LF);
        if (response.getCode() != 200) {
            result.append("Error description:").append(response.getHeaders().get("X-Ca-Error-Message")).append(SdkConstant.CLOUDAPI_LF).append(SdkConstant.CLOUDAPI_LF);
        }

        result.append("ResultBody:").append(SdkConstant.CLOUDAPI_LF).append(new String(response.getBody(), SdkConstant.CLOUDAPI_ENCODING));

        return result.toString();
    }
}
