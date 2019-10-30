<<<<<<< HEAD
package com.example.signlanguage;

import android.os.Handler;
import android.os.Message;

public class IntroThread extends  Thread {
    private Handler handler;

    public IntroThread(Handler handler){
        this.handler= handler;
    }
    @Override
    public void run() {
        Message msg = new Message();

        try {
            Thread.sleep(3000);
            msg.what = 1;
            handler.sendEmptyMessage(msg.what);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
=======
package com.example.signlanguage;

import android.os.Handler;
import android.os.Message;

public class IntroThread extends  Thread {
    private Handler handler;

    public IntroThread(Handler handler){
        this.handler= handler;
    }
    @Override
    public void run() {
        Message msg = new Message();

        try {
            Thread.sleep(3000);
            msg.what = 1;
            handler.sendEmptyMessage(msg.what);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
>>>>>>> 6ce524b3c2500aa88cd62069804333e159a41d7e
