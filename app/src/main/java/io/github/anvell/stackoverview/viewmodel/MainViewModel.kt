package io.github.anvell.stackoverview.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import io.github.anvell.stackoverview.model.Question
import io.github.anvell.stackoverview.model.QuestionsResponse
import io.github.anvell.stackoverview.repository.StackOverflowRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class MainViewModel : ViewModel() {

    val selectedQuestion = MutableLiveData<Question>()
    val questions = MutableLiveData<MutableList<Question>>()
    private val querySubject = PublishSubject.create<String>()
    private val repository = StackOverflowRepository()

    init {
        querySubject.debounce(300, TimeUnit.MILLISECONDS)
                .filter { it.isNotEmpty() }
                .distinctUntilChanged()
                .switchMap { repository.searchQuestions(it) }
                .subscribe { onDataResponse(it) }
    }

    fun connectionStatus() = repository.connectionAvailable
    fun submitQuery(query: String) = querySubject.onNext(query)

    private fun onDataResponse(data: QuestionsResponse) {
        if(data.items.isNotEmpty()) {
            questions.value = data.items.toMutableList()
        }
    }

}