package com.example.app.main.comic.di

import com.example.app.core.SchedulerFactory
import com.example.app.main.comic.viewmodel.ComicScreenViewModel
import com.example.domain.interactor.comic.ComicInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent

@InstallIn(FragmentComponent::class)
@Module
class ComicScreenFragmentModule {

    @Provides
    fun provideComicScreenViewModel(
        schedulerFactory: SchedulerFactory,
        comicInteractor: ComicInteractor
    ): ComicScreenViewModel =
        ComicScreenViewModel(
            schedulerFactory,
            comicInteractor
        )

}
