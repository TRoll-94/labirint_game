package com.labirint.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.labirint.util.Localization
import com.labirint.util.ProjectColors

@Composable
fun gameToolbar(
    isPaused: MutableState<Boolean>,
) {
    return Row(
        modifier = Modifier.fillMaxWidth().padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(Localization.getString("gamePage"), color = ProjectColors.white)
        Button(
            onClick = { isPaused.value = !isPaused.value },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = ProjectColors.transparent, contentColor = ProjectColors.white
            ),
            border = BorderStroke(0.dp, ProjectColors.white),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.padding(0.dp).height(24.dp).width(24.dp),

            ) {
            Text("II", modifier = Modifier.padding(0.dp))
        }
    }
}