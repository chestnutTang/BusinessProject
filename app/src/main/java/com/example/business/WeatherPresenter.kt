package com.example.business

import com.example.business.bean.WeatherBean
import com.example.business.model.IWeatherModel

class WeatherPresenter internal constructor( private val mView: WeatherContract.View) : WeatherContract.Presenter {
    private val mModel: WeatherModel = WeatherModel()

    override fun cityName(cityName: String) {
        mModel.cityWeather(cityName, object : IWeatherModel.CityWeatherListener {
            override fun responseWeatherSuccess(bean: WeatherBean) {
                mView.getCityWeatherSuccess(bean)
            }

            override fun responseWeatherFail(msg: String) {
                mView.getCityWeatherFail(msg)
            }
        })
    }
}
