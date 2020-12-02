package com.suitcore.feature.tabmenu

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import androidx.viewpager.widget.ViewPager
import com.suitcore.R
import com.suitcore.base.ui.BaseActivity
import com.suitcore.databinding.ActivityTabMenuBinding
import com.suitcore.databinding.LayoutBottomMenuBinding
import com.suitcore.feature.login.LoginActivity


/**
 * Created by @dodydmw19 on 10, September, 2020
 */

class TabMenuActivity : BaseActivity() {

    private var mPagerAdapter: MainPagerAdapter? = null
    private lateinit var tabMenuBinding : ActivityTabMenuBinding
    private lateinit var linBottomNavBinding: LayoutBottomMenuBinding

    override fun setBinding(layoutInflater: LayoutInflater) = initBinding(layoutInflater)

    private fun initBinding(layoutInflater: LayoutInflater) : ViewBinding {
        tabMenuBinding = ActivityTabMenuBinding.inflate(layoutInflater)
        return tabMenuBinding
    }

    override fun onViewReady(savedInstanceState: Bundle?) {
        initIncludeViewBinding()
        setUpPagerListener()
        actionClick()
    }

    private fun initIncludeViewBinding(){
        linBottomNavBinding = tabMenuBinding.linBottomNav
    }

    private fun setUpPagerListener() {
        mPagerAdapter = MainPagerAdapter(supportFragmentManager)
        tabMenuBinding.pager.clipToPadding = false
        tabMenuBinding.pager.offscreenPageLimit = 3

        val gap = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, resources.displayMetrics).toInt()

        tabMenuBinding.pager.pageMargin = gap
        tabMenuBinding.pager.adapter = mPagerAdapter
        tabMenuBinding.pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                setSelectedNavigation(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        tabMenuBinding.pager.setPageTransformer(false) { view, _ ->
            view.alpha = 0f
            view.visibility = View.VISIBLE

            // Start Animation for a short period of time
            view.animate().alpha(1f).duration = view.resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
        }

    }

    private fun setTitle(title: String) {
        tabMenuBinding.tvTitle.text = title
    }

    private fun setSelectedNavigation(position: Int) {
        when (position) {
            0 -> {
                setTitle("Member List")
                setSelectorColorNav(linBottomNavBinding.imgMenu1, linBottomNavBinding.tvMenu1, R.color.colorPrimary)
                setSelectorColorNav(linBottomNavBinding.imgMenu2, linBottomNavBinding.tvMenu2, R.color.gray)
                setSelectorColorNav(linBottomNavBinding.imgMenu3, linBottomNavBinding.tvMenu3, R.color.gray)
            }
            1 -> {
                setTitle("Dialog Sample")
                setSelectorColorNav(linBottomNavBinding.imgMenu1, linBottomNavBinding.tvMenu1, R.color.gray)
                setSelectorColorNav(linBottomNavBinding.imgMenu2, linBottomNavBinding.tvMenu2, R.color.colorPrimary)
                setSelectorColorNav(linBottomNavBinding.imgMenu3, linBottomNavBinding.tvMenu3, R.color.gray)
            }
            2 -> {
                setTitle("Map Sample")
                setSelectorColorNav(linBottomNavBinding.imgMenu1, linBottomNavBinding.tvMenu1, R.color.gray)
                setSelectorColorNav(linBottomNavBinding.imgMenu2, linBottomNavBinding.tvMenu2, R.color.gray)
                setSelectorColorNav(linBottomNavBinding.imgMenu3, linBottomNavBinding.tvMenu3, R.color.colorPrimary)
            }
        }
    }

    private fun setSelectorColorNav(imgMenu: ImageView, tvMenu: TextView, selectedColorRes: Int) {
        imgMenu.setBackgroundColor(ContextCompat.getColor(this, selectedColorRes))
        tvMenu.setTextColor(ContextCompat.getColor(this, selectedColorRes))
    }

    private fun actionClick() {
        linBottomNavBinding.relButton1.setOnClickListener {
            tabMenuBinding.pager.currentItem = 0
        }

        linBottomNavBinding.relButton2.setOnClickListener {
            tabMenuBinding.pager.currentItem = 1
        }

        linBottomNavBinding.relButton3.setOnClickListener {
            tabMenuBinding.pager.currentItem = 2
        }

        linBottomNavBinding.relButton4.setOnClickListener {
            goToActivity(LoginActivity::class.java, null, clearIntent = false, isFinish = false)
        }

    }
}