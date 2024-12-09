package app.encuentrastodo.ui.views.listarpaquetes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import app.encuentrastodo.EncuentrasTodoApplication
import app.encuentrastodo.data.repositorios.PaquetesRepositorio

class ListarPaquetesViewModel(
    paquetesRepositorio: PaquetesRepositorio = EncuentrasTodoApplication.paquetesRepositorio
) : ViewModel() {
    val paquetes = paquetesRepositorio.listarPaquetes().cachedIn(viewModelScope)
}