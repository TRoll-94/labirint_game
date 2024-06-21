import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import com.labirint.util.Localization


@Composable
fun winDialog(onNewGame: () -> Unit, onExit: () -> Unit) {

    Dialog(onDismissRequest = { onNewGame() }) {
        // Подложка диалога
        Surface(
            shape = MaterialTheme.shapes.medium,  // Используем формы из текущей темы
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(Localization.getString("your_win"), style = MaterialTheme.typography.h6)

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        onNewGame()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(Localization.getString("new_game"))
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        onExit()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(Localization.getString("exit_to_menu"))
                }
            }
        }
    }
}
