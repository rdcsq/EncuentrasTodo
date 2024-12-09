package app.encuentrastodo.ui.views.reportes.acumulado

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import app.encuentrastodo.data.toLocalDate
import app.encuentrastodo.data.toMoneyString
import app.encuentrastodo.ui.components.LazyTable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReporteAcumuladoView(
    viewModel: ReporteAcumuladoViewModel = viewModel(),
    regresar: () -> Unit
) {
    var mostrarDatePicker by remember { mutableStateOf(true) }
    val datePickerState = rememberDateRangePickerState()
    val datos by viewModel.datos.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Reporte")
                        Text(
                            "Acumulado",
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
                headers = arrayOf("Producto", "Fecha", "Unidades", "Importe"),
                rows = datos!!.size,
                columns = 4,
                cellData = { row, column ->
                    when (column) {
                        0 -> datos!![row].productoId.toString()
                        1 -> datos!![row].fecha.toString()
                        2 -> datos!![row].unidades.toString()
                        3 -> "$${datos!![row].importe.toMoneyString()}"
                        else -> ""
                    }
                }
            )
        }
    }

    if (mostrarDatePicker) {
        DatePickerDialog(
            onDismissRequest = {
                mostrarDatePicker = false
                regresar()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val inicio = datePickerState.selectedStartDateMillis!!.toLocalDate()
                        val fin = datePickerState.selectedEndDateMillis!!.toLocalDate()
                        viewModel.buscar(inicio, fin)
                        mostrarDatePicker = false
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarDatePicker = false
                        regresar()
                    }
                ) {
                    Text("Cancelar")
                }
            }
        ) {
            DateRangePicker(
                state = datePickerState,
                title = {
                    Text("Elige rango de fechas")
                },
                showModeToggle = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .padding(16.dp)
            )
        }
    }
}