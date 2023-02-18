package com.cs501.cs501app.assignment4.hangman

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class HMBackend(private val savedStateHandle: SavedStateHandle): ViewModel() {
    // returns the hint for the current word, this could change state in the backend
//    fun getHint(): String
//
//    // returns the player's current HP, 0 for dead (hanged/lost), max value is 6.
//    fun getHP(): Int
//
//    // returns if s is the correct character to add
//    fun addChar(s: Char): Boolean
//
//    // returns available chars (excluding ones players already used)
//    fun getAvailableChars(): List<Char>
//
//    // returns the current word with blanks for unguessed chars
//    fun getWord(): String
//
//    // returns if the player has won
//    fun hasWon(): Boolean
}