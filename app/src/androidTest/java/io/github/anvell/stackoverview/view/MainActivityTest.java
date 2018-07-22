package io.github.anvell.stackoverview.view;

import android.os.SystemClock;
import android.support.test.espresso.Espresso.*;
import android.support.test.espresso.action.ViewActions.*;
import android.support.test.espresso.assertion.ViewAssertions.*;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers.*;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.widget.AutoCompleteTextView;
import com.squareup.rx2.idler.Rx2Idler;
import io.github.anvell.stackoverview.R;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
class MainActivityTest {

/*
    @Rule
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    companion object {

        private const val testQuery = "Kotlin"

        @JvmStatic
        @BeforeClass
        fun setupClass() {
            RxAndroidPlugins.setInitMainThreadSchedulerHandler(Rx2Idler.create("RxMainScheduler"))
            RxJavaPlugins.setInitIoSchedulerHandler(Rx2Idler.create("RxIoScheduler"))
            RxJavaPlugins.setInitComputationSchedulerHandler(Rx2Idler.create("RxComputationScheduler"))
            RxJavaPlugins.setInitNewThreadSchedulerHandler(Rx2Idler.create("RxNewThreadScheduler"))
            RxJavaPlugins.setInitSingleSchedulerHandler(Rx2Idler.create("RxSingleScheduler"))
        }
    }

    @Test
    fun mainActivityTest() {

        onView(withId(R.id.mi_search_view)).check(matches(isDisplayed()))
        onView(withId(R.id.mi_search_view)).perform(click())

        onView(isAssignableFrom(AutoCompleteTextView::class.java)).perform(typeText(testQuery), pressImeActionButton())
        SystemClock.sleep(1500)

        onView(withId(R.id.searchResults)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))

        onView(withId(R.id.searchResults)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click())
    }
*/

}
