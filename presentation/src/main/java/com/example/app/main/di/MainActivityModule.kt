package com.example.app.main.di

import com.example.app.main.view.MainActivity
import com.example.app.main.view.MainView
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {

    @Provides
    fun provideMainView(mainActivity: MainActivity): MainView = mainActivity
}
