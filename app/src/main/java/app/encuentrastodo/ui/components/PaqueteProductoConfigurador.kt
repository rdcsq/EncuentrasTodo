package app.encuentrastodo.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import app.encuentrastodo.data.models.PaqueteProducto

@Composable
fun PaqueteProductoConfigurador(
    paqueteProducto: PaqueteProducto,
) {
    var nUnidades by remember { mutableIntStateOf(paqueteProducto.nUnidades) }
    var porcentajeDescuento by remember { mutableIntStateOf(paqueteProducto.porcentajeDescuento) }

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(
            start = 16.dp,
            end = 16.dp,
            bottom = 8.dp
        )
    ) {
        OutlinedTextField(
            value = nUnidades.toString(),
            onValueChange = { v ->
                if (v.isBlank()) {
                    nUnidades = 0
                    paqueteProducto.nUnidades = 0
                    return@OutlinedTextField
                }
                val nuevoValor = v.toIntOrNull() ?: return@OutlinedTextField
                if (nuevoValor < 0) return@OutlinedTextField
                nUnidades = nuevoValor
                paqueteProducto.nUnidades = nuevoValor
            },
            label = { Text("# unidades") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.weight(1f)
        )
        OutlinedTextField(
            value = porcentajeDescuento.toString(),
            onValueChange = { v ->
                if (v.isBlank()) {
                    porcentajeDescuento = 0
                    paqueteProducto.porcentajeDescuento = 0
                    return@OutlinedTextField
                }
                val nuevoValor = v.toIntOrNull() ?: return@OutlinedTextField
                if (nuevoValor < 0 || nuevoValor > 100) return@OutlinedTextField
                porcentajeDescuento = nuevoValor
                paqueteProducto.porcentajeDescuento = nuevoValor
            },
            label = { Text("% descuento (0-100)") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.weight(1f)
        )
    }
}