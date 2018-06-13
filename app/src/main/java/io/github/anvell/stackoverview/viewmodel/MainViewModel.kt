package io.github.anvell.stackoverview.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.github.anvell.stackoverview.enumeration.ActiveScreen
import io.github.anvell.stackoverview.model.Question
import io.github.anvell.stackoverview.model.QuestionDetails
import io.github.anvell.stackoverview.model.QuestionsResponse
import io.github.anvell.stackoverview.model.ResponseBase
import io.github.anvell.stackoverview.repository.StackOverflowRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class MainViewModel : ViewModel() {

    val isBusy = MutableLiveData<Boolean>()
    val activeScreen = MutableLiveData<ActiveScreen>()
    val selectedQuestion = MutableLiveData<QuestionDetails?>()
    val questions = MutableLiveData<MutableList<Question>>()
    var repository = StackOverflowRepository()
    private val querySubject = PublishSubject.create<Query>()
    private var lastQuery = Query()
    private val disposables = CompositeDisposable()

    init {
        isBusy.value = false
        activeScreen.value = ActiveScreen.SEARCH
        querySubject.debounce(300, TimeUnit.MILLISECONDS)
                .filter { it.query.isNotEmpty() }
                .distinctUntilChanged()
                .doOnNext { isBusy.postValue(true) }
                .switchMap { repository.searchQuestions(it.query, it.page) }
                .doOnEach { isBusy.postValue(false) }
                .subscribe { onDataResponse(it) }
    }

    fun clearSelectedQuestion() {
        selectedQuestion.value = null
    }

    fun connectionStatus() = repository.connectionAvailable

    fun submitQuery(query: String) {
        if(query.isNotBlank() && lastQuery.query != query) {
            lastQuery = Query(query)
            querySubject.onNext(lastQuery)
        }
    }

    fun requestMore() {
        if(lastQuery.hasMore) {
            lastQuery = Query(lastQuery.query, lastQuery.hasMore, lastQuery.page+1)
            querySubject.onNext(lastQuery)
        }
    }

    fun requestQuestion(id: Int) {
        disposables.clear()

        disposables.add(
            repository.requestQuestion(id)
                      .doOnSubscribe { isBusy.postValue(true) }
                      .doFinally { isBusy.postValue(false) }
                      .subscribe ({ r -> onDataResponse(r) },
                       { /* TODO: Handle different exceptions */ })
        )
    }

    private fun onDataResponse(data: ResponseBase<QuestionDetails>) {
        if(data.items.isNotEmpty()) {
            selectedQuestion.value = data.items.first()
            activeScreen.value = ActiveScreen.DETAILS
        }
    }

    private fun onDataResponse(data: QuestionsResponse) {
        if(data.items.isNotEmpty()) {
            lastQuery.hasMore = data.hasMore

            if(lastQuery.page > 1) {
                val newData = questions.value
                newData?.addAll(data.items)
                questions.value = newData
            } else {
                questions.value = data.items.toMutableList()
            }
            activeScreen.value = ActiveScreen.SEARCH
        }
    }

    private inner class Query(var query: String = "",
                              var hasMore: Boolean = false,
                              var page: Int = 1)
}