package com.example.business

import com.example.business.bean.WeatherBean

interface WeatherContract {
    interface View {
        fun getCityWeatherSuccess(bean: WeatherBean)

        fun getCityWeatherFail(error: String)
    }

    interface Presenter {
        fun cityName(cityName: String)
    }
}
