package com.example.myapplication.view

import android.view.View
import com.example.baselibrary.ARouter
import com.example.baselibrary.Config
import com.example.myapplication.R
import com.gsls.gt.GT
import com.gsls.gt.GT.Annotations.GT_AnnotationView
import com.gsls.gt.GT.Annotations.GT_Click
import com.gsls.gt_databinding.annotation.GT_DataBinding
import com.gsls.gt_databinding.route.annotation.GT_Autowired
import com.gsls.gt_databinding.route.annotation.GT_Route
import com.gsls.gtk.logt

//姓名修改
@GT_Route(value = Config.AppConfig.BASE_VIEW, extras = "GTView 模块", interceptors = [Config.TestInterceptor.MAIN, Config.TestInterceptor2.MAIN])
@GT_AnnotationView(R.layout.view_age)
class AgeView: GT.GT_View.AnnotationView() {

    @GT_Autowired
    private val name: String? = null

    override fun initView(view: View?) {
        super.initView(view)
        ARouter.getInstance().inject(this) //与Autowired配合使用

        val arguments = arguments
        val extra = arguments?.getString("extra")
        "extra:$extra".logt()
        "name:$name".logt()
    }

    @GT_Click(R.id.btn_ok)
    fun viewClick(view: View){
        when(view.id){
            R.id.btn_ok->{
                "单击了:$name".logt()
            }
        }
    }

}