package com.example.domain.interactor.comic

import io.reactivex.Single
import io.reactivex.rxkotlin.toObservable
import model.Comic
import repository.comic.ComicRepository

class ComicInteractorImpl(private val comicRepository: ComicRepository) : ComicInteractor {

    companion object {
        val RESERVED_ID: Int = 404
    }
    override fun getComic(id: Int): Single<Comic> {
        return comicRepository.getComic(id)
    }

    override fun getNextComic(mainComicId: Int, latestComicId: Int): Single<Comic> {
        var id = if (mainComicId >= latestComicId) {
            1
        } else {
            mainComicId + 1
        }
        if (id == RESERVED_ID) {
            id++
        }
        return comicRepository.getComic(id)
    }

    override fun getPreviousComic(mainComicId: Int, latestComicId: Int): Single<Comic> {
        var id = if (mainComicId <= 1) {
            latestComicId
        } else {
            mainComicId - 1
        }
        if (id == RESERVED_ID) {
            id--
        }
        return comicRepository.getComic(id)
    }

    override fun getLatestComic(): Single<Comic> {
        return comicRepository.getLatestComic()
    }

    override fun getRandomComic(latestComicId: Int): Single<Comic> {
        if (latestComicId <= 0) {
            return comicRepository.getComic(1)
        }
        var randomNum = (1..latestComicId).random()
        while (randomNum == RESERVED_ID)
        {
            randomNum = (1..latestComicId).random()
        }
        return comicRepository.getComic(randomNum)
    }

}
