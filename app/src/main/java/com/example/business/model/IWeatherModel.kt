package com.example.business.model

import com.example.business.bean.WeatherBean

interface IWeatherModel {
    fun cityWeather(cityName: String, listener: CityWeatherListener)

    interface CityWeatherListener {

        fun responseWeatherSuccess(bean: FFF)

        fun responseWeatherFail(msg: String)
    }
}
