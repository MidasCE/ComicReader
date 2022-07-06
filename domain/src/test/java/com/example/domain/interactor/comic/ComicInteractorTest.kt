package com.example.domain.interactor.comic

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
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
class ComicInteractorTest {

    @Mock
    lateinit var repository: ComicRepository

    private lateinit var interactor: ComicInteractor

    @Before
    fun setUp() {
        interactor = ComicInteractorImpl(repository)
    }

    @Test
    fun `Test getComic | should getComic from repository`() {
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

        whenever(repository.getComic(any())).thenReturn(Single.just(comic))

        interactor.getComic(1).test()
            .assertValue(comic)
    }

    @Test
    fun `Test getLatestComic | should getLatestComic from repository`() {
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

        whenever(repository.getLatestComic()).thenReturn(Single.just(comic))

        interactor.getLatestComic().test()
            .assertValue(comic)
    }

    @Test
    fun `Test getRandomComic with latest Id less than zero | should getComic with id 1`() {
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

        whenever(repository.getComic(1)).thenReturn(Single.just(comic))

        interactor.getRandomComic(-1).test()
            .assertValue(comic)

        verify(repository, times(1)).getComic(1)
    }

    @Test
    fun `Test getRandomComic with latest Id more than | should getComic with random id`() {
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

        whenever(repository.getComic(any())).thenReturn(Single.just(comic))

        interactor.getRandomComic(3).test()
            .assertValue(comic)

        verify(repository, times(1)).getComic(any())
    }

}
