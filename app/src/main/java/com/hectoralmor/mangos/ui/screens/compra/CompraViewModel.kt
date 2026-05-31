package com.hectoralmor.mangos.ui.screens.compra

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.hectoralmor.mangos.MangosApp
import com.hectoralmor.mangos.data.CompraConProveedor
import com.hectoralmor.mangos.data.entity.CompraEntity
import com.hectoralmor.mangos.data.entity.ProveedorEntity
import com.hectoralmor.mangos.data.repository.CompraRepository
import com.hectoralmor.mangos.data.repository.ProveedorRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.collections.emptyList
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.Double

class CompraViewModel(
    private val compraRepository: CompraRepository,
    private val proveedorRepository: ProveedorRepository
) : ViewModel() {


    private val _compraSeleccionada = MutableStateFlow<CompraConProveedor?>(null)
    val proveedorSeleccionado: StateFlow<CompraConProveedor?> = _compraSeleccionada.asStateFlow()

    val comprasDeHoy: StateFlow<List<CompraConProveedor>> =
        compraRepository
            .obtenerPorFecha(LocalDate.now().toString())
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    val compras: StateFlow<List<CompraConProveedor>> =
        compraRepository
            .obtenerTodas()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun obtenerCompraId(id: Long) {
        viewModelScope.launch {
            _compraSeleccionada.value = compraRepository.obtenerCompraId(id)
        }
    }
    val proveedores: StateFlow<List<ProveedorEntity>> =
        proveedorRepository
            .obtenerTodos()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun guardarCompra(proveedorId: Int, cantidad: Double, precio: Double) {
        viewModelScope.launch {
            compraRepository.guardar(
                CompraEntity(
                    proveedorId = proveedorId,
                    cantidad = cantidad,
                    precio = precio,
                    fecha = LocalDate.now().toString(),
                    total = cantidad * precio
                )
            )
        }
    }

    fun eliminarCompra(compra: CompraEntity) {
        viewModelScope.launch { compraRepository.eliminar(compra) }
    }

    fun editarCompra(compra: CompraEntity){
        viewModelScope.launch {
            compraRepository.actualizar(
                CompraEntity(
                    proveedorId = compra.proveedorId,
                    cantidad = compra.cantidad,
                    precio = compra.precio,
                    fecha = compra.fecha,
                    total = compra.cantidad * compra.precio
                )
            )
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as MangosApp
                CompraViewModel(
                    compraRepository = CompraRepository(app.database.compraDao()),
                    proveedorRepository = ProveedorRepository(app.database.proveedorDao())
                )
            }
        }
    }
}