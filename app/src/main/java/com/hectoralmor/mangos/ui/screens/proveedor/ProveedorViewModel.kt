package com.hectoralmor.mangos.ui.screens.proveedor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.hectoralmor.mangos.MangosApp
import com.hectoralmor.mangos.data.repository.ProveedorRepository
import com.hectoralmor.mangos.domain.model.Proveedor
import com.hectoralmor.mangos.domain.model.repository.ProveedorRepository as IProveedorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProveedorViewModel(
    private val proveedorRepository: IProveedorRepository
) : ViewModel() {

    private val _proveedorSeleccionado = MutableStateFlow<Proveedor?>(null)
    val proveedorSeleccionado: StateFlow<Proveedor?> = _proveedorSeleccionado.asStateFlow()

    val proveedores: StateFlow<List<Proveedor>> =
        proveedorRepository.obtenerTodos()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun obtenerProveedor(id: Int) {
        viewModelScope.launch {
            _proveedorSeleccionado.value = proveedorRepository.obtenerProveedorId(id)
        }
    }

    fun seleccionarProveedor(proveedor: Proveedor) {
        _proveedorSeleccionado.value = proveedor
    }

    fun limpiarSeleccion() {
        _proveedorSeleccionado.value = null
    }

    fun guardarProveedor(nombre: String, direccion: String) {
        viewModelScope.launch {
            proveedorRepository.guardar(Proveedor(nombre = nombre, direccion = direccion))
        }
    }

    fun actualizarProveedor(proveedor: Proveedor) {
        viewModelScope.launch { proveedorRepository.actualizar(proveedor) }
    }

    fun eliminarProveedor(proveedor: Proveedor) {
        viewModelScope.launch { proveedorRepository.eliminar(proveedor) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as MangosApp
                ProveedorViewModel(
                    proveedorRepository = ProveedorRepository(app.database.proveedorDao())
                )
            }
        }
    }
}