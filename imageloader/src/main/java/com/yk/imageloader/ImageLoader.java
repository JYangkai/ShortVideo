package com.yk.imageloader;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.yk.imageloader.cache.DiskCache;
import com.yk.imageloader.requester.BaseRequester;
import com.yk.imageloader.retriever.RequestRetriever;

public class ImageLoader {

    private final RequestRetriever requestRetriever;

    private static volatile ImageLoader instance;

    private ImageLoader(Context context) {
        requestRetriever = new RequestRetriever();
        DiskCache.getInstance().init(context.getApplicationContext());
    }

    public static ImageLoader getInstance(Context context) {
        if (instance == null) {
            synchronized (ImageLoader.class) {
                if (instance == null) {
                    instance = new ImageLoader(context);
                }
            }
        }
        return instance;
    }

    public RequestRetriever getRequestRetriever() {
        return requestRetriever;
    }

    public static RequestRetriever getRetriever(Context context) {
        return ImageLoader.getInstance(context).getRequestRetriever();
    }

    public static BaseRequester with(Context context) {
        if (context instanceof FragmentActivity) {
            return with((FragmentActivity) context);
        } else if (context instanceof Activity) {
            return with((Activity) context);
        }
        return with((Application) context);
    }

    public static BaseRequester with(Application application) {
        return getRetriever(application).get(application);
    }

    public static BaseRequester with(FragmentActivity fragmentActivity) {
        return getRetriever(fragmentActivity).get(fragmentActivity);
    }

    public static BaseRequester with(Activity activity) {
        return getRetriever(activity).get(activity);
    }

    public static BaseRequester with(Fragment fragment) {
        return with(fragment.getActivity());
    }

    public static BaseRequester with(android.app.Fragment fragment) {
        return with(fragment.getActivity());
    }

    public static BaseRequester with(View view) {
        return with(view.getContext());
    }
}
