package com.javaDot.jvm;

import com.javaDot.jvm.JMM01;

public class JMM03 {
    private static class InstanceHolder {
        public static JMM01 instance = new JMM01();
    }

    public static JMM01 getInstance() {
        return InstanceHolder.instance ;  // 这里将导致 InstanceHolder 类被初始化
    }
}
