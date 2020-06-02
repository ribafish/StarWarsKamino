package com.example.starwarskamino.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.starwarskamino.ui.main.MainViewModel
import com.example.starwarskamino.ui.residentDetails.ResidentDetailsViewModel
import com.example.starwarskamino.ui.residentList.ResidentListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
abstract class ViewModelsModule {

    @Singleton
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindsMainViewModel(vm: MainViewModel): ViewModel

    @Singleton
    @Binds
    @IntoMap
    @ViewModelKey(ResidentListViewModel::class)
    abstract fun bindesResidentListViewModel(vm: ResidentListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ResidentDetailsViewModel::class)
    abstract fun bindsResidentDetailsViewModel(vm: ResidentDetailsViewModel): ViewModel

    // Factory
    @Singleton
    @Binds abstract fun bindViewModelFactory(vmFactory: ViewModelFactory): ViewModelProvider.Factory
}