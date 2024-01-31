package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.retrofit.ApiFactory
import com.example.myapplication.enums.Sections
import com.example.myapplication.database.GifDatabase
import com.example.myapplication.models.Data
import com.example.myapplication.models.Datum
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = GifDatabase.getInstance(application).gifDao()
    private var currentIndex = -1
    val isInternetWorking = MutableLiveData<Boolean>()
    val isError = MutableLiveData<String>()
    val progress = MutableLiveData<Boolean>()

    private val compositeDisposable = CompositeDisposable()
    val displayGif = MutableLiveData<Data>()
    val canGoBack = MutableLiveData<Boolean>()

    private var cachedGifs: List<Data>? = null

    init {
        loadRandomGif(Sections.OVERALL)
        canGoBack.value = false
    }

    fun loadPreviousGif() {
        if (currentIndex <= 0) {
            return
        }
        currentIndex--
        displayGif.value = cachedGifs?.get(currentIndex)
        canGoBack.value = currentIndex > 0
    }

    fun loadRandomGif(section : Sections) {
        progress.value = false
        val gifsSize = cachedGifs?.size ?: 0

        if (currentIndex >= gifsSize - 1) {
            val disposableRandom = when(section) {
                Sections.KIDS -> kidRetrofit()
                Sections.ADULTS -> adultRetrofit()
                else -> overAllRetrofit()
            }
                .subscribeOn(Schedulers.io())
                .map { it.data }
                .doOnSubscribe {
                    progress.postValue(true)
                }
                .doOnSuccess {
                    saveGifToDatabase(it)
                    currentIndex++
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        progress.value = false
                        isInternetWorking.value = true
                        displayGif.value = it
                    }, {
                        isInternetWorking.value = false
                    }
                )
            canGoBack.value = currentIndex > 0
            compositeDisposable.add(disposableRandom)
        } else {
            currentIndex++
            displayGif.value = cachedGifs?.get(currentIndex)
            canGoBack.value = currentIndex > 0
        }
    }

    private fun overAllRetrofit() : Single<Datum> = ApiFactory.apiService.getGif()
    private fun kidRetrofit() : Single<Datum> = ApiFactory.apiService.getGifKids()
    private fun adultRetrofit() : Single<Datum> = ApiFactory.apiService.getGifAdults()

    private fun saveGifToDatabase(gif: Data) {
        val disposable = Single.fromCallable { database.insertGif(gif) }
            .subscribeOn(Schedulers.io())
            .flatMapCompletable {
                database.getAllGifs()
                    .observeOn(AndroidSchedulers.mainThread())
                    .map { cachedGifs = it }
                    .ignoreElements()
            }
            .subscribe({}, { isError.value = it.message })

        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}