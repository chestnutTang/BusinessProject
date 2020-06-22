package com.example.business.ui.home

import com.example.business.base.IBasePresenter
import com.example.business.base.IBaseView
import com.example.business.bean.ArticleEntity
import com.example.business.bean.BannerEntity

interface HomeContract {
    interface View : IBaseView {
        fun showList(list: MutableList<ArticleEntity.DatasBean>)
        fun showBanner(bannerList: MutableList<BannerEntity>)
        fun collectSuccess()
        fun unCollectSuccess()
    }

    interface Presenter<T> : IBasePresenter<View> {
        fun loadData(pageNum: Int)
        fun loadBanner()
        fun collect(id: Int)
        fun unCollect(id: Int)
    }
}