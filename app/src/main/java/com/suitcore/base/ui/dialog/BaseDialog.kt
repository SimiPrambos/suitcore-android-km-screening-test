package com.suitcore.base.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.suitcore.R

class BaseDialog private constructor(var context: Context,
                                     var type: Type,
                                     var dismiss: Boolean = false,
                                     var title: String? = null,
                                     var content: String? = null,
                                     var submitBtnText: String? = null,
                                     var cancelBtnText: String? = null,
                                     var singleButton: Boolean = true,
                                     var basicButton: Boolean = true,
                                     var listener: BaseDialogInterface? = null,
                                     var imgContent: Int?,
                                     var hideAllButton: Boolean = false,
                                     var showPanelButton: Boolean = false
) {

    enum class Type {
        BASE, ALERT, CONFIRM,
    }

    data class BuildBaseDialog(
            var dismiss: Boolean = false,
            var content: String? = "Please Wait"
    ){
        fun onBackpressedDismiss(dismiss: Boolean = false) = apply { this.dismiss = dismiss  }
        fun setContent(content: String?) = apply { this.content = content }
        fun build(context: Context) = BaseDialog(context, Type.BASE, dismiss, null, content,
                null, null, singleButton = true, basicButton = true,
                listener = null, imgContent = null, hideAllButton = false, showPanelButton = false)
    }

    data class BuildAlertDialog(
            var dismiss: Boolean = false,
            var title: String? = null,
            var content: String? = null,
            var submitBtnText: String? = null,
            var basicButton: Boolean = true,
            var panelButton: Boolean = false,
            var hideButton: Boolean = false,
            var listener: BaseDialogInterface? = null,
            var imgContent: Int? = null
    ){
        fun onBackpressedDismiss(dismiss: Boolean = false) = apply { this.dismiss = dismiss  }
        fun setTitle(title: String?) = apply { this.title = title }
        fun setContent(content: String?) = apply { this.content = content }
        fun useMaterialButton(basicButton: Boolean) = apply { this.basicButton = basicButton }
        fun setListener(listener: BaseDialogInterface?) = apply{ this.listener = listener  }
        fun setSubmitButtonText(textSubmit: String?) = apply { this.submitBtnText = textSubmit}
        fun setImageContent(imgContent: Int?) = apply { this.imgContent = imgContent }
        fun showPanelButton(panelButton: Boolean) = apply { this.panelButton = panelButton }
        fun hideAllButton(hideButton: Boolean) = apply { this.hideButton = hideButton }
        fun build(context: Context) = BaseDialog(context, Type.ALERT, dismiss, title, content,
                submitBtnText, null, singleButton = true, basicButton = basicButton,
                listener = null, imgContent = imgContent, hideAllButton = hideButton,  showPanelButton = panelButton)
    }

    data class BuildConfirmationDialog(
            var dismiss: Boolean = false,
            var title: String? = null,
            var content: String? = null,
            var submitBtnText: String? = null,
            var cancelBtnText: String? = null,
            var listener: BaseDialogInterface? = null,
            var singleButton: Boolean = true,
            var basicButton: Boolean = true,
            var panelButton: Boolean = false,
            var hideButton: Boolean = false,
            var imgContent: Int? = null
    ){
        fun onBackpressedDismiss(dismiss: Boolean = false) = apply { this.dismiss = dismiss  }
        fun setTitle(title: String?) = apply { this.title = title }
        fun setContent(content: String?) = apply { this.content = content }
        fun setListener(listener: BaseDialogInterface?) = apply{ this.listener = listener  }
        fun useMaterialButton(basicButton: Boolean) = apply { this.basicButton = basicButton }
        fun setSingleButton(singleButton: Boolean) = apply { this.singleButton = singleButton }
        fun setSubmitButtonText(TextBtn: String?) = apply { this.submitBtnText = TextBtn}
        fun setCancelButtonText(TextBtn: String?) = apply { this.cancelBtnText = TextBtn}
        fun showPanelButton(panelButton: Boolean) = apply { this.panelButton = panelButton }
        fun hideAllButton(hideButton: Boolean) = apply { this.hideButton = hideButton }
        fun setImageContent(imgContent: Int?) = apply { this.imgContent = imgContent }
        fun build(context: Context) = BaseDialog(context, Type.CONFIRM, dismiss, title,
                content, submitBtnText, cancelBtnText, singleButton, basicButton = basicButton,
                listener = listener, imgContent = imgContent,  hideAllButton = hideButton,  showPanelButton = panelButton)
    }

    init {
        when(type){
            Type.BASE -> {
                createBaseDialog()
            }
            Type.CONFIRM -> {
                createConfirmationDialog()
            }
            Type.ALERT -> {
                createAlertDialog()
            }
        }
    }

    private var dialogSpass     : Dialog? = null
    private var imgContentDialog: ImageView? = null
    private var tvProgressDialog: TextView? = null
    private var tvTitleDialog   : TextView? = null
    private var tvContentDialog : TextView? = null
    private var tvBtnSubmit   : TextView? = null
    private var tvBtnCancel : TextView? = null
    private var dialogInterface : ConstraintLayout? = null
    private var relSubmitDialog : RelativeLayout? = null
    private var relCancelDialog : RelativeLayout? = null
    private var loadingInterface: LinearLayout? = null
    private var customLayout    : LinearLayout? = null
    private var layButton       : LinearLayout? = null
    private var parentLayout       : LinearLayout? = null
    private var imgCloseDialog  : ImageView? = null


    private fun initViews() {
        parentLayout = dialogSpass?.findViewById<View>(R.id.parentLayout) as LinearLayout

        //progress dialog
        loadingInterface = dialogSpass?.findViewById<View>(R.id.loadingInterface) as LinearLayout
        tvProgressDialog = dialogSpass?.findViewById<View>(R.id.tvProgressBar) as TextView

        //confirmation dialog
        dialogInterface = dialogSpass?.findViewById<View>(R.id.dialogInterface) as ConstraintLayout
        imgContentDialog = dialogSpass?.findViewById<View>(R.id.imgContentDialog) as ImageView
        tvTitleDialog = dialogSpass?.findViewById<View>(R.id.tvTitleDialog) as TextView
        tvContentDialog = dialogSpass?.findViewById<View>(R.id.tvContentDialog) as TextView

        //Button layout
        layButton = dialogSpass?.findViewById<View>(R.id.layButton) as LinearLayout
        tvBtnSubmit = dialogSpass?.findViewById<View>(R.id.tvBtnSubmit) as TextView
        tvBtnCancel = dialogSpass?.findViewById<View>(R.id.tvBtnCancel) as TextView
        relSubmitDialog = dialogSpass?.findViewById<View>(R.id.relSubmitDialog) as RelativeLayout
        relCancelDialog = dialogSpass?.findViewById<View>(R.id.relCancelDialog) as RelativeLayout
        imgCloseDialog = dialogSpass?.findViewById<View>(R.id.imgCloseDialog) as ImageView
    }

    private fun initDialog(){
        dialogSpass = Dialog(context)
        dialogSpass?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogSpass?.setCanceledOnTouchOutside(false)
        dialogSpass?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialogSpass?.setContentView(R.layout.base_dialog)
        dialogSpass?.setOnCancelListener {if (dismiss) { dismissDialog()}}

        initViews()
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSpass?.window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        dialogSpass?.window?.attributes = lp
    }

    private fun createBaseDialog() {
        initDialog()
        basicDialog()
        initializeView()
    }


    private fun createAlertDialog() {
        initDialog()
        alertDialog()
        initializeView()
    }

    private fun createConfirmationDialog() {
        initDialog()
        confirmDialog()
        initializeView()
    }

    private fun initializeView() {
        if(title!=null){
            tvTitleDialog?.visibility = View.VISIBLE
            tvTitleDialog?.text = title
        }else{
            tvTitleDialog?.visibility = View.GONE
        }

        if(imgContent!=null){
            imgContentDialog?.visibility = View.VISIBLE
            imgContentDialog?.setBackgroundResource(imgContent!!)
        }else{
            imgContentDialog?.visibility = View.GONE
            imgContentDialog?.setBackgroundResource(0)
        }
        if(Type.BASE == type){
            if(content!=null){
                tvProgressDialog?.visibility = View.VISIBLE
                tvProgressDialog?.text = content
            }else{
                tvProgressDialog?.visibility = View.GONE
            }
        }else{
            if(content!=null){
                tvContentDialog?.visibility = View.VISIBLE
                tvContentDialog?.text = content
            }else{
                tvContentDialog?.visibility = View.GONE
            }
        }

        if(submitBtnText!=null){
            tvBtnSubmit?.text = submitBtnText
        }else{
            tvBtnSubmit?.text = context.getString(R.string.txt_yes)
        }

        if(cancelBtnText!=null){
            tvBtnCancel?.text = cancelBtnText
        }else{
            tvBtnCancel?.text = context.getString(R.string.txt_no)
        }

        //Listener
        relSubmitDialog?.setOnClickListener {
            listener?.onSubmitClick()
            dismissDialog()
        }
        relCancelDialog?.setOnClickListener {
            listener?.onDismissClick()
            dismissDialog()
        }

        //using basic button
        if(basicButton){
            relSubmitDialog?.setBackgroundResource(0)
            tvBtnSubmit?.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
            tvBtnCancel?.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
            relCancelDialog?.setBackgroundResource(0)
        }

        //hide all layout button
        if(hideAllButton){
            layButton?.visibility = View.GONE
        }

        //panel close
        if(showPanelButton){
            imgCloseDialog?.visibility = View.VISIBLE
            imgCloseDialog?.setOnClickListener {
                dismissDialog()
            }
        }else {
            imgCloseDialog?.visibility = View.GONE
        }
    }

    private fun basicDialog(){
        loadingInterface?.visibility = View.VISIBLE
        customLayout?.visibility= View.GONE
        dialogInterface?.visibility = View.GONE
        layButton?.visibility = View.GONE
        parentLayout?.setBackgroundResource(0)
    }

    private fun alertDialog(){
        dialogInterface?.visibility = View.VISIBLE
        relSubmitDialog?.visibility = View.VISIBLE
        relCancelDialog?.visibility= View.GONE
        customLayout?.visibility= View.GONE
        loadingInterface?.visibility = View.GONE
    }

    private fun confirmDialog(){
        dialogInterface?.visibility = View.VISIBLE
        layButton?.visibility = View.VISIBLE
        relSubmitDialog?.visibility = View.VISIBLE
        if(!singleButton){
            relCancelDialog?.visibility = View.VISIBLE
        }else{
            relCancelDialog?.visibility = View.GONE
        }
        relSubmitDialog?.visibility = View.VISIBLE
        customLayout?.visibility= View.GONE
        loadingInterface?.visibility = View.GONE
    }

    fun show() {
        dialogSpass?.show()
    }

    fun dismissDialog() {
        dialogSpass?.dismiss()
        if (listener != null) {
            listener?.onDismissClick()
        }
    }

    fun isShowing(): Boolean {
        return dialogSpass!!.isShowing
    }

}