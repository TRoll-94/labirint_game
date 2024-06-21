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
fun simpleDialog(
    onExit: @Composable (() -> Unit),
    title: String,
) {
    val isExit = remember { mutableStateOf(false) }

    Dialog(onDismissRequest = { isExit.value = true }) {
        // Подложка диалога
        Surface(
            shape = MaterialTheme.shapes.medium,  // Используем формы из текущей темы
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(title, style = MaterialTheme.typography.h6)

                Spacer(modifier = Modifier.height(20.dp))

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        isExit.value = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(Localization.getString("exit_to_menu"))
                }
            }
        }
    }
    if (isExit.value) {
        onExit()
        isExit.value = false
    }
}
