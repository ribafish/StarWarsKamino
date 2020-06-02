package com.example.starwarskamino.di

import com.example.starwarskamino.MainActivity
import com.example.starwarskamino.di.main.MainFragmentModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(
        modules = [MainFragmentModule::class]
    )
    abstract fun contributeMainActivity(): MainActivity?
}