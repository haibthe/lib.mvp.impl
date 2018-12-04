package com.hb.lib.mvp.impl.lce

import com.hb.lib.mvp.impl.HBMvpPresenter
import com.hb.lib.mvp.lce.MvpLceContract

abstract class HBMvpLcePresenter<V : MvpLceContract.View<*>>
    : HBMvpPresenter<V>(), MvpLceContract.Presenter