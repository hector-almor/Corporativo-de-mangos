package com.hectoralmor.mangos.ui.screens.principal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.hectoralmor.mangos.data.CompraConProveedor
import com.hectoralmor.mangos.data.entity.CompraEntity
import com.hectoralmor.mangos.data.repository.CompraRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.hectoralmor.mangos.MangosApp
import java.time.LocalDate

class PrincipalViewModel(
    private val compraRepository: CompraRepository
) : ViewModel() {

    val compras: StateFlow<List<CompraConProveedor>> =
        compraRepository.obtenerTodas()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )


    val comprasDeHoy: StateFlow<List<CompraConProveedor>> =
        compraRepository
            .obtenerPorFecha(LocalDate.now().toString())
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as MangosApp
                PrincipalViewModel(
                    compraRepository = CompraRepository(app.database.compraDao())
                )
            }
        }
    }
}