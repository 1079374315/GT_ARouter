package com.example.myapplication.floating_window;

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.baselibrary.ARouter
import com.example.baselibrary.Config
import com.example.myapplication.R
import com.gsls.gt.GT
import com.gsls.gt.GT.Annotations.GT_AnnotationFloatingWindow
import com.gsls.gt.GT.Annotations.GT_Click
import com.gsls.gt_databinding.route.annotation.GT_Autowired
import com.gsls.gt_databinding.route.annotation.GT_Route
import com.gsls.gtk.logt


@GT_Route(value = Config.AppConfig.FLOATING, extras = "悬浮窗")
@GT_AnnotationFloatingWindow(R.layout.floating_ruler)
class RulerFloating : GT.GT_FloatingWindow.AnnotationFloatingWindow() {

    companion object{
        const val CLOSE = "close"
    }

    @GT_Autowired
    private val name: String? = null

    override fun initView(view: View?) {
        super.initView(view)
        ARouter.getInstance().inject(this) //与Autowired配合使用
        "name:$name".logt()
        "初始化 悬浮窗".logt()
        isDrag = true;//设置可拖动

    }

    override fun loadData(context: Context?, intent: Intent?, view: View?) {
        super.loadData(context, intent, view)
        val bundle = intent?.extras ?: return
        val extra = bundle.getString("extra")
        "传递过来的名称：$extra name:$name".logt()
    }

    @GT_Click(R.id.btn_cancel)
    fun onClick(view : View){
        when(view.id){
            R.id.btn_cancel -> finish()
        }
    }

    @GT.EventBus.Subscribe
    fun close(){
        finish()
    }

}