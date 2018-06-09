package io.github.anvell.stackoverview.repository

import io.github.anvell.stackoverview.model.QuestionsResponse
import io.github.anvell.stackoverview.network.ApiClient
import io.reactivex.Observable

class StackOverflowRepository() {

    fun searchQuestions(query: String) : Observable<QuestionsResponse>
        = ApiClient.client.searchQuestions(query)

}