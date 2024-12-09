package app.encuentrastodo.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import app.encuentrastodo.ui.views.agregarproducto.AgregarProducto
import app.encuentrastodo.ui.views.crearpaquete.CrearPaquete
import app.encuentrastodo.ui.views.inicio.Inicio
import app.encuentrastodo.ui.views.listarpaquetes.ListarPaquetes
import app.encuentrastodo.ui.views.listarproductos.ListarProductos
import app.encuentrastodo.ui.views.listarventas.ListarVentas
import app.encuentrastodo.ui.views.reportes.ReportePaquetes3Productos10UnidadesView
import app.encuentrastodo.ui.views.reportes.ReporteProductosNoPaquetesNoVentasView
import app.encuentrastodo.ui.views.reportes.ReporteVentaProductoView
import app.encuentrastodo.ui.views.reportes.acumulado.ReporteAcumuladoView
import app.encuentrastodo.ui.views.verpaquete.VerPaquete
import app.encuentrastodo.ui.views.verventa.VentaActual
import kotlinx.serialization.Serializable

@Serializable
data class VerPaqueteParams(val id: Int)

@Serializable
data class VerProductoParams(val id: Int)

@Serializable
data class CrearEditarPaquete(val id: Int? = null)

@Serializable
data class CrearLeerVenta(val folio: Int? = null)

@Composable
fun Navigation() {
    val navController = rememberNavController()

    fun regresar() = navController.navigateUp()

    NavHost(navController, startDestination = "/") {
        composable("/") {
            Inicio(
                listarProductos = { navController.navigate("/listar-productos") },
                verProducto = { navController.navigate(VerProductoParams(it)) },
                crearProducto = { navController.navigate("/crear-producto") },
                listarPaquetes = { navController.navigate("/listar-paquetes") },
                verPaquete = { navController.navigate(VerPaqueteParams(it)) },
                crearPaquete = { navController.navigate(CrearEditarPaquete()) },
                abrirVentaActual = { navController.navigate(CrearLeerVenta()) },
                abrirVenta = { navController.navigate(CrearLeerVenta(it)) },
                listarVentas = { navController.navigate("/listar-ventas") },
                abrirReporte1 = { navController.navigate("/reporte1") },
                abrirReporte2 = { navController.navigate("/reporte2") },
                abrirReporte3 = { navController.navigate("/reporte3") },
                abrirReporte4 = { navController.navigate("/reporte4") },
            )
        }
        composable("/listar-productos") {
            ListarProductos(
                regresar = ::regresar,
                editarProducto = { navController.navigate(VerProductoParams(it.id!!)) }
            )
        }
        composable("/crear-producto") {
            AgregarProducto(
                regresar = ::regresar
            )
        }
        composable<CrearEditarPaquete> {
            val params = it.toRoute<CrearEditarPaquete>()
            CrearPaquete(
                editarPaqueteId = params.id,
                regresar = ::regresar
            )
        }
        composable("/listar-paquetes") {
            ListarPaquetes(
                regresar = ::regresar,
                verPaquete = { navController.navigate(VerPaqueteParams(it)) }
            )
        }
        composable<VerPaqueteParams> {
            val params = it.toRoute<VerPaqueteParams>()
            VerPaquete(
                id = params.id,
                regresar = ::regresar,
                editarPaquete = { pq -> navController.navigate(CrearEditarPaquete(pq)) }
            )
        }
        composable<CrearLeerVenta> {
            val params = it.toRoute<CrearLeerVenta>()
            VentaActual(
                folio = params.folio,
                regresar = ::regresar
            )
        }
        composable<VerProductoParams> {
            val params = it.toRoute<VerProductoParams>()
            AgregarProducto(
                idInicial = params.id,
                regresar = ::regresar
            )
        }
        composable("/listar-ventas") {
            ListarVentas(
                regresar = ::regresar,
                verVenta = { navController.navigate(CrearLeerVenta(it)) }
            )
        }
        composable("/reporte1") {
            ReporteProductosNoPaquetesNoVentasView(
                regresar = ::regresar,
            )
        }
        composable("/reporte2") {
            ReportePaquetes3Productos10UnidadesView(
                regresar = ::regresar,
                verPaquete = { navController.navigate(VerPaqueteParams(it)) }
            )
        }
        composable("/reporte3") {
            ReporteVentaProductoView(
                regresar = ::regresar
            )
        }
        composable("/reporte4") {
            ReporteAcumuladoView(
                regresar = ::regresar
            )
        }
    }
}