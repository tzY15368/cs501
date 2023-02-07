package com.cs501.cs501app.assignment2

import android.text.InputType
import com.cs501.cs501app.R
import org.junit.Assert.*
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.anything
import org.hamcrest.CoreMatchers.containsString
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

class Calc2ActivityTest {
    private lateinit var scenario: ActivityScenario<Calc2Activity>
    @Before
    fun setUp() {
        scenario = ActivityScenario.launch(Calc2Activity::class.java)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun showsPlusOperation() {
        onView(withId(R.id.btn_2)).perform(click())
        onView(withId(R.id.btn_plus)).perform(click())
        onView(withId(R.id.btn_3)).perform(click())
        onView(withId(R.id.btn_equals)).perform(click())
        onView(withId(R.id.edit_advanced)).check(matches(withText("5")))
    }

    @Test
    fun showsSubOperation() {
        onView(withId(R.id.btn_3)).perform(click())
        onView(withId(R.id.btn_sub)).perform(click())
        onView(withId(R.id.btn_2)).perform(click())
        onView(withId(R.id.btn_equals)).perform(click())
        onView(withId(R.id.edit_advanced)).check(matches(withText("1")))
    }

    @Test
    fun showsMultiOperation() {
        onView(withId(R.id.btn_2)).perform(click())
        onView(withId(R.id.btn_multi)).perform(click())
        onView(withId(R.id.btn_3)).perform(click())
        onView(withId(R.id.btn_equals)).perform(click())
        onView(withId(R.id.edit_advanced)).check(matches(withText("6")))
    }

    @Test
    fun showsDivOperation() {
        onView(withId(R.id.btn_4)).perform(click())
        onView(withId(R.id.btn_div)).perform(click())
        onView(withId(R.id.btn_2)).perform(click())
        onView(withId(R.id.btn_equals)).perform(click())
        onView(withId(R.id.edit_advanced)).check(matches(withText("2")))
    }

    @Test
    fun showsModOperation() {
        onView(withId(R.id.btn_3)).perform(click())
        onView(withId(R.id.btn_mod)).perform(click())
        onView(withId(R.id.btn_2)).perform(click())
        onView(withId(R.id.btn_equals)).perform(click())
        onView(withId(R.id.edit_advanced)).check(matches(withText("1")))
    }

    @Test
    fun showsACOperation() {
        onView(withId(R.id.btn_2)).perform(click())
        onView(withId(R.id.btn_plus)).perform(click())
        onView(withId(R.id.btn_3)).perform(click())
        onView(withId(R.id.btn_ac)).perform(click())
        onView(withId(R.id.edit_advanced)).check(matches(withText("")))
    }

    @Test
    fun showsSqrtOperation() {
        onView(withId(R.id.btn_4)).perform(click())
        onView(withId(R.id.btn_sqrt)).perform(click())
        onView(withId(R.id.btn_equals)).perform(click())
        onView(withId(R.id.edit_advanced)).check(matches(withText("2")))
    }
}