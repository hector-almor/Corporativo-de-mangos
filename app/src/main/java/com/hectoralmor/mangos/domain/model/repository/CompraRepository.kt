package com.hectoralmor.mangos.domain.model.repository

import com.hectoralmor.mangos.domain.model.Compra
import com.hectoralmor.mangos.data.entity.CompraEntity
import kotlinx.coroutines.flow.Flow


interface CompraRepository {
    fun obtenerPorFecha(fecha: String): Flow<List<Compra>>
    suspend fun guardar(compra: CompraEntity)
    suspend fun eliminar(compra: CompraEntity)
    suspend fun actualizar(compra: CompraEntity)
}