package com.yk.blockwatcher;

import android.os.Looper;
import android.util.Printer;

public class BlockWatcher {
    private static final String START = ">>>>> Dispatching to";
    private static final String END = "<<<<< Finished to";

    public static void start() {
        Looper.getMainLooper().setMessageLogging(new Printer() {
            @Override
            public void println(String x) {
                if (x.startsWith(START)) {
                    BlockManager.getInstance().post();
                } else if (x.startsWith(END)) {
                    BlockManager.getInstance().cancel();
                }
            }
        });
    }

}
