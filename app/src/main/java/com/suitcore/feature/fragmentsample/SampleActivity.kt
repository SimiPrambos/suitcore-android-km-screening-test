package com.suitcore.feature.fragmentsample

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.suitcore.R
import com.suitcore.base.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_test.*

/**
 * Created by dodydmw19 on 7/30/18.
 */

class SampleActivity : BaseActivity() {

    private lateinit var mCurrentFragment: Fragment

    override val resourceLayout: Int
        get() = R.layout.activity_test

    override fun onViewReady(savedInstanceState: Bundle?) {
        setupToolbar(mToolbar, true)
        setContentFragment(SampleFragment.newInstance())
    }

    private fun setContentFragment(fragment: Fragment) {
        mCurrentFragment = fragment
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, mCurrentFragment)
                .commitAllowingStateLoss()
    }
}