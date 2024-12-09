package app.encuentrastodo.data.repositorios

import androidx.paging.Pager
import androidx.paging.PagingConfig
import app.encuentrastodo.data.EncuentrasTodoDatabase
import app.encuentrastodo.data.models.Producto

class ProductosRepositorio(private val database: EncuentrasTodoDatabase) {
    fun select(id: Int) = database.productos().select(id)

    fun listarPaginado(incluirInactivos: Boolean = true) = Pager(
        config = PagingConfig(20, initialLoadSize = 30),
        pagingSourceFactory = { database.productos().listarPaginado(incluirInactivos) }
    ).flow

    fun agregar(producto: Producto) = database.productos().insertar(producto)

    fun update(producto: Producto) = database.productos().update(producto)

    fun marcarComoInactivo(id: Int) = database.productos().marcarComoInactivo(id)
}