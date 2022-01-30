package com.yk.rxsimpler;

/**
 * 可订阅的对象
 */
public class Observable<T> {
    /**
     * 订阅回调接口
     */
    private final OnSubscribe<T> onSubscribe;

    /**
     * 构造函数
     *
     * @param onSubscribe 订阅回调接口
     */
    private Observable(OnSubscribe<T> onSubscribe) {
        this.onSubscribe = onSubscribe;
    }

    /**
     * 订阅
     *
     * @param subscriber 订阅者
     */
    public void subscribe(Subscriber<T> subscriber) {
        onSubscribe.call(subscriber);
    }

    /**
     * 创建可订阅的对象
     *
     * @param onSubscribe 订阅回调事件
     * @return 可订阅的对象
     */
    public static <T> Observable<T> create(OnSubscribe<T> onSubscribe) {
        return new Observable<>(onSubscribe);
    }

    /**
     * 创建可订阅的对象
     *
     * @return 可订阅的对象
     */
    public static <T> Observable<T> from(T t) {
        return new Observable<>(new OnSubscribe<T>() {
            @Override
            public void call(Subscriber<T> subscriber) {
                subscriber.onNext(t);
                subscriber.onComplete();
            }
        });
    }

    /**
     * 创建可订阅的对象
     *
     * @return 可订阅的对象
     */
    public static <T> Observable<T> just(T t) {
        return new Observable<>(new OnSubscribe<T>() {
            @Override
            public void call(Subscriber<T> subscriber) {
                subscriber.onNext(t);
            }
        });
    }

    /**
     * 创建可订阅的对象
     *
     * @param onCallable 订阅回调事件
     * @return 可订阅的对象
     */
    public static <T> Observable<T> fromCallable(OnCallable<T> onCallable) {
        return new Observable<>(new OnSubscribe<T>() {
            @Override
            public void call(Subscriber<T> subscriber) {
                T t = null;
                Exception exception = null;
                try {
                    t = onCallable.call();
                } catch (Exception e) {
                    exception = e;
                }
                if (exception != null) {
                    subscriber.onError(exception);
                } else {
                    subscriber.onNext(t);
                    subscriber.onComplete();
                }
            }
        });
    }

    /**
     * 参数变换
     *
     * @param function 变换接口
     * @return 新的可订阅对象
     */
    public <R> Observable<R> map(Function1<T, R> function) {
        return new Observable<>(new OnSubscribe<R>() {
            @Override
            public void call(Subscriber<R> subscriber) {
                subscribe(new Subscriber<T>() {
                    @Override
                    public void onNext(T t) {
                        R r = function.call(t);
                        subscriber.onNext(r);
                    }

                    @Override
                    public void onComplete() {
                        subscriber.onComplete();
                    }

                    @Override
                    public void onError(Exception e) {
                        subscriber.onError(e);
                    }
                });
            }
        });
    }

    /**
     * 可订阅对象变换
     *
     * @param function 变换接口
     * @return 新的可订阅对象
     */
    public <R> Observable<R> flatMap(Function1<T, Observable<R>> function) {
        return new Observable<>(new OnSubscribe<R>() {
            @Override
            public void call(Subscriber<R> subscriber) {
                subscribe(new Subscriber<T>() {
                    @Override
                    public void onNext(T t) {
                        Observable<R> r = function.call(t);
                        r.subscribe(subscriber);
                    }

                    @Override
                    public void onComplete() {
                        subscriber.onComplete();
                    }

                    @Override
                    public void onError(Exception e) {
                        subscriber.onError(e);
                    }
                });
            }
        });
    }

    /**
     * 切换子线程
     *
     * @return 可订阅的对象
     */
    public Observable<T> subscribeOnIo() {
        return new Observable<>(new OnSubscribe<T>() {
            @Override
            public void call(Subscriber<T> subscriber) {
                RxThreadManager.getInstance().postIo(new Runnable() {
                    @Override
                    public void run() {
                        subscribe(new Subscriber<T>() {
                            @Override
                            public void onNext(T t) {
                                subscriber.onNext(t);
                            }

                            @Override
                            public void onComplete() {
                                subscriber.onComplete();
                            }

                            @Override
                            public void onError(Exception e) {
                                subscriber.onError(e);
                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * 切换至主线程
     *
     * @return 可订阅的对象
     */
    public Observable<T> observeOnUi() {
        return new Observable<>(new OnSubscribe<T>() {
            @Override
            public void call(Subscriber<T> subscriber) {
                subscribe(new Subscriber<T>() {
                    @Override
                    public void onNext(T t) {
                        RxThreadManager.getInstance().postUi(new Runnable() {
                            @Override
                            public void run() {
                                subscriber.onNext(t);
                            }
                        });
                    }

                    @Override
                    public void onComplete() {
                        RxThreadManager.getInstance().postUi(new Runnable() {
                            @Override
                            public void run() {
                                subscriber.onComplete();
                            }
                        });
                    }

                    @Override
                    public void onError(Exception e) {
                        subscriber.onError(e);
                    }
                });
            }
        });
    }

    /**
     * 订阅回调接口
     */
    public interface OnSubscribe<T> {
        /**
         * 回调方法
         *
         * @param subscriber 订阅者
         */
        void call(Subscriber<T> subscriber);
    }

    /**
     * 转换功能接口
     */
    public interface Function1<T, R> {
        R call(T t);
    }

    /**
     * 订阅回调接口（可处理异常）
     */
    public interface OnCallable<T> {
        T call();
    }
}
