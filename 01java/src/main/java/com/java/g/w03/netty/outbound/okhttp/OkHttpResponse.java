package com.java.g.w03.netty.outbound.okhttp;

import okhttp3.MediaType;
import okhttp3.Request;

public class OkHttpResponse {
    /**
     * 该response对应的request
     */
    private Request request;

    /**
     * 返回的状态码
     */
    private int code;

    /**
     * 跳转链接
     */
    private String location;

    /**
     * 返回的类型
     */
    private MediaType contentType;

    /**
     * 返回的内容
     */
    private String body;

    /**
     * Refresh头部内容
     */
    private String refresh;

    private int contentLength;

    public String getRefresh() {
        return refresh;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

//    public boolean isRedirect() {
//        switch (code) {
//            case HTTP_PERM_REDIRECT:
//            case HTTP_TEMP_REDIRECT:
//            case HTTP_MULT_CHOICE:
//            case HTTP_MOVED_PERM:
//            case HTTP_MOVED_TEMP:
//            case HTTP_SEE_OTHER:
//                return true;
//            default:
//                return false;
//        }
//    }

    public boolean isSuccessful() {
        return code >= 200 && code < 400;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public MediaType getContentType() {
        return contentType;
    }

    public void setContentType(MediaType contentType) {
        this.contentType = contentType;
    }
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }
}
