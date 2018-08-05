package io.github.anvell.stackoverview.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.Handler;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.github.anvell.stackoverview.database.AppDatabase;
import io.github.anvell.stackoverview.database.QuestionDao;
import io.github.anvell.stackoverview.model.Question;
import io.github.anvell.stackoverview.model.QuestionDetails;
import io.github.anvell.stackoverview.model.QuestionsResponse;
import io.github.anvell.stackoverview.model.ResponseBase;
import io.github.anvell.stackoverview.network.ApiClient;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;

public class StackOverflowRepository {

    private static long REQUEST_DELAY = 3000;

    private QuestionDao userCollection;
    public MutableLiveData<Boolean> connectionAvailable = new MutableLiveData<>();

    private static volatile StackOverflowRepository instance = null;

    public static void initialize(@NonNull Application application) {
        if(instance == null) {
            instance = new StackOverflowRepository(application);
        }
    }

    public static StackOverflowRepository getInstance() {
        return instance;
    }

    private StackOverflowRepository(@NonNull Application application) {
        connectionAvailable.setValue(true);
        userCollection = AppDatabase.get(application).questionDao();
    }

    private void setConnectionStatus(Boolean available) {
        if (available != connectionAvailable.getValue()) {
            connectionAvailable.postValue(available);
        }
    }

    public Call<QuestionsResponse> requestHotQuestions() {
        return ApiClient.getClient().requestHotQuestions();
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
                .observeOn(Schedulers.io())
                .doOnError(t -> setConnectionStatus(false))
                .doAfterSuccess(n -> setConnectionStatus(true));
    }

    public LiveData<List<Question>> getQuestionsCollection() {
        return userCollection.loadAll();
    }

    public void storeQuestion(Question q) {
        if(q != null) {
            userCollection.store(q);
        }
    }

    public Question getQuestionById(int id) {
        return userCollection.load(id);
    }

    public void deleteQuestion(int id) {
        userCollection.delete(id);
    }
}