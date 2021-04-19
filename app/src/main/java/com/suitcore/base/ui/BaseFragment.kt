package com.suitcore.base.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.suitcore.base.presenter.MvpView
import com.suitcore.base.ui.recyclerview.BaseRecyclerView

abstract class BaseFragment: Fragment(), MvpView {

    private var baseActivity: BaseActivity? = null
    protected open var binding: ViewBinding? = null

    abstract fun setBinding(inflater: LayoutInflater, container: ViewGroup?): ViewBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            baseActivity = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.binding = this.setBinding(inflater, container)
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        onViewReady(savedInstanceState)
    }

    protected abstract fun onViewReady(savedInstanceState: Bundle?)

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    protected fun showToast(message: String) {
        Toast.makeText(baseActivity, message, Toast.LENGTH_SHORT).show()
    }

    fun goToActivity(actDestination: Class<out Activity>, data: Bundle?, clearIntent: Boolean, isFinish: Boolean) {
        baseActivity?.goToActivity(actDestination, data, clearIntent, isFinish)
    }

    fun goToActivity(resultCode: Int, actDestination: Class<out Activity>, data: Bundle?) {
        baseActivity?.goToActivity(resultCode, actDestination, data)
    }

    fun finishLoad(recycler: BaseRecyclerView?) {
        baseActivity?.finishLoad(recycler)
    }

    fun clearRecyclerView(recyclerView: BaseRecyclerView?) {
        baseActivity?.clearRecyclerView(recyclerView)
    }

    //Custom Dialog
    override fun showDialogLoading(dismiss: Boolean, message: String?){
        baseActivity?.showDialogLoading(dismiss,message)
    }

    override fun showDialogAlert(title: String?, message: String?, confirmCallback: () -> Unit?, drawableImage: Int?){
        baseActivity?.showDialogAlert(title,message,confirmCallback, drawableImage)
    }

    override fun showDialogConfirmation(title: String?, message: String?, confirmCallback: () -> Unit?, cancelCallback: ()-> Unit?, drawableImage: Int?){
        baseActivity?.showDialogConfirmation(title, message, confirmCallback, cancelCallback, drawableImage)
    }

    override fun showDialogPopImage(drawableImage: Int?){
        baseActivity?.showDialogPopImage(drawableImage)
    }

    override fun hideLoading() {
        baseActivity?.hideLoading()
    }

}

