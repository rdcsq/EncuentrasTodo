package app.encuentrastodo.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import app.encuentrastodo.data.enums.Estatus

@Entity("productos")
open class Producto(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val nombre: String,
    val precioUnitario: Double,
    val nUnidades: Int,
    val estatus: Estatus
)

class ProductoReadView(
    id: Int,
    nombre: String,
    precioUnitario: Double,
    nUnidades: Int,
    estatus: Estatus,
    val unidadesEnCarrito: Int = 0,
    val disponibles: Int = nUnidades,
    val tieneVentas: Boolean = false
) : Producto(id, nombre, precioUnitario, nUnidades, estatus)
