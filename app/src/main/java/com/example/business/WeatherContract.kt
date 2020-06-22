package com.example.business

import com.example.business.bean.WeatherBean
import com.example.business.model.FFF

interface WeatherContract {
    interface View {
        fun getCityWeatherSuccess(bean: FFF)

        fun getCityWeatherFail(error: String)
    }

    interface Presenter {
        fun cityName(cityName: String)
    }
}
