package com.suitcore.feature.member

import android.view.ViewGroup
import com.suitcore.R
import com.suitcore.base.ui.adapter.BaseRecyclerAdapter
import com.suitcore.data.model.User

/**
 * Created by DODYDMW19 on 1/30/2018.
 */
class MemberAdapter : BaseRecyclerAdapter<User, MemberItemView>() {

    private var mOnActionListener: MemberItemView.OnActionListener? = null

    fun setOnActionListener(onActionListener: MemberItemView.OnActionListener) {
        mOnActionListener = onActionListener
    }

    override fun getItemResourceLayout(): Int  = R.layout.item_member

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : MemberItemView{
        val view = MemberItemView(getView(parent))
        mOnActionListener?.let { view.setOnActionListener(it) }
        return view
    }

}