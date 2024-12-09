package app.encuentrastodo.data.repositorios

import androidx.paging.Pager
import androidx.paging.PagingConfig
import app.encuentrastodo.data.EncuentrasTodoDatabase
import app.encuentrastodo.data.enums.TipoItemVenta
import app.encuentrastodo.data.models.VentaProgreso

class VentasRepositorio(
    private val database: EncuentrasTodoDatabase
) {
    fun listarCarrito() = database.ventas().listarCarrito()

    fun configurarCarrito(tipoItemVenta: TipoItemVenta, id: Int, cantidad: Int) {
        if (cantidad == 0) {
            database.ventas().eliminarCarrito(tipoItemVenta, id)
        } else {
            database.ventas().insertarCarrito(VentaProgreso(tipoItemVenta, id, cantidad))
        }
    }

    fun finalizarVenta() = database.ventas().finalizarCarrito()

    fun listarVentas() = Pager(
        config = PagingConfig(20, initialLoadSize = 20),
        pagingSourceFactory = { database.ventas().listarVentas() }
    ).flow

    fun listarVenta(folio: Int) = database.ventas().listarVentaDetalle(folio)
}