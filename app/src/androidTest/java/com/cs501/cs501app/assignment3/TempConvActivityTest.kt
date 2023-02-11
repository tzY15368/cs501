package com.cs501.cs501app.assignment3

import android.view.View
import android.widget.SeekBar
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import com.cs501.cs501app.R
import com.cs501.cs501app.assignment3.tempconv.TempConvActivity
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.Description


class TempConvActivityTest {
    private lateinit var scenario: ActivityScenario<TempConvActivity>
    @Before
    fun setUp() {
        scenario = ActivityScenario.launch(TempConvActivity::class.java)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun moveSeekBarCF() {
        onView(withId(R.id.seekBarC)).perform(setProgress(10))
        onView(withId(R.id.textViewTempF)).check(matches(withProgress(50)))
    }
    @Test
    fun moveSeekBarFC() {
        onView(withId(R.id.seekBarF)).perform(setProgress(50))
        onView(withId(R.id.textViewTempC)).check(matches(withProgress(10)))
    }

    private fun setProgress(progress: Int): ViewAction {
        return object : ViewAction {
            override fun perform(uiController: UiController, view: View) {
                val seekBar = view as SeekBar
                seekBar.progress = progress
            }

            override fun getDescription(): String {
                return "Set a progress on a SeekBar"
            }

            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(SeekBar::class.java)
            }
        }
    }

    private fun withProgress(expectedProgress: Int): Matcher<View?> {
        return object : BoundedMatcher<View?, SeekBar>(SeekBar::class.java) {

            override fun matchesSafely(seekBar: SeekBar): Boolean {
                return seekBar.progress == expectedProgress
            }

            override fun describeTo(description: org.hamcrest.Description?) {
                description?.appendText("expected: ");
                description?.appendText(""+expectedProgress);
            }
        }
    }

}