package com.hectoralmor.mangos.ui.screens.principal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.hectoralmor.mangos.MangosApp
import com.hectoralmor.mangos.data.repository.CompraRepository
import com.hectoralmor.mangos.domain.model.Compra
import com.hectoralmor.mangos.domain.model.repository.CompraRepository as ICompraRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

class PrincipalViewModel(
    private val compraRepository: ICompraRepository
) : ViewModel() {

    val compras: StateFlow<List<Compra>> =
        compraRepository.obtenerTodas()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    val comprasDeHoy: StateFlow<List<Compra>> =
        compraRepository.obtenerPorFecha(LocalDate.now().toString())
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