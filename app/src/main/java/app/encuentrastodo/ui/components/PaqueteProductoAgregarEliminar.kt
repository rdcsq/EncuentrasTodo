package app.encuentrastodo.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

@Composable
fun PaqueteProductoAgregarEliminar(
    seleccionado: Boolean,
    eliminar: () -> Unit,
    agregar: () -> Unit
) {
    IconButton(
        onClick = {
            if (seleccionado) {
                eliminar()
            } else {
                agregar()
            }
        }
    ) {
        Icon(
            if (seleccionado) Icons.Filled.Delete else Icons.Filled.Add,
            null
        )
    }
}
