package com.labirint.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.labirint.game.*
import com.labirint.util.ProjectColors
import com.labirint.util.dpToPx


data class DrawGameCanvasModifiers (
    val size: Dp = 150.dp,
    val visibleNestedCells: Int = 2,
    val showDiagonals: Boolean = true,
    val isInteractive: Boolean = false,
    val showBorders: Boolean = true,
)


@Composable
fun drawGameMinimap(
    gameField: GameField,
    modifier: Modifier = Modifier,
    gameModifiers: DrawGameCanvasModifiers = DrawGameCanvasModifiers(),
) {
    val textMeasurer = rememberTextMeasurer()
    val gameFieldInner = getNearCells(gameField, gameModifiers = gameModifiers)
    val cellSize = dpToPx(gameModifiers.size) / gameFieldInner.size.width
    Canvas(
        modifier = modifier.size(gameModifiers.size),
    ) {
        gameFieldInner.field.flatten().forEachIndexed { index, cell ->
            val alpha = calculateAlpha(cell, gameField)
            val x = index % gameFieldInner.size.width
            val y = index / gameFieldInner.size.width
            drawIntoCanvas {
                withTransform({
                    translate(
                        left = x * cellSize,
                        top = y * cellSize,
                    )
                }) {
                    drawRect(
                        color = if (cell.number == 0) {
                            ProjectColors.secondary
                        } else {
                            ProjectColors.miniMapCell.copy(alpha = alpha)
                        },
                        size = Size(cellSize, cellSize)
                    )
                    drawRect(
                        color = if (cell.number == 0) {
                            ProjectColors.secondary
                        } else {
                            ProjectColors.miniMapCell
                        },
                        size = Size(cellSize + 1, cellSize + 1),
                        style = Stroke(cap = StrokeCap.Square)
                    )
                    val textToDraw = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = if (cell.position == gameFieldInner.currentCell.position || cell.number == 0) {
                                    Color.White
                                } else {
                                    Color.Gray
                                },
                                fontSize = 12.sp,
                            )
                        ) {
                            append(if (cell.number == -1) " " else cell.number.toString())
                            append("\n")
                        }
                        withStyle(
                            style = SpanStyle(
                                color = Color.White,
                                fontSize = 8.sp,
                            )
                        ) {
                            if (cell.number != -1) {
                                val directions = Coreutils.cellDirection(cell)
                                append(if (directions.up) "U" else "-")
                                append(if (directions.down) "D" else "-")
                                append(if (directions.left) "L" else "-")
                                append(if (directions.right) "R" else "-")
                            }
                        }
                    }
                    drawText(
                        textMeasurer = textMeasurer,
                        text = textToDraw,
                        topLeft = Offset(
                            x = 3f,
                            y = 1f,
                        )
                    )
                }
            }
        }
    }
}


fun getNearCells(gameField: GameField, gameModifiers: DrawGameCanvasModifiers): GameField {
    val currentCell = gameField.currentCell.position
    val result = GameField()
    val field = gameField.field

    val xStart = currentCell.x - gameModifiers.visibleNestedCells
    val xEnd = currentCell.x + gameModifiers.visibleNestedCells
    val yStart = currentCell.y - gameModifiers.visibleNestedCells
    val yEnd = currentCell.y + gameModifiers.visibleNestedCells

    val cells = mutableListOf<List<FieldCell>>()

    for (y in yStart..yEnd) {
        val row = mutableListOf<FieldCell>()
        for (x in xStart..xEnd) {
            if (x < 0 || y < 0 || x >= field[0].size || y >= field.size) {
                row.add(FieldCell(number = -1, position = CellPosition(x, y), size = FieldSize(0, 0)))
            } else {
                row.add(field[y][x])
            }
        }
        cells.add(row)
    }
    result.field = cells
    result.currentCell = result.findCellByPosition(CellPosition(gameModifiers.visibleNestedCells, gameModifiers.visibleNestedCells))
    result.size = FieldSize(result.field[0].size, result.field.size)
    return result

}

private fun calculateAlpha(cell: FieldCell, gameField: GameField): Float {
    if (cell.number == -1) {
        return 0.0f
    }
    val distance = kotlin.math.abs(cell.position.x - gameField.currentCell.position.x) +
            kotlin.math.abs(cell.position.y - gameField.currentCell.position.y)
    val directions = Coreutils.cellDirection(gameField.currentCell)

    if (cell.number == 0) {
        return 0.9f
    } else if (distance == 1) {
        if (cell.position.x == gameField.currentCell.position.x) {
            if (cell.position.y == gameField.currentCell.position.y - 1) {
                return if (directions.up) .8f else 0.0f
            }
            if (cell.position.y == gameField.currentCell.position.y + 1) {
                return if (directions.down) .8f else 0.0f
            }
        }
        if (cell.position.y == gameField.currentCell.position.y) {
            if (cell.position.x == gameField.currentCell.position.x - 1) {
                return if (directions.left) .8f else 0.0f
            }
            if (cell.position.x == gameField.currentCell.position.x + 1) {
                return if (directions.right) .8f else 0.0f
            }
        }
    } else if (distance == 0) {
        return 1.0f
    }
    return 0.0f

//    return when (distance) {
//        0 -> 1.0f
//        1 -> 0.8f
//        else -> 0.2f
//    }
}