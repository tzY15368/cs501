package com.cs501.cs501app.assignment3.flashcard

import androidx.lifecycle.SavedStateHandle
import org.junit.Assert.*

import org.junit.Test
import java.math.BigDecimal

class FCBackendTest {

    @Test
    fun getCurrentProblem() {
        val savedStateHandle = SavedStateHandle()
        val fcBackend = FCBackend(savedStateHandle)
        fcBackend.generateTenProblems()
        assertEquals(0, fcBackend.getCurrentProblem().index)

    }
    @Test
    fun checkAnswer() {
        val savedStateHandle = SavedStateHandle()
        val fcBackend = FCBackend(savedStateHandle)
        fcBackend.generateTenProblems()
        val problem = fcBackend.getCurrentProblem()
        println(problem)
        val answer : String
        if (problem.isPlus) {
            val temp = BigDecimal(fcBackend.answerList.get(0).toString()).toInt()
            println(temp)
            println(fcBackend.answerList.get(0))
            assertEquals(true, fcBackend.checkAnswer(temp.toString()))
        } else {
            val temp = BigDecimal(fcBackend.answerList.get(0).toString()).toInt()
            println(temp)
            println(fcBackend.answerList.get(0))
            assertEquals(true, fcBackend.checkAnswer(temp.toString()))
        }
    }
}