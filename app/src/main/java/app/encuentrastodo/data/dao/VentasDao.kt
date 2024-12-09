package app.encuentrastodo.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import app.encuentrastodo.data.enums.TipoItemVenta
import app.encuentrastodo.data.models.Venta
import app.encuentrastodo.data.models.VentaProgreso
import app.encuentrastodo.data.models.VentaReadView
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface VentasDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertarCarrito(detalle: VentaProgreso)

    @Query("DELETE FROM ventaProgreso WHERE tipoItemVenta = :tipoItemVenta AND id = :id")
    fun eliminarCarrito(tipoItemVenta: TipoItemVenta, id: Int)

    @Query(
        """
        SELECT vp.tipoItemVenta,
               vp.id,
               coalesce(pr.nombre, 'Paquete ' || vp.id) AS nombre,
               vp.unidades,
               coalesce(sum(pprp.precioUnitario * ppr.nUnidades), pr.precioUnitario * vp.unidades) AS precioUnitario,
               sum(pprp.precioUnitario * ppr.nUnidades * vp.unidades * (1 - (ppr.porcentajeDescuento / 100.0))) AS precioDescontado
            FROM ventaprogreso vp
                LEFT JOIN productos pr ON vp.tipoItemVenta = 0 AND pr.id = vp.id
                LEFT JOIN paquetesProductos ppr ON vp.tipoItemVenta = 1 AND vp.id = ppr.paqueteId
                LEFT JOIN productos pprp ON ppr.productoId = pprp.id
            GROUP BY vp.tipoItemVenta, vp.id
    """
    )
    fun listarCarrito(): Flow<List<VentaReadView>>

    @Query("INSERT INTO venta(fecha) VALUES (:fecha)")
    fun internalCrearVenta(fecha: LocalDate = LocalDate.now()): Long

    @Query(
        """
        INSERT INTO ventaDetalle(folio, tipoItemVenta, id, unidades, precio)
        SELECT :folio AS folio,
               vp.tipoItemVenta AS tipoItemVenta,
               vp.id AS id,
               vp.unidades AS unidades,
               coalesce(sum(p.precioUnitario * pap.nUnidades * vp.unidades * (1 - (pap.porcentajeDescuento / 100.0))), p.precioUnitario * vp.unidades) AS precio
        FROM ventaProgreso vp
            LEFT JOIN paquetes pa ON vp.tipoItemVenta = 1 AND vp.id = pa.id
            LEFT JOIN paquetesProductos pap ON pa.id = pap.paqueteId
            LEFT JOIN productos p ON (vp.tipoItemVenta = 0 AND vp.id = p.id) OR (pap.productoId = p.id)
        GROUP BY vp.id, vp.tipoItemVenta
    """
    )
    fun internalMoverCarritoAVentaConFolio(folio: Long)

    @Query(
        """
        UPDATE productos
        SET nUnidades = nUnidades - (SELECT unidades FROM ventaProgreso WHERE tipoItemVenta = 0 AND id = productos.id)
        WHERE EXISTS (SELECT 1 FROM ventaProgreso WHERE tipoItemVenta = 0 AND id = productos.id)
    """
    )
    fun internalActualizarUnidadesProductos()

    @Query(
        """
        UPDATE productos
        SET nUnidades = nUnidades - (
            SELECT pp.nUnidades * vp.unidades FROM ventaProgreso vp
                JOIN paquetesProductos pp ON vp.id = pp.paqueteId
            WHERE vp.tipoItemVenta = 1 AND pp.productoId = productos.id
        )
        WHERE EXISTS (
            SELECT 1 FROM ventaProgreso vp
                JOIN paquetesProductos pp ON vp.id = pp.paqueteId 
            WHERE vp.tipoItemVenta = 1 AND pp.productoId = productos.id
        )
    """
    )
    fun internalActualizarUnidadesPaquetes()

    @Query("DELETE FROM ventaprogreso")
    fun internalLimpiarVentaProgreso()

    @Transaction
    fun finalizarCarrito() {
        val folio = internalCrearVenta()
        internalMoverCarritoAVentaConFolio(folio)
        internalActualizarUnidadesProductos()
        internalActualizarUnidadesPaquetes()
        internalLimpiarVentaProgreso()
    }

    @Query("SELECT * FROM venta")
    fun listarVentas(): PagingSource<Int, Venta>

    @Query(
        """
        SELECT vp.tipoItemVenta,
               vp.id,
               coalesce(pr.nombre, 'Paquete ' || vp.id) AS nombre,
               vp.unidades,
               vp.precio AS precioUnitario
            FROM ventaDetalle vp
                LEFT JOIN productos pr ON vp.tipoItemVenta = 0 AND pr.id = vp.id
            WHERE vp.folio = :folio
    """
    )
    fun listarVentaDetalle(folio: Int): Flow<List<VentaReadView>>
}