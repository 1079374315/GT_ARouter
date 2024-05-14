package com.example.model1

import android.os.Bundle
import android.view.View
import com.example.baselibrary.Config
import com.gsls.gt.GT
import com.gsls.gt_databinding.route.annotation.GT_Autowired
import com.gsls.gt_databinding.route.annotation.GT_Route
import com.gsls.gtk.logt

@GT_Route(Config.Model1Config.ModelFragment1.MAIN)
class Mode2Fragment : GT.GT_Fragment.BaseFragment() {

    override fun loadLayout(): Int {
        return R.layout.fragment_model1
    }

    @GT_Autowired
    private val name: String? = null


    override fun initView(view: View?, savedInstanceState: Bundle?) {
        GT.logt("初始化 Mode2Fragment")
        GT.ARouter.getInstance().inject(this) //与Autowired配合使用
        "name:$name".logt()
        arguments


    }

}