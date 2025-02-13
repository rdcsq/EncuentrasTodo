package app.encuentrastodo.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.encuentrastodo.data.models.ListadoPaquete

@Composable
fun PaqueteView(
    paquete: ListadoPaquete,
    verDetalles: (() -> Unit)? = null,
    derecha: (@Composable RowScope.() -> Unit)? = null
) {
    Row(
        Modifier
            .clickable { verDetalles?.invoke() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(Modifier.weight(1f)) {
            Text("Paquete #${paquete.id}")
            Text("${paquete.cantidad} productos (${paquete.cantidadProductosInactivos} inactivos)")
        }
        if (derecha != null) derecha()
    }
}