package com.cs501.cs501app.assignment4.boggle

import androidx.fragment.app.testing.FragmentScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.cs501.cs501app.R
import com.cs501.cs501app.assignment4.hangman.HintFragment
import org.hamcrest.CoreMatchers
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

class BoggleLowerFragmentTest {

    @Before
    fun launchFragment() {
        FragmentScenario.launchInContainer(BoggleLowerFragment::class.java)
    }

    @Test
    fun testButtonDisabledAfter3Clicks() {
        // Find the button and click it three times
        Espresso.onView(ViewMatchers.withId(R.id.boggle_new_game))
            .perform(ViewActions.click())

        // Verify that the button is disabled
        Espresso.onView(ViewMatchers.withId(R.id.cur_score))
            .check(
                ViewAssertions.matches(ViewMatchers.withText(""))
            )
    }
}