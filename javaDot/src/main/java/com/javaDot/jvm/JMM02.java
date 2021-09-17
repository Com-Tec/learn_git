package com.javaDot.jvm;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class JMM02 {
    private AtomicInteger cnt = new AtomicInteger();

    public void add() {
        cnt.incrementAndGet();
    }

    public int get() {
        return cnt.get();
    }

    public static void main(String[] args) throws InterruptedException {

        final int threadSize = 1000;

        JMM02 jmm02 = new JMM02(); // 只修改这条语句

        //CountDownLatch，倒计时器，让线程等待其他线程完成一组操作后才能执行，否则一直等待。通过AQS共享锁实现的。
        //new的时候设置了state=count，此时是获取不到锁的，所以调用await()的线程挂起并构造成结点进入AQS阻塞队列。
        //锁重入count次，依次-1，依次释放，直到=0，唤醒AQS队列中阻塞的线程获取锁
        final CountDownLatch countDownLatch = new CountDownLatch(threadSize);

        ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i = 0; i < threadSize; i++) {

            executorService.execute(() -> {

                jmm02.add();
                //CountDownLatch.countDown()方法是将count-1，如果发现count=0了，就唤醒阻塞的线程。
                //CountDownLatch.countDown()调用AQS释放锁的方法，每次将state减1。当state减到0时是无锁状态了，就依次唤醒AQS队列中阻塞的线程来获取锁，继续执行逻辑代码
                countDownLatch.countDown();

            });
        }

        //CountDownLatch.await()调用的就是AQS获取共享锁的方法。当AQS.state=0时才能获取到锁

        countDownLatch.await();
        //停止接收新任务，原来的任务继续执行。只是关闭了提交通道submit()
        executorService.shutdown();

        System.out.println("----"+jmm02.cnt.get());

    }

}