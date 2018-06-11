package io.github.anvell.stackoverview.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import io.github.anvell.stackoverview.enumeration.ActiveScreen
import io.github.anvell.stackoverview.model.Question
import io.github.anvell.stackoverview.model.QuestionDetails
import io.github.anvell.stackoverview.model.QuestionsResponse
import io.github.anvell.stackoverview.model.ResponseBase
import io.github.anvell.stackoverview.repository.StackOverflowRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.net.URLDecoder
import java.util.concurrent.TimeUnit

class MainViewModel : ViewModel() {

    val activeScreen = MutableLiveData<ActiveScreen>()
    val selectedQuestion = MutableLiveData<QuestionDetails>()
    val questions = MutableLiveData<MutableList<Question>>()
    private val querySubject = PublishSubject.create<String>()
    private val repository = StackOverflowRepository()

    init {
        activeScreen.value = ActiveScreen.SEARCH
        querySubject.debounce(300, TimeUnit.MILLISECONDS)
                .filter { it.isNotEmpty() }
                .distinctUntilChanged()
                .switchMap { repository.searchQuestions(it) }
                .subscribe { onDataResponse(it) }
    }

    fun connectionStatus() = repository.connectionAvailable
    fun submitQuery(query: String) = querySubject.onNext(query)

    fun requestQuestion(id: Int) {
        repository.requestQuestion(id)
                  .subscribe { r -> onDataResponse(r) }
    }

    private fun onDataResponse(data: ResponseBase<QuestionDetails>) {
        if(data.items.isNotEmpty()) {
            selectedQuestion.value = data.items.first()
            activeScreen.value = ActiveScreen.DETAILS
        }
    }

    private fun onDataResponse(data: QuestionsResponse) {
        if(data.items.isNotEmpty()) {
            questions.value = data.items.toMutableList()
        }
    }

}