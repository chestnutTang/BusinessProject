package com.example.business.net

import com.example.business.bean.BaseBean
import com.example.business.bean.WeatherBean
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query

interface NetService {
    /**
     * 请求天气接口
     *
     * @param cityName 城市名称
     */
    @GET("weatherApi")
    fun cityWeather(@Query("city") cityName: String): Flowable<BaseBean<WeatherBean>>
}
