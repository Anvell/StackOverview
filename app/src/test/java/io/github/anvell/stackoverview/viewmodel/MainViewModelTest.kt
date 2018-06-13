package io.github.anvell.stackoverview.viewmodel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import io.github.anvell.stackoverview.enumeration.ActiveScreen
import io.github.anvell.stackoverview.model.Question
import io.github.anvell.stackoverview.model.QuestionDetails
import io.github.anvell.stackoverview.model.QuestionsResponse
import io.github.anvell.stackoverview.model.ResponseBase
import io.github.anvell.stackoverview.repository.StackOverflowRepository
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class MainViewModelTest {

    private val testTitle = "FIRST"
    private val testTitleNext = "SECOND"
    private val testQuery = "QUERY"

    private lateinit var viewModel: MainViewModel

    @Mock
    private lateinit var testRepository: StackOverflowRepository

    // Ensure single thread for LiveData
    @Rule
    @JvmField
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {

        // Ensure single thread for RxJava
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

        testRepository = mock(StackOverflowRepository::class.java)
        `when`(testRepository.requestQuestion(1)).thenReturn(
                Single.just(ResponseBase(
                        listOf(QuestionDetails(title = testTitle))
                ))
        )

        `when`(testRepository.searchQuestions(testQuery, 1)).thenReturn(
                Observable.just(QuestionsResponse(
                       items = listOf(Question(title = testTitle)),
                       hasMore = true
                ))
        )

        `when`(testRepository.searchQuestions(testQuery, 2)).thenReturn(
                Observable.just(QuestionsResponse(
                        items = listOf(Question(title = testTitleNext)),
                        hasMore = true
                ))
        )

        viewModel = MainViewModel()
        viewModel.repository = testRepository
    }

    @Test
    fun submitQuery() {

        viewModel.submitQuery(testQuery)

        assertThat(viewModel.activeScreen.value, `is`(ActiveScreen.SEARCH))
        assertThat(viewModel.questions.value!!.first().title, `is`(testTitle))
    }

    @Test
    fun requestMore() {

        viewModel.submitQuery(testQuery)
        viewModel.requestMore()

        assertThat(viewModel.activeScreen.value, `is`(ActiveScreen.SEARCH))
        assertThat(viewModel.questions.value!![0].title, `is`(testTitle))
        assertThat(viewModel.questions.value!![1].title, `is`(testTitleNext))
    }

    @Test
    fun requestQuestion() {

        viewModel.requestQuestion(1)

        assertThat(viewModel.activeScreen.value, `is`(ActiveScreen.DETAILS))
        assertThat(viewModel.selectedQuestion.value!!.title, `is`(testTitle))
    }
}