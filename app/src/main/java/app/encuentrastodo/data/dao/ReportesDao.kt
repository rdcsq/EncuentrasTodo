package app.encuentrastodo.data.dao

import androidx.room.Dao
import androidx.room.Query
import app.encuentrastodo.data.models.Producto
import app.encuentrastodo.data.models.ReporteAcumulado
import app.encuentrastodo.data.models.ReportePaquetes3Productos10Unidades
import app.encuentrastodo.data.models.ReporteProductoGanancia
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface ReportesDao {
    @Query(
        """
        SELECT p.id AS id,
               p.nombre AS nombre, 
               p.precioUnitario AS precioUnitario,
               p.nUnidades AS nUnidades,
               p.estatus AS estatus
        FROM productos p
            LEFT JOIN paquetesProductos pp ON pp.productoId = p.id
        WHERE NOT EXISTS (
            SELECT 1 FROM ventaDetalle vd WHERE vd.id IN (pp.productoId, p.id)
        ) AND pp.productoId IS NULL
    """
    )
    fun productosSinPaqueteNiVentas(): Flow<List<Producto>>

    @Query(
        """
        SELECT pa.id AS paqueteId,
               count(pp.productoId) AS cantidadProductos,
               sum(pp.nUnidades) AS cantidadUnidades
        FROM paquetes pa
            JOIN paquetesProductos pp ON pa.id = pp.paqueteId
        GROUP BY pa.id
        HAVING cantidadProductos >= 3 AND cantidadUnidades >= 10
    """
    )
    fun paquetes3Productos10Unidades(): Flow<List<ReportePaquetes3Productos10Unidades>>

    @Query(
        """
        SELECT p.id AS productoId,
               coalesce(sum(vd.unidades) * p.precioUnitario, 0) AS gananciaIndividual,
               coalesce(pp.nUnidades * vd.unidades * p.precioUnitario * (1 - (pp.porcentajeDescuento / 100.0)), 0) AS gananciaPaquete
        FROM productos p
            LEFT JOIN ventaDetalle vd ON vd.tipoItemVenta = 0 AND vd.id = p.id
            LEFT JOIN paquetesProductos pp ON pp.productoId = p.id
            LEFT JOIN ventaDetalle vd2 ON vd2.tipoItemVenta = 1 AND vd2.id = pp.paqueteId
        GROUP BY p.id, p.nombre, p.estatus, p.precioUnitario
    """
    )
    fun reporteVentaProducto(): Flow<List<ReporteProductoGanancia>>

    @Query(
        """
        SELECT p.id AS productoId, 
               v.fecha AS fecha,
               sum(coalesce(pp.nUnidades * vd.unidades, vd.unidades)) AS unidades,
                sum(coalesce(pp.nUnidades * vd.unidades * p.precioUnitario * (1 - (pp.porcentajeDescuento / 100.0)), p.precioUnitario * vd.unidades)) AS importe
        FROM venta v
            JOIN ventaDetalle vd ON v.folio = vd.folio
            LEFT JOIN paquetesProductos pp ON vd.tipoItemVenta = 1 AND pp.paqueteId = vd.id
            LEFT JOIN productos p ON (vd.tipoItemVenta = 0 AND vd.id = p.id) OR (pp.productoId = p.id)
        WHERE v.fecha BETWEEN :inicio AND :fin
        GROUP BY p.id, v.fecha
        ORDER BY p.id
    """
    )
    fun acumulado(inicio: LocalDate, fin: LocalDate): Flow<List<ReporteAcumulado>>
}