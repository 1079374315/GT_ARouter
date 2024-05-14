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
class WebViews(context: Context, viewGroup: ViewGroup, bundle: Bundle) : AnnotationWebView(context, viewGroup, bundle) {

    //是否初始化
    private var isInit = false

    @GT_Autowired
    private val name: String? = null

    //开始加载
    override fun pageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.pageStarted(view, url, favicon)
        if(!isInit){
            isInit = true
        }
    }

    //结束加载
    override fun pageFinished(view: WebView, url: String) {
        super.pageFinished(view, url)
    }


    override fun loadProgress(progress: Int) {
        super.loadProgress(progress)
        if (progress >= 100) {
        }
    }

    //处理异常重定向链接
    override fun shouldOverrideUrlLoad(view: WebView?, url: String?): Boolean {
        context?.let {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                it.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                webView?.goBack()
                webView?.goBack()
            }
        }
        return super.shouldOverrideUrlLoad(view, url)
    }

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

    override fun onShowFileChoosers(acceptType: String): Boolean {
        return super.onShowFileChoosers(acceptType)
    }


    //监听下载文件处理
    override fun onDownloadFile(downloadUrl: String, userAgent: String?, contentDisposition: String?, mimetype: String?, fileSize: String?, contentLength: Long) {
        super.onDownloadFile(downloadUrl, userAgent, contentDisposition, mimetype, fileSize, contentLength)
    }

    //读取 html 发送的数据
    override fun readData(json: String) {
        super.readData(json)
    }

    override fun onDestroy() {
        super.onDestroy()

    }


}