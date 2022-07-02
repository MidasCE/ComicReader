package com.example.app.di

import android.app.Application
import android.content.Context
import com.example.app.core.SchedulerFactory
import com.example.app.core.SchedulerFactoryImpl
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Named
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Provides
    @Singleton
    fun provideContext(app: Application): Context = app

    @Provides
    @Singleton
    @Named("main")
    fun provideMainScheduler(): Scheduler =  AndroidSchedulers.mainThread()

    @Provides
    @Singleton
    @Named("io")
    fun provideIoScheduler(): Scheduler = Schedulers.io()

    @Provides
    @Singleton
    fun provideSchedulerFactory(@Named("io") ioScheduler : Scheduler,
                                @Named("main") mainScheduler : Scheduler
    ): SchedulerFactory
            = SchedulerFactoryImpl(ioScheduler, mainScheduler)
}
