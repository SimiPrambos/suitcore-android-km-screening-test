package com.suitcore.feature.member

import android.annotation.SuppressLint
import android.view.View
import com.suitcore.base.ui.adapter.viewholder.BaseItemViewHolder
import com.suitcore.data.model.User
import kotlinx.android.synthetic.main.item_member.view.*

/**
 * Created by DODYDMW19 on 1/30/2018.
 */
class MemberItemView(itemView: View) : BaseItemViewHolder<User>(itemView) {

    private var mActionListener: OnActionListener? = null
    private var user: User? = null

    @SuppressLint("SetTextI18n")
    override fun bind(data: User?) {
        data.let {
            // for get context = itemView.context

            this.user = data
            itemView.imgMember.setImageURI(data?.avatar)
            itemView.txtMemberName.text = data?.firstName + " " + data?.lastName
            itemView.button.setOnClickListener {
                mActionListener?.onClicked(this)
            }

        }
    }

    fun getData(): User {
        return user!!
    }

    fun setOnActionListener(listener: OnActionListener) {
        mActionListener = listener
    }

    interface OnActionListener {
        fun onClicked(view: MemberItemView?)
    }
}