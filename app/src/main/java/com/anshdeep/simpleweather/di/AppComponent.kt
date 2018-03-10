package com.anshdeep.simpleweather.di

import com.anshdeep.simpleweather.SimpleWeatherApp
import com.anshdeep.simpleweather.utils.di.ViewModelBuilder
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Created by ansh on 13/02/18.
 */

// A Component is an interface where we specify from which modules instances
// should be injected.

// AndroidSupportInjectionModule is the module that helps us to inject instances
// into Android ecosystem classes such are: Activities, Fragments, Services,
// BroadcastReceivers or ContentProviders.
@Singleton
@Component(
        modules = [AndroidSupportInjectionModule::class,
            AppModule::class,
            ViewModelBuilder::class,
            ActivityModule::class])
interface AppComponent : AndroidInjector<SimpleWeatherApp> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<SimpleWeatherApp>()
}