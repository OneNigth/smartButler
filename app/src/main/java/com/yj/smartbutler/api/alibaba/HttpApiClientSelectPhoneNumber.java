//
//  Created by  fred on 2017/1/12.
//  Copyright © 2016年 Alibaba. All rights reserved.
//

package com.yj.smartbutler.api.alibaba;
import com.alibaba.cloudapi.sdk.client.HttpApiClient;
import com.alibaba.cloudapi.sdk.enums.HttpMethod;
import com.alibaba.cloudapi.sdk.enums.ParamPosition;
import com.alibaba.cloudapi.sdk.enums.Scheme;
import com.alibaba.cloudapi.sdk.model.ApiCallback;
import com.alibaba.cloudapi.sdk.model.ApiRequest;
import com.alibaba.cloudapi.sdk.model.HttpClientBuilderParams;




public class HttpApiClientSelectPhoneNumber extends HttpApiClient{
    public final static String HOST = "e5e36a3b453a418d86c9fb1658f36c98-cn-beijing.alicloudapi.com";
    static HttpApiClientSelectPhoneNumber instance = new HttpApiClientSelectPhoneNumber();
    public static HttpApiClientSelectPhoneNumber getInstance(){return instance;}

    public void init(HttpClientBuilderParams httpClientBuilderParams){
        httpClientBuilderParams.setScheme(Scheme.HTTP);
        httpClientBuilderParams.setHost(HOST);
        super.init(httpClientBuilderParams);
    }



    //手机号码归属地查询接口
    public void sendSelectPhoneNumber(String shouji , ApiCallback callback) {
        String path = "/shouji/query";
        ApiRequest request = new ApiRequest(HttpMethod.GET , path);
        request.addParam("shouji" , shouji , ParamPosition.QUERY , true);

        sendAsyncRequest(request , callback);
    }
}