package com.example.myapplication.viewmode

import androidx.lifecycle.MutableLiveData
import com.gsls.gt.GT
import com.gsls.gt.GT.Frame.GT_BindingViewModel
import com.gsls.gt.GT.Frame.ViewModelFeedback

/**
 * @param <T>
 * @模块类型: 公共的 ViewModel (所有页面的 ViewModel)
 * @描述： 所有集成了 GT库封装类的 页面均可以直接使用这里的 ViewModel
 * 注意,一般 公共的 ViewModel 是很少会有改动的，几乎不用改动，但一旦修改了公共的 ViewModel 那么引用了 ViewModel 里方法的 View层就会收到影响，而没有引用到的 View则不会有任何影响
</T> */
open class BaseViewModel<T : ViewModelFeedback?> : GT_BindingViewModel<T, BaseModel<*>>() {

    /***************************************** 测试内容 *************************************/

    //两种方式返回 数据
    private val liveDataType by lazy { MutableLiveData<String>() }
    val type: MutableLiveData<String>
        get() {
            GT.Thread.runJava {
                GT.Thread.sleep(2000)
                liveDataType.postValue("你好！我是成功返回")
                GT.Thread.sleep(2000)
                if (bindingView != null) {
                    bindingView!!.onViewModeFeedback("getType", "我是自定义的成功")
                }
            }
            return liveDataType
        }


}
