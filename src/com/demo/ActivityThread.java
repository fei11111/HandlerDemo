package com.demo;

/**
 * 模拟android ActivityThread 消息机制
 */
public class ActivityThread {
    public static void main(String[] args) {

        Looper.prepareMainLooper();


        attach(false);

        Looper.loop();



    }

    /**
     * 这里只为了演示
     *
     * @param b
     */
    private static void attach(boolean b) {
        //创建MainActivity

        MainActivity activity = new MainActivity();
        activity.onCreate();

        activity.onResume();
    }
}
