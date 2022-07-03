package com.example.domain.interactor.comic

import io.reactivex.Single
import model.Comic
import repository.comic.ComicRepository

class ComicInteractorImpl(private val comicRepository: ComicRepository) : ComicInteractor {

    override fun getComic(id: Int): Single<Comic> {
        return comicRepository.getComic(id)
    }

    override fun getCurrentComic(): Single<Comic> {
        return comicRepository.getCurrentComic()
    }

}
