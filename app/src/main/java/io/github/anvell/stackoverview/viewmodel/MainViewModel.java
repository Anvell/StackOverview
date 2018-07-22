package io.github.anvell.stackoverview.viewmodel;

import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
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
    public StackOverflowRepository repository = new StackOverflowRepository();

    private PublishSubject<Query> querySubject = PublishSubject.create();
    private Query lastQuery = new Query("");
    private CompositeDisposable disposables = new CompositeDisposable();

    @SuppressLint("CheckResult")
    public MainViewModel() {
        isBusy.setValue(false);
        activeScreen.setValue(ActiveScreen.SEARCH);
        querySubject.debounce(DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .filter(it -> !it.query.isEmpty())
                .distinctUntilChanged()
                .doOnNext(x -> isBusy.postValue(true))
                .switchMap(it -> repository.searchQuestions(it.query, it.page))
                .doOnEach(x -> isBusy.postValue(false))
                .subscribe(this::onDataResponse);
    }

    public void clearSelectedQuestion() {
        selectedQuestion.setValue(null);
    }

    public MutableLiveData<Boolean> connectionStatus() {
        return repository.connectionAvailable;
    }

    public void submitQuery(String query) {
        if(Utils.isNotBlank(query) && !lastQuery.query.equalsIgnoreCase(query)) {
            lastQuery = new Query(query);
            querySubject.onNext(lastQuery);
        }
    }

    public void requestMore() {
        if(lastQuery.hasMore) {
            lastQuery = new Query(lastQuery.query, true, lastQuery.page+1);
            querySubject.onNext(lastQuery);
        }
    }

    public void requestQuestion(int id) {
        disposables.clear();

        disposables.add(
            repository.requestQuestion(id)
                      .doOnSubscribe(x -> isBusy.postValue(true))
                      .doFinally(() -> isBusy.postValue(false))
                      .subscribe (this::onDataResponse,
                                  t -> {/* TODO: Handle different exceptions */})
        );
    }

    private void onDataResponse(ResponseBase<QuestionDetails> data) {
        if(!data.items.isEmpty()) {
            selectedQuestion.setValue(data.items.get(0));
            activeScreen.setValue(ActiveScreen.DETAILS);
        }
    }

    private void onDataResponse(QuestionsResponse data) {
        if(!data.items.isEmpty()) {
            lastQuery.hasMore = data.hasMore;

            if(lastQuery.page > 1) {
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