package com.suitcore.feature.member

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.suitcore.base.ui.adapter.BaseRecyclerAdapter
import com.suitcore.data.model.User
import com.suitcore.databinding.ItemMemberBinding

/**
 * Created by DODYDMW19 on 1/30/2018.
 */

class MemberAdapter(var context: Context) : BaseRecyclerAdapter<User, MemberItemView>() {

    private lateinit var itemMemberBinding: ItemMemberBinding
    private var mOnActionListener: MemberItemView.OnActionListener? = null

    fun setOnActionListener(onActionListener: MemberItemView.OnActionListener) {
        mOnActionListener = onActionListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : MemberItemView{
        itemMemberBinding = ItemMemberBinding.inflate(LayoutInflater.from(context) , parent,false)
        val view = MemberItemView(itemMemberBinding)
        mOnActionListener?.let { view.setOnActionListener(it) }
        return view
    }

}