package com.yk.eventposter;

import androidx.annotation.Nullable;

import java.lang.reflect.Method;
import java.util.List;

public class Subscriber {
    private Object o;
    private List<Method> methodList;

    public Subscriber(Object o, List<Method> methodList) {
        this.o = o;
        this.methodList = methodList;
    }

    public Object getO() {
        return o;
    }

    public void setO(Object o) {
        this.o = o;
    }

    public List<Method> getMethodList() {
        return methodList;
    }

    public void setMethodList(List<Method> methodList) {
        this.methodList = methodList;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Subscriber) {
            Subscriber subscriber = (Subscriber) obj;
            return subscriber.o.equals(this.o);
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "Subscriber{" +
                "o=" + o +
                ", methodList=" + methodList +
                '}';
    }
}
