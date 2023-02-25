package com.cs501.cs501app.assignment4.boggle

import android.content.Context
import java.io.InputStream

class BoggleGame(
    private val boardSize: Int = 4,
    private val wordLength: Int = 4,
    inputStream: InputStream,
    private val context: Context
) {
    private var board = Array(boardSize) { Array(boardSize) { ' ' } }
    private var score = 0
    public var answer = ""
    private var answerMap = HashMap<String, Boolean>()
    private val dataReader = DataReader(inputStream, wordLength, context)

    init {
        answer = ""
        score = 0
        answerMap = HashMap<String, Boolean>()
        generateBoard()
    }

    fun generateBoard() {
        // fill the rest of the board with random letters
        for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                if (board[i][j] == ' ') {
                    board[i][j] = ('a'..'z').random()
                }
            }
        }
        // generate with at least 1 valid word
        // get a random valid word and place it on the random connected positions on board
        val randWord = dataReader.getRandWord()
        var curX = (0 until boardSize).random()
        var curY = (0 until boardSize).random()
        var curLen = 0
        val history = ArrayList<Pair<Int, Int>>()
        while (curLen < randWord.length) {
            val randDir = intArrayOf((0..3).random())
            val directions = arrayOf(
                Pair(0, 1),
                Pair(0, -1),
                Pair(1, 0),
                Pair(-1, 0)
            )
            val nextX = curX + directions[randDir[0]].first
            val nextY = curY + directions[randDir[0]].second
            if (nextX in 0 until boardSize && nextY in 0 until boardSize) {
                if (!history.contains(Pair(nextX, nextY))) {
                    board[curX][curY] = randWord[curLen]
                    history.add(Pair(curX, curY))
                    curLen++
                    curX = nextX
                    curY = nextY
                }
            }
        }
    }

    private fun isValidWord(currentWord: String): Boolean {
        return dataReader.getData().contains(currentWord)
    }

    fun findAllValidWords(): Set<String> {
        val validWords = mutableSetOf<String>()
        val visited = Array(4) { BooleanArray(4) }
        var count = 0

        fun searchWords(
            row: Int,
            col: Int,
            visited: Array<BooleanArray>,
            currentWord: String,
        ) {
            count++
            val newWord = currentWord + board[row][col]
            // check if the current word is valid
            if (newWord.length == wordLength) {
                if (isValidWord(newWord)) {
                    validWords.add(newWord)
                }
                return
            }
            if (currentWord.length > wordLength) {
                return
            }
            // search for words starting from the current cell
            if (row in 0 until boardSize && col in 0 until boardSize && !visited[row][col]) {
                val newVisited = visited.map { it.clone() }.toTypedArray()
                newVisited[row][col] = true
                // search in all eight directions from the current cell
                for (i in -1..1) {
                    for (j in -1..1) {
                        if (i == 0 && j == 0) {
                            continue
                        }
                        if (row + i in 0 until boardSize && col + j in 0 until boardSize)
                            searchWords(row + i, col + j, newVisited, newWord)
                    }
                }
            }
        }

        for (i in 0 until 4) {
            for (j in 0 until 4) {
                searchWords(i, j, visited, "")
            }
        }
        return validWords
    }

    fun getScore(): Int {
        return score
    }

    fun setScore(new_score: Int) {
        score = new_score
    }

    fun getBoard(): Array<Array<Char>> {
        return board
    }

    fun getBoardByPosition(x : Int, y : Int): Char {
        return board[x][y]
    }

    fun displayBoard() {
        for (i in 0 until boardSize) {
            println("-" + "--".repeat(boardSize))
            for (j in 0 until boardSize) {
                print("|" + board[i][j])
            }
            println("|")
        }
        println("-" + "--".repeat(boardSize))
    }

    fun checkAnswer(): Boolean {
        if (isValidWord(answer) && (!answerMap.containsKey(answer) || answerMap[answer] == false)) {
            answerMap[answer] = true
            // 5 points for each vowel, 1 point for each consonant
            for (c in answer) {
                val vowel = setOf('a', 'e', 'i', 'o', 'u')
                score += if (c in vowel) {
                    5
                } else {
                    1
                }
            }
            // If use one or more of the consonants ‘S’, ‘Z’, ‘P’, ‘X’ or ‘Q’ the value of the word is doubled
            val doubleScore = setOf('s', 'z', 'p', 'x', 'q')
            for (c in answer) {
                if (c in doubleScore) {
                    score *= 2
                    break
                }
            }
            return true
        }
        // if the word is not valid or already used, reduce 10 points
        score -= 10
        return false
    }

    fun addAnswer(newChar: Char) {
        answer += newChar.lowercase()
    }

    fun clearAnswer() {
        answer = ""
    }

}

// unit test
//fun main() {
//    // FIXME: THIS WILL NOT WORK OUTSIDE OF ANDROID classes
//    val game = BoggleGame()
//    game.displayBoard()
//    println(game.findAllValidWords())
//}