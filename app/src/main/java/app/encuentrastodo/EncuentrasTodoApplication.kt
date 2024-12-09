package app.encuentrastodo

import android.app.Application
import app.encuentrastodo.data.EncuentrasTodoDatabase
import app.encuentrastodo.data.repositorios.PaquetesRepositorio
import app.encuentrastodo.data.repositorios.ProductosRepositorio
import app.encuentrastodo.data.repositorios.ReportesRepositorio
import app.encuentrastodo.data.repositorios.VentasRepositorio

class EncuentrasTodoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        database = EncuentrasTodoDatabase.getInstance(this)
    }

    companion object {
        private lateinit var database: EncuentrasTodoDatabase

        val productosRepositorio by lazy {
            ProductosRepositorio(database)
        }

        val paquetesRepositorio by lazy {
            PaquetesRepositorio(database)
        }

        val ventasRepositorio by lazy {
            VentasRepositorio(database)
        }

        val reportesRepositorio by lazy {
            ReportesRepositorio(database)
        }
    }
}