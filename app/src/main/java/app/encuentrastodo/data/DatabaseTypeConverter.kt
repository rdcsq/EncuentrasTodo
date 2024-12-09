package app.encuentrastodo.data

import androidx.room.TypeConverter
import app.encuentrastodo.data.enums.Estatus
import app.encuentrastodo.data.enums.TipoItemVenta
import java.time.LocalDate

class DatabaseTypeConverter {
    @TypeConverter
    fun toEstatus(value: String) = Estatus.fromString(value)

    @TypeConverter
    fun fromEstatus(value: Estatus): String = value.estatus.toString()

    @TypeConverter
    fun toTipoItemVenta(value: Int) = enumValues<TipoItemVenta>()[value]

    @TypeConverter
    fun fromTipoItemVenta(value: TipoItemVenta) = value.id

    @TypeConverter
    fun toLocalDate(value: String) = LocalDate.parse(value)

    @TypeConverter
    fun fromLocalDate(value: LocalDate) = value.toString()
}