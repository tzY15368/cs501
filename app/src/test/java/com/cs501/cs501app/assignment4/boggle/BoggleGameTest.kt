package com.cs501.cs501app.assignment4.boggle

import org.junit.Test
import java.io.File
import java.io.InputStream

class BoggleGameTest {
    @Test
    fun testBoggleGame() {
        // Read from assets
        val inputStream:InputStream = File("src/main/assets/words.txt").inputStream()
        val boggleGame = BoggleGame(3,3,inputStream, null)
        boggleGame.generateBoard()
        boggleGame.displayBoard()
        val validWords = boggleGame.findAllValidWords()
        for (word in validWords) {
            assert(boggleGame.isValidWord(word))
        }
    }
}