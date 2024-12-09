package app.encuentrastodo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import app.encuentrastodo.data.dao.PaquetesDao
import app.encuentrastodo.data.dao.ProductosDao
import app.encuentrastodo.data.dao.ReportesDao
import app.encuentrastodo.data.dao.VentasDao
import app.encuentrastodo.data.models.Paquete
import app.encuentrastodo.data.models.PaqueteProducto
import app.encuentrastodo.data.models.Producto
import app.encuentrastodo.data.models.Venta
import app.encuentrastodo.data.models.VentaDetalle
import app.encuentrastodo.data.models.VentaProgreso

@Database(
    entities = [Producto::class, Paquete::class, PaqueteProducto::class, Venta::class, VentaDetalle::class, VentaProgreso::class],
    version = 1,
)
@TypeConverters(DatabaseTypeConverter::class)
abstract class EncuentrasTodoDatabase : RoomDatabase() {
    abstract fun productos(): ProductosDao
    abstract fun paquetes(): PaquetesDao
    abstract fun ventas(): VentasDao
    abstract fun reportes(): ReportesDao

    companion object {
        private var INSTANCE: EncuentrasTodoDatabase? = null

        fun getInstance(context: Context): EncuentrasTodoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    EncuentrasTodoDatabase::class.java,
                    "encuentras-todo"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}