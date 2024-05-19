package com.example.myapplication.adapter

import android.content.Context
import android.view.View
import com.example.baselibrary.Config
import com.example.baselibrary.DemoBean
import com.example.myapplication.R
import com.gsls.gt.GT
import com.gsls.gt.GT.Annotations.GT_AnnotationAdapter
import com.gsls.gt_databinding.annotation.GT_DataBinding
import com.gsls.gt_databinding.route.annotation.GT_Route

//左边 item 类型
@GT_Route(value = Config.AppConfig.ADAPTER, extras = "适配器测试")
@GT_DataBinding(setLayout = "item_discover_left", setBindingType = GT_DataBinding.Adapter)
@GT_AnnotationAdapter(R.layout.item_discover_left)
class DemoAdapter(context: Context, private val onListener: GT.OnListener<DemoBean>) :
   DemoAdapterBinding<DemoBean>(context) {

    override fun initView(holder: DemoAdapterViewHolder?, itemView: View?,
                          position: Int, t: DemoBean?, context: Context?) {
        super.initView(holder, itemView, position, t, context)
        holder ?: return
        t ?: return
        holder.tv_type.text = t.name
        holder.tv_type.setOnClickListener {
            onListener.onListener(t)
        }
    }

}