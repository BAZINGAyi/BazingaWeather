package com.bazinga.bazingaweather.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.bazinga.bazingaweather.presenter.BasePresenter;


/**
 * Created by bazinga on 2017/4/12.
 */

public abstract class MVpBaseFragment<V,T extends BasePresenter<V>> extends Fragment {

    protected T mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //创建Presenter
        mPresenter = createPresenter();
        //内存泄漏
        //关联View
        mPresenter.attchView((V)this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        super.onDestroy();
        //接触关联
        mPresenter.detachView();
    }

    protected abstract T createPresenter();
}
