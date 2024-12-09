package app.encuentrastodo.ui.views.crearpaquete

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import app.encuentrastodo.data.models.PaqueteProducto
import app.encuentrastodo.ui.behaviors.HideKeyboardOnScroll
import app.encuentrastodo.ui.components.PaqueteProductoAgregarEliminar
import app.encuentrastodo.ui.components.PaqueteProductoConfigurador
import app.encuentrastodo.ui.components.ProductoView
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearPaquete(
    viewModel: CrearPaqueteViewModel = viewModel(),
    editarPaqueteId: Int? = null,
    regresar: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val nestedScrollConnection = remember {
        HideKeyboardOnScroll(keyboardController)
    }
    val context = LocalContext.current

    val productos = viewModel.productos.collectAsLazyPagingItems()
    val productosSeleccionados = remember { mutableStateMapOf<Int, PaqueteProducto>() }

    LaunchedEffect(editarPaqueteId) {
        if (editarPaqueteId == null) return@LaunchedEffect
        viewModel.obtenerSeleccionadosActual(editarPaqueteId) {
            if (it.isEmpty()) {
                Toast.makeText(context, "Paquete no existe", Toast.LENGTH_LONG).show()
                regresar()
                return@obtenerSeleccionadosActual
            }
            it.forEach { p -> productosSeleccionados[p.productoId] = p }
        }
    }

    fun validarGuardar() {
        if (productosSeleccionados.any { k -> !k.value.esValido() }) {
            scope.launch {
                snackbarHostState.showSnackbar("Algún producto del paquete está configurado con cero unidades.")
            }
            return
        }
        val lista = productosSeleccionados.values.toList()
        if (editarPaqueteId == null) {
            viewModel.crearPaquete(lista)
        } else {
            viewModel.actualizarPaquete(editarPaqueteId, lista)
        }
        regresar()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(if (editarPaqueteId == null) "Crear paquete" else "Editar paquete")
                },
                navigationIcon = {
                    IconButton(onClick = { regresar() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                actions = {
                    if (productosSeleccionados.size > 0) {
                        IconButton(onClick = ::validarGuardar) {
                            Icon(Icons.Filled.Check, null)
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            Modifier
                .padding(innerPadding)
                .nestedScroll(nestedScrollConnection)
        ) {
            items(
                productos.itemCount,
                key = productos.itemKey { it.id!! }
            ) { idx ->
                productos[idx]?.let {
                    Column {
                        ProductoView(
                            it,
                            derecha = {
                                PaqueteProductoAgregarEliminar(
                                    seleccionado = productosSeleccionados.containsKey(it.id),
                                    eliminar = { productosSeleccionados.remove(it.id) },
                                    agregar = {
                                        productosSeleccionados[it.id!!] =
                                            PaqueteProducto(
                                                paqueteId = editarPaqueteId ?: 0,
                                                productoId = it.id
                                            )
                                    }
                                )
                            }
                        )
                        productosSeleccionados[it.id]?.let { configuracion ->
                            PaqueteProductoConfigurador(configuracion)
                        }
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}
