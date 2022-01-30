package com.yk.markdown.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableStringBuilder;
import android.widget.TextView;

import com.yk.markdown.core.MdParser;
import com.yk.markdown.core.MdRender;
import com.yk.markdown.style.style.BaseMdStyle;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RequestThreadManager {

    private static volatile RequestThreadManager instance;

    private final ExecutorService ioService;
    private final Handler uiHandler;

    private RequestThreadManager() {
        ioService = Executors.newFixedThreadPool(5);
        uiHandler = new Handler(Looper.getMainLooper());
    }

    public static RequestThreadManager getInstance() {
        if (instance == null) {
            synchronized (RequestThreadManager.class) {
                if (instance == null) {
                    instance = new RequestThreadManager();
                }
            }
        }
        return instance;
    }

    public void executeIo(Context context, String src, BaseMdStyle style, TextView tv) {
        ioService.execute(new IORunnable(context, src, style, tv));
    }

    public void executeUi(SpannableStringBuilder spanStrBuilder, TextView tv) {
        uiHandler.post(new UIRunnable(spanStrBuilder, tv));
    }

    public static class IORunnable implements Runnable {
        private final MdParser parser;
        private final MdRender render;

        private final Context context;
        private final String src;
        private final BaseMdStyle style;
        private final TextView tv;

        public IORunnable(Context context, String src, BaseMdStyle style, TextView tv) {
            parser = new MdParser();
            render = new MdRender();

            this.context = context;
            this.src = src;
            this.style = style;
            this.tv = tv;
        }

        @Override
        public void run() {
            SpannableStringBuilder spanStrBuilder = render.render(
                    context, parser.parser(src), style
            );

            RequestThreadManager.getInstance().executeUi(spanStrBuilder, tv);
        }
    }

    public static class UIRunnable implements Runnable {
        private final SpannableStringBuilder spanStrBuilder;
        private final TextView tv;

        public UIRunnable(SpannableStringBuilder spanStrBuilder, TextView tv) {
            this.spanStrBuilder = spanStrBuilder;
            this.tv = tv;
        }

        @Override
        public void run() {
            tv.setText(spanStrBuilder);
        }
    }

}
