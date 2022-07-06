package com.example.app.main.comic.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.app.core.SchedulerFactory
import com.example.domain.interactor.comic.ComicInteractor
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import model.Comic
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.TimeUnit


@RunWith(MockitoJUnitRunner::class)
class ComicScreenViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var schedulerFactory: SchedulerFactory

    @Mock
    lateinit var comicInteractor: ComicInteractor

    private lateinit var viewModel: ComicScreenViewModel

    private lateinit var ioScheduler: TestScheduler

    private lateinit var mainScheduler: TestScheduler

    @Before
    fun setUp() {
        ioScheduler = TestScheduler()
        mainScheduler = TestScheduler()

        whenever(schedulerFactory.io()).thenReturn(ioScheduler)
        whenever(schedulerFactory.main()).thenReturn(mainScheduler)

        viewModel = ComicScreenViewModel(
            schedulerFactory,
            comicInteractor
        )

        viewModel.mainComicUrlLiveData.observeForever {  }
        viewModel.comicTitleLiveData.observeForever {  }
        viewModel.comicDescriptionLiveData.observeForever {  }
        viewModel.errorLiveData.observeForever {  }
        viewModel.loadingLiveData.observeForever {  }
    }

    @Test
    fun `Test getComic with empty input | should not do anything`() {
        viewModel.getComic("")
        verify(comicInteractor, never()).getComic(any())
    }

    @Test
    fun `Test getComic with non integer input | should not do anything`() {
        viewModel.getComic("comic")
        verify(comicInteractor, never()).getComic(any())
    }

    @Test
    fun `Test getComic with integer input | should do the search`() {
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

        whenever(comicInteractor.getComic(1)).thenReturn(
            Single.just(
                comic
            )
        )

        viewModel.getComic("1")

        Assert.assertEquals(viewModel.loadingLiveData.value, true)

        ioScheduler.triggerActions()
        mainScheduler.triggerActions()

        verify(comicInteractor, times(1)).getComic(1)
        Assert.assertEquals(viewModel.loadingLiveData.value, false)

        val urlResult = viewModel.mainComicUrlLiveData.value
        Assert.assertEquals(urlResult, "img")

        val titleResult = viewModel.comicTitleLiveData.value
        Assert.assertEquals(titleResult, "title")

        val descriptionResult = viewModel.comicDescriptionLiveData.value
        Assert.assertEquals(descriptionResult, "alt")
    }

    @Test
    fun `Test getComic with integer input but got error | should show error`() {
        val throwable = Throwable("error")

        whenever(comicInteractor.getComic(1)).thenReturn(
            Single.error(
                throwable
            )
        )

        viewModel.getComic("1")
        Assert.assertEquals(viewModel.loadingLiveData.value, true)

        ioScheduler.triggerActions()
        mainScheduler.triggerActions()

        verify(comicInteractor, times(1)).getComic(1)

        val errorResult = viewModel.errorLiveData.value
        Assert.assertEquals(errorResult, throwable.toString())
    }

    @Test
    fun `Test getLatestComic | should call getLatestComic from interactor`() {
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

        whenever(comicInteractor.getLatestComic()).thenReturn(
            Single.just(
                comic
            )
        )

        viewModel.getLatestComic()
        Assert.assertEquals(viewModel.loadingLiveData.value, true)

        ioScheduler.triggerActions()
        mainScheduler.triggerActions()
        Assert.assertEquals(viewModel.loadingLiveData.value, false)

        verify(comicInteractor, times(1)).getLatestComic()

        val urlResult = viewModel.mainComicUrlLiveData.value
        Assert.assertEquals(urlResult, "img")

        val titleResult = viewModel.comicTitleLiveData.value
        Assert.assertEquals(titleResult, "title")

        val descriptionResult = viewModel.comicDescriptionLiveData.value
        Assert.assertEquals(descriptionResult, "alt")
    }

    @Test
    fun `Test getRandomComic | should call getRandomComic from interactor`() {
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

        whenever(comicInteractor.getRandomComic(any())).thenReturn(
            Single.just(
                comic
            )
        )

        viewModel.getRandomComic()
        Assert.assertEquals(viewModel.loadingLiveData.value, true)

        ioScheduler.triggerActions()
        mainScheduler.triggerActions()
        Assert.assertEquals(viewModel.loadingLiveData.value, false)

        verify(comicInteractor, times(1)).getRandomComic(any())

        val urlResult = viewModel.mainComicUrlLiveData.value
        Assert.assertEquals(urlResult, "img")

        val titleResult = viewModel.comicTitleLiveData.value
        Assert.assertEquals(titleResult, "title")

        val descriptionResult = viewModel.comicDescriptionLiveData.value
        Assert.assertEquals(descriptionResult, "alt")
    }

    @Test
    fun `Test getNextComic | should call getPreviousComic from interactor`() {
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

        whenever(comicInteractor.getNextComic(any(), any())).thenReturn(
            Single.just(
                comic
            )
        )

        viewModel.getNextComic()
        Assert.assertEquals(viewModel.loadingLiveData.value, true)

        ioScheduler.triggerActions()
        mainScheduler.triggerActions()
        Assert.assertEquals(viewModel.loadingLiveData.value, false)

        verify(comicInteractor, times(1)).getNextComic(any(), any())

        val urlResult = viewModel.mainComicUrlLiveData.value
        Assert.assertEquals(urlResult, "img")

        val titleResult = viewModel.comicTitleLiveData.value
        Assert.assertEquals(titleResult, "title")

        val descriptionResult = viewModel.comicDescriptionLiveData.value
        Assert.assertEquals(descriptionResult, "alt")
    }

    @Test
    fun `Test getPreviousComic | should call getPreviousComic from interactor`() {
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

        whenever(comicInteractor.getPreviousComic(any(), any())).thenReturn(
            Single.just(
                comic
            )
        )

        viewModel.getPreviousComic()
        Assert.assertEquals(viewModel.loadingLiveData.value, true)

        ioScheduler.triggerActions()
        mainScheduler.triggerActions()
        Assert.assertEquals(viewModel.loadingLiveData.value, false)

        verify(comicInteractor, times(1)).getPreviousComic(any(), any())

        val urlResult = viewModel.mainComicUrlLiveData.value
        Assert.assertEquals(urlResult, "img")

        val titleResult = viewModel.comicTitleLiveData.value
        Assert.assertEquals(titleResult, "title")

        val descriptionResult = viewModel.comicDescriptionLiveData.value
        Assert.assertEquals(descriptionResult, "alt")
    }
}
