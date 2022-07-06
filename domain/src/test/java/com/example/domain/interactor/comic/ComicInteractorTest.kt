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
    fun `Test getRandomComic | should getComic with random id`() {
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

    @Test
    fun `Test getPreviousComic with mainComicId less than or equal 1 | should getComic with latestComicId`() {
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

        interactor.getPreviousComic(1, 3).test()
            .assertValue(comic)

        verify(repository, times(1)).getComic(3)
    }

    @Test
    fun `Test getPreviousComic with mainComicId more than 1 | should getComic with mainComicId - 1`() {
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

        interactor.getPreviousComic(2, 3).test()
            .assertValue(comic)

        verify(repository, times(1)).getComic(1)
    }

    @Test
    fun `Test getPreviousComic with mainComicId at 405 | should avoid 404 reserved id`() {
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

        interactor.getPreviousComic(405, 9999).test()
            .assertValue(comic)

        verify(repository, times(1)).getComic(403)
    }

    @Test
    fun `Test getNextComic with mainComicId more than latestComicId | should getComic with 1`() {
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

        interactor.getNextComic(4, 3).test()
            .assertValue(comic)

        verify(repository, times(1)).getComic(1)
    }

    @Test
    fun `Test getNextComic with mainComicId less than latestComicId | should getComic with mainComicId + 1`() {
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

        interactor.getNextComic(1, 3).test()
            .assertValue(comic)

        verify(repository, times(1)).getComic(2)
    }

    @Test
    fun `Test getNextComic with mainComicId at 403 | should avoid 404 reserved id`() {
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

        interactor.getNextComic(403, 9999).test()
            .assertValue(comic)

        verify(repository, times(1)).getComic(405)
    }

}
