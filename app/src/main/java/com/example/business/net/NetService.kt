package com.example.business.net

import com.example.business.bean.BaseBean
import com.example.business.bean.WeatherBean
import com.example.business.model.FFF
import io.reactivex.Flowable
import retrofit2.http.*

interface NetService {
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
}
