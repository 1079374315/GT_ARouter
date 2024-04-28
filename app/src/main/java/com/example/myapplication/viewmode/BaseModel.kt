package com.example.myapplication.viewmode

import com.gsls.gt.GT.Frame.GT_Model

/**
 * @param <T>
 * @模块名称：数据库Model 公共的Model (独立的一个单向调用model)
 * @描述： 下面代码仅仅是个 demo演示，可以按你喜欢的网络框架进行修改，但继承 SQLApi<T> 格式不能变
</T></T> */
abstract class BaseModel<T> : GT_Model<T>() { //这里还可以再向后面继承 其他数据 model，如果有需要的话


}
