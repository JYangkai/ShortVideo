package com.yk.mvp;

import java.lang.ref.WeakReference;

public class BaseMvpPresenter<V extends BaseMvpView> {

    private WeakReference<V> viewRef;

    public V getView() {
        if (viewRef == null) {
            return null;
        }
        return viewRef.get();
    }

    public void attachView(V view) {
        viewRef = new WeakReference<>(view);
    }

    public void detachView() {
        viewRef.clear();
        viewRef = null;
    }
}
