package com.example.myapplication.viewmode

import androidx.lifecycle.MutableLiveData
import com.example.baselibrary.Config
import com.example.baselibrary.DemoBean
import com.example.baselibrary.NullViewModelAllL
import com.gsls.gt.GT.Frame.GT_BindingViewModel
import com.gsls.gt.GT.Frame.ViewModelFeedback
import com.gsls.gt_databinding.route.annotation.GT_Route
import com.gsls.gtk.runAndroid
import com.gsls.gtk.runJava
import kotlinx.coroutines.delay

/**
 * @param <T>
 * @模块类型: 公共的 ViewModel (所有页面的 ViewModel)
 * @描述： 所有集成了 GT库封装类的 页面均可以直接使用这里的 ViewModel
 * 注意,一般 公共的 ViewModel 是很少会有改动的，几乎不用改动，但一旦修改了公共的 ViewModel 那么引用了 ViewModel 里方法的 View层就会收到影响，而没有引用到的 View则不会有任何影响
</T> */

@GT_Route(value = Config.AppConfig.VIEW_MODEL, extras = "ViewModel 测试")
class NullViewModelAll<T : ViewModelFeedback?> : GT_BindingViewModel<T, BaseViewModel<*>>(), NullViewModelAllL {

    private val getAppDataValue by lazy { MutableLiveData<DemoBean>() }

    override fun getAppData(): MutableLiveData<DemoBean> {
        runJava {
            delay(3000)
            runAndroid {
                val demoBean = DemoBean()
                demoBean.name = "viewmodel Name"
                demoBean.age = 1079
                getAppDataValue.postValue(demoBean)
            }
        }
        return getAppDataValue;
    }

}
