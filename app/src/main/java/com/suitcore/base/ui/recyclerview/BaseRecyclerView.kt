package com.suitcore.base.ui.recyclerview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.jcodecraeer.xrecyclerview.XRecyclerView
import com.suitcore.R
import kotlinx.android.synthetic.main.layout_base_empty.view.*
import kotlinx.android.synthetic.main.layout_base_error.view.*

class BaseRecyclerView : FrameLayout {

    private lateinit var mRecyclerView: XRecyclerView
    private lateinit var mEmptyView: FrameLayout
    private lateinit var mErrorView: FrameLayout
    private lateinit var mShimmerContainer: ShimmerFrameLayout

    private var mClipToPadding: Boolean = false
    private var mPadding: Int = 0
    private var mPaddingTop: Int = 0
    private var mPaddingBottom: Int = 0
    private var mPaddingLeft: Int = 0
    private var mPaddingRight: Int = 0
    private var mScrollbarStyle: Int = 0
    private var mScrollbar: Int = 0

    interface ReloadListener : OnClickListener

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initAttrs(attrs)
        initView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        initAttrs(attrs)
        initView()
    }

    private fun initAttrs(attrs: AttributeSet) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.BaseRecyclerView)
        try {
            mClipToPadding = a.getBoolean(R.styleable.BaseRecyclerView_recyclerClipToPadding, false)
            mPadding = a.getDimension(R.styleable.BaseRecyclerView_recyclerPadding, -1.0f).toInt()
            mPaddingLeft = a.getDimension(R.styleable.BaseRecyclerView_recyclerPaddingLeft, 0.0f).toInt()
            mPaddingTop = a.getDimension(R.styleable.BaseRecyclerView_recyclerPaddingTop, 0.0f).toInt()
            mPaddingRight = a.getDimension(R.styleable.BaseRecyclerView_recyclerPaddingRight, 0.0f).toInt()
            mPaddingBottom = a.getDimension(R.styleable.BaseRecyclerView_recyclerPaddingBottom, 0.0f).toInt()
            mScrollbar = a.getInteger(R.styleable.BaseRecyclerView_scrollbars, -1)
            mScrollbarStyle = a.getInteger(R.styleable.BaseRecyclerView_scrollbarStyle, -1)
        } finally {
            a.recycle()
        }
    }

    private fun initView() {
        buildViews()
        addViews()

        // Configure the recycler view
        mRecyclerView.apply {
            clipToPadding = mClipToPadding
            if (mPadding != (-1.0f).toInt())
                setPadding(mPadding, mPadding, mPadding, mPadding)
            else
                setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom)
            when (mScrollbar) {
                0 -> isVerticalScrollBarEnabled = false
                1 -> isHorizontalScrollBarEnabled = false
                2 -> {
                    isVerticalScrollBarEnabled = false
                    isHorizontalScrollBarEnabled = false
                }
            }
            if (mScrollbarStyle != -1) scrollBarStyle = mScrollbarStyle
        }
    }

    private fun buildViews() {
        mRecyclerView = XRecyclerView(context)
        mEmptyView = FrameLayout(context)
        mErrorView = FrameLayout(context)
        mShimmerContainer = ShimmerFrameLayout(context)

        LayoutInflater.from(context).inflate(R.layout.layout_base_empty, mEmptyView)
        LayoutInflater.from(context).inflate(R.layout.layout_base_error, mErrorView)
        LayoutInflater.from(context).inflate(R.layout.layout_base_shimmer, mShimmerContainer)
    }

    private fun addViews() {
        val lp = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

        addView(mRecyclerView, lp)
        addView(mEmptyView, lp)
        addView(mErrorView, lp)
        addView(mShimmerContainer, lp)
    }

    fun setUpAsList() {
        mRecyclerView.apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(context)
        }
    }

    fun setUpAsListInScroll() {
        mRecyclerView.apply {
            setHasFixedSize(false)
            layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        }
    }

    fun setUpAsGrid(spanCount: Int) {
        mRecyclerView.apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager = GridLayoutManager(context, spanCount)
        }
    }

    fun setUpAsGridInScroll(spanCount: Int) {
        mRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = object : GridLayoutManager(context, spanCount) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        }
    }

    fun getRecyclerView(): XRecyclerView {
        return mRecyclerView
    }

    fun getEmptyView(): View? {
        return if (mEmptyView.childCount > 0) mEmptyView.getChildAt(0) else null
    }

    fun getErrorView(): View? {
        return if (mErrorView.childCount > 0) mErrorView.getChildAt(0) else null
    }

    fun setEmptyView(view: View) {
        mEmptyView.apply {
            removeAllViews()
            addView(view)
        }
    }

    fun setEmptyView(viewResourceId: Int) {
        mEmptyView.apply {
            removeAllViews()
            LayoutInflater.from(context).inflate(viewResourceId, this)
        }
    }

    fun setErrorView(view: View) {
        mErrorView.apply {
            removeAllViews()
            addView(view)
        }
    }

    fun setErrorView(viewResourceId: Int) {
        mErrorView.apply {
            removeAllViews()
            LayoutInflater.from(context).inflate(viewResourceId, this)
        }
    }

    /**
     * Below are some methods for setting the RecyclerView attributes
     */

    fun setRecyclerViewPadding(left: Int, top: Int, right: Int, bottom: Int) {
        this.mPaddingLeft = left
        this.mPaddingTop = top
        this.mPaddingRight = right
        this.mPaddingBottom = bottom
        mRecyclerView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom)
    }

    fun setRecyclerClipToPadding(clipToPadding: Boolean) {
        mClipToPadding = clipToPadding.also {
            mRecyclerView.clipToPadding = it
        }
    }

    fun setHasFixedSize(hasFixedSize: Boolean) {
        mRecyclerView.setHasFixedSize(hasFixedSize)
    }

    fun setItemAnimator(animator: RecyclerView.ItemAnimator) {
        mRecyclerView.itemAnimator = animator
    }

    fun addItemDecoration(decor: RecyclerView.ItemDecoration) {
        mRecyclerView.addItemDecoration(decor)
    }

    fun addItemDecoration(decor: RecyclerView.ItemDecoration, index: Int) {
        mRecyclerView.addItemDecoration(decor, index)
    }

    fun removeItemDecoration(decor: RecyclerView.ItemDecoration) {
        mRecyclerView.removeItemDecoration(decor)
    }

    fun getAdapter(): RecyclerView.Adapter<*>? {
        return mRecyclerView.adapter
    }

    /**
     * Below are some methods from XRecyclerView
     */

    fun destroy() {
        mRecyclerView.destroy()
    }

    fun addHeaderView(view: View) {
        mRecyclerView.addHeaderView(view)
    }

    fun loadMoreComplete() {
        mRecyclerView.loadMoreComplete()
    }

    fun setNoMore(isNoMore: Boolean) {
        mRecyclerView.setNoMore(isNoMore)
    }

    fun setPullToRefreshEnable(isPullToRefresh: Boolean) {
        mRecyclerView.setPullRefreshEnabled(isPullToRefresh)
    }

    fun completeRefresh() {
        mRecyclerView.refreshComplete()
    }

    fun setLoadingMoreEnabled(isLoadMore: Boolean) {
        mRecyclerView.setLoadingMoreEnabled(isLoadMore)
    }

    fun setAdapter(adapter: RecyclerView.Adapter<*>?) {
        mRecyclerView.adapter = adapter
    }

    fun setLayoutManager(layout: RecyclerView.LayoutManager) {
        mRecyclerView.layoutManager = layout
    }

    fun setLoadingListener(listener: XRecyclerView.LoadingListener) {
        mRecyclerView.setLoadingListener(listener)
    }

    fun scrollToPosition(position: Int) {
        mRecyclerView.scrollToPosition(position)
    }

    /**
     * Below are some methods for managing layout visibility
     */
    private fun hideAll() {
        mRecyclerView.visibility = View.GONE
        mEmptyView.visibility = View.GONE
        mErrorView.visibility = View.GONE
        mShimmerContainer.visibility = View.GONE
        mShimmerContainer.stopShimmer()
    }

    fun showRecycler() {
        hideAll()
        mRecyclerView.visibility = View.VISIBLE
    }

    fun showShimmer() {
        if (mShimmerContainer.childCount > 0) {
            hideAll()
            mShimmerContainer.visibility = View.VISIBLE
        } else
            showRecycler()
    }

    fun stopShimmer() {
        if (mShimmerContainer.isShimmerStarted) {
            mShimmerContainer.stopShimmer()
            mShimmerContainer.visibility = View.GONE
        }
    }

    fun showEmpty() {
        if (mEmptyView.childCount > 0) {
            hideAll()
            mEmptyView.visibility = View.VISIBLE
        } else
            showRecycler()
    }

    fun hideEmpty() {
        mEmptyView.visibility = View.GONE
        showRecycler()
    }

    fun showError() {
        if (mErrorView.childCount > 0) {
            hideAll()
            mErrorView.visibility = View.VISIBLE
        } else
            showRecycler()
    }

    fun initialShimmer(){
        hideEmpty()
        showShimmer()
    }

    fun finishShimmer(){
        stopShimmer()
        showRecycler()
    }

    /**
     * Below are some methods for managing empty state & error state
     */

    fun setImageEmptyView(imageRes: Int) {
        imgEmptyView.setImageResource(imageRes)
    }

    fun setTitleEmptyView(text: String) {
        tvTitleEmptyView.text = text
    }

    fun setContentEmptyView(text: String) {
        tvContentEmptyView.text = text
    }

    fun showEmptyTitleView(status: Boolean) {
        when(status){
            true -> tvTitleEmptyView.visibility = View.VISIBLE
            false -> tvTitleEmptyView.visibility = View.GONE
        }
    }

    fun setTextButtonEmptyView(text: String) {
        btnEmpty.text = text
    }

    fun setBackgroungEmptyButton(drawableRes: Int){
        btnEmpty.setBackgroundResource(drawableRes)
    }

    fun setEmptyButtonListener(listener: ReloadListener) {
        btnEmpty.setOnClickListener(listener)
    }

    fun setImageErrorView(imageRes: Int) {
        imgErrorView.setImageResource(imageRes)
    }

    fun setTitleErrorView(text: String) {
        tvTitleErrorView.text = text
    }

    fun setContentErrorView(text: String) {
        tvContentErrorView.text = text
    }

    fun showErrorTitleView(status: Boolean) {
        when(status){
            true -> tvTitleErrorView.visibility = View.VISIBLE
            false -> tvTitleErrorView.visibility = View.GONE
        }
    }

    fun setTextButtonErrorView(text: String) {
        btnError.text = text
    }

    fun setBackgroungErrorButton(drawableRes: Int){
        btnError.setBackgroundResource(drawableRes)
    }

    fun setErrorButtonListener(listener: ReloadListener) {
        btnError.setOnClickListener(listener)
    }

}
