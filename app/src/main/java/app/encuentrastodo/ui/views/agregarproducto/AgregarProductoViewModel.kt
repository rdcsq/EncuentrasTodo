package app.encuentrastodo.ui.views.agregarproducto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.encuentrastodo.EncuentrasTodoApplication
import app.encuentrastodo.data.models.Producto
import app.encuentrastodo.data.repositorios.ProductosRepositorio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

class AgregarProductoViewModel(
    private val productosRepositorio: ProductosRepositorio = EncuentrasTodoApplication.productosRepositorio
) : ViewModel() {
    fun buscar(id: Int, callback: (Producto?) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        productosRepositorio.select(id).take(1).collect {
            launch(Dispatchers.Main) {
                callback(it)
            }
        }
    }

    fun agregar(producto: Producto) = viewModelScope.launch(Dispatchers.IO) {
        productosRepositorio.agregar(producto)
    }

    fun update(producto: Producto) = viewModelScope.launch(Dispatchers.IO) {
        productosRepositorio.update(producto)
    }
}