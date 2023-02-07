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

@RunWith(AndroidJUnit4::class)
class Calc1ActivityTest {
    private lateinit var scenario: ActivityScenario<Calc1Activity>

    @Before
    fun setUp() {
        scenario = launch(Calc1Activity::class.java)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun showsPlusOperation() {
        onView(withId(R.id.edit_op1)).perform(typeText("2"))
        onView(withId(R.id.edit_op2)).perform(typeText("3"), closeSoftKeyboard())
        onView(withId(R.id.spinnerOperator)).perform(click())
        onData(anything()).atPosition(0).perform(click())
        onView(withId(R.id.btn_op)).perform(click())
        onView(withId(R.id.result_text)).check(matches(withText("5")))
    }

    @Test
    fun showsSubOperation() {
        onView(withId(R.id.edit_op1)).perform(typeText("3"))
        onView(withId(R.id.edit_op2)).perform(typeText("2"), closeSoftKeyboard())
        onView(withId(R.id.spinnerOperator)).perform(click())
        onData(anything()).atPosition(1).perform(click())
        onView(withId(R.id.btn_op)).perform(click())
        onView(withId(R.id.result_text)).check(matches(withText("1")))
    }

    @Test
    fun showsMultiOperation() {
        onView(withId(R.id.edit_op1)).perform(typeText("2"))
        onView(withId(R.id.edit_op2)).perform(typeText("3"), closeSoftKeyboard())
        onView(withId(R.id.spinnerOperator)).perform(click())
        onData(anything()).atPosition(2).perform(click())
        onView(withId(R.id.btn_op)).perform(click())
        onView(withId(R.id.result_text)).check(matches(withText("6")))
    }

    @Test
    fun showsDivOperation() {
        onView(withId(R.id.edit_op1)).perform(typeText("4"))
        onView(withId(R.id.edit_op2)).perform(typeText("2"), closeSoftKeyboard())
        onView(withId(R.id.spinnerOperator)).perform(click())
        onData(anything()).atPosition(3).perform(click())
        onView(withId(R.id.btn_op)).perform(click())
        onView(withId(R.id.result_text)).check(matches(withText("2")))
    }

    @Test
    fun showsModOperation() {
        onView(withId(R.id.edit_op1)).perform(typeText("3"))
        onView(withId(R.id.edit_op2)).perform(typeText("2"), closeSoftKeyboard())
        onView(withId(R.id.spinnerOperator)).perform(click())
        onData(anything()).atPosition(4).perform(click())
        onView(withId(R.id.btn_op)).perform(click())
        onView(withId(R.id.result_text)).check(matches(withText("1")))
    }
}