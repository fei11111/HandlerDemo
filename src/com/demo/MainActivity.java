package com.demo;

public class MainActivity extends Activity {

    TextView textView;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message message) {
            System.out.println("子线程名字 = " + Thread.currentThread().getName());
            textView.setText((String)message.obj);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        System.out.println("进来onCreate");

        //测试创建TextView，并在子线程中更新数据

         textView = findViewById(R.id.text_view);
        textView.setText("初始化数据");

        new Thread() {
            @Override
            public void run() {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("子线程名字 = " + Thread.currentThread().getName());
                Message message = new Message();
                message.obj = "更新数据";
                mHandler.sendMessage(message);
//                textView.setText("更新数据");
//                Looper.prepare();
//                Handler handler = new Handler();
            }
        }.start();

    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("进来onResume");
    }
}
