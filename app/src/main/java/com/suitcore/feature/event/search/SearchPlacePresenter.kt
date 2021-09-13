package com.suitcore.feature.event.search

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.location.places.Place
import com.suitcore.BaseApplication
import com.suitcore.R
import com.suitcore.base.presenter.BasePresenter
import com.suitcore.data.model.ErrorCodeHelper
import com.suitcore.data.remote.services.APIService
import com.suitcore.helper.CommonConstant
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


/**
 * Created by dodydmw19 on 1/14/19.
 */

class SearchPlacePresenter(var context: Context?) : BasePresenter<SearchPlaceView> {

    @Inject
    lateinit var apiService: APIService
    private var mvpView: SearchPlaceView? = null
    private var mCompositeDisposable: CompositeDisposable? = CompositeDisposable()

    init {
        BaseApplication.applicationComponent.inject(this)
    }

    fun searchPlaces(keyword: String?) {

        val url: String = "https://api.mapbox.com/geocoding/v5/mapbox.places/" + keyword + ".json" +
                "?access_token=" + CommonConstant.MAP_BOX_TOKEN

        mCompositeDisposable?.add(
                apiService.searchPlace(url)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe {
                            mvpView?.showDialogLoading(true, context?.getString(R.string.txt_loading))
                        }
                        .doOnComplete {
                            mvpView?.hideLoading()
                        }
                        .subscribe({ data ->
                            if (data != null) {
                                if (data.arrayData?.isNotEmpty()!!) {
                                    mvpView?.onPlaceReceive(data.arrayData!!)
                                } else {
                                    mvpView?.onPlaceNotFound()
                                }
                            } else {
                                mvpView?.onFailedLoadPlaces(context?.getString(R.string.txt_error_global))
                            }
                        }, {
                            mvpView?.onFailedLoadPlaces(ErrorCodeHelper.getErrorMessage(context, it))
                        })
        )
    }

    @SuppressLint("CheckResult")
    fun reverseGeoCoder(latitude: Double, longitude: Double) {
        val url: String? = "https://api.mapbox.com/geocoding/v5/mapbox.places/" + longitude +","+ latitude + ".json" +
                "?access_token=" + CommonConstant.MAP_BOX_TOKEN

        mCompositeDisposable?.add(
                apiService.searchPlace(url)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe {
                            mvpView?.showDialogLoading(true, context?.getString(R.string.txt_loading))
                        }
                        .doOnComplete {
                            mvpView?.hideLoading()
                        }
                        .subscribe({ data ->
                            if (data != null) {
                                if (data.arrayData?.isNotEmpty()!!) {
                                    val address: Place? = data.arrayData?.get(0)
//                                    MapPoint().also { mapPoint ->
//                                        mapPoint.identifier = "${latitude}, $longitude"
//                                        mapPoint.name = address?.keywordSearch
//                                        mapPoint.fullAddress = address?.name
//                                        mvpView?.onAddressReceive(LatLng(latitude, longitude), mapPoint)
//                                    }
                                    mvpView?.onAddressReceive(address?.name as String?)

                                } else {
                                    mvpView?.onPlaceNotFound()
                                }
                            } else {
                                mvpView?.onPlaceNotFound()
                            }
                        }, {
                            //mvpView?.hideLoading()
                            mvpView?.onPlaceNotFound()
                            mvpView?.hideLoading()
                        })
        )

    }

    override fun onDestroy() {
        detachView()
    }

    override fun attachView(view: SearchPlaceView) {
        mvpView = view
        // Initialize this presenter as a lifecycle-aware when a view is a lifecycle owner.
        if (mvpView is LifecycleOwner) {
            (mvpView as LifecycleOwner).lifecycle.addObserver(this)
        }
    }

    override fun detachView() {
        mvpView = null
        mCompositeDisposable.let { mCompositeDisposable?.clear() }
    }
}