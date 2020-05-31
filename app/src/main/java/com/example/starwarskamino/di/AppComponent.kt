package com.example.starwarskamino.di

import com.example.starwarskamino.App
import com.example.starwarskamino.MainActivity
import com.example.starwarskamino.ui.MainModule
import dagger.Component
import dagger.Module
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import javax.inject.Singleton


// Definition of a Dagger component
@Singleton
@Component(
    modules = [
        AppModule::class,
        AndroidInjectionModule::class,
        AppComponent.ActivityBindingModule::class,
        MainModule::class
    ]
)
interface AppComponent : AndroidInjector<App> {

    @Module
    abstract class ActivityBindingModule {
        @ContributesAndroidInjector
        abstract fun contributeMainActivity(): MainActivity?
    }

    @Component.Factory
    abstract class Factory : AndroidInjector.Factory<App?>

}