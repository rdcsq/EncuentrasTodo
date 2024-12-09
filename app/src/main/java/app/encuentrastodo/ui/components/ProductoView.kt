package app.encuentrastodo.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.encuentrastodo.data.enums.Estatus
import app.encuentrastodo.data.models.Producto
import app.encuentrastodo.data.models.ProductoReadView
import app.encuentrastodo.data.toMoneyString

@Composable
fun ProductoViewV0(
    producto: Producto,
    editarCallback: (() -> Unit)? = null,
    agregarVenta: ((Int) -> Unit)? = null,
    eliminarCallback: (() -> Unit)? = null,
    derecha: (@Composable RowScope.() -> Unit)? = null
) = ProductoView(
    ProductoReadView(
        producto.id ?: 0,
        producto.nombre,
        producto.precioUnitario,
        producto.nUnidades,
        producto.estatus,
        0,
        producto.nUnidades
    ),
    editarCallback,
    agregarVenta,
    eliminarCallback,
    derecha
)

@Composable
fun ProductoView(
    producto: ProductoReadView,
    editarCallback: (() -> Unit)? = null,
    agregarVenta: ((Int) -> Unit)? = null,
    eliminarCallback: (() -> Unit)? = null,
    derecha: (@Composable RowScope.() -> Unit)? = null
) {
    var expanded by remember { mutableStateOf(false) }
    var mostrarDeleteDialog by remember { mutableStateOf(false) }
    var mostrarUnidadesDialog by remember { mutableStateOf(false) }

    val mostrarDropdown = editarCallback != null || agregarVenta != null || eliminarCallback != null

    Row(
        Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(producto.id.toString())
        Column(
            Modifier.weight(1f)
        ) {
            Text(
                producto.nombre,
                color = if (producto.estatus == Estatus.INACTIVO) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
            )
            Text("Disponibilidad: ${producto.disponibles}")
            Text("Precio: $${producto.precioUnitario.toMoneyString()}")
            if (producto.unidadesEnCarrito > 0) {
                Text("Unidades en carrito: ${producto.unidadesEnCarrito}")
            }
        }
        if (mostrarDropdown) {
            Box(Modifier.wrapContentSize(Alignment.TopEnd)) {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Filled.MoreVert, null)
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    if (editarCallback != null) {
                        DropdownMenuItem(
                            text = { Text("Editar") },
                            onClick = {
                                editarCallback()
                                expanded = false
                            },
                            leadingIcon = {
                                Icon(Icons.Filled.Edit, null)
                            }
                        )
                    }
                    if (agregarVenta != null && producto.estatus != Estatus.INACTIVO) {
                        DropdownMenuItem(
                            text = { Text(if (producto.unidadesEnCarrito > 0) "Editar venta" else "Agregar a venta") },
                            onClick = {
                                mostrarUnidadesDialog = true
                                expanded = false
                            },
                            leadingIcon = {
                                Icon(Icons.Filled.ShoppingCart, null)
                            }
                        )
                    }
                    if (eliminarCallback != null && producto.estatus != Estatus.INACTIVO) {
                        DropdownMenuItem(
                            text = { Text("Eliminar") },
                            onClick = {
                                mostrarDeleteDialog = true
                                expanded = false
                            },
                            leadingIcon = {
                                Icon(Icons.Filled.Delete, null)
                            },
                            enabled = producto.unidadesEnCarrito == 0 && !producto.tieneVentas
                        )
                    }
                }
            }
        }
        if (derecha != null) derecha()
    }

    if (eliminarCallback != null && mostrarDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                mostrarDeleteDialog = false
            },
            title = { Text("Eliminar producto") },
            text = { Text("¿Desea marcar como inactivo al producto ${producto.id} - ${producto.nombre}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarDeleteDialog = false
                        eliminarCallback()
                    }
                ) {
                    Text("Continuar")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    mostrarDeleteDialog = false
                }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (agregarVenta != null && mostrarUnidadesDialog) {
        UnidadesDialog(
            dismiss = { mostrarUnidadesDialog = false },
            precioUnitario = producto.precioUnitario,
            completar = { agregarVenta(it) },
            maxUnidades = producto.disponibles,
            unidadesIniciales = producto.unidadesEnCarrito
        )
    }
}