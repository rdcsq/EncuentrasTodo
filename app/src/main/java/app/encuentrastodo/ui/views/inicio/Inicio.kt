package app.encuentrastodo.ui.views.inicio

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import app.encuentrastodo.ui.components.ProductoView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Inicio(
    viewModel: InicioViewModel = viewModel(),
    listarProductos: () -> Unit,
    verProducto: (Int) -> Unit,
    crearProducto: () -> Unit,
    listarPaquetes: () -> Unit,
    verPaquete: (Int) -> Unit,
    crearPaquete: () -> Unit,
    abrirVentaActual: () -> Unit,
    abrirVenta: (Int) -> Unit,
    listarVentas: () -> Unit,
    abrirReporte1: () -> Unit,
    abrirReporte2: () -> Unit,
    abrirReporte3: () -> Unit,
    abrirReporte4: () -> Unit,
) {
    val scroll = rememberScrollState()
    var productoId by remember { mutableStateOf("") }
    var paqueteId by remember { mutableStateOf("") }
    var ventaId by remember { mutableStateOf("") }
    val producto by viewModel.producto.collectAsStateWithLifecycle(null)

    val context = LocalContext.current

    fun buscarProducto() {
        val pId = productoId.toIntOrNull() ?: return
        viewModel.buscar(pId) {
            if (it == null) {
                Toast.makeText(context, "Producto no encontrado", Toast.LENGTH_LONG).show()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Encuentras Todo") }
            )
        }
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .verticalScroll(scroll)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Productos")
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { crearProducto() }
                ) {
                    Text("Crear producto")
                }
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { listarProductos() }
                ) {
                    Text("Listar productos")
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = productoId,
                    onValueChange = { v ->
                        if (!v.isDigitsOnly()) return@OutlinedTextField
                        productoId = v
                    },
                    label = { Text("ID de producto") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = { buscarProducto() },
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, null)
                }
            }
            if (producto != null) {
                Card {
                    ProductoView(
                        producto!!,
                        editarCallback = { verProducto(producto!!.id!!) },
                        eliminarCallback = {
                            viewModel.eliminar(producto!!.id!!)
                        },
                        agregarVenta = { viewModel.configurarCarritoProducto(producto!!.id!!, it) }
                    )
                }
            }
            HorizontalDivider(Modifier.padding(vertical = 8.dp))
            Text("Paquetes")
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { crearPaquete() }
                ) {
                    Text("Crear paquete")
                }
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { listarPaquetes() }
                ) {
                    Text("Listar paquetes")
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = paqueteId,
                    onValueChange = { v ->
                        if (!v.isDigitsOnly()) return@OutlinedTextField
                        paqueteId = v
                    },
                    label = { Text("ID de paquete") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = {
                        val id = paqueteId.toIntOrNull() ?: return@IconButton
                        verPaquete(id)
                    },
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, null)
                }
            }
            HorizontalDivider(Modifier.padding(vertical = 8.dp))
            Text("Venta")
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { listarVentas() }
                ) {
                    Text("Listar ventas")
                }
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { abrirVentaActual() }
                ) {
                    Text("Ver venta actual")
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = ventaId,
                    onValueChange = { v ->
                        if (!v.isDigitsOnly()) return@OutlinedTextField
                        ventaId = v
                    },
                    label = { Text("ID de venta") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = {
                        val id = ventaId.toIntOrNull() ?: return@IconButton
                        abrirVenta(id)
                    },
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, null)
                }
            }
            HorizontalDivider(Modifier.padding(vertical = 8.dp))
            Text("Reportes")
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { abrirReporte1() }
            ) {
                Text("Producto - No paquetes - No vendidos")
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { abrirReporte2() }
            ) {
                Text("Paquetes >= 3 productos y 10 unidades")
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { abrirReporte3() }
            ) {
                Text("Importe de ventas por producto")
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { abrirReporte4() }
            ) {
                Text("Acumulado")
            }
        }
    }
}