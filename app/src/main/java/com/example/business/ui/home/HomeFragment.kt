package com.example.business.ui.home

import ReloadListener
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import cn.bingoogolapple.bgabanner.BGABanner
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.business.R
import com.example.business.adapter.ArticleAdapter
import com.example.business.adapter.OnCollectClickListener
import com.example.business.base.AppLazyFragment
import com.example.business.bean.ArticleEntity
import com.example.business.bean.BannerEntity
import com.example.business.constants.Constants
import com.example.business.event.LoginEvent
import com.example.business.event.LogoutEvent
import com.example.business.proxy.ImageLoad
import com.example.business.util.AppManager
import com.example.business.util.ToastUtils
import com.example.business.web.WebActivity
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.fragment_home.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class HomeFragment : AppLazyFragment<HomeContract.Presenter<HomeContract.View>>(),
    BGABanner.Adapter<ImageView?, String?>
    , BGABanner.Delegate<ImageView?, String?>, HomeContract.View, OnLoadMoreListener,
    OnRefreshListener, ReloadListener
    , BaseQuickAdapter.OnItemClickListener, OnCollectClickListener {

    private lateinit var homeViewModel: HomeViewModel
    private var articleList = mutableListOf<ArticleEntity.DatasBean>()
    private var bannerList = mutableListOf<BannerEntity>()
    private var articleAdapter: ArticleAdapter? = null
    private var currentPosition = 0
    private var pageNum: Int = 0

    /**
     * 点击收藏后将点击事件上锁,等接口有相应结果再解锁
     * 避免重复点击产生的bug
     */
    private var lockCollectClick = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        homeViewModel =
//            ViewModelProviders.of(this).get(HomeViewModel::class.java)
//        val root = inflater.inflate(R.layout.fragment_home, container, false)
//        val textView: TextView = root.findViewById(R.id.text_home)
//        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
//        return root
//    }


    override fun lazyInit() {
        loadData()
    }

    private fun initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            rlSearch.elevation = 10f
            llRadius.elevation = 20f
            rvHomeList.isNestedScrollingEnabled = false
        }

        articleAdapter =
            ArticleAdapter(articleList)
        articleAdapter?.onItemClickListener = this
        articleAdapter?.setCollectClickListener(this)
        articleAdapter?.setNewData(articleList)
        rvHomeList.adapter = articleAdapter
        loadingTip.setReloadListener(this)
        smartRefresh?.setOnRefreshListener(this)
        smartRefresh?.setOnLoadMoreListener(this)
        addScrollListener()
        rvHomeList.layoutManager = LinearLayoutManager(context)
        ivSearch.setOnClickListener {
            intent(WebActivity::class.java, false)
            //瞬间开启activity，无动画
            activity?.overridePendingTransition(0, 0)

        }
    }

    private fun loadData() {
        articleList.clear()
        articleAdapter?.setNewData(articleList)
        pageNum = 0
        presenter?.loadData(pageNum)
    }

    /**
     * 为NestedScrollView增加滑动事件
     * 改变搜索框的透明度
     */
    private fun addScrollListener() {
        nestedView.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener
            { _, _, scrollY, _, _ ->
                val alpha = if (scrollY > 0) {
                    ivSearch.isEnabled = true
                    scrollY.toFloat() / (300).toFloat()
                } else {
                    ivSearch.isEnabled = false
                    0f
                }
                rlSearch.alpha = alpha
            })
    }

    /**
     * 初始化banner
     */
    private fun initBanner() {
        banner.setAutoPlayAble(true)
        val views: MutableList<View> = ArrayList()
        bannerList.forEach { _ ->
            views.add(
                LayoutInflater.from(context).inflate(R.layout.banner_layout, null)
                    .findViewById(R.id.ivBanner)
            )
        }
        banner.setAdapter(this)
        banner.setDelegate(this)
        banner.setData(views)
    }


    override fun createPresenter(): HomeContract.Presenter<HomeContract.View>? {
        return HomePresenter(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun fillBannerItem(
        banner: BGABanner?,
        itemView: ImageView?,
        model: String?,
        position: Int
    ) {
        itemView?.let {
            it.scaleType = ImageView.ScaleType.CENTER_CROP
            val bannerEntity = bannerList[position]
            Glide.with(this)
                .load(bannerEntity.imagePath)
                .into(it)
            ImageLoad.load(it, bannerEntity.imagePath)
        }
    }

    override fun onBannerItemClick(
        banner: BGABanner?,
        itemView: ImageView?,
        model: String?,
        position: Int
    ) {
        intent(Bundle().apply {
            putString(Constants.WEB_URL, bannerList[position].url)
            putString(Constants.WEB_TITLE, bannerList[position].title)
        }, WebActivity::class.java, false)
    }

    override fun showList(list: MutableList<ArticleEntity.DatasBean>) {
        dismissRefresh()
        loadingTip.dismiss()
        if (list.isNotEmpty()) {
            articleList.addAll(list)
            articleAdapter?.setNewData(articleList)
        } else {
            if (articleList.size == 0) loadingTip.showEmpty()
            else ToastUtils.show("没有数据啦...")
        }
    }

    override fun showBanner(bannerList: MutableList<BannerEntity>) {
        this.bannerList.addAll(bannerList)
        initBanner()
    }

    override fun unCollectSuccess() {
        lockCollectClick = true
        if (currentPosition < articleList.size) {
            articleList[currentPosition].collect = false
            articleAdapter?.notifyItemChanged(currentPosition)
        }
    }

    override fun collectSuccess() {
        lockCollectClick = true
        if (currentPosition < articleList.size) {
            articleList[currentPosition].collect = true
            articleAdapter?.notifyItemChanged(currentPosition)
        }
    }

    override fun onError(error: String) {
        lockCollectClick = true
        //请求失败将page -1
        if (pageNum > 0) pageNum--
        loadingTip.dismiss()
        dismissRefresh()
        ToastUtils.show(error)
    }

    /**
     * 加载更多
     */
    override fun onLoadMore(refreshLayout: RefreshLayout) {
        pageNum++
        presenter?.loadData(pageNum)
    }

    /**
     * 刷新
     */
    override fun onRefresh(refreshLayout: RefreshLayout) {
        loadData()
    }

    /**
     * 无网络，重新加载
     */
    override fun reload() {
        loadingTip.loading()
        loadData()
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        intent(Bundle().apply {
            putString(Constants.WEB_URL, articleList[position].link)
            putString(Constants.WEB_TITLE, articleList[position].title)
        }, WebActivity::class.java, false)
    }

    /**
     * 收藏点击
     */
    override fun onCollectClick(helper: BaseViewHolder, position: Int) {
        if (!AppManager.isLogin()) {
            ToastUtils.show("请先登录")
            return
        }
        if (position < articleList.size && lockCollectClick) {
            lockCollectClick = false
            //记录当前点击的item
            currentPosition = position
            //收藏状态调用取消收藏接口，反之亦然
            articleList[position].apply {
                if (collect) presenter?.unCollect(id)
                else presenter?.collect(id)
            }
        }
    }


    /**
     * 隐藏刷新加载
     */
    private fun dismissRefresh() {
        if (smartRefresh.state.isOpening) {
            smartRefresh.finishLoadMore()
            smartRefresh.finishRefresh()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    /**
     * 登陆消息，更新收藏状态
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun loginEvent(loginEvent: LoginEvent) {

    }

    /**
     * 退出消息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun logoutEvent(loginEvent: LogoutEvent) {
        articleList.forEach {
            it.collect = false
        }
        articleAdapter?.notifyDataSetChanged()
    }

}