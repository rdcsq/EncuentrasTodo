package app.encuentrastodo.ui.views.reportes.acumulado

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.encuentrastodo.EncuentrasTodoApplication
import app.encuentrastodo.data.models.ReporteAcumulado
import app.encuentrastodo.data.repositorios.ReportesRepositorio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class ReporteAcumuladoViewModel(
    private val reportesRepositorio: ReportesRepositorio = EncuentrasTodoApplication.reportesRepositorio
) : ViewModel() {
    private var _datos = MutableStateFlow<List<ReporteAcumulado>?>(null)
    val datos = _datos.asStateFlow()

    private var job: Job? = null

    fun buscar(inicio: LocalDate, fin: LocalDate) {
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            reportesRepositorio.acumulado(inicio, fin).collect {
                Log.d("ReporteAcumulado", it.toString())
                _datos.value = it
            }
        }
    }
}