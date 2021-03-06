package com.mm.luna.ui.violet;

import android.annotation.SuppressLint;

import com.mm.luna.api.Api;
import com.mm.luna.base.BasePresenterImpl;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ZMM on 2018/2/6.
 */

public class VioletPresenter extends BasePresenterImpl<VioletContract.View> implements VioletContract.Presenter {

    VioletPresenter(VioletContract.View view) {
        super(view);
    }
    @SuppressLint("CheckResult")
    @Override
    public void getVideoList() {
        Api.getInstance().getComicsList()
                .subscribeOn(Schedulers.io())
                .map(entity -> entity)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(entity -> {
                    view.onFinish();
                    view.setData(entity);
                }, throwable -> view.onError());
    }
}
