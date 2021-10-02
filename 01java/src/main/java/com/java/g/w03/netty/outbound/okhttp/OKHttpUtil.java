package com.java.g.w03.netty.outbound.okhttp;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.util.Map;

public class OKHttpUtil {

    private OKHttpUtil() {}
    private static OKHttpUtil instance = new OKHttpUtil();
    public static OKHttpUtil getInstance() {
        return instance;
    }

    private OkHttpClient httpClient = new OkHttpClient().newBuilder().followRedirects(false).build();

    public void enqueue(String url, Callback callBack) {
        enqueue(url, callBack, null);
    }

    public void enqueue(String url, Callback callBack, Map<String, String> headers) {
        Request.Builder builder = new Request.Builder().url(url);
        if(headers !=null && headers.size()>0) {
            for(String key : headers.keySet()) {
                builder = builder.addHeader(key, headers.get(key));
            }
        }
        Request request = builder.build();
        httpClient.newCall(request).enqueue(callBack);
    }
}
