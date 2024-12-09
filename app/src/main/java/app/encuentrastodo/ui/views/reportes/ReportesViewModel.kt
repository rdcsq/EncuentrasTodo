package app.encuentrastodo.ui.views.reportes

import androidx.lifecycle.ViewModel
import app.encuentrastodo.EncuentrasTodoApplication
import app.encuentrastodo.data.repositorios.ReportesRepositorio

class ReportesViewModel(
    val reportesRepositorio: ReportesRepositorio = EncuentrasTodoApplication.reportesRepositorio
) : ViewModel()