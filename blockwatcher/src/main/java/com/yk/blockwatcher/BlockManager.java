package com.yk.blockwatcher;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

public class BlockManager {
    private static final String TAG = "BLOCK-LOG";

    private static final long MAX_BLOCK_TIME = 100L;

    private static volatile BlockManager instance;

    private final Handler blockHandler;

    private long startTime;

    private int times;

    private BlockManager() {
        HandlerThread thread = new HandlerThread("block-thread");
        thread.start();
        blockHandler = new Handler(thread.getLooper());
    }

    public static BlockManager getInstance() {
        if (instance == null) {
            synchronized (BlockManager.class) {
                if (instance == null) {
                    instance = new BlockManager();
                }
            }
        }
        return instance;
    }

    public void post() {
        startTime = System.currentTimeMillis();
        times = 1;
        execute();
    }

    public void cancel() {
        blockHandler.removeCallbacks(traceRunnable);
    }

    public void execute() {
        blockHandler.postDelayed(traceRunnable, times * MAX_BLOCK_TIME);
        times++;
    }

    private final Runnable traceRunnable = new Runnable() {
        @Override
        public void run() {
            String trace = getTrace();

            if (TextUtils.isEmpty(trace)) {
                return;
            }

            Log.d(TAG, "\ncoast:" + (System.currentTimeMillis() - startTime) + "\n" + trace);

            execute();
        }
    };

    private String getTrace() {
        StackTraceElement[] stackTraceElements = Looper.getMainLooper().getThread().getStackTrace();
        if (stackTraceElements.length <= 0) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            sb.append(stackTraceElement.toString()).append("\n");
        }

        return sb.toString();
    }

}
