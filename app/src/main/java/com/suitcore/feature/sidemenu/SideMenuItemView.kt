package com.suitcore.feature.sidemenu

import android.view.View
import android.widget.TextView
import com.suitcore.base.ui.adapter.viewholder.BaseItemViewHolder
import com.suitcore.data.model.SideMenu
import kotlinx.android.synthetic.main.item_side_menu.view.*

/**
 * Created by dodydmw19 on 1/3/19.
 */

class SideMenuItemView(itemView: View) : BaseItemViewHolder<SideMenu>(itemView) {

    private var mActionListener: OnActionListener? = null
    private var sideMenu: SideMenu? = null

    override fun bind(data: SideMenu?) {
        data?.let {
            sideMenu = data
            itemView.textViewMenuTitle.text = data.label

            itemView.linearLayoutBackground?.setOnClickListener {
                if (mActionListener != null) {
                    mActionListener?.onClicked(this, adapterPosition - 1)
                }
            }
        }
    }

    fun getTitleView(): TextView {
        return itemView.textViewMenuTitle
    }

    fun getData() : SideMenu?{
        return sideMenu
    }

    fun setOnActionListener(listener: OnActionListener) {
        mActionListener = listener
    }

    interface OnActionListener {
        fun onClicked(view: SideMenuItemView, position: Int)
    }

}