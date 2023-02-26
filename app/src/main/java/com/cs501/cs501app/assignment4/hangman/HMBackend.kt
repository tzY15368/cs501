package com.cs501.cs501app.assignment4.hangman

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class HMBackend(private val savedStateHandle: SavedStateHandle): ViewModel() {
    // if vowels don't ever display with hints, set it as false.
    // in input fragment, detect this variable to set vowels display
    private var disabledVowels = false
    var randomWordArray = charArrayOf() // word selected put into char array
    private var hangmanBody: Array<ImageView?> = Array(6) { null }
    var countWrong = 0 // number wrong
    var wordSelectedUnderscoresArr =
        java.util.ArrayList<Char>() // displayed string of underscores arraylist
    private var wordSelected // word selected textview
            : TextView? = null
    var counthint = 0
    var category: String? = null
    // returns the hint for the current word, this could change state in the backend
    fun getHint(): String {

        return ""

    }
//
//    // returns the player's current HP, 0 for dead (hanged/lost), max value is 6.
    fun getHP(): Int{}
    fun setHP(hp : Int){}
//
//    // returns if s is the correct character to add
    fun addChar(s: Char): Boolean{}
//
//    // returns available chars (excluding ones players already used as well as disabled)
    fun getAvailableChars(): List<Char> {}
    fun setAvailableChars(avaliableChars: List<Char>){}
    fun getDisabledVowels() : Boolean{}
    fun setDisabledVowels(disabledVowelsState: Boolean) {}

    //
//    // returns the current word with blanks for unguessed chars
    fun getWord(): String{}
//
//    // returns if the player has won
    //fun hasWon(): Boolean{}
//    fun hasWon(): Boolean

    public fun gameEnd(): Boolean{
        if (countWrong == hangmanBody.size) {
           return true;
        }
        return false;
    }

    public fun hasWon(): Boolean{
        // whole word completed
        var contains = false
        for (c in wordSelectedUnderscoresArr) {
            if (c == '_') {
                contains = true
                break
            }
        }
        return !contains;
    }

    public fun getHint():String{
        counthint++
        if (counthint < 2) {
           return category.toString()
        }
        return "no more hint"

    }

    public fun getHP(): Int{
        return 6 - countWrong
    }



    public fun letterChecker(fromButton: Char) {
        // linear search to check if input letter is in the word
        var check = false
        for (c in randomWordArray) {
            if (c == fromButton) {
                check = true
            }
        }
        val indexList: MutableList<Int> = ArrayList()
        if (!check) {
            // negative value means not in the word -> wrong -> set next body part visible

            //set body part visible
            hangmanBody[countWrong]!!.visibility = View.VISIBLE

            countWrong++

            if(gameEnd()){
                //todo: make toast
            }
        } else {
            // else user got it right -> update underscore(s)

            // getting the array indexes of letter instance in word
            for (iter in randomWordArray.indices) {
                if (randomWordArray.get(iter) == fromButton) {
                    indexList.add(iter)
                }
            }

            // updating the array of underscores
            for (iter in indexList.indices) {
                wordSelectedUnderscoresArr.set(indexList[iter], fromButton)
            }
            val sb = StringBuilder()
            for (ch in wordSelectedUnderscoresArr) {
                sb.append(ch)
            }
            // TODO: set words selected

            // whole word completed
            hasWon()
        }
    }
}