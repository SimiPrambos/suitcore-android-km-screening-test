package com.suitcore.base.ui.adapter.viewholder

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseItemViewHolder<in Data>(itemView: View) : RecyclerView.ViewHolder(itemView){
    var baseContext: Context? = null
    abstract fun bind(data: Data?)
}
