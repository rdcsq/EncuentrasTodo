package app.encuentrastodo.ui.views.listarproductos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.encuentrastodo.EncuentrasTodoApplication
import app.encuentrastodo.data.enums.TipoItemVenta
import app.encuentrastodo.data.models.ProductoReadView
import app.encuentrastodo.data.repositorios.ProductosRepositorio
import app.encuentrastodo.data.repositorios.VentasRepositorio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ListarProductosViewModel(
    private val productosRepositorio: ProductosRepositorio = EncuentrasTodoApplication.productosRepositorio,
    private val ventasRepositorio: VentasRepositorio = EncuentrasTodoApplication.ventasRepositorio
) : ViewModel() {
    val productos: Flow<PagingData<ProductoReadView>> = productosRepositorio
        .listarPaginado()
        .cachedIn(viewModelScope)

    fun delete(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        productosRepositorio.marcarComoInactivo(id)
    }

    fun configurarCarrito(productoId: Int, cantidad: Int) = viewModelScope.launch(Dispatchers.IO) {
        ventasRepositorio.configurarCarrito(TipoItemVenta.PRODUCTO, productoId, cantidad)
    }
}