package com.labirint.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.labirint.Router
import com.labirint.util.Localization
import com.labirint.util.ProjectColors


@Composable
fun mainMenu(router: Router) {
    starField()
    Column(
        modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = Localization.getString("gameTitle"),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp),
            color = Color.White
        )
        Button(
            onClick = { router.setPage(Router.Page.GamePage) },
        ) {
            Text(Localization.getString("start"))
        }
        Button(
            onClick = { router.setPage(Router.Page.SettingsPage) },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = ProjectColors.transparent, contentColor = ProjectColors.white
            ),
            border = BorderStroke(1.dp, ProjectColors.white)
        ) {
            Text(Localization.getString("settingsTitle"))
        }
    }
}

@Composable
fun notFound(router: Router) {
    Column(
        modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)
    ) {
        Text(Localization.getString("pageNotFound"))
        Button(onClick = { router.mainPage()}) {
            Text("Go back")
        }
    }
}
