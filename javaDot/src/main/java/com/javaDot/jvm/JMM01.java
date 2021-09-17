package com.javaDot.jvm;

/**
 * int类型，复合操作，非原子性造成的线程安全问题。。。演示
 * 解决方案：
 * 1使用Synchronized锁
 * 2使用原子类AtomicInteger(具体实现--JMM02)
 */
public class JMM01 {
    public int cnt=0;
    public void add(){
        cnt++;
        System.out.println(Thread.currentThread().getName()+"---"+cnt);
    }
    public static void main(String[] args) {
        final JMM01 jmm01=new JMM01();
        for(int i=0;i<100;i++){
            // new Thread(jmm01::add,"线程"+i).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for(int j=0;j<10000;j++){
                        jmm01.add();
                    }
                }
            }).start();

        }
        // Thread.activeCount() 当前线程所在线程组中线程存活数，idea中=2而不是预期的1（eclipse=1），因为idea执行的时候会创建一个监控线程
        //while循环的目的：保证前面的线程都执行完
        while(Thread.activeCount()>2)
            //Thread.yield() ，当前线程由running.running状态-->runnable.ready状态
            Thread.yield();
        System.out.println("----"+jmm01.cnt);
    }
}
