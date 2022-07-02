package com.example.app.di

import com.example.app.main.di.MainActivityModule
import com.example.app.main.view.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = [MainActivityModule::class, FragmentBindingModule::class])
    abstract fun mainActivity(): MainActivity
}
