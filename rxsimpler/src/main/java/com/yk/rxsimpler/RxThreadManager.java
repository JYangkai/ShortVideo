package com.yk.rxsimpler;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RxThreadManager {

    private static volatile RxThreadManager instance;

    private final Handler uiHandler = new Handler(Looper.getMainLooper());

    private final ExecutorService ioThread;

    private RxThreadManager() {
        ioThread = Executors.newSingleThreadExecutor();
    }

    public static RxThreadManager getInstance() {
        if (instance == null) {
            synchronized (RxThreadManager.class) {
                if (instance == null) {
                    instance = new RxThreadManager();
                }
            }
        }
        return instance;
    }

    public void postUi(Runnable runnable) {
        uiHandler.post(runnable);
    }

    public void postIo(Runnable runnable) {
        ioThread.execute(runnable);
    }
}
