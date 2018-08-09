package io.github.anvell.stackoverview.view;

import android.os.SystemClock;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.AutoCompleteTextView;

import com.squareup.rx2.idler.Rx2Idler;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.anvell.stackoverview.R;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule activityTestRule = new ActivityTestRule(MainActivity.class);

    private static final String testQuery = "Qwerty";

    @BeforeClass
    static void setupClass() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(Rx2Idler.create("RxMainScheduler"));
        RxJavaPlugins.setInitIoSchedulerHandler(Rx2Idler.create("RxIoScheduler"));
        RxJavaPlugins.setInitComputationSchedulerHandler(Rx2Idler.create("RxComputationScheduler"));
        RxJavaPlugins.setInitNewThreadSchedulerHandler(Rx2Idler.create("RxNewThreadScheduler"));
        RxJavaPlugins.setInitSingleSchedulerHandler(Rx2Idler.create("RxSingleScheduler"));
    }

    @Test
    public void mainActivityTest() {

        onView(withId(R.id.mi_search_view)).check(matches(isDisplayed()));
        onView(withId(R.id.mi_search_view)).perform(click());

        onView(isAssignableFrom(AutoCompleteTextView.class)).perform(typeText(testQuery), pressImeActionButton());
        SystemClock.sleep(1500);

        onView(withId(R.id.searchResults)).perform(RecyclerViewActions.scrollToPosition(0));

        onView(withId(R.id.searchResults)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
    }

}
