package app.encuentrastodo.ui.views.crearpaquete

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import app.encuentrastodo.EncuentrasTodoApplication
import app.encuentrastodo.data.models.PaqueteProducto
import app.encuentrastodo.data.repositorios.PaquetesRepositorio
import app.encuentrastodo.data.repositorios.ProductosRepositorio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CrearPaqueteViewModel(
    private val productosRepositorio: ProductosRepositorio = EncuentrasTodoApplication.productosRepositorio,
    private val paquetesRepositorio: PaquetesRepositorio = EncuentrasTodoApplication.paquetesRepositorio
) : ViewModel() {
    val productos = productosRepositorio.listarPaginado(true)
        .cachedIn(viewModelScope)

    fun crearPaquete(productos: List<PaqueteProducto>) {
        viewModelScope.launch(Dispatchers.IO) {
            val paqueteId = paquetesRepositorio.crear().toInt()
            productos.forEach {
                it.paqueteId = paqueteId
            }
            paquetesRepositorio.agregarProductos(productos)
        }
    }

    fun obtenerSeleccionadosActual(idPaquete: Int, callback: (List<PaqueteProducto>) -> Unit) =
        viewModelScope.launch(Dispatchers.IO) {
            val paquete = paquetesRepositorio.listarPaquetesProductosRaw(idPaquete)
            launch(Dispatchers.Main) {
                callback(paquete)
            }
        }

    fun actualizarPaquete(id: Int, productos: List<PaqueteProducto>) =
        viewModelScope.launch(Dispatchers.IO) {
            paquetesRepositorio.actualizarPaquete(id, productos)
        }
}