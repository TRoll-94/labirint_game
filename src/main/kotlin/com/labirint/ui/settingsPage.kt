package com.labirint.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.labirint.Router
import com.labirint.ui.elems.DropdownComponent
import com.labirint.util.Difficulty
import com.labirint.util.DifficultyName
import com.labirint.util.Localization
import com.labirint.util.ProjectColors


@Composable
fun settingsPage(router: Router) {
    starField()

    Column(
        modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            Localization.getString("settingsTitle"),
            color = ProjectColors.white,
            modifier = Modifier.padding(bottom = 20.dp),
        )

        DropdownComponent(
            selected = Localization.getLanguage(),
            values = Localization.getLanguages(),
            label = "language",
            onChange = { language: String ->
                Localization.setLanguage(language)
            }
        ).draw()

        DropdownComponent(
            selected = Difficulty.getDifficulty(),
            values = Difficulty.getDifficulties(),
            label = "difficulty",
            onChange = { difficulty: String ->
                 Difficulty.setDifficulty(difficulty)
            }
        ).draw()

        Button(onClick = { router.mainPage() }) {
            Text(Localization.getString("back"))
        }

    }
}