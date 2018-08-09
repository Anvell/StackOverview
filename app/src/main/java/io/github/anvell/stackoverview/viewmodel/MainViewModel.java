package io.github.anvell.stackoverview.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.github.anvell.stackoverview.enumeration.ActiveScreen;
import io.github.anvell.stackoverview.extension.Utils;
import io.github.anvell.stackoverview.model.Query;
import io.github.anvell.stackoverview.model.Question;
import io.github.anvell.stackoverview.model.QuestionDetails;
import io.github.anvell.stackoverview.model.QuestionsResponse;
import io.github.anvell.stackoverview.model.ResponseBase;
import io.github.anvell.stackoverview.repository.StackOverflowRepository;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;

public class MainViewModel extends ViewModel {

    private static final int DEFAULT_DELAY = 300;

    public MutableLiveData<Boolean> isBusy = new MutableLiveData<>();
    public MutableLiveData<ActiveScreen> activeScreen = new MutableLiveData<>();
    public MutableLiveData<QuestionDetails> selectedQuestion = new MutableLiveData<>();
    public MutableLiveData<ArrayList<Question>> questions = new MutableLiveData<>();
    public LiveData<List<Question>> questionsCollection;

    private PublishSubject<Query> querySubject = PublishSubject.create();
    private Query lastQuery = new Query("");
    private CompositeDisposable disposables = new CompositeDisposable();
    public StackOverflowRepository repository = StackOverflowRepository.getInstance();

    @SuppressLint("CheckResult")
    public MainViewModel() {

        isBusy.setValue(false);
        activeScreen.setValue(ActiveScreen.COLLECTION);
        querySubject.debounce(DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .filter(it -> !it.query.isEmpty())
                .distinctUntilChanged()
                .doOnNext(x -> isBusy.postValue(true))
                .switchMap(it -> repository.searchQuestions(it.query, it.page))
                .doOnEach(x -> isBusy.postValue(false))
                .subscribe(this::onDataResponse);

        AsyncTask.execute(() -> questionsCollection = getQuestionsCollection());
    }

    private LiveData<List<Question>> getQuestionsCollection() {
        return repository.getQuestionsCollection();
    }

    public void storeSelectedQuestion() {
        QuestionDetails q = selectedQuestion.getValue();

        if (q != null) q.isFavorite = true;
        AsyncTask.execute(() -> repository.storeQuestion(q));
    }

    public void deleteSelectedQuestion() {
        QuestionDetails q = selectedQuestion.getValue();

        if (q != null) {
            q.isFavorite = false;
            AsyncTask.execute(() -> repository.deleteQuestion(q.questionId));
        }
    }

    private Question getQuestionById(int id) {
        return repository.getQuestionById(id);
    }

    public void clearSelectedQuestion() {
        selectedQuestion.setValue(null);
    }

    public MutableLiveData<Boolean> connectionStatus() {
        return repository.connectionAvailable;
    }

    public void submitQuery(String query) {
        if (Utils.isNotBlank(query)) {
            lastQuery = new Query(query);
            querySubject.onNext(lastQuery);
        }
    }

    public void requestMore() {
        if (lastQuery.hasMore) {
            lastQuery = new Query(lastQuery.query, true, lastQuery.page + 1);
            querySubject.onNext(lastQuery);
        }
    }

    public void requestQuestion(int id) {
        disposables.clear();

        disposables.add(
                repository.requestQuestion(id)
                        .doOnSubscribe(x -> isBusy.postValue(true))
                        .doFinally(() -> isBusy.postValue(false))
                        .subscribe(this::onDataResponse,
                                t -> {/* TODO: Handle different exceptions */})
        );
    }

    // Everything here is done on back Thread, so we can pull data from Room
    private void onDataResponse(ResponseBase<QuestionDetails> data) {
        if (!data.items.isEmpty()) {
            QuestionDetails q = data.items.get(0);
            q.updateAnswerCount();

            if (getQuestionById(q.questionId) != null) {
                q.isFavorite = true;
            }

            selectedQuestion.postValue(q);
            activeScreen.postValue(ActiveScreen.DETAILS);
        }
    }

    private void onDataResponse(QuestionsResponse data) {
        if (!data.items.isEmpty()) {
            lastQuery.hasMore = data.hasMore;

            if (lastQuery.page > 1) {
                ArrayList<Question> newData = questions.getValue();

                if (newData != null) {
                    newData.addAll(data.items);
                    questions.setValue(newData);
                }
            } else {
                questions.setValue(data.items);
            }
            activeScreen.setValue(ActiveScreen.SEARCH);
        }
    }
}