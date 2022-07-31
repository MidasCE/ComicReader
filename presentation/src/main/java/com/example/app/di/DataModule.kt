package com.example.app.di

import com.example.data.api.ComicAPI
import com.example.data.repository.ComicRepositoryImpl
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import repository.comic.ComicRepository
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataModule {

    @Provides
    @Singleton
    fun provideComicAPI(retrofit: Retrofit): ComicAPI =
        retrofit.create(ComicAPI::class.java)

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, logging: HttpLoggingInterceptor): Retrofit = Retrofit.Builder()
        .baseUrl("https://xkcd.com/")
        .client(OkHttpClient.Builder().addInterceptor(logging).build())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
        .create()

    @Provides
    @Singleton
    fun provideProductRepository(api: ComicAPI): ComicRepository =
        ComicRepositoryImpl(api)

}
