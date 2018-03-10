package com.anshdeep.simpleweather.api

import com.anshdeep.simpleweather.model.WeatherResult
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by ansh on 10/03/18.
 */
interface WeatherService {

    @GET("weather?units=imperial")
    fun getCurrentWeather(@Query("lat") latitude: Double,
                               @Query("lon") longitude: Double,
                               @Query("appid") apiKey: String): Observable<WeatherResult>


    /**
     * Kinda like a static block in Java
     */
    companion object Factory {

        private const val BASE_URL = "http://api.openweathermap.org/data/2.5/"


        fun getWeatherService(): WeatherService {
            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                    .create(WeatherService::class.java)
        }
    }
}