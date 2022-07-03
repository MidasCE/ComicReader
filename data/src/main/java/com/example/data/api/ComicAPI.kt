package com.example.data.api

import com.example.data.entity.ComicResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface ComicAPI {

    @GET("{id}/info.0.json")
    fun getComic(@Path("id") id: Int)
            : Single<ComicResponse>

    @GET("info.0.json")
    fun getCurrentComic() : Single<ComicResponse>
}
