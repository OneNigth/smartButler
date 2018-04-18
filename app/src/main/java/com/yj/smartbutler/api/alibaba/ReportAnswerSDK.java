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

public class ReportAnswerSDK {

    static{
        //HTTP Client init
        HttpClientBuilderParams httpParam = new HttpClientBuilderParams();
        httpParam.setAppKey(APIConstant.ALIBABA_APP_KEY);
        httpParam.setAppSecret(APIConstant.ALIBABA_APP_SECRET);
        HttpApiClientReportAnswer.getInstance().init(httpParam);

    }

    /**
     * 智能回复接口
     */
    public static void ReportAnswerHttpTest(String question,ApiCallback callback){
        HttpApiClientReportAnswer.getInstance().sendReportAnswer(question ,callback);
    }




    private static String getResultString(ApiResponse response) throws IOException {
        StringBuilder result = new StringBuilder();
        result.append("Response from backend server").append(SdkConstant.CLOUDAPI_LF).append(SdkConstant.CLOUDAPI_LF);
        result.append("ResultCode:").append(SdkConstant.CLOUDAPI_LF).append(response.getCode()).append(SdkConstant.CLOUDAPI_LF).append(SdkConstant.CLOUDAPI_LF);
        if(response.getCode() != 200){
            result.append("Error description:").append(response.getHeaders().get("X-Ca-Error-Message")).append(SdkConstant.CLOUDAPI_LF).append(SdkConstant.CLOUDAPI_LF);
        }

        result.append("ResultBody:").append(SdkConstant.CLOUDAPI_LF).append(new String(response.getBody() , SdkConstant.CLOUDAPI_ENCODING));

        return result.toString();
    }
}