package com.suitcore.feature.event

import android.os.Bundle
import com.suitcore.R
import com.suitcore.base.ui.BaseFragment
import com.suitcore.data.model.Event
import kotlinx.android.synthetic.main.item_event.*


class ImageFragment : BaseFragment() {

    var data: Event? = null

    companion object {
        fun newInstance(data: Event): BaseFragment? {
            val fragment =  ImageFragment()
            fragment.data = data
            return fragment
        }
    }

    override val resourceLayout: Int = R.layout.item_event

    override fun onViewReady(savedInstanceState: Bundle?) {
        imgItemEvent.setImageURI(data?.imgUrl)
        tvItemEventName.text = data?.name
    }

}