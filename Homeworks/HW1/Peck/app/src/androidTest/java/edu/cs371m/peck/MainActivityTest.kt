package edu.cs371m.peck


import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Rule
import androidx.test.rule.ActivityTestRule
import org.junit.Before
import android.widget.TextView
import androidx.test.internal.util.Checks
import kotlinx.android.synthetic.main.content_main.*
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @Before
    fun prepare() {
        val activity = mActivityRule.activity
        activity.testing = true
    }

    @Rule
    @JvmField
    var mActivityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun test_startGame() {
        onView(withId(R.id.button)).perform(ViewActions.click())
        onView(withId(R.id.sentence)).check(matches(withText("after the visit was paid she")))
        onView(withId(R.id.frame)).check(matches(hasChildCount(6)))
    }

    @Test
    fun test_clickCorrectWord() {
        onView(withId(R.id.button)).perform(ViewActions.click())

        onView(withId(R.id.frame)).check(matches(hasChildCount(6)))
        onView(withId(R.id.text_id_1)).perform(ViewActions.click())
        onView(withId(R.id.frame)).check(matches(hasChildCount(5)))
    }

    @Test
    fun test_clickWrongWord() {
        onView(withId(R.id.button)).perform(ViewActions.click())

        onView(withId(R.id.frame)).check(matches(hasChildCount(6)))
        onView(withId(R.id.text_id_3)).perform(ViewActions.click())
        onView(withId(R.id.frame)).check(matches(hasChildCount(6)))
    }

    @Test
    fun test_loseGame() {
        onView(withId(R.id.button)).perform(ViewActions.click())

        onView(withId(R.id.frame)).check(matches(hasChildCount(6)))
        onView(withId(R.id.text_id_1)).perform(ViewActions.click())
        Thread.sleep(3000)
        onView(withId(R.id.text_id_2)).perform(ViewActions.click())
        Thread.sleep(3000)
        onView(withId(R.id.text_id_3)).perform(ViewActions.click())
        Thread.sleep(3000)

        onView(withId(R.id.scoreTV)).check(matches(withText("0")))
        onView(withId(R.id.frame)).check(matches(hasChildCount(3)))
        onView(withId(R.id.button)).check(matches(withText("0")))
    }

    @Test
    fun test_winGame() {
        onView(withId(R.id.button)).perform(ViewActions.click())

        onView(withId(R.id.frame)).check(matches(hasChildCount(6)))
        onView(withId(R.id.text_id_1)).perform(ViewActions.click())
        onView(withId(R.id.text_id_2)).perform(ViewActions.click())
        onView(withId(R.id.text_id_3)).perform(ViewActions.click())
        onView(withId(R.id.text_id_4)).perform(ViewActions.click())
        onView(withId(R.id.text_id_5)).perform(ViewActions.click())
        onView(withId(R.id.text_id_6)).perform(ViewActions.click())

        Thread.sleep(10)

        onView(withId(R.id.scoreTV)).check(matches(withText("1")))
        onView(withId(R.id.frame)).check(matches(hasChildCount(0)))
    }

    @Test
    fun test_multipleGames() {
        onView(withId(R.id.button)).perform(ViewActions.click())

        onView(withId(R.id.frame)).check(matches(hasChildCount(6)))
        onView(withId(R.id.text_id_1)).perform(ViewActions.click())
        onView(withId(R.id.text_id_2)).perform(ViewActions.click())
        onView(withId(R.id.text_id_3)).perform(ViewActions.click())
        onView(withId(R.id.text_id_4)).perform(ViewActions.click())
        onView(withId(R.id.text_id_5)).perform(ViewActions.click())
        onView(withId(R.id.text_id_6)).perform(ViewActions.click())

        Thread.sleep(10)

        onView(withId(R.id.scoreTV)).check(matches(withText("1")))
        onView(withId(R.id.frame)).check(matches(hasChildCount(0)))

        onView(withId(R.id.button)).perform(ViewActions.click())

        onView(withId(R.id.frame)).check(matches(hasChildCount(6)))
        onView(withId(R.id.text_id_1)).perform(ViewActions.click())
        onView(withId(R.id.text_id_2)).perform(ViewActions.click())
        onView(withId(R.id.text_id_3)).perform(ViewActions.click())
        onView(withId(R.id.text_id_4)).perform(ViewActions.click())
        onView(withId(R.id.text_id_5)).perform(ViewActions.click())
        onView(withId(R.id.text_id_6)).perform(ViewActions.click())

        Thread.sleep(10)

        onView(withId(R.id.scoreTV)).check(matches(withText("2")))
        onView(withId(R.id.frame)).check(matches(hasChildCount(0)))
    }
}
