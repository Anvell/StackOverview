package io.github.anvell.stackoverview.network

import io.github.anvell.stackoverview.model.QuestionsResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface StackOverflowService {

    @GET("/2.2/search?order=desc&sort=relevance&site=stackoverflow")
    fun searchQuestions(@Query("intitle") query: String): Observable<QuestionsResponse>
}