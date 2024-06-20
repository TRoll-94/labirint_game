package com.labirint.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import com.labirint.Router
import com.labirint.game.GameField
import com.labirint.util.Localization
import com.labirint.util.ProjectColors
import pauseDialog


@Composable
fun gamePage(router: Router) {
    val isPaused = remember { mutableStateOf(false) }
    val requester = remember { FocusRequester() }
    val gameField = remember { GameField() }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .onKeyEvent { keyEvent: KeyEvent ->
                if (keyEvent.key == Key.Escape && keyEvent.type == KeyEventType.KeyDown) {
                    isPaused.value = !isPaused.value
                    true
                } else {
                    false
                }
            }
            .focusRequester(requester)
            .focusable()
            .background(ProjectColors.black)
    ) {
        Column(
            modifier = Modifier.matchParentSize(),
        ) {
            Row(
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Top
            ) {
                drawGameMinimap(gameField = gameField)
            }
        }
        if (isPaused.value) {
            pauseDialog(
                onResume = { isPaused.value = false },
                onExit = {
                    isPaused.value = false
                    router.mainPage()
                }
            )
        }
    }
    LaunchedEffect(Unit) {
        requester.requestFocus()
    }

}