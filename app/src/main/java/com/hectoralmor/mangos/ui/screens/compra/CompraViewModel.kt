package com.hectoralmor.mangos.ui.screens.compra

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.hectoralmor.mangos.MangosApp
import com.hectoralmor.mangos.data.repository.CompraRepository
import com.hectoralmor.mangos.data.repository.ProveedorRepository
import com.hectoralmor.mangos.domain.model.Compra
import com.hectoralmor.mangos.domain.model.Proveedor
import com.hectoralmor.mangos.domain.model.repository.CompraRepository as ICompraRepository
import com.hectoralmor.mangos.domain.model.repository.ProveedorRepository as IProveedorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class CompraViewModel(
    private val compraRepository: ICompraRepository,
    private val proveedorRepository: IProveedorRepository
) : ViewModel() {

    private val _compraSeleccionada = MutableStateFlow<Compra?>(null)
    val compraSeleccionada: StateFlow<Compra?> = _compraSeleccionada.asStateFlow()

    val compras: StateFlow<List<Compra>> =
        compraRepository
            .obtenerTodas()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    val comprasDeHoy: StateFlow<List<Compra>> =
        compraRepository
            .obtenerPorFecha(LocalDate.now().toString())
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    val proveedores: StateFlow<List<Proveedor>> =
        proveedorRepository
            .obtenerTodos()
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

    fun guardarCompra(proveedorId: Int, nombreProveedor: String, cantidad: Double, precio: Double) {
        viewModelScope.launch {
            compraRepository.guardar(
                Compra(
                    proveedorId = proveedorId,
                    nombre = nombreProveedor,
                    cantidad = cantidad,
                    precio = precio,
                    fecha = LocalDate.now().toString()
                )
            )
        }
    }

    fun eliminarCompra(compra: Compra) {
        viewModelScope.launch { compraRepository.eliminar(compra) }
    }

    fun editarCompra(compra: Compra) {
        viewModelScope.launch {
            compraRepository.actualizar(compra.copy(total = compra.cantidad * compra.precio))
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