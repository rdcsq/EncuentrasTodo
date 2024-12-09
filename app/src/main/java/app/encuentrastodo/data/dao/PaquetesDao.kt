package app.encuentrastodo.data.dao

import android.database.Cursor
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import app.encuentrastodo.data.models.ListadoPaquete
import app.encuentrastodo.data.models.PaqueteProducto

@Dao
interface PaquetesDao {
    @Query("INSERT INTO paquetes (fecha) VALUES (CURRENT_DATE)")
    fun crear(): Long

    @Insert
    fun agregarProductos(productos: List<PaqueteProducto>)

    @Query(
        """
        SELECT p.id AS id,
               sum(iif(pp.paqueteId is not null, 1, 0)) AS cantidad,
               sum(iif(pr.estatus = 'B', 1, 0)) AS cantidadProductosInactivos
        FROM paquetes p
            LEFT JOIN paquetesProductos pp ON p.id = pp.paqueteId
            LEFT JOIN productos pr ON pp.productoId = pr.id
        GROUP BY p.id
    """
    )
    fun listarPaquetes(): PagingSource<Int, ListadoPaquete>

    @Query(
        """
         SELECT p.id AS id,
               pp.productoId AS productoId,
               pp.nUnidades AS nUnidades,
               pp.porcentajeDescuento AS porcentajeDescuento,
               pr.nombre AS nombre,
               pr.precioUnitario AS precioUnitario,
               pr.nUnidades AS prNUnidades,
               pr.estatus AS prEstatus,
               coalesce(vp.unidades, 0) AS cantidadEnCarrito,
               pr.nUnidades - coalesce(voPr.unidades, 0) - coalesce(sum(voOtrPq.unidades * ppOtr.nUnidades), 0) AS disponibles,
               max(iif(vd.id is not null, 1, 0)) AS tieneVentas
        FROM paquetes p
            JOIN paquetesProductos pp ON p.id = pp.paqueteId
            JOIN productos pr ON pp.productoId = pr.id
            LEFT JOIN ventaProgreso vp ON vp.tipoItemVenta = 1 AND vp.id = p.id
            LEFT JOIN paquetesProductos ppOtr ON ppOtr.productoId = pr.id AND pp.paqueteId != p.id
            LEFT JOIN ventaprogreso voOtrPq ON voOtrPq.tipoItemVenta = 1 AND voOtrPq.id = ppOtr.productoId
            LEFT JOIN ventaProgreso voPr ON voPr.tipoItemVenta = 0 AND voPr.id = pr.id
            LEFT JOIN ventaDetalle vd ON vd.tipoItemVenta = 1 AND vd.id = p.id
        WHERE p.id = :id
        GROUP BY p.id, pp.productoId, pp.nUnidades, pp.porcentajeDescuento, pr.nombre, pr.precioUnitario, pr.nUnidades, pr.estatus, cantidadEnCarrito
    """
    )
    fun leerPaquete(id: Int): Cursor

    @Query("DELETE FROM paquetesProductos WHERE paqueteId = :id")
    fun internalEliminarPaqueteProductosPorPaquete(id: Int)

    @Query("DELETE FROM paquetes WHERE id = :id")
    fun eliminarPaquete(id: Int)

    @Query("SELECT * FROM paquetesProductos WHERE paqueteId = :paqueteId")
    fun listarPaquetesProductosRaw(paqueteId: Int): List<PaqueteProducto>

    @Transaction
    fun actualizarPaquete(id: Int, productos: List<PaqueteProducto>) {
        internalEliminarPaqueteProductosPorPaquete(id)
        agregarProductos(productos)
    }
}
