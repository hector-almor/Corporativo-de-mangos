package com.hectoralmor.mangos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hectoralmor.mangos.data.Producto
import com.hectoralmor.mangos.data.ProductoDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductoViewModel(private val dao: ProductoDao) : ViewModel() {

    val productos: StateFlow<List<Producto>> = dao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun agregarProducto(nombre: String, precio: Double, descripcion: String) {
        viewModelScope.launch {
            dao.upsert(Producto(nombre = nombre, precio = precio, descripcion = descripcion))
        }
    }

    fun eliminarProducto(producto: Producto) {
        viewModelScope.launch {
            dao.delete(producto)
        }
    }
}