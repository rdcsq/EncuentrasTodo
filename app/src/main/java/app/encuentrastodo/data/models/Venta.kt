package app.encuentrastodo.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import app.encuentrastodo.data.enums.TipoItemVenta
import java.time.LocalDate

@Entity("venta")
data class Venta(
    @PrimaryKey(autoGenerate = true)
    val folio: Int? = null,
    val fecha: LocalDate = LocalDate.now()
)

@Entity(
    "ventaDetalle", primaryKeys = ["folio", "tipoItemVenta", "id"], foreignKeys = [
        ForeignKey(
            entity = Venta::class,
            parentColumns = ["folio"],
            childColumns = ["folio"]
        )
    ]
)
data class VentaDetalle(
    val folio: Int,
    val tipoItemVenta: TipoItemVenta,
    val id: Int,
    val unidades: Int,
    val precio: Double
)

@Entity(
    "ventaProgreso",
    primaryKeys = ["tipoItemVenta", "id"]
)
data class VentaProgreso(
    val tipoItemVenta: TipoItemVenta,
    val id: Int,
    val unidades: Int,
)

data class VentaReadView(
    val folio: Int? = null,
    val tipoItemVenta: TipoItemVenta,
    val id: Int,
    val nombre: String,
    val unidades: Int,
    val precioUnitario: Double,
    val precioDescontado: Double? = null,
)