package com.labirint.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerMoveFilter
import com.labirint.Router
import com.labirint.util.ProjectColors
import kotlinx.coroutines.delay
import java.util.Collections.addAll
import kotlin.math.*
import kotlin.random.Random


fun Offset.distanceTo(other: Offset): Float {
    return sqrt((this.x - other.x).pow(2) + (this.y - other.y).pow(2))
}

class StartProperty(
    private val initialValue: Float,
    private var needIncrease: Boolean,
    private val changeSpeed: Float,
    value: Float = initialValue,
    private val maxValue: Float = initialValue * 2,
    private val minValue: Float = initialValue
) {
    var value by mutableStateOf(value)
    fun update() {
        if (needIncrease) {
            value = min(value + changeSpeed, maxValue)
            if (value >= maxValue) {
                needIncrease = false
            }
        } else {
            value = max(value - changeSpeed, minValue)
            if (value <= minValue) {
                needIncrease = true
            }
        }
    }
}

class Star(
    var position: Offset,
    val color: Color,
    opacity: Float,
    radius: Float,
) {
    val initialPosition = position
    var opacityProp by mutableStateOf(
        StartProperty(
            opacity, false, Random.nextFloat() * 0.001f + 0.001f,
            minValue = 0.001f, maxValue = opacity
        )
    )
    var radiusProp by mutableStateOf(
        StartProperty(
            radius, true, Random.nextFloat() * 0.01f + 0.01f
        )
    )

}

var possibleColors = listOf(
    ProjectColors.star,
)

fun DrawScope.animatedStar(star: Star, mousePosition: Offset) {
    if (mousePosition != Offset.Unspecified && mousePosition.distanceTo(star.position) < 100) {
        star.position = Offset(
            star.position.x + (mousePosition.x - star.position.x) * 0.01f,
            star.position.y + (mousePosition.y - star.position.y) * 0.01f
        )
        drawLine(color = Color.White, strokeWidth = .5f, start = mousePosition, end = star.position)
        drawCircle(
            color = star.color, radius = star.radiusProp.value * 2f, center = star.position
        )
    } else {
        star.position = Offset(
            star.position.x + (star.initialPosition.x - star.position.x) * 0.01f,
            star.position.y + (star.initialPosition.y - star.position.y) * 0.01f
        )
        drawCircle(
            color = star.color.copy(alpha = star.opacityProp.value),
            radius = star.radiusProp.value,
            center = star.position,
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun starField() {
    var mousePosition by remember { mutableStateOf(Offset.Unspecified) }

    val stars = remember {
        mutableStateListOf<Star>().apply {
            addAll(List(100) {
                    Star(
                        position = Offset(Random.nextFloat() * 800, Random.nextFloat() * 600),
                        radius = max(1f, Random.nextFloat() * 2f),
                        color = possibleColors.random(),
                        opacity = max(0.1f, Random.nextFloat() * 0.5f + 0.5f)
                    )
                }
            )
        }
    }

    LaunchedEffect(key1 = true) {
        while(true) {
            delay(1000 / 60L)
            stars.forEach { star ->
                star.opacityProp.update()
                star.radiusProp.update()
            }
        }
    }

    Canvas(modifier = Modifier.fillMaxSize().pointerMoveFilter(
        onMove = {
            mousePosition = it
            true
        },
        onExit = {
            mousePosition = Offset.Unspecified
            false
        }
    )) {

        drawRect(color = Color.Black, size = size)
        stars.forEach { star: Star -> animatedStar(star, mousePosition) }

    }
}