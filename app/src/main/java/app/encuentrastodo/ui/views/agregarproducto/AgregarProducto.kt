package app.encuentrastodo.ui.views.agregarproducto

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import app.encuentrastodo.R
import app.encuentrastodo.data.TestingData
import app.encuentrastodo.data.enums.Estatus
import app.encuentrastodo.data.models.Producto
import app.encuentrastodo.ui.components.ComboBoxActivoInactivo
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarProducto(
    idInicial: Int? = null,
    viewModel: AgregarProductoViewModel = AgregarProductoViewModel(),
    regresar: () -> Unit,
) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var precioUnitario by rememberSaveable { mutableStateOf("") }
    var cantidadUnidades by rememberSaveable { mutableStateOf("") }
    var estatusProductoId by rememberSaveable { mutableIntStateOf(Estatus.ACTIVO.toInt()) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val scroll = rememberScrollState()
    val context = LocalContext.current

    LaunchedEffect(idInicial) {
        if (idInicial == null) return@LaunchedEffect
        viewModel.buscar(idInicial) {
            if (it == null) {
                Toast.makeText(context, "Producto no existe", Toast.LENGTH_LONG).show()
                regresar()
                return@buscar
            }

            nombre = it.nombre
            precioUnitario = it.precioUnitario.toString()
            cantidadUnidades = it.nUnidades.toString()
            estatusProductoId = it.estatus.toInt()
        }
    }

    fun limpiar() {
        nombre = ""
        precioUnitario = ""
        cantidadUnidades = ""
    }

    fun guardar() {
        val intNUnidades = cantidadUnidades.toIntOrNull()
        val doublePrecioUnitario = precioUnitario.toDoubleOrNull()
        if (intNUnidades == null || doublePrecioUnitario == null || intNUnidades < 0 || doublePrecioUnitario < 0) {
            scope.launch {
                snackbarHostState.showSnackbar("Verifique los datos ingresados")
            }
            return
        }

        val producto = Producto(
            id = idInicial,
            nombre = nombre,
            estatus = enumValues<Estatus>()[estatusProductoId],
            nUnidades = intNUnidades,
            precioUnitario = doublePrecioUnitario
        )
        if (idInicial != null) {
            viewModel.update(producto)
        } else {
            viewModel.agregar(producto)
            limpiar()
        }
    }

    fun generarAleatorio() {
        nombre = TestingData.nombresDeProducto.random()
        precioUnitario = Random.nextInt(200).toString()
        cantidadUnidades = Random.nextInt(50).toString()
        guardar()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (idInicial == null) "Agregar producto" else "Editar producto"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { regresar() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                actions = {
                    if (idInicial == null) {
                        IconButton(onClick = ::generarAleatorio) {
                            Icon(painterResource(R.drawable.ic_random), null)
                        }
                        IconButton(onClick = ::limpiar) {
                            Icon(Icons.Filled.Delete, null)
                        }
                    }
                    IconButton(onClick = ::guardar) {
                        Icon(Icons.Filled.Done, null)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .verticalScroll(scroll),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (idInicial != null) {
                OutlinedTextField(
                    enabled = false,
                    value = idInicial.toString(),
                    onValueChange = {},
                    label = { Text("ID") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = precioUnitario,
                onValueChange = {
                    if (it.count { c -> c == '.' } > 1) return@OutlinedTextField
                    val precio = it.filter { c -> c.isDigit() || c == '.' }
                    val idxDecimal = precio.indexOf('.')
                    if (idxDecimal != -1 && precio.length - idxDecimal > 3) return@OutlinedTextField
                    precioUnitario = precio
                },
                label = { Text("Precio unitario") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
            )

            OutlinedTextField(
                value = cantidadUnidades,
                onValueChange = {
                    if (!it.isDigitsOnly()) return@OutlinedTextField
                    cantidadUnidades = it
                },
                label = { Text("Número de unidades") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
            )

            if (idInicial != null) {
                ComboBoxActivoInactivo(
                    estatus = estatusProductoId,
                    setEstatus = { estado -> estatusProductoId = estado }
                )
            }
        }
    }
}