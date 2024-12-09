package app.encuentrastodo.data.repositorios

import app.encuentrastodo.data.EncuentrasTodoDatabase
import java.time.LocalDate

class ReportesRepositorio(private val database: EncuentrasTodoDatabase) {
    fun productosSinPaqueteNiVentas() = database.reportes().productosSinPaqueteNiVentas()

    fun paquetes3Productos10Unidades() = database.reportes().paquetes3Productos10Unidades()

    fun reporteVentaProducto() = database.reportes().reporteVentaProducto()

    fun acumulado(inicio: LocalDate, fin: LocalDate) = database.reportes().acumulado(inicio, fin)
}