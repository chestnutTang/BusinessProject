package com.example.business.net

import com.example.business.bean.*
import com.example.business.model.FFF
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.http.*

interface ApiService {
    /**
     * 请求天气接口
     *
     * @param cityName 城市名称
     */
    @POST("/small/user/v1/login")
    @FormUrlEncoded
    fun cityWeather(
        @Field("phone") mobile: String,
        @Field("pwd") pwd: String
    ): Flowable<BaseBean<WeatherBean>>

    @POST("small/user/v1/login")
    @FormUrlEncoded
    fun cityWeather2(
        @Field("phone") mobile: String,
        @Field("pwd") pwd: String
    ): Flowable<BaseBean<FFF>>

    /**
     * 获取首页文章数据
     */
    @GET("/article/list/{page}/json")
    fun getHomeList(@Path("page") pageNo: Int): Observable<BaseResponse<ArticleEntity>>

    /**
     * 获取首页置顶文章数据
     */
    @GET("/article/top/json")
    fun getTopList(): Observable<BaseResponse<MutableList<ArticleEntity.DatasBean>>>

    /**
     * banner
     */
    @GET("/banner/json")
    fun getBanner(): Observable<BaseResponse<MutableList<BannerEntity>>>

    /**
     * 取消收藏
     */
    @POST("/lg/uncollect_originId/{id}/json")
    fun unCollect(@Path("id") id: Int): Observable<BaseResponse<Any>>

    /**
     * 收藏
     */
    @POST("/lg/collect/{id}/json")
    fun collect(@Path("id") id: Int): Observable<BaseResponse<Any>>

}
