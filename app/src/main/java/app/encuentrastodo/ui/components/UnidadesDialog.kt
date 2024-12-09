package app.encuentrastodo.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import app.encuentrastodo.R
import app.encuentrastodo.data.toMoneyString

@Composable
fun UnidadesDialog(
    dismiss: () -> Unit,
    completar: (Int) -> Unit,
    maxUnidades: Int,
    precioUnitario: Double,
    unidadesIniciales: Int
) {
    var unidades by remember { mutableIntStateOf(unidadesIniciales) }

    AlertDialog(
        title = { Text("Agregar a carrito") },
        confirmButton = {
            TextButton(
                onClick = {
                    completar(unidades)
                    dismiss()
                },
            ) {
                Text("Completar")
            }
        },
        dismissButton = {
            TextButton(onClick = { dismiss() }) { Text("Cancelar") }
        },
        onDismissRequest = { dismiss() },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            if (unidades == 0) return@IconButton
                            unidades -= 1
                        },
                        enabled = unidades > 0
                    ) {
                        Icon(painterResource(R.drawable.ic_remove), null)
                    }
                    Text(
                        unidades.toString(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(
                        onClick = {
                            if (unidades == maxUnidades) return@IconButton
                            unidades += 1
                        },
                        enabled = unidades < maxUnidades
                    ) {
                        Icon(Icons.Filled.Add, null)
                    }
                }
                Text("Precio unitario: $${precioUnitario.toMoneyString()}")
                Text("Precio total: $${(precioUnitario * unidades).toMoneyString()}")
            }
        }
    )
}