package io.github.anvell.stackoverview.repository

import android.arch.lifecycle.MutableLiveData
import io.github.anvell.stackoverview.model.QuestionDetails
import io.github.anvell.stackoverview.model.QuestionsResponse
import io.github.anvell.stackoverview.model.ResponseBase
import io.github.anvell.stackoverview.network.ApiClient
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class StackOverflowRepository {

    companion object {
        private const val REQUEST_DELAY = 3000L
    }

    val connectionAvailable = MutableLiveData<Boolean>()

    init {
        connectionAvailable.value = true
    }

    private fun setConnectionStatus(available: Boolean) {
        connectionAvailable.value?.let {
            if(available != it) connectionAvailable.value = available
        }
    }

    fun searchQuestions(query: String, page: Int): Observable<QuestionsResponse> {
        return ApiClient.client.searchQuestions(query, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { setConnectionStatus(false) }
                .doOnNext { setConnectionStatus(true) }
                .retryWhen { it.delay(REQUEST_DELAY, TimeUnit.MILLISECONDS) }
    }

    fun requestQuestion(id: Int): Single<ResponseBase<QuestionDetails>> {
        return ApiClient.client.requestQuestion(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { setConnectionStatus(false) }
                .doAfterSuccess { setConnectionStatus(true) }
    }

}