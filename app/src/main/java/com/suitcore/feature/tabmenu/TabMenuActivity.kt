package com.suitcore.feature.tabmenu

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.suitcore.R
import com.suitcore.base.ui.BaseActivity
import com.suitcore.feature.login.LoginActivity
import kotlinx.android.synthetic.main.activity_tab_menu.*
import kotlinx.android.synthetic.main.layout_bottom_menu.*


/**
 * Created by @dodydmw19 on 10, September, 2020
 */

class TabMenuActivity : BaseActivity() {

    private var mPagerAdapter: MainPagerAdapter? = null

    override val resourceLayout: Int = R.layout.activity_tab_menu

    override fun onViewReady(savedInstanceState: Bundle?) {
        setUpPagerListener()
        actionClick()
    }

    private fun setUpPagerListener() {
        mPagerAdapter = MainPagerAdapter(supportFragmentManager)
        pager.clipToPadding = false
        pager.offscreenPageLimit = 3

        val gap = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, resources.displayMetrics).toInt()

        pager.pageMargin = gap
        pager.adapter = mPagerAdapter
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                setSelectedNavigation(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        pager.setPageTransformer(false) { view, _ ->
            view.alpha = 0f
            view.visibility = View.VISIBLE

            // Start Animation for a short period of time
            view.animate().alpha(1f).duration = view.resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
        }

    }

    private fun setTitle(title: String) {
        tvTitle.text = title
    }

    private fun setSelectedNavigation(position: Int) {
        when (position) {
            0 -> {
                setTitle("Member List")
                setSelectorColorNav(imgMenu1, tvMenu1, R.color.colorPrimary)
                setSelectorColorNav(imgMenu2, tvMenu2, R.color.gray)
                setSelectorColorNav(imgMenu3, tvMenu3, R.color.gray)
            }
            1 -> {
                setTitle("Dialog Sample")
                setSelectorColorNav(imgMenu1, tvMenu1, R.color.gray)
                setSelectorColorNav(imgMenu2, tvMenu2, R.color.colorPrimary)
                setSelectorColorNav(imgMenu3, tvMenu3, R.color.gray)
            }
            2 -> {
                setTitle("Map Sample")
                setSelectorColorNav(imgMenu1, tvMenu1, R.color.gray)
                setSelectorColorNav(imgMenu2, tvMenu2, R.color.gray)
                setSelectorColorNav(imgMenu3, tvMenu3, R.color.colorPrimary)
            }
        }
    }

    private fun setSelectorColorNav(imgMenu: ImageView, tvMenu: TextView, selectedColorRes: Int) {
        imgMenu.setBackgroundColor(ContextCompat.getColor(this, selectedColorRes))
        tvMenu.setTextColor(ContextCompat.getColor(this, selectedColorRes))
    }

    private fun actionClick() {
        relButton1.setOnClickListener {
            pager.currentItem = 0
        }

        relButton2.setOnClickListener {
            pager.currentItem = 1
        }

        relButton3.setOnClickListener {
            pager.currentItem = 2
        }

        relButton4.setOnClickListener {
            goToActivity(LoginActivity::class.java, null, clearIntent = false, isFinish = false)
        }

    }
}