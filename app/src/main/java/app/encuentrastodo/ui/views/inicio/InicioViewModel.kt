package app.encuentrastodo.ui.views.inicio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.encuentrastodo.EncuentrasTodoApplication
import app.encuentrastodo.data.enums.TipoItemVenta
import app.encuentrastodo.data.models.ProductoReadView
import app.encuentrastodo.data.repositorios.ProductosRepositorio
import app.encuentrastodo.data.repositorios.VentasRepositorio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.launch

class InicioViewModel(
    private val productosRepositorio: ProductosRepositorio = EncuentrasTodoApplication.productosRepositorio,
    private val ventasRepositorio: VentasRepositorio = EncuentrasTodoApplication.ventasRepositorio
) : ViewModel() {
    private val _producto = MutableStateFlow<ProductoReadView?>(null)
    val producto = _producto.asStateFlow()

    private var productoFlowJob: Job? = null

    fun buscar(id: Int, callback: (ProductoReadView?) -> Unit) {
        productoFlowJob?.cancel()
        productoFlowJob = viewModelScope.launch(Dispatchers.IO) {
            productosRepositorio.select(id).cancellable().collect { p ->
                launch(Dispatchers.Main) {
                    _producto.value = p
                    callback(p)
                }
            }
        }
    }

    fun eliminar(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        productosRepositorio.marcarComoInactivo(id)
    }

    fun configurarCarritoProducto(productoId: Int, cantidad: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            ventasRepositorio.configurarCarrito(TipoItemVenta.PRODUCTO, productoId, cantidad)
        }
}