package app.encuentrastodo.ui.views.verventa

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import app.encuentrastodo.data.toMoneyString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VentaActual(
    viewModel: VerVentaViewModel = viewModel(),
    folio: Int? = null,
    regresar: () -> Unit,
) {
    val context = LocalContext.current

    val listado by viewModel.listado.collectAsStateWithLifecycle(null)
    var total by remember { mutableDoubleStateOf(0.0) }
    var mostrarDialogoConfirmacion by remember { mutableStateOf(false) }

    LaunchedEffect(folio) {
        viewModel.buscar(folio)
    }

    LaunchedEffect(listado) {
        if (listado == null) return@LaunchedEffect
        if (listado!!.isEmpty() && folio != null) {
            Toast.makeText(context, "No se encontró el folio de venta", Toast.LENGTH_SHORT).show()
            regresar()
            return@LaunchedEffect
        }
        total = listado!!.sumOf { it.precioDescontado ?: it.precioUnitario }
    }

    fun finalizarVenta() {
        mostrarDialogoConfirmacion = false
        viewModel.finalizarVenta()
        regresar()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (folio == null) "Venta actual" else "Venta") },
                navigationIcon = {
                    IconButton(onClick = { regresar() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                actions = {
                    if (folio == null && listado?.isEmpty() == false) {
                        IconButton(onClick = { mostrarDialogoConfirmacion = true }) {
                            Icon(Icons.AutoMirrored.Filled.Send, null)
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            Modifier
                .padding(horizontal = 16.dp, 8.dp)
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (listado != null) {
                item {
                    Text("${listado!!.size} elementos")
                }

                items(listado!!) { item ->
                    Row(
                        Modifier.padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Column(
                            modifier = Modifier.weight(0.7f)
                        ) {
                            Text(
                                "ID: ${item.id} - ${item.nombre}",
                                fontWeight = FontWeight.Bold,
                            )
                            Text("Cant: ${item.unidades}")
                        }
                        Column(
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                "$${item.precioUnitario.toMoneyString()}",
                                textDecoration = if (item.precioDescontado != null) TextDecoration.LineThrough else TextDecoration.None
                            )
                            if (item.precioDescontado != null) {
                                Text("$${item.precioDescontado.toMoneyString()}")
                            }
                        }
                    }
                    HorizontalDivider()
                }

                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text("Total: $${total.toMoneyString()}")
                    }
                }
            }
        }
    }

    if (listado != null && mostrarDialogoConfirmacion) {
        AlertDialog(
            title = { Text("Confirmación") },
            text = { Text("¿Finalizar venta?") },
            confirmButton = {
                TextButton(onClick = ::finalizarVenta) {
                    Text("Finalizar")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoConfirmacion = false }) {
                    Text("Regresar")
                }
            },
            onDismissRequest = { mostrarDialogoConfirmacion = false }
        )
    }
}