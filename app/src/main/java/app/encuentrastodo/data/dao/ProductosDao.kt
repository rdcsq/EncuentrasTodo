package app.encuentrastodo.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import app.encuentrastodo.data.models.Producto
import app.encuentrastodo.data.models.ProductoReadView
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductosDao {
    @Query(
        """
        SELECT p.id,
               p.nUnidades,
               p.precioUnitario,
               p.estatus,
               p.nombre,
               coalesce(vp.unidades, 0) AS unidadesEnCarrito,
               p.nUnidades - coalesce(sum(vp2.unidades * pp.nUnidades), 0) AS disponibles,
               max(iif(vd.id IS NOT NULL, 1, 0)) AS tieneVentas
        FROM productos p
            LEFT JOIN ventaProgreso vp ON p.id = vp.id AND vp.tipoItemVenta = 0
            LEFT JOIN paquetesProductos pp ON pp.productoId = p.id
            LEFT JOIN ventaProgreso vp2 ON vp2.tipoItemVenta = 1 AND vp2.id = pp.paqueteId
            LEFT JOIN ventaDetalle vd ON (vd.tipoItemVenta = 0 AND vd.id = p.id) OR (vd.tipoItemVenta = 1 AND vd.id = pp.paqueteId)
        WHERE p.id = :id
        GROUP BY p.id, p.nUnidades, p.precioUnitario, p.estatus, p.nombre, unidadesEnCarrito
    """
    )
    fun select(id: Int): Flow<ProductoReadView?>

    @Query(
        """
        SELECT p.id,
               p.nUnidades,
               p.precioUnitario,
               p.estatus,
               p.nombre,
               coalesce(vp.unidades, 0) AS unidadesEnCarrito,
               p.nUnidades - coalesce(sum(vp2.unidades * pp.nUnidades), 0) AS disponibles,
               max(iif(vd.id IS NOT NULL, 1, 0)) AS tieneVentas
        FROM productos p 
            LEFT JOIN ventaProgreso vp ON p.id = vp.id AND vp.tipoItemVenta = 0
            LEFT JOIN paquetesProductos pp ON pp.productoId = p.id
            LEFT JOIN ventaProgreso vp2 ON vp2.tipoItemVenta = 1 AND vp2.id = pp.paqueteId
            LEFT JOIN ventaDetalle vd ON (vd.tipoItemVenta = 0 AND vd.id = p.id) OR (vd.tipoItemVenta = 1 AND vd.id = pp.paqueteId)
        WHERE :incluirInactivos = 1 OR estatus = 'A'
        GROUP BY p.id, p.nUnidades, p.precioUnitario, p.estatus, p.nombre, unidadesEnCarrito
    """
    )
    fun listarPaginado(incluirInactivos: Boolean): PagingSource<Int, ProductoReadView>

    @Insert
    fun insertar(producto: Producto)

    @Update
    fun update(producto: Producto)

    @Query("UPDATE productos SET estatus = 'B' WHERE id = :id")
    fun marcarComoInactivo(id: Int)
}