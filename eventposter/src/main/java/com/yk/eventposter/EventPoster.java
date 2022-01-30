package com.yk.eventposter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class EventPoster {
    private static volatile EventPoster instance;

    private final List<Subscriber> subscriberList = new ArrayList<>();

    private EventPoster() {
    }

    public static EventPoster getInstance() {
        if (instance == null) {
            synchronized (EventPoster.class) {
                if (instance == null) {
                    instance = new EventPoster();
                }
            }
        }
        return instance;
    }

    public void register(Object o) {
        Subscriber subscriber = getSubscriber(o);

        if (subscriber == null) {
            return;
        }

        if (subscriberList.contains(subscriber)) {
            return;
        }

        subscriberList.add(subscriber);
    }

    public void unregister(Object o) {
        Subscriber subscriber = getSubscriber(o);

        if (subscriber == null) {
            return;
        }

        if (!subscriberList.contains(subscriber)) {
            return;
        }

        subscriberList.remove(subscriber);
    }

    public void post(Object event) {
        if (event == null) {
            return;
        }

        if (subscriberList.isEmpty()) {
            return;
        }

        for (Subscriber subscriber : subscriberList) {
            invokeSubscribe(subscriber, event);
        }
    }

    private void invokeSubscribe(Subscriber subscriber, Object event) {
        List<Method> methodList = subscriber.getMethodList();

        if (methodList.isEmpty()) {
            return;
        }

        for (Method method : methodList) {
            invokeMethod(subscriber.getO(), method, event);
        }
    }

    private void invokeMethod(Object o, Method method, Object event) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length != 1) {
            return;
        }

        if (!parameterTypes[0].equals(event.getClass())) {
            return;
        }

        Subscribe subscribe = method.getAnnotation(Subscribe.class);

        if (subscribe == null) {
            return;
        }

        switch (subscribe.threadMode()) {
            case CUR:
                invoke(o, method, event);
                break;
            case IO:
                EventThreadManager.getInstance().postIo(new Runnable() {
                    @Override
                    public void run() {
                        invoke(o, method, event);
                    }
                });
                break;
            case UI:
                EventThreadManager.getInstance().postUi(new Runnable() {
                    @Override
                    public void run() {
                        invoke(o, method, event);
                    }
                });
                break;
        }
    }

    private void invoke(Object o, Method method, Object event) {
        try {
            method.invoke(o, event);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private Subscriber getSubscriber(Object o) {
        if (o == null) {
            return null;
        }

        Subscriber subscriber = new Subscriber(o, new ArrayList<>());

        Class<?> cls = o.getClass();
        Method[] methods = cls.getMethods();

        for (Method method : methods) {
            if (!method.isAnnotationPresent(Subscribe.class)) {
                continue;
            }

            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != 1) {
                continue;
            }

            subscriber.getMethodList().add(method);
        }

        return subscriber;
    }

}
