package com.suitcore.feature.member

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.jcodecraeer.xrecyclerview.XRecyclerView
import com.suitcore.R
import com.suitcore.base.ui.BaseFragment
import com.suitcore.base.ui.recyclerview.BaseRecyclerView
import com.suitcore.data.model.ErrorCodeHelper
import com.suitcore.data.model.User
import com.suitcore.databinding.FragmentMemberBinding
import io.realm.RealmResults

/**
 * Created by DODYDMW19 on 1/30/2018.
 */

class MemberFragment : BaseFragment(), MemberView, MemberItemView.OnActionListener {

    private var memberPresenter: MemberPresenter? = null
    private var currentPage: Int = 1
    private var memberAdapter: MemberAdapter? = null
    private lateinit var memberBinding: FragmentMemberBinding

    companion object {
        fun newInstance(): BaseFragment {
            return MemberFragment()
        }
    }

    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): ViewBinding {
        memberBinding = FragmentMemberBinding.inflate(inflater, container, false)
        return memberBinding
    }

    override fun onViewReady(savedInstanceState: Bundle?) {
        setupProgressView()
        setupEmptyView()
        setupErrorView()
        setupList()
        Handler(Looper.getMainLooper()).postDelayed({
            setupPresenter()
        }, 100)
    }

    override fun onDestroy() {
        super.onDestroy()
        clearRecyclerView(memberBinding.rvMember)
    }

    private fun setupPresenter() {
        memberPresenter = MemberPresenter()
        memberPresenter?.attachView(this)
        memberPresenter?.getMemberCache()
    }

    private fun setupList() {
        memberAdapter = context?.let { MemberAdapter(it) }
        memberBinding.rvMember.apply {
            setUpAsList()
            setAdapter(memberAdapter)
            setPullToRefreshEnable(true)
            setLoadingMoreEnabled(true)
            setLoadingListener(object : XRecyclerView.LoadingListener {
                override fun onRefresh() {
                    currentPage = 1
                    loadData(currentPage)
                }

                override fun onLoadMore() {
                    currentPage++
                    loadData(currentPage)
                }
            })
        }
        memberAdapter?.setOnActionListener(this)
        memberBinding.rvMember.showShimmer()
    }

    private fun loadData(page: Int) {
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
        memberBinding.rvMember.stopShimmer()
        memberBinding.rvMember.showRecycler()
    }

    private fun setupProgressView() {
        R.layout.layout_shimmer_member.apply {
            memberBinding.rvMember.baseShimmerBinding.viewStub.layoutResource = this
        }

        memberBinding.rvMember.baseShimmerBinding.viewStub.inflate()
    }

    private fun setupEmptyView() {
        memberBinding.rvMember.setImageEmptyView(R.drawable.empty_state)
        memberBinding.rvMember.setTitleEmptyView(getString(R.string.txt_empty_member))
        memberBinding.rvMember.setContentEmptyView(getString(R.string.txt_empty_member_content))
        memberBinding.rvMember.setEmptyButtonListener(object : BaseRecyclerView.ReloadListener {

            override fun onClick(v: View?) {
                loadData(1)
            }

        })
    }

    private fun setupErrorView() {
        memberBinding.rvMember.setImageErrorView(R.drawable.empty_state)
        memberBinding.rvMember.setTitleErrorView(getString(R.string.txt_error_no_internet))
        memberBinding.rvMember.setContentErrorView(getString(R.string.txt_error_connection))
        memberBinding.rvMember.setErrorButtonListener(object : BaseRecyclerView.ReloadListener {

            override fun onClick(v: View?) {
                loadData(1)
            }

        })
    }

    private fun showError() {
        finishLoad(memberBinding.rvMember)
        memberBinding.rvMember.showError()
    }

    private fun showEmpty() {
        finishLoad(memberBinding.rvMember)
        memberBinding.rvMember.showEmpty()
    }

    override fun onMemberCacheLoaded(members: RealmResults<User>?) {
        members.let {
            if (members?.isNotEmpty()!!) {
                setData(members)
            }
        }
        finishLoad(memberBinding.rvMember)
        loadData(currentPage)
    }

    override fun onMemberLoaded(members: List<User>?) {
        members.let {
            if (members?.isNotEmpty()!!) {
                setData(members)
            }
        }
        finishLoad(memberBinding.rvMember)
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