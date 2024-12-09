package app.encuentrastodo.data.repositorios

import androidx.paging.Pager
import androidx.paging.PagingConfig
import app.encuentrastodo.data.EncuentrasTodoDatabase
import app.encuentrastodo.data.enums.Estatus
import app.encuentrastodo.data.models.PaqueteDetalles
import app.encuentrastodo.data.models.PaqueteProducto
import app.encuentrastodo.data.models.ProductoReadView

class PaquetesRepositorio(
    private val database: EncuentrasTodoDatabase
) {
    fun crear() = database.paquetes().crear()

    fun agregarProductos(productos: List<PaqueteProducto>) =
        database.paquetes().agregarProductos(productos)

    fun listarPaquetes() = Pager(
        config = PagingConfig(20, initialLoadSize = 20),
        pagingSourceFactory = { database.paquetes().listarPaquetes() }
    ).flow

    fun leerPaquete(id: Int): PaqueteDetalles? {
        val cursor = database.paquetes().leerPaquete(id)
        if (!cursor.moveToFirst()) return null

        val cantidadEnCarrito = cursor.getInt(8)
        val tieneVentas = cursor.getInt(10) == 1

        val productos = mutableListOf<PaqueteProducto>()
        do {
            val pp = PaqueteProducto(
                paqueteId = id,
                productoId = cursor.getInt(1),
                nUnidades = cursor.getInt(2),
                porcentajeDescuento = cursor.getInt(3),
            )
            pp.producto = ProductoReadView(
                id = pp.productoId,
                nombre = cursor.getString(4),
                precioUnitario = cursor.getDouble(5),
                nUnidades = cursor.getInt(6),
                estatus = Estatus.fromString(cursor.getString(7)),
                unidadesEnCarrito = cursor.getInt(8),
                disponibles = cursor.getInt(9)
            )
            productos.add(pp)
        } while (cursor.moveToNext())
        return PaqueteDetalles(id, productos.toList(), cantidadEnCarrito, tieneVentas)
    }

    fun eliminarPaquete(id: Int) = database.paquetes().eliminarPaquete(id)

    fun listarPaquetesProductosRaw(paqueteId: Int) =
        database.paquetes().listarPaquetesProductosRaw(paqueteId)

    fun actualizarPaquete(id: Int, productos: List<PaqueteProducto>) =
        database.paquetes().actualizarPaquete(id, productos)
}