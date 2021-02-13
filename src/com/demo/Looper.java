package com.demo;

import com.demo.Message;
import com.demo.MessageQueue;
import com.sun.istack.internal.Nullable;

public class Looper {

    // sThreadLocal.get() will return null unless you've called prepare().
    static final ThreadLocal<Looper> sThreadLocal = new ThreadLocal<Looper>();
    private static Looper sMainLooper;  // guarded by Looper.class

    public MessageQueue mQueue;

    public Looper() {
        mQueue = new MessageQueue();
    }

    public static void prepareMainLooper() {
        prepare();
        synchronized (Looper.class) {
            if (sMainLooper != null) {
                throw new IllegalStateException("The main Looper has already been prepared.");
            }
            sMainLooper = myLooper();
        }
    }

    public static @Nullable Looper myLooper() {
        return sThreadLocal.get();
    }


    public static void prepare() {
        if (sThreadLocal.get() != null) {
            throw new RuntimeException("Only one Looper may be created per thread");
        }
        sThreadLocal.set(new Looper());
    }

    /**
     * 循环从messageQueue取消息处理
     */
    public static void loop() {
        final Looper me = myLooper();
        final MessageQueue queue = me.mQueue;

        for (; ; ) {
            Message msg = queue.next(); // might block
            if (msg == null) {
                // No message indicates that the message queue is quitting.
                return;
            }
            msg.target.handleMessage(msg);

            msg.recycleUnchecked();
        }
    }
}

