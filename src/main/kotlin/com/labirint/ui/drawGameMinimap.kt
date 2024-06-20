package com.labirint.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerMoveFilter
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


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun drawGameMinimap(
    gameField: GameField,
    currentCell: FieldCell,
    modifier: Modifier = Modifier,
    gameModifiers: DrawGameCanvasModifiers = DrawGameCanvasModifiers(),
    onCellClick: (GameField, FieldCell) -> Unit = { _, _ -> }
) {
    val textMeasurer = rememberTextMeasurer()
    val gameFieldInner = getNearCells(gameField, gameModifiers = gameModifiers)
    val cellSize = dpToPx(gameModifiers.size) / gameFieldInner.size.width
    var mousePosition by remember { mutableStateOf(Offset.Unspecified) }
    Canvas(
        modifier = modifier.size(gameModifiers.size).pointerMoveFilter(
            onMove = {
                if (gameModifiers.isInteractive) {
                    mousePosition = it
                }
                true
            },
            onExit = {
                mousePosition = Offset.Unspecified
                false
            }
        ).pointerInput(Unit) {
            detectTapGestures { offset ->
                if (!gameModifiers.isInteractive) {
                    return@detectTapGestures
                } else {
                    val clickedCell = getCellAtPosition(offset, cellSize, gameFieldInner)
                    clickedCell?.let { onCellClick(gameFieldInner, it) }
                }
            }
        },

    ) {
        gameFieldInner.field.flatten().forEachIndexed { index, cell ->
            val alpha = calculateAlpha(cell, gameField)
            val x = index % gameFieldInner.size.width
            val y = index / gameFieldInner.size.width
            val cellPosition = Offset(x.toFloat(), y.toFloat())
            val isMouseOver = isCursorInCell(mousePosition, cellPosition, cellSize)
            drawIntoCanvas {
                withTransform({
                    translate(
                        left = x * cellSize,
                        top = y * cellSize,
                    )
                }) {
                    drawRect(
                        color = getCellBgColor(gameFieldInner, cell, isMouseOver, alpha),
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
                                color = if (cell.position == currentCell.position || cell.number == 0) {
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
            if (!gameModifiers.showDiagonals && (x != currentCell.x && y != currentCell.y)) {
                row.add(FieldCell(number = -1, position = CellPosition(x, y), size = FieldSize(0, 0)))
                continue
            }
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

fun getCellAtPosition(offset: Offset, cellSizePx: Float, gameField: GameField): FieldCell? {
    val x = (offset.x / cellSizePx).toInt()
    val y = (offset.y / cellSizePx).toInt()

    return gameField.field.getOrNull(y)?.getOrNull(x)
}

fun isCursorInCell(cursorPosition: Offset, cellPosition: Offset, cellSize: Float): Boolean {
    if (cursorPosition == Offset.Unspecified) {
        return false
    }
    return cursorPosition.x >= cellPosition.x * cellSize &&
            cursorPosition.x <= (cellPosition.x + 1) * cellSize &&
            cursorPosition.y >= cellPosition.y * cellSize &&
            cursorPosition.y <= (cellPosition.y + 1) * cellSize
}

fun getCellBgColor(gameField: GameField, cell: FieldCell, isMouseOver: Boolean, alpha: Float): Color {
    return when {
        cell.number == 0 -> ProjectColors.secondary
        (cell.number == -1 && isMouseOver) -> ProjectColors.error.copy(alpha = 0.2f)
        (cell == gameField.currentCell) -> ProjectColors.miniMapCell.copy(alpha = alpha)
        (!gameField.isPossibleCellMove(cell) && isMouseOver) -> ProjectColors.error.copy(alpha = 0.2f)
        isMouseOver -> ProjectColors.miniMapCell.copy(alpha = alpha * 0.5f)
        else -> ProjectColors.miniMapCell.copy(alpha = alpha)
    }
}

private fun calculateAlpha(cell: FieldCell, gameField: GameField): Float {
    if (cell.number == -1) {
        return 0.0f
    }


    if (cell.number == 0) {
        return 0.9f
    } else if (gameField.isPossibleCellMove(cell)) {
        return 0.8f
    } else if (cell == gameField.currentCell) {
        return 1.0f
    }
    return 0.0f
}