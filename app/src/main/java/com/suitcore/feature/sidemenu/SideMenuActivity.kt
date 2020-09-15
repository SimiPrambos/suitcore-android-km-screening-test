package com.suitcore.feature.sidemenu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.suitcore.R
import com.suitcore.base.ui.BaseActivity
import com.suitcore.base.ui.recyclerview.BaseRecyclerView
import com.suitcore.data.model.SideMenu
import com.suitcore.feature.event.EventFragment
import com.suitcore.feature.fragmentsample.SampleFragment
import com.suitcore.feature.login.LoginActivity
import com.suitcore.feature.member.MemberFragment
import com.suitcore.helper.CommonConstant
import kotlinx.android.synthetic.main.activity_side_menu.*
import kotlinx.android.synthetic.main.layout_base_shimmer.*
import kotlinx.android.synthetic.main.layout_side_menu.*


/**
 * Created by @dodydmw19 on 10, September, 2020
 */

class SideMenuActivity : BaseActivity(), SideMenuView, SideMenuItemView.OnActionListener{

    private var sideMenuPresenter: SideMenuPresenter? = null
    private var sideMenuAdapter: SideMenuAdapter? = SideMenuAdapter(this)
    private var isDrawerOpen = false
    private var finalFragment: Fragment? = null
    private var arraySideMenu: List<SideMenu>? = emptyList()

    override val resourceLayout: Int = R.layout.activity_side_menu

    companion object {
        fun createIntent(context: Context?): Intent {
            return Intent(context, SideMenuActivity::class.java)
        }
    }

    override fun onViewReady(savedInstanceState: Bundle?) {
        setupProgressView()
        setupEmptyView()
        setupErrorView()
        setupPresenter()
        setUpSideBar()
        actionClick()
    }

    private fun setupPresenter() {
        sideMenuPresenter = SideMenuPresenter(this)
        sideMenuPresenter?.attachView(this)
        sideMenuPresenter?.getMenuFromFile()
    }

    private fun setUpSideBar() {
        val drawerToggle = object : ActionBarDrawerToggle(this, drawerLayout, mToolbar, 0, 0) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                isDrawerOpen = true
            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                isDrawerOpen = false
                if (finalFragment != null) {
                    setContentFragment(finalFragment)
                }
            }
        }

        drawerToggle.drawerArrowDrawable.color = ContextCompat.getColor(this, R.color.black)
        drawerToggle.syncState()
        drawerLayout?.addDrawerListener(drawerToggle)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        sideMenuAdapter?.selectedItem = 0
        finalFragment = MemberFragment()
        tvTitle.text = getString(R.string.txt_toolbar_home)
        setContentFragment(finalFragment)
    }

    private fun setupList(sideMenus: List<SideMenu>) {
        rvSideMenu.apply {
            setUpAsList()
            setAdapter(sideMenuAdapter)
            setPullToRefreshEnable(false)
            setLoadingMoreEnabled(false)
        }
        sideMenuAdapter?.setOnActionListener(this)
        sideMenuAdapter?.add(sideMenus)
        rvSideMenu.stopShimmer()
        rvSideMenu.showRecycler()
        finishLoad(rvSideMenu)
    }

    private fun setupProgressView() {
        R.layout.layout_shimmer_member.apply {
            viewStub.layoutResource = this
        }

        viewStub.inflate()
    }

    private fun setupEmptyView() {
        rvSideMenu.setImageEmptyView(R.drawable.empty_state)
        rvSideMenu.setTitleEmptyView(getString(R.string.txt_empty_member))
        rvSideMenu.setContentEmptyView(getString(R.string.txt_empty_member_content))
        rvSideMenu.setEmptyButtonListener(object : BaseRecyclerView.ReloadListener {

            override fun onClick(v: View?) {
            }

        })
    }

    private fun setupErrorView() {
        rvSideMenu.setImageErrorView(R.drawable.empty_state)
        rvSideMenu.setTitleErrorView(getString(R.string.txt_error_no_internet_connection))
        rvSideMenu.setContentErrorView(getString(R.string.txt_error_connection))
        rvSideMenu.setErrorButtonListener(object : BaseRecyclerView.ReloadListener {

            override fun onClick(v: View?) {
            }

        })
    }

    private fun setContentFragment(fragment: Fragment?) {
        finalFragment = fragment
        finalFragment?.let {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, it)
                    .commitAllowingStateLoss()
        }
    }

    private fun closeDrawers() {
        drawerLayout.closeDrawers()
    }

    override fun onSideMenuLoaded(sideMenus: List<SideMenu>?) {
        sideMenus?.let {
            arraySideMenu = sideMenus
            setupList(sideMenus)
            sideMenuAdapter?.selectedItem = 0
        }
    }

    override fun onFailedLoadSideMenu(message: String?) {
        message?.let {
            showToast(it)
        }
    }

    override fun onClicked(view: SideMenuItemView, position: Int) {
        view.getData().let { data ->

            tvTitle.text = data?.label
            finalFragment = null
            sideMenuAdapter?.selectedItem = position
            finalFragment = when (view.getData()?.url) {
                CommonConstant.MENU_HOME -> {
                    MemberFragment.newInstance()
                }
                CommonConstant.MENU_FRAGMENT_1 -> {
                    EventFragment.newInstance()
                }
                CommonConstant.MENU_FRAGMENT_2 -> {
                    SampleFragment.newInstance()
                }
                CommonConstant.MENU_FRAGMENT_3 -> {
                    SampleFragment.newInstance()
                }
                else -> {
                    MemberFragment.newInstance()
                }
            }

            closeDrawers()

        }
    }

    private fun actionClick() {
        relLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            closeDrawers()
        }
    }

    override fun onBackPressed() {
        if (isDrawerOpen) {
            closeDrawers()
        } else {
            finish()
        }
    }


}