package com.labirint.game


class FieldCell(
    val number: Int,
    val position: CellPosition,
    val code: String = "${number}_${position.x}_${position.y}"
) {

    fun copy(
        number: Int = this.number,
        position: CellPosition = this.position,
        code: String = this.code
    ): FieldCell {
        return FieldCell(number, position, code)
    }
}