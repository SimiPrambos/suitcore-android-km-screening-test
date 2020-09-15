package com.suitcore.feature.member

import android.os.Bundle
import android.os.Handler
import android.view.View
import com.jcodecraeer.xrecyclerview.XRecyclerView
import com.suitcore.R
import com.suitcore.base.ui.BaseFragment
import com.suitcore.base.ui.recyclerview.BaseRecyclerView
import com.suitcore.data.model.ErrorCodeHelper
import com.suitcore.data.model.User
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_member.*
import kotlinx.android.synthetic.main.layout_base_shimmer.*

/**
 * Created by DODYDMW19 on 1/30/2018.
 */

class MemberFragment : BaseFragment(),
        MemberView,
        MemberItemView.OnActionListener {

    private var memberPresenter: MemberPresenter? = null
    private var currentPage: Int = 1
    private var memberAdapter: MemberAdapter? = MemberAdapter()

    companion object {
        fun newInstance(): BaseFragment? {
            return MemberFragment()
        }
    }

    override val resourceLayout: Int = R.layout.fragment_member

    override fun onViewReady(savedInstanceState: Bundle?) {
        setupProgressView()
        setupEmptyView()
        setupErrorView()
        setupList()
        Handler().postDelayed({ setupPresenter() }, 100)
    }

    override fun onDestroy() {
        super.onDestroy()
        clearRecyclerView(rvMember)
    }

    private fun setupPresenter() {
        memberPresenter = MemberPresenter()
        memberPresenter?.attachView(this)
        memberPresenter?.getMemberCache()
        memberPresenter?.getMember(currentPage)
    }

    private fun setupList() {
        rvMember.apply {
            setUpAsList()
            setAdapter(memberAdapter)
            setPullToRefreshEnable(true)
            setLoadingMoreEnabled(true)
            setLoadingListener(object : XRecyclerView.LoadingListener {
                override fun onRefresh() {
                    loadData(1)
                }

                override fun onLoadMore() {
                    currentPage++
                    loadData(currentPage)
                }
            })
        }
        memberAdapter?.setOnActionListener(this)
        rvMember.showShimmer()
    }

    private fun loadData(page: Int){
        memberPresenter?.getMember(page)
    }

    private fun setData(data: List<User>?) {
        data?.let {
            if (currentPage == 1) {
                memberAdapter.let {
                    memberAdapter?.clear()
                }
            }
            memberAdapter?.add(it)
        }
        rvMember.stopShimmer()
        rvMember.showRecycler()
    }

    private fun setupProgressView() {
        R.layout.layout_shimmer_member.apply {
            viewStub.layoutResource = this
        }

        viewStub.inflate()
    }

    private fun setupEmptyView() {
        rvMember.setImageEmptyView(R.drawable.empty_state)
        rvMember.setTitleEmptyView(getString(R.string.txt_empty_member))
        rvMember.setContentEmptyView(getString(R.string.txt_empty_member_content))
        rvMember.setEmptyButtonListener(object : BaseRecyclerView.ReloadListener {

            override fun onClick(v: View?) {
                loadData(1)
            }

        })
    }

    private fun setupErrorView() {
        rvMember.setImageErrorView(R.drawable.empty_state)
        rvMember.setTitleErrorView(getString(R.string.txt_error_no_internet))
        rvMember.setContentErrorView(getString(R.string.txt_error_connection))
        rvMember.setErrorButtonListener(object : BaseRecyclerView.ReloadListener {

            override fun onClick(v: View?) {
                loadData(1)
            }

        })
    }

    private fun showError(){
        finishLoad(rvMember)
        rvMember.showError()
    }

    private fun showEmpty(){
        finishLoad(rvMember)
        rvMember.showEmpty()
    }

    override fun onMemberCacheLoaded(members: RealmResults<User>?) {
        members.let {
            if (members?.isNotEmpty()!!) {
                setData(members)
            }
        }
        finishLoad(rvMember)
    }

    override fun onMemberLoaded(members: List<User>?) {
        members.let {
            if (members?.isNotEmpty()!!) {
                setData(members)
            }
        }
        finishLoad(rvMember)
    }

    override fun onMemberEmpty() {
        showEmpty()
    }

    override fun onFailed(error: Any?) {
        error?.let { ErrorCodeHelper.getErrorMessage(context, it)?.let { msg -> showToast(msg) } }
        showError()
    }


    override fun onClicked(view: MemberItemView?) {
        view?.getData()?.let {
            showToast(it.firstName.toString())
        }
    }

}