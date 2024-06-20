import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier


@Composable
fun pauseDialog(onResume: () -> Unit, onExit: () -> Unit) {

    Dialog(onDismissRequest = { onResume() }) {
        // Подложка диалога
        Surface(
            shape = MaterialTheme.shapes.medium,  // Используем формы из текущей темы
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Game Paused", style = MaterialTheme.typography.h6)

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        onResume()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Resume")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        onExit()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Exit to Menu")
                }
            }
        }
    }
}
