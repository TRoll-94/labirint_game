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

    fun cellDirection(cell: FieldCell): CellPossibleDirections {
        val directions = CellPossibleDirections(
            up = (cell.number and 4) != 0 && cell.position.y > 0,
            down = (cell.number and 8) != 0 && cell.position.y < cell.size.height - 1,
            left = (cell.number and 1) != 0 && cell.position.x > 0,
            right = (cell.number and 2) != 0 && cell.position.x < cell.size.width - 1,
            bit4by2 = (cell.number and 16) != 0
        )
        return directions
    }

}