package com.suitcore.helper

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
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
                                     var closePanel: Boolean = false
) {

    enum class Type {
        BASE, ALERT, CONFIRM, CUSTOM
    }

    data class BuildBaseDialog(
            var dismiss: Boolean = false,
            var content: String? = "Please Wait"
    ){
        fun onBackpressedDismiss(dismiss: Boolean = false) = apply { this.dismiss = dismiss  }
        fun setContent(content: String?) = apply { this.content = content }
        fun build(context: Context) = BaseDialog(context, Type.BASE, dismiss, null, content,
                null, null, singleButton = true, basicButton = true,
                listener = null, imgContent = null, closePanel = false)
    }


    data class BuildAlertDialog(
            var dismiss: Boolean = false,
            var title: String? = null,
            var content: String? = null,
            var submitBtnText: String? = null,
            var basicButton: Boolean = true,
            var listener: BaseDialogInterface? = null,
            var imgContent: Int? = null
    ){
        fun onBackpressedDismiss(dismiss: Boolean = false) = apply { this.dismiss = dismiss  }
        fun setTitle(title: String?) = apply { this.title = title }
        fun setContent(content: String?) = apply { this.content = content }
        fun setBasicButton(basicButton: Boolean) = apply { this.basicButton = basicButton }
        fun setListener(listener: BaseDialogInterface?) = apply{ this.listener = listener  }
        fun setSubmitButtonText(textSubmit: String?) = apply { this.submitBtnText = textSubmit}
        fun setImageContent(imgContent: Int?) = apply { this.imgContent = imgContent }
        fun build(context: Context) = BaseDialog(context, Type.ALERT, dismiss, title, content,
                submitBtnText, null, singleButton = true, basicButton = basicButton,
                listener = null, imgContent = imgContent, closePanel = false)
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
            var imgContent: Int? = null
    ){
        fun onBackpressedDismiss(dismiss: Boolean = false) = apply { this.dismiss = dismiss  }
        fun setTitle(title: String?) = apply { this.title = title }
        fun setContent(content: String?) = apply { this.content = content }
        fun setListener(listener: BaseDialogInterface?) = apply{ this.listener = listener  }
        fun setBasicButton(basicButton: Boolean) = apply { this.basicButton = basicButton }
        fun setSingleButton(singleButton: Boolean) = apply { this.singleButton = singleButton }
        fun setSubmitButton(TextBtn: String?) = apply { this.submitBtnText = TextBtn}
        fun setCancelButton(TextBtn: String?) = apply { this.cancelBtnText = TextBtn}
        fun setImageContent(imgContent: Int?) = apply { this.imgContent = imgContent }
        fun build(context: Context) = BaseDialog(context, Type.CONFIRM, dismiss, title,
                content, submitBtnText, cancelBtnText, singleButton, basicButton = basicButton,
                listener = listener, imgContent = imgContent, closePanel = false)
    }

    data class BuildCustomViewDialog(
            var dismiss: Boolean = false,
            var title: String? = null,
            var content: String? = null,
            var basicButton: Boolean = false,
            var closePanel: Boolean = false,
            var submitBtnText: String? = null,
            var listener: BaseDialogInterface? = null,
            var resource: Int? = null
    ){
        fun onBackpressedDismiss(dismiss: Boolean = false) = apply { this.dismiss = dismiss  }
        fun setSubmitButton(TextBtn: String?) = apply { this.submitBtnText = TextBtn}
        fun setBasicButton(basicButton: Boolean) = apply { this.basicButton = basicButton }
        fun setClosePanel(closePanel: Boolean) = apply { this.closePanel = closePanel }
        fun setListener(listener: BaseDialogInterface?) = apply{ this.listener = listener  }
        fun setLayout(imgContent: Int?) = apply { this.resource = imgContent }
        fun build(context: Context) = BaseDialog(context, Type.CUSTOM, dismiss, title, content,
                submitBtnText, null, false, basicButton = basicButton,
                listener= listener, imgContent = resource, closePanel = closePanel)
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
            Type.CUSTOM -> {
                createCustomDialog()
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
    private var child: View? = null


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
        imgCloseDialog = dialogSpass?.findViewById<View>(R.id.imgCloseDialog) as ImageView

        //custom layout
        customLayout = dialogSpass?.findViewById<View>(R.id.customLayout) as LinearLayout

        //custom layout
        layButton = dialogSpass?.findViewById<View>(R.id.layButton) as LinearLayout
        tvBtnSubmit = dialogSpass?.findViewById<View>(R.id.tvBtnSubmit) as TextView
        tvBtnCancel = dialogSpass?.findViewById<View>(R.id.tvBtnCancel) as TextView
        relSubmitDialog = dialogSpass?.findViewById<View>(R.id.relSubmitDialog) as RelativeLayout
        relCancelDialog = dialogSpass?.findViewById<View>(R.id.relCancelDialog) as RelativeLayout
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

    private fun createCustomDialog() {
        initDialog()
        imgContent?.let { customDialog(it) }
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
            tvBtnSubmit?.text = "Yes"
        }

        if(cancelBtnText!=null){
            tvBtnCancel?.text = cancelBtnText
        }else{
            tvBtnCancel?.text = "No"
        }


        relSubmitDialog?.setOnClickListener {
            listener?.onSubmitClick()
            dismissDialog()
        }
        relCancelDialog?.setOnClickListener {
            listener?.onDismissClick()
            dismissDialog()
        }

        if(basicButton){
            relSubmitDialog?.setBackgroundResource(0)
            tvBtnSubmit?.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
            tvBtnCancel?.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
            relCancelDialog?.setBackgroundResource(0)
        }
        if(closePanel){
            imgCloseDialog?.visibility = View.VISIBLE
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

    private fun customDialog(resource: Int){
        customLayout?.visibility= View.VISIBLE
        layButton?.visibility = View.VISIBLE
        relSubmitDialog?.visibility = View.VISIBLE
        relCancelDialog?.visibility = View.GONE
        dialogInterface?.visibility = View.GONE
        loadingInterface?.visibility = View.GONE
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        child = layoutInflater.inflate(resource, null)
        customLayout?.addView(child)
    }

    fun getCustomView(): View? {
        return child
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