package com.example.domain.interactor.comic

import io.reactivex.Single
import io.reactivex.rxkotlin.toObservable
import model.Comic
import repository.comic.ComicRepository

class ComicInteractorImpl(private val comicRepository: ComicRepository) : ComicInteractor {

    override fun getComic(id: Int): Single<Comic> {
        return comicRepository.getComic(id)
    }

    override fun getLatestComic(): Single<Comic> {
        return comicRepository.getLatestComic()
    }

    override fun getRandomComic(): Single<Comic> {
        //TODO remove magic number
        return comicRepository.getComic((0..2641).random())
    }

}
