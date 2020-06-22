package com.example.business.net

import com.example.business.bean.BaseBean
import io.reactivex.Flowable
import io.reactivex.functions.Function


class AppException<T> : Function<BaseBean<T>, Flowable<T>> {
    override fun apply(baseBean: BaseBean<T>): Flowable<T> {
        return if (baseBean.status?.toInt() != 0) {
            Flowable.error(baseBean.message?.let { baseBean.status?.let { it1 ->
                HttpCodeException(
                    it1, it)
            } })
            //说明一下上面的这种写法在java代码中是这样
            // Flowable.error(new HttpCodeException(baseBean.getCode(), baseBean.getMsg()));
        } else Flowable.just(baseBean.result!!)
    }
}
