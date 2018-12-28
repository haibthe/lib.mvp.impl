package com.hb.lib.mvp.impl

import android.os.Bundle
import android.view.View
import butterknife.ButterKnife
import com.google.android.material.snackbar.Snackbar
import com.hb.lib.app.OnActivityCurrentListener
import com.hb.lib.mvp.base.MvpActivity
import com.hb.lib.mvp.base.MvpContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


abstract class HBMvpActivity<P : MvpContract.Presenter> : MvpActivity<P>() {

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

    fun showSnackBar(msg: String, listener: View.OnClickListener) {
        val popup = Snackbar.make(getView(), msg, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.button_close, listener)
        popup.show()
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