package com.hectoralmor.mangos.domain.model.repository

import com.hectoralmor.mangos.domain.model.Compra
import kotlinx.coroutines.flow.Flow

interface CompraRepository {
    fun obtenerTodas(): Flow<List<Compra>>
    fun obtenerPorFecha(fecha: String): Flow<List<Compra>>
    suspend fun obtenerCompraId(id: Long): Compra
    suspend fun guardar(compra: Compra)
    suspend fun actualizar(compra: Compra)
    suspend fun eliminar(compra: Compra)
}