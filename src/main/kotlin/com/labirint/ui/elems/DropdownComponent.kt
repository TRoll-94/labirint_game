package com.labirint.ui.elems

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.labirint.util.Localization
import com.labirint.util.ProjectColors


class DropdownComponent(
    var selected: String,
    var values: List<String>,
    var label: String,
    var onChange: (String) -> Unit
) {

    @Composable
    fun draw() {
        val showDropdown = remember { mutableStateOf(false) }
        val selectedValue = remember { mutableStateOf(selected) }

        Button(
            onClick = { showDropdown.value = true },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = ProjectColors.transparent,
                contentColor = ProjectColors.white
            ),
        ) {
            Text("${Localization.getString(label)}: ${Localization.getString(selectedValue.value)}")
        }

        DropdownMenu(
            expanded = showDropdown.value,
            onDismissRequest = { showDropdown.value = false },
        ) {
            values.forEach { language ->
                DropdownMenuItem(onClick = {
                    selectedValue.value = language
                    showDropdown.value = false
                    onChange(language)
                }) {
                    Text(Localization.getString(language))
                }
            }
        }

    }

}
