package com.cs501.cs501app.assignment4.hangman

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

const val MAX_HP = 6;

enum class GameState {
    WIN, LOSE, IN_PROGRESS
}

class HMBackend(application: Application): AndroidViewModel(application) {




    private val vowels = arrayOf("a","e","i","o","u")

    /* 10 animals */
    // translate previous line into kotlin
    private val animals = arrayOf("bear","bee","bird","cat","crab","dolphin","cow","duck","snake","frog")
    /* 10 foods*/
    private val foods = arrayOf("rice","soup","pasta","noodles","corn","bread","apple","butter","beef","pork")
    /* 10 music*/
    private val instruments = arrayOf("guitar","violin","viola","cello","bass","piano","drums","clarinet","flute","trumpet")

    private val categories = arrayOf("animals", "food", "music")

    private val wordMap = mapOf(
        "animals" to animals,
        "food" to foods,
        "music" to instruments
    )

    // a list of all letters
    val remainingLetters = MutableLiveData(('a'..'z').toList())
    val gameState = MutableLiveData(GameState.IN_PROGRESS)
    private val _currentHP = MutableLiveData(MAX_HP)
    val hint = MutableLiveData("")
    fun getCurrentHP(): LiveData<Int>{return _currentHP}
    fun decrCurrentHP() {_currentHP.value = _currentHP.value?.minus(1)}
    fun setCurrentHP(hp: Int) {_currentHP.value = hp}

    // random int
    private var selectedCategory = MutableLiveData(categories.random())

    // random word
    private var selectedWord = MutableLiveData(wordMap[selectedCategory.value]!!.random())

    // repeated underscores with length of word
    val currentDisplayWord = MutableLiveData(getSelectedWord().replace("[a-zA-Z]".toRegex(), "_"))

    private var currentIndex = MutableLiveData(0)

    private var hintCount = MutableLiveData(0)

    fun getCurrentDisplayWord(): String{return currentDisplayWord.value!!}
    fun getSelectedWord(): String{return selectedWord.value!!}
    fun getRemainingLetters(): List<Char>{return remainingLetters.value!!}
    fun getCurrentIndex(): Int{return currentIndex.value!!}

    fun reset(){
        println("resetting")
        setCurrentHP(MAX_HP)

        currentIndex.value = 0
        hintCount.value = 0
        gameState.value = GameState.IN_PROGRESS
        hint.value = ""
        selectedCategory.value = categories.random()
        selectedWord.value = wordMap[selectedCategory.value]!!.random()
        currentDisplayWord.value = selectedWord.value!!.replace("[a-zA-Z]".toRegex(), "_")
        remainingLetters.value =('a'..'z').toList()
    }

    private fun disableChars(){
        // disables half of the remaining letters
        decrCurrentHP()
        // generate a map of all the letters in a-z
        val alphabet = ('a'..'z').toList()
        // generate a list of all the letters in the word
        val displayLetters = getCurrentDisplayWord().toList()
        val wordLetters = getSelectedWord().toList()
        // generate a list of all the letters not in the word
        val notWordLet = alphabet.filter { !displayLetters.contains(it) }
        val notWordLetters = alphabet.filter { !displayLetters.contains(it) }
        val notWordLetters2 = notWordLetters.filter { !wordLetters.contains(it) }
        // randomly drop half of the letters
        val disabledLetters = notWordLetters2.shuffled().drop(notWordLetters2.size / 2)
        remainingLetters.value = getRemainingLetters().filter { !disabledLetters.contains(it) }
    }
    private fun showVowels(){
        decrCurrentHP()
        // loop through the word by index
        for(i in 0 until getSelectedWord().length){
            // if the index is even, replace the underscore with the letter
            if(vowels.contains(""+getSelectedWord()[i])){
                val b = currentDisplayWord.value?.toCharArray()
                b?.set(i, getSelectedWord()[i])
                currentDisplayWord.value = b.toString()
            }
        }
    }

    // returns the hint for the current word, this changes state in the backend
    fun getHint(){
        hintCount.value = hintCount.value?.plus(1)
        Log.d("hint", "getHint: ${hintCount.value}")
        if (hintCount.value == 1) {
            hint.value = "The category is " + selectedCategory.value.toString()
        } else if (hintCount.value == 2) {
            disableChars()
            hint.value =   "disabled half of the remaining letters"
        } else if (hintCount.value == 3) {
            showVowels()
            hint.value =  "showed all vowels"
        }else {
            hint.value = "no more hints"
        }
        checkState()
    }

    private fun checkState(){
        if(currentIndex.value==-1){
            println("end of game")
            gameState.value = GameState.WIN
            hint.value = "You win!"
            return
        }
        if(getCurrentHP().value==0) {
            println("end of game")
            gameState.value = GameState.LOSE
            hint.value = "You lose!"
            return
        }
    }

    fun handleInput(_c:Char){
        var c = _c.lowercaseChar()
        println("input: $c")

        if(c==this.getSelectedWord()[getCurrentIndex()]){
            currentDisplayWord.value = getCurrentDisplayWord()
                .replaceRange(getCurrentIndex(), getCurrentIndex()+1, c.toString())
            currentIndex.value = getCurrentDisplayWord().indexOf('_')
            if(getCurrentIndex()!=-1 && !this.getSelectedWord().substring(getCurrentIndex()).contains(c)){
                remainingLetters.value = getRemainingLetters().filter { it != c }
            }
        } else {
            remainingLetters.value = getRemainingLetters().filter { it != c }
            decrCurrentHP()
        }
        println("current word: ${selectedWord.value}|${currentDisplayWord.value}, " +
                "current HP: ${getCurrentHP().value}, current index: ${currentIndex.value}")

        checkState()
    }
}
