package com.java.g.w03.netty.outbound.okhttp;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

import java.util.LinkedList;
import java.util.Queue;

public class OkHttpCallBackPool {
        private static OkHttpCallBackPool instance = new OkHttpCallBackPool();
        private static Logger log = LoggerFactory.getLogger(OkHttpCallBackPool.class);

        private Queue<OkHttpCallback> inactive = new LinkedList<>();

        private OkHttpCallBackPool() {
        }

        public static OkHttpCallBackPool getInstance() {
            return instance;
        }

        /**
         * 获取一个HttpCallBack
         *
         * @return
         */
        public OkHttpCallback allocate(long itemId, int successEventType, int failureEventType) {
            OkHttpCallback httpCallback = inactive.poll();
            if (httpCallback == null) {
                httpCallback = new OkHttpCallback();
            }
            httpCallback.setItemId(itemId);
            httpCallback.setSuccessEventType(successEventType);
            httpCallback.setFailureEventType(failureEventType);
            return httpCallback;
        }

        /**
         * 释放HttpCallBack
         *
         * @param
         */
        public void release(OkHttpCallback httpCallback) {
//    httpCallback.setItemId(EventBus.INVALID_ITEM_ID);
            inactive.add(httpCallback);
            System.out.println("httpCallbackPool size: {}"+inactive.size());
        }

}
