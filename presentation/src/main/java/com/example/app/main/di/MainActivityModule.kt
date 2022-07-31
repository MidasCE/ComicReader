package com.example.app.main.di

import com.example.app.main.view.MainActivity
import com.example.app.main.view.MainView
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
class MainActivityModule {

    @Provides
    fun provideMainView(mainActivity: MainActivity): MainView = mainActivity
}
