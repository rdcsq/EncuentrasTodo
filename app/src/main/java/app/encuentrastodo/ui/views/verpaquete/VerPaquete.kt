package app.encuentrastodo.ui.views.verpaquete

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import app.encuentrastodo.data.enums.Estatus
import app.encuentrastodo.data.toMoneyString
import app.encuentrastodo.ui.components.UnidadesDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerPaquete(
    id: Int,
    viewModel: VerPaqueteViewModel = viewModel(),
    regresar: () -> Unit,
    editarPaquete: (Int) -> Unit,
) {
    val context = LocalContext.current
    val paquete by viewModel.datos.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var mostrarEliminarConfirmacion by remember { mutableStateOf(false) }
    var mostrarCarritoConfigurar by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.buscar(id) {
            if (it == null) {
                Toast.makeText(context, "Paquete no existe", Toast.LENGTH_LONG).show()
                regresar()
                return@buscar
            }
        }
    }

    fun editar() {
        if (paquete!!.tieneVentas) {
            scope.launch {
                snackbarHostState.showSnackbar("El paquete ya tiene ventas registradas, por lo que no puede ser editado")
            }
            return
        }
        editarPaquete(id)
    }

    fun eliminar() {
        if (paquete!!.tieneVentas) {
            scope.launch {
                snackbarHostState.showSnackbar("El paquete ya tiene ventas registradas, por lo que no puede ser editado")
            }
            return
        }
        mostrarEliminarConfirmacion = true
    }

    fun eliminarConfirmado() {
        viewModel.eliminarPaquete(id)
        mostrarEliminarConfirmacion = false
        regresar()
    }

    fun agregarVenta() {
        if (!paquete!!.puedeVenderse) {
            scope.launch {
                snackbarHostState.showSnackbar("El paquete no puede venderse porque algún producto está inactivo.")
            }
            return
        }
        mostrarCarritoConfigurar = true
    }

    fun confirmarCarrito(cantidad: Int) {
        viewModel.configurarCarrito(paquete!!.id, cantidad)
        mostrarCarritoConfigurar = false
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text("Paquete")
                },
                navigationIcon = {
                    IconButton(onClick = { regresar() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                actions = {
                    if (paquete != null) {
                        IconButton(onClick = { eliminar() }) {
                            Icon(Icons.Filled.Delete, null)
                        }
                        IconButton(onClick = { editar() }) {
                            Icon(Icons.Filled.Edit, null)
                        }
                        IconButton(onClick = { agregarVenta() }) {
                            Icon(Icons.Filled.ShoppingCart, null)
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        if (paquete == null) {
            Box(
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                item {
                    Text(
                        "${paquete!!.productos.size} productos. Total: $${paquete!!.precioOriginal.toMoneyString()} -> $${paquete!!.precioDescontado.toMoneyString()}"
                    )
                    Text("Disponibilidad: ${paquete!!.paquetesDisponibles}")
                }

                items(paquete?.productos?.size ?: 0) { idx ->
                    val pp = paquete!!.productos[idx]
                    Column(
                        Modifier.padding(vertical = 8.dp)
                    ) {
                        Text(
                            "ID: ${pp.producto!!.id} - ${pp.producto!!.nombre}",
                            color = if (pp.producto!!.estatus == Estatus.INACTIVO) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                        )
                        Text("Unidades: ${pp.nUnidades} (Disp: ${pp.producto!!.disponibles})")
                        Text("Unit: $${pp.producto!!.precioUnitario.toMoneyString()}, Desc: ${pp.porcentajeDescuento}%")
                        Text(
                            "$${
                                pp.calcularPrecioOriginal().toMoneyString()
                            } -> $${pp.calcularPrecioDescontado().toMoneyString()}"
                        )
                    }
                    HorizontalDivider()
                }
            }
        }
    }

    if (mostrarEliminarConfirmacion) {
        AlertDialog(
            onDismissRequest = { mostrarEliminarConfirmacion = false },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Desea eliminar el paquete ${id}?") },
            confirmButton = {
                TextButton(onClick = ::eliminarConfirmado) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarEliminarConfirmacion = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (mostrarCarritoConfigurar) {
        UnidadesDialog(
            dismiss = { mostrarCarritoConfigurar = false },
            maxUnidades = paquete!!.paquetesDisponibles,
            precioUnitario = paquete!!.precioDescontado,
            completar = ::confirmarCarrito,
            unidadesIniciales = paquete!!.unidadesEnCarrito
        )
    }
}