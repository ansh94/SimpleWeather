package com.anshdeep.simpleweather.ui.home

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.util.Log
import com.anshdeep.simpleweather.data.WeatherRepository
import com.anshdeep.simpleweather.model.WeatherResult
import com.anshdeep.simpleweather.utils.extensions.plusAssign
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject



/**
 * Created by ansh on 13/02/18.
 */

// All of our view models should extend the ViewModel() class
class HomeViewModel @Inject constructor(var weatherRepository: WeatherRepository) : ViewModel() {

    // ObservableField is a class from Data Binding library that we can use instead of
    // creating an Observable object. It wraps the object that we would like to be observed.
    val isLoading = ObservableField(false)

    val isRefreshing = ObservableBoolean(false)

    var weather = MutableLiveData<WeatherResult>()

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    // CompositeDisposable, a disposable container that can hold onto multiple other disposables
    private val compositeDisposable = CompositeDisposable()



    fun setLatAndLon(lat: Double,lon: Double){
        this.latitude = lat
        this.longitude = lon
        loadRepositories(latitude, longitude)
    }

    fun getLat() : Double{
        return this.latitude
    }

    fun getLon() : Double{
        return this.longitude
    }

    /**
     * Return weather to observe on the UI.
     */
    fun getUpdatedWeather(): MutableLiveData<WeatherResult>{
        return weather
    }

    private fun loadRepositories(lat: Double, lon:Double) {
        isLoading.set(true)

        // we can choose which thread will observable operate on using subscribeOn() method and
        // which thread observer will operate on using observeOn() method. Usually, all code
        // from data layer should be operated on background thread.
        compositeDisposable += weatherRepository
                .getWeather(lat,lon)
                .subscribeOn(Schedulers.newThread())   // Background thread
                .observeOn(AndroidSchedulers.mainThread()) // Android work on ui thread
                .subscribeWith(object : DisposableObserver<WeatherResult>() {

                    override fun onError(e: Throwable) {
                        //if some error happens in our data layer our app will not crash, we will
                        // get error here
                        isLoading.set(false)
                        Log.d("HomeVieWModel", "Erorr: " + e.message + e.stackTrace)
                    }

                    // called every time observable emits the data
                    override fun onNext(data: WeatherResult) {
                        Log.d("HomeViewModel", "in on next()")
//                        news.value = data.articles
                        weather.value = data

                    }

                    // called when observable finishes emitting all the data
                    override fun onComplete() {
                        isLoading.set(false)
                    }
                })
    }

    // for swipe to refresh
    fun onRefresh(lat: Double,lon:Double) {
        isRefreshing.set(true)

        // we can choose which thread will observable operate on using subscribeOn() method and
        // which thread observer will operate on using observeOn() method. Usually, all code
        // from data layer should be operated on background thread.
        compositeDisposable += weatherRepository
                .getWeather(lat,lon)
                .subscribeOn(Schedulers.newThread())   // Background thread
                .observeOn(AndroidSchedulers.mainThread()) // Android work on ui thread
                .subscribeWith(object : DisposableObserver<WeatherResult>() {

                    override fun onError(e: Throwable) {
                        //if some error happens in our data layer our app will not crash, we will
                        // get error here
                        isRefreshing.set(false)
                        Log.d("HomeVieWModel", "Erorr: " + e.message + e.stackTrace)
                    }

                    // called every time observable emits the data
                    override fun onNext(data: WeatherResult) {
                        Log.d("HomeViewModel", "in on next()")
//                        news.value = data.articles
                        weather.value = data

                    }

                    // called when observable finishes emitting all the data
                    override fun onComplete() {
                        isRefreshing.set(false)
                    }
                })
    }

    // In ViewModel's onCleared() method we should dispose all our disposables in CompositeDisposable
    override fun onCleared() {
        super.onCleared()
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }
}