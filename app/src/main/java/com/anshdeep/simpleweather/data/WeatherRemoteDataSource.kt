package com.anshdeep.simpleweather.data

import com.anshdeep.simpleweather.BuildConfig
import com.anshdeep.simpleweather.api.WeatherService
import com.anshdeep.simpleweather.model.WeatherResult
import io.reactivex.Observable

/**
 * Created by ansh on 13/02/18.
 */
class WeatherRemoteDataSource {

    private val weatherService: WeatherService = WeatherService.getWeatherService()

    private val apiKey = BuildConfig.WEATHER_API_KEY

    fun getWeather(lat: Double, lon: Double): Observable<WeatherResult> {
        return weatherService.getCurrentWeather(lat, lon, apiKey)
    }

}