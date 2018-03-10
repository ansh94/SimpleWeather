package com.anshdeep.simpleweather.di

import com.anshdeep.simpleweather.ui.MainActivity
import com.anshdeep.simpleweather.ui.home.HomeFragmentProvider
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by ansh on 22/02/18.
 */

@Module
internal abstract class ActivityModule {

    @ContributesAndroidInjector(modules = [HomeFragmentProvider::class])
    internal abstract fun bindMainActivity(): MainActivity

}