package app.encuentrastodo.data.models

import java.time.LocalDate

data class ReportePaquetes3Productos10Unidades(
    val paqueteId: Int,
    val cantidadProductos: Int,
    val cantidadUnidades: Int
)

data class ReporteProductoGanancia(
    val productoId: Int,
    val gananciaIndividual: Double,
    val gananciaPaquete: Double
)

data class ReporteAcumulado(
    val productoId: Int,
    val fecha: LocalDate,
    val unidades: Int,
    val importe: Double
)