package app.encuentrastodo.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import app.encuentrastodo.data.enums.Estatus
import java.time.LocalDate

data class ListadoPaquete(
    val id: Int,
    val cantidad: Int,
    val cantidadProductosInactivos: Int
)

@Entity("paquetes")
data class Paquete(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val fecha: LocalDate = LocalDate.now()
)

@Entity(
    "paquetesProductos",
    foreignKeys = [
        ForeignKey(
            entity = Producto::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("productoId")
        ),
        ForeignKey(
            entity = Paquete::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("paqueteId"),
            onDelete = ForeignKey.CASCADE
        )
    ],
    primaryKeys = ["paqueteId", "productoId"],
    indices = [
        Index("paqueteId"),
        Index("productoId")
    ]
)
data class PaqueteProducto(
    var paqueteId: Int = 0,
    val productoId: Int,
    var nUnidades: Int = 0,
    var porcentajeDescuento: Int = 0,
) {
    @Ignore
    var producto: ProductoReadView? = null

    fun esValido() = nUnidades != 0

    fun calcularPrecioOriginal(): Double {
        if (producto == null) return 0.0
        return nUnidades * producto!!.precioUnitario
    }

    fun calcularPrecioDescontado(): Double {
        if (producto == null) return 0.0
        return nUnidades * producto!!.precioUnitario * (1.0 - (porcentajeDescuento / 100.0))
    }
}

data class PaqueteDetalles(
    val id: Int,
    val productos: List<PaqueteProducto>,
    val unidadesEnCarrito: Int = 0,
    val tieneVentas: Boolean = false,
) {
    val precioOriginal: Double
    val precioDescontado: Double
    val paquetesDisponibles: Int
    val puedeVenderse = productos.all { it.producto!!.estatus == Estatus.ACTIVO }

    init {
        var precioOriginal = 0.0
        var precioDescontado = 0.0
        var maxUnidades = productos.first().let { it.producto!!.disponibles / it.nUnidades }
        productos.forEach {
            precioOriginal += it.calcularPrecioOriginal()
            precioDescontado += it.calcularPrecioDescontado()
            (it.producto!!.disponibles / it.nUnidades).let { pMaxUnidades ->
                if (pMaxUnidades < maxUnidades) {
                    maxUnidades = pMaxUnidades
                }
            }
        }
        this.precioOriginal = precioOriginal
        this.precioDescontado = precioDescontado
        this.paquetesDisponibles = maxUnidades
    }
}