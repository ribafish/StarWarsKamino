package com.example.starwarskamino.di.main

import com.example.starwarskamino.di.FragmentScope
import com.example.starwarskamino.di.ViewModelsModule
import com.example.starwarskamino.ui.main.MainFragment
import com.example.starwarskamino.ui.residentDetails.ResidentDetailsFragment
import com.example.starwarskamino.ui.residentList.ResidentListFragment
import dagger.Module

import dagger.android.ContributesAndroidInjector


@Module
abstract class MainFragmentModule {
    @ContributesAndroidInjector
    internal abstract fun contributeMainFragment(): MainFragment?

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeResidentDetailsFragment(): ResidentDetailsFragment?

    @ContributesAndroidInjector
    internal abstract fun contributeResidentListFragment(): ResidentListFragment?
}