package com.example.myapplication.viewmode

import com.gsls.gt.GT.Frame.GT_BindingViewModel
import com.gsls.gt.GT.Frame.ViewModelFeedback

/**
 * @param <T>
 * @模块类型: 公共的 ViewModel (所有页面的 ViewModel)
 * @描述： 所有集成了 GT库封装类的 页面均可以直接使用这里的 ViewModel
 * 注意,一般 公共的 ViewModel 是很少会有改动的，几乎不用改动，但一旦修改了公共的 ViewModel 那么引用了 ViewModel 里方法的 View层就会收到影响，而没有引用到的 View则不会有任何影响
</T> */
class NullViewModelAll<T : ViewModelFeedback?> : GT_BindingViewModel<T, BaseViewModel<*>>() {


}
