package com.labirint

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.labirint.game.GameField
import com.labirint.ui.*
import com.labirint.util.Localization


object Router {
    enum class Page {
        MainMenu,
        StarFieldPage,
        SettingsPage,
        NotFound,
        GamePage,
    }

    private var currentPage by mutableStateOf(Page.GamePage)

    private val pages = mapOf(
        Page.MainMenu to PageConfig(Localization.getString("gameTitle")) { router -> mainMenu(router) },
        Page.SettingsPage to PageConfig(Localization.getString("settingsTitle")) { router -> settingsPage(router) },
        Page.NotFound to PageConfig(Localization.getString("pageNotFound")) { router -> notFound(router) },
        Page.GamePage to PageConfig(Localization.getString("gameTitle")) { router -> gamePage(
            router, gameField = GameField()
        ) },
    )

    fun setPage(page: Page) {
        currentPage = page
    }

    fun mainPage() {
        currentPage = Page.MainMenu
    }

    @Composable
    fun getCurrentPageContent() {
        pages[currentPage]?.content?.invoke(this) ?: notFound(this)
    }

    fun getCurrentTitle(): String {
        return pages[currentPage]?.title ?: Localization.getString("pageNotFound")
    }

}

data class PageConfig(
    val title: String,
    val content: @Composable (Router) -> Unit
)


@Composable
@Preview
fun game() {
    MaterialTheme {
        Router.getCurrentPageContent()
    }
}

fun main() = application {
    createDb()
    Window(onCloseRequest = ::exitApplication, title = Router.getCurrentTitle()) {
        game()
    }
}
