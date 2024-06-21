package com.labirint.game

import java.util.*

class LabirintGenerator {
    var WIDTH = 7
    private var START = 21
    private var END = 0

    private val DIRECTIONS = arrayOf(
        intArrayOf(0, -1),   // left
        intArrayOf(-1, 0),   // top
        intArrayOf(0, 1),    // right
        intArrayOf(1, 0),    // button
    )

    private fun isValidMove(newRow: Int, newCol: Int, moves: Int, direction: IntArray): Boolean {
        if (newRow < 0 || newRow >= WIDTH || newCol < 0 || newCol >= WIDTH) return false
        if (direction[0] == 1 && (moves and 0b1000) != 0) return true
        if (direction[0] == -1 && (moves and 0b0100) != 0) return true
        if (direction[1] == 1 && (moves and 0b0010) != 0) return true
        if (direction[1] == -1 && (moves and 0b0001) != 0) return true
        return false
    }

    private fun determineNextMove(number: Int): Int {
        return number and 0b1111
    }

    private fun isPathValid(array: Array<IntArray>, row: Int, col: Int, visited: Array<BooleanArray>): Boolean {
        if (array[row][col] == 0) return true

        visited[row][col] = true
        val moves = determineNextMove(array[row][col])

        for (direction in DIRECTIONS) {
            val newRow = row + direction[0]
            val newCol = col + direction[1]
            if (isValidMove(newRow, newCol, moves, direction) && !visited[newRow][newCol]) {
                if (isPathValid(array, newRow, newCol, visited)) {
                    return true
                }
            }
        }
        visited[row][col] = false
        return false
    }

    fun generateArray(): Array<IntArray> {
        val array = Array(WIDTH) { IntArray(WIDTH) }
        val rand = Random()

        val numbers = (1..START).filter { it != 16 }.shuffled().toMutableList()

        val posStartRow = rand.nextInt(WIDTH)
        val posStartCol = rand.nextInt(WIDTH)
        var posFinishRow = rand.nextInt(WIDTH)
        var posFinishCol = rand.nextInt(WIDTH)

        while (posStartRow == posFinishRow && posStartCol == posFinishCol) {
            posFinishRow = rand.nextInt(WIDTH)
            posFinishCol = rand.nextInt(WIDTH)
        }

        array[posStartRow][posStartCol] = START
        array[posFinishRow][posFinishCol] = END

        var index = 0
        for (i in 0 until WIDTH) {
            for (j in 0 until WIDTH) {
                if ((i != posStartRow || j != posStartCol) && (i != posFinishRow || j != posFinishCol)) {
                    array[i][j] = numbers[index % numbers.size]
                    index++
                }
            }
        }

        while (!isPathValid(array, posStartRow, posStartCol, Array(WIDTH) { BooleanArray(WIDTH) })) {
            numbers.shuffle()
            index = 0
            for (i in 0 until WIDTH) {
                for (j in 0 until WIDTH) {
                    if ((i != posStartRow || j != posStartCol) && (i != posFinishRow || j != posFinishCol)) {
                        array[i][j] = numbers[index % numbers.size]
                        index++
                    }
                }
            }
        }
        return array
    }
}
