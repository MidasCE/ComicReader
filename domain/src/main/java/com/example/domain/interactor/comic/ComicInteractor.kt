package com.example.domain.interactor.comic

import io.reactivex.Single
import model.Comic

interface ComicInteractor {

    fun getComic(id: Int) : Single<Comic>

    fun getCurrentComic() : Single<Comic>

}
