package com.yk.eventposter;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventThreadManager {

    private static volatile EventThreadManager instance;

    private final Handler uiHandler = new Handler(Looper.getMainLooper());

    private final ExecutorService ioThread;

    private EventThreadManager() {
        ioThread = Executors.newSingleThreadExecutor();
    }

    public static EventThreadManager getInstance() {
        if (instance == null) {
            synchronized (EventThreadManager.class) {
                if (instance == null) {
                    instance = new EventThreadManager();
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
