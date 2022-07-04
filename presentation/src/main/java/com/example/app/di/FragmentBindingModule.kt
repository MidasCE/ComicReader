package com.example.app.di

import com.example.app.main.comic.ComicScreenFragment
import com.example.app.main.comic.di.ComicScreenFragmentModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBindingModule {
    @ContributesAndroidInjector(modules = [ComicScreenFragmentModule::class])
    abstract fun bindComicScreenFragment(): ComicScreenFragment

}
