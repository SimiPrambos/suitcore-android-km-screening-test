package com.suitcore.feature.tabmenu

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.suitcore.base.ui.BaseFragment
import com.suitcore.feature.event.EventFragment
import com.suitcore.feature.fragmentsample.SampleFragment
import com.suitcore.feature.member.MemberFragment

class MainPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var memberFragment: BaseFragment? = null
    private var dialogFragment: BaseFragment? = null
    private var mapFragment: BaseFragment? = null

    override fun getItem(position: Int): BaseFragment = when (position) {
        0 -> generateMemberFragment()
        1 -> generateDialogFragment()
        2 -> generateMapFragment()
        else -> generateMemberFragment()
    }
    override fun getCount(): Int = 3

    private fun generateMemberFragment(): BaseFragment = if (memberFragment == null) {
        MemberFragment.newInstance()
    }else{
        memberFragment!!
    }

    private fun generateDialogFragment(): BaseFragment = if (dialogFragment == null) {
        SampleFragment.newInstance()
    }else{
        dialogFragment!!
    }

    private fun generateMapFragment(): BaseFragment = if (mapFragment == null) {
        EventFragment.newInstance()
    }else{
        mapFragment!!
    }

}