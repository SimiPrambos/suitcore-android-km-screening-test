package com.suitcore.feature.event.search

import com.google.android.gms.location.places.Place
import com.suitcore.base.presenter.MvpView

/**
 * Created by dodydmw19 on 1/14/19.
 */

interface SearchPlaceView : MvpView {

    fun onPlaceReceive(places: List<Place>?)

    fun onAddressReceive(address: String?)

    fun onPlaceNotFound()

    fun onFailedLoadPlaces(message: String?)

}