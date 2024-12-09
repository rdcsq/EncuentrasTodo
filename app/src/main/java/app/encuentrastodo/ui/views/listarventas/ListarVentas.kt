package app.encuentrastodo.ui.views.listarventas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListarVentas(
    viewModel: ListarVentasViewModel = viewModel(),
    regresar: () -> Unit,
    verVenta: (Int) -> Unit
) {
    val listado = viewModel.listado.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Listar ventas") },
                navigationIcon = {
                    IconButton(onClick = { regresar() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            Modifier.padding(innerPadding)
        ) {
            items(
                listado.itemCount,
                key = listado.itemKey { it.folio!! }
            ) { id ->
                if (listado[id] != null) {
                    Column(
                        Modifier
                            .clickable { verVenta(listado[id]!!.folio!!) }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .fillMaxWidth()
                    ) {
                        Text("Venta #${listado[id]!!.folio}")
                        Text("${listado[id]!!.fecha}")
                    }
                }
                HorizontalDivider()
            }
        }
    }
}