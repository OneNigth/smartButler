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

public class HttpApiClientReportAnswer extends HttpApiClient {
    public final static String HOST = "e81a161d4975407fbd013f2dd298535b-cn-beijing.alicloudapi.com";
    static HttpApiClientReportAnswer instance = new HttpApiClientReportAnswer();
    public static HttpApiClientReportAnswer getInstance(){return instance;}

    public void init(HttpClientBuilderParams httpClientBuilderParams){
        httpClientBuilderParams.setScheme(Scheme.HTTP);
        httpClientBuilderParams.setHost(HOST);
        super.init(httpClientBuilderParams);
    }


    /**
     * 智能回复接口
     * @param question
     * @param callback
     */
    public void sendReportAnswer(String question , ApiCallback callback) {
        String path = "/iqa/query";
        ApiRequest request = new ApiRequest(HttpMethod.GET , path);
        request.addParam("question" , question , ParamPosition.QUERY , true);



        sendAsyncRequest(request , callback);
    }
}