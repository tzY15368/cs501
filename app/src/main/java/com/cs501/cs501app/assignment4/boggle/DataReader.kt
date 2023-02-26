package com.cs501.cs501app.assignment4.boggle

import android.content.Context
import androidx.core.content.ContentProviderCompat.requireContext
import com.cs501.cs501app.R
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader

class DataReader(private val inStream: InputStream, private val wordLength: Int, private val context: Context) {
    private val data = ArrayList<String>()

    init {
        readData()
//        inStream.close()
    }

    private fun readData() {
        // create buffered reader from instream

//        val reader = inStream.bufferedReader()
//        var line = reader.readLine()
//        while (line != null) {
//            if (isValidWord(line)) {
//                data.add(line.lowercase())
//            }
//            line = reader.readLine()
//        }
//        reader.close()

//        val contents = inStream.bufferedReader().use { it.readText() }
//        for (line in contents.lines()) {
//            if (isValidWord(line)) {
//                data.add(line.lowercase())
//            }
//        }
        val inputStream = context.assets.open("words.txt")
        val reader = BufferedReader(InputStreamReader(inputStream))
        val words = ArrayList<String>()
        var line = reader.readLine()
        while (line != null) {
            if (isValidWord(line)) {
                data.add(line.lowercase())
//                println(data.size)
            }
            line = reader.readLine()
        }

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
//fun main() {
//    // FIXME: WIP: this is incorrect. Fix this
//    val inStream = File("app/src/main/java/com/cs501/cs501app/assignment4/boggle/words.txt").inputStream()
//    val reader = DataReader(inStream, 4)
//    println(reader.getData())
//    println(reader.getRandWord())
//    println(reader.getRandWordList(16))
//}