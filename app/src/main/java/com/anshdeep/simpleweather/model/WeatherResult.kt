package com.anshdeep.simpleweather.model

import com.google.gson.annotations.SerializedName

/**
 * Created by ansh on 10/03/18.
 */
data class WeatherResult(
        @SerializedName("coord") val coord: Coord,
        @SerializedName("sys") val sys: Sys,
        @SerializedName("weather") val weather: List<Weather>,
        @SerializedName("main") val main: Main,
        @SerializedName("wind") val wind: Wind,
        @SerializedName("rain") val rain: Rain,
        @SerializedName("clouds") val clouds: Clouds,
        @SerializedName("dt") val dt: Long,
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String,
        @SerializedName("cod") val cod: Int
)

data class Weather(
        @SerializedName("id") val id: Int,
        @SerializedName("main") val main: String,
        @SerializedName("description") val description: String,
        @SerializedName("icon") val icon: String
)


data class Clouds(
        @SerializedName("all") val all: Int
)

data class Coord(
        @SerializedName("lon") val lon: Double,
        @SerializedName("lat") val lat: Double
)

data class Main(
        @SerializedName("temp") val temp: Double,
        @SerializedName("humidity") val humidity: Int,
        @SerializedName("pressure") val pressure: Double,
        @SerializedName("temp_min") val temp_min: Double,
        @SerializedName("temp_max") val temp_max: Double
)

data class Rain(
        @SerializedName("3h") val rain: Int
)

data class Sys(
        @SerializedName("country") val country: String,
        @SerializedName("sunrise") val sunrise: Long,
        @SerializedName("sunset") val sunset: Long
)

data class Wind(
        @SerializedName("speed") val speed: Double,
        @SerializedName("deg") val deg: Double
)

