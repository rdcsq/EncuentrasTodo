package app.encuentrastodo.ui.views.reportes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import app.encuentrastodo.data.toMoneyString
import app.encuentrastodo.ui.components.LazyTable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReporteVentaProductoView(
    viewModel: ReportesViewModel = viewModel(),
    regresar: () -> Unit
) {
    val datos by viewModel.reportesRepositorio.reporteVentaProducto()
        .collectAsStateWithLifecycle(null)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Reporte")
                        Text(
                            "Importe de ventas",
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
        if (datos != null) {
            LazyTable(
                modifier = Modifier.padding(innerPadding),
                columns = 3,
                rows = datos!!.size,
                headers = arrayOf("ID", "Individual", "Paquete"),
                cellData = { row, column ->
                    when (column) {
                        0 -> datos!![row].productoId.toString()
                        1 -> "$${datos!![row].gananciaIndividual.toMoneyString()}"
                        2 -> "$${datos!![row].gananciaPaquete.toMoneyString()}"
                        else -> ""
                    }
                }
            )
        }
    }
}