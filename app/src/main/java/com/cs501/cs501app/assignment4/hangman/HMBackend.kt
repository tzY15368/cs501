package com.cs501.cs501app.assignment4.hangman

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class HMBackend(private val savedStateHandle: SavedStateHandle): ViewModel() {
    // if vowels don't ever display with hints, set it as false.
    // in input fragment, detect this variable to set vowels display
    private var disabledVowels = false
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
    fun hasWon(): Boolean{}
}