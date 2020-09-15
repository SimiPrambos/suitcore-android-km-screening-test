package com.suitcore.firebase.remoteconfig

import com.suitcore.base.presenter.MvpView

interface RemoteConfigView : MvpView {

    fun onUpdateAppNeeded(forceUpdate: Boolean, message: String?)

    fun onUpdateBaseUrlNeeded(type: String?, url: String?)

}