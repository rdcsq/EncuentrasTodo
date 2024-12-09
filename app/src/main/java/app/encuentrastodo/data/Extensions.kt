package app.encuentrastodo.data

import android.icu.text.DecimalFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

private val moneyFormatter = DecimalFormat("#,###,##0.00")

fun Double.toMoneyString(): String = moneyFormatter.format(this)
fun Long.toLocalDate(): LocalDate =
    Instant.ofEpochMilli(this).atOffset(ZoneOffset.UTC).toLocalDate()