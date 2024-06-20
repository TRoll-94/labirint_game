package com.labirint.game

import androidx.compose.ui.geometry.Size
import kotlin.random.Random

//var testMap = listOf(
//    listOf(10, 8, 10, 9),
//    listOf(28, 1, 0, 12),
//    listOf(12, 10, 9, 13),
//    listOf(6, 5, 6, 5),
//)

var testMap = generateRandomField(15, 15, 0..100)

fun generateRandomField(rows: Int, cols: Int, range: IntRange): List<List<Int>> {
    return List(rows) {
        List(cols) {
            Random.nextInt(range.first, range.last + 1)
        }
    }
}

class GameField(
    var size: FieldSize = FieldSize(4, 4),
) {
    var field: List<List<FieldCell>> = listOf()
    var currentCell: FieldCell = FieldCell(
        number = 0,
        position = CellPosition(0, 0),
        size = size
    )

    fun generateField() {
        field = testMap.mapIndexed { y, row ->
            row.mapIndexed { x, cell ->
                FieldCell(
                    number = cell,
                    position = CellPosition(x, y),
                    size = size
                )
            }
        }
    }

    init {
        generateField()
        currentCell = findStartCell()
        size = FieldSize(field[0].size, field.size)
    }

    fun findCellByPosition(position: CellPosition): FieldCell {
        return field[position.y][position.x]
    }

    fun findStartCell(): FieldCell {
        val goodCells = field.flatten().filter {
            val directions: CellPossibleDirections = Coreutils.cellDirection(it)
            directions.bit4by2 && (directions.up || directions.down || directions.left || directions.right)
        }

        return goodCells.random()
    }

    fun move(direction: CellDirection) {
        val newPosition = when (direction) {
            CellDirection.UP -> CellPosition(currentCell.position.x, currentCell.position.y - 1)
            CellDirection.DOWN -> CellPosition(currentCell.position.x, currentCell.position.y + 1)
            CellDirection.LEFT -> CellPosition(currentCell.position.x - 1, currentCell.position.y)
            CellDirection.RIGHT -> CellPosition(currentCell.position.x + 1, currentCell.position.y)
        }
        val newCell = findCellByPosition(newPosition)
        currentCell = newCell
    }

}