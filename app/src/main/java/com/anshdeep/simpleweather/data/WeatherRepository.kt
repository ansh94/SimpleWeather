package com.anshdeep.simpleweather.data

import com.anshdeep.simpleweather.androidmanagers.NetManager
import com.anshdeep.simpleweather.model.WeatherResult
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by ansh on 13/02/18.
 */

/*
The only thing that repository needs to know is that the data is coming from remote or local.
There is no need to know how we are getting those remote or local data.
 */
class WeatherRepository @Inject constructor(var netManager: NetManager) {

    private val remoteDataSource = WeatherRemoteDataSource()

    fun getWeather(lat: Double, lon: Double): Observable<WeatherResult> {

        return remoteDataSource.getWeather(lat, lon)

    }

}