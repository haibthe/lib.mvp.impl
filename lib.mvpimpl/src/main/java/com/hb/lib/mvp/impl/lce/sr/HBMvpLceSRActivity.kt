package com.hb.lib.mvp.impl.lce.sr

import android.os.Bundle
import butterknife.ButterKnife
import com.hb.lib.app.OnActivityCurrentListener
import com.hb.lib.mvp.impl.HBMvpPresenter
import com.hb.lib.mvp.lce.MvpLceContract
import com.hb.lib.mvp.lce.sr.MvpLceSRActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

abstract class HBMvpLceSRActivity<M, P : MvpLceContract.Presenter> : MvpLceSRActivity<M, P>() {

    private val disposable = CompositeDisposable()

    open fun busEvent(obj: Any) {
    }

    override fun createPresenter(): P = mPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ButterKnife.bind(this)

        disposable.clear()
        val rxBus = (mPresenter as HBMvpPresenter<*>).rxBus
        disposable.addAll(
                rxBus.toObservable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::busEvent)
        )


    }

    override fun onResume() {
        super.onResume()
        val app = application
        if (app is OnActivityCurrentListener) {
            app.currentActivity = this
        }
    }
    override fun onDestroy() {
        disposable.clear()
        super.onDestroy()
    }

}