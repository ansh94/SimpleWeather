package com.anshdeep.simpleweather

import com.anshdeep.simpleweather.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

/**
 * Created by ansh on 10/03/18.
 */
class SimpleWeatherApp : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }
}