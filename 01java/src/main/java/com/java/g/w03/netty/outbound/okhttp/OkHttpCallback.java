package com.java.g.w03.netty.outbound.okhttp;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;

public class OkHttpCallback implements Callback {

    private int successEventType;
    private int failureEventType;
    private long itemId;

    @Override
    public void onFailure(Call call, IOException e) {
        System.out.println("send http request fail. offer id {}, url {}"+itemId+"---"+call.request().url().toString());
        System.out.println("send http request fail reason !"+e);
//    EventBus.getInstance().push(failureEventType, itemId, null);
        OkHttpCallBackPool.getInstance().release(this);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        OkHttpResponse httpResponse = buildHttpResponse(response);
//    EventBus.getInstance().push(successEventType, itemId, httpResponse);
        OkHttpCallBackPool.getInstance().release(this);
    }

    private OkHttpResponse buildHttpResponse(Response response) {
        OkHttpResponse httpResponse = new OkHttpResponse();
        httpResponse.setRequest(response.request());
        httpResponse.setCode(response.code());
        httpResponse.setLocation(response.header("Location"));
        httpResponse.setContentType(response.body().contentType());
        httpResponse.setRefresh(response.header("Refresh"));
        httpResponse.setContentLength(Integer.valueOf(Long.toString(response.body().contentLength())));
        try {
            httpResponse.setBody(response.body().string());
        } catch (IOException e) {
            System.out.println("get response body fail. url: {}"+response.request().url().toString());
            httpResponse.setBody("");
        }
        return httpResponse;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public void setSuccessEventType(int successEventType) {
        this.successEventType = successEventType;
    }

    public void setFailureEventType(int failureEventType) {
        this.failureEventType = failureEventType;
    }
}
