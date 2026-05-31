package com.hectoralmor.mangos.ui.screens.proveedor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.hectoralmor.mangos.MangosApp
import com.hectoralmor.mangos.data.entity.CompraEntity
import com.hectoralmor.mangos.data.entity.ProveedorEntity
import com.hectoralmor.mangos.data.repository.ProveedorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class ProveedorViewModel(
    private val proveedorRepository: ProveedorRepository
): ViewModel() {
    private val _proveedorSeleccionado = MutableStateFlow<ProveedorEntity?>(null)
    val proveedorSeleccionado: StateFlow<ProveedorEntity?> = _proveedorSeleccionado.asStateFlow()


    val proveedores: StateFlow<List<ProveedorEntity>> =
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

    fun guardarProveedor(proveedor: ProveedorEntity){
        viewModelScope.launch {
            proveedorRepository.guardar(
                ProveedorEntity(
                    nombre = proveedor.nombre,
                    direccion = proveedor.direccion
                )
            )
        }
    }

    fun actualizarProveedor(proveedor: ProveedorEntity){
        viewModelScope.launch {
            proveedorRepository.actualizar(
                ProveedorEntity(
                    nombre = proveedor.nombre,
                    direccion = proveedor.direccion
                )
            )
        }
    }

    fun eliminarProveedor(proveedor: ProveedorEntity){
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