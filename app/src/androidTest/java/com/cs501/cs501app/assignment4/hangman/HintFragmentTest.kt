package com.cs501.cs501app.assignment4.hangman

import com.cs501.cs501app.R
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


class HintFragmentTest {
    private val clickCount = 0
    @Before
    fun launchFragment() {
        FragmentScenario.launchInContainer(HintFragment::class.java)
    }

    @Test
    fun testButtonDisabledAfter3Clicks() {
        // Find the button and click it three times
        onView(withId(R.id.hintButton))
            .perform(click())
            .perform(click())
            .perform(click())

        // Verify that the button is disabled
        onView(withId(R.id.hintButton))
            .check(
                ViewAssertions.matches(
                    CoreMatchers.not(ViewMatchers.isEnabled())
                )
            )
    }

    companion object {
        private const val MAX_CLICKS = 3
    }
}