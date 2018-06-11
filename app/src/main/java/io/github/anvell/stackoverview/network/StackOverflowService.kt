package io.github.anvell.stackoverview.network

import io.github.anvell.stackoverview.model.QuestionDetails
import io.github.anvell.stackoverview.model.QuestionsResponse
import io.github.anvell.stackoverview.model.ResponseBase
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StackOverflowService {

    @GET("/2.2/search?order=desc&sort=relevance&site=stackoverflow&filter=!4(Yr)(Wb2EFpO).L9")
    fun searchQuestions(@Query("intitle") query: String): Observable<QuestionsResponse>

    @GET("/2.2/questions/{id}?order=desc&sort=votes&site=stackoverflow&filter=!oDhDpauvKgSJFm30wCKlzQGXv4S5(X0t8hAcZa)sA_y")
    fun requestQuestion(@Path("id") id: Int): Single<ResponseBase<QuestionDetails>>

}