package com.yk.mvp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public abstract class BaseMvpFragment<V extends BaseMvpView, P extends BaseMvpPresenter<V>> extends Fragment {

    private P presenter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        presenter = createPresenter();
        presenter.attachView((V) this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.detachView();
    }

    public abstract P createPresenter();
}
