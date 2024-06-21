package com.labirint.game

import androidx.compose.runtime.Composable
import com.labirint.util.Difficulty


class GameField(
    var size: FieldSize = FieldSize(4, 4),
    var field: List<List<FieldCell>> = listOf(),
) {
    var labirintGenerator = LabirintGenerator()

    fun generateField() {
        labirintGenerator.WIDTH = Difficulty.getSizeByDifficulty()
        val testMap = labirintGenerator.generateArray()
        field = testMap.mapIndexed { y, row ->
            row.mapIndexed { x, cell ->
                FieldCell(
                    number = cell,
                    position = CellPosition(x, y),
                )
            }
        }
    }

    fun init(): GameField {
        size = FieldSize(labirintGenerator.WIDTH, labirintGenerator.WIDTH)
        generateField()
        return this
    }

    @Composable
    fun findCellByPosition(position: CellPosition): FieldCell {
        return field[position.y][position.x]
    }

    fun findCellById(id: String): FieldCell {
        return field.flatten().first { it.code == id }
    }

    fun findStartCell(): FieldCell {
        println("MIAN findStartCell()")
        val goodCells = field.flatten().filter {
            val directions: CellPossibleDirections = Coreutils.cellDirection(it, this)
            directions.bit4by2 && (directions.up || directions.down || directions.left || directions.right)
        }

        return goodCells.random()
    }

    fun getDistanceTo(cell: FieldCell, currentCell: FieldCell): Int {
        return kotlin.math.abs(cell.position.x - currentCell.position.x) +
                kotlin.math.abs(cell.position.y - currentCell.position.y)
    }

    fun isPossibleCellMove(cell: FieldCell, currentCell: FieldCell): Boolean {
        val distance = getDistanceTo(cell, currentCell)
        val directions = Coreutils.cellDirection(currentCell, this)
        if (distance == 1) {
            if (cell.position.x == currentCell.position.x) {
                if (cell.position.y == currentCell.position.y - 1) {
                    return directions.up
                }
                if (cell.position.y == currentCell.position.y + 1) {
                    return directions.down
                }
            }
            if (cell.position.y == currentCell.position.y) {
                if (cell.position.x == currentCell.position.x - 1) {
                    return directions.left
                }
                if (cell.position.x == currentCell.position.x + 1) {
                    return directions.right
                }
            }
        }
        return false
    }


}