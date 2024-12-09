package app.encuentrastodo.ui.views.verpaquete

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.encuentrastodo.EncuentrasTodoApplication
import app.encuentrastodo.data.enums.TipoItemVenta
import app.encuentrastodo.data.models.PaqueteDetalles
import app.encuentrastodo.data.repositorios.PaquetesRepositorio
import app.encuentrastodo.data.repositorios.VentasRepositorio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VerPaqueteViewModel(
    private val paqueteRepositorio: PaquetesRepositorio = EncuentrasTodoApplication.paquetesRepositorio,
    private val ventasRepositorio: VentasRepositorio = EncuentrasTodoApplication.ventasRepositorio,
) : ViewModel() {
    private var _datos = MutableStateFlow<PaqueteDetalles?>(null)
    val datos = _datos.asStateFlow()

    fun buscar(id: Int, callback: (PaqueteDetalles?) -> Unit) =
        viewModelScope.launch(Dispatchers.IO) {
            val res = paqueteRepositorio.leerPaquete(id)
            launch(Dispatchers.Main) {
                _datos.value = res
                callback(res)
            }
        }

    fun eliminarPaquete(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        paqueteRepositorio.eliminarPaquete(id)
    }

    fun configurarCarrito(paqueteId: Int, cantidad: Int) = viewModelScope.launch(Dispatchers.IO) {
        ventasRepositorio.configurarCarrito(TipoItemVenta.PAQUETE, paqueteId, cantidad)
    }
}