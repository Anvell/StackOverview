package io.github.anvell.stackoverview.viewmodel;

import android.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;

import io.github.anvell.stackoverview.enumeration.ActiveScreen;
import io.github.anvell.stackoverview.model.Question;
import io.github.anvell.stackoverview.model.QuestionDetails;
import io.github.anvell.stackoverview.model.QuestionsResponse;
import io.github.anvell.stackoverview.model.ResponseBase;
import io.github.anvell.stackoverview.repository.StackOverflowRepository;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class MainViewModelTest {

    private static final String testTitle = "FIRST";
    private static final String testTitleNext = "SECOND";
    private static final String testQuery = "QUERY";

    private MainViewModel viewModel;

    @Mock
    private StackOverflowRepository testRepository;

    // Ensure single thread for LiveData
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() {

        // Ensure single thread for RxJava
        RxJavaPlugins.setIoSchedulerHandler(it -> Schedulers.trampoline());
        RxJavaPlugins.setComputationSchedulerHandler(it -> Schedulers.trampoline());
        RxJavaPlugins.setNewThreadSchedulerHandler(it -> Schedulers.trampoline());
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(it -> Schedulers.trampoline());

        QuestionDetails details = new QuestionDetails();
        details.title = testTitle;

        testRepository = mock(StackOverflowRepository.class);
        when(testRepository.requestQuestion(1)).thenReturn(
                Single.just(new ResponseBase<>(
                        new QuestionDetails[] { details }
                ))
        );

        Question question = new Question();
        question.title = testTitle;
        QuestionsResponse response = new QuestionsResponse();
        response.hasMore = true;
        response.items.add(question);

        when(testRepository.searchQuestions(testQuery, 1)).thenReturn(
                Observable.just(response)
        );

        question = new Question();
        question.title = testTitleNext;
        response = new QuestionsResponse();
        response.hasMore = true;
        response.items.add(question);

        when(testRepository.searchQuestions(testQuery, 2)).thenReturn(
                Observable.just(response)
        );

        viewModel = new MainViewModel();
        viewModel.repository = testRepository;
    }

    @Test
    public void submitQuery() {

        viewModel.submitQuery(testQuery);

        assertEquals(viewModel.activeScreen.getValue(), ActiveScreen.SEARCH);
        assertEquals(viewModel.questions.getValue().get(0).title, testTitle);
    }

    @Test
    public void requestMore() {

        viewModel.submitQuery(testQuery);
        viewModel.requestMore();

        assertEquals(viewModel.activeScreen.getValue(), ActiveScreen.SEARCH);
        assertEquals(viewModel.questions.getValue().get(0).title, testTitle);
        assertEquals(viewModel.questions.getValue().get(1).title, testTitleNext);
    }

    @Test
    public void requestQuestion() {

        viewModel.requestQuestion(1);

        assertEquals(viewModel.activeScreen.getValue(), ActiveScreen.DETAILS);
        assertEquals(viewModel.selectedQuestion.getValue().title, testTitle);
    }
}
