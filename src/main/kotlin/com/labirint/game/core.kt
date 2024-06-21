package com.labirint.game


enum class CellDirection {
    UP, DOWN, LEFT, RIGHT
}

data class CellPossibleDirections (
    var up: Boolean = false,
    var down: Boolean = false,
    var left: Boolean = false,
    var right: Boolean = false,
    var bit4by2: Boolean = false
)

data class CellPosition (
    var x: Int,
    var y: Int
)

data class FieldSize (
    var width: Int,
    var height: Int
)


object Coreutils {

    fun cellDirection(cell: FieldCell, gameField: GameField): CellPossibleDirections {
        val directions = CellPossibleDirections(
            up = (cell.number and 0b0100) != 0 && cell.position.y > 0,
            down = (cell.number and 0b1000) != 0 && cell.position.y < gameField.size.height - 1,
            left = (cell.number and 0b0001) != 0 && cell.position.x > 0,
            right = (cell.number and 0b0010) != 0 && cell.position.x < gameField.size.width - 1,
            bit4by2 = (cell.number and 16) != 0
        )
        return directions
    }

}