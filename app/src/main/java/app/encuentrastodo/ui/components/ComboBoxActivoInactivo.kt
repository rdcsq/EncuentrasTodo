package app.encuentrastodo.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import app.encuentrastodo.data.enums.Estatus

private val opciones = listOf(
    Pair("Activo", Estatus.ACTIVO), Pair("Inactivo", Estatus.INACTIVO)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComboBoxActivoInactivo(
    modifier: Modifier = Modifier,
    estatus: Int,
    setEstatus: (Int) -> Unit
) {
    var mostrarComboBox by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = mostrarComboBox,
        onExpandedChange = {
            mostrarComboBox = !mostrarComboBox
        },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = opciones[estatus].first,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = mostrarComboBox)
            },
            label = { Text("Estatus") },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = mostrarComboBox,
            onDismissRequest = { mostrarComboBox = false }
        ) {
            opciones.forEachIndexed { idx, (nombre, _) ->
                DropdownMenuItem(
                    text = { Text(nombre) },
                    onClick = {
                        setEstatus(idx)
                        mostrarComboBox = false
                    }
                )
            }
        }
    }

}