package app.encuentrastodo.ui.views.verventa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.encuentrastodo.EncuentrasTodoApplication
import app.encuentrastodo.data.models.VentaReadView
import app.encuentrastodo.data.repositorios.VentasRepositorio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VerVentaViewModel(
    private val ventasRepositorio: VentasRepositorio = EncuentrasTodoApplication.ventasRepositorio
) : ViewModel() {
    private val _listado = MutableStateFlow<List<VentaReadView>?>(null)
    val listado = _listado.asStateFlow()

    fun buscar(folio: Int?) = viewModelScope.launch(Dispatchers.IO) {
        if (folio == null) {
            ventasRepositorio.listarCarrito().collect {
                _listado.value = it
            }
        } else {
            ventasRepositorio.listarVenta(folio).collect {
                _listado.value = it
            }
        }
    }

    fun finalizarVenta() = viewModelScope.launch(Dispatchers.IO) {
        ventasRepositorio.finalizarVenta()
    }
}