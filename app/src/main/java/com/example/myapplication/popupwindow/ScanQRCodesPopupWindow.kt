package com.example.myapplication.popupwindow

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.PopupWindow
import com.example.baselibrary.Config
import com.example.myapplication.R
import com.example.myapplication.viewmode.NullViewModelAll
import com.gsls.gt.GT
import com.gsls.gt.GT.Annotations.GT_AnnotationPopupWindow
import com.gsls.gt.GT.Annotations.GT_Click
import com.gsls.gt_databinding.annotation.GT_DataBinding
import com.gsls.gt_databinding.route.annotation.GT_Autowired
import com.gsls.gt_databinding.route.annotation.GT_Route
import com.gsls.gtk.logt

//扫码 选择 PopupWindow
@GT_DataBinding(setLayout = "popup_window_scan_qr_code", setBindingType = GT_DataBinding.PopupWindow)
@GT_Route(value = Config.AppConfig.POPUP_WINDOW, extras = "长安 更多内容")
@GT_AnnotationPopupWindow(R.layout.popup_window_scan_qr_code)
class ScanQRCodesPopupWindow(context: Context, bundle: Bundle) :
    ScanQRCodesPopupWindowBinding<NullViewModelAll<*>>(context, bundle) {

    @GT_Autowired
    private val name: String? = null

    override fun initView(view: View?, popWindow: PopupWindow?) {
        super.initView(view, popWindow)
        GT.ARouter.getInstance().inject(this) //与Autowired配合使用
        "name:$name".logt()
        "arguments:$arguments".logt()
        val bundle = arguments ?: return
        val extra = bundle.getString("extra")
        "传递过来的 extra：$extra".logt()
    }

    @GT_Click(R.id.ll_scan_qr_codes)
    fun onClick(view: View) {
        when (view.id) {
            R.id.ll_scan_qr_codes -> {//扫一扫
                //调佣公共的 扫码窗口 获取扫码数据
                "arguments:$arguments".logt()
                val bundle = arguments
                val extra = bundle?.getString("extra")
                "传递过来的 extra：$extra".logt()
                finish()
            }

        }
    }

}