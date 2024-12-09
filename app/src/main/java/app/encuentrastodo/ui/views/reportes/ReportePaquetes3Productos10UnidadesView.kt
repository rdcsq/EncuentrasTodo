package app.encuentrastodo.ui.views.reportes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportePaquetes3Productos10UnidadesView(
    viewModel: ReportesViewModel = viewModel(),
    regresar: () -> Unit,
    verPaquete: (Int) -> Unit
) {
    val datos by viewModel.reportesRepositorio.paquetes3Productos10Unidades()
        .collectAsStateWithLifecycle(null)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Reporte")
                        Text(
                            "Paquetes con 3 productos, 10 unidades",
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                },

                navigationIcon = {
                    IconButton(onClick = { regresar() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(Modifier.padding(innerPadding)) {
            if (datos != null) {
                items(datos!!) {
                    Column(
                        Modifier
                            .clickable { verPaquete(it.paqueteId) }
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text("Paquete ${it.paqueteId}")
                        Text("Productos: ${it.cantidadProductos} - Unidades: ${it.cantidadUnidades}")
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}