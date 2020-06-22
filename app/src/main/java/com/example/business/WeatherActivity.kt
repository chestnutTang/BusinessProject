package com.example.business

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.business.bean.WeatherBean
import com.example.business.model.FFF
import kotlinx.android.synthetic.main.activity_weather.*

class WeatherActivity : AppCompatActivity(), WeatherContract.View {

    private var presenter: WeatherPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        presenter = WeatherPresenter(this)
        etCity.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
        })

        btnStart.setOnClickListener {
            val cityName = etCity.text.toString().trim();
            if (TextUtils.isEmpty(cityName)) {
                Toast.makeText(this, "请输入城市名称", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            presenter!!.cityName(cityName)

        }
    }

    @SuppressLint("SetTextI18n")
    override fun getCityWeatherSuccess(bean: FFF) {
        Toast.makeText(this, "网络请求成功", Toast.LENGTH_SHORT).show()
        if (bean.result != null) {
            val forecast = bean.result
            Toast.makeText(this, "网络请求失败", Toast.LENGTH_SHORT).show()
            tvContent.text = forecast?.headPic + "\n" + forecast?.nickName
        }
    }

    override fun getCityWeatherFail(error: String) {
        Toast.makeText(this, "网络请求失败", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter = null
    }
}
