package com.example.data.repository

import com.example.data.api.ComicAPI
import com.example.data.entity.ComicResponse
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import model.Comic
import org.junit.Test

import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import repository.comic.ComicRepository

@RunWith(MockitoJUnitRunner::class)
class ComicRepositoryImplTest {

    @Mock
    lateinit var api: ComicAPI


    private lateinit var repository: ComicRepository

    @Before
    fun setUp() {
        repository = ComicRepositoryImpl(api)
    }

    @Test
    fun `Test getComic | should return data as a domain type`() {
        val comicResponse = ComicResponse(
            1,
            "link",
            "year",
            "news",
            "safeTitle",
            "transcript",
            "alt",
            "img",
            "title",
            "day"
        )

        val comic = Comic(
            1,
            "link",
            "year",
            "news",
            "safeTitle",
            "transcript",
            "alt",
            "img",
            "title",
            "day"
        )

        whenever(api.getComic(any())).thenReturn(Single.just(comicResponse))

        repository.getComic(1).test()
            .assertValue(comic)
    }

    @Test
    fun `Test getLatestComic | should return data as a domain type`() {
        val comicResponse = ComicResponse(
            1,
            "link",
            "year",
            "news",
            "safeTitle",
            "transcript",
            "alt",
            "img",
            "title",
            "day"
        )

        val comic = Comic(
            1,
            "link",
            "year",
            "news",
            "safeTitle",
            "transcript",
            "alt",
            "img",
            "title",
            "day"
        )

        whenever(api.getLatestComic()).thenReturn(Single.just(comicResponse))

        repository.getLatestComic().test()
            .assertValue(comic)
    }
}
