package com.hb.lib.mvp.impl

import com.hb.lib.mvp.base.MvpBasePresenter
import com.hb.lib.mvp.base.MvpContract
import com.hb.lib.utils.RxBus
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


abstract class HBMvpPresenter<V : MvpContract.View> : MvpBasePresenter<V>() {


    @Inject
    lateinit var rxBus: RxBus

    @Inject
    lateinit var disposable: CompositeDisposable


}