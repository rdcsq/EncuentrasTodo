package app.encuentrastodo.data.enums

enum class Estatus(val estatus: Char) {
    ACTIVO('A'),
    INACTIVO('B');

    fun toInt(): Int = when (this) {
        ACTIVO -> 0
        INACTIVO -> 1
    }

    companion object {
        fun fromString(estatus: String): Estatus {
            return when (estatus) {
                "A" -> ACTIVO
                "B" -> INACTIVO
                else -> throw IllegalArgumentException("Valor inválido")
            }
        }
    }
}