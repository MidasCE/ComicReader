package com.example.data.repository

import com.example.data.api.ComicAPI
import com.example.data.mapper.toDomain
import io.reactivex.Single
import model.Comic
import repository.comic.ComicRepository

class ComicRepositoryImpl(private val api: ComicAPI) : ComicRepository {

    override fun getCurrentComic(): Single<Comic> {
        return api.getCurrentComic().map { response ->
            response.toDomain()
        }
    }

    override fun getComic(id: Int): Single<Comic> {
        return api.getComic(id).map { response ->
            response.toDomain()
        }
    }
}
