package app.encuentrastodo.ui.views.listarventas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import app.encuentrastodo.EncuentrasTodoApplication
import app.encuentrastodo.data.repositorios.VentasRepositorio

class ListarVentasViewModel(
    private val ventasRepositorio: VentasRepositorio = EncuentrasTodoApplication.ventasRepositorio
) : ViewModel() {
    val listado = ventasRepositorio.listarVentas().cachedIn(viewModelScope)
}