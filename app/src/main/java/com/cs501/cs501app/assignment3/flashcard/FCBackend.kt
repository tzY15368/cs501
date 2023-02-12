package com.cs501.cs501app.assignment3.flashcard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import java.math.BigDecimal
import java.util.*

const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"
const val CURRENT_SCORE_KEY = "CURRENT_SCORE_KEY"

class FCBackend(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    val problemList = mutableListOf<FCProblem>()
    val answerList = mutableListOf<Float>()
    private val rangeOp1Start = 1.0f
    private val rangeOp1End = 99.0f
    private val rangeOp2Start = 1.0f
    private val rangeOp2End = 20.0f
    private val decimalPlaces = 1
    private val format = "%.${decimalPlaces}f"
    private val random = Random()

    private var current_index: Int
        get() = savedStateHandle.get(CURRENT_INDEX_KEY) ?: 0
        set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)

    private var current_score: Int
        get() = savedStateHandle.get(CURRENT_SCORE_KEY) ?: 0
        set(value) = savedStateHandle.set(CURRENT_SCORE_KEY, value)

    /**
     * get current problem, it will automatically jump to next problem for next call.
     *
     * @return FCProblem object.
     */
    fun getCurrentProblem(): FCProblem {
        if (current_index >= 10) {
            return problemList.get(9)
        }
        return problemList.get(current_index)
    }

    /**
     * check if the user's answer is correct.
     *
     * @param targetAnswer user's answer.
     * @return if the answer is correct.
     */
    fun checkAnswer(targetAnswer: String): Boolean {
        val expectAnswer = answerList.get(current_index).toString()
        val target = BigDecimal(targetAnswer).stripTrailingZeros()
        if (target.toString().length > expectAnswer.length) {
            current_index++
            return false
        }
        val targetAnswer = target.setScale(1).toString()
        val isCorrect = BigDecimal(expectAnswer) == BigDecimal(targetAnswer)
        current_index++
        if (isCorrect) current_score++
        return isCorrect
    }

    fun hasNextProblem(): Boolean {
        return current_index < 10
    }

    /**
     * generate all problems for each game, call this function if user want to begin a new game (don't need to clear state, it will call automatically)
     *
     * @return a list of problem, each type is FCProblem object
     */
    fun generateTenProblems(): MutableList<FCProblem> {
        clearAllList()
        for (i in 0 until 10) {
            val genereatedProblem = generateOneProblem(i)
            problemList.add(i, genereatedProblem)
            val answer: Float = if (genereatedProblem.isPlus) {
                (BigDecimal(genereatedProblem.operand1) + BigDecimal(genereatedProblem.operand2)).toFloat()
            } else {
                (BigDecimal(genereatedProblem.operand1) - BigDecimal(genereatedProblem.operand2)).toFloat()
            }
            answerList.add(i, answer)
        }
        return problemList
    }

    /**
     * inner function to support generateTenProblems()
     *
     * @param index problem number, from 0 - 9
     * @return a FCProblem object
     */
    private fun generateOneProblem(index: Int): FCProblem {
        val randomFloat1 = random.nextFloat() * (rangeOp1End - rangeOp1Start) + rangeOp1Start
        val formattedFloat1 = format.format(randomFloat1)
        val op1 = formattedFloat1.toFloat().toString()

        val randomFloat2 = random.nextFloat() * (rangeOp2End - rangeOp2Start) + rangeOp2Start
        val formattedFloat2 = format.format(randomFloat2)
        val op2 = formattedFloat2.toFloat().toString()

        val isPlus = ((random.nextInt(2) % 2) == 0)
        val problem = FCProblem(
            index = index,
            operand1 = op1,
            operand2 = op2,
            isPlus = isPlus
        )
        return problem
    }

    /**
     * get current problem index, from 0 - 9
     *
     * @return current problem index
     */
    fun getCurrentIndex(): Int {
        return current_index
    }

    /**
     * get current score for the game, from 0 - 10
     *
     * @return current game score
     */
    fun getCurrentScore(): Int {
        return current_score
    }

    /**
     * reset state for each game, call automatically from generateTenProblems()
     */
    private fun clearAllList() {
        problemList.clear()
        answerList.clear()
        current_score = 0
        current_index = 0
    }

}