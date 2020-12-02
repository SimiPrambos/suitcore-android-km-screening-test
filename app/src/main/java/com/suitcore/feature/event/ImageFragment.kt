package com.suitcore.feature.event

import android.graphics.drawable.Animatable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.viewbinding.ViewBinding
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.drawee.controller.ControllerListener
import com.facebook.drawee.interfaces.DraweeController
import com.facebook.imagepipeline.image.ImageInfo
import com.suitcore.base.ui.BaseFragment
import com.suitcore.data.model.Event
import com.suitcore.databinding.ItemEventBinding


class ImageFragment : BaseFragment() {

    var data: Event? = null
    private lateinit var imageBinding: ItemEventBinding

    companion object {
        fun newInstance(data: Event): BaseFragment {
            val fragment =  ImageFragment()
            fragment.data = data
            return fragment
        }
    }

    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): ViewBinding {
        imageBinding = ItemEventBinding.inflate(inflater, container, false)
        return imageBinding
    }

    override fun onViewReady(savedInstanceState: Bundle?) {
        imageBinding.imgItemEvent.setImageURI(data?.imgUrl)
        imageBinding.tvItemEventName.text = data?.name
    }

}