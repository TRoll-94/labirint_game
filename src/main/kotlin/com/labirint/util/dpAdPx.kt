package com.labirint.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun dpToPx(dp: Dp): Float {
    val density = LocalDensity.current.density
    return dp.value * density
}
