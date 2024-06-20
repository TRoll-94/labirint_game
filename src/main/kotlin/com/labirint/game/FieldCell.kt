package com.labirint.game


class FieldCell(
    val number: Int,
    val position: CellPosition,
    val size: FieldSize,
) {
    val directions: CellPossibleDirections = Coreutils.cellDirection(this)
}