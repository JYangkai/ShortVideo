package com.yk.imageloader.fragment;

import android.app.Fragment;

import com.yk.imageloader.requester.BaseRequester;

public class ARequestFragment extends Fragment {
    public static final String TAG = "ARequestFragment";

    public static ARequestFragment newInstance() {
        return new ARequestFragment();
    }

    private BaseRequester requester;

    public void setRequester(BaseRequester requester) {
        this.requester = requester;
    }

    public BaseRequester getRequester() {
        return requester;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (requester != null) {
            requester.onStart();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (requester != null) {
            requester.onStop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (requester != null) {
            requester.onDestroy();
        }
    }
}
