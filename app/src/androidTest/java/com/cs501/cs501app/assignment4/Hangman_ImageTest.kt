package com.cs501.cs501app.assignment4


import com.cs501.cs501app.R
import org.junit.Assert.*
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cs501.cs501app.assignment4.hangman.HangManActivity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class Hangman_ImageTest {
    private lateinit var scenario: ActivityScenario<HangManActivity>

    @Before
    fun setUp() {
        scenario = launch(HangManActivity::class.java)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun showSixPictures() {
        onView(withId(R.id.hangman_btn_a)).perform(click())
        onView(withId(R.id.hangman_btn_b)).perform(click())
        onView(withId(R.id.hangman_btn_c)).perform(click())
        onView(withId(R.id.hangman_btn_d)).perform(click())
        onView(withId(R.id.hangman_btn_e)).perform(click())
        onView(withId(R.id.hangman_btn_f)).perform(click())
        onView(withId(R.id.hangman_btn_g)).perform(click())
        onView(withId(R.id.resetButton)).perform(click())
    }

}