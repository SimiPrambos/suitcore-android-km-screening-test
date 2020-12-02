package com.suitcore.base.ui.adapter.viewholder

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseItemViewHolder<in Data>(itemView: ViewBinding) : RecyclerView.ViewHolder(itemView.root){
    var baseContext: Context? = null
    abstract fun bind(data: Data?)
}
