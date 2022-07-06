package com.example.app.main.comic.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app.core.Event
import com.example.app.core.SchedulerFactory
import com.example.domain.interactor.comic.ComicInteractor
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import model.Comic

class ComicScreenViewModel(
    private val schedulerFactory: SchedulerFactory,
    private val comicInteractor: ComicInteractor
) : ViewModel() {
    private val _openDetailsEvent = MutableLiveData<Event<String>>()
    val openDetailsEvent: LiveData<Event<String>> = _openDetailsEvent

    val loadingLiveData = MutableLiveData<Boolean>()
    val mainComicUrlLiveData = MutableLiveData<String>()
    val comicTitleLiveData = MutableLiveData<String>()
    val comicDescriptionLiveData = MutableLiveData<String>()
    val errorLiveData = MutableLiveData<String>()

    private var latestComicId: Int = 2641
    private var mainComicId: Int = 1
    private var compositeDisposable = CompositeDisposable()

    fun getLatestComic() {
        loadingLiveData.postValue(true)
        val disposable = comicInteractor.getLatestComic()
            .subscribeOn(schedulerFactory.io())
            .observeOn(schedulerFactory.main())
            .subscribe({ comic ->
                loadingLiveData.value = false
                handleComicLoaded(comic)
                latestComicId = comic.num

            }, {
                loadingLiveData.value = false
                handleError(it)
            })

        compositeDisposable.add(disposable)
    }

    fun getComic(textInput: String) {
        textInput.toIntOrNull()?.let { id ->
            loadingLiveData.postValue(true)
            val disposable = handleComicRequest(comicInteractor.getComic(id))

            compositeDisposable.add(disposable)
        }
    }

    fun getNextComic() {
        var id = if (mainComicId >= latestComicId) {
            1
        } else {
            mainComicId + 1
        }
        loadingLiveData.postValue(true)
        val disposable = handleComicRequest(comicInteractor.getComic(id))

        compositeDisposable.add(disposable)
    }

    fun getPreviousComic() {
        var id = if (mainComicId <= 1) {
            latestComicId
        } else {
            mainComicId - 1
        }
        loadingLiveData.postValue(true)
        val disposable = handleComicRequest(comicInteractor.getComic(id))

        compositeDisposable.add(disposable)
    }

    fun getRandomComic() {
        loadingLiveData.postValue(true)
        val disposable = handleComicRequest(comicInteractor.getRandomComic(latestComicId))

        compositeDisposable.add(disposable)
    }

    fun onTapExplanation() {
        _openDetailsEvent.value = Event("https://www.explainxkcd.com/wiki/index.php/" + mainComicId)
    }

    private fun handleComicRequest(singleStream: Single<Comic>): Disposable
    {
        return singleStream
            .subscribeOn(schedulerFactory.io())
            .observeOn(schedulerFactory.main())
            .subscribe({ comic ->
                loadingLiveData.value = false
                handleComicLoaded(comic)
            }, {
                loadingLiveData.value = false
                handleError(it)
            })
    }

    private fun handleComicLoaded(comic: Comic) {
        mainComicUrlLiveData.value = comic.img
        comicTitleLiveData.value = comic.title
        comicDescriptionLiveData.value = comic.alt
        mainComicId = comic.num
    }

    private fun handleError(throwable: Throwable) {
        Log.println(Log.ERROR, "error", throwable.toString())
        errorLiveData.postValue(throwable.toString())
    }

    fun onViewDestroy() {
        compositeDisposable.clear()
    }
}
