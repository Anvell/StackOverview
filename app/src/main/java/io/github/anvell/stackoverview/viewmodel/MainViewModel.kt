package io.github.anvell.stackoverview.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.github.anvell.stackoverview.model.Question

class MainViewModel : ViewModel() {

    var selectedQuestion = MutableLiveData<Question>()
    var questions = MutableLiveData<MutableList<Question>>()

    fun submitQuery(query: String) {

    }
}