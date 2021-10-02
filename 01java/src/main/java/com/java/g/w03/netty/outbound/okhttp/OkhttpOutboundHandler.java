package com.java.g.w03.netty.outbound.okhttp;

import com.java.g.w03.netty.filter.HeaderHttpResponseFilter;
import com.java.g.w03.netty.filter.HttpRequestFilter;
import com.java.g.w03.netty.filter.HttpResponseFilter;
import com.java.g.w03.netty.outbound.httpClient4.NamedThreadFactory;
import com.java.g.w03.netty.router.HttpEndpointRouter;
import com.java.g.w03.netty.router.RandomHttpEndpointRouter;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import lombok.SneakyThrows;
import okhttp3.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class OkhttpOutboundHandler {
    private OkHttpClient okHttpClient;
    private ExecutorService proxyService;
    private List<String> backendUrls;

    HttpResponseFilter filter = new HeaderHttpResponseFilter();
    HttpEndpointRouter router = new RandomHttpEndpointRouter();
    public OkhttpOutboundHandler(List<String> backends) {

        this.backendUrls = backends.stream().map(this::formatUrl).collect(Collectors.toList());

        int cores = Runtime.getRuntime().availableProcessors();
        long keepAliveTime = 1000;
        int queueSize = 2048;
        RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();//.DiscardPolicy();
        proxyService = new ThreadPoolExecutor(cores, cores,
                keepAliveTime, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(queueSize),
                new NamedThreadFactory("proxyService"), handler);

    }

    private String formatUrl(String backend) {
        return backend.endsWith("/")?backend.substring(0,backend.length()-1):backend;
    }

    public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, HttpRequestFilter filter) {
        String backendUrl = router.route(this.backendUrls);
        final String url = backendUrl + fullRequest.uri();
        filter.filter(fullRequest, ctx);
        proxyService.submit(()->fetchGet(fullRequest, ctx, url));
    }

    private void fetchGet(final FullHttpRequest inbound, final ChannelHandlerContext ctx, final String url) {
       // final HttpGet httpGet = new HttpGet(url);
        okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override

            public void onFailure(Call call, IOException e) {
                System.out.println("okhttp callback failure...");
            }


            @SneakyThrows
            @Override

            public void onResponse(Call call, Response response) throws
                    IOException {
                // Exception in thread "OkHttp Dispatcher" java.lang.IllegalStateException: closed
                // 这个错误是由于response.body().string()调用了多次导致的，string()仅可调用一次！
               // System.out.println(response.body().string());
                OkHttpResponse httpResponse = buildHttpResponse(response);

                handleResponse(inbound, ctx,  httpResponse);
            }

        });


    }

    private void handleResponse(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, final OkHttpResponse endpointResponse) throws Exception {
        FullHttpResponse response = null;
        try {
//            String value = "hello,kimmking";
//            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(value.getBytes("UTF-8")));
//            response.headers().set("Content-Type", "application/json");
//            response.headers().setInt("Content-Length", response.content().readableBytes());


            byte[] body = endpointResponse.getBody().getBytes();
//            System.out.println(new String(body));
//            System.out.println(body.length);

            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(body));

            response.headers().set("Content-Type", "application/json");
            response.headers().setInt("Content-Length", Integer.valueOf(endpointResponse.getContentLength()));

            filter.filter(response);

//            for (Header e : endpointResponse.getAllHeaders()) {
//                //response.headers().set(e.getName(),e.getValue());
//                System.out.println(e.getName() + " => " + e.getValue());
//            }

        } catch (Exception e) {
            e.printStackTrace();
            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
            exceptionCaught(ctx, e);
        } finally {
            if (fullRequest != null) {
                if (!HttpUtil.isKeepAlive(fullRequest)) {
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    //response.headers().set(CONNECTION, KEEP_ALIVE);
                    ctx.write(response);
                }
            }
            ctx.flush();
            //ctx.close();
        }

    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
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
}
