package com.example.business

import android.util.Log
import android.util.MalformedJsonException
import com.example.business.bean.WeatherBean
import com.example.business.model.IWeatherModel
import com.example.business.net.AppException
import com.example.business.net.BaseHttp
import com.example.business.net.HttpCodeException
import com.google.gson.JsonParseException
import io.reactivex.AndroidSchedulers
import org.json.JSONException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.ResourceSubscriber


class WeatherModel : IWeatherModel {
    override fun cityWeather(cityName: String, listener: IWeatherModel.CityWeatherListener) {
        BaseHttp.baseHttp.cityWeather(cityName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()) // 请求都完成后，切换到UI线程，将结果进行渲染
            .flatMap(AppException())
            .subscribe(object : ResourceSubscriber<WeatherBean>() {
                override fun onComplete() {
                }

                override fun onNext(t: WeatherBean) {
                    listener.responseWeatherSuccess(t)
                }

                override fun onError(e: Throwable) {
                    //类似listener.responseWeatherFail(e.message);
                    e.message?.let { listener.responseWeatherFail(it) }
                }
            })
    }
}

//这里再提供一个code异常类
private fun errorMsg(e: Throwable): String {
    if (e is HttpCodeException) {
        val httpException = e as HttpCodeException
        Log.e("error", "------httpException")
        return if (e.message == null) "网络错误..." else e.message!!
    } else if (e is HttpException) {//对网络异常 打出相应的log
        val errorMsg = e.message
        return errorMsg ?: "网络错误..."
    } else if (e is JsonParseException || e is JSONException || e is ParseException) {//解析异常
        return "解析异常..."
    } else if (e is UnknownHostException) {
        return "域名解析错误..."
    } else if (e is SocketTimeoutException) {
        return "网络链接超时..."
    } else if (e is MalformedJsonException) {
        return if (e.message == null) "网络错误..." else e.message!!
    }
    return "网络错误..."
}
