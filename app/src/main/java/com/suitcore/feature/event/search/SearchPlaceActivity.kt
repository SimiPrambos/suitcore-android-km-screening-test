package com.suitcore.feature.event.search

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.google.android.gms.location.places.Place
import com.mapbox.api.geocoding.v5.models.CarmenFeature
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.Symbol
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions
import com.suitcore.R
import com.suitcore.base.ui.BaseActivity
import com.suitcore.databinding.ActivitySearchPlaceBinding
import com.suitcore.helper.CommonConstant
import com.suitcore.helper.CommonUtils

/**
 * Created by dodydmw19 on 1/14/19.
 */

class SearchPlaceActivity : BaseActivity(), SearchPlaceView {

    private var placePresenter: SearchPlacePresenter? = null

    private var arrayPlace: ArrayList<Place> = ArrayList()
    private var arrayPlaceOfString: MutableList<String> = mutableListOf()

    private var mapBox: MapboxMap? = null
    private var symbolManager: SymbolManager? = null
    private var symbol: Symbol? = null
    private var marker = "marker"

    private var currentLocation: LatLng? = null
    private var isFirstTime = true

    private lateinit var searchPlaceBinding: ActivitySearchPlaceBinding

    override fun setBinding(layoutInflater: LayoutInflater) = initBinding(layoutInflater)

    private fun initBinding(layoutInflater: LayoutInflater) : ViewBinding {
        searchPlaceBinding = ActivitySearchPlaceBinding.inflate(layoutInflater)
        return searchPlaceBinding
    }

    override fun onViewReady(savedInstanceState: Bundle?) {
        setupToolbar(searchPlaceBinding.mToolbar, true)
        setupPresenter()
        setupMap()
        actionClick()
    }

    private fun setupPresenter() {
        placePresenter = SearchPlacePresenter(this)
        placePresenter?.attachView(this)
    }

    private fun setupMap() {
        searchPlaceBinding.mapEvent.getMapAsync { mapBoxMap ->
            this.mapBox = mapBoxMap
            mapBoxMap.setStyle(Style.MAPBOX_STREETS) { style ->
                style.addImage(marker, BitmapFactory.decodeResource(this.resources, R.drawable.ic_pick_location))
                symbolManager = SymbolManager(searchPlaceBinding.mapEvent, mapBoxMap, style, null, null)
                symbolManager?.iconAllowOverlap = true

                mapBoxMap.uiSettings.isCompassEnabled = false

                mapBoxMap.addOnMapClickListener { this.addSymbol(it) }
            }
        }
    }

    private fun addSymbol(point: LatLng): Boolean {
        setCurrentLocation(point.latitude, point.longitude)

        if (isFirstTime) {
            val symbolOptions = SymbolOptions()
                    .withLatLng(LatLng(point.latitude, point.longitude))
                    .withIconImage(marker)

            symbol = symbolManager?.create(symbolOptions)
            isFirstTime = false
        } else {
            symbol?.latLng = LatLng(point.latitude, point.longitude)
            symbolManager?.update(symbol)
        }

        CommonUtils.setCamera(point.latitude, point.longitude, mapBox)
        placePresenter?.reverseGeoCoder(point.latitude, point.longitude)

        return true
    }

    private fun setCurrentLocation(latitude: Double, longitude: Double) {
        currentLocation = LatLng()
        currentLocation?.latitude = latitude
        currentLocation?.longitude = longitude
        searchPlaceBinding.relSubmit.setBackgroundResource(R.drawable.bg_button_rounded_green)
    }

    override fun onPlaceReceive(places: List<Place>?) {
        if (places != null && places.isNotEmpty()) {
            places.forEach { p ->
                arrayPlaceOfString.add(p.name.toString())
                arrayPlace.add(p)
            }
        }
    }

    override fun onAddressReceive(address: String?) {
        address?.let { it ->
            searchPlaceBinding.tvAddress.text = it
        }
    }

    override fun onPlaceNotFound() {
        showToast("Place not found")
    }

    override fun onFailedLoadPlaces(message: String?) {
        message?.let { it ->
            showToast(it)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
                if(data != null){
                    val feature: CarmenFeature? = PlaceAutocomplete.getPlace(data)
                    feature?.let{
                        addSymbol(LatLng((it.geometry() as Point).latitude(), (it.geometry() as Point).longitude()))
                        searchPlaceBinding.tvAddress.text = it.text().toString()
                    }
                }
        }
    }

    private fun actionClick() {
        searchPlaceBinding.relSearch.setOnClickListener{
            val intent = PlaceAutocomplete.IntentBuilder()
                    .accessToken(CommonConstant.MAP_BOX_TOKEN)
                    .placeOptions(PlaceOptions.builder()
                            .backgroundColor(Color.parseColor("#EEEEEE"))
                            .toolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
                            .limit(10)
                            .build(PlaceOptions.MODE_CARDS))
                    .build(this)
            startActivityForResult(intent, 101)
        }

        searchPlaceBinding.tvClear.setOnClickListener {
           // actPlaceName.text.clear()
        }

        searchPlaceBinding.relSubmit.setOnClickListener {
//            if (currentLocation != null && currentLocation?.latitude != null && currentLocation?.longitude != null) {
//
//            }
        }
    }

}