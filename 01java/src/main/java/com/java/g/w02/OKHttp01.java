package com.java.g.w02;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class OKHttp01 {
    public static void main(String[] args) throws IOException {
        String url="http://127.0.0.1:8801";
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try(Response response=okHttpClient.newCall(request).execute()){
            if(response.isSuccessful()){
                System.out.println(response.body().string());
            }else{
                System.out.println("exception happend---"+response);
            }

        }
        ;

    }
}
