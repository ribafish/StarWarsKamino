package com.example.starwarskamino.ui

import com.example.starwarskamino.ui.main.MainFragment
import com.example.starwarskamino.ui.residentDetails.ResidentDetailsFragment
import com.example.starwarskamino.ui.residentList.ResidentListFragment
import dagger.Module

import dagger.android.ContributesAndroidInjector


@Module
abstract class MainModule {
    @ContributesAndroidInjector
    abstract fun contributeMainFragment(): MainFragment?

    @ContributesAndroidInjector
    abstract fun contributeResidentDetailsFragment(): ResidentDetailsFragment?

    @ContributesAndroidInjector
    abstract fun contributeResidentListFragment(): ResidentListFragment?
}