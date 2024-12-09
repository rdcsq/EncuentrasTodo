package app.encuentrastodo.ui.views.listarpaquetes

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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import app.encuentrastodo.ui.components.PaqueteView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListarPaquetes(
    viewModel: ListarPaquetesViewModel = viewModel(),
    regresar: () -> Unit,
    verPaquete: (Int) -> Unit,
) {
    val paquetes = viewModel.paquetes.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Listar paquetes") },
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
                paquetes.itemCount,
                key = paquetes.itemKey { it.id }
            ) { id ->
                if (paquetes[id] != null) {
                    PaqueteView(
                        paquetes[id]!!,
                        verDetalles = { verPaquete(paquetes[id]!!.id) }
                    )
                }
                HorizontalDivider()
            }
        }
    }
}