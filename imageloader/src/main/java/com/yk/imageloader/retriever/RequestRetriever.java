package com.yk.imageloader.retriever;

import android.app.Activity;
import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.yk.imageloader.fragment.ARequestFragment;
import com.yk.imageloader.fragment.FARequestFragment;
import com.yk.imageloader.requester.ApplicationRequester;
import com.yk.imageloader.requester.BaseRequester;
import com.yk.imageloader.requester.Requester;

import java.util.HashMap;
import java.util.Map;

public class RequestRetriever implements Handler.Callback {
    private static final int REMOVE_FA_FRAGMENT = 0;
    private static final int REMOVE_A_FRAGMENT = 1;

    private final Map<FragmentManager, FARequestFragment> faRequestFragmentMap;
    private final Map<android.app.FragmentManager, ARequestFragment> aRequestFragmentMap;

    private final Handler uiHandler;

    private ApplicationRequester applicationRequester;

    public RequestRetriever() {
        faRequestFragmentMap = new HashMap<>();
        aRequestFragmentMap = new HashMap<>();

        uiHandler = new Handler(Looper.getMainLooper(), this);
    }

    public BaseRequester get(Application application) {
        if (applicationRequester == null) {
            applicationRequester = new ApplicationRequester();
        }
        return applicationRequester;
    }

    public BaseRequester get(FragmentActivity fragmentActivity) {
        FARequestFragment fragment = getFARequestFragment(fragmentActivity.getSupportFragmentManager());

        BaseRequester requester = fragment.getRequester();
        if (requester == null) {
            requester = new Requester();
            fragment.setRequester(requester);
        }

        return requester;
    }

    public BaseRequester get(Activity activity) {
        ARequestFragment fragment = getARequestFragment(activity.getFragmentManager());

        BaseRequester requester = fragment.getRequester();
        if (requester == null) {
            requester = new Requester();
            fragment.setRequester(requester);
        }

        return requester;
    }

    private FARequestFragment getFARequestFragment(FragmentManager fm) {
        FARequestFragment fragment = (FARequestFragment) fm.findFragmentByTag(FARequestFragment.TAG);
        if (fragment == null) {
            fragment = faRequestFragmentMap.get(fm);
            if (fragment == null) {
                fragment = FARequestFragment.newInstance();
                faRequestFragmentMap.put(fm, fragment);
                fm.beginTransaction().add(fragment, FARequestFragment.TAG).commitAllowingStateLoss();
                uiHandler.obtainMessage(REMOVE_FA_FRAGMENT, fm).sendToTarget();
            }
        }
        return fragment;
    }

    private ARequestFragment getARequestFragment(android.app.FragmentManager fm) {
        ARequestFragment fragment = (ARequestFragment) fm.findFragmentByTag(ARequestFragment.TAG);
        if (fragment == null) {
            fragment = aRequestFragmentMap.get(fm);
            if (fragment == null) {
                fragment = ARequestFragment.newInstance();
                aRequestFragmentMap.put(fm, fragment);
                fm.beginTransaction().add(fragment, ARequestFragment.TAG).commitAllowingStateLoss();
                uiHandler.obtainMessage(REMOVE_A_FRAGMENT, fm).sendToTarget();
            }
        }
        return fragment;
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        boolean handle = true;
        switch (msg.what) {
            case REMOVE_FA_FRAGMENT:
                FragmentManager fm1 = (FragmentManager) msg.obj;
                faRequestFragmentMap.remove(fm1);
                break;
            case REMOVE_A_FRAGMENT:
                android.app.FragmentManager fm2 = (android.app.FragmentManager) msg.obj;
                aRequestFragmentMap.remove(fm2);
                break;
            default:
                handle = false;
                break;
        }
        return handle;
    }
}
