package com.hb.lib.mvp.impl

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import com.hb.lib.mvp.base.MvpContract
import com.hb.lib.mvp.base.MvpFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


abstract class HBMvpFragment<P : MvpContract.Presenter> : MvpFragment<P>() {


    private val disposable = CompositeDisposable()

    open fun busEvent(obj: Any) {
    }

    override fun createPresenter(): P = mPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        ButterKnife.bind(this, view!!)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disposable.clear()
        val rxBus = (mPresenter as HBMvpPresenter<*>).rxBus
        disposable.addAll(
                rxBus.toObservable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::busEvent)
        )
    }


}