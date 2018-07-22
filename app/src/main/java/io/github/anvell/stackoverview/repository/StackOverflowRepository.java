package io.github.anvell.stackoverview.repository;

import android.arch.lifecycle.MutableLiveData;

import java.util.concurrent.TimeUnit;

import io.github.anvell.stackoverview.model.QuestionDetails;
import io.github.anvell.stackoverview.model.QuestionsResponse;
import io.github.anvell.stackoverview.model.ResponseBase;
import io.github.anvell.stackoverview.network.ApiClient;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class StackOverflowRepository {

    private static long REQUEST_DELAY = 3000;

    public MutableLiveData<Boolean> connectionAvailable = new MutableLiveData<>();

    public StackOverflowRepository() {
        connectionAvailable.setValue(true);
    }

    private void setConnectionStatus(Boolean available) {
        if (available != connectionAvailable.getValue()) {
            connectionAvailable.setValue(available);
        }
    }

    public Observable<QuestionsResponse> searchQuestions(String query, int page) {
        return ApiClient.getClient().searchQuestions(query, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(t -> setConnectionStatus(false))
                .doOnNext(n -> setConnectionStatus(true))
                .retryWhen(e -> e.delay(REQUEST_DELAY, TimeUnit.MILLISECONDS));
    }

    public Single<ResponseBase<QuestionDetails>> requestQuestion(int id) {
        return ApiClient.getClient().requestQuestion(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(t -> setConnectionStatus(false))
                .doAfterSuccess(n -> setConnectionStatus(true));
    }

}