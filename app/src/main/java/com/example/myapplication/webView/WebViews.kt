package com.example.myapplication.webView

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import com.example.baselibrary.Config
import com.gsls.gt.GT
import com.gsls.gt.GT.GT_WebView.AnnotationWebView
import com.gsls.gt_databinding.route.annotation.GT_Autowired
import com.gsls.gt_databinding.route.annotation.GT_Route
import com.gsls.gtk.logt


/**
 * 构建 JS Web
 *
 * @param context   上下文
 * @param viewGroup 装 Web 容器
 */
@GT_Route(value = Config.AppConfig.WEB_VIEW, extras = "web 网页")
class WebViews(context: Context, viewGroup: ViewGroup, bundle: Bundle) :
    AnnotationWebView(context, viewGroup, bundle) {

    @GT_Autowired
    private val name: String? = null

    override fun initView(context: Context, webView: WebView) {
        super.initView(context, webView)
        setCache(true)//开启缓存
        GT.ARouter.getInstance().inject(this) //与Autowired配合使用
        "name:$name".logt()
        "arguments:$arguments".logt()
        if (arguments != null) {
            val extra = arguments!!.getString("extra")
            GT.logt("extra:$extra")
        }
    }

}