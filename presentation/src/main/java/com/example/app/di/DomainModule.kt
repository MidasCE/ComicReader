package com.example.app.di

import com.example.domain.interactor.comic.ComicInteractor
import com.example.domain.interactor.comic.ComicInteractorImpl
import dagger.Module
import dagger.Provides
import repository.comic.ComicRepository
import javax.inject.Singleton

@Module
class DomainModule {

    @Provides
    @Singleton
    fun provideComicInteractor(repository: ComicRepository): ComicInteractor =
        ComicInteractorImpl(repository)
}
