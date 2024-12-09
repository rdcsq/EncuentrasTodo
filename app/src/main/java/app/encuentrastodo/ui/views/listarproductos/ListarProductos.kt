package app.encuentrastodo.ui.views.listarproductos

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import app.encuentrastodo.data.models.Producto
import app.encuentrastodo.ui.components.ProductoView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListarProductos(
    viewModel: ListarProductosViewModel = viewModel(),
    regresar: () -> Unit,
    editarProducto: (Producto) -> Unit,
) {
    val productos = viewModel.productos.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Productos")
                },
                navigationIcon = {
                    IconButton(onClick = { regresar() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        ) {
            items(
                count = productos.itemCount,
                key = productos.itemKey { it.id!! }
            ) { i ->
                if (productos[i] != null) {
                    ProductoView(
                        productos[i]!!,
                        editarCallback = { editarProducto(productos[i]!!) },
                        eliminarCallback = { viewModel.delete(productos[i]!!.id!!) },
                        agregarVenta = {
                            viewModel.configurarCarrito(
                                productoId = productos[i]!!.id!!,
                                cantidad = it
                            )
                        }
                    )
                }
            }
        }
    }
}