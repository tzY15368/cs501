package com.cs501.cs501app.assignment4.boogle

import java.io.File

class DataReader(private val file: String, private val wordLength: Int) {
    private val data = ArrayList<String>()

    init {
        readData()
    }

    private fun readData() {
        val reader = File(file).bufferedReader()
        var line = reader.readLine()
        while (line != null) {
            if (isValidWord(line)) {
                data.add(line.lowercase())
            }
            line = reader.readLine()
        }
        reader.close()
    }

    private fun isValidWord(word: String): Boolean {
        if (word.length == wordLength) {
            // check if contains only letters
            for (c in word) {
                if (!c.isLetter()) {
                    return false
                }
            }
            // check if contains at least 2 vowels
            var vowelCount = 0
            val vowels = "aeiou"
            for (c in word) {
                if (vowels.contains(c)) {
                    vowelCount++
                }
            }
            if (vowelCount >= 2) {
                return true
            }
        }
        return false
    }

    fun getData(): ArrayList<String> {
        readData()
        return data
    }

    fun getRandWord(): String {
        val rand = (0..data.size).random()
        return data[rand]
    }

    fun getRandWordList(num: Int): ArrayList<String> {
        // return a list of random words without duplicates
        val randList = ArrayList<String>()
        while (randList.size < num) {
            val rand = getRandWord()
            if (!randList.contains(rand)) {
                randList.add(rand)
            }
        }
        return randList
    }

}

// unit test
fun main() {
    val reader = DataReader("app/src/main/java/com/cs501/cs501app/assignment4/boogle/words.txt", 4)
    println(reader.getData())
    println(reader.getRandWord())
    println(reader.getRandWordList(16))
}